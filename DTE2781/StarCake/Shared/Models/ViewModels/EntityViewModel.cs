using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace StarCake.Shared.Models.ViewModels
{
    public class EntityViewModel : MasterMaintenanceViewModel
    {
        public int EntityId { get; set; }
        [Required]
        public int EntityTypeId { get; set; }
        public bool IsArchived { get; set; }
        
        [Required]
        public string ImageContentType { get; set; }
        [Required]
        public string ImageBase64 { get; set; }
        
        public virtual List<ComponentViewModel> Components { get; set; }
        public virtual List<FlightLogViewModel> FlightLogs { get; set; }
        public virtual int FlightLogCount { get; set; }
        public bool HasComponentRequiringMaintenance { get; set; }
        
        public EntityViewModel Clone()
        {
            return (EntityViewModel) MemberwiseClone();
        }

        public void Update(EntityViewModel updatedEntity)
        {
            EntityTypeId = updatedEntity.EntityTypeId;
            IsArchived = updatedEntity.IsArchived;
            //FileDetailId = updatedEntity.FileDetailId;
            ImageContentType = ImageContentType; 
            ImageBase64 = ImageBase64; 
            Name = updatedEntity.Name;
            CreationDate = updatedEntity.CreationDate;
            DepartmentId = updatedEntity.DepartmentId;
            ManufacturerId = updatedEntity.ManufacturerId;
            SerialNumber = updatedEntity.SerialNumber;
            TotalFlightCycles = updatedEntity.TotalFlightCycles;
            TotalFlightDurationInSeconds = updatedEntity.TotalFlightDurationInSeconds;
            CyclesSinceLastMaintenance = updatedEntity.CyclesSinceLastMaintenance;
            FlightSecondsSinceLastMaintenance = updatedEntity.FlightSecondsSinceLastMaintenance;
            LastMaintenanceDate = updatedEntity.LastMaintenanceDate;
            MaxCyclesBtwMaintenance = updatedEntity.MaxCyclesBtwMaintenance;
            MaxDaysBtwMaintenance = updatedEntity.MaxDaysBtwMaintenance;
            MaxFlightSecondsBtwMaintenance = updatedEntity.MaxFlightSecondsBtwMaintenance;
        }
        
        public string ImageAsDataUriBase64()
        {
            if (ImageBase64==null)
                return "";
            return ImageBase64 == null ? "" : $"data:{ImageContentType};base64,{ImageBase64}";
        }
    }

    public class EntityTypeViewModel
    {
        public int EntityTypeId { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Name { get; set; }
        [Required, DefaultValue(true)]
        public bool IsActive { get; set; }

        public EntityTypeViewModel Clone()
        {
            return (EntityTypeViewModel) MemberwiseClone();
        }

        public void Update(EntityTypeViewModel entityType)
        {
            Name = entityType.Name;
            IsActive = entityType.IsActive;

        }
    }
}