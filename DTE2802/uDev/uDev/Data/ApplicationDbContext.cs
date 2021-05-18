using System;
using System.Collections.Generic;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;
using uDev.Models.Entity;
using uDev.Models.ViewModels;

namespace uDev.Data
{
    public class ApplicationDbContext : IdentityDbContext<ApplicationUser>
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options): base(options)
        {}

        public virtual DbSet<Mission> Missions { get; set; }
        public virtual DbSet<Comment> Comments { get; set; }
        public virtual DbSet<Category> Categories { get; set; }
        public virtual DbSet<Transaction> Transactions { get; set; }
        public virtual DbSet<SpecialtyLanguage> SpecialtyLanguages { get; set; }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);

            var hasher = new PasswordHasher<ApplicationUser>();
            builder.Entity<ApplicationUser>().HasData(
                new ApplicationUser
                {
                    AccessFailedCount = 0,
                    ConcurrencyStamp = "13463cec-4163-48e8-8467-7af1ec336e59",
                    Email = "admin@udev.com",
                    EmailConfirmed = true,
                    Id = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4",
                    LockoutEnabled = true,
                    LockoutEnd = null, //??
                    NormalizedEmail = "ADMIN@UDEV.COM",
                    NormalizedUserName = "ADMIN@UDEV.COM",
                    PasswordHash = hasher.HashPassword(null,
                        "Adminpass1!"),
                    PhoneNumber = null,
                    PhoneNumberConfirmed = false,
                    SecurityStamp = "NPGYAUEPXSPS7MMYH6FBJY5VQEA4MFIE",
                    TwoFactorEnabled = false,
                    UserName = "admin@udev.com",
                    Transactions = null,
                    CryptoAddress = "2N3XCd9qoJjTk7GFshyKfShFoHU6LwNG1fP",
                    CryptoLabel = "default",
                    CryptoBalanceConfirmed = 0.0,
                    CryptoBalancePending = 0.0,
                },
                new ApplicationUser
                {
                    AccessFailedCount = 0,
                    ConcurrencyStamp = "954be0ca-c058-4e90-9161-e8550e99bdf3",
                    Email = "customer@udev.com",
                    EmailConfirmed = true,
                    Id = "d2b4b9aa-6b16-4917-bfd9-de6bfb0a057d",
                    LockoutEnabled = true,
                    LockoutEnd = null, //??
                    NormalizedEmail = "CUSTOMER@UDEV.COM",
                    NormalizedUserName = "CUSTOMER@UDEV.COM",
                    PasswordHash = hasher.HashPassword(null,
                        "Customerpass1!"),
                    PhoneNumber = null,
                    PhoneNumberConfirmed = false,
                    SecurityStamp = "FFWCULY3HQFS5GXZLST7EFWTPDCDQ4I3",
                    TwoFactorEnabled = false,
                    UserName = "customer@udev.com",
                    Transactions = null,
                    CryptoAddress = "2MvGWt7JJgmap5vmky91vZKECzA4KE38ZhV",
                    CryptoLabel = "fruchy55",
                    CryptoBalanceConfirmed = 0.0,
                    CryptoBalancePending = 0.0,
                },
                new ApplicationUser
                {
                    AccessFailedCount = 0,
                    ConcurrencyStamp = "ca798e6a-ef92-4fe6-b95c-7b9034e1a0a1",
                    Email = "freelancer@udev.com",
                    EmailConfirmed = true,
                    Id = "5fda77bc-30f6-492a-bf1c-1c7d390a9804",
                    LockoutEnabled = true,
                    LockoutEnd = null, //??
                    NormalizedEmail = "FREELANCER@UDEV.COM",
                    NormalizedUserName = "FREELANCER@UDEV.COM",
                    PasswordHash = hasher.HashPassword(null,
                        "Freelancerpass1!"),
                    PhoneNumber = null,
                    PhoneNumberConfirmed = false,
                    SecurityStamp = "5IBMGWDQ24Y3BP4Q3KEDYAI56A6TLXJD",
                    TwoFactorEnabled = false,
                    UserName = "freelancer@udev.com",
                    Transactions = null,
                    CryptoAddress = "2N7stQ2oHUEMKYKyUNuC9wt2YK5fu6mTXLL",
                    CryptoLabel = "refe71",
                    CryptoBalanceConfirmed = 0.0,
                    CryptoBalancePending = 0.0,
                }
            );
            
            builder.Entity<IdentityRole>().HasData(new List<IdentityRole>
            {
                new IdentityRole {ConcurrencyStamp = "e8502592-75ee-4035-8c45-aee494191354", Id = "5770c2a4-12c1-43bb-b674-c089ee446dbb", Name = "Customer", NormalizedName = "CUSTOMER"},
                new IdentityRole {ConcurrencyStamp = "e59ce5c9-5710-485c-85e2-0de4b4ffb02d", Id = "8614ecaa-f15a-4c5f-83a7-0e9118fd79be", Name = "Administrator", NormalizedName = "ADMINISTRATOR"},
                new IdentityRole {ConcurrencyStamp = "7a955259-868c-4a0c-8ba4-e6c230bcae3e", Id = "8d5a652c-05fb-483a-b5ce-6d35dca63361", Name = "Other", NormalizedName = "OTHER"},
                new IdentityRole {ConcurrencyStamp = "01d3eda7-c61e-4806-89a9-79e2676c822c", Id = "b13b6a3f-3c62-4a54-b5fb-c1ac78d1a709", Name = "Freelancer", NormalizedName = "FREELANCER"},
                new IdentityRole {ConcurrencyStamp = "1b2d022f-e237-495c-bc9b-b88d07865374", Id = "e84b3cd6-ad56-4aee-a378-a8fc158579d2", Name = "User", NormalizedName = "USER"}
            });
            
            builder.Entity<IdentityUserRole<string>>().HasData(
                new IdentityUserRole<string> {UserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", RoleId = "5770c2a4-12c1-43bb-b674-c089ee446dbb"},
                new IdentityUserRole<string> {UserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", RoleId = "8614ecaa-f15a-4c5f-83a7-0e9118fd79be"},
                new IdentityUserRole<string> {UserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", RoleId = "8d5a652c-05fb-483a-b5ce-6d35dca63361"},
                new IdentityUserRole<string> {UserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", RoleId = "b13b6a3f-3c62-4a54-b5fb-c1ac78d1a709"},
                new IdentityUserRole<string> {UserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", RoleId = "e84b3cd6-ad56-4aee-a378-a8fc158579d2"},
                new IdentityUserRole<string> {UserId = "5fda77bc-30f6-492a-bf1c-1c7d390a9804", RoleId = "b13b6a3f-3c62-4a54-b5fb-c1ac78d1a709"},
                new IdentityUserRole<string> {UserId = "5fda77bc-30f6-492a-bf1c-1c7d390a9804", RoleId = "e84b3cd6-ad56-4aee-a378-a8fc158579d2"},
                new IdentityUserRole<string> {UserId = "d2b4b9aa-6b16-4917-bfd9-de6bfb0a057d", RoleId = "5770c2a4-12c1-43bb-b674-c089ee446dbb"},
                new IdentityUserRole<string> {UserId = "d2b4b9aa-6b16-4917-bfd9-de6bfb0a057d", RoleId = "e84b3cd6-ad56-4aee-a378-a8fc158579d2"}
            );

            builder.Entity<Category>().HasData(new List<Category>
            {
                new Category {CategoryId = 1, Title = "Type", Content = "Front-end"},
                new Category {CategoryId = 2,Title = "Type", Content = "Back-end"},
                new Category {CategoryId = 3, Title = "Type", Content = "Fullstack"}
            });
            builder.Entity<SpecialtyLanguage>().HasData(new List<SpecialtyLanguage>
            {
                new SpecialtyLanguage {SpecialtyLanguageId = 1, Name = "C#"},
                new SpecialtyLanguage {SpecialtyLanguageId = 2, Name = "C++"},
                new SpecialtyLanguage {SpecialtyLanguageId = 3, Name = "JavaScript"},
                new SpecialtyLanguage {SpecialtyLanguageId = 4, Name = "PHP"}
            });

        }

        public DbSet<uDev.Models.Entity.Specialties> Specialties { get; set; }
    }
}