using System;
using System.Collections;
using System.Collections.Generic;
using System.Security.Principal;
using System.Text;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels.ExportData;
using OfficeOpenXml;
using OfficeOpenXml.Table;
using StarCake.Shared.Models.ViewModels;

//using PdfSharp.Pdf;

namespace StarCake.Server.Models.Interfaces
{
    public interface IExportDataRepository
    {
        string GetFlightLogsAsCsv(List<int> flightLogIds);
        string GetFlightLogsAsJson(List<int> flightLogIds);
        ExcelPackage GetFlightLogsAsExcelPackage(List<int> flightLogIds, string applicationUserIdExtracted);
        byte[] GetFlightLogsAsPdf(List<int> flightLogIds, string applicationUserIdExtracted, int departmentId);
    }
}