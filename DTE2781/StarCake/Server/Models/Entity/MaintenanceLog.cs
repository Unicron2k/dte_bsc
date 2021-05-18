using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;


namespace StarCake.Server.Models.Entity
{
    public class MaintenanceLog
    {
        [Key]
        public int MaintenanceLogId { get; set; }
        
        [Required]
        public DateTime Date { get; set; }
        
        [Required]
         public long ACCSeconds { get; set; }
         
        [Required, StringLength(500, MinimumLength = 8, ErrorMessage = "Min/Max string length is 8/500")]
        public string TaskDescription { get; set; }
        
        [Required, StringLength(500, MinimumLength = 8, ErrorMessage = "Min/Max string length is 8/500")]
        public string ActionDescription { get; set; }

        [Required]
        public string ApplicationUserIdLogged { get; set; }
        [ForeignKey("ApplicationUserIdLogged")]
        [JsonIgnore]
        public virtual ApplicationUser ApplicationUserLogged { get; set; }

        //The department the component/Entity was associated with 
        [Required]
        public int DepartmentId { get; set; }
        [ForeignKey("DepartmentId")]
        [JsonIgnore]
        public virtual Department Department { get; set; }

        [Required]
        public int EntityId { get; set; }
        [ForeignKey("EntityId")]
        [JsonIgnore]
        public virtual Entity Entity { get; set; }
        
        //If this is set to NULL/0, the maintenance was performed on the entity itself
        public int? ComponentId { get; set; }
        [ForeignKey("ComponentId")]
        [JsonIgnore]
        public virtual Component Component { get; set; }

    }
}
