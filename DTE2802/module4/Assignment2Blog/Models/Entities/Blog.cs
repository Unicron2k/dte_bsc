
using System.ComponentModel.DataAnnotations;
using Microsoft.AspNetCore.Identity;

namespace Assignment2Blog.Models.Entities
{
    public class Blog
    {
        public int BlogId { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public virtual IdentityUser Owner { get; set; }
        public string CreationDate { get; set; }
        public bool BlogLocked { get; set; }
    }
}