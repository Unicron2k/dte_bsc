using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;
using StarCake.Shared.Models.ViewModels;
// ReSharper disable ClassWithVirtualMembersNeverInherited.Global

namespace StarCake.Server.Models.Entity
{
    public class Component : MasterMaintenance
    {
        public int ComponentId { get; set; }

        public int? EntityId { get; set; }
        [ForeignKey("EntityId")]
        [JsonIgnore]
        public virtual Entity Entity { get; set; }
        
        [Required]
        public int ComponentTypeId { get; set; }
        [ForeignKey("ComponentTypeId")]
        [JsonIgnore]
        public virtual ComponentType ComponentType { get; set; }
        
        public ComponentViewModel MapToViewModel()
        {
            return new ComponentViewModel()
            {
                ComponentId = ComponentId,
                EntityId = EntityId,
                ComponentTypeId = ComponentTypeId,
                Name = Name,
                CreationDate = CreationDate,
                DepartmentId = DepartmentId,
                ManufacturerId = ManufacturerId,
                SerialNumber = SerialNumber,
                TotalFlightCycles = TotalFlightCycles,
                TotalFlightDurationInSeconds = TotalFlightDurationInSeconds,
                CyclesSinceLastMaintenance = CyclesSinceLastMaintenance,
                FlightSecondsSinceLastMaintenance = FlightSecondsSinceLastMaintenance,
                LastMaintenanceDate = LastMaintenanceDate,
                MaxCyclesBtwMaintenance = MaxCyclesBtwMaintenance,
                MaxDaysBtwMaintenance = MaxDaysBtwMaintenance,
                MaxFlightSecondsBtwMaintenance = MaxFlightSecondsBtwMaintenance,
            };
        }

        public static Component ViewModelToModel(ComponentViewModel componentViewModel)
        {
            return new Component
            {
                ComponentId = componentViewModel.ComponentId,
                EntityId = componentViewModel.EntityId,
                ComponentTypeId = componentViewModel.ComponentTypeId,
                Name = componentViewModel.Name,
                CreationDate = componentViewModel.CreationDate,
                DepartmentId = componentViewModel.DepartmentId,
                ManufacturerId = componentViewModel.ManufacturerId,
                SerialNumber = componentViewModel.SerialNumber,
                TotalFlightCycles = componentViewModel.TotalFlightCycles,
                TotalFlightDurationInSeconds = componentViewModel.TotalFlightDurationInSeconds,
                CyclesSinceLastMaintenance = componentViewModel.CyclesSinceLastMaintenance,
                FlightSecondsSinceLastMaintenance = componentViewModel.FlightSecondsSinceLastMaintenance,
                LastMaintenanceDate = componentViewModel.LastMaintenanceDate,
                MaxCyclesBtwMaintenance = componentViewModel.MaxCyclesBtwMaintenance,
                MaxDaysBtwMaintenance = componentViewModel.MaxDaysBtwMaintenance,
                MaxFlightSecondsBtwMaintenance = componentViewModel.MaxFlightSecondsBtwMaintenance
            };
        }

    }
}