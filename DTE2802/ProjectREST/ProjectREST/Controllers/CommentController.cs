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
    
    public class CommentController : Controller
    {
        private readonly ICommentRepository _repository;
        private readonly ILogger<CommentController> _logger;
        private readonly UserManager<ApplicationUser> _userManager;

        public CommentController(ILogger<CommentController> logger, UserManager<ApplicationUser> userManager, ICommentRepository repository)
        {
            _logger = logger;
            _userManager = userManager;
            _repository = repository;
        }

        // GET: Home/Create
        [Authorize]
        public IActionResult Create(int id, int replyTo=0)
        {
            var post = _repository.GetPost(id).Result;
            if (post.BlogPostLocked || post.Blog.BlogLocked)
            {
                TempData["error"] = "This post has been locked!";
                return RedirectToAction("View", "Blog", new {id = post.BlogPostId});
            }
            
            return View(new CommentViewModel
            {
                ReplyTo = replyTo,
                BlogPostId = id
            });
        }

        // POST: Home/Create
        // To protect from over-posting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [Authorize]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("BlogPostId,Content,ReplyTo")] CommentViewModel comment)
        {
            var post = await _repository.GetPost(comment.BlogPostId);
            if (post.BlogPostLocked || post.Blog.BlogLocked)
            {
                TempData["error"] = "This post has been locked!";
                return RedirectToAction("View", "Blog", new {id = post.BlogPostId});
            }
            if (ModelState.IsValid)
            {
                await _repository.SaveComment(comment, User);
                TempData["message"] = "Your comment have been posted";
                return RedirectToAction(nameof(View), "Blog",new {id=comment.BlogPostId});
            }
            TempData["error"] = "There was an error. Please try again.";
            return View(comment);
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

            var comment = await _repository.GetComment(id);
            if (comment == null)
            {
                return NotFound();
            } 
            if (comment.BlogPost.BlogPostLocked || comment.BlogPost.Blog.BlogLocked) 
            {
                TempData["error"] = "This post has been locked!";
                return RedirectToAction("View", "Blog", new {id = comment.BlogPostId});
            }
            return View(new CommentViewModel
            {
                CommentId = comment.CommentId,
                Content = comment.Content,
                Owner = comment.Owner,
                PostDate = comment.PostDate,
                ReplyTo = comment.ReplyTo,
                BlogPostId = comment.BlogPostId,
                BlogPost =comment.BlogPost 
            });
        }

        // POST: Home/Edit/5
        // To protect from over-posting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [Authorize]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("CommentId,BlogPostId,Content")]CommentViewModel comment)
        {
            
            //TODO: Add security-checks
            if (id != comment.CommentId)
            {
                return NotFound();
            }

            if (comment.BlogPost.BlogPostLocked || comment.BlogPost.Blog.BlogLocked)
            {
                TempData["error"] = "This post has been locked!";
                return RedirectToAction("View", "Blog", new {id = comment.BlogPostId});
            }
            if (ModelState.IsValid)
            {
                try
                {
                    await _repository.UpdateComment(comment);
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!CommentExists(comment.CommentId))
                    {
                        TempData["error"] = "The comment does not exist!";
                        return NotFound();
                    }
                    throw;
                }
                TempData["message"] = "Your comment have been updated!";
                return RedirectToAction(nameof(View), "Blog",new {id=comment.BlogPostId});
            }
            TempData["error"] = "There was an error updating your comment";
            return View(comment);
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

            var comment = await _repository.GetComment(id);
            if (comment == null)
            {
                return NotFound();
            }
            if (comment.BlogPost.BlogPostLocked || comment.BlogPost.Blog.BlogLocked)
            {
                TempData["error"] = "This post has been locked!";
                return RedirectToAction("View", "Blog", new {id = comment.BlogPostId});
            }
            return View(comment);
        }

        // POST: Home/Delete/5
        [Authorize]
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            //TODO: Add security-checks
            var comment = await _repository.GetComment(id);
            if (comment.BlogPost.BlogPostLocked || comment.BlogPost.Blog.BlogLocked)
            {
                TempData["error"] = "This post has been locked!";
                return RedirectToAction("View", "Blog", new {id = comment.BlogPostId});
            }
            await _repository.DeleteComment(id);
            TempData["message"] = "Comment deleted!";
            return RedirectToAction(nameof(View), "Blog",new {id=comment.BlogPostId});
        }


        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel {RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier});
        }

        private bool CommentExists(int id)
        {
            return _repository.CommentExists(id);
        }
    }
}
/**/