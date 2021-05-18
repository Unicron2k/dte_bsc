using System.Collections.Generic;
using System.Security.Claims;
using System.Threading.Tasks;
using Assignment2Blog.Models.Entities;
using Assignment2Blog.Models.ViewModels;

namespace Assignment2Blog.Models.Interfaces
{
    public interface IBlogPostRepository
    {
        public Task<BlogViewModel> GetAll(int? id);
        
        public Task SaveBlogPost(BlogPostViewModel blogPost, ClaimsPrincipal principal);

        public Task<BlogPostViewModel> GetBlogPost(int? id);
        
        public Task UpdateBlogPost(BlogPostViewModel blogPost);

        public Task DeleteBlogPost(int? id);

        public bool BlogPostExists(int? id);
    }
}
