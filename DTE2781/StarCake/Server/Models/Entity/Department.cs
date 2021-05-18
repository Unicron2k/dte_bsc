using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using StarCake.Shared.Models.ViewModels;
// ReSharper disable UnusedAutoPropertyAccessor.Global
// ReSharper disable ClassWithVirtualMembersNeverInherited.Global

namespace StarCake.Server.Models.Entity
{
    public class Department
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
        public int OrganizationId { get; set; }
        [ForeignKey("OrganizationId")]
        public virtual Organization Organization { get; set; }
        
        public virtual ICollection<DepartmentApplicationUser> DepartmentApplicationUsers { get; set; }
        
        [Range(0, int.MaxValue, ErrorMessage = "Please enter valid number")]
        public int DeltaCycles { get; set; }
        [Range(0, int.MaxValue, ErrorMessage = "Please enter valid number")]
        public int DeltaDays { get; set; }
        [Range(0, int.MaxValue, ErrorMessage = "Please enter valid number")]
        public int DeltaSeconds { get; set; }
        
        public bool IsActive { get; set; }
        

        public DepartmentViewModel MapToViewModel()
        {
            return new DepartmentViewModel
            {
                DepartmentId = DepartmentId,
                Name = Name,
                City = City,
                Address = Address,
                ZipCode = ZipCode,
                Email = Email,
                PhoneNumber = PhoneNumber,
                OrganizationId = OrganizationId,
                DeltaCycles = DeltaCycles,
                DeltaDays = DeltaDays,
                DeltaSeconds = DeltaSeconds,
                IsActive = IsActive
            };
        }
    }
}
