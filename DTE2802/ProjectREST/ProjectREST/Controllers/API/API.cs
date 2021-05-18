using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using ProjectREST.Data;
using ProjectREST.Models.Entities;
using ProjectREST.Models.Interfaces;
using ProjectREST.Models.ViewModels;

namespace ProjectREST.Controllers.API
{
    [AllowAnonymous]
    [Route("/api/comment")]
    [ApiController]
    public class API : ControllerBase
    {
        private readonly ApplicationDbContext _context;
        private readonly IBlogPostRepository _blogPostRepository;
        private readonly ICommentRepository _commentRepository;
        private readonly UserManager<ApplicationUser> _userManager;


        public API(ApplicationDbContext context, IBlogPostRepository blogPostRepository, ICommentRepository commentRepository, UserManager<ApplicationUser> userManager)
        {
            _context = context;
            _blogPostRepository = blogPostRepository;
            _commentRepository = commentRepository;
            _userManager = userManager;
        }

        // GET: api/comment
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Comment>>> GetComments()
        {
            return await _context.Comments.ToListAsync();
        }

        // Returns a list of comments belonging to the specified blogpost
        // GET: api/comment/5
        [HttpGet("{id}")]
        public async Task<ActionResult<List<Comment>>> GetComments(int id)
        {
            //var blogPost = await _context.BlogPosts.FindAsync(id);
            //This works, but removes all usernames, so we cannot attach a username to a comment
            //unless we store it in the comment-entity
            var blogPost = await _blogPostRepository.GetBlogPost(id);
            if (blogPost == null)
            {
                return NotFound();
            }

            return blogPost.Comments;
        }
        
        // Returns a specific comment belonging to the specified blogpost
        // GET: api/comment/5/5
        [HttpGet("{blogpostId}/{commentId}")]
        public async Task<ActionResult<Comment>> GetComment(int blogpostId, int commentId)
        {
            if (!CommentExists(commentId))
            {
                return NotFound();
            }
            
            var blogPost = await _context.BlogPosts.FindAsync(blogpostId);
            if (blogPost == null)
            {
                return NotFound();
            }

            var comment = blogPost.Comments.Find(c => c.CommentId == commentId);
            if (comment == null)
            {
                return NotFound();
            }
            
            return comment;
        }

        // PUT: api/comment/5
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        [HttpPut("{id}")]
        public async Task<IActionResult> PutComment(int id, Comment comment)
        {
            if (id != comment.CommentId)
            {
                return BadRequest();
            }

            _context.Entry(comment).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!CommentExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return NoContent();
        }

        // POST: api/comment
        // To protect from overposting attacks, enable the specific properties you want to bind to, for
        // more details, see https://go.microsoft.com/fwlink/?linkid=2123754.
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        [HttpPost]
        public async Task<ActionResult<Comment>> PostComment(CommentViewModel comment)
        {
            await _commentRepository.SaveComment(comment, User);

            return CreatedAtAction("GetComments", new { id = comment.CommentId }, comment);
        }

        // DELETE: api/comment/5
        [Authorize(AuthenticationSchemes = JwtBearerDefaults.AuthenticationScheme)]
        [HttpDelete("{id}")]
        public async Task<ActionResult<Comment>> DeleteComment(int id)
        {
            var comment = await _context.Comments.FindAsync(id);
            if (comment == null)
            {
                return NotFound();
            }

            _context.Comments.Remove(comment);
            await _context.SaveChangesAsync();

            return comment;
        }

        private bool CommentExists(int id)
        {
            return _context.Comments.Any(e => e.CommentId == id);
        }
    }
}
