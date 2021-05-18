using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace StarCake.Shared.Models.ViewModels.Maintenance
{
    public class MaintenanceLogViewModel
    {
        [Key]
        public int MaintenanceLogId { get; set; }
        
        public DateTime Date { get; set; }
        
        [Required]
        public long ACCSeconds { get; set; }
         
        [Required, StringLength(500, MinimumLength = 8, ErrorMessage = "Min/Max string length is 8/500")]
        public string TaskDescription { get; set; }
        
        [Required, StringLength(500, MinimumLength = 8, ErrorMessage = "Min/Max string length is 8/500")]
        public string ActionDescription { get; set; }
        
        
        public string ApplicationUserIdLogged { get; set; } = null;
        [ForeignKey("ApplicationUserIdLogged")]
        public virtual ApplicationUserViewModel ApplicationUserLogged { get; set; }

        //The department the component/Entity was associated with 
        [Required]
        public int DepartmentId { get; set; }
        [ForeignKey("DepartmentId")]
        public virtual DepartmentViewModel Department { get; set; }

        [Required]
        public int EntityId { get; set; }
        [ForeignKey("EntityId")]
        public virtual EntityViewModel Entity { get; set; }
        
        //If this is set to NULL/0, the maintenance was performed on the entity itself
        public int ComponentId { get; set; }
        [ForeignKey("ComponentId")]
        public virtual ComponentViewModel Component { get; set; }
    }
}