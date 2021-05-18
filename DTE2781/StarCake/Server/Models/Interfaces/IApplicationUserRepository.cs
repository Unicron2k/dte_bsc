using System.Collections;
using System.Collections.Generic;
using System.Security.Principal;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IApplicationUserRepository
    {
        Task<IEnumerable<ApplicationUser>> GetAll();
        Task Save(ApplicationUser applicationUser);
        //Task Update(ApplicationUserViewModel applicationUserViewModel);
        Task UpdateCurrentDepartmentId(string id, int departmentId);
        ApplicationUserViewModel GetApplicationUserViewModel(string? id);
        ApplicationUser Get(string? id);
        //Task<IEnumerable<Department>> GetDepartments(string? id);
        string GetName(string? id);
        Task Add(ApplicationUser c, IPrincipal principal);
        Task Remove(ApplicationUser c);
        bool IsInDepartment(int departmentId);
        Task ChangeDepartment(int departmentId);
        Task DisableUserLockEndDate(string userId, bool disable);

        Task UpdateMaintainDepartment(DepartmentApplicationUserViewModel departmentApplicationUserViewModel);

        Task UpdateOrganizationMaintainer(DepartmentApplicationUserViewModel departmentApplicationUserViewModel);
    }
}