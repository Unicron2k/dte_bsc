using Microsoft.AspNetCore.Identity;

namespace Assignment2Blog.Models.Entities
{
    public class Comment
    {
        public int CommentId { get; set; }
        
        public string Content { get; set; }
        
        public virtual IdentityUser Owner { get; set; }
        
        public string PostDate { get; set; }
        
        public int ReplyTo { get; set; }

        public int BlogPostId { get; set; }
        
        public virtual BlogPost BlogPost { get; set; }
    }
}