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
    public class ManufacturRepository : IManufacturRepository
    {
        private readonly ApplicationDbContext _db;

        public ManufacturRepository(ApplicationDbContext db)
        {
            _db = db;
        }
        public async Task<List<Manufacturer>> GetAll()
        {
            return await _db.Manufacturers.ToListAsync();
        }

        public async Task SaveManufacturer(Manufacturer manufacturer)
        {
            await _db.Manufacturers.AddAsync(manufacturer);
            await _db.SaveChangesAsync();
        }

        public async Task<Manufacturer> GetManufacturer(int? id)
        {
            if (id == null)
                return new Manufacturer();
            return await _db.Manufacturers.FirstOrDefaultAsync(m => m.ManufacturerId == id);
        }

        public async Task UpdateManufacturer(ManufacturerViewModel manufacturer)
        {
            var m = new Manufacturer
            {
                ManufacturerId = manufacturer.ManufacturerId,
                Name = manufacturer.Name,
                IsActive = manufacturer.IsActive
            };
            _db.Manufacturers.Update(m);
            await _db.SaveChangesAsync();
        }

        public async Task DeleteManufacturer(Manufacturer manufacturer)
        {
            _db.Manufacturers.Remove(manufacturer);
            await _db.SaveChangesAsync();
        }

        public ManufacturerViewModel GetManufacturViewModel(int? id)
        {
            ManufacturerViewModel mViewModel;
            if (id == null)
            {
                mViewModel = new ManufacturerViewModel();
            }
            else
            {
                mViewModel = (from m in _db.Manufacturers
                        where m.ManufacturerId == id
                        select new ManufacturerViewModel()
                        {
                            ManufacturerId = m.ManufacturerId,
                            Name = m.Name,
                            IsActive = m.IsActive
                        }
                    ).FirstOrDefault();
            }

            return mViewModel;
        }
    }
}