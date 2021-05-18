using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Controllers
{
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
        [HttpGet]
        public async Task<IEnumerable<ComponentType>> GetComponentTypes()
        {
            IEnumerable<ComponentType> componentTypes = await _context.ComponentTypes.ToListAsync();
            return componentTypes;
        }
        
        // GET: api/ComponentType/
        [HttpGet("{id}")]
        public async Task<ActionResult<ComponentType>> GetComponentType(int id)
        {
            var componentType = await _context.ComponentTypes.FindAsync(id);
            if (componentType == null)
                return NotFound();
            return componentType;
        }
        
        // POST: api/ComponentType
        [HttpPost]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<ActionResult<ComponentType>> PostComponentType(ComponentType componentType)
        {
            await _context.ComponentTypes.AddAsync(componentType);
            await _context.SaveChangesAsync();
            return CreatedAtAction("GetComponentType", new { id = componentType.ComponentTypeId }, componentType);
        }
        
        //PUT: api/ComponentType/{id}
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
                return CreatedAtAction("GetComponentType", new {id = componentType.ComponentTypeId}, componentType);

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
