using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Repositories
{
    public class CountryRepository : ICountryRepository
    {
        private readonly ApplicationDbContext _db;

        public CountryRepository(ApplicationDbContext db)
        {
            _db = db;
        }

        public async Task<IEnumerable<Country>> GetAll()
        {
            return await _db.Countries
                .ToListAsync();
        }

        public async Task Save(Country country)
        {
            await _db.Countries.AddAsync(country);
            await _db.SaveChangesAsync();
        }

        public async Task Update(CountryViewModel countryViewModel)
        {
            var country = new Country
            {
                CountryId = countryViewModel.CountryId,
                Name = countryViewModel.Name,
                CountryCode = countryViewModel.CountryCode,
                IsActive = countryViewModel.IsActive
            };
            _db.Countries.Update(country);
            await _db.SaveChangesAsync();
        }

        
        public async Task<Country> Get(int? id)
        {
            if (id == null)
                return new Country();
            return await _db.Countries.FirstOrDefaultAsync(c => c.CountryId == id);
        }
    }
}