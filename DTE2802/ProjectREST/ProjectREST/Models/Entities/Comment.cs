using Microsoft.AspNetCore.Identity;
using Newtonsoft.Json;

namespace ProjectREST.Models.Entities
{
    public class Comment
    {
        public int CommentId { get; set; }
        
        public string Content { get; set; }
        
        [JsonIgnore]
        public virtual ApplicationUser Owner { get; set; }
        
        public string PostDate { get; set; }
        
        public int ReplyTo { get; set; }

        public int BlogPostId { get; set; }
        
        [JsonIgnore]
        public virtual BlogPost BlogPost { get; set; }
    }
}