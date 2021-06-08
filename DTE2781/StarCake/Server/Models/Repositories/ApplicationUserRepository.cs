using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Security.Principal;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Repositories
{
    public class ApplicationUserRepository : IApplicationUserRepository
    {
        private readonly ApplicationDbContext _db;
        private readonly IHttpContextAccessor _httpContextAccessor;
        private readonly UserManager<ApplicationUser> _userManager;

        public ApplicationUserRepository(ApplicationDbContext db, IHttpContextAccessor httpContextAccessor, UserManager<ApplicationUser> userManager)
        {
            _db = db;
            _httpContextAccessor = httpContextAccessor;
            _userManager = userManager;
        }

        /// <summary>
        /// Get all ApplicationUsers in database
        /// Returns an IEnumerable<ApplicationUser>.
        /// Must be awaited.
        /// 
        /// Consider using the UserManager or a LINQ-expression to fetch only relevant records
        /// </summary>
        /// <returns>Awaitable task</returns>
        public async Task<IEnumerable<ApplicationUser>> GetAll()
        {
            IEnumerable<ApplicationUser>
                countries = await _db.ApplicationUsers
                    .ToListAsync();
            return countries;
        }

        //Dangerous, should really only be handled by the user-manager!!!
        /// <summary>
        /// Save the supplied ApplicationUser to database.
        /// Must be awaited.
        /// </summary>
        /// <param name="applicationUser"></param>
        /// <returns>Awaitable task</returns>
        public async Task Save(ApplicationUser applicationUser)
        {
            await _db.ApplicationUsers.AddAsync(applicationUser);
            await _db.SaveChangesAsync();
        }

        /// <summary>
        /// Check if current user is in given department
        /// </summary>
        /// <param name="departmentId">Id of department the department we wish to check</param>
        /// <returns>True if user is in the department, false for any other</returns>
        public bool IsInDepartment(int departmentId)
        {
            if (departmentId<1) return false;
            var userId = _httpContextAccessor.HttpContext.User.FindFirst(ClaimTypes.NameIdentifier).Value;
            var departmentIds =  _db.DepartmentApplicationUsers
                .Where(x => x.ApplicationUserId == userId)
                .Select(x => x.DepartmentId)
                .ToList();
            return departmentIds.Contains(departmentId);
        }

        /// <summary>
        /// Change change the department the user is currently logged in to.
        /// Must be awaited.
        /// </summary>
        /// <param name="departmentId"></param>
        /// <returns>Awaitable task</returns>
        public async Task ChangeDepartment(int departmentId)
        {
            try
            {

                var userId = _httpContextAccessor.HttpContext.User.FindFirst(ClaimTypes.NameIdentifier).Value;
                var currentUser = await _userManager.FindByIdAsync(userId);
                currentUser.CurrentLoggedInDepartmentId = departmentId;

                _db.ApplicationUsers.Update(currentUser);
                await _db.SaveChangesAsync();
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                throw;
            }
        }

        public async Task UpdateCurrentDepartmentId(string applicationUserId, int departmentId)
        {
            // TODO: Optimize by only passing string Id and int DepartmentId from WebAPI
            //var c = new ApplicationUser()
            //{
            //    Id = applicationUser.Id,
            //    CurrentLoggedInDepartmentId = applicationUser.CurrentLoggedInDepartmentId
            //};
            var user = new ApplicationUser()
                {Id = applicationUserId, CurrentLoggedInDepartmentId = departmentId};
            await using (_db)
            {
                _db.ApplicationUsers.Attach(user);
                _db.Entry(user).Property(x => x.CurrentLoggedInDepartmentId).IsModified = true;
                 await _db.SaveChangesAsync();
            }
        }

        /// <summary>
        /// When to activate or disable a account with two parameters.
        /// When disable is _TRUE_ will the account get a LockoutEndDate to MaxValue.
        /// Either disable is false will the account get a LockOutEndDate to NULL.
        /// </summary>
        /// <param name="userId">String for userId to get currentUser object</param>
        /// <param name="disable">Boolen to activate or disable accont</param>
        /// <returns></returns>
        public async Task DisableUserLockEndDate(string userId, bool disable)
        {
            //Find the user-object to change Lockenddatae
            var currentUserToUpdate = await _userManager.FindByIdAsync(userId);

            //If Disable is true, sett max value in LockEndDate, if not. Putt NULL into LockEndDate then user will became active
            if (disable)
            {
                currentUserToUpdate.LockoutEnd = DateTimeOffset.MaxValue;
                currentUserToUpdate.LockoutEnabled = true;
                
                //Update to database
                _db.ApplicationUsers.Update(currentUserToUpdate);
                await _db.SaveChangesAsync();
            }
            else
            {
                currentUserToUpdate.LockoutEnd = null;

                _db.ApplicationUsers.Update(currentUserToUpdate);
                await _db.SaveChangesAsync();
            }
        }

        /// <summary>
        /// Get the ApplicationUser specified by the id.
        /// If you are using ths, you either don't know what you're doing,
        /// or you know _exactly_ what you're doing
        /// 
        /// Consider using the UserManager to fetch the user.
        /// </summary>
        /// <param name="id">ApplicationUserId-string</param>
        /// <returns>ApplicationUser</returns>
        public ApplicationUser Get(string id)
        {
            var c = (from o in _db.ApplicationUsers
                where o.Id == id
                select o).FirstOrDefault();
            return c;
        }
        
        /// <summary>
        /// Get username for the ApplicationUser specified by the id.
        /// If you are using ths, you either don't know what you're doing,
        /// or you know _exactly_ what you're doing
        /// 
        /// Consider using the UserManager to fetch the username.
        /// </summary>
        /// <param name="id">ApplicationUserId-string</param>
        /// <returns>string</returns>
        public string GetName(string? id)
        {
            var name = (from o in _db.ApplicationUsers
                where o.Id == id
                select o.UserName).FirstOrDefault();
            return name;
        }

        /// <summary>
        /// Get all departments for a user with a given id
        /// </summary>
        /// <param name="id">ApplicationUser.Id</param>
        /// <returns>IEnumerable async Department </returns>
        /*
        public async Task<IEnumerable<Department>> GetDepartments(string id)
        {
            List<DepartmentApplicationUser> departmentApplicationUsers;
            departmentApplicationUsers = _db.DepartmentApplicationUsers.Where(x => x.ApplicationUserId == id).ToList();
            int[] departmentIds = new int[departmentApplicationUsers.Count];
            foreach (var departmentApplication in departmentApplicationUsers)
                departmentIds = departmentIds.Append(departmentApplication.DepartmentId).ToArray();
            IEnumerable<Department> departments = await _db.Departments.Where(x => departmentIds.Contains(x.DepartmentId)).ToListAsync();
            return departments;
        }*/
        
        public Task<IEnumerable<Department>> GetDepartments()
        {
            throw new NotImplementedException();
        }
        
        
        public Task Add(ApplicationUser c, IPrincipal principal)
        {
            throw new NotImplementedException();
        }
        
        public async Task Remove(ApplicationUser c)
        {
            _db.ApplicationUsers.Remove(c);
            await _db.SaveChangesAsync();
        }

        /// <summary>
        /// When user is granted to be a maintainer to current Department
        /// Update isMaintainer in DepartmentApplicationUsers to current Department
        /// Add DepartmentRoles to the Current user.
        ///
        /// When remove Department Maintainer roles, will remove isMaintainer and Role.
        /// </summary>
        /// <param name="departmentApplicationUserView">Vale with isMaintainer, Department and username</param>
        public async Task UpdateMaintainDepartment(DepartmentApplicationUserViewModel departmentApplicationUserView)
        {
            
            //Update the DepartmentApplicationUser to isMaintainer to current user
            DepartmentApplicationUser departmentApplicationUser = new DepartmentApplicationUser
            {
                ApplicationUserId = departmentApplicationUserView.ApplicationUserId,
                DepartmentId = departmentApplicationUserView.DepartmentId,
                IsMaintainer = departmentApplicationUserView.IsMaintainer
            };
            _db.DepartmentApplicationUsers.Update(departmentApplicationUser);
            await _db.SaveChangesAsync();
            
            var currentUser = _userManager.FindByIdAsync(departmentApplicationUser.ApplicationUserId).Result;
            var roleName = Roles.DepartmentMaintainer;
            
            //If user is not in DepartmentRoles so add to role or removce. 
            if (departmentApplicationUser.IsMaintainer)
            {
                var isInRole = await _userManager.IsInRoleAsync(currentUser, "Department Maintainer");

                if (!isInRole)
                {
                    await _userManager.AddToRoleAsync(currentUser, roleName);
                }
            }
            else
            {
                var isUserMaintainer = _db.DepartmentApplicationUsers.Any(x =>
                    x.IsMaintainer && x.ApplicationUserId == currentUser.Id);
                if (!isUserMaintainer)
                {
                    await _userManager.RemoveFromRoleAsync(currentUser, roleName);
                }
            }
        }

        /// <summary>
        /// Set a user to OrganizationMaintainer.
        /// The user will add to DepartmentApplicationUser as maintainer by all department in current organization.
        ///  
        /// </summary>
        /// <param name="departmentApplicationUserViewModel"></param>
        public async Task UpdateOrganizationMaintainer(DepartmentApplicationUserViewModel departmentApplicationUserViewModel)
        {
           
            try
            {
                //Update IsOrganizationMaintainer in AspNetUser table
                var currentUser = _userManager.FindByIdAsync(departmentApplicationUserViewModel.ApplicationUserId).Result;
                currentUser.IsOrganizationMaintainer = departmentApplicationUserViewModel.ApplicationUser.IsOranizationMaintainer;
                var currentDepartment = _db.Departments.FirstOrDefaultAsync(x =>
                    x.DepartmentId == departmentApplicationUserViewModel.DepartmentId).Result;

                //TODO: Remove or fix. Når man fjerner brukeren fra DepartmentApplicaction, så har bruekrne ingen department og forsvinner fra nettsiden. 
                /**
                var departmens =
                     _db.Departments.Where(x => x.OrganizationId == currentDepartment.OrganizationId).ToList();
                foreach (var jaja in departmens)
                {
                    DepartmentApplicationUser departmentApplicationUser = new DepartmentApplicationUser
                    {
                        ApplicationUserId = currentUser.Id,
                        DepartmentId = jaja.DepartmentId,
                        IsMaintainer = departmentApplicationUserViewModel.ApplicationUser.IsOranizationMaintainer
                    };
                    if (departmentApplicationUserViewModel.ApplicationUser.IsOranizationMaintainer)
                    {
                        _db.DepartmentApplicationUsers.Update(departmentApplicationUser);
                        await _db.SaveChangesAsync();
                    }
                    else
                    {
                        _db.DepartmentApplicationUsers.Remove(departmentApplicationUser);
                        await _db.SaveChangesAsync();
                    }
                      }
                    **/
                   
              
                _db.ApplicationUsers.Update(currentUser);
                await _db.SaveChangesAsync();
                
                //Update AspNetUserRoles
                var isMaintainer = departmentApplicationUserViewModel.ApplicationUser.IsOranizationMaintainer;
                var roleName = Roles.OrganizationMaintainer;
                if (isMaintainer)
                {
                    await _userManager.AddToRoleAsync(currentUser, roleName);
                }
                else
                {
                    await _userManager.RemoveFromRoleAsync(currentUser, roleName);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                throw;
            }
            
        }

        public ApplicationUserViewModel GetApplicationUserViewModel(string? id)
        {
            ApplicationUserViewModel c;
            if (id == null)
            {
                c = new ApplicationUserViewModel();
            }
            else
            {
                c = (from o in _db.ApplicationUsers
                        where o.Id == id
                        select new ApplicationUserViewModel()
                        {
                            Id = o.Id,
                            UserName = o.UserName
                            // TODO: Ensure this is enough fields..
                        }
                    ).FirstOrDefault();
            }
            return c;
        }
    }
}