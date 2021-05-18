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
    public class BlogRepository: IBlogRepository
    {

        private readonly ApplicationDbContext _db;
        private readonly UserManager<ApplicationUser> _userManager;

        public BlogRepository(UserManager<ApplicationUser> userManager, ApplicationDbContext db)
        {
            _userManager = userManager;
            _db = db;
        }
        
        public async Task<List<Blog>> GetAll()
        {
            return await _db.Blogs.ToListAsync();
        }

        public async Task SaveBlog(BlogViewModel blog, ClaimsPrincipal principal)
        {
            var b = new Blog
            {
                BlogId = blog.BlogId,
                Name = blog.Name,
                Description = blog.Description,
                Owner = await _userManager.FindByNameAsync(principal.Identity.Name),
                CreationDate = DateTime.UtcNow.ToString("yyyy-MM-dd"),
                BlogLocked = false
            };
            await _db.Blogs.AddAsync(b);
            await _db.SaveChangesAsync();
        }

        public async Task<Blog> GetBlog(int? id)
        {
            return await _db.Blogs.FirstOrDefaultAsync(b => b.BlogId == id);
        }

        public async Task UpdateBlog(BlogViewModel blog)
        {
            var b = await _db.Blogs.FirstOrDefaultAsync(b => b.BlogId == blog.BlogId);
            b.Name = blog.Name;
            b.Description = blog.Description;
            b.BlogLocked = blog.BlogLocked;
            _db.Update(b);
            await _db.SaveChangesAsync();
        }

        public async Task DeleteBlog(int? id)
        {
            var blog = await _db.Blogs.FirstOrDefaultAsync(b => b.BlogId == id);
           _db.Blogs.Remove(blog);
            await _db.SaveChangesAsync();
        }

        public bool BlogExists(int? id)
        {
            return _db.Blogs.Any(e => e.BlogId == id);
        }
        
        
        public async Task Subscribe(int blogId, ClaimsPrincipal principal)
        {
            var user = await _userManager.FindByNameAsync(principal.Identity.Name);

            var blog = await (from b in _db.Blogs
                where b.BlogId == blogId
                select b).FirstOrDefaultAsync();

            user.SubscribedBlogs.Add(blog);
            
            var result = await _db.SaveChangesAsync();

            if (result == 0)
            {
                throw new Exception("Error creating subscription");
            }
        }
        
        public async Task<List<Blog>> GetSubscribedBlogs(ClaimsPrincipal principal)
        {
            var user = await _userManager.FindByNameAsync(principal.Identity.Name);
            return user?.SubscribedBlogs;
        }
    }
}