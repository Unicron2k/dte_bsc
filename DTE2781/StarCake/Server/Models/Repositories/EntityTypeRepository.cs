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
    public class EntityTypeRepository : IEntityTypeRepository
    {
        private ApplicationDbContext _db;

        public EntityTypeRepository(ApplicationDbContext db)
        {
            _db = db;
        }
        public async Task DeleteEntityType(EntityType entityType)
        {
            _db.EntityTypes.Remove(entityType);
            await _db.SaveChangesAsync();
        }

        public  async Task<List<EntityType>> GetAll()
        {
            return await _db.EntityTypes.ToListAsync();
        }

        public async Task<EntityType> GetEntityType(int? id)
        {
            if (id == null)
            {
                return new EntityType();
            }

            return await _db.EntityTypes.FirstOrDefaultAsync(e => e.EntityTypeId == id);
        }


        public EntityTypeViewModel GetEntityTypeViewModel(int? id)
        {
            EntityTypeViewModel eViewModel;
            if (id == null)
            {
                eViewModel = new EntityTypeViewModel();
            }
            else
            {
                eViewModel = (from e in _db.EntityTypes
                        where e.EntityTypeId == id
                        select new EntityTypeViewModel()
                        {
                            EntityTypeId = e.EntityTypeId,
                            Name = e.Name,
                            IsActive = e.IsActive
                        }
                    ).FirstOrDefault();
            }

            return eViewModel;
        }

        public async Task SaveEntityType(EntityType entityType)
        {
            await _db.EntityTypes.AddAsync(entityType);
            await _db.SaveChangesAsync();
        }

        public async Task UpdateEntityType(EntityTypeViewModel entityType)
        {
            var e = new EntityType
            {
                EntityTypeId = entityType.EntityTypeId,
                Name = entityType.Name,
                IsActive = entityType.IsActive
            };
            _db.EntityTypes.Update(e);
            await _db.SaveChangesAsync();
        }

    }
}