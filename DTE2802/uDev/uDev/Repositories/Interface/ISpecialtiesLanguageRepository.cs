using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using uDev.Models.Entity;

namespace uDev.Repositories.Interface
{
    public interface ISpecialtiesLanguageRepository
    {
        public Task<SpecialtyLanguage> GetSpecialtyLanguage(int? id);
    }
}
