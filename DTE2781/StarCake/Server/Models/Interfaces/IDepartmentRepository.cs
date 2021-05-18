using System.Collections;
using System.Collections.Generic;
using System.Security.Claims;
using System.Security.Principal;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IDepartmentRepository
    {
        Task<IEnumerable<Department>> GetAll();
        public Task<IEnumerable<Department>> GetAllForUser();
        Department Get(int? id);
        Task<DepartmentViewModel> GetViewModel(int? id);
        Task Save(Department department);
        Task Update(DepartmentViewModel departmentViewModel);
    }
}