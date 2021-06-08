using System;
using System.Collections;
using System.Linq;
using System.Collections.Generic;
using System.Text.Json;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    
    public class EntityAPIController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IEntityRepository _repository;

        public EntityAPIController(IEntityRepository repository, ApplicationDbContext context)
        {
            _repository = repository;
            _context = context;
        }

        // GET: api/EntityAPI
        /// <summary>
        /// Get all Entities from database
        /// </summary>
        /// <returns>IENumerable of Entities</returns>
        [HttpGet]
        public async Task<IEnumerable<Entity>> GetEntities()
        {
            return await _repository.GetAll();;
        }
        
        // GET: api/EntityAPI/{id}
        /// <summary>
        /// Get a specific Entity from database
        /// </summary>
        /// <param name="id">int EntityID</param>
        /// <returns>IActionResult Enitiy</returns>
        [HttpGet("{id:int}")]
        public IActionResult Get([FromRoute] int id)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            var entity = _repository.Get(id);
            if (entity == null)
                return NotFound();
            return Ok(entity);
        }
        
        // PUT: api/EntityAPI/{id}
        /// <summary>
        /// Update the specific entity
        /// </summary>
        /// <param name="id">int EntityId</param>
        /// <param name="entity">Object Entity, passing to update DB</param>
        /// <returns>IActionResult</returns>
        [HttpPut("{id:int}")]
        public async Task<IActionResult> Put([FromRoute] int id, [FromBody] Entity entity)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            if (id != entity.EntityId)
                return BadRequest();
            try
            {
                await _repository.Update(entity);
                return CreatedAtAction("Get", new { id = entity.EntityId }, entity);
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!EntityExists(id))
                    return NotFound();
                throw;
            }
        }
        
        // POST: api/EntityAPI
        /// <summary>
        /// Add a new Entity to DB
        /// </summary>
        /// <param name="entity">Object Entity, passing to add DB</param>
        /// <returns></returns>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Entity entity)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            await _repository.Save(entity);
            return CreatedAtAction("Get", new { id = entity.EntityId }, entity);
        }
        
        private bool EntityExists(int id)
        {
            return _context.Entities.Any(e => e.EntityId == id);
        }
    }
}