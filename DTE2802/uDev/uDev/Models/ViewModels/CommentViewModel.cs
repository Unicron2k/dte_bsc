using System;
using System.ComponentModel.DataAnnotations;
using uDev.Models.Entity;

namespace uDev.Models.ViewModels
{
    public class CommentViewModel
    {
        [Key]
        public int CommentId { get; set; }
        
        [StringLength(500)]
        [MinLength(2)]
        [Required(ErrorMessage = "You need to write a comment!")]
        public string Content { get; set; }

        public int MissionId { get; set; }
        public virtual Mission Mission { get; set; }
        
        public DateTime Created { get; set; }
        
        public int OwnerId { get; set; }
        public virtual ApplicationUser Owner {get; set;} 
    }
}