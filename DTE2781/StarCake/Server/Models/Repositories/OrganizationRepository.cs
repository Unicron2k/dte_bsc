using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Repositories
{
    public class OrganizationRepository : IOrganizationRepository
    {
        private readonly ApplicationDbContext _db;

        public OrganizationRepository(ApplicationDbContext db)
        {
            _db = db;
        }
        
        /// <summary>
        /// Get the specified organization
        /// </summary>
        /// <param name="id">Id of organization</param>
        /// <returns>Organization</returns>
        public Organization Get(int? id)
        {
            return (from o in _db.Organizations
                where o.OrganizationId == id
                select o).FirstOrDefault();
        }

        /// <summary>
        /// Returns ALL Organizations in the database
        /// </summary>
        /// <returns>async Task IEnumerable of Organizations</returns>
        public  IEnumerable<Organization> GetAll()
        {
            return (from o in _db.Organizations select o).ToList();
        }

        /// <summary>
        /// Add new Organization
        /// </summary>
        /// <param name="organization"> Organization ro add</param>
        /// <returns>Awaitable Task</returns>
        public async Task Add(Organization organization)
        {
            await _db.Organizations.AddAsync(organization);
            await _db.SaveChangesAsync();
        }

        /// <summary>
        /// Update a given organization
        /// </summary>
        /// <param name="organization">Entity to update</param>
        /// <returns>Awaitable Task</returns>
        public async Task Update(Organization organization)
        {
            _db.Organizations.Update(organization);
            await _db.SaveChangesAsync();
        }

        /// <summary>
        /// Return a viewmodel of the specified organization
        /// Includes a list of departments
        /// </summary>
        /// <param name="organizationId">Id of organization to get</param>
        /// <returns>OrganizationViewModel</returns>
        public async Task<OrganizationViewModel> GetViewModel(int? organizationId)
        {
            var organization = await _db.Organizations.FindAsync(organizationId);
            return organization.MapToViewModel();
        }
        
        /// <summary>
        /// Check if an organization exists
        /// Returns true if it exists
        /// </summary>
        /// <param name="id">Id of organization to check</param>
        /// <returns>Boolean</returns>
        public bool OrganizationExists(int? id)
        {
            return _db.Organizations.Any(o => o.OrganizationId == id);
        }
    }
}