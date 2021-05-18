using System.Threading.Tasks;
using Assignment1.Models;
using Assignment1.Models.ViewModels;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;

namespace Assignment1.Controllers
{
    public class ProductController : Controller
    {
        private readonly IProductRepository _repository;
        private readonly UserManager<IdentityUser> _userManager;

        public ProductController(UserManager<IdentityUser> userManager, IProductRepository repository)
        {
            _userManager = userManager;
            _repository = repository;
        }
        // GET
        public IActionResult Index()
        {
            return View(_repository.GetAll());
        }

        [Authorize]
        public ActionResult Create()
        {
            return View(_repository.GetProductEditViewModel());
        }

        [Authorize]
        [HttpPost]
        public async Task<ActionResult> Create([Bind("Name,Description,Price,ManufacturerId,CategoryId")]ProductEditViewModel product)
        {
            try
            {
                if (!ModelState.IsValid) return View(_repository.GetProductEditViewModel());
                await _repository.Save(product, User);
                TempData["message"] = $"{product.Name} har blitt opprettet";
                return RedirectToAction("Index");

            }
            catch
            {
                return View(_repository.GetProductEditViewModel());
            }
        }
    }
}