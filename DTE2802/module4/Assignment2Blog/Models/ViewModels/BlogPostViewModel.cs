using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Assignment2Blog.Models.Entities;
using Microsoft.AspNetCore.Identity;

namespace Assignment2Blog.Models.ViewModels
{
    public class BlogPostViewModel
    {
        [Key] public int BlogPostId { get; set; }

        [StringLength(128)]
        [Required(ErrorMessage = "Your post must have a title!")]
        public string Title { get; set; }

        [StringLength(1500)]
        [Required(ErrorMessage = "Your post must have content")]
        public string Content { get; set; }

        public virtual IdentityUser Owner { get; set; }

        public string PostDate { get; set; }

        public bool BlogPostLocked { get; set; }

        public int BlogId { get; set; }
        public virtual Blog Blog { get; set; }

        public List<Comment> Comments { get; set; }
    }
}