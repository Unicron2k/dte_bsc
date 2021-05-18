using System.Collections.Generic;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IManufacturRepository
    {
        public Task<List<Manufacturer>> GetAll();
        public Task SaveManufacturer(Manufacturer manufacturer);
        public Task<Manufacturer> GetManufacturer(int? id);
        public Task UpdateManufacturer(ManufacturerViewModel componenmanufacturertType);
        public Task DeleteManufacturer(Manufacturer manufacturer);
        public ManufacturerViewModel GetManufacturViewModel(int? id);
    }
}