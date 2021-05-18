using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Repositories
{
    public class ComponentTypeRepository : IComponentTypeRepository
    {
        private readonly ApplicationDbContext _db;
        private readonly UserManager<ApplicationUser> _userManager;

        public ComponentTypeRepository(UserManager<ApplicationUser> userManager, ApplicationDbContext db)
        {
            _db = db;
            _userManager = userManager;
        }
        public async Task DeleteComponentType(ComponentType componentTypes)
        {
            _db.ComponentTypes.Remove(componentTypes);
            await _db.SaveChangesAsync();
        }

        public async Task<List<ComponentType>> GetAll()
        {
            return await _db.ComponentTypes.ToListAsync();
        }
        

        public async Task<ComponentType> GetComponentType(int? id)
        {
            if (id == null)
            {
                return new ComponentType();
            }
            return await _db.ComponentTypes.FirstOrDefaultAsync(c => c.ComponentTypeId == id);
        }

        public ComponentTypeViewModel GetComponentTypeViewModel(int? id)
        {
            ComponentTypeViewModel cViewModel;
            if (id == null)
            {
                cViewModel = new ComponentTypeViewModel();
            }
            else
            {
                cViewModel = (from c in _db.ComponentTypes
                        where c.ComponentTypeId == id
                        select new ComponentTypeViewModel()
                        {
                            ComponentTypeId = c.ComponentTypeId,
                            Name = c.Name,
                            IsActive = c.IsActive
                        }
                    ).FirstOrDefault();
            }

            return cViewModel;
        }

        public async Task SaveComponentType(ComponentType componentTypes)
        {
            await _db.ComponentTypes.AddAsync(componentTypes);
            await _db.SaveChangesAsync();
        }

        public async Task UpdateComponentType(ComponentTypeViewModel componentTypes)
        {
            var c = new ComponentType
            {
                ComponentTypeId = componentTypes.ComponentTypeId,
                Name = componentTypes.Name,
                IsActive = componentTypes.IsActive
            };
            _db.ComponentTypes.Update(c);
            await _db.SaveChangesAsync();
        }
    }
}
