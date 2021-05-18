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

        // GET: api/Component/
        [HttpGet("{id:int}")]
        public async Task<ActionResult<Component>> Get(int id)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            var component = await _context.Components.FindAsync(id);
            if (component == null)
                return NotFound();
            return Ok(component);
        }

        // PUT: api/Component/{id}
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
        [HttpPost]
        public async Task<ActionResult<Component>> Post([FromBody]Component component)
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