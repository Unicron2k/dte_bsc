using System.Collections.Generic;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IComponentRepository
    {
        Task<List<ComponentViewModel>> AllInEntityViewModel(int entityId);
        Task<List<ComponentViewModel>> AllArchivedInDepartment(int departmentId);

        Task Update(Component component);
        Task Save(Component component);
        
        Task<IEnumerable<Component>> GetAll();
        public Task<Component> Get(int? id);
    }
}