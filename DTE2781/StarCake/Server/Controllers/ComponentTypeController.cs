using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Controllers
{
    [Authorize]
    [Route("api/[controller]")]
    [ApiController]
    public class ComponentTypeController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IComponentTypeRepository _repository;

        public ComponentTypeController(ApplicationDbContext context, IComponentTypeRepository repository)
        {
            _context = context;
            _repository = repository;
        }
        
        
        // GET: api/ComponentType
        /// <summary>
        /// Get all ComponentTypes from database
        /// </summary>
        /// <returns>Task IEnumerable</returns>
        [HttpGet]
        public async Task<IEnumerable<ComponentType>> GetComponentTypes()
        {
            IEnumerable<ComponentType> componentTypes = await _repository.GetAll();
            return componentTypes;
        }
        
        // GET: api/ComponentType/
        /// <summary>
        /// Get a specific component type
        /// </summary>
        /// <param name="id">ID of componenttype</param>
        /// <returns>ComponentType</returns>
        [HttpGet("{id}")]
        [Authorize(Roles = Roles.DepartmentMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<ActionResult<ComponentType>> GetEntityType(int? id)
        {
            if (id == null)
            {
                return NotFound("Bad parameter");
            }
            
            var componentType = await _repository.GetComponentType(id);
            if (componentType == null)
                return NotFound();
            return componentType;
        }
        
 /// <summary>
 /// Add a new ComponentType to Database
 /// </summary>
 /// <param name="componentType">Object ComponentType, passing new data to DB</param>
 /// <returns>IActionResult</returns>
        [HttpPost]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<IActionResult> PostComponentType([FromBody] ComponentType componentType)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            await _repository.SaveComponentType(componentType);
            
            return CreatedAtAction("GetEntityType", new { id = componentType.ComponentTypeId }, componentType);
        }
        
        
        //PUT: api/ComponentType/{id}
        /// <summary>
        /// Update a specific component type
        /// </summary>
        /// <param name="id">ID of component type</param>
        /// <param name="componentType">Data of new values to update</param>
        /// <returns>TaskIActionResult</returns>
        [HttpPut("{id}")]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<IActionResult> Put([FromRoute] int id, [FromBody] ComponentTypeViewModel componentType)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != componentType.ComponentTypeId)
            {
                return BadRequest();
            }

            try
            {
                await _repository.UpdateComponentType(componentType);
                return CreatedAtAction("GetEntityType", new {id = componentType.ComponentTypeId}, componentType);

            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ComponentTypeExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }


        private bool ComponentTypeExists(int id)
        {
            return _context.ComponentTypes.Any(e => e.ComponentTypeId == id);
        }
    }
}
