using System.Collections.Generic;
using System.Security.Claims;
using System.Threading.Tasks;
using ProjectREST.Models.Entities;
using ProjectREST.Models.ViewModels;

namespace ProjectREST.Models.Interfaces
{
    public interface IBlogRepository
    {
        public Task<List<Blog>> GetAll();
        
        public Task SaveBlog(BlogViewModel blog, ClaimsPrincipal principal);

        public Task<Blog> GetBlog(int? id);
        
        public Task UpdateBlog(BlogViewModel blog);

        public Task DeleteBlog(int? id);

        public bool BlogExists(int? id);

        public Task Subscribe(int blogId, ClaimsPrincipal principal);

        public Task<List<Blog>> GetSubscribedBlogs(ClaimsPrincipal principal);
    }
}
