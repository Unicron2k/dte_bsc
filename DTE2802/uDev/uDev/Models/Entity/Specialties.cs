using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace uDev.Models.Entity
{
    public class Specialties
    {
        public int SpecialtiesId { get; set; }
        public virtual ApplicationUser User { get; set; }
        public virtual SpecialtyLanguage SpecialtyLanguage { get; set; }
        public virtual Mission Mission { get; set; }
    }
}
