using System.Collections.Generic;
using Microsoft.AspNetCore.Identity;

namespace ProjectREST.Models.Entities
{
    public class ApplicationUser : IdentityUser
    {
        public virtual List<Blog> SubscribedBlogs { get; set; }
    }
}