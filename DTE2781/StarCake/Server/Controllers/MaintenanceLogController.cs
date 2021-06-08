using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;
using StarCake.Shared.Models.ViewModels.Maintenance;

namespace StarCake.Server.Controllers
{ 
    [ApiController, Route("api/[controller]"), Authorize]
    public class MaintenanceLogController : ControllerBase
    {
        private readonly IMaintenanceLogRepository _maintenanceLogRepository;
        private readonly IDepartmentRepository _departmentRepository;
        private readonly IEntityRepository _entityRepository;
        private readonly IComponentRepository _componentRepository;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IHttpContextAccessor _httpContextAccessor;

        public MaintenanceLogController(IMaintenanceLogRepository maintenanceLogRepository, UserManager<ApplicationUser> userManager, IHttpContextAccessor httpContextAccessor, IDepartmentRepository departmentRepository, IEntityRepository entityRepository, IComponentRepository componentRepository)
        {
            _maintenanceLogRepository = maintenanceLogRepository;
            _userManager = userManager;
            _httpContextAccessor = httpContextAccessor;
            _departmentRepository = departmentRepository;
            _entityRepository = entityRepository;
            _componentRepository = componentRepository;
        }

        /// <summary>
        /// Get a specific maintenance-log
        /// </summary>
        /// <param name="id">ID of maintenance-log</param>
        /// <returns>Task MaintenanceLogViewModel</returns>
        [HttpGet("{id:int}")]
        public async Task<MaintenanceLogViewModel> Get([FromRoute] int id)
        {
            var userId = _httpContextAccessor.HttpContext.User.FindFirst(ClaimTypes.NameIdentifier).Value;
            var user = await _userManager.FindByIdAsync(userId);
            var entity = await _maintenanceLogRepository.Get(id);
            return entity?.DepartmentId==user.CurrentLoggedInDepartmentId ? ToModel(entity) : new MaintenanceLogViewModel();
        }

        /// <summary>
        /// Gets all logs for the current department in which the user is logged in to
        /// </summary>
        /// <returns>Task IEnumerable</returns>
        [HttpGet("GetAll")]
        public async Task<IEnumerable<MaintenanceLogViewModel>> GetAll()
        {
            var userId = _httpContextAccessor.HttpContext.User.FindFirst(ClaimTypes.NameIdentifier).Value;
            var user = await _userManager.FindByIdAsync(userId);
            var logs = await _maintenanceLogRepository.GetAllInDepartment(user.CurrentLoggedInDepartmentId);
            return logs.Select(ToModel).ToList();
        }

        /// <summary>
        /// Save new maintenance-log to the database
        /// </summary>
        /// <param name="model">MaintenanceLogViewModel</param>
        /// <returns>Task IActionResult</returns>
        [HttpPost]
        public async Task<IActionResult> Save(MaintenanceLogItemViewModel model)
        {
            //TODO: Check all provided data
            var userId = _httpContextAccessor.HttpContext.User.FindFirst(ClaimTypes.NameIdentifier).Value;
            var user = await _userManager.FindByIdAsync(userId);
            var entity = _entityRepository.Get(model.EntityId);
            if(user.CurrentLoggedInDepartmentId!=entity.DepartmentId)
                return Unauthorized("You are not authorized to perform this action");
            
            var component = model.ComponentId > 0
                ? entity.Components.FirstOrDefault(c => c.ComponentId == model.ComponentId)
                : null;
            
            var logEntity = new MaintenanceLog
            {
                Date = DateTime.UtcNow,
                //sett ACCSeconds to totalFlightTime for the object we create a log-entry for. If componentID==0, use Entity-ACC, otherwise use Component-ACC
                ACCSeconds = model.ComponentId==0?entity.TotalFlightDurationInSeconds:component.TotalFlightDurationInSeconds,
                TaskDescription = model.TaskDescription,
                ActionDescription = model.ActionDescription,
                ApplicationUserIdLogged = user.Id,
                ApplicationUserLogged = user,
                DepartmentId = user.CurrentLoggedInDepartmentId,
                Department = _departmentRepository.Get(user.CurrentLoggedInDepartmentId),
                EntityId = entity.EntityId,
                Entity = entity,
                ComponentId = model.ComponentId == 0 ? (int?) null : model.ComponentId,
                //Set the component only if componentID>0. if ComponentID==0, then it is the entity whe have performed maintenance on.
                Component = model.ComponentId == 0 ? null : component
            };
            await _maintenanceLogRepository.Save(logEntity);
            
            if (logEntity.MaintenanceLogId < 1)
                return StatusCode(500, "Unable to save maintenance-log");

            //if componentID>0, assume we have maintained a component
            if (component!=null)
            {
                //reset maintenance-data for component
                component.LastMaintenanceDate = DateTime.UtcNow;
                component.CyclesSinceLastMaintenance = 0;
                component.FlightSecondsSinceLastMaintenance = 0;
                await _componentRepository.Update(component);
            }
            //if componentID==0, assume we have maintained an entire entity
            else
            {
                //reset maintenance-data for entity
                entity.LastMaintenanceDate=DateTime.UtcNow;
                entity.CyclesSinceLastMaintenance = 0;
                entity.FlightSecondsSinceLastMaintenance = 0;
                await _entityRepository.Update(entity);
                
                //Iterate through all components and reset maintenance-data
                foreach (var entityComponent in entity.Components)
                {
                    entityComponent.LastMaintenanceDate = DateTime.UtcNow;
                    entityComponent.CyclesSinceLastMaintenance = 0;
                    entityComponent.FlightSecondsSinceLastMaintenance = 0;
                    await _componentRepository.Update(entityComponent);
                }
            }

            return Ok("Maintenance-log saved successfully");

        }

        /// <summary>
        /// Convert from entity to model, 1 level deep
        /// </summary>
        /// <param name="entity">MaintenanceLog</param>
        /// <returns>MaintenanceLogViewModel</returns>
        private static MaintenanceLogViewModel ToModel(MaintenanceLog entity)
        {
            //TODO: Fix this atrocity!!!
            return new MaintenanceLogViewModel
            {
                MaintenanceLogId = entity.MaintenanceLogId,
                Date = entity.Date,
                ACCSeconds = entity.ACCSeconds,
                TaskDescription = entity.TaskDescription,
                ActionDescription = entity.ActionDescription,
                ApplicationUserIdLogged = entity.ApplicationUserIdLogged,
                ApplicationUserLogged = new ApplicationUserViewModel
                {
                    Id = entity.ApplicationUserLogged.Id,
                    CreationDate = entity.ApplicationUserLogged.CreationDate,
                    UserName = entity.ApplicationUserLogged.UserName,
                    FirstName = entity.ApplicationUserLogged.FirstName,
                    LastName = entity.ApplicationUserLogged.LastName,
                    LockoutEnd = entity.ApplicationUserLogged.LockoutEnd,
                    Departments = null,
                    CurrentLoggedInDepartmentId = entity.ApplicationUserLogged.CurrentLoggedInDepartmentId,
                    CurrentDepartment = null,
                    DepartmentApplicationUsers = null,
                    IsOranizationMaintainer = false
                },
                DepartmentId = entity.DepartmentId,
                Department = new DepartmentViewModel
                {
                    DepartmentId = entity.Department.DepartmentId,
                    Name = entity.Department.Name,
                    City = entity.Department.City,
                    Address = entity.Department.Address,
                    ZipCode = entity.Department.ZipCode,
                    Email = entity.Department.Email,
                    PhoneNumber = entity.Department.PhoneNumber,
                    DeltaCycles = entity.Department.DeltaCycles,
                    DeltaDays = entity.Department.DeltaDays,
                    DeltaSeconds = entity.Department.DeltaSeconds,
                    IsActive = entity.Department.IsActive,
                    OrganizationId = entity.Department.OrganizationId,
                    Organization = null,
                    Entities = null,
                    ArchivedComponents = null,
                    DepartmentApplicationUsers = null,
                    MaintenanceLogs = null
                },
                EntityId = entity.EntityId,
                Entity = new EntityViewModel
                {
                    EntityId = entity.Entity.EntityId,
                    Name = entity.Entity.Name,
                    EntityTypeId = entity.Entity.EntityTypeId,
                    IsArchived = false,
                    DepartmentId = entity.Entity.DepartmentId,
                    ManufacturerId = entity.Entity.ManufacturerId,
                    SerialNumber = entity.Entity.SerialNumber,
                    CreationDate = entity.Entity.CreationDate,
                    TotalFlightCycles = entity.Entity.TotalFlightCycles,
                    TotalFlightDurationInSeconds = entity.Entity.TotalFlightCycles,
                    MaxCyclesBtwMaintenance = entity.Entity.MaxCyclesBtwMaintenance,
                    MaxDaysBtwMaintenance = entity.Entity.MaxDaysBtwMaintenance,
                    MaxFlightSecondsBtwMaintenance = entity.Entity.MaxFlightSecondsBtwMaintenance,
                    RequiresMaintenance = false,
                    RequiresImmediateMaintenance = false,
                    LastMaintenanceDate = entity.Entity.LastMaintenanceDate,
                    CyclesSinceLastMaintenance = entity.Entity.CyclesSinceLastMaintenance,
                    FlightSecondsSinceLastMaintenance = entity.Entity.FlightSecondsSinceLastMaintenance,
                    Components = null,
                    FlightLogs = null,
                    FlightLogCount = 0,
                    HasComponentRequiringMaintenance = false
                },
                ComponentId = entity.ComponentId ?? 0,
                Component = entity.Component==null?new ComponentViewModel():new ComponentViewModel
                {
                    ComponentId = entity.Component.ComponentId,
                    Name = entity.Component.Name,
                    EntityId = entity.Component.EntityId,
                    ComponentTypeId = entity.Component.ComponentTypeId,
                    ManufacturerId = entity.Component.ManufacturerId,
                    DepartmentId = entity.Component.DepartmentId,
                    CreationDate = entity.Component.CreationDate,
                    TotalFlightCycles = entity.Component.TotalFlightCycles,
                    TotalFlightDurationInSeconds = entity.Component.TotalFlightDurationInSeconds,
                    SerialNumber = entity.Component.SerialNumber,
                    CyclesSinceLastMaintenance = entity.Component.CyclesSinceLastMaintenance,
                    FlightSecondsSinceLastMaintenance = entity.Component.FlightSecondsSinceLastMaintenance,
                    LastMaintenanceDate = entity.Component.LastMaintenanceDate,
                    MaxCyclesBtwMaintenance = entity.Component.MaxCyclesBtwMaintenance,
                    MaxDaysBtwMaintenance = entity.Component.MaxDaysBtwMaintenance,
                    MaxFlightSecondsBtwMaintenance = entity.Component.MaxFlightSecondsBtwMaintenance,
                    RequiresMaintenance = false,
                    RequiresImmediateMaintenance = false
                }
            };

        }
    }
}