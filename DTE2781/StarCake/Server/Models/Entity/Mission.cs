using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;


namespace StarCake.Server.Models.Entity
{
    public class Mission
    {
        public int MissionId { get; set; }
        [Required]
        public virtual ApplicationUser User { get; set; }
        [Required, StringLength(500, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/500")]
        public string BriefingText { get;  set; }
        
        [Required]
        public int EntityId { get; set; }
        [ForeignKey("EntityId")]
        [JsonIgnore]
        public virtual Entity Entity { get; set; }
        
        [Required, DataType(DataType.Date)]
        public DateTime MissionDate { get; set; }
    }
}
