using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;
using StarCake.Shared.Models;
using StarCake.Shared.Models.ViewModels;
// ReSharper disable UnusedAutoPropertyAccessor.Global
// ReSharper disable ClassWithVirtualMembersNeverInherited.Global

namespace StarCake.Server.Models.Entity
{
    public class Entity : MasterMaintenance
    {
        public int EntityId { get; set; }
        public bool IsArchived { get; set; }

        [Required]
        public int EntityTypeId { get; set; }
        [ForeignKey("EntityTypeId")]
        [JsonIgnore]
        public virtual EntityType EntityType { get; set; }
        
        public string ImageContentType { get; set; }
        public string ImageBase64 { get; set; }
        
        public virtual ICollection<Component> Components { get; set; }
        public virtual List<FlightLog> FlightLogs { get; set; }

        
        public const int ImageEncodingQuality = 30; // max = 100
        public EntityViewModel MapToViewModel()
        {
            return new EntityViewModel
            {
                EntityId = EntityId,
                IsArchived = IsArchived,
                EntityTypeId = EntityTypeId,
                ImageContentType = ImageContentType,
                ImageBase64 = ImageBase64,
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

        public static Entity ViewModelToModel(EntityViewModel entityViewModel)
        {
            var entity =  new Entity
            {
                EntityId = entityViewModel.EntityId,
                IsArchived = entityViewModel.IsArchived,
                EntityTypeId = entityViewModel.EntityTypeId,
                ImageContentType = entityViewModel.ImageContentType,
                ImageBase64 = entityViewModel.ImageBase64,
                Name = entityViewModel.Name,
                CreationDate = entityViewModel.CreationDate,
                DepartmentId = entityViewModel.DepartmentId,
                ManufacturerId = entityViewModel.ManufacturerId,
                SerialNumber = entityViewModel.SerialNumber,
                TotalFlightCycles = entityViewModel.TotalFlightCycles,
                TotalFlightDurationInSeconds = entityViewModel.TotalFlightDurationInSeconds,
                CyclesSinceLastMaintenance = entityViewModel.CyclesSinceLastMaintenance,
                FlightSecondsSinceLastMaintenance = entityViewModel.FlightSecondsSinceLastMaintenance,
                LastMaintenanceDate = entityViewModel.LastMaintenanceDate,
                MaxCyclesBtwMaintenance = entityViewModel.MaxCyclesBtwMaintenance,
                MaxDaysBtwMaintenance = entityViewModel.MaxDaysBtwMaintenance,
                MaxFlightSecondsBtwMaintenance = entityViewModel.MaxFlightSecondsBtwMaintenance
            };

            foreach (var componentViewModel in entityViewModel.Components)
                entity.Components.Add(Component.ViewModelToModel(componentViewModel));
            return entity;
        }
    }

    public class EntityType
    {
        public int EntityTypeId { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Name { get; set; }
        [Required, DefaultValue(true)]
        public bool IsActive { get; set; }
    }
}
