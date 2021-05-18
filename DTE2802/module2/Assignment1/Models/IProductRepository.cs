using System.Collections.Generic;
using System.Security.Claims;
using System.Security.Principal;
using System.Threading.Tasks;
using Assignment1.Models.Entities;
using Assignment1.Models.ViewModels;

namespace Assignment1.Models
{
    public interface IProductRepository
    {
        IEnumerable<Product> GetAll();
        public Task Save(ProductEditViewModel product,  ClaimsPrincipal principal);

        public ProductEditViewModel GetProductEditViewModel();
    }
}