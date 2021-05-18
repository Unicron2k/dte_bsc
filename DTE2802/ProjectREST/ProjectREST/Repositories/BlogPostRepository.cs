using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using ProjectREST.Data;
using ProjectREST.Models.Entities;
using ProjectREST.Models.Interfaces;
using ProjectREST.Models.ViewModels;

namespace ProjectREST.Repositories
{
    public class BlogPostRepository: IBlogPostRepository
    {

        private readonly ApplicationDbContext _db;
        private readonly UserManager<ApplicationUser> _userManager;

        public BlogPostRepository(UserManager<ApplicationUser> userManager, ApplicationDbContext db)
        {
            _userManager = userManager;
            _db = db;
        }
        
        public async Task<BlogViewModel> GetAll(int? id)
        {
            var blog = await _db.Blogs.FirstOrDefaultAsync(bp => bp.BlogId == id);
            var model = new BlogViewModel
            {
                BlogId = blog.BlogId,
                Name = blog.Name,
                Description = blog.Description,
                Owner = blog.Owner,
                CreationDate = blog.CreationDate,
                BlogLocked = blog.BlogLocked,
                BlogPosts = new List<BlogPost>( await _db.BlogPosts.Where(bp => bp.BlogId == id).ToListAsync())
            };
            return model;
        }

        public async Task SaveBlogPost(BlogPostViewModel blogPost, ClaimsPrincipal principal)
        {
            var bp = new BlogPost
            {
                BlogPostId = blogPost.BlogPostId,
                Title = blogPost.Title,
                Content = blogPost.Content,
                Owner = await _userManager.FindByNameAsync(principal.Identity.Name),
                PostDate = DateTime.UtcNow.ToString("yyyy-MM-dd"),
                BlogPostLocked = false,
                BlogId = blogPost.BlogId,
                Blog = blogPost.Blog
            };
            await _db.BlogPosts.AddAsync(bp);
            await _db.SaveChangesAsync();
        }

        public async Task<BlogPostViewModel> GetBlogPost(int? id)
        {
            var blogPost = await _db.BlogPosts.FirstOrDefaultAsync(bp => bp.BlogPostId == id);
            var model = new BlogPostViewModel
            {
                BlogPostId = blogPost.BlogPostId,
                Title = blogPost.Title,
                Content = blogPost.Content,
                Owner = blogPost.Owner,
                PostDate = blogPost.PostDate,
                BlogPostLocked = blogPost.BlogPostLocked,
                BlogId = blogPost.BlogId,
                Blog = blogPost.Blog,
                Comments = new List<Comment>( await _db.Comments.Where(c => c.BlogPostId == id).ToListAsync())
            };
            return model;
        }

        public async Task UpdateBlogPost(BlogPostViewModel blogPost)
        {
            var post = await _db.BlogPosts.FirstOrDefaultAsync(bp => bp.BlogPostId == blogPost.BlogPostId);
            post.Title = blogPost.Title;
            post.Content = blogPost.Content;
            post.BlogPostLocked = blogPost.BlogPostLocked;
            _db.Update(post);
            await _db.SaveChangesAsync();
        }

        public async Task DeleteBlogPost(int? id)
        {
            var blog = await _db.BlogPosts.FirstOrDefaultAsync(b => b.BlogPostId == id);
           _db.BlogPosts.Remove(blog);
            await _db.SaveChangesAsync();
        }

        public bool BlogPostExists(int? id)
        {
            return _db.BlogPosts.Any(e => e.BlogPostId == id);
        }
    }
}