using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Microsoft.AspNetCore.Identity;
using ProjectREST.Models.Entities;

namespace ProjectREST.Models.ViewModels
{
    public class BlogViewModel
    {
        [Key]
        public int BlogId { get; set; }
        
        [StringLength(64)]
        [Required(ErrorMessage = "You must name your blog!")]
        public string Name { get; set; }
        
        [StringLength(256)]
        [Required(ErrorMessage = "Your blog needs a description.")]
        public string Description { get; set; }
        
        public virtual ApplicationUser Owner { get; set; }

        public string CreationDate { get; set; }
        
        public bool BlogLocked { get; set; }
        
        public List<BlogPost> BlogPosts { get; set; }
    }
}