using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using iTextSharp.text;
using iTextSharp.text.pdf;
using StarCake.Server.Data;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels.ExportData;
using OfficeOpenXml;
using OfficeOpenXml.Table;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using StarCake.Shared;
using StarCake.Shared.Models.ViewModels;
using FlightLogViewModel = StarCake.Shared.Models.ViewModels.ExportData.FlightLogViewModel;

namespace StarCake.Server.Models.Repositories
{
    public class ExportDataRepository : IExportDataRepository
    {
        private ApplicationDbContext db;

        public ExportDataRepository(ApplicationDbContext db)
        {
            this.db = db;
        }
        

        private IEnumerable<Shared.Models.ViewModels.ExportData.FlightLogViewModel> GetFlightLogsFromIds(List<int> flightLogIds)
        {
            return new List<FlightLogViewModel>();
            //var flightLogViewModels = db.FlightLogs
            //    .Where(x => flightLogIds.Contains(x.FlightLogId))
            //    .Select(m => new Shared.Models.ViewModels.ExportData.FlightLogViewModel()
            //    {
            //        FlightLogId = m.FlightLogId,
            //        Date = m.Date,
            //        UserPilotedNameFormal = 
            //            db.ApplicationUsers
            //                .Where(x=>x.Id==m.ApplicationUserIdPiloted)
            //                .Select(x=>new string($"{x.LastName}, {x.FirstName}")).FirstOrDefault(),
            //        TypesOfOperationCommaSeparated = string.Join(", ", db.FlightLogTypeOfOperations
            //            .Where(x=>x.FlightLogId==m.FlightLogId)
            //            .OrderBy(x=>x.TypeOfOperation.Name)
            //            .Select(x=>x.TypeOfOperation.Name).ToList()),
            //        PlaceCityAddress = $"{m.City}/ {m.Address}",
            //        UserLoggedNameFormal = 
            //            db.ApplicationUsers
            //                .Where(x=>x.Id==m.ApplicationUserIdLogged)
            //                .Select(x=>new string($"{x.LastName}, {x.FirstName}")).FirstOrDefault(), 
            //        Address = m.Address,
            //        City = m.City,
            //        ApplicationUserLogged = db.ApplicationUsers
            //            .Where(x => x.Id == m.ApplicationUserIdLogged)
            //            .Select(userLogged => new ApplicationUserViewModel
            //            {
            //                FirstName = userLogged.FirstName,
            //                LastName = userLogged.LastName
            //            }).FirstOrDefault(),
            //        FlightDurationInSeconds = m.FlightDurationInSeconds,
            //        EntityId = m.EntityId,
            //        HasRemarks = (m.Remarks != null && !m.Remarks.Equals("")),
            //        EntityName = db.Entities
            //            .Where(x=>x.EntityId==m.EntityId)
            //            .Select(x=>x.Name).FirstOrDefault(),
            //        Remarks = m.Remarks
            //    }).ToList();
            //return flightLogViewModels;
        }
        
        public string GetFlightLogsAsCsv(List<int> flightLogIds)
        {
            var flightLogs = GetFlightLogsForFileExporting(flightLogIds).Result;
            var stringBuilder = new StringBuilder();
            stringBuilder.AppendLine(
                "ID,CountryCode,OrganizationName,DepartmentName," +
                "UserPilotedNameFormal,UserLoggedNameFormal," +
                "TypeOfOperations,EntityName,DateTakeOff," +
                "AddressTakeOff,CoordinatesTakeOff,SecondsFlown," +
                "AddressLanding,CoordinatesLanding,Remarks");
            foreach (var flightLog in flightLogs)
            {
                flightLog.Remarks ??= "";
                var stringArray = new[]
                {
                    flightLog.FlightLogId.ToString(),
                    
                    flightLog.CountryCode,
                    flightLog.OrganizationName,
                    flightLog.DepartmentName,
                    
                    flightLog.UserPiloted.GetNameFormal(),
                    flightLog.UserLogged.GetNameFormal(),
                    flightLog.TypeOfOperationsCommaSeparated(),
                    flightLog.EntityName,
                    flightLog.DateTakeOff.ToString("yyyy/MM/dd HH:mm"),
                    flightLog.AddressTakeOff,
                    flightLog.coordinatesTakeOffToString(),
                    TimeFormatting.SecondsToHHMM(flightLog.SecondsFlown),
                    flightLog.AddressLanding,
                    flightLog.coordinatesLandingToString(),
                    flightLog.Remarks
                };
                stringBuilder.AppendLine(string.Join(", ", stringArray.Select(x=>$"\"{x.ToString()}\"").ToArray()));
            }
            return stringBuilder.ToString();
        }

        public ExcelPackage GetFlightLogsAsExcelPackage(List<int> flightLogIds, string applicationUserIdExtracted)
        {
            var flightLogs = GetFlightLogsForFileExporting(flightLogIds).Result;

            // Make a new Excel Package
            var package = new ExcelPackage();
            package.Workbook.Properties.Title = $"FlightLog-Report generated at {DateTime.Now:yyyy-M-d dddd}";
            package.Workbook.Properties.Author = "StarCake server";
            package.Workbook.Properties.Subject = "FlightLog";
            package.Workbook.Properties.Keywords = "FlightLogs";

            var worksheetFlightLogs = package.Workbook.Worksheets.Add("FlightLogs");

            // Add the headers
            worksheetFlightLogs.Cells[1, 1].Value = "ID";
            worksheetFlightLogs.Cells[1, 2].Value = "CountryCode";
            worksheetFlightLogs.Cells[1, 3].Value = "OrganizationName";
            worksheetFlightLogs.Cells[1, 4].Value = "DepartmentName";
            worksheetFlightLogs.Cells[1, 5].Value = "UserPilotedNameForma";
            worksheetFlightLogs.Cells[1, 6].Value = "UserLoggedNameFormal";
            worksheetFlightLogs.Cells[1, 7].Value = "TypeOfOperations";
            worksheetFlightLogs.Cells[1, 8].Value = "EntityName";
            worksheetFlightLogs.Cells[1, 9].Value = "DateTakeOff (yyyy.MM.dd HH:mm)";
            worksheetFlightLogs.Cells[1, 10].Value = "AddressTakeOff";
            worksheetFlightLogs.Cells[1, 11].Value = "CoordinatesTakeOff";
            worksheetFlightLogs.Cells[1, 12].Value = "SecondsFlown";
            worksheetFlightLogs.Cells[1, 13].Value = "AddressLanding";
            worksheetFlightLogs.Cells[1, 14].Value = "CoordinatesLanding";
            worksheetFlightLogs.Cells[1, 15].Value = "Remarks";
            
            // Add some formats
            var numberformat = "#,##0";
            var dataCellStyleName = "TableNumber";
            var numStyle = package.Workbook.Styles.CreateNamedStyle(dataCellStyleName);
            numStyle.Style.Numberformat.Format = numberformat;

            // Populate headers with data
            var count = 1;
            foreach (var flightLog in flightLogs)
            {
                count++;
                //worksheetFlightLogs.Cells[count, 1].Value = (count - 1).ToString();
                worksheetFlightLogs.Cells[count, 1].Value = flightLog.FlightLogId.ToString();
                worksheetFlightLogs.Cells[count, 2].Value = flightLog.CountryCode;
                worksheetFlightLogs.Cells[count, 3].Value = flightLog.OrganizationName;
                worksheetFlightLogs.Cells[count, 4].Value = flightLog.DepartmentName;
                worksheetFlightLogs.Cells[count, 5].Value = flightLog.UserPiloted.GetNameFormal();
                worksheetFlightLogs.Cells[count, 6].Value = flightLog.UserLogged.GetNameFormal();
                worksheetFlightLogs.Cells[count, 7].Value = flightLog.TypeOfOperationsCommaSeparated();
                worksheetFlightLogs.Cells[count, 8].Value = flightLog.EntityName;
                worksheetFlightLogs.Cells[count, 9].Value = flightLog.DateTakeOff.ToString("yyyy/MM/dd HH:mm");
                worksheetFlightLogs.Cells[count, 10].Value = flightLog.AddressTakeOff;
                worksheetFlightLogs.Cells[count, 11].Value = flightLog.coordinatesTakeOffToString();
                worksheetFlightLogs.Cells[count, 12].Value = TimeFormatting.SecondsToHHMM(flightLog.SecondsFlown);
                worksheetFlightLogs.Cells[count, 13].Value = flightLog.AddressLanding;
                worksheetFlightLogs.Cells[count, 14].Value = flightLog.coordinatesLandingToString();
                worksheetFlightLogs.Cells[count, 15].Value = flightLog.Remarks;
            }

            var nAttributesInFlightLog = 15;

            // Add to table / Add summary row
            var tbl = worksheetFlightLogs.Tables.Add(new ExcelAddressBase(fromRow: 1, fromCol: 1, toRow: flightLogs.Count()+1, toColumn: nAttributesInFlightLog), "Data");
            tbl.ShowHeader = true;
            tbl.TableStyle = TableStyles.Dark9;

            // AutoFitColumns
            worksheetFlightLogs.Cells[1, 1, flightLogs.Count()+1, nAttributesInFlightLog].AutoFitColumns();

            // Add extra info worksheet
            var worksheetInfo = package.Workbook.Worksheets.Add("info");
            worksheetInfo.Cells[1, 1].Value = "File extraction date: ";
            worksheetInfo.Cells[1, 2].Value = DateTime.Now.ToString("yyyy/MM/dd HH:mm");
            worksheetInfo.Cells[2, 1].Value = "File extracted by user: ";
            worksheetInfo.Cells[2, 2].Value = db.ApplicationUsers
                .Where(x=>x.Id==applicationUserIdExtracted)
                .Select(x=>new string($"{x.LastName}, {x.FirstName} ({x.Email})")).FirstOrDefault();
            worksheetInfo.Cells[3, 1].Value = "Number of FlightLogs extracted: ";
            worksheetInfo.Cells[3, 2].Value = flightLogs.Count().ToString();
            worksheetInfo.Cells[1, 1, 3, 2].AutoFitColumns();

            // Return as ExcelPackage
            return package;
        }

        public string GetFlightLogsAsJson(List<int> flightLogIds)
        {
            var flightLogs = GetFlightLogsForFileExporting(flightLogIds);
            return JsonSerializer.Serialize(flightLogs);
        }

        // https://localhost:44345/api/ExportDataAPI/FlightLogs/Pdf?flightLogIds=WzEsMyw0LDUsNiw3LDgsOSwxMCwxMSwxMiwxMywxNCwxNSwxNiwyXQ%3d%3d&applicationUserIdExtracted=IjEwZTA1YTJiLTFlYjMtNDdjOS04OWQwLWIzMTk3N2JiNTdlNCI%3d
        public byte[] GetFlightLogsAsPdf(List<int> flightLogIds, string applicationUserIdExtracted, int departmentId)
        {
            var flightLogs = GetFlightLogsForFileExporting(flightLogIds).Result;
            var applicationUser = db.ApplicationUsers.FindAsync(applicationUserIdExtracted).Result;
            var department = db.Departments.FindAsync(departmentId).Result;
            var organization = db.Organizations.FindAsync(department.OrganizationId).Result;


            
            /* Creat a new Document */
            using var ms = new MemoryStream();
            var document = new Document(PageSize.A4.Rotate(), 25, 25, 30, 30);
            var pdfWriter = PdfWriter.GetInstance(document, ms);
            
            /* Define fonts */
            var baseFont = BaseFont.CreateFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            var titleFont = new Font(baseFont, 22, Font.NORMAL);
            var font = new Font(baseFont, 10, Font.NORMAL);


            /* START */
            document.Open();
            /* Add some metadata */
            document.AddCreator("StaCake.Server");
            document.AddAuthor($"{applicationUser.FirstName} {applicationUser.LastName}");
            document.AddHeader("FlightLogs", "FlightLogs");
            document.AddTitle("Flight-logs");
            document.AddSubject("Flight-logs");
            document.AddKeywords("FlightLogs Flight Logs Log");
            document.AddCreationDate();

            /* Add centred title */
            var title = new Paragraph("Flight-logs\n\n", titleFont) {Alignment = 1};
            document.Add(title);
            
            /* Create a table */
            const int numAttribsInModel = 15; 
            var table = new PdfPTable(numAttribsInModel) {WidthPercentage = 100};
            table.SetWidths(new[] { 1f, 1f, 2f, 2f, 1f, 2f, 3f, 2f, 2f, 2f, 2f, 1f, 2f, 3f, 3f });
            /* Add table-header */
            table.AddCell(new Phrase(new Chunk("ID", font)));
            table.AddCell(new Phrase(new Chunk("CountryCode", font)));
            table.AddCell(new Phrase(new Chunk("OrganizationName", font)));
            table.AddCell(new Phrase(new Chunk("DepartmentName", font)));
            table.AddCell(new Phrase(new Chunk("UserPilotedNameForma", font)));
            table.AddCell(new Phrase(new Chunk("UserLoggedNameFormal", font)));
            table.AddCell(new Phrase(new Chunk("TypeOfOperations", font)));
            table.AddCell(new Phrase(new Chunk("EntityName", font)));
            table.AddCell(new Phrase(new Chunk("DateTakeOff (yyyy.MM.dd HH:mm)", font)));
            table.AddCell(new Phrase(new Chunk("AddressTakeOff", font)));
            table.AddCell(new Phrase(new Chunk("CoordinatesTakeOff", font)));
            table.AddCell(new Phrase(new Chunk("SecondsFlown", font)));
            table.AddCell(new Phrase(new Chunk("AddressLanding", font)));
            table.AddCell(new Phrase(new Chunk("CoordinatesLanding", font)));
            table.AddCell(new Phrase(new Chunk("Remarks", font)));
            
            /* Add table-data */
            foreach (var flightLog in flightLogs)
            {
                table.AddCell(new Phrase(new Chunk(flightLog.FlightLogId.ToString(), font)));
                table.AddCell(new Phrase(new Chunk(flightLog.CountryCode, font)));
                table.AddCell(new Phrase(new Chunk(flightLog.OrganizationName, font)));
                table.AddCell(new Phrase(new Chunk(flightLog.DepartmentName, font)));
                table.AddCell(new Phrase(new Chunk(flightLog.UserPiloted.GetNameFormal(), font)));
                table.AddCell(new Phrase(new Chunk(flightLog.UserLogged.GetNameFormal(), font)));
                table.AddCell(new Phrase(new Chunk(flightLog.TypeOfOperationsCommaSeparated(), font)));
                table.AddCell(new Phrase(new Chunk(flightLog.EntityName, font)));
                table.AddCell(new Phrase(new Chunk(flightLog.DateTakeOff.ToString("yyyy/MM/dd HH:mm"), font)));
                table.AddCell(new Phrase(new Chunk(flightLog.AddressTakeOff, font)));
                table.AddCell(new Phrase(new Chunk(flightLog.coordinatesTakeOffToString(), font)));
                table.AddCell(new Phrase(new Chunk(TimeFormatting.SecondsToHHMM(flightLog.SecondsFlown), font)));
                table.AddCell(new Phrase(new Chunk(flightLog.AddressLanding, font)));
                table.AddCell(new Phrase(new Chunk(flightLog.coordinatesLandingToString(), font)));
                table.AddCell(new Phrase(new Chunk(flightLog.Remarks, font)));
            }
            document.Add(table);

            /* Add some info about the document */
            document.Add(new Paragraph(
                "\n\nInfo:" +
                $"File extraction date: {DateTime.Now:yyyy/MM/dd HH:mm}\n" +
                $"File extracted by user: {applicationUser.FirstName} {applicationUser.LastName}\n" +
                $"Number of flight-logs extracted: {flightLogs.Count}\n"
            ));
            
            /* FIN */
            document.Close();
            return ms.ToArray();

        }
        
        // Returns List sorted by the given flightLogIds
        private async Task<List<FlightLogViewModelForFile>> GetFlightLogsForFileExporting(List<int> flightLogIds)
        {
             var flightLogs = await db.FlightLogs
                .Where(x => flightLogIds.Contains(x.FlightLogId))
                //.OrderBy(x => flightLogIds.IndexOf(x.FlightLogId))
                .Select(f => new FlightLogViewModelForFile()
                {
                    CountryCode = db.Countries
                        .Where(x=>x.CountryId==f.CountryId)
                        .Select(x=>x.CountryCode).FirstOrDefault(),
                    OrganizationName = db.Organizations
                        .Where(x => x.OrganizationId == db.Departments
                            .Where(d => d.DepartmentId == f.DepartmentId)
                            .Select(d => d.OrganizationId).FirstOrDefault())
                        .Select(x => x.Name).FirstOrDefault(),
                    DepartmentName = db.Departments
                        .Where(x=>x.DepartmentId==f.DepartmentId)
                        .Select(x=>x.Name).FirstOrDefault(),
                    FlightLogId = f.FlightLogId,
                    ApplicationUserIdPiloted = f.ApplicationUserIdPiloted,
                    UserPiloted = db.ApplicationUsers
                        .Where(x => x.Id == f.ApplicationUserIdPiloted)
                        .Select(piloted => new UserInfo
                        {
                            FirstName = piloted.FirstName,
                            LastName = piloted.LastName
                        }).FirstOrDefault(),
                    ApplicationUserIdLogged = f.ApplicationUserIdLogged,
                    UserLogged = db.ApplicationUsers
                        .Where(x => x.Id == f.ApplicationUserIdPiloted)
                        .Select(logged => new UserInfo
                        {
                            FirstName = logged.FirstName,
                            LastName = logged.LastName
                        }).FirstOrDefault(),
                    TypeOfOperationViewModels = db.FlightLogTypeOfOperations
                        .Where(x => x.FlightLogId == f.FlightLogId)
                        .Select(z => new TypeOfOperationViewModel
                        {
                            TypeOfOperationId = z.TypeOfOperation.TypeOfOperationId,
                            Name = z.TypeOfOperation.Name,
                            IsActive = z.TypeOfOperation.IsActive
                        }).ToList(),
                    EntityId = f.EntityId,
                    EntityName = db.Entities.FirstOrDefault(x => x.EntityId == f.EntityId).Name,
                    CountryId = f.CountryId,
                    DateTakeOff = f.DateTakeOff,
                    AddressTakeOff = f.AddressTakeOff,
                    LatitudeTakeOff = f.LatitudeTakeOff,
                    LongitudeTakeOff = f.LongitudeTakeOff,
                    SecondsFlown = f.SecondsFlown,
                    AddressLanding = f.AddressLanding,
                    LatitudeLanding = f.LatitudeLanding,
                    LongitudeLanding = f.LongitudeLanding,
                    Remarks = f.Remarks
                }).ToListAsync();
            return flightLogs;
            
            
            
            
            //var flightLogs = db.FlightLogs
            //    .Where(x => flightLogIds.Contains(x.FlightLogId))
            //    //.OrderBy(x=>x.Date)
            //    .AsEnumerable()
            //    .OrderBy(x=>flightLogIds.IndexOf(x.FlightLogId))
            //    .Select(m => new FlightLogViewModelForFile
            //    {
            //        FlightLogId = m.FlightLogId,
            //        Address = m.Address,
            //        City = m.City,
            //        ZipCode = m.ZipCode,
            //        CountryCode = db.Countries
            //            .Where(x=>x.CountryId==m.CountryId)
            //            .Select(x=>x.CountryCode).FirstOrDefault(),
            //        UserPilotedNameFormal = 
            //            db.ApplicationUsers
            //            .Where(x=>x.Id==m.ApplicationUserIdPiloted)
            //            .Select(x=>new string($"{x.LastName}, {x.FirstName}")).FirstOrDefault(),
            //        UserLoggedNameFormal = 
            //            db.ApplicationUsers
            //                .Where(x=>x.Id==m.ApplicationUserIdLogged)
            //                .Select(x=>new string($"{x.LastName}, {x.FirstName}")).FirstOrDefault(), 
            //        DepartmentName = db.Departments
            //            .Where(x=>x.DepartmentId == m.DepartmentId)
            //            .Select(x=>x.Name).FirstOrDefault(),
            //        OrganizationName = db.Organizations
            //            .Where(x=>x.OrganizationId==db.Departments
            //                .Where(d=>d.DepartmentId==m.DepartmentId)
            //                .Select(d=>d.OrganizationId).FirstOrDefault())
            //            .Select(x=>x.Name).FirstOrDefault(),
            //        EntityName = db.Entities
            //            .Where(x=>x.EntityId==m.EntityId)
            //            .Select(x=>x.Name).FirstOrDefault(),
            //        Date = m.Date,
            //        FlightDurationInSeconds = m.FlightDurationInSeconds,
            //        Remarks = m.Remarks,
            //        Latitude = m.Latitude,
            //        Longitude = m.Longitude,
            //        TypesOfOperationCommaSeparated = string.Join(", ", db.FlightLogTypeOfOperations
            //            .Where(x=>x.FlightLogId==m.FlightLogId)
            //            .OrderBy(x=>x.TypeOfOperation.Name)
            //            .Select(x=>x.TypeOfOperation.Name).ToList())
            //    }).ToList();
            //return flightLogs;
        }
    }

    // https://localhost:44345/api/ExportDataAPI/FlightLogs/Pdf?flightLogIds=WzFd&applicationUserIdExtracted=IjEwZTA1YTJiLTFlYjMtNDdjOS04OWQwLWIzMTk3N2JiNTdlNCI%3d
}