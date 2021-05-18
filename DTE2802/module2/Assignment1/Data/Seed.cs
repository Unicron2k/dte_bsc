using System;
using Assignment1.Models.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Internal;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;

namespace Assignment1.Data
{
    public class Seed
    {
        public static void Initialize(IServiceProvider app)
        {
            using var context =
                new ApplicationDbContext(app.GetRequiredService<DbContextOptions<ApplicationDbContext>>());
            
            // Get a logger
            var logger = app.GetRequiredService<ILogger<Seed>>();
            
            // Make sure the database is created
            context.Database.EnsureCreated();
            
            // // Look for any products
            if (context.Products.Any())
            {
                logger.LogInformation("The database was already seeded");
                return;
                // DB has been seeded
            }
            context.Categories.AddRange(
                new Category {Name = "Dagligvarer", Description = "Dagligvarer for hus og hjem"},
                new Category {Name = "Verktøy", Description = "Verktøy for alle Terrans"},
                new Category {Name = "Kjøretøy", Description = "Objekter for å transportere din Kongelige Bakende med stil"}
            );
            context.SaveChanges();
            
            context.Manufacturers.AddRange(
                new Manufacturer {Name = "Hammer Inc.", Description = "BFH. Alt er en hammer.", Address = "Hammerlia 12"},
                new Manufacturer {Name = "Biltema", Description = "Bare ræl, men noe bra!", Address = "???"},
                new Manufacturer {Name = "Volvo", Description = "Bilprodusent", Address = "Kina"},
                new Manufacturer {Name = "BMW", Description = "Bilprodusent", Address = "Bayern, Deutchland"},
                new Manufacturer {Name = "Kavli", Description = "Matprodusent", Address = "Kavlibakken 1"}
            );
            context.SaveChanges();
            
            context.Products.AddRange(
                new Product {Name = "Brød", Description = "Spiselig, tror vi...", Price = 25.50m, ManufacturerId = 5, CategoryId = 1},
                new Product {Name = "Hammer", Description = "Alt er en BFH!", Price = 121.50m, ManufacturerId = 3, CategoryId = 2},
                new Product {Name = "Vinkelsliper", Description = "Alt er en BFH!", Price = 1520.00m, ManufacturerId = 3, CategoryId = 2},
                new Product {Name = "Volvo XC90", Description = "MYE kinesium", Price = 990000m, ManufacturerId = 1, CategoryId = 3},
                new Product {Name = "Volvo XC60", Description = "Kinesium", Price = 620000m, ManufacturerId = 1, CategoryId = 3}
            );
            context.SaveChanges();
            
            logger.LogInformation("Finished seeding the database.");
        }            
    }
}