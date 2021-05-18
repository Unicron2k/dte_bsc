using System.Diagnostics;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using ProjectREST.Authorization;
using ProjectREST.Models.Entities;
using ProjectREST.Models.Interfaces;
using ProjectREST.Models.ViewModels;

namespace ProjectREST.Controllers
{
    public class BlogController : Controller
    {
        private readonly IBlogPostRepository _repository;
        private readonly ILogger<BlogController> _logger;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IAuthorizationService _authorizationService;

        public BlogController(ILogger<BlogController> logger, UserManager<ApplicationUser> userManager, IBlogPostRepository repository, IAuthorizationService authorizationService = null)
        {
            _logger = logger;
            _userManager = userManager;
            _repository = repository;
            _authorizationService = authorizationService;
        }

        // GET: Home
        [AllowAnonymous]
        public async Task<IActionResult> Index(int? id)
        {
            if (id == null || id <= 0)
            {
                return RedirectToAction("Index", "Home");
            }
            
            
            return View(await _repository.GetAll(id));
        }
    

        // GET: Home/Details/5
        [AllowAnonymous]
        public async Task<IActionResult> View(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var blog = await _repository.GetBlogPost(id);
            if (blog == null)
            {
                return NotFound();
            }

            return View(blog);
        }
        
        // GET: Home/Create
        [Authorize]
        public IActionResult Create(int id)
        {
            var blog = _repository.GetAll(id).Result;
            if (!blog.BlogLocked)
                return View(new BlogPostViewModel
                {
                    BlogId = id
                });
            TempData["error"] = "This blog has been locked!";
            return RedirectToAction("Index", new {id=blog.BlogId});
        }

        // POST: Home/Create
        // To protect from over-posting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [Authorize]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Title,Content,BlogId")] BlogPostViewModel blogPost)
        {
            var blog = _repository.GetAll(blogPost.BlogId).Result;
            if (blog.BlogLocked)
            {
                TempData["error"] = "This blog has been locked!";
                return RedirectToAction("Index", new {id=blog.BlogId});
            }

            if (!ModelState.IsValid)
            {
                TempData["error"] = "There was an error. Please try again.";
                return View(blogPost);
            }

            var post = new BlogPost
            {
                Title = blogPost.Title,
                Content = blogPost.Content,
                BlogId = blogPost.BlogId,
                Owner = blog.Owner
            };

            var isAuthorized = await _authorizationService.AuthorizeAsync(User, post, BlogOperations.Create);
            if (!isAuthorized.Succeeded)
            {
                return new ForbidResult();
            }
            
            await _repository.SaveBlogPost(blogPost, User);
            TempData["message"] = $"Post \"{blogPost.Title}\" have been created!";
            return RedirectToAction(nameof(Index), new {id=blogPost.BlogId});
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

            var blog = await _repository.GetBlogPost(id);
            if (blog == null)
            {
                return NotFound();
            }

            var b = new BlogPost
            {
                BlogPostId = blog.BlogPostId,
                Title = blog.Title,
                Content = blog.Content,
                Owner = blog.Owner,
                PostDate = blog.PostDate,
                BlogPostLocked = blog.BlogPostLocked,
                BlogId = blog.BlogId,
                Blog = blog.Blog 
            };
            var isAuthorized = await _authorizationService.AuthorizeAsync(User, b, BlogOperations.Create);
            if (!isAuthorized.Succeeded)
            {
                return new ForbidResult();
            }
            
            if (!blog.Blog.BlogLocked)
                return View(new BlogPostViewModel
                {
                    BlogPostId = blog.BlogPostId,
                    Title = blog.Title,
                    Content = blog.Content,
                    Owner = blog.Owner,
                    PostDate = blog.PostDate,
                    BlogPostLocked = blog.BlogPostLocked,
                    BlogId = blog.BlogId,
                    Blog = blog.Blog
                });
            TempData["error"] = "This blog has been locked!";
            return RedirectToAction("Index", new {id=blog.BlogId});
        }

        // POST: Home/Edit/5
        // To protect from over-posting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [Authorize]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("BlogId,BlogPostId,Title,Content,BlogPostLocked")]BlogPostViewModel blogPost)
        {
            
            //TODO: Add security-checks
            if (id != blogPost.BlogPostId)
            {
                return NotFound();
            }

            var blog = _repository.GetBlogPost(id).Result;
            if (blog.Blog.BlogLocked)
            {
                TempData["error"] = "This blog has been locked!";
                return RedirectToAction("Index", new {id=blog.BlogId});
            }
            var b = new BlogPost
            {
                BlogPostId = blog.BlogPostId,
                Title = blog.Title,
                Content = blog.Content,
                Owner = blog.Owner,
                PostDate = blog.PostDate,
                BlogPostLocked = blog.BlogPostLocked,
                BlogId = blog.BlogId,
                Blog = blog.Blog 
            };
            var isAuthorized = await _authorizationService.AuthorizeAsync(User, b, BlogOperations.Create);
            if (!isAuthorized.Succeeded)
            {
                return new ForbidResult();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    await _repository.UpdateBlogPost(blogPost);
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (BlogExists(blogPost.BlogId)) throw;
                    TempData["error"] = "There was an error updating your post.";
                    return NotFound();
                }
                TempData["message"] = $"\"{blogPost.Title}\" have been updated!";
                return RedirectToAction("Index", new{id=blogPost.BlogId});
            }
            TempData["error"] = $"There was an error updating \"{blogPost.Title}\"";
            return View(blogPost);
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
            var blog = await _repository.GetBlogPost(id);
            
            if (blog == null)
            {
                return NotFound();
            }
            var b = new BlogPost
            {
                BlogPostId = blog.BlogPostId,
                Title = blog.Title,
                Content = blog.Content,
                Owner = blog.Owner,
                PostDate = blog.PostDate,
                BlogPostLocked = blog.BlogPostLocked,
                BlogId = blog.BlogId,
                Blog = blog.Blog 
            };
            var isAuthorized = await _authorizationService.AuthorizeAsync(User, b, BlogOperations.Create);
            if (!isAuthorized.Succeeded)
            {
                return new ForbidResult();
            }
            
            if (!blog.Blog.BlogLocked) return View(blog);
            TempData["error"] = "This blog has been locked!";
            return RedirectToAction("Index", new {id=blog.BlogId});
        }

        // POST: Home/Delete/5
        [Authorize]
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            //TODO: Add security-checks
            var blog = await _repository.GetBlogPost(id);
            if (blog.Blog.BlogLocked)
            {
                TempData["error"] = "This blog has been locked!";
                return RedirectToAction("Index", new {id=blog.BlogId});
            }
            var b = new BlogPost
            {
                BlogPostId = blog.BlogPostId,
                Title = blog.Title,
                Content = blog.Content,
                Owner = blog.Owner,
                PostDate = blog.PostDate,
                BlogPostLocked = blog.BlogPostLocked,
                BlogId = blog.BlogId,
                Blog = blog.Blog 
            };
            var isAuthorized = await _authorizationService.AuthorizeAsync(User, b, BlogOperations.Create);
            if (!isAuthorized.Succeeded)
            {
                return new ForbidResult();
            }
            await _repository.DeleteBlogPost(id);
            TempData["message"] = "Post deleted!";
            return RedirectToAction("Index", new{id=blog.BlogId});
        }
        
        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel {RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier});
        }

        private bool BlogExists(int id)
        {
            return _repository.BlogPostExists(id);
        }
    }
}
