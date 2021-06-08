using System.Collections.Generic;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface ITypeOfOperationRepository
    {
        Task<IEnumerable<TypeOfOperation>> GetAll();
        Task Save(TypeOfOperation typeOfOperation);
        Task Update(TypeOfOperationViewModel typeOfOperationViewModel);
        public Task<TypeOfOperation> Get(int? id);
    }
}