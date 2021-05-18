using System.Collections.Generic;
using System.Security.Claims;
using System.Threading.Tasks;
using Assignment2Blog.Models.Entities;
using Assignment2Blog.Models.ViewModels;

namespace Assignment2Blog.Models.Interfaces
{
    public interface IBlogRepository
    {
        public Task<List<Blog>> GetAll();
        
        public Task SaveBlog(BlogViewModel blog, ClaimsPrincipal principal);

        public Task<Blog> GetBlog(int? id);
        
        public Task UpdateBlog(BlogViewModel blog);

        public Task DeleteBlog(int? id);

        public bool BlogExists(int? id);
    }
}
