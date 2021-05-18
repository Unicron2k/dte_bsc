using System.Threading.Tasks;
using uDev.Models.Entity;
using uDev.Models.ViewModels;

namespace uDev.Repositories.Interface
{
    public interface ICommentRepository
    {
        public Task SaveComment(Comment comment);

        public Task<Comment> GetComment(int? id);
        
        public Task<Mission> GetMission(int? id);
        
        public Task UpdateComment(CommentViewModel comment);

        public Task DeleteComment(int? id);

        public bool CommentExists(int? id);
    }
}
