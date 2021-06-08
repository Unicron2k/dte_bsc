using System;
using System.Collections.Generic;
using System.Text;
using Newtonsoft.Json;
using System.Linq;
using System.Web;
using System.Collections.Specialized;
using System.ComponentModel;
using System.Globalization;
using System.IO;


namespace StarCake.Shared
{
    public static class Utils
    {
            /// <summary>
            /// @Author: aes014@uit.no
            /// Helper methods for many builtin functions in C#
            /// </summary>
            public static class BuiltIns
            {
                public static bool IsAllStringNullOrEmpty(params string[] strings)
                {
                    return strings.All(string.IsNullOrEmpty);
                }
                
                public static bool IsAnyStringNullOrEmpty(params string[] strings)
                {
                    return strings.Any(string.IsNullOrEmpty);
                }
            }
    }
    

    
    /// <summary>
    /// @Author: aes014@uit.no
    /// Helper methods for validation, mostly in AppendFlightLog.razor
    /// </summary>
    public static class Validation
    {
        public static class Coordinates
        {
            private static bool StandardCoordinateValidation(decimal coordinate, int minLenBeforePrecision, int maxLenBeforePrecision, out string errorMessage,  int maxLenAfterPrecision = 6)
            {
                // Check if bigger than Int32 max
                if (coordinate > int.MaxValue)
                {
                    errorMessage = "Coordinate is too large";
                    return false;
                }

                if (coordinate==0)
                {
                    errorMessage = "Coordinate cannot be 0";
                    return false;
                }
                    
                // Check if contain decimal point ( . )
                if (!coordinate.ToString(CultureInfo.InvariantCulture).Contains(".", StringComparison.OrdinalIgnoreCase))
                {
                    errorMessage = "Coordinate did not contain any precision";
                    return false;
                }
                
                // Check if length after ( . ) less than or equal to 6
                var asString = coordinate.ToString(CultureInfo.InvariantCulture);
                var lenAfterPrecision =  asString[(asString.IndexOf(".", StringComparison.Ordinal)+1)..].Length;
                if (lenAfterPrecision > maxLenAfterPrecision)
                {
                    errorMessage = "Maximum precision after . is 6";
                    return false;
                }
                
                // Check if length before precision is allowed
                var abs = Math.Abs(coordinate);
                var numberOfDigitsBeforePrecision =  abs < 1 ? 0 : (int)(Math.Log10(decimal.ToDouble(abs)) + 1);
                if (!Numbers.IsIntBetween(numberOfDigitsBeforePrecision, minLenBeforePrecision, maxLenBeforePrecision))
                {
                    errorMessage = $"Length before . bust be between {minLenBeforePrecision} and {maxLenAfterPrecision}";
                    return false;
                }
                
                errorMessage = "";
                return true;
            }
            
            public static bool IsLatitudeValid(decimal? latitude, out string errorMessage)
            {
                // Return false if it is null
                if (latitude == null)
                {
                    errorMessage = "Latitude was null";
                    return false;
                }

                if (!StandardCoordinateValidation(latitude.Value, 1, 2, out var errorMessageStandard))
                {
                    errorMessage = errorMessageStandard;
                    return false;
                }
                
                // No errors, it is a Latitude
                errorMessage = "No errors";
                return true;
            }
            
            public static bool IsLongitudeValid(decimal? longitude, out string errorMessage)
            {
                // Return false if it is null
                if (longitude == null)
                {
                    errorMessage = "Latitude was null";
                    return false;
                }
                
                if (!StandardCoordinateValidation(longitude.Value, 1, 3, out var errorMessageStandard))
                {
                    errorMessage = errorMessageStandard;
                    return false;
                }
                
                // No errors, it is a Latitude
                errorMessage = "No errors";
                return true;
            }
        }
        
        public static class Numbers
        {
            public static bool IsIntBetween(int n, int min, int max)
            {
                return n >= min && n <= max;
            }
        }
    }
    
    public static class Enums
    {
        public static string GetEnumDescription(Enum value)
        {
            var fi = value.GetType().GetField(value.ToString());
            if (fi.GetCustomAttributes(typeof(DescriptionAttribute), false) is DescriptionAttribute[] attributes && attributes.Any())
                return attributes.First().Description;
            return value.ToString();
        }
    }
    
    public static class Coding
    {
        public static class Base64
        {
            public static string ToBase64(object obj)
            {
                var json = JsonConvert.SerializeObject(obj);
                var bytes = Encoding.Default.GetBytes(json);
                return Convert.ToBase64String(bytes);
            }
            
            public static T FromBase64<T>(string base64Text)
            {
                var bytes = Convert.FromBase64String(base64Text);
                var json = Encoding.Default.GetString(bytes);
                return JsonConvert.DeserializeObject<T>(json);
            }
        }
        public static class Query
        {
            public static string ToQueryString(NameValueCollection nvc)
            {
                var array = (
                    from key in nvc.AllKeys
                    from value in nvc.GetValues(key)
                    select $"{HttpUtility.UrlEncode(key)}={HttpUtility.UrlEncode(value)}"
                ).ToArray();
                return "?" + string.Join("&", array);
            }
        }
    }

    public static class TimeFormatting
    {
        public static string DateTimeToYYMMDD(DateTime dateTime)
        {
            return dateTime.ToString("yyMMdd");
        }
        
        
        public static string DateToHHMM(DateTime dateTime) {
            return $"{dateTime:HH}:{dateTime:mm}";
        }
        
        public static string SecondsToHHMM(long seconds) {
            var t = TimeSpan.FromSeconds(seconds);
            return $"{t.Hours:D2}h:{t.Minutes:D2}m";
        }
        
        public static string SecondsToHMM(long seconds)
        {
            var hours = seconds / 3600;
            var minutes = (seconds % 3600) / 60;
            var test = $"{hours:D2}h:{minutes:D2}m";
            return test;
        }
        
    }

    public static class FileTypeEnums
    {
        public static class FlightLogGenerator
        {
            public const string FileExcel = "Excel";
            public const string FileCsv = "Csv";
            public const string FilePdf = "Pdf";
            public const string FileJson = "Json";
        }
        public static class FileUploads
        {
            //private const string Root = "Files/";
            public class FileServerInfo
            {
                public string RelativeServerPath { get; set; }
                public string[] AllowedExtensions { get; set; }
            }
            public const string ConstImage = "Images";
            public const string ConstDocument = "Documents";

            public static string[] ConstToAllowedExtensions(string fileUploadsConstType)
            {
                return FileServerInfoList().FirstOrDefault(x => x.RelativeServerPath.Contains(fileUploadsConstType))?.AllowedExtensions;
            }
            
            private static IEnumerable<FileServerInfo> FileServerInfoList()
            {
                return new List<FileServerInfo>
                {
                    new FileServerInfo {RelativeServerPath = $"{ConstImage}", AllowedExtensions = new[] {".jpg", ".png", ".jpeg"}},
                    new FileServerInfo {RelativeServerPath = $"{ConstDocument}", AllowedExtensions = new[] {".pdf", ".xlsm", ".xlsx", ".docx"}},
                };
            }
            public static string GetRelativeFilePath(string fileName)
            {
                foreach (var fsi in FileServerInfoList())
                    foreach (var str in fsi.AllowedExtensions)
                        if (Path.GetExtension(fileName.ToUpper()).Equals(str.ToUpper()))
                            return fsi.RelativeServerPath;
                return null;
            }

            private static string ExtensionInFile(string fileName)
            {
                return Path.GetExtension(fileName.ToUpper());
            }
        }
    }
    
    public static class CalendarStringEnums
    {
        public static class Month
        {
            public static readonly string[] Default = {
                "January", "February",
                "March", "April",
                "May", "June",
                "July", "August",
                "September", "October",
                "November", "December",
            };
            public static readonly string[] Shortened = {
                "Jan.", "Feb.",
                "Mar.", "Apr.",
                "May", "June",
                "July", "Aug.",
                "Sept.", "Oct.",
                "Nov.", "Dec."
            };
        }
        
        public static class Day
        {
            public static readonly string[] Default = {
                "Monday", "Tuesday",
                "Wednesday", "Thursday",
                "Friday", "Saturday",
                "Sunday"
            };
            public static readonly string[] Shortened = {
                "Mon", "Tue",
                "Wed", "Thu",
                "Fri", "Sat",
                "Sun"
            };
        }
    }
}