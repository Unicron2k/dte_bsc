using System.ComponentModel.DataAnnotations.Schema;
using Newtonsoft.Json;
using StarCake.Shared.Models.ViewModels;
// ReSharper disable UnusedAutoPropertyAccessor.Global
// ReSharper disable ClassWithVirtualMembersNeverInherited.Global

namespace StarCake.Server.Models.Entity
{
    public class DepartmentApplicationUser
    {
        public string ApplicationUserId { get; set; }
        [ForeignKey("ApplicationUserId")]
        //[JsonIgnore]
        public virtual ApplicationUser ApplicationUser { get; set; }
        
        public int DepartmentId { get; set; }
        [ForeignKey("DepartmentId")]
        [JsonIgnore]
        public virtual Department Department { get; set; }
        
        public bool IsMaintainer { get; set; }

        public DepartmentApplicationUserViewModel MapToViewModel()
        {
            return new DepartmentApplicationUserViewModel
            {
                ApplicationUserId = ApplicationUserId,
                ApplicationUser = ApplicationUser.MapToViewModel(),
                DepartmentId = DepartmentId,
                IsMaintainer = IsMaintainer
            };
        }
    }
}