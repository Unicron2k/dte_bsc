using System;
using System.ComponentModel.DataAnnotations;
using Microsoft.AspNetCore.Identity;
using System.Collections.Generic;
using Newtonsoft.Json;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Entity
{
    public class ApplicationUser : IdentityUser
    {
        public DateTime CreationDate { get;  set; } = DateTime.Now;
        [Required]
        [PersonalData]
        public string FirstName { get; set; }
        [Required]
        [PersonalData]
        public string LastName { get; set; }
        [Required]
        public int CurrentLoggedInDepartmentId { get; set; }
        [Required]
        [PersonalData]
        public virtual DateTime BirthDate { get; set; }
        [Required]
        [ProtectedPersonalData]
        public string EmployeeNumber { get; set; }

        public bool IsOrganizationMaintainer { get; set; }
        
        public bool CanTrack { get; set; }

        [JsonIgnore]
        public virtual ICollection<DepartmentApplicationUser> DepartmentApplicationUsers { get; set; }

        public ApplicationUserViewModel MapToViewModel()
        {
            return new ApplicationUserViewModel
            {
                
                Id = Id,
                CreationDate = CreationDate,
                UserName = UserName,
                FirstName = FirstName,
                LastName = LastName,
                LockoutEnd = LockoutEnd,
                CurrentLoggedInDepartmentId = CurrentLoggedInDepartmentId,
                IsOranizationMaintainer = IsOrganizationMaintainer,
                CanTrack = CanTrack
            };
        }
    }
}