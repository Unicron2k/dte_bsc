using System.ComponentModel.DataAnnotations;
using Assignment2Blog.Models.Entities;
using Microsoft.AspNetCore.Identity;

namespace Assignment2Blog.Models.ViewModels
{
    public class CommentViewModel
    {
        [Key]
        public int CommentId { get; set; }
        
        [StringLength(500)]
        [MinLength(2)]
        [Required(ErrorMessage = "You need to write a comment!")]
        public string Content { get; set; }
        
        
        public virtual IdentityUser Owner { get; set; }
        
        public string PostDate { get; set; }
        
        public int ReplyTo { get; set; }

        public int BlogPostId { get; set; }
        public virtual BlogPost BlogPost { get; set; }
    }
}