using System.Collections.Generic;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IOrganizationRepository
    {
        public Organization Get(int? id);
        public IEnumerable<Organization> GetAll();
        public Task Add(Organization organization);
        public Task Update(Organization organization);
        public bool OrganizationExists(int? id);

        Task<OrganizationViewModel> GetViewModel(int? organizationId);
    }
}