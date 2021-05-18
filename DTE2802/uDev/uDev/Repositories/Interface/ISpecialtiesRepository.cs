using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using uDev.Models.Entity;

namespace uDev.Repositories.Interface
{
    public interface ISpecialtiesRepository
    {
        public Task<Specialties> GetSpecialties(int? id);
    }
}
