using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Assignment2Blog.Data;
using Assignment2Blog.Models.Entities;
using Assignment2Blog.Models.Interfaces;
using Assignment2Blog.Models.ViewModels;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;

namespace Assignment2Blog.Repositories
{
    public class CommentRepository: ICommentRepository
    {

        private readonly ApplicationDbContext _db;
        private readonly UserManager<IdentityUser> _userManager;

        public CommentRepository(UserManager<IdentityUser> userManager, ApplicationDbContext db)
        {
            _userManager = userManager;
            _db = db;
        }

        public async Task SaveComment(CommentViewModel comment, ClaimsPrincipal principal)
        {
            var c = new Comment
            {
                CommentId = comment.CommentId,
                Content = comment.Content,
                Owner = await _userManager.FindByNameAsync(principal.Identity.Name),
                PostDate = DateTime.UtcNow.ToString("yyyy-MM-dd HH:mm:ss"),
                ReplyTo = comment.ReplyTo,
                BlogPostId = comment.BlogPostId,
                BlogPost = comment.BlogPost

            };
            await _db.Comments.AddAsync(c);
            await _db.SaveChangesAsync();
        }

        public async Task<Comment> GetComment(int? id)
        {
            return await _db.Comments.FirstOrDefaultAsync(c => c.CommentId == id);
        }
        
        public async Task<BlogPost> GetPost(int? id)
        {
            var post =await _db.BlogPosts.FirstOrDefaultAsync(bp => bp.BlogPostId == id);
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