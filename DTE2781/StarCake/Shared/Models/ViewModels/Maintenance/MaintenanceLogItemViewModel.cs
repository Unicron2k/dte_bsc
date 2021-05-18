using System.ComponentModel.DataAnnotations;

namespace StarCake.Shared.Models.ViewModels.Maintenance
{
    public class MaintenanceLogItemViewModel
    {
        [Required] public int EntityId { get; set; }
        public int ComponentId { get; set; }
        [Required] public string Name { get; set; }
        [Required] public string SerialNumber { get; set; }
        [Required] public string TaskDescription { get; set; }
        [Required] public string ActionDescription { get; set; }

        public static MaintenanceLogItemViewModel ToModel(ComponentViewModel model)
        {
            return new MaintenanceLogItemViewModel
            {
                EntityId = model.EntityId ?? 0,
                ComponentId = model.ComponentId,
                Name = model.Name,
                SerialNumber = model.SerialNumber,
                TaskDescription = "",
                ActionDescription = ""
            };
        }
    }
}