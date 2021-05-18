using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace StarCake.Shared.Models
{
    /// <summary>
    /// Contains common attributes between EntityViewModel.cs and ComponentViewModel.cs
    /// </summary>
    public class MasterMaintenanceViewModel
    {
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Name { get; set; }
        [Required]
        public DateTime CreationDate { get; set; }
        
        [Required]
        public int DepartmentId { get; set; }
        
        [Required]
        public int ManufacturerId { get; set; }

        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string SerialNumber { get; set; }
        
        
        [DefaultValue(0)]
        public int TotalFlightCycles { get; set; }
        [DefaultValue(0)]
        public int TotalFlightDurationInSeconds { get; set; }
        [Required]
        public int CyclesSinceLastMaintenance { get; set; }
        [Required]
        public int FlightSecondsSinceLastMaintenance { get; set; }
        [Required]
        public DateTime LastMaintenanceDate { get; set; }
        [Required]
        [DefaultValue(100)]
        public int MaxCyclesBtwMaintenance { get; set; }
        [Required]
        [DefaultValue(30)]
        public int MaxDaysBtwMaintenance { get; set; }
        [Required]
        [DefaultValue(86400)]//24hours
        public int MaxFlightSecondsBtwMaintenance { get; set; }
        
        public bool RequiresMaintenance { get; set; }
        
        public bool RequiresImmediateMaintenance  { get; set; }
    }
}