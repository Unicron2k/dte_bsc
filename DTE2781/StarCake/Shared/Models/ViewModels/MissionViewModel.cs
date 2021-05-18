using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace StarCake.Shared.Models.ViewModels
{
    public class MissionViewModel
    {
        public int MissionId { get; set; }
        [Required]
        public virtual ApplicationUserViewModel UserViewModels { get; set; }
        [Required, StringLength(500, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/500")]
        public string BriefingText { get;  set; }
        
        [Required]
        public int EntityId { get; set; }
        [ForeignKey("EntityId")]
        public virtual EntityViewModel EntityViewModel { get; set; }
        
        [Required, DataType(DataType.Date)]
        public DateTime MissionDate { get; set; }
    }
}
