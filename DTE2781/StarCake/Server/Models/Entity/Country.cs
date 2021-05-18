using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
// ReSharper disable UnusedAutoPropertyAccessor.Global

namespace StarCake.Server.Models.Entity
{
    public class Country
    {
        public int CountryId { get; set; }  
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Name must be between {0} and {1} characters!")]
        public string Name { get; set; }
        [Required, StringLength(10, MinimumLength = 1, ErrorMessage = "Country code must be between {0} and {1} characters!")]
        public string CountryCode { get; set; }
        [Required, DefaultValue(true)] 
        public bool IsActive { get; set; }
    }
}
