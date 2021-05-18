using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;


namespace StarCake.Shared.Models.ViewModels.Administrate
{
    public class EntityViewModelFrontend
    {
        public int EntityId { get; set; }
        public string Name { get; set; }
        public int EntityTypeId { get; set; }
        public DateTime CreationDate { get; set; } = DateTime.Now;
        public int TotalFlightCycles { get; set; }
        public int TotalFlightDurationInSeconds { get; set; }
        
        public virtual ICollection<ComponentViewModelAdministrate> Components { get; set; }
    }

    public class ComponentViewModelAdministrate
    {
        public int EntityId { get; set; }
        public int ComponentId { get; set; }
        public string Name { get; set; }
        public int ComponentTypeId { get; set; }
        public int ManufacturerId { get; set; }
        public DateTime CreationDate { get; set; }
        public long TotalFlightCycles { get; set; }
        public long TotalFlightDurationInSeconds { get; set; }
        public string SerialNumber { get; set; }
        public int CyclesSinceLastMaint { get; set; } = 0;
        public int FlightSecondsSinceLastMaint { get; set; } = 0;
        public DateTime LatestMaintenanceDate { get; set; }
        public int MaxCyclesBtwMaint { get; set; }
        public int MaxDaysBtwMaint { get; set; }
        public int MaxFlightSecondsBtwMaint { get; set; }
        
        public string FlightDurationInHMM(int seconds)
        {
            TimeSpan t = TimeSpan.FromSeconds(seconds);
            return $"{t.Hours:D2}h:{t.Minutes:D2}m";
        }

    }
}