using System.Security.Claims;
using System.Threading.Tasks;
using ProjectREST.Models.Entities;
using ProjectREST.Models.ViewModels;

namespace ProjectREST.Models.Interfaces
{
    public interface ICommentRepository
    {

        public Task SaveComment(CommentViewModel comment, ClaimsPrincipal principal);

        public Task<Comment> GetComment(int? id);
        public Task<BlogPost> GetPost(int? id);
        
        public Task UpdateComment(CommentViewModel comment);

        public Task DeleteComment(int? id);

        public bool CommentExists(int? id);
    }
}
