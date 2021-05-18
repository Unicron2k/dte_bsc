using System.Collections.Generic;
using Newtonsoft.Json;
using Microsoft.AspNetCore.Identity;

namespace ProjectREST.Models.Entities
{
    public class Blog
    {
        public int BlogId { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        
        [JsonIgnore]
        public virtual ApplicationUser Owner { get; set; }
        public string CreationDate { get; set; }
        public bool BlogLocked { get; set; }
        
        [JsonIgnore]
        public virtual List<BlogPost> BlogPosts { get; set; }
    }
}