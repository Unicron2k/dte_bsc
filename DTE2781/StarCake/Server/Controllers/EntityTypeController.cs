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
        /// <summary>
        /// Get all entitytpes from database
        /// </summary>
        /// <returns></returns>
        [HttpGet]
        public async Task<IEnumerable<EntityType>> GetEntityTypes()
        {
            IEnumerable<EntityType> entityTypes = await _repository.GetAll();
            return entityTypes;
        }
        
        // GET: api/EntityType/
        /// <summary>
        /// Get a specific entity type
        /// </summary>
        /// <param name="id">ID of entity type</param>
        /// <returns>entity type</returns>
        [HttpGet("{id}")]
        public async Task<ActionResult<EntityType>> GetEntityType(int? id)
        {
            var entityType = await _repository.GetEntityType(id);
            if (entityType == null)
                return NotFound();
            return entityType;
        }
        
        // POST: api/EntityType
        /// <summary>
        /// Add a new entity type to the database
        /// </summary>
        /// <param name="entityType">Data to post database</param>
        /// <returns></returns>
        [HttpPost]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<IActionResult> PostEntityType(EntityType entityType)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (entityType == null)
            {
                return NotFound();
            }

            await _repository.SaveEntityType(entityType);
            return CreatedAtAction("GetEntityType", new { id = entityType.EntityTypeId }, entityType);
        }
        
        //PUT: api/EntityType/{id}
        /// <summary>
        /// Update a specific entity type
        /// </summary>
        /// <param name="id">ID of entity type</param>
        /// <param name="entityType">Data of new values to update</param>
        /// <returns></returns>
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
