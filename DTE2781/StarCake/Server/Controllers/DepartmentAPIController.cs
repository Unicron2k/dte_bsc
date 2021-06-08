using System;
using System.Collections;
using System.Linq;
using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using StarCake.Shared;

namespace StarCake.Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    
    public class DepartmentAPIController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IDepartmentRepository _departmentRepository;
        private readonly IOrganizationRepository _organizationRepository;
        private readonly IEntityRepository _entityRepository;
        private readonly IComponentRepository _componentRepository;
        private readonly IDepartmentApplicationUserRepository _departmentApplicationUserRepository;
        private readonly IFlightLogRepository _flightLogRepository;
        private readonly UserManager<ApplicationUser> _userManager;

        public DepartmentAPIController(IDepartmentRepository departmentRepository, ApplicationDbContext context,
            IOrganizationRepository organizationRepository, IEntityRepository entityRepository, IComponentRepository componentRepository, IDepartmentApplicationUserRepository departmentApplicationUserRepository, IFlightLogRepository flightLogRepository,
            UserManager<ApplicationUser> userManager)
        {
            _departmentRepository = departmentRepository;
            _context = context;
            _userManager = userManager;
            _organizationRepository = organizationRepository;
            _entityRepository = entityRepository;
            _componentRepository = componentRepository;
            _departmentApplicationUserRepository = departmentApplicationUserRepository;
            _flightLogRepository = flightLogRepository;
        }
        
        // GET: api/DepartmentAPI
        /// <summary>
        /// Get all departmens from database
        /// </summary>
        /// <returns>Task IEnumerable Department</returns>
        [HttpGet]
        public async Task<IEnumerable<Department>> GetDepartments()
        {
            var departments = await _departmentRepository.GetAll();
            return departments;
        }
        
        /// <summary>
        /// Get all departments for current user
        /// </summary>
        /// <returns>OK-result containing all the department-view-models</returns>
        [HttpGet("all")]
        public async Task<ActionResult<IEnumerable<DepartmentViewModel>>> GetAll()
        {
            var departments = await _departmentRepository.GetAllForUser();
            var enumerable = departments.ToList();
            if (!enumerable.Any())
            {
                return NotFound();
            }
            
            var models = enumerable.Select(department => new DepartmentViewModel
                {
                    DepartmentId = department.DepartmentId,
                    Name = department.Name,
                    City = department.City,
                    Address = department.Address,
                    ZipCode = department.ZipCode,
                    Email = department.Email,
                    PhoneNumber = department.PhoneNumber,
                    DeltaCycles = department.DeltaCycles,
                    DeltaDays = department.DeltaDays,
                    DeltaSeconds = department.DeltaSeconds,
                    OrganizationId = department.OrganizationId,
                    IsActive = department.IsActive,
                    Organization = new OrganizationViewModel
                    {
                        OrganizationId = department.Organization.OrganizationId,
                        Name = department.Organization.Name,
                        City = department.Organization.City,
                        Address = department.Organization.Address,
                        ZipCode = department.Organization.ZipCode,
                        Email = department.Organization.Email,
                        PhoneNumber = department.Organization.PhoneNumber,
                        OperatorNumber = department.Organization.OperatorNumber,
                        OrganizationNumber = department.Organization.OrganizationNumber,
                        //TODO: Fetch this
                        Departments = new List<DepartmentViewModel>() { }
                    },
                    //TODO: Fetch this
                    Entities = new List<EntityViewModel>() { },
                    //TODO: Fetch this
                    DepartmentApplicationUsers = new List<DepartmentApplicationUserViewModel>() { }
                })
                .ToList();
            return Ok(models);
        }
        
        // GET: api/DepartmentAPI/{id}
        /// <summary>
        /// Get a specific department by departmentID
        /// </summary>
        /// <param name="id">ID of departmentID</param>
        /// <returns>IActionResult</returns>
        [HttpGet("{id:int}")]
        public IActionResult Get([FromRoute] int id)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            var departmentViewModel = _departmentRepository.GetViewModel(id).Result;
            departmentViewModel.Organization = _organizationRepository.GetViewModel(departmentViewModel.OrganizationId).Result;
            departmentViewModel.Entities = _entityRepository.GetViewModelsInDepartment(departmentViewModel.DepartmentId, _componentRepository, _flightLogRepository).Result;
            departmentViewModel.ArchivedComponents = _componentRepository.AllArchivedInDepartment(departmentViewModel.DepartmentId).Result;
            departmentViewModel.DepartmentApplicationUsers = _departmentApplicationUserRepository.GetViewModelsInDepartment(departmentViewModel.DepartmentId).Result;
            
            
            return Ok(departmentViewModel);
        }
        
        //PUT: api/DepartmentAPI
        /// <summary>
        /// Update the specific department
        /// </summary>
        /// <param name="department">Object departmentViewMode, passing data to update DB</param>
        /// <returns>Task IActionResult</returns>
        [HttpPut]
        public async Task<IActionResult> Put([FromBody] DepartmentViewModel department)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            try
            {
                await _departmentRepository.Update(department);
                return CreatedAtAction("Get", new {id = department.DepartmentId}, department);
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DepartmentExists(department.DepartmentId))
                {
                    return NotFound();
                }
                throw;
            }
        }
        
        
        /// <summary>
        /// API to update DepartmentInfo
        /// </summary>
        /// <param name="id">int DepartmentId from current department</param>
        /// <param name="department">Object departmentViewMode, passing data to update DB</param>
        /// <returns></returns>
        // PUT: api/DepartmentAPI/UpdateDepartmentInfo/{id}
        [HttpPut("{id:int}")]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer + "," + Roles.Admin)]
        public async Task<IActionResult> Put([FromRoute] int id, [FromBody] DepartmentViewModel department)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            if (id != department.DepartmentId)
                return BadRequest();

            try
            {
                await _departmentRepository.Update(department);
                return CreatedAtAction("Get", new { id = department.DepartmentId }, department);

            }
            catch (DbUpdateConcurrencyException)
            {
                if (!DepartmentExists(id))
                    return NotFound();
                throw;
            }
        }
        
        // POST: api/DepartmentAPI
        /// <summary>
        /// Add a new department to database
        /// </summary>
        /// <param name="department">Object Department, passing data to update DB</param>
        /// <returns></returns>
        [HttpPost]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.Admin)]
        public async Task<IActionResult> Post([FromBody] Department department)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            await _departmentRepository.Save(department);
            return CreatedAtAction("Get", new { id = department.DepartmentId }, department);
        }
        /// api/DepartmentAPI/DepartmentUser/id
        /// <summary>
        /// Add a user to another department
        /// </summary>
        /// <param name="departmentId">int departmentId, passing data to add DB</param>
        /// <param name="userId">string ApplicationUser, passing data to add DB</param>
        /// <returns>IActionResult</returns>
        [HttpPost("DepartmentUser/{departmentId:int}")]
        [Authorize(Roles = Roles.OrganizationMaintainer + "," + Roles.Admin)]
        public async Task<IActionResult> Post([FromRoute] int departmentId, [FromBody] string userId)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var user = await _userManager.FindByIdAsync(userId);
            var department = _departmentRepository.Get(departmentId);

            if (user == null)
            {
                return NotFound("User is not found");
            }

            if (department == null)
            {
                return NotFound("Department is not found");
            }

            var depUser = new DepartmentApplicationUser
            {
                ApplicationUserId = user.Id,
                DepartmentId = department.DepartmentId,
                IsMaintainer = false
            };

            await _departmentApplicationUserRepository.Add(depUser);
            return CreatedAtAction("Get", new {id = department.DepartmentId}, department);
        }
        
        //Delete
        /// <summary>
        /// Remove a users membership to a department
        /// </summary>
        /// <param name="departmentId">int departmentId, passing data to remove DB</param>
        /// <param name="userId">string userId, passing data to remove DB</param>
        /// <returns>IActionResult</returns>
        [HttpPost("DepartmentUserDelete/{departmentId:int}")]
        [Authorize(Roles = Roles.OrganizationMaintainer + "," + Roles.Admin)]
        public async Task<IActionResult> Delete([FromRoute] int departmentId, [FromBody] string userId)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var user = await _userManager.FindByIdAsync(userId);
            var department = _departmentRepository.Get(departmentId);

            if (user == null)
            {
                return NotFound("User is not found");
            }

            if (department == null)
            {
                return NotFound("Department is not found");
            }

            var depUser = new DepartmentApplicationUser
            {
                ApplicationUserId = user.Id,
                DepartmentId = department.DepartmentId,
                IsMaintainer = false
            };

            await _departmentApplicationUserRepository.Remove(userId, department.DepartmentId);
            return CreatedAtAction("Get", new {id = department.DepartmentId}, department);
        }

        private bool DepartmentExists(int id)
        {
            return _context.Departments.Any(e => e.DepartmentId == id);
        }
        
    }
}