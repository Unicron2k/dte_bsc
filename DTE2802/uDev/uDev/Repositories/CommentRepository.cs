using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using uDev.Data;
using uDev.Models.Entity;
using uDev.Models.ViewModels;
using uDev.Repositories.Interface;

namespace uDev.Repositories
{
    public class CommentRepository : ICommentRepository
    {
        private readonly ApplicationDbContext _db;

        public CommentRepository(ApplicationDbContext db)
        {
            _db = db;
        }

        public async Task SaveComment(Comment comment)
        {
            await _db.Comments.AddAsync(comment);
            await _db.SaveChangesAsync();
        }

        public async Task<Comment> GetComment(int? id)
        {
            return await _db.Comments.FirstOrDefaultAsync(c => c.CommentId == id);
        }

        public async Task<Mission> GetMission(int? id)
        {
            var post =await _db.Missions.FirstOrDefaultAsync(m => m.MissionId == id);
            return post;
        }

        public async Task UpdateComment(CommentViewModel comment)
        {
            _db.Update(comment);
            await _db.SaveChangesAsync();
        }

        public async Task DeleteComment(int? id)
        {
            var comment = await _db.Comments.FirstOrDefaultAsync(c => c.CommentId == id);
           _db.Comments.Remove(comment);
            await _db.SaveChangesAsync();
        }

        public bool CommentExists(int? id)
        {
            return _db.Comments.Any(c => c.CommentId == id);
        }
    }
}