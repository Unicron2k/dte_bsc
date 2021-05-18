using System;

namespace uDev.Models.Entity
{
    public class Comment
    {
        public int CommentId { get; set; }

        public string Content { get; set; }

        public int MissionId { get; set; }
        public virtual Mission Mission { get; set; }
        public DateTime Created { get; set; }
        public int OwnerId { get; set; }
        public virtual ApplicationUser Owner {get; set;} 
    }
}
