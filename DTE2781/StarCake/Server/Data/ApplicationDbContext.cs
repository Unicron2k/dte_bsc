using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;
using System.Text;
using IdentityServer4.EntityFramework.Options;
using Microsoft.AspNetCore.ApiAuthorization.IdentityServer;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Options;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models;
using Entity_Organization = StarCake.Server.Models.Entity.Organization;


namespace StarCake.Server.Data
{
    // ReSharper disable UnusedAutoPropertyAccessor.Global
    // ReSharper disable PossibleMultipleEnumeration
    public class ApplicationDbContext : ApiAuthorizationDbContext<ApplicationUser>
    {
        private const int NumberOfFlightLogs = 1000;

        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options, IOptions<OperationalStoreOptions> operationalStoreOptions)
            : base(options, operationalStoreOptions)
        {
            
        }
        
        public virtual DbSet<ApplicationUser> ApplicationUsers { get; set; }
        public virtual DbSet<Component> Components { get; set; }
        public virtual DbSet<Country> Countries { get; set; }
        public virtual DbSet<Department> Departments { get; set; }
        public virtual DbSet<Entity> Entities { get; set; }
        public virtual DbSet<FlightLog> FlightLogs { get; set; }
        public virtual DbSet<Manufacturer> Manufacturers { get; set; }
        public virtual DbSet<Mission> Missions { get; set; }
        public virtual DbSet<Organization> Organizations { get; set; }
        public virtual DbSet<TypeOfOperation> TypeOfOperations { get; set; }
        public virtual DbSet<ComponentType> ComponentTypes { get; set; }
        public virtual DbSet<DepartmentApplicationUser> DepartmentApplicationUsers { get; set; }
        public virtual DbSet<FlightLogTypeOfOperation> FlightLogTypeOfOperations { get; set; }
        public virtual DbSet<EntityType> EntityTypes { get; set; }
        public virtual DbSet<MaintenanceLog> MaintenanceLogs { get; set; }
        public virtual DbSet<FileDetail> FileDetails { get; set; }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);
            
            /* RELATIONSHIP many-many  - Department <-> ApplicationUser */ 
            builder.Entity<DepartmentApplicationUser>()
                .HasKey(c => new {c.DepartmentId, c.ApplicationUserId});
            builder.Entity<DepartmentApplicationUser>()
                .HasOne(bc => bc.Department)
                .WithMany(d => d.DepartmentApplicationUsers)
                .HasForeignKey(bc => bc.DepartmentId);
            builder.Entity<DepartmentApplicationUser>()
                .HasOne(bc => bc.ApplicationUser)
                .WithMany(a => a.DepartmentApplicationUsers)
                .HasForeignKey(bc => bc.ApplicationUserId);
            
            /* RELATIONSHIP many-many  - FlightLog <-> TypeOfOperation */
            builder.Entity<FlightLogTypeOfOperation>()
                .HasKey(c => new {c.FlightLogId, c.TypeOfOperationId});
            builder.Entity<FlightLogTypeOfOperation>()
                .HasOne(bc => bc.FlightLog)
                .WithMany(d => d.FlightLogTypeOfOperations)
                .HasForeignKey(bc => bc.FlightLogId);
            builder.Entity<FlightLogTypeOfOperation>()
                .HasOne(bc => bc.TypeOfOperation)
                .WithMany(d => d.FlightLogTypeOfOperations)
                .HasForeignKey(bc => bc.TypeOfOperationId);
            
            /* ApplicationUser */
            var hasher = new PasswordHasher<ApplicationUser>();
            builder.Entity<ApplicationUser>()
                .HasIndex(x => x.EmployeeNumber)
                .IsUnique();
            builder.Entity<ApplicationUser>().HasData(
                new ApplicationUser
                {
                    Id = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4",
                    AccessFailedCount = 0,
                    ConcurrencyStamp = "13463cec-4163-48e8-8467-7af1ec336e59",
                    Email = "admin@website.com",
                    EmailConfirmed = true,
                    LockoutEnabled = true,
                    LockoutEnd = null,
                    NormalizedEmail = "ADMIN@WEBSITE.COM",
                    NormalizedUserName = "ADMIN@WEBSITE.COM",
                    PasswordHash = hasher.HashPassword(null, "Adminpass1!"),
                    PhoneNumber = "96857748",
                    PhoneNumberConfirmed = false,
                    SecurityStamp = "NPGYAUEPXSPS7MMYH6FBJY5VQEA4MFIE",
                    TwoFactorEnabled = false,
                    UserName = "admin@website.com",
                    FirstName = "Admin",
                    LastName = "User",
                    CurrentLoggedInDepartmentId = 1,
                    IsOrganizationMaintainer = true,
                    BirthDate = DateTime.Today,
                    EmployeeNumber = "AA123AA"
                },
                new ApplicationUser
                {
                    Id = "797e688f-7077-4e68-a573-3518e2edfda2",
                    AccessFailedCount = 0,
                    ConcurrencyStamp = "21ed6f2d-ec7f-4616-8302-d661f1cbe307",
                    Email = "",
                    EmailConfirmed = true,
                    LockoutEnabled = true,
                    LockoutEnd = null,
                    NormalizedEmail = "",
                    NormalizedUserName = "",
                    PasswordHash = hasher.HashPassword(null, ""),
                    PhoneNumber = "", 
                    PhoneNumberConfirmed = false,
                    SecurityStamp = "NPGYAUEPXSPS7MMYH6FBJY5VQEA4MDIE",
                    TwoFactorEnabled = false,
                    UserName = "",
                    CreationDate = DateTime.Now,
                    FirstName = "",
                    LastName = "",
                    CurrentLoggedInDepartmentId = 3,
                    IsOrganizationMaintainer = false,
                    BirthDate = DateTime.Today,
                    EmployeeNumber = "AA123BB"
                },
                new ApplicationUser
                {
                    AccessFailedCount = 0,
                    ConcurrencyStamp = "6b051d10-5865-417a-8f68-333dfddc8fe4",
                    Email = "",
                    EmailConfirmed = true,
                    Id = "2779cc6b-f760-4fac-94f2-bddd01321b6a",
                    LockoutEnabled = true,
                    LockoutEnd = null,
                    NormalizedEmail = "",
                    NormalizedUserName = "",
                    PasswordHash = hasher.HashPassword(null, ""),
                    PhoneNumber = "", 
                    PhoneNumberConfirmed = false,
                    SecurityStamp = "6OZ2S6PONAPMJZPRQKSCHIIF2BD2LD5A",
                    TwoFactorEnabled = false,
                    UserName = "",
                    CreationDate = DateTime.Now,
                    FirstName = "",
                    LastName = "",
                    CurrentLoggedInDepartmentId = 1,
                    IsOrganizationMaintainer = false,
                    BirthDate = DateTime.Today,
                    EmployeeNumber = "AA123CC"
                },
                new ApplicationUser
                {
                    AccessFailedCount = 0,
                    ConcurrencyStamp = "6eae8d67-ed3e-47d3-a66b-225e9616c612",
                    Email = "sim@uit.no",
                    EmailConfirmed = true,
                    Id = "36054a79-4b10-4fab-8bf3-74a48eab6c01",
                    LockoutEnabled = true,
                    LockoutEnd = null,
                    NormalizedEmail = "SIM@UIT.NO",
                    NormalizedUserName = "SIM@UIT.NO",
                    PasswordHash = hasher.HashPassword(null, "Qwerty1!"),
                    PhoneNumber = "48484848", 
                    PhoneNumberConfirmed = false,
                    SecurityStamp = "FGUG4FCSBZQZXYA5ADUMCCSAJBODBGFX",
                    TwoFactorEnabled = false,
                    UserName = "sim@uit.no",
                    CreationDate = DateTime.Now,
                    FirstName = "Simon",
                    LastName = "Karlsen",
                    CurrentLoggedInDepartmentId = 1,
                    IsOrganizationMaintainer = false,
                    BirthDate = DateTime.Today,
                    EmployeeNumber = "AA123DD"
                },
                new ApplicationUser
                {
                    AccessFailedCount = 0,
                    ConcurrencyStamp = "29034355-7e5d-42cf-95e9-8b073e728983",
                    Email = "",
                    EmailConfirmed = true,
                    Id = "cc7d22c3-3b99-4259-86e1-faff83e42f56",
                    LockoutEnabled = true,
                    LockoutEnd = null,
                    NormalizedEmail = "",
                    NormalizedUserName = "",
                    PasswordHash = hasher.HashPassword(null, "Qwerty1!"),
                    PhoneNumber = "48484848", 
                    PhoneNumberConfirmed = false,
                    SecurityStamp = "X25JJ46ZADBNFI7DGSF3G4KSEPQVBJYB",
                    TwoFactorEnabled = false,
                    UserName = "",
                    CreationDate = DateTime.Now,
                    FirstName = "",
                    LastName = "",
                    CurrentLoggedInDepartmentId = 2,
                    IsOrganizationMaintainer = false,
                    BirthDate = DateTime.Today,
                    EmployeeNumber = "AA123EE"
                },
                new ApplicationUser
                {
                    AccessFailedCount = 0,
                    ConcurrencyStamp = "1dbf8ec3-8eb0-41e6-aae7-406184790f86",
                    Email = "",
                    EmailConfirmed = true,
                    Id = "d8173df1-a168-4beb-bddd-6f7158f65c0f",
                    LockoutEnabled = true,
                    LockoutEnd = null,
                    NormalizedEmail = "",
                    NormalizedUserName = "",
                    PasswordHash = hasher.HashPassword(null, ""),
                    PhoneNumber = "48484848", 
                    PhoneNumberConfirmed = false,
                    SecurityStamp = "44Q4G5DMLMBFO3EKYP2OS2A3LWWNUQKX",
                    TwoFactorEnabled = false,
                    UserName = "",
                    CreationDate = DateTime.Now,
                    FirstName = "",
                    LastName = "",
                    CurrentLoggedInDepartmentId = 3,
                    IsOrganizationMaintainer = false,
                    BirthDate = DateTime.Today,
                    EmployeeNumber = "AA123FF"
                });
            
            /* IdentityRole */
            builder.Entity<IdentityRole>().HasData(new List<IdentityRole>
            {
                new IdentityRole {ConcurrencyStamp = "e59ce5c9-5710-485c-85e2-0de4b4ffb02d", Id = "8614ecaa-f15a-4c5f-83a7-0e9118fd79be", Name = "Administrator", NormalizedName = "ADMINISTRATOR"},
                new IdentityRole {ConcurrencyStamp = "7a955259-868c-4a0c-8ba4-e6c230bcae3e", Id = "8d5a652c-05fb-483a-b5ce-6d35dca63361", Name = "Organization Maintainer", NormalizedName = "ORGANIZATION MAINTAINER"},
                new IdentityRole {ConcurrencyStamp = "8d23356a-7854-11eb-9439-0242ac130002", Id = "9316169a-7854-11eb-9439-0242ac130002", Name = "Department Maintainer", NormalizedName = "DEPARTMENT MAINTAINER"},
                new IdentityRole {ConcurrencyStamp = "1b2d022f-e237-495c-bc9b-b88d07865374", Id = "e84b3cd6-ad56-4aee-a378-a8fc158579d2", Name = "User", NormalizedName = "USER"}
            });
            
            /* IdentityUserRole */
            builder.Entity<IdentityUserRole<string>>().HasData(
                new IdentityUserRole<string> { UserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", RoleId = "8614ecaa-f15a-4c5f-83a7-0e9118fd79be" }, // user_1-> Administrator
                new IdentityUserRole<string> { UserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", RoleId = "8d5a652c-05fb-483a-b5ce-6d35dca63361" }, // user_1-> Organization Maintainer
                new IdentityUserRole<string> { UserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", RoleId = "9316169a-7854-11eb-9439-0242ac130002" }, // user_1-> Department Maintainer
                new IdentityUserRole<string> { UserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", RoleId = "e84b3cd6-ad56-4aee-a378-a8fc158579d2" }, // user_1-> User
                new IdentityUserRole<string> { UserId = "797e688f-7077-4e68-a573-3518e2edfda2", RoleId = "e84b3cd6-ad56-4aee-a378-a8fc158579d2" }, // user_2 -> User
                new IdentityUserRole<string> { UserId = "797e688f-7077-4e68-a573-3518e2edfda2", RoleId = "8614ecaa-f15a-4c5f-83a7-0e9118fd79be" }, // user_2-> Administrator
                new IdentityUserRole<string> { UserId = "2779cc6b-f760-4fac-94f2-bddd01321b6a", RoleId = "e84b3cd6-ad56-4aee-a378-a8fc158579d2" }, // AJO239 -> User
                new IdentityUserRole<string> { UserId = "36054a79-4b10-4fab-8bf3-74a48eab6c01", RoleId = "e84b3cd6-ad56-4aee-a378-a8fc158579d2" }, // SIM -> User
                new IdentityUserRole<string> { UserId = "cc7d22c3-3b99-4259-86e1-faff83e42f56", RoleId = "e84b3cd6-ad56-4aee-a378-a8fc158579d2" }, // And -> User
                new IdentityUserRole<string> { UserId = "d8173df1-a168-4beb-bddd-6f7158f65c0f", RoleId = "e84b3cd6-ad56-4aee-a378-a8fc158579d2" } // MAE -> User
            );
            
            /* Organization */
            var organizations = DbTestBuilder.Organizations;
            builder.Entity<Organization>().HasData(organizations);
            
            /*Department */
            var departments = DbTestBuilder.Departments;
            builder.Entity<Department>().HasData(departments);
            
            /* DepartmentApplicationUser */
            builder.Entity<DepartmentApplicationUser>().HasData(
                new DepartmentApplicationUser{ApplicationUserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", DepartmentId = 1 , IsMaintainer = true},
                new DepartmentApplicationUser{ApplicationUserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", DepartmentId = 2 , IsMaintainer = true},
                new DepartmentApplicationUser{ApplicationUserId = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4", DepartmentId = 3 , IsMaintainer = false},
                new DepartmentApplicationUser{ApplicationUserId = "797e688f-7077-4e68-a573-3518e2edfda2", DepartmentId = 3 , IsMaintainer = false},
                new DepartmentApplicationUser{ApplicationUserId = "2779cc6b-f760-4fac-94f2-bddd01321b6a", DepartmentId = 1, IsMaintainer = false},
                new DepartmentApplicationUser{ApplicationUserId = "36054a79-4b10-4fab-8bf3-74a48eab6c01", DepartmentId = 1, IsMaintainer = false},
                new DepartmentApplicationUser{ApplicationUserId = "cc7d22c3-3b99-4259-86e1-faff83e42f56", DepartmentId = 2, IsMaintainer = false},
                new DepartmentApplicationUser{ApplicationUserId = "d8173df1-a168-4beb-bddd-6f7158f65c0f", DepartmentId = 3, IsMaintainer = false});
            
            
            
            /* Country */ 
            builder.Entity<Country>().HasData(
                new Country{CountryId = 1, Name = "Norway",  CountryCode = "NO", IsActive = true},
                new Country{CountryId = 2, Name = "Sweden",  CountryCode = "SWE", IsActive = true},
                new Country{CountryId = 3, Name = "Denmark", CountryCode = "DK", IsActive = true},
                new Country{CountryId = 4, Name = "Finland", CountryCode = "FI", IsActive = true});
            
            /* EntityType */
            builder.Entity<EntityType>().HasData(DbTestBuilder.EntityTypes);
            
            /* Manufacturer */
            var manufacturers = DbTestBuilder.Manufacturers;
            builder.Entity<Manufacturer>().HasData(manufacturers);
            
            
            /* Entity and FlightLog */
            var entities = DbTestBuilder.Entities();
            var flightLogs = DbTestBuilder.RandomFlightLogs(NumberOfFlightLogs, ref entities);
            builder.Entity<Entity>().HasData(entities);
            builder.Entity<FlightLog>().HasData(flightLogs);
            
            /* ComponentType */
            builder.Entity<ComponentType>().HasData(
                new ComponentType{ComponentTypeId = 1, Name = "Motor", IsActive = true},
                new ComponentType{ComponentTypeId = 2, Name = "Propeller", IsActive = true},
                new ComponentType{ComponentTypeId = 3, Name = "Camera", IsActive = true},
                new ComponentType{ComponentTypeId = 4, Name = "Gimbal", IsActive = true},
                new ComponentType{ComponentTypeId = 5, Name = "Battery", IsActive = true});
            
            /* Component */
            builder.Entity<Component>().HasData(
                new Component{ComponentId = 1, EntityId = 1, Name = "D5035-125KV Sensored 4pcs",          ComponentTypeId = 1, ManufacturerId = 2, DepartmentId = 1,    CreationDate = DateTime.Now,    TotalFlightCycles = 0,    TotalFlightDurationInSeconds = 0,    SerialNumber = DbTestBuilder.SerialNumber(),    CyclesSinceLastMaintenance = 20,    FlightSecondsSinceLastMaintenance = 3600,    LastMaintenanceDate = DateTime.Now,    MaxCyclesBtwMaintenance = 60,     MaxDaysBtwMaintenance = 120,   MaxFlightSecondsBtwMaintenance = 60*60*8},
                new Component{ComponentId = 2, EntityId = 1, Name = "7x3.8 Std & Reverse Rotation 2pcs",  ComponentTypeId = 2, ManufacturerId = 5, DepartmentId = 1,    CreationDate = DateTime.Now,    TotalFlightCycles = 0,    TotalFlightDurationInSeconds = 0,    SerialNumber = DbTestBuilder.SerialNumber(),    CyclesSinceLastMaintenance = 20,    FlightSecondsSinceLastMaintenance = 1600,    LastMaintenanceDate = DateTime.Now,    MaxCyclesBtwMaintenance = 120,    MaxDaysBtwMaintenance = 90,    MaxFlightSecondsBtwMaintenance = 60*60*12},
                new Component{ComponentId = 3, EntityId = 1, Name = "GoPro Hero 3",                       ComponentTypeId = 3, ManufacturerId = 6, DepartmentId = 1,    CreationDate = DateTime.Now,    TotalFlightCycles = 0,    TotalFlightDurationInSeconds = 0,    SerialNumber = DbTestBuilder.SerialNumber(),    CyclesSinceLastMaintenance = 20,    FlightSecondsSinceLastMaintenance = 1900,    LastMaintenanceDate = DateTime.Now,    MaxCyclesBtwMaintenance = 120,    MaxDaysBtwMaintenance = 90,    MaxFlightSecondsBtwMaintenance = 60*60*12},
                new Component{ComponentId = 4, EntityId = 1, Name = "GoPro Hero 5",                       ComponentTypeId = 3, ManufacturerId = 6, DepartmentId = 1,    CreationDate = DateTime.Now,    TotalFlightCycles = 0,    TotalFlightDurationInSeconds = 0,    SerialNumber = DbTestBuilder.SerialNumber(),    CyclesSinceLastMaintenance = 20,    FlightSecondsSinceLastMaintenance = 2200,    LastMaintenanceDate = DateTime.Now,    MaxCyclesBtwMaintenance = 120,    MaxDaysBtwMaintenance = 90,    MaxFlightSecondsBtwMaintenance = 60*60*12},
                new Component{ComponentId = 5, EntityId = 1, Name = "CamStabilizer 2000",                 ComponentTypeId = 4, ManufacturerId = 5, DepartmentId = 1,    CreationDate = DateTime.Now,    TotalFlightCycles = 0,    TotalFlightDurationInSeconds = 0,    SerialNumber = DbTestBuilder.SerialNumber(),    CyclesSinceLastMaintenance = 20,    FlightSecondsSinceLastMaintenance = 800,     LastMaintenanceDate = DateTime.Now,    MaxCyclesBtwMaintenance = 120,    MaxDaysBtwMaintenance = 90,    MaxFlightSecondsBtwMaintenance = 60*60*12},
                new Component{ComponentId = 6, EntityId = 1, Name = "3s 5000mAh - 50C - Gens Ace XT90",   ComponentTypeId = 5, ManufacturerId = 6, DepartmentId = 1,    CreationDate = DateTime.Now,    TotalFlightCycles = 0,    TotalFlightDurationInSeconds = 0,    SerialNumber = DbTestBuilder.SerialNumber(),    CyclesSinceLastMaintenance = 20,    FlightSecondsSinceLastMaintenance = 1800,    LastMaintenanceDate = DateTime.Now,    MaxCyclesBtwMaintenance = 120,    MaxDaysBtwMaintenance = 90,    MaxFlightSecondsBtwMaintenance = 60*60*12});

            /* TypeOfOperation */
            var typeOfOperations = new List<TypeOfOperation>
            {
                new TypeOfOperation{TypeOfOperationId = 1, IsActive = true, Name = "Proximity Flight"},
                new TypeOfOperation{TypeOfOperationId = 2, IsActive = true, Name = "Radar Flight"},
                new TypeOfOperation{TypeOfOperationId = 3, IsActive = true, Name = "View Flight"},
                new TypeOfOperation{TypeOfOperationId = 4, IsActive = true, Name = "Sample Flight"},
                new TypeOfOperation{TypeOfOperationId = 5, IsActive = true, Name = "Survey Flight"},
                new TypeOfOperation{TypeOfOperationId = 6, IsActive = true, Name = "Mapping Flight"}
            };
            builder.Entity<TypeOfOperation>().HasData(typeOfOperations);
            
            /* FlightLogTypeOfOperation */
            var flightLogTypeOfOperations = DbTestBuilder.RandomFlightLogTypeOfOperations(ref flightLogs, typeOfOperations);
            builder.Entity<FlightLogTypeOfOperation>().HasData(flightLogTypeOfOperations);
            
            /* FileDetail */
            /*var fileDetails = DbTestBuilder.FileDetails(ref entities);
            builder.Entity<FileDetail>().HasData(fileDetails);*/
            
            builder.Entity<MaintenanceLog>().HasData(
                new MaintenanceLog
                {
                    MaintenanceLogId = 1,
                    Date = default,
                    ACCSeconds = 28000,
                    TaskDescription = "General component maintenance",
                    ActionDescription = "General component oversight performed. No issues.",
                    ApplicationUserIdLogged = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4",
                    DepartmentId = 1,
                    EntityId = 1,
                    ComponentId = 1
                },
                new MaintenanceLog
                {
                    MaintenanceLogId = 2,
                    Date = default,
                    ACCSeconds = 28000,
                    TaskDescription = "General entity maintenance",
                    ActionDescription = "General entity oversight performed. Tightened various screws. Secured using red LocTite, those fuckers ain't goin' anywhere!",
                    ApplicationUserIdLogged = "10e05a2b-1eb3-47c9-89d0-b31977bb57e4",
                    DepartmentId = 1,
                    EntityId = 1,
                    ComponentId = null
                });
        }
    }
}

/// <summary>
/// @author aes014
/// To be used when seeding data, mainly in FlightLog table for testing purposes
/// </summary>
public static class DbTestBuilder
{
    private static readonly Random Random = new Random();

    private static readonly string[] UserIds = {
        "797e688f-7077-4e68-a573-3518e2edfda2", "10e05a2b-1eb3-47c9-89d0-b31977bb57e4",
    };
    
    public static readonly Organization[] Organizations = {
        new Organization {OrganizationId = 1, Name = "Fly Lavt AS", City = "Bodø", Address = "Jernbaneveien 57", ZipCode = "8012", Email = "kontakt@flylavt.no", PhoneNumber = "94979445", OperatorNumber = "NO.RPAS.00000", OrganizationNumber = "914797411", ApiKeyOpenCageData = "7aa9a926d6ba4561b2c7d924010c9ca2"},
        new Organization {OrganizationId = 2, Name = "Fly høyt", City = "Oslo", Address = "Osloveien 33", ZipCode = "0374", Email = "kontakt@flyhøyt.no", PhoneNumber = "96857425", OperatorNumber = "NO.RPAS.00001", OrganizationNumber = "93425874"}
    };
    public static readonly Department[] Departments = {
        new Department{DepartmentId = 1,Name = "Narvik",  City = "Narvik",  Address = "Lodve Langesgate 2", ZipCode = "8514", Email = "narvik@flylavt.no", PhoneNumber = "96857425", OrganizationId = 1, DeltaCycles = 20, DeltaDays = 7, DeltaSeconds = 43200, IsActive = true}, // 43 200 seconds = 12 hour
        new Department{DepartmentId = 2,Name = "Tromsø",  City = "Tromsø",  Address = "Tromsøgata 23",      ZipCode = "7597", Email = "Tromsø@flylavt.no", PhoneNumber = "91528574", OrganizationId = 1, DeltaCycles = 20, DeltaDays = 7, DeltaSeconds = 43200, IsActive = true}, // 43 200 seconds = 12 hour},
        new Department{DepartmentId = 3,Name = "Alta",    City = "Alta",    Address = "Altagata 37",        ZipCode = "9965", Email = "Alta@flyhøyt.no",   PhoneNumber = "99685214", OrganizationId = 2, DeltaCycles = 20, DeltaDays = 7, DeltaSeconds = 43200, IsActive = true} // 43 200 seconds = 12 hour}
    };
    public static readonly Manufacturer[] Manufacturers = {
        new Manufacturer{ManufacturerId = 1, Name = "DJI", IsActive = true},
        new Manufacturer{ManufacturerId = 2, Name = "Turnigy", IsActive = true},
        new Manufacturer{ManufacturerId = 3, Name = "TrackStar", IsActive = true},
        new Manufacturer{ManufacturerId = 4, Name = "3RD", IsActive = true},
        new Manufacturer{ManufacturerId = 5, Name = "HobbyKing", IsActive = true},
        new Manufacturer{ManufacturerId = 6, Name = "GoPro", IsActive = true},
        new Manufacturer{ManufacturerId = 7, Name = "Gens Ace", IsActive = true},
        new Manufacturer{ManufacturerId = 8, Name = "FLYLAVT", IsActive = true}};
    public static readonly EntityType[] EntityTypes = {
        new EntityType{EntityTypeId = 1, Name = "QuadCopter", IsActive = true},
        new EntityType{EntityTypeId = 2, Name = "HexaCopter", IsActive = true},
        new EntityType{EntityTypeId = 3, Name = "OctoCopter", IsActive = true},
        new EntityType{EntityTypeId = 4, Name = "TriCopter", IsActive = true}};

    public static IEnumerable<Entity> Entities()
    {
        var entityNames = new[] {
            "Alfa Copter", "Bravo Copter", "Charlie Copter", "Delta Copter",
            "Echo Copter", "Foxtrot Copter", "Golf Copter", "Hotel Copter",
            "India Copter", "Juliett Copter", "Kilo Copter", "Lima Copter",
            "Mike Copter", "November Copter", "Oscar Copter", "Papa Copter",
            "Quebec Copter", "Romeo Copter", "Sierra Copter", "Tango Copter",
            "Uniform Copter", "Victor Copter", "Whiskey Copter", "X-ray Copter",
            "Yankee Copter", "Zulu Copter"
        };

        var list = new List<Entity>();
        for (var i = 1; i <= entityNames.Length; i++)
        {
            var imageFileId = Guid.NewGuid();
            var creationDate = RandomDateTime(new DateTime(2012, 1, 1, 1, 1, 0), DateTime.Now);
            list.Add(new Entity
            {
                EntityId = i,
                Name = entityNames[i-1],
                EntityTypeId = EntityTypes.ElementAt(Random.Next(EntityTypes.Length)).EntityTypeId,
                DepartmentId = Departments.ElementAt(Random.Next(Departments.Length)).DepartmentId, 
                CreationDate = creationDate,
                TotalFlightCycles = 0,
                ManufacturerId = Manufacturers.ElementAt(Random.Next(Manufacturers.Length)).ManufacturerId,
                IsArchived = RandomBool(25),
                TotalFlightDurationInSeconds = 0,
                MaxCyclesBtwMaintenance = 100, 
                MaxDaysBtwMaintenance = 30,
                MaxFlightSecondsBtwMaintenance = 20000, 
                LastMaintenanceDate = DateTime.Now,
                CyclesSinceLastMaintenance = 5,
                FlightSecondsSinceLastMaintenance = 3600,
                //FileDetailId = imageFileId,
                SerialNumber = SerialNumber(),
                ImageContentType = "image/jpeg",
                ImageBase64 = "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"
            });
        }
        return list;
    }
    
    public static IEnumerable<FileDetail> FileDetails(ref IEnumerable<Entity> entities)
    {
        var entityImages = new[]
        {
            "A777341E-3BB0-4FF6-9896-37A4A17C10EE.jpeg",
            "897E235B-BF4F-4D28-8123-4DEC888B9D55.jpeg",
            "06E01026-8D06-49E7-9333-A8C6B1E9A558.jpeg",
            "D3057E9C-0DF6-47E6-B808-B999C9A5F827.jpeg",
            "F827F2BF-E718-497F-9651-391F2CC28CB7.jpeg",
            "B025822A-5946-4738-88AC-4EC81688D37E.jpeg",
            "442F109F-288E-4EEC-B867-758EAAF344FC.jpeg",
            "502F796F-6AE2-4D2B-8AF5-FF63EFFA9D54.jpeg",
            "02389273-47B4-46FC-B18F-D291EC91A386.jpeg",
            "E038F080-A071-4944-995E-A8CBED0D4E8E.jpeg",
            "27634F43-94F3-45B9-B70B-CAEC431F7FAF.jpeg",
            "661BE3F8-9EF4-4536-B22F-237C2658ED48.jpeg",
            "5B8E5DBE-7D22-4802-9F52-BB7E1070EF62.jpeg",
            "4F563733-95F7-44CE-A681-0B7AAE6674A4.jpeg",
            "0DBFEF54-0373-40A1-9C74-673CA43E88D0.jpeg",
            "DF8554D2-44F0-47B5-9AE1-0D3F938DB961.jpeg",
            "63E02A3A-AE1D-48CE-9611-7051E4E27F55.jpeg",
            "6BBDC8B4-006E-46DC-84F2-0CB9970D1EC8.jpeg",
            "3A99C9C0-D835-4918-9CC9-B9EEC8CDBB97.jpeg",
            "E1258DA0-DC94-416A-824C-A70850F46C56.jpeg",
            "B4B072CA-4197-44CD-A1E6-58E163B9152F.jpeg",
            "49DBB22E-A44E-42A7-A592-AD78152418AD.jpeg",
            "6F509977-47B0-48C9-913B-24B0130FCE7A.jpeg",
            "473BCEC3-668C-441A-8CCF-860B1A9811B0.jpeg",
            "755259AE-DFAF-4BD6-A434-353C4EE9FD1D.jpeg",
            "D072D828-B5E5-4F1A-AF2C-C4B7DB75C584.jpeg"
        };

        var list = new List<FileDetail>();
        var count = 0;
        foreach (var entity in entities)
        {
            list.Add(new FileDetail
            {
                //Id = entity.FileDetailId,
                DateEntered = entity.CreationDate,
                Deleted = false,
                DocumentName = $"{entity.Name}.jpeg",
                ContentType = "image/jpeg",
                RelativePath = "Images",
                DocumentNameServer = $"{entityImages[count]}"
            });
            count++;
        }
        return list;
    }
    
    [SuppressMessage("ReSharper", "PossibleMultipleEnumeration")]
    public static IEnumerable<FlightLog> RandomFlightLogs(int n, ref IEnumerable<Entity> entities)
    {
        var list = new List<FlightLog>();
        for (var i = 1; i <= n; i++)
        {
            var entity = entities.ElementAt(Random.Next(entities.Count()));
            var takeOffDate = entity.CreationDate.AddDays(Random.Next(1, 99));
            var secondsFlown = Random.Next(60, 1800);
            list.Add(new FlightLog
            {
                FlightLogId = i,
                DepartmentId = entity.DepartmentId,
                ApplicationUserIdPiloted = UserIds[Random.Next(0, UserIds.Length)],
                ApplicationUserIdLogged = UserIds[Random.Next(0, UserIds.Length)],
                EntityId = entity.EntityId,
                CountryId = 1,
                
                DateTakeOff = takeOffDate,
                AddressTakeOff = $"{Geolocation.RandomCity()}, {Geolocation.RandomAddress()}",
                LatitudeTakeOff = 69.123456m,
                LongitudeTakeOff = 034.123456m,
                
                SecondsFlown = secondsFlown,
                AddressLanding = $"{Geolocation.RandomCity()}, {Geolocation.RandomAddress()}",
                LatitudeLanding = 79.123456m,
                LongitudeLanding = 039.123456m,
                
                Remarks = LoremIpsum(0, 2, 0, 3, 1)
            });
            entity.TotalFlightCycles++;
            entity.TotalFlightDurationInSeconds += secondsFlown;

            //entity.TotalFlightDurationInSeconds += Convert.ToInt32(
            //    (list[^1].DateLanding - list[^1].DateTakeOff).TotalSeconds);
        }
        return list;
    }
    
    [SuppressMessage("ReSharper", "PossibleMultipleEnumeration")]
    public static IEnumerable<FlightLogTypeOfOperation> RandomFlightLogTypeOfOperations(ref IEnumerable<FlightLog> flightLogs, IEnumerable<TypeOfOperation> typeOfOperations)
    {
        IEnumerable<int> RandomTypeOfOperationIdList()
        {
            return typeOfOperations.OrderBy(x => Random.Next()).Take(Random.Next(minValue: 1, maxValue: typeOfOperations.Count())).Select(x=>x.TypeOfOperationId).ToList();
        }
        
        var list = new List<FlightLogTypeOfOperation>();
        foreach (var flightLog in flightLogs)
            foreach (var intList in RandomTypeOfOperationIdList())
                list.Add(new FlightLogTypeOfOperation{TypeOfOperationId = intList, FlightLogId = flightLog.FlightLogId});
        return list;
    }

    private static class Geolocation
    {
        private static long LongRandom(long min, long max) {
            long result = Random.Next((int)(min >> 32), (int)(max >> 32));
            result = (result << 32);
            return result | (uint) Random.Next((int)min, (int)max);
        }
        public static string RandomCoordinate(long minBoundary, long maxBoundary, long minDeciBoundary=600000, long maxDeciBoundary=950000)
        {
            return $"{LongRandom(minBoundary, maxBoundary)}.{LongRandom(minDeciBoundary, maxDeciBoundary)}";
        }
        public static string RandomCity()
        {
            var cities = new[] {
                "Tromsø", "Bodø",
                "Narvik", "Alta",
                "Oslo", "Trondheim",
                "Bergen", "Haugesund",
            };
            return cities[Random.Next(0, cities.Length)];
        }

        public static string RandomAddress(int maxHouseNumber = 99)
        {
            var addresses = new[] {
                "Storgata", "Fjellveien",
                "Hålogalands Gate", "Havnegata",
                "Bygata", "Fjordveien"
            };
            return $"{addresses[Random.Next(0, addresses.Length)]} {Random.Next(1, maxHouseNumber)}";
        }
    }

    private static bool RandomBool(int truePercentage=50)
    {
        return Random.Next(100) <= truePercentage;
    }

    private static DateTime RandomDateTime(DateTime startDate, DateTime endDate)
    {
        var timeSpan = endDate - startDate; 
        var newSpan = new TimeSpan(0, Random.Next(0, (int)timeSpan.TotalMinutes), 0);
        return startDate + newSpan;
    }

    private static string LoremIpsum(int minWords, int maxWords, int minSentences, int maxSentences, int numParagraphs, bool includeHtmlParagraphTag=false) {

        var words = new[]{"lorem", "ipsum", "dolor", "sit", "amet", "consectetuer",
            "adipiscing", "elit", "sed", "diam", "nonummy", "nibh", "euismod",
            "tincidunt", "ut", "laoreet", "dolore", "magna", "aliquam", "erat"};

        var rand = new Random();
        var numSentences = rand.Next(maxSentences - minSentences) + minSentences + 1;
        var numWords = rand.Next(maxWords - minWords) + minWords + 1;

        var result = new StringBuilder();

        for(var p = 0; p < numParagraphs; p++) {
            if (includeHtmlParagraphTag)
                result.Append("<p>");
            for(var s = 0; s < numSentences; s++) 
            {
                for (var w = 0; w < numWords; w++)
                {
                    if (w > 0)
                        result.Append(" ");
                    result.Append(words[rand.Next(words.Length)]);
                }
                result.Append(". ");
            }
            if (includeHtmlParagraphTag)
                result.Append("<p>");
        }
        return result.ToString();
    }
    
    public static string SerialNumber(int length = 10)
    {
        const string chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return new string(Enumerable.Repeat(chars, length)
            .Select(s => s[Random.Next(s.Length)]).ToArray());
    }
}
