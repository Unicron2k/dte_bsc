using System.Linq;
using System.Collections.Generic;
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
    
    public class TypeOfOperationController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly ITypeOfOperationRepository _repository;

        public TypeOfOperationController(ITypeOfOperationRepository repository, ApplicationDbContext context)
        {
            _repository = repository;
            _context = context;
        }
        
        // GET: api/TypeOfOperation/all
        [HttpGet("all/")]
        public async Task<IEnumerable<TypeOfOperation>> GetTypeOfOperations()
        {
            return await _repository.GetAll();
        }
        
        // GET: api/TypeOfOperation/{id}
        [HttpGet("{id:int}")]
        public IActionResult Get([FromRoute] int id)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            var typeOfOperation = _repository.Get(id);
            if (typeOfOperation == null)
                return NotFound();
            return Ok(typeOfOperation);
        }
        
        // PUT: api/TypeOfOperation/{id}
        [HttpPut("{id:int}")]
        public async Task<IActionResult> Put([FromRoute] int id, [FromBody] TypeOfOperationViewModel typeOfOperation)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            if (id != typeOfOperation.TypeOfOperationId)
                return BadRequest();
            try
            {
                await _repository.Update(typeOfOperation);
                return CreatedAtAction("Get", new { id = typeOfOperation.TypeOfOperationId }, typeOfOperation);
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!TypeOfOperationExists(id))
                    return NotFound();
                throw;
            }
        }
        
        // POST: api/TypeOfOperation
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] TypeOfOperation typeOfOperation)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            await _repository.Save(typeOfOperation);
            return CreatedAtAction("Get", new { id = typeOfOperation.TypeOfOperationId }, typeOfOperation);
        }

        private bool TypeOfOperationExists(int id)
        {
            return _context.TypeOfOperations.Any(e => e.TypeOfOperationId == id);
        }
    }
}