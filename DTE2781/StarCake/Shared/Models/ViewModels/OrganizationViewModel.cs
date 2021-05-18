using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace StarCake.Shared.Models.ViewModels
{
    public class OrganizationViewModel
    {
        public int OrganizationId { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Name { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string City { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Address { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string ZipCode { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Email { get; set; }
        [Required, StringLength(20, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/20")]
        public string PhoneNumber { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string OperatorNumber { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string OrganizationNumber { get; set; }
        public string ApiKeyOpenCageData { get; set; }
        
        //public virtual ICollection<Department> Departments { get; set; }
        public virtual List<DepartmentViewModel> Departments { get; set; }
        public OrganizationViewModel Clone(){
            return (OrganizationViewModel) MemberwiseClone();
        }
    }
    
   
}
