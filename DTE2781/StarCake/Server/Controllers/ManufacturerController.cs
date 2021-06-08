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
        /// <summary>
        /// Get all Manufacturer from database
        /// </summary>
        /// <returns>Task IEnumerable</returns>
        [HttpGet]
        public async Task<IEnumerable<Manufacturer>> GetManufacturers()
        {
            return await _repostory.GetAll();
        }
        
        // GET: api/Manufacturer/
        /// <summary>
        /// Get a specific manufactur
        /// </summary>
        /// <param name="id">ID of manufactur</param>
        /// <returns>Task ActionResult</returns>
        [HttpGet("{id}")]
        public async Task<ActionResult<Manufacturer>> GetManufacturer(int? id)
        {
            //var manufacturer = await _context.Manufacturers.FindAsync(id);
            var manufacturer = await _repostory.GetManufacturer(id);
            if (manufacturer == null)
                return NotFound();
            return manufacturer;
        }
        
        // POST: api/Manufacturer
        /// <summary>
        /// Add a new manufacturer to the database
        /// </summary>
        /// <param name="manufacturer">manufacturer</param>
        /// <returns>Task ActionResult</returns>
        [HttpPost]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<ActionResult> PostManufacturer(Manufacturer manufacturer)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            await _repostory.SaveManufacturer(manufacturer);
            return CreatedAtAction("GetManufacturer", new { id = manufacturer.ManufacturerId }, manufacturer);
        }
        
        //PUT: api/Manufacturer/{id}
        /// <summary>
        /// Update a specific manufacturer
        /// </summary>
        /// <param name="id">ID of manufacturer</param>
        /// <param name="manufacturer">Data of new values to update</param>
        /// <returns>Task IActionResult</returns>
        [HttpPut("{id:int}")]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.DepartmentMaintainer+ "," +Roles.Admin)]
        public async Task<IActionResult> Put([FromRoute] int id, [FromBody] ManufacturerViewModel manufacturer)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            if (id != manufacturer.ManufacturerId)
                return BadRequest();

            try
            {
                await _repostory.UpdateManufacturer(manufacturer);
                return CreatedAtAction("GetManufacturer", new {id = manufacturer.ManufacturerId}, manufacturer);

            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ManufacturerExists(id))
                    return NotFound();
                throw;
            }
        }


        private bool ManufacturerExists(int id)
        {
            return _context.Manufacturers.Any(e => e.ManufacturerId == id);
        }
    }
}