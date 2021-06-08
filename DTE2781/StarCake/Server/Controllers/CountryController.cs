using System.Linq;
using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Server.Models.Repositories;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CountryController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly ICountryRepository _repository;

        public CountryController(ICountryRepository repository, ApplicationDbContext context)
        {
            _repository = repository;
            _context = context;
        }
        
        // GET: api/Country
        /// <summary>
        /// Get all countries from database
        /// </summary>
        /// <returns>IEnumerable of countries</returns>
        [HttpGet]
        public async Task<IEnumerable<Country>> GetCountries()
        {
            return await _repository.GetAll();
        }
        
        // GET: api/Country/{id}
        /// <summary>
        /// Get a specific country
        /// </summary>
        /// <param name="id">CountryId - int</param>
        /// <returns>Country ActionResult</returns>
        [HttpGet("{id:int}")]
        public async Task<ActionResult<Country>> Get([FromRoute] int? id)
        {
            if (id == null)
                return NotFound("Bad parameter");
            var componentType = await _repository.Get(id);
            if (componentType == null)
                return NotFound();
            return componentType;
        }
        
        // PUT: api/Country/{id}
        /// <summary>
        /// Update the specific countru
        /// </summary>
        /// <param name="id">CountryId</param>
        /// <param name="country">CountryViewModel with updated data</param>
        /// <returns>Updated Country IActionResult</returns>
        [HttpPut("{id:int}")]
        public async Task<IActionResult> Put([FromRoute] int id, [FromBody] CountryViewModel country)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            if (id != country.CountryId)
                return BadRequest();
            try
            {
                await _repository.Update(country);
                return CreatedAtAction("Get", new { id = country.CountryId }, country);
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!CountryExists(id))
                    return NotFound();
                throw;
            }
        }
        
        // POST: api/Country
        /// <summary>
        /// Add a new Country to database
        /// </summary>
        /// <param name="country">Country, data to add to database</param>
        /// <returns>New country</returns>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] Country country)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            await _repository.Save(country);
            return CreatedAtAction("Get", new { id = country.CountryId }, country);
        }
        
        private bool CountryExists(int id)
        {
            return _context.Countries.Any(e => e.CountryId == id);
        }
    }
}