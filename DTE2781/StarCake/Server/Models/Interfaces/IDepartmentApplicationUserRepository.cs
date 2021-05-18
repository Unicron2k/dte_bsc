using System.Collections;
using System.Collections.Generic;
using System.Security.Principal;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IDepartmentApplicationUserRepository
    {
        Task<IEnumerable<DepartmentApplicationUser>> GetAll();
        // TODO: Ensure this is working
        Task<IEnumerable<DepartmentApplicationUser>> GetAllByDepartmentId(int? departmentId);
        Task<IEnumerable<DepartmentApplicationUser>> GetAllByUserId(string userId);
        Task<List<DepartmentApplicationUser>> GetAllByEntityId(int? entityId);
        Task Save(DepartmentApplicationUser departmentApplicationUser);
        DepartmentApplicationUser Get(int? id);
        Task Add(DepartmentApplicationUser departmentApplicationUser);
        Task Remove(string userId, int departmentId);
        Task<List<DepartmentApplicationUserViewModel>> GetViewModelsInDepartment(int departmentId);

        Task UpdateDepartmentMaintainer(string userId, int departmentId);
    }
}