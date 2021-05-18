using System;
using System.Linq;
using System.Collections.Generic;
using System.Text;
using Microsoft.AspNetCore.Mvc;
using StarCake.Server.Data;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared;

namespace StarCake.Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ExportDataAPIController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IExportDataRepository _repository;
        
        private const string ContentTypeXlsx = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        private const string ContentTypeCsv = "text/csv";
        private const string ContentTypePdf = "application/pdf";
        private const string ContentTypeJson = "application/json";

        public ExportDataAPIController(IExportDataRepository repository, ApplicationDbContext context)
        {
            _repository = repository;
            _context = context;
        }
        
        // GET: api/ExportDataAPI/File/?
        [HttpGet("FlightLogs/")]
        public IActionResult GetFlightLogsAsFile(
            [FromQuery] string base64FlightLogIds,
            [FromQuery] string base64ApplicationUserId,
            [FromQuery] string base64DepartmentId,
            [FromQuery] string base64FileType)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            
            var flightLogIds = Coding.Base64.FromBase64<List<int>>(base64FlightLogIds);
            var applicationUserId = Coding.Base64.FromBase64<Guid>(base64ApplicationUserId).ToString();
            var departmentId  = Coding.Base64.FromBase64<int>(base64DepartmentId);
            var fileType  = Coding.Base64.FromBase64<string>(base64FileType);
            
            byte[] fileBytes = { };
            var contentType = "";
            var fileExtension = "";
            
            switch (fileType)
            {
                case FileTypeEnums.FlightLogGenerator.FileExcel:
                    var excelFilePackage = _repository.GetFlightLogsAsExcelPackage(flightLogIds, applicationUserId);
                    using (var package = excelFilePackage)
                    {
                        fileBytes = package.GetAsByteArray();
                    }
                    contentType = ContentTypeXlsx;
                    fileExtension = ".xlsx";
                    break;
                case FileTypeEnums.FlightLogGenerator.FileCsv:
                    var stringCsv = _repository.GetFlightLogsAsCsv(flightLogIds);
                    fileBytes = Encoding.UTF8.GetBytes(stringCsv);
                    contentType = ContentTypeCsv;
                    fileExtension = ".csv";
                    break;
                case FileTypeEnums.FlightLogGenerator.FileJson:
                    var stringJson = _repository.GetFlightLogsAsJson(flightLogIds);
                    fileBytes = Encoding.UTF8.GetBytes(stringJson);
                    contentType = ContentTypeJson;
                    fileExtension = ".csv";
                    break;
                
                case FileTypeEnums.FlightLogGenerator.FilePdf:
                    fileBytes = _repository.GetFlightLogsAsPdf(flightLogIds, applicationUserId, departmentId);
                    contentType = ContentTypePdf;
                    fileExtension = ".pdf";
                    break;
            }
            
            if (!fileBytes.Any())
                return NotFound();
            return File(fileBytes, contentType, $"FlightLogs_generated-{DateTime.Now:yyyy-MM-dd}{fileExtension}");
        }
    }
}