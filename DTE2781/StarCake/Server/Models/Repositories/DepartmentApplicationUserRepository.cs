using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Microsoft.AspNetCore.Identity;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Repositories
{
    public class DepartmentApplicationUserRepository : IDepartmentApplicationUserRepository
    {
        
        private readonly ApplicationDbContext _db;
        private readonly UserManager<ApplicationUser> _userManager;

        public DepartmentApplicationUserRepository(ApplicationDbContext db, UserManager<ApplicationUser> userManager)
        {
            _db = db;
            _userManager = userManager;
        }

        public async Task<IEnumerable<DepartmentApplicationUser>> GetAll()
        {
            return await _db.DepartmentApplicationUsers.ToListAsync();
        }

        public async Task<IEnumerable<DepartmentApplicationUser>> GetAllByDepartmentId(int? departmentId)
        {
            return await _db.DepartmentApplicationUsers
                .Where(department => department.DepartmentId == departmentId)
                .ToListAsync();
        }

        public async Task<IEnumerable<DepartmentApplicationUser>> GetAllByUserId(string userId)
        {
            return await _db.DepartmentApplicationUsers
                .Where(user => user.ApplicationUserId == userId)
                .ToListAsync();
        }

        public Task<List<DepartmentApplicationUser>> GetAllByEntityId(int? entityId)
        {
            throw new System.NotImplementedException();
        }

        public Task Save(DepartmentApplicationUser departmentApplicationUser)
        {
            throw new System.NotImplementedException();
        }

        public DepartmentApplicationUser Get(int? id)
        {
            throw new System.NotImplementedException();
        }

        public async Task Add(DepartmentApplicationUser departmentApplicationUser)
        {
            await _db.DepartmentApplicationUsers.AddAsync(departmentApplicationUser);
            await _db.SaveChangesAsync();
        }

        public async Task Remove(string userid, int departmentId)
        {
            var result = await _db.DepartmentApplicationUsers.FirstOrDefaultAsync(x =>
                x.ApplicationUserId == userid && x.DepartmentId == departmentId);
            if (result != null)
            {
                _db.DepartmentApplicationUsers.Remove(result);
                await _db.SaveChangesAsync();
            }
        }

        public async Task<List<DepartmentApplicationUserViewModel>> GetViewModelsInDepartment(int departmentId)
        {
            var departmentApplicationUsers = await _db.DepartmentApplicationUsers
                .Where(x => x.DepartmentId == departmentId)
                .ToListAsync();
            return departmentApplicationUsers.Select(departmentApplicationUser => departmentApplicationUser.MapToViewModel()).ToList();
        }
        public async Task UpdateDepartmentMaintainer(string userId, int departmentId)
        {
            var currentUser = await _userManager.FindByIdAsync(userId);
            
        }
    }
    
}