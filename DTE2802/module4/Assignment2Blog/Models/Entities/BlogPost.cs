using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Microsoft.AspNetCore.Identity;

namespace Assignment2Blog.Models.Entities
{
    public class BlogPost
    {
        public int BlogPostId { get; set; }
        
        public string Title { get; set; }
        
        public string Content { get; set; }
        
        public virtual IdentityUser Owner { get; set; }
        
        public string PostDate { get; set; }
        
        public bool BlogPostLocked { get; set; }
        
        public int BlogId { get; set; }
        public virtual Blog Blog { get; set; }
    }
}