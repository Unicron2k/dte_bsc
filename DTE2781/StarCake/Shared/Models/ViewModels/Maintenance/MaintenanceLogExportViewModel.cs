using System;

namespace StarCake.Shared.Models.ViewModels.Maintenance
{
    public class MaintenanceLogExportViewModel
    {
        public DateTime Date { get; set; }
        public long ACCSeconds { get; set; }
        public string TaskDescription { get; set; }
        public string ActionDescription { get; set; }
        public string ApplicationUser { get; set; }
        public string MaintainedItemName { get; set; }
        public string MaintainedItemSerialNumber { get; set; }

    }
}