using System.Collections;
using System.Collections.Generic;
using System.Security.Principal;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IMaintenanceLogRepository
    {
        public Task<MaintenanceLog> Get(int id);
        public Task<IEnumerable<MaintenanceLog>> GetAll();
        public Task<IEnumerable<MaintenanceLog>> GetAllInOrganization(int organizationId);
        public Task<IEnumerable<MaintenanceLog>> GetAllInDepartment(int departmentId);
        public Task Save(MaintenanceLog maintenanceLog);
    }
}