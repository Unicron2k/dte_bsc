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
        /// <summary>
        /// Get all Type of operation from DB
        /// </summary>
        /// <returns>IEnumerable TypeOfOperation</returns>
        [HttpGet("all/")]
        public async Task<IEnumerable<TypeOfOperation>> GetTypeOfOperations()
        {
            return await _repository.GetAll();
        }
        
        // GET: api/TypeOfOperation/{id}
        /// <summary>
        /// Get a specific Type of operation
        /// </summary>
        /// <param name="id">int typeOfOperationId</param>
        /// <returns>IActionResult </returns>
        [HttpGet("{id:int}")]
        public async Task<ActionResult<TypeOfOperation>> Get([FromRoute] int? id)
        {
            if (id == null)
                return NotFound("Bad parameter");
            var componentType = await _repository.Get(id);
            if (componentType == null)
                return NotFound();
            return componentType;
        }
        
        // PUT: api/TypeOfOperation/{id}
        /// <summary>
        /// Update a specific Type of operation
        /// </summary>
        /// <param name="id">int typeOfOperationId</param>
        /// <param name="typeOfOperation">Object TypeOfOperationViewModel, passing data to update DB</param>
        /// <returns>IActionResult </returns>
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
        /// <summary>
        /// Add a new Type of operation to DB
        /// </summary>
        /// <param name="typeOfOperation">Object TypeOfOperation, passing data to DB</param>
        /// <returns></returns>
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