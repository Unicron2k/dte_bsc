using System.Collections.Generic;
using System.Security.Claims;
using System.Security.Principal;
using System.Threading.Tasks;
using Assignment1.Models;
using Assignment1.Models.Entities;
using Assignment1.Models.ViewModels;

namespace Assignment1.Repositories
{
    public class FakeProductRepository : IProductRepository
    {
        public IEnumerable<Product> GetAll()
        {
            List<Product> products = new List<Product> {
                new Product {Name = "Hammer", Price = 121.50m, CategoryId = 2},
                new Product {Name = "Vinkelsliper", Price = 1520.00m, CategoryId = 2},
                new Product {Name = "Volvo XC90", Price = 990000m, CategoryId = 3},
                new Product {Name = "Volvo XC60", Price = 620000m, CategoryId = 3},
                new Product {Name = "Brød", Price = 25.50m, CategoryId = 1}
            };

            return products;
        }

        public Task Save(ProductEditViewModel product, ClaimsPrincipal principal)
        {
            throw new System.NotImplementedException();
        }

        public ProductEditViewModel GetProductEditViewModel()
        {
            throw new System.NotImplementedException();
        }
    }
}