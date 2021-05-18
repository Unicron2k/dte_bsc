using System.Collections.Generic;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface ICountryRepository
    {
        Task<IEnumerable<Country>> GetAll();
        Task Save(Country country);
        Task Update(CountryViewModel countryViewModel);
        Country Get(int? id);
    }
}