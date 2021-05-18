using System.Collections.Generic;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IComponentTypeRepository
    {
        public Task<List<ComponentType>> GetAll();
        public Task SaveComponentType(ComponentType componentType);
        public Task<ComponentType> GetComponentType(int? id);
        public Task UpdateComponentType(ComponentTypeViewModel componentType);
        public Task DeleteComponentType(ComponentType componentType);
        public ComponentTypeViewModel GetComponentTypeViewModel(int? id);

    }
}
