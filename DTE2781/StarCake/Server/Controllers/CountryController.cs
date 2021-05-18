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
        [HttpGet]
        public async Task<IEnumerable<Country>> GetCountries()
        {
            return await _repository.GetAll();
        }
        
        // GET: api/Country/{id}
        [HttpGet("{id:int}")]
        public IActionResult Get([FromRoute] int id)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            var country = _repository.Get(id);
            if (country == null)
                return NotFound();
            return Ok(country);
        }
        
        // PUT: api/Country/{id}
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