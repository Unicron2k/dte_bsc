using Microsoft.EntityFrameworkCore;

namespace Assignment1.Models
{
    public class DbContext : Microsoft.EntityFrameworkCore.DbContext
    {
        public DbContext(DbContextOptions options) : base(options)
        {
            
        }
    }
}