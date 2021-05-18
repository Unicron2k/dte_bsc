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
    public class EntityTypeController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IEntityTypeRepository _repository;

        public EntityTypeController(ApplicationDbContext context, IEntityTypeRepository repository)
        {
            _context = context;
            _repository = repository;
        }
        
        
        // GET: api/EntityType
        [HttpGet]
        public async Task<IEnumerable<EntityType>> GetEntityTypes()
        {
            IEnumerable<EntityType> entityTypes = await _context.EntityTypes.ToListAsync();
            return entityTypes;
        }
        
        // GET: api/EntityType/
        [HttpGet("{id}")]
        public async Task<ActionResult<EntityType>> GetEntityType(int id)
        {
            var entityType = await _context.EntityTypes.FindAsync(id);
            if (entityType == null)
                return NotFound();
            return entityType;
        }
        
        // POST: api/EntityType
        [HttpPost]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<ActionResult<EntityType>> PostEntityType(EntityType entityType)
        {
            await _context.EntityTypes.AddAsync(entityType);
            await _context.SaveChangesAsync();
            return CreatedAtAction("GetEntityType", new { id = entityType.EntityTypeId }, entityType);
        }
        
        //PUT: api/EntityType/{id}
        [HttpPut("{id}")]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<IActionResult> Put([FromRoute] int id, [FromBody] EntityTypeViewModel entityType)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != entityType.EntityTypeId)
            {
                return BadRequest();
            }

            try
            {
                await _repository.UpdateEntityType(entityType);
                return CreatedAtAction("GetEntityType", new {id = entityType.EntityTypeId}, entityType);
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!EntityTypeExists(id))
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
        
        private bool EntityTypeExists(int id)
        {
            return _context.EntityTypes.Any(e => e.EntityTypeId == id);
        }
    }
}
