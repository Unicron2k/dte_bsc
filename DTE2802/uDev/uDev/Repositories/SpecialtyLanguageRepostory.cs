using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using uDev.Data;
using uDev.Models.Entity;
using uDev.Repositories.Interface;

namespace uDev.Repositories
{
    public class SpecialtyLanguageRepostory : ISpecialtiesLanguageRepository
    {
        private readonly ApplicationDbContext _db;

        public SpecialtyLanguageRepostory(ApplicationDbContext db)
        {
            _db = db;
        }
        public async Task<SpecialtyLanguage> GetSpecialtyLanguage(int? id)
        {
            return await _db.SpecialtyLanguages.FirstOrDefaultAsync(s => s.SpecialtyLanguageId == id);
        }
    }
}
