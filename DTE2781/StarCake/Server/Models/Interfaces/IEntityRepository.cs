using System.Collections.Generic;
using System.Threading.Tasks;
using StarCake.Server.Models.Repositories;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IEntityRepository
    {
        Task<IEnumerable<Entity.Entity>> GetAll();
        Task Save(Entity.Entity entity);
        Task Update(Entity.Entity entity);
        Entity.Entity Get(int? id);

        Task<List<EntityViewModel>> GetViewModelsInDepartment(int departmentId, IComponentRepository componentRepository, IFlightLogRepository flightLogRepository);
        Task<EntityViewModel> GetViewModel(int entityId);
    }
}