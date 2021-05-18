using System.Collections.Generic;
using Newtonsoft.Json;
using Microsoft.AspNetCore.Identity;

namespace ProjectREST.Models.Entities
{
    public class BlogPost
    {
        public int BlogPostId { get; set; }
        
        public string Title { get; set; }
        
        public string Content { get; set; }
        
        [JsonIgnore]
        public virtual ApplicationUser Owner { get; set; }
        
        public string PostDate { get; set; }
        
        public bool BlogPostLocked { get; set; }
        
        public int BlogId { get; set; }
        [JsonIgnore]
        public virtual Blog Blog { get; set; }
        [JsonIgnore]
        public virtual List<Comment> Comments { get; set; }
    }
}