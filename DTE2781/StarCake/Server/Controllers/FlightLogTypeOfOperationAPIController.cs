using System.Collections;
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
    
    public class FlightLogTypeOfOperationAPIController : ControllerBase
    {
        private readonly ApplicationDbContext context;
        private IFlightLogTypeOfOperationRepository repository;

        public FlightLogTypeOfOperationAPIController(IFlightLogTypeOfOperationRepository repository)
        {
            this.repository = repository;
        }
        
        // GET: api/FlightLogTypeOfOperationAPI
        [HttpGet]
        public async Task<IEnumerable<FlightLogTypeOfOperation>> GetFlightLogTypeOfOperations()
        {
            var flightLogTypeOfOperations = await repository.GetAll();
            return flightLogTypeOfOperations;
        }
        
        // GET: api/FlightLogTypeOfOperationAPI/{id}
        [HttpGet("{id}")]
        public IActionResult Get([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            var flightLogTypeOfOperation = repository.Get(id);
            if (flightLogTypeOfOperation == null)
            {
                return NotFound();
            }
            return Ok(flightLogTypeOfOperation);
        }
        
        // POST: api/FlightLogTypeOfOperationAPI
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] FlightLogTypeOfOperation flightLogTypeOfOperation)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }
            await repository.Save(flightLogTypeOfOperation);
            return CreatedAtAction("Get", new { id = flightLogTypeOfOperation.FlightLogId }, flightLogTypeOfOperation);
        }
        
        // DELETE: api/FlightLogTypeOfOperationAPI/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteFlightLogTypeOfOperation([FromRoute] int id)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            var flightLogTypeOfOperation =  repository.Get(id);
            if (flightLogTypeOfOperation == null)
            {
                return NotFound();
            }
            
            await repository.Remove(flightLogTypeOfOperation);
            return Ok(flightLogTypeOfOperation);
        }
        
        
        private bool FlightLogTypeOfOperationExists(int id)
        {
            return context.FlightLogTypeOfOperations.Any(e => e.FlightLogId == id);
        }
    }
}