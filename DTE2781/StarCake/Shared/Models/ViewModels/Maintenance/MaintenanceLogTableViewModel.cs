using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace StarCake.Shared.Models.ViewModels.Maintenance
{
    public class MaintenanceLogTableViewModel
    {
        [Key] public int MaintenanceLogId { get; set; }

        public DateTime Date { get; set; }

        [Required] public long ACCSeconds { get; set; }

        [Required] public string TaskDescription { get; set; }

        [Required] public string ActionDescription { get; set; }


        public string ApplicationUserIdLogged { get; set; } = null;
        [ForeignKey("ApplicationUserIdLogged")]
        public virtual ApplicationUserViewModel ApplicationUserLogged { get; set; }

        //The department the component/Entity was associated with 
        [Required] public int DepartmentId { get; set; }
        [ForeignKey("DepartmentId")] public virtual DepartmentViewModel Department { get; set; }

        [Required] public string MaintainedItemName { get; set; }
        [Required] public string MaintainedItemSerialNumber { get; set; }
        
        public static MaintenanceLogTableViewModel ToModel(MaintenanceLogViewModel model)
        {
            return new MaintenanceLogTableViewModel
            {
                MaintenanceLogId = model.MaintenanceLogId,
                Date = model.Date,
                ACCSeconds = model.ACCSeconds,
                TaskDescription = model.TaskDescription,
                ActionDescription = model.ActionDescription,
                ApplicationUserIdLogged = model.ApplicationUserIdLogged,
                ApplicationUserLogged = model.ApplicationUserLogged,
                DepartmentId = model.DepartmentId,
                Department = model.Department,
                MaintainedItemName = model.ComponentId == 0 ? model.Entity.Name : model.Component.Name,
                MaintainedItemSerialNumber = model.ComponentId == 0 ? model.Entity.SerialNumber : model.Component.SerialNumber
            };
        }
    }
}