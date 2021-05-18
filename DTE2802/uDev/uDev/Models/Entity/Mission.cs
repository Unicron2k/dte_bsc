using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Newtonsoft.Json;

namespace uDev.Models.Entity
{
    public class Mission
    {
        public int MissionId { get; set; }
        [Required]
        public string Title { get; set; }
        [Required]
        public string Content { get; set; }
        [JsonIgnore]
        public virtual ApplicationUser Owner { get; set; }
        public DateTime Created { get; set; }
        public DateTime Modified { get; set; }
        public bool Claimed { get; set; }
        public virtual List<Claimer> Claimers { get; set; }
        public bool Completed { get; set; }
        public virtual List<Specialties> Specialtieses { get; set; }
        public int SpecialtyLanguageId { get; set; }

        public virtual List<Comment> Comments { get; set; }

        public int CategoryId { get; set; }
        public virtual Category Category { get; set; }
    }
}
