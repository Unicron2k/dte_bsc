using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations.Schema;
using StarCake.Shared.Models.ViewModels;
// ReSharper disable UnusedAutoPropertyAccessor.Global
// ReSharper disable ClassWithVirtualMembersNeverInherited.Global

namespace StarCake.Shared.Models.ViewModels
{
    public class ApplicationUserViewModel
    {
        public string Id { get; set; }
        
        public DateTime CreationDate { get;  set; }
        public string UserName { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public DateTimeOffset? LockoutEnd { get; set; }
        public int CurrentLoggedInDepartmentId { get; set; }


        public virtual ICollection<DepartmentViewModel> Departments { get; set; }
        public virtual DepartmentViewModel CurrentDepartment { get; set; }
        public virtual ICollection<DepartmentApplicationUserViewModel> DepartmentApplicationUsers { get; set; }
        public bool IsOranizationMaintainer { get; set; }
        public bool CanTrack { get; set; }

        public string GetNameFormal()
        {
            return $"{LastName}, {FirstName}";
        }
    }
}