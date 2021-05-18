using System.Collections;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.IO.Pipelines;
using StarCake.Shared.Models.ViewModels.Maintenance;

namespace StarCake.Shared.Models.ViewModels
{
    public class DepartmentViewModel
    {
        public int DepartmentId { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Name { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string City { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Address { get; set; }
        [Required, StringLength(10, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string ZipCode { get; set; }
        [Required, StringLength(200, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/200")]
        public string Email { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string PhoneNumber { get; set; }
        [Required]
        public int DeltaCycles { get; set; }
        [Required]
        public int DeltaDays { get; set; }
        [Required]
        public int DeltaSeconds { get; set; }
        [Required]
        public bool IsActive { get; set; }
        [Required]
        public int OrganizationId { get; set; }

        public DepartmentViewModel Clone()
        {
            return (DepartmentViewModel) MemberwiseClone();
        }

        public void Update(DepartmentViewModel updatedDepartment)
        {
            DepartmentId = updatedDepartment.DepartmentId;
            Name = updatedDepartment.Name;
            City = updatedDepartment.City;
            Address = updatedDepartment.Address;
            ZipCode = updatedDepartment.ZipCode;
            Email = updatedDepartment.Email;
            PhoneNumber = updatedDepartment.PhoneNumber;
            DeltaCycles = updatedDepartment.DeltaCycles;
            DeltaDays = updatedDepartment.DeltaDays;
            DeltaSeconds = updatedDepartment.DeltaSeconds;
            IsActive = updatedDepartment.IsActive;
            OrganizationId = updatedDepartment.OrganizationId;
        }
        
      
        [ForeignKey("OrganizationId")]
        public virtual OrganizationViewModel Organization { get; set; }
        
        public virtual List<EntityViewModel> Entities { get; set; }
        public virtual List<ComponentViewModel> ArchivedComponents { get; set; }
        //public virtual ICollection<ApplicationUserViewModel> ApplicationUsers { get; set; }
        public virtual ICollection<DepartmentApplicationUserViewModel> DepartmentApplicationUsers { get; set; }
        public virtual IList<MaintenanceLogViewModel> MaintenanceLogs { get; set; }
        
    }
}
