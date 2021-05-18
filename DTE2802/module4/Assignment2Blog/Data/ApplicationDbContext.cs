using System;
using System.Collections.Generic;
using System.Text;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using Blog = Assignment2Blog.Models.Entities.Blog;
using BlogPost = Assignment2Blog.Models.Entities.BlogPost;
using Comment = Assignment2Blog.Models.Entities.Comment;

namespace Assignment2Blog.Data
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