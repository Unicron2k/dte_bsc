using System;
using System.Linq;
using System.Collections.Generic;
using System.IO;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;
using iTextSharp.text;
using iTextSharp.text.pdf;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using OfficeOpenXml;
using OfficeOpenXml.Table;
using ServiceStack;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared;
using StarCake.Shared.Models.ViewModels.Maintenance;

namespace StarCake.Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ExportMaintenanceLogsController : ControllerBase
    {
        private readonly IMaintenanceLogRepository _maintenanceLogRepository;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IHttpContextAccessor _httpContextAccessor;
        private readonly IDepartmentRepository _departmentRepository;
        private ApplicationUser _user;

        private const string ContentTypeXlsx = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        private const string ContentTypeCsv = "text/csv";
        private const string ContentTypePdf = "application/pdf";
        private const string ContentTypeJson = "application/json";

        public ExportMaintenanceLogsController(IMaintenanceLogRepository maintenanceLogRepository,
            UserManager<ApplicationUser> userManager, IHttpContextAccessor httpContextAccessor, IDepartmentRepository departmentRepository)
        {
            _maintenanceLogRepository = maintenanceLogRepository;
            _userManager = userManager;
            _httpContextAccessor = httpContextAccessor;
            _departmentRepository = departmentRepository;
        }

        // GET: api/ExportDataAPI/File/?
        /// <summary>
        /// Export the maintencelog
        /// </summary>
        /// <param name="base64MaintenanceLogIds">List of all mainteancelogId to export</param>
        /// <param name="base64FileType">const string of type file to export</param>
        /// <returns></returns>
        [HttpGet]
        public async Task<IActionResult> GetFlightLogsAsFile([FromQuery] string base64MaintenanceLogIds, [FromQuery] string base64FileType)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            
            var userId = _httpContextAccessor.HttpContext.User.FindFirst(ClaimTypes.NameIdentifier).Value;
            _user = await _userManager.FindByIdAsync(userId);
            
            var flightLogIds = Coding.Base64.FromBase64<List<int>>(base64MaintenanceLogIds);
            var fileType  = Coding.Base64.FromBase64<string>(base64FileType);
            
            byte[] fileBytes = { };
            var contentType = "";
            var fileExtension = "";
            
            switch (fileType)
            {
                case FileTypeEnums.FlightLogGenerator.FileExcel:
                    var excelFilePackage = await GetMaintenanceLogsAsExcelPackage(flightLogIds);
                    using (var package = excelFilePackage)
                    {
                        fileBytes = package.GetAsByteArray();
                    }
                    contentType = ContentTypeXlsx;
                    fileExtension = ".xlsx";
                    break;
                case FileTypeEnums.FlightLogGenerator.FileCsv:
                    var stringCsv = await GetMaintenanceLogsAsCsv(flightLogIds);
                    fileBytes = Encoding.UTF8.GetBytes(stringCsv);
                    contentType = ContentTypeCsv;
                    fileExtension = ".csv";
                    break;
                case FileTypeEnums.FlightLogGenerator.FileJson:
                    var stringJson = await GetMaintenanceLogsAsJson(flightLogIds);
                    fileBytes = Encoding.UTF8.GetBytes(stringJson);
                    contentType = ContentTypeJson;
                    fileExtension = ".json";
                    break;
                
                case FileTypeEnums.FlightLogGenerator.FilePdf:
                    fileBytes = await GetMaintenanceLogsAsPdf(flightLogIds);
                    contentType = ContentTypePdf;
                    fileExtension = ".pdf";
                    break;
            }
            
            if (!fileBytes.Any())
                return NotFound();
            return File(fileBytes, contentType, $"MaintenanceLogs_generated-{DateTime.Now:yyyy-MM-dd}{fileExtension}");
        }
        
        private async Task<List<MaintenanceLogExportViewModel>> GetMaintenanceLogsFromIds(ICollection<int> maintenanceLogIds)
        {
            return (await _maintenanceLogRepository.GetAllInDepartment(_user.CurrentLoggedInDepartmentId))
                    .Where(x => maintenanceLogIds.Contains(x.MaintenanceLogId))
                    .Select(ToExportModel)
                    .ToList();
        }
        private async Task<string> GetMaintenanceLogsAsCsv(ICollection<int> flightLogIds)
        {
            var builder = new StringBuilder()
                .AppendLine("Date,User,Item,SerialNumber,ACC,Task,Corrective Action");
            (await GetMaintenanceLogsFromIds(flightLogIds))
                .ForEach(delegate(MaintenanceLogExportViewModel m)
                {
                    builder.AppendLine(
                        $"\"{m.Date.ToString($"yyyy/MM/dd HH:mm")}\"," +
                        $"\"{m.ApplicationUser}\"," + 
                        $"\"{m.MaintainedItemName}\"," +
                        $"\"{m.MaintainedItemSerialNumber}\"," +
                        $"\"{m.ACCSeconds}\"," +
                        $"\"{m.TaskDescription}\"," +
                        $"\"{m.ActionDescription}\""
                    );
                });
            return builder.ToString();
        }
        
        private async Task<string> GetMaintenanceLogsAsJson(ICollection<int> flightLogIds)
        {
            return (await GetMaintenanceLogsFromIds(flightLogIds)).ToSafeJson();
        }
        
        private async Task<ExcelPackage> GetMaintenanceLogsAsExcelPackage(ICollection<int> flightLogIds)
        {
            var maintenanceLogs = await GetMaintenanceLogsFromIds(flightLogIds);

            // Make a new Excel Package
            var package = new ExcelPackage();
            package.Workbook.Properties.Title = $"Maintenance-Report generated at {DateTime.Now:yyyy-M-d dddd}";
            package.Workbook.Properties.Author = "StarCake.Server";
            package.Workbook.Properties.Subject = "MaintenanceLog";
            package.Workbook.Properties.Keywords = "MaintenanceLog";

            var worksheetMaintenanceLogs = package.Workbook.Worksheets.Add("MaintenanceLogs");

            //.AppendLine("Date,User,Item,SerialNumber,ACC,Task,Corrective Action");
            // Add the headers
            worksheetMaintenanceLogs.Cells[1, 1].Value = "Date (yyyy.MM.dd HH:mm)";
            worksheetMaintenanceLogs.Cells[1, 2].Value = "User";
            worksheetMaintenanceLogs.Cells[1, 3].Value = "Item";
            worksheetMaintenanceLogs.Cells[1, 4].Value = "SerialNumber";
            worksheetMaintenanceLogs.Cells[1, 5].Value = "ACC";
            worksheetMaintenanceLogs.Cells[1, 6].Value = "Task";
            worksheetMaintenanceLogs.Cells[1, 7].Value = "Corrective Action";

            // Add some formats
            const string numberFormat = "#,##0";
            const string dataCellStyleName = "TableNumber";
            var numStyle = package.Workbook.Styles.CreateNamedStyle(dataCellStyleName);
            numStyle.Style.Numberformat.Format = numberFormat;

            // Populate headers with data
            var count = 1;
            foreach (var maintenanceLog in maintenanceLogs)
            {
                count++;
                //worksheetMaintenanceLogs.Cells[count, 1].Value = (count - 1).ToString();
                worksheetMaintenanceLogs.Cells[count, 1].Value = maintenanceLog.Date.ToString("yyyy/MM/dd HH:mm");
                worksheetMaintenanceLogs.Cells[count, 2].Value = maintenanceLog.ApplicationUser;
                worksheetMaintenanceLogs.Cells[count, 3].Value = maintenanceLog.MaintainedItemName;
                worksheetMaintenanceLogs.Cells[count, 4].Value = maintenanceLog.MaintainedItemSerialNumber;
                worksheetMaintenanceLogs.Cells[count, 5].Value = maintenanceLog.ACCSeconds;
                worksheetMaintenanceLogs.Cells[count, 6].Value = maintenanceLog.TaskDescription;
                worksheetMaintenanceLogs.Cells[count, 7].Value = maintenanceLog.ActionDescription;
            }

            const int numAttribsInMaintenanceLogs = 7;

            // Add to table / Add summary row
            var tbl = worksheetMaintenanceLogs.Tables.Add(new ExcelAddressBase( 1, 1,maintenanceLogs.Count+1,numAttribsInMaintenanceLogs), "Data");
            
            tbl.ShowHeader = true;
            tbl.TableStyle = TableStyles.Dark9;

            // AutoFitColumns
            worksheetMaintenanceLogs.Cells[1, 1, maintenanceLogs.Count+1, numAttribsInMaintenanceLogs].AutoFitColumns();

            // Add extra info worksheet
            var worksheetInfo = package.Workbook.Worksheets.Add("info");
            worksheetInfo.Cells[1, 1].Value = "File extraction date: ";
            worksheetInfo.Cells[1, 2].Value = DateTime.Now.ToString("yyyy/MM/dd HH:mm");
            worksheetInfo.Cells[2, 1].Value = "File extracted by user: ";
            worksheetInfo.Cells[2, 2].Value = $"{_user.LastName}, {_user.FirstName} ({_user.Email})";
            worksheetInfo.Cells[3, 1].Value = "Number of maintenance-logs extracted: ";
            worksheetInfo.Cells[3, 2].Value = maintenanceLogs.Count.ToString();
            worksheetInfo.Cells[1, 1, 3, 2].AutoFitColumns();

            // Return as ExcelPackage
            return package;
        }

        private async Task<byte[]> GetMaintenanceLogsAsPdf(ICollection<int> flightLogIds)
        {
            var maintenanceLogs = await GetMaintenanceLogsFromIds(flightLogIds);

            /* Creat a new Document */
            await using var ms = new MemoryStream();
            var landscape = true; //whether or not to use landscape-mode
            var document = new Document(landscape?PageSize.A4.Rotate():PageSize.A4, 25, 25, 30, 30);
            var pdfWriter = PdfWriter.GetInstance(document, ms);
            /* Define fonts */
            var baseFont = BaseFont.CreateFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            var titleFont = new Font(baseFont, 22, Font.NORMAL);
            var font = new Font(baseFont, 10, Font.NORMAL);


            /* START */
            document.Open();
            /* Add some metadata */
            document.AddCreator("StaCake.Server");
            document.AddAuthor($"{_user.FirstName} {_user.LastName}");
            document.AddHeader("MaintenanceLogs", "MaintenanceLogs");
            document.AddTitle("Maintenance-logs");
            document.AddSubject("Maintenance-logs");
            document.AddKeywords("MaintenanceLogs Maintenance Logs Log");
            document.AddCreationDate();

            /* Add centred title */
            var title = new Paragraph("Maintenance-logs\n\n", titleFont) {Alignment = 1};
            document.Add(title);
            
            /* Create a table */
            const int numAttribsInModel = 7; 
            var table = new PdfPTable(numAttribsInModel) {WidthPercentage = 100};
            table.SetWidths(new[] { 2f, 2f, 2f, 2f, 1f, 2f, 3f });
            /* Add table-header */
            table.AddCell(new Phrase(new Chunk("Date", font)));
            table.AddCell(new Phrase(new Chunk("User", font)));
            table.AddCell(new Phrase(new Chunk("Item", font)));
            table.AddCell(new Phrase(new Chunk("SerialNumber", font)));
            table.AddCell(new Phrase(new Chunk("ACC", font)));
            table.AddCell(new Phrase(new Chunk("Task", font)));
            table.AddCell(new Phrase(new Chunk("Corrective Action", font)));
            
            /* Add table-data */
            maintenanceLogs.ForEach(delegate(MaintenanceLogExportViewModel m)
            {
                table.AddCell(new Phrase(new Chunk($"{m.Date.ToString($"yyyy/MM/dd HH:mm")}", font)));
                table.AddCell(new Phrase(new Chunk($"{m.ApplicationUser}", font)));
                table.AddCell(new Phrase(new Chunk($"{m.MaintainedItemName}", font))); 
                table.AddCell(new Phrase(new Chunk($"{m.MaintainedItemSerialNumber}", font)));
                table.AddCell(new Phrase(new Chunk($"{m.ACCSeconds}", font)));
                table.AddCell(new Phrase(new Chunk($"{m.TaskDescription}", font))); 
                table.AddCell(new Phrase(new Chunk($"{m.ActionDescription}", font)));
            });
            document.Add(table);

            /* Add some info about the document */
            document.Add(new Paragraph(
                "\n\nInfo:" +
                $"File extraction date: {DateTime.Now:yyyy/MM/dd HH:mm}\n" +
                $"File extracted by user: {_user.FirstName} {_user.LastName}\n" +
                $"Number of maintenance-logs extracted: {maintenanceLogs.Count}\n"
            ));
            
            /* FIN */
            document.Close();
            return ms.ToArray();
        }
        
        private static MaintenanceLogExportViewModel ToExportModel(MaintenanceLog entity)
        {
            return new MaintenanceLogExportViewModel
            {
                Date = entity.Date,
                ACCSeconds = entity.ACCSeconds,
                TaskDescription = entity.TaskDescription,
                ActionDescription = entity.ActionDescription,
                ApplicationUser = entity.ApplicationUserLogged.FirstName + " " + entity.ApplicationUserLogged.LastName,
                MaintainedItemName = entity.ComponentId == null ? entity.Entity.Name : entity.Component.Name,
                MaintainedItemSerialNumber = entity.ComponentId == null ? entity.Entity.SerialNumber : entity.Component.SerialNumber
            };
        }
    }
}