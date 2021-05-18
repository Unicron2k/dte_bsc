using System;
using System.Linq;
using System.Collections.Generic;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Internal;
using StarCake.Shared;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    
    public class ApplicationUserAPIController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IApplicationUserRepository _repository;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly HttpContext _httpContext;

        public ApplicationUserAPIController(IApplicationUserRepository repository,
            UserManager<ApplicationUser> userManager,
            IHttpContextAccessor httpContextAccessor, ApplicationDbContext context, IDepartmentApplicationUserRepository repositoryDepartmentApplication)
        {
            _repository = repository;
            _userManager = userManager;
            _context = context;
            _httpContext = httpContextAccessor?.HttpContext;
        }
        
        // GET: api/ApplicationUserAPI/all
        [HttpGet("all")]
        public async Task<List<ApplicationUserViewModel>> GetApplicationUsers()
        {
            var users = await _repository.GetAll();
            return users.Select(user => new ApplicationUserViewModel
            {
                UserName = user.UserName,
                FirstName = user.FirstName,
                LastName = user.LastName,
                LockoutEnd = user.LockoutEnd,
                CurrentLoggedInDepartmentId = user.CurrentLoggedInDepartmentId,
                IsOranizationMaintainer = user.IsOrganizationMaintainer,
                Id = user.Id,
            }).ToList();
        }
        
        [HttpGet]
        [Authorize]
        //For no ID given, return current ApplicationUser
        public ActionResult<ApplicationUserViewModel> Get()
        {
            var userId = HttpContext.User.Claims.FirstOrDefault(x => x.Type == ClaimTypes.NameIdentifier)?.Value;
            var applicationUser = _repository.Get(userId);
            if (applicationUser == null)
            {
                return NotFound();
            }

            var model = new ApplicationUserViewModel
            {
                Id = applicationUser.Id,
                CreationDate = applicationUser.CreationDate,
                UserName = applicationUser.UserName,
                FirstName = applicationUser.FirstName,
                LastName = applicationUser.LastName, 
                Departments = null,
                CurrentLoggedInDepartmentId = applicationUser.CurrentLoggedInDepartmentId,
                LockoutEnd = applicationUser.LockoutEnd,
                IsOranizationMaintainer = applicationUser.IsOrganizationMaintainer,
                CanTrack = applicationUser.CanTrack
            };

            return model;
        }
        
        
        // GET: api/ApplicationUserAPI/{id}
        [HttpGet("{id}")]
        public IActionResult Get([FromRoute] string id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            var applicationUser = _repository.Get(id);
            if (applicationUser == null)
            {
                return NotFound();
            }
            return Ok(applicationUser);
        }

        // GET: api/ApplicationUserAPI/NAME/{id}
        [HttpGet("NAME/"+"{id}")]
        public IActionResult GetName([FromRoute] string id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            string name = _repository.GetName(id);
            if (name == null)
                return NotFound();
            return Ok(name);
        }

        // PUT: api/ApplicationUserAPI/CURRENTDEPARTMENTID/{id}
        [HttpPut("CURRENTDEPARTMENTID/"+"{id}")]
        public async Task<IActionResult> Put([FromRoute] string id, [FromBody] int departmentId)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
        
            //if (id != applicationUser.Id)
            //{
            //    return BadRequest();
            //}
        
            try
            {
                await _repository.UpdateCurrentDepartmentId(id, departmentId);
                return CreatedAtAction("Get", new {id}, departmentId);
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ApplicationUserExists(id))
                {
                    return NotFound();
                }
                throw;
            }
        }
        
        // POST: api/ApplicationUserAPI
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] ApplicationUser applicationUser)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            await _repository.Save(applicationUser);
            return CreatedAtAction("Get", new { id = applicationUser.Id }, applicationUser);
        }

        /// <summary>
        /// Change the department of current logged in user
        /// </summary>
        /// <param name="id">ID of department to change to</param>
        /// <returns>Awaitable task IActionResult Status</returns>
        [HttpGet("ChangeDepartment/{id:int}")]
        public async Task<IActionResult> ChangeDepartment(int id)
        {
            if (id<1)
                return BadRequest("Invalid DepartmentID given");
            if (!_repository.IsInDepartment(id))
                return Unauthorized("You are not allowed to access this resource");
            await _repository.ChangeDepartment(id);
            return Ok("Department changed");
        }
        
        /// <summary>
        /// API to Disable or activate a account
        /// </summary>
        /// <param name="userId">string userId from current user to activate or disable</param>
        /// <param name="disable">bool disable, decide oon the user to activate or disable</param>
        /// <returns></returns>
        [HttpPut("DisableUser/{userId}")]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<IActionResult> DisableUser([FromRoute] string userId, [FromBody] bool disable)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            
            try
            {
                await _repository.DisableUserLockEndDate(userId, disable);
                var currentUser = _repository.Get(userId);
                return CreatedAtAction("Get", new {id = currentUser.Id}, currentUser);
            }
            catch (DbUpdateConcurrencyException e)
            {
                if (!ApplicationUserExists(userId))
                {
                    return NotFound();
                }

                throw;
            }

            return null;
        }
        
        /// <summary>
        /// Update the Department maintainer with current user.
        /// Update the AspNetRoles and isMaintainer in DepartmentApplicationUser
        /// </summary>
        /// <param name="departmentApplicationUserViewModel"></param>
        /// <returns></returns>
        //api/ApplicationUserAPI/DMaintainerUpdate/
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer)]
        [HttpPut("DMaintainerUpdate/")]
        public async Task<IActionResult> DMaintainerUpdate(
            [FromBody] DepartmentApplicationUserViewModel departmentApplicationUserViewModel)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            await _repository.UpdateMaintainDepartment(departmentApplicationUserViewModel);
                return Ok(departmentApplicationUserViewModel);
                
                
        }
        /// <summary>
        /// Update the Organization maintainer with current user
        /// Update cuser into AspNetRoles
        /// </summary>
        /// <param name="departmentApplicationUserViewModel"></param>
        /// <returns></returns>
        //api/ApplicationUserAPI/OMaintainerUpdate
        [HttpPut("OMaintainerUpdate/")]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.Admin)]
        public async Task<IActionResult> OMaintainerUpdate(
            [FromBody] DepartmentApplicationUserViewModel departmentApplicationUserViewModel)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            

            await _repository.UpdateOrganizationMaintainer(departmentApplicationUserViewModel);
            return Ok(departmentApplicationUserViewModel);


        }
        private bool ApplicationUserExists(string id)
        {
            return _context.ApplicationUsers.Any(e => e.Id == id);
        }
    }
}