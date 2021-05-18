using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace uDev.Models.Entity
{
    public class Category
    {
        public int CategoryId { get; set; }
        [Required]
        public string Title { get; set; }
        [Required]
        public string Content { get; set; }

    }
}
