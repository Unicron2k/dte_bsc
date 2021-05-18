using System.ComponentModel.DataAnnotations.Schema;
// ReSharper disable ClassWithVirtualMembersNeverInherited.Global
// ReSharper disable UnusedAutoPropertyAccessor.Global

namespace StarCake.Shared.Models.ViewModels
{
    public class DepartmentApplicationUserViewModel
    {
          
        public string ApplicationUserId { get; set; }
        [ForeignKey("ApplicationUserId")]
        public virtual ApplicationUserViewModel ApplicationUser { get; set; }
        
        public int DepartmentId { get; set; }
        
        public bool IsMaintainer { get; set; }

        public DepartmentApplicationUserViewModel Clone()
        {
            return (DepartmentApplicationUserViewModel) MemberwiseClone();
        }
    }
}