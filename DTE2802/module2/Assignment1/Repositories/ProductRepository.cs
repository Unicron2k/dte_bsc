using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Assignment1.Data;
using Assignment1.Models;
using Assignment1.Models.Entities;
using Assignment1.Models.ViewModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;

namespace Assignment1.Repositories
{
    public class ProductRepository : IProductRepository
    {
        private readonly ApplicationDbContext _db;
        private readonly UserManager<IdentityUser> _userManager;
        
        public ProductRepository(UserManager<IdentityUser> userManager,  ApplicationDbContext db)
        {
            _userManager = userManager;
            _db = db;
        }

        public IEnumerable<Product> GetAll()
        {
            return _db.Products.Include("Category").Include("Manufacturer");
        }

        [Authorize]
        public async Task Save(ProductEditViewModel product,  ClaimsPrincipal principal)
        {
            var owner = await _userManager.FindByNameAsync(principal.Identity.Name);
            var p = new Product
            {
                CategoryId = product.CategoryId,
                Description = product.Description,
                ManufacturerId = product.ManufacturerId,
                Name = product.Name,
                Price = product.Price,
                Owner = owner
            };

            await _db.Products.AddAsync(p);
            await _db.SaveChangesAsync();
        }

        public ProductEditViewModel GetProductEditViewModel()
        {
            var model = new ProductEditViewModel();
            var manufacturers = new List<Manufacturer>(_db.Manufacturers.ToList());
            var categories = new List<Category>(_db.Categories.ToList());
            model.Manufacturers = manufacturers;
            model.Categories = categories;
            return model;
        }
    }
}