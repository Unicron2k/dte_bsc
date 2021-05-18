using System.Collections.Generic;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IEntityTypeRepository
    {
        public Task<List<EntityType>> GetAll();
        public Task SaveEntityType(EntityType entityType);
        public Task<EntityType> GetEntityType(int? id);
        public Task UpdateEntityType(EntityTypeViewModel entityType);
        public Task DeleteEntityType(EntityType entityType);
        public EntityTypeViewModel GetEntityTypeViewModel(int? id);

    }
}