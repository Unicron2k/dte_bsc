using System.Diagnostics;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using ProjectREST.Models.Entities;
using ProjectREST.Models.Interfaces;
using ProjectREST.Models.ViewModels;

namespace ProjectREST.Controllers
{
    public class HomeController : Controller
    {
        private readonly IBlogRepository _repository;
        private readonly ILogger<HomeController> _logger;
        private readonly UserManager<ApplicationUser> _userManager;

        public HomeController(ILogger<HomeController> logger, UserManager<ApplicationUser> userManager, IBlogRepository repository)
        {
            _logger = logger;
            _userManager = userManager;
            _repository = repository;
        }

        // GET: Home
        [AllowAnonymous]
        public async Task<IActionResult> Index()
        {
            return View(await _repository.GetAll());
        }

        // GET: Home/Details/5
        [AllowAnonymous]
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                var result = NotFound();
                return NotFound();
            }

            var blog = await _repository.GetBlog(id);
            if (blog == null)
            {
                return NotFound();
            }

            return View(blog);
        }
        
        // GET: Home/Create
        [Authorize]
        public IActionResult Create()
        {
            return View();
        }

        // POST: Home/Create
        // To protect from over-posting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [Authorize]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Name,Description")] BlogViewModel blog)
        {
            if (ModelState.IsValid)
            {
                await _repository.SaveBlog(blog, User);
                TempData["message"] = $"\"{blog.Name}\" have been created!";
                return RedirectToAction(nameof(Index));
            }
            TempData["error"] = "There was an error. Please try again.";
            return View(blog);
        }

        // GET: Home/Edit/5
        [Authorize]
        public async Task<IActionResult> Edit(int? id)
        {
            //TODO: Add security-checks
            if (id == null)
            {
                return NotFound();
            }

            var blog = await _repository.GetBlog(id);
            if (blog == null)
            {
                return NotFound();
            } 
            return View(new BlogViewModel
            {
                BlogId = blog.BlogId,
                Name = blog.Name,
                Description = blog.Description,
                Owner = blog.Owner,
                CreationDate = blog.CreationDate,
                BlogLocked = blog.BlogLocked
            });
        }

        // POST: Home/Edit/5
        // To protect from over-posting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [Authorize]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("BlogId,Name,Description,BlogLocked")]BlogViewModel blog)
        {
            
            //TODO: Add security-checks
            if (id != blog.BlogId)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    await _repository.UpdateBlog(blog);
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!BlogExists(blog.BlogId))
                    {
                        TempData["error"] = "There was an error updating your blog";
                        return NotFound();
                    }
                    throw;
                }
                TempData["message"] = $"\"{blog.Name}\" have been updated!";
                return RedirectToAction(nameof(Index));
            }
            TempData["error"] = $"There was an error updating \"{blog.Name}\"";
            return View(blog);
        }

        // GET: Home/Delete/5
        [Authorize]
        public async Task<IActionResult> Delete(int? id)
        {
            //TODO: Add security-checks
            if (id == null)
            {
                return NotFound();
            }

            var blog = await _repository.GetBlog(id);
            if (blog == null)
            {
                return NotFound();
            }

            return View(blog);
        }

        // POST: Home/Delete/5
        [Authorize]
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            //TODO: Add security-checks
            await _repository.DeleteBlog(id);
            TempData["message"] = "Blog deleted!";
            return RedirectToAction(nameof(Index));
        }
        
        [AllowAnonymous]
        public IActionResult Privacy()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel {RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier});
        }

        private bool BlogExists(int id)
        {
            return _repository.BlogExists(id);
        }
        
        
        public async Task<RedirectToActionResult> Subscribe(int blogId)
        {
            await _repository.Subscribe(blogId, User);

            var blog = await _repository.GetBlog(blogId);

            TempData["message"] = $"Subscribed to: {blog.Name}!";

            return RedirectToAction("Details", new {blogId});
        }
    }
}
