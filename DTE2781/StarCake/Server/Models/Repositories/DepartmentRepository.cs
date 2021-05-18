using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Repositories
{
    public class DepartmentRepository : IDepartmentRepository
    {
        private readonly ApplicationDbContext _db;
        private readonly IHttpContextAccessor _httpContextAccessor;

        public DepartmentRepository(
            ApplicationDbContext db, IHttpContextAccessor httpContextAccessor)
        {
            _db = db;
            _httpContextAccessor = httpContextAccessor;
        }

        /// <summary>
        /// Returns ALL Departments in database
        /// </summary>
        /// <returns>async Task IEnumerable of Departments</returns>
        public async Task<IEnumerable<Department>> GetAll()
        {
            IEnumerable<Department>
                departments = await _db.Departments
                    .ToListAsync();
            return departments;
        }
        
        /// <summary>
        /// Returns all Departments for current logged in user
        /// </summary>
        /// <returns>async Task IEnumerable of Departments</returns>
        public async Task<IEnumerable<Department>> GetAllForUser()
        {
            var userId = _httpContextAccessor.HttpContext.User.FindFirst(ClaimTypes.NameIdentifier).Value;

            var departmentIds = await _db.DepartmentApplicationUsers
                .Where(x => x.ApplicationUserId == userId)
                .Select(x => x.DepartmentId)
                .ToListAsync();

            var departments = await _db.Departments
                .Where(x => departmentIds.Contains(x.DepartmentId))
                .ToListAsync();

            return departments;
        }
        
        public async Task Save(Department department)
        {
            department.DeltaCycles = 20;
            department.DeltaDays = 7;
            department.DeltaSeconds = 43200;
            
            _db.Departments.Add(department);
            await _db.SaveChangesAsync();
        }

        public async Task Update(DepartmentViewModel department)
        {
            var c = new Department
            {
                DepartmentId = department.DepartmentId,
                Name = department.Name,
                Address = department.Address,
                City = department.City,
                ZipCode = department.ZipCode,
                Email = department.Email,
                PhoneNumber = department.PhoneNumber,
                OrganizationId = department.OrganizationId,
                DeltaCycles = department.DeltaCycles,
                DeltaDays = department.DeltaDays,
                DeltaSeconds = department.DeltaSeconds,
                IsActive = department.IsActive
            };
            _db.Departments.Update(c);
             await _db.SaveChangesAsync();
        }
        
        public Department Get(int? departmentId)
        {
              var c = (from o in _db.Departments
              where o.DepartmentId == departmentId
              select o).FirstOrDefault();
            return c;
        }
        
        public async Task<DepartmentViewModel> GetViewModel(int? departmentId)
        {
            var department = await _db.Departments.FindAsync(departmentId);
            return department.MapToViewModel();
        }
    }
}