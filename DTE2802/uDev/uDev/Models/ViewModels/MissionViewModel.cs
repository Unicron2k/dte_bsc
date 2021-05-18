using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using uDev.Models.Entity;

namespace uDev.Models.ViewModels
{
    public class MissionViewModel
    {
      [Key]
      public int MissionId { get; set; }
      [StringLength(128)]
      [Required(ErrorMessage = "Your Mission needs a Title")]
      public string Title { get; set; }
      [StringLength(256)]
      [Required(ErrorMessage = "Your Mission needs a description")]
      public string Content { get; set; }
      public virtual ApplicationUser Owner { get; set; }
      public DateTime Created { get; set; }
      public DateTime Modified { get; set; }
      public bool Claimed { get; set; }
      public virtual List<Claimer> Claimers { get; set; }
      public bool Completed { get; set; }
      public virtual List<Comment> Comments { get; set; }
      public int CategoryId { get; set; }
      public Category Category { get; set; }
      public virtual List<Category> Categories { get; set; }
      public virtual List<SpecialtyLanguage> SpecialtyLanguages { get; set; }
      public int SpecialtyLanguageId { get; set; }
    }
}
