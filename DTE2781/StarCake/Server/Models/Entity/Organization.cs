using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;
using StarCake.Shared.Models.ViewModels;
// ReSharper disable UnusedAutoPropertyAccessor.Global
// ReSharper disable ClassWithVirtualMembersNeverInherited.Global

namespace StarCake.Server.Models.Entity
{
    public class Organization
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
        
        [Required]
        [JsonIgnore]
        public virtual List<Department> Departments { get; set; }
        
        public OrganizationViewModel MapToViewModel()
        {
            return new OrganizationViewModel
            {
                OrganizationId = OrganizationId,
                Name = Name,
                City = City,
                Address = Address,
                ZipCode = ZipCode,
                Email = Email,
                PhoneNumber = PhoneNumber,
                OperatorNumber = OperatorNumber,
                OrganizationNumber = OrganizationNumber,
                ApiKeyOpenCageData = ApiKeyOpenCageData
            };
        }
    }
}
