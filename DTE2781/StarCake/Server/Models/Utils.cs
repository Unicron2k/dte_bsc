using System;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Linq;

namespace StarCake.Server.Models
{
    /// <summary>
    /// @Author aes014@uit.no
    /// </summary>
    public static class Utils
    {
        public static class File
        {
            public static class ImageEncoding
            {
                public static class ToBase64
                {
                    public static class Compression
                    {
                        public static string ImageToBase64(string globalFilePath, int jpegQuality = 75)
                        {
                            using var image = Image.FromFile(globalFilePath);
                            var imageBytes = SaveImageToByteArray(image, jpegQuality);
            
                            // Convert byte[] to Base64 String
                            return Convert.ToBase64String(imageBytes);
                        }

                        private static byte[] SaveImageToByteArray(Image image, int jpegQuality = 90)
                        {
                            using var ms = new MemoryStream();
                            var jpegEncoder = GetEncoder(ImageFormat.Jpeg);
                            var encoderParameters = new EncoderParameters(1)
                            {
                                Param = {[0] = new EncoderParameter(Encoder.Quality, jpegQuality)}
                            };
                            image.Save(ms, jpegEncoder, encoderParameters);
                            return ms.ToArray();
                        }
                
                        private static ImageCodecInfo GetEncoder(ImageFormat format)
                        {
                            var codecs = ImageCodecInfo.GetImageDecoders();
                            return codecs.FirstOrDefault(codec => codec.FormatID == format.Guid);
                        }
                    }

                    public static class Raw
                    {
                        public static string ImageToBase64(string globalFilePath)
                        {
                            using var image = Image.FromFile(globalFilePath);
                            
                            using var memoryStream = new MemoryStream();
                            image.Save(memoryStream, image.RawFormat);
                            var imageBytes = memoryStream.ToArray();

                            // Convert byte[] to Base64 String
                            return Convert.ToBase64String(imageBytes);
                        }
                    }
                }
            }
        }
    }
}