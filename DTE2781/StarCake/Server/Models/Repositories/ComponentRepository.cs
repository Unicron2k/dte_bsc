using System;
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
    public class ComponentRepository : IComponentRepository
    {
        private readonly ApplicationDbContext _db;

        public ComponentRepository(ApplicationDbContext db)
        {
            _db = db;
        }
        
        public async Task Update(Component component)
        {
            _db.Components.Update(component);
            await _db.SaveChangesAsync();
        }

        public async Task Save(Component component)
        {
            await _db.Components.AddAsync(component);
            await _db.SaveChangesAsync();
        }

        public async Task<List<ComponentViewModel>> AllInEntityViewModel(int entityId)
        {
            var componentList = await _db.Components
                .Where(x => x.EntityId == entityId)
                .Select(x => x.ComponentId)
                .ToListAsync();
            var componentViewModels = new List<ComponentViewModel>();
            foreach (var viewModel in componentList.Select(componentId => GetViewModel(componentId).Result))
            {
                Console.WriteLine("");
                componentViewModels.Add(viewModel);
                // TODO: Add image in Component also
            }
            return componentViewModels;
        }

        private async Task<ComponentViewModel> GetViewModel(int componentId)
        {
            var component = await _db.Components.FindAsync(componentId);
            return component.MapToViewModel();
        }

        public async Task<List<ComponentViewModel>> AllArchivedInDepartment(int departmentId)
        {
            var componentList = await _db.Components
                .Where(x => x.DepartmentId == departmentId)
                .Where(x => x.EntityId == null)
                .Select(x => x.ComponentId)
                .ToListAsync();
            return componentList.Select(componentId => GetViewModel(componentId).Result).ToList();
        }
    }
}