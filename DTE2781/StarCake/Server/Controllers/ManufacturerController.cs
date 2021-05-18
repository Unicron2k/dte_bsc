using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;
using Microsoft.AspNetCore.Authorization;
using StarCake.Shared;


namespace StarCake.Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ManufacturerController : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IManufacturRepository _repostory;

        public ManufacturerController(ApplicationDbContext context, IManufacturRepository repository)
        {
            _context = context;
            _repostory = repository;
        }
        
        
        // GET: api/Manufacturer
        [HttpGet]
        public async Task<IEnumerable<Manufacturer>> GetManufacturers()
        {
            IEnumerable<Manufacturer> entities = await _context.Manufacturers.ToListAsync();
            return entities;
        }
        
        // GET: api/Manufacturer/
        [HttpGet("{id}")]
        public async Task<ActionResult<Manufacturer>> GetManufacturer(int id)
        {
            var component = await _context.Manufacturers.FindAsync(id);
            if (component == null)
                return NotFound();
            return component;
        }
        
        // POST: api/Manufacturer
        [HttpPost]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<ActionResult<Manufacturer>> PostManufacturer(Manufacturer manufacturer)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            await _repostory.SaveManufacturer(manufacturer);
            return CreatedAtAction("GetManufacturer", new { id = manufacturer.ManufacturerId }, manufacturer);
        }
        
        //PUT: api/Manufacturer/{id}
        [HttpPut("{id}")]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<IActionResult> Put([FromRoute] int id, [FromBody] ManufacturerViewModel manufacturer)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != manufacturer.ManufacturerId)
            {
                return BadRequest();
            }

            try
            {
                await _repostory.UpdateManufacturer(manufacturer);
                return CreatedAtAction("GetManufacturer", new {id = manufacturer.ManufacturerId}, manufacturer);

            }
            catch (DbUpdateConcurrencyException e)
            {
                if (!ManufacturerExists(id))
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


        private bool ManufacturerExists(int id)
        {
            return _context.Manufacturers.Any(e => e.ManufacturerId == id);
        }
    }
}
