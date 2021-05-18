using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class OrganizationController : ControllerBase
    {
        private readonly IOrganizationRepository _organizationRepository;

        public OrganizationController(IOrganizationRepository repository1)
        {
            _organizationRepository = repository1;
        }
        
        /// <summary>
        /// GET: api/Organization
        /// Returns all organizations
        /// </summary>
        /// <returns>IEnumerable<Organization></returns>
        [HttpGet]
        public IActionResult GetAll()
        {
            return Ok(_organizationRepository.GetAll());
        }
        
        /// <summary>
        /// GET: api/Organization/{id}
        /// Gets the specified organization
        /// </summary>
        /// <param name="id">Id of organization to fetch</param>
        /// <returns>IActionResult with the specified organization</returns>
        [HttpGet("{id}")]
        public IActionResult Get([FromRoute] int? id)
        {
            if (id<1)
            {
                return BadRequest("Invalid OrganizationID");
            }
            var organization = _organizationRepository.Get(id);
            if (organization == null)
            {
                return NotFound();
            }
            return Ok(organization);
        }
        
        /// <summary>
        /// GET: api/Organization/{id}/model
        /// Gets the specified organization
        /// </summary>
        /// <param name="id">Id of organization to fetch</param>
        /// <returns>IActionResult with the specified organization</returns>
        [HttpGet("{id:int}/model")]
        public IActionResult GetModel([FromRoute] int? id)
        {
            if (id<1)
                return BadRequest("Invalid OrganizationID");
            var organizationModel = _organizationRepository.GetViewModel(id);
            if (organizationModel == null)
            {
                return NotFound();
            }
            return Ok(organizationModel);
        }
        
        /// <summary>
        /// PUT: api/Organization/
        /// Update an organization from the supplied model
        /// </summary>
        /// <param name="model">OrganizationViewModel</param>
        /// <returns>Awaitable Task<IActionResult></returns>
        [HttpPut]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.Admin)]
        public async Task<IActionResult> Put([FromBody] OrganizationViewModel model)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            if (model.OrganizationId<1)
                return BadRequest("Invalid OrganizationID");
            
            //TODO: Add more validation-checks
            
            try
            {
                var entity = _organizationRepository.Get(model.OrganizationId);
                {
                    entity.Name = model.Name;
                    entity.City = model.City;
                    entity.Address = model.Address;
                    entity.ZipCode = model.ZipCode;
                    entity.Email = model.Email;
                    entity.PhoneNumber = model.PhoneNumber;
                    entity.OperatorNumber = model.OperatorNumber;
                    entity.OrganizationNumber = model.OrganizationNumber;
                    entity.ApiKeyOpenCageData = model.ApiKeyOpenCageData;
                }
                await _organizationRepository.Update(entity);
                return CreatedAtAction("GET", new { id = model.OrganizationId }, model);

            }
            catch (DbUpdateConcurrencyException)
            {
                if (!_organizationRepository.OrganizationExists(model.OrganizationId))
                    return NotFound();
                throw;
            }
        }
        
        /// <summary>
        /// POST: api/Organization
        /// Add a new organization with the supplied model
        /// </summary>
        /// <param name="model">OrganizationViewModel</param>
        /// <returns>Awaitable Task</returns>
        [HttpPost]
        [Authorize(Roles = Roles.OrganizationMaintainer+ "," + Roles.Admin)]
        public async Task<IActionResult> Post([FromBody] OrganizationViewModel model)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            if (model.OrganizationId<1)
                return BadRequest("Invalid OrganizationID");
            
            //TODO: Add more validation-checks
            
            try
            {
                var entity = _organizationRepository.Get(model.OrganizationId);
                {
                    entity.Name = model.Name;
                    entity.City = model.City;
                    entity.Address = model.Address;
                    entity.ZipCode = model.ZipCode;
                    entity.Email = model.Email;
                    entity.PhoneNumber = model.PhoneNumber;
                    entity.OperatorNumber = model.OperatorNumber;
                    entity.OrganizationNumber = model.OrganizationNumber;
                    entity.Departments = new List<Department>();
                }
                await _organizationRepository.Add(entity);
                return CreatedAtAction("GET", new { id = model.OrganizationId }, model);

            }
            catch (DbUpdateConcurrencyException)
            {
                if (!_organizationRepository.OrganizationExists(model.OrganizationId))
                    return NotFound();
                throw;
            }
            
        }
    }
}