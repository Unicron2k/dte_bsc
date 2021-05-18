using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;
using StarCake.Server.Models.Entity;

namespace StarCake.Server.Models
{
    /// <summary>
    /// Contains common attributes between Entity.cs and Component.cs
    /// </summary>
    public class MasterMaintenance
    {
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Name { get; set; }
        [Required]
        public DateTime CreationDate { get; set; }
        
        [Required]
        public int DepartmentId { get; set; }
        [ForeignKey("DepartmentId")] 
        [JsonIgnore]
        public virtual Department Department { get; set; }
        
        [Required]
        public int ManufacturerId { get; set; }
        [ForeignKey("ManufacturerId")] 
        [JsonIgnore]
        public virtual Manufacturer Manufacturer { get; set; }
        
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
    }
}