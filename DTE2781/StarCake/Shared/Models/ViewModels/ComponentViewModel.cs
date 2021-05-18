using System.ComponentModel.DataAnnotations;

namespace StarCake.Shared.Models.ViewModels
{
    public class ComponentViewModel : MasterMaintenanceViewModel
    {
        public int ComponentId { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public int? EntityId { get; set; }
        [Required]
        public int ComponentTypeId { get; set; }

        public ComponentViewModel Clone()
        {
            return (ComponentViewModel)MemberwiseClone();
        }

        public void Update(ComponentViewModel updatedComponent)
        {
            ComponentId = updatedComponent.ComponentId;
            EntityId = updatedComponent.EntityId;
            ComponentTypeId = updatedComponent.ComponentTypeId;
            Name = updatedComponent.Name;
            CreationDate = updatedComponent.CreationDate;
            DepartmentId = updatedComponent.DepartmentId;
            ManufacturerId = updatedComponent.ManufacturerId;
            SerialNumber = updatedComponent.SerialNumber;
            TotalFlightCycles = updatedComponent.TotalFlightCycles;
            TotalFlightDurationInSeconds = updatedComponent.TotalFlightDurationInSeconds;
            CyclesSinceLastMaintenance = updatedComponent.CyclesSinceLastMaintenance;
            FlightSecondsSinceLastMaintenance = updatedComponent.FlightSecondsSinceLastMaintenance;
            LastMaintenanceDate = updatedComponent.LastMaintenanceDate;
            MaxCyclesBtwMaintenance = updatedComponent.MaxCyclesBtwMaintenance;
            MaxDaysBtwMaintenance = updatedComponent.MaxDaysBtwMaintenance;
            MaxFlightSecondsBtwMaintenance = updatedComponent.MaxFlightSecondsBtwMaintenance;
        }
    }
}
