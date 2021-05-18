using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace StarCake.Server.Models.Entity
{
    public class ComponentType
    {
        public int ComponentTypeId { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Name { get; set; }
        [Required, DefaultValue(true)]
        public bool IsActive { get; set; }
    }
}