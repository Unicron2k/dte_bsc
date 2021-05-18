using System.Collections.Generic;
using Microsoft.AspNetCore.Identity;
using uDev.Models.Entity;

namespace uDev.Models
{
    public class RoleEdit
    {
        public IdentityRole Role { get; set; }
        public IEnumerable<ApplicationUser> Members { get; set; }
        public IEnumerable<ApplicationUser> NonMembers { get; set; }
    }
}
