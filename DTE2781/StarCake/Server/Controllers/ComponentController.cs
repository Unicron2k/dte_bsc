using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;

namespace StarCake.Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ComponentController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IComponentRepository _repository;

        public ComponentController(IComponentRepository repository, ApplicationDbContext context)
        {
            _repository = repository;
            _context = context;
        }

        // GET: api/Component/all
        /// <summary>
        /// Get all Compoennts from DB
        /// </summary>
        /// <returns>IEnumerable Component</returns>
        [HttpGet("all/")]
        public async Task<IEnumerable<Component>> GetComponents()
        {
            return await _repository.GetAll();
        }

        // GET: api/Component/
        /// <summary>
        /// Get a specific component by id 
        /// </summary>
        /// <param name="id">ComponentId - int</param>
        /// <returns>Component</returns>
        [HttpGet("{id:int}")]
        public async Task<ActionResult<Component>> Get([FromRoute] int? id)
        {
            if (id == null)
                return NotFound("Bad parameter");
            var component = await _repository.Get(id);
            if (component == null)
                return NotFound();
            return component;
        }

        // PUT: api/Component/{id}
        /// <summary>
        /// Update the specific component by id and data
        /// </summary>
        /// <param name="id">ComponentId-int</param>
        /// <param name="component">Component data to update</param>
        /// <returns>Updated component IActionResult</returns>
        [HttpPut("{id:int}")]
        public async Task<IActionResult> Put([FromRoute] int id, [FromBody] Component component)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            if (id != component.ComponentId)
                return BadRequest();
            try
            {
                await _repository.Update(component);
                return CreatedAtAction("Get", new {id = component.ComponentId}, component);
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ComponentExists(id))
                    return NotFound();
                throw;
            }
        }

        // POST: api/Component
        /// <summary>
        /// Save a new Component
        /// </summary>
        /// <param name="component">Component</param>
        /// <returns>New Component</returns>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody]Component component)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            await _repository.Save(component);
            return CreatedAtAction("Get", new {id = component.ComponentId}, component);
        }

        private bool ComponentExists(int id)
        {
            return _context.Components.Any(e => e.ComponentId == id);
        }
    }
}