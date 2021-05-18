using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;

namespace StarCake.Server.Models.Repositories
{
    public class MaintenanceLogRepository : IMaintenanceLogRepository
    {
        private readonly ApplicationDbContext _db;

        public MaintenanceLogRepository(ApplicationDbContext db)
        {
            _db = db;
        }
        public async Task<MaintenanceLog> Get(int id)
        {
            return await _db.MaintenanceLogs.Where(log => log.MaintenanceLogId == id).FirstOrDefaultAsync();
        }
        
        public async Task<IEnumerable<MaintenanceLog>> GetAll()
        {
            return await _db.MaintenanceLogs.ToListAsync();
        }

        public async Task<IEnumerable<MaintenanceLog>> GetAllInOrganization(int organizationId)
        {
            return await _db.MaintenanceLogs
                .Where(log => log.Department.OrganizationId == organizationId)
                .ToListAsync();
        }

        public async Task<IEnumerable<MaintenanceLog>> GetAllInDepartment(int departmentId)
        {
            return await _db.MaintenanceLogs
                .Where(log => log.Department.DepartmentId == departmentId)
                .ToListAsync();
        }

        public async Task Save(MaintenanceLog maintenanceLog)
        {
            _db.Add(maintenanceLog);
            await _db.SaveChangesAsync();
        }
    }
}