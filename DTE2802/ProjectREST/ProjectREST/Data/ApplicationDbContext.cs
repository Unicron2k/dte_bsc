using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Blog = ProjectREST.Models.Entities.Blog;
using BlogPost = ProjectREST.Models.Entities.BlogPost;
using Comment = ProjectREST.Models.Entities.Comment;

namespace ProjectREST.Data
{
    public class ApplicationDbContext : IdentityDbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options)
        {
        }
        
        //Blogs on the server
        public DbSet<Blog> Blogs { get; set; }
        
        //Posts in a blog
        public DbSet<BlogPost> BlogPosts { get; set; }
        
        //Comments on a blogpost
        public DbSet<Comment> Comments { get; set; }
    }
}