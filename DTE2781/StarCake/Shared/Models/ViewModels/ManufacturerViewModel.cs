using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace StarCake.Shared.Models.ViewModels
{
    public class ManufacturerViewModel
    {
        public int ManufacturerId { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Name { get; set; }
        [Required, DefaultValue(true)]
        public bool IsActive { get; set; }

        public ManufacturerViewModel Clone()
        {
            return (ManufacturerViewModel) MemberwiseClone();
        }

        public void Update(ManufacturerViewModel manufactur)
        {
            Name = manufactur.Name;
            IsActive = manufactur.IsActive;
        }
        
    }
}
