using System.Security.Claims;
using System.Threading.Tasks;
using ProjectREST.Models.ViewModels;

namespace ProjectREST.Models.Interfaces
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
