using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.UI.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using StarCake.Server.Data;
using StarCake.Server.Models;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Server.Models.Repositories;
using StarCake.Server.Services;


namespace StarCake.Server
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        // For more information on how to configure your application, visit https://go.microsoft.com/fwlink/?LinkID=398940
        public void ConfigureServices(IServiceCollection services)
        {
            services.Configure<CookiePolicyOptions>(options =>
            {
                // This lambda determines whether user consent for non-essential cookies is needed for a given request.
                options.CheckConsentNeeded = context => true;
                options.MinimumSameSitePolicy = SameSiteMode.None;
            });
            
            //Database setup
            services.AddDbContext<ApplicationDbContext>(options =>
                options
                    .UseLazyLoadingProxies()
                    .UseMySql(Configuration.GetConnectionString("DefaultConnection"))
            ) ;
            // TODO: 32 depth overload
            services.AddControllersWithViews()
                .AddNewtonsoftJson(options =>
                    options.SerializerSettings.ReferenceLoopHandling = ReferenceLoopHandling.Serialize)
                .SetCompatibilityVersion(CompatibilityVersion.Version_3_0);
                //.AddNewtonsoftJson(options =>
                //    options.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore
                //);
            
            
            /* Web API Services */
            services.AddTransient<IApplicationUserRepository, ApplicationUserRepository>();
            services.AddTransient<IComponentTypeRepository, ComponentTypeRepository>();
            services.AddTransient<ICountryRepository, CountryRepository>();
            services.AddTransient<IDepartmentRepository, DepartmentRepository>();
            services.AddTransient<IEntityRepository, EntityRepository>();
            services.AddTransient<IEntityTypeRepository, EntityTypeRepository>();
            services.AddTransient<IFlightLogRepository, FlightLogRepository>();
            services.AddTransient<ITypeOfOperationRepository, TypeOfOperationRepository>();
            services.AddTransient<IFlightLogTypeOfOperationRepository, FlightLogTypeOfOperationRepository>();
            services.AddTransient<IExportDataRepository, ExportDataRepository>();
            services.AddTransient<IOrganizationRepository, OrganizationRepository>();
            services.AddTransient<IManufacturRepository, ManufacturRepository>();
            services.AddTransient<IDepartmentApplicationUserRepository, DepartmentApplicationUserRepository>();
            services.AddTransient<IMaintenanceLogRepository, MaintenanceLogRepository>();
            services.AddTransient<IComponentRepository, ComponentRepository>();
            
            // MailSetup
            services.Configure<EmailSettings>(Configuration.GetSection("EmailSettings"));
            services.AddTransient<IEmailSender, EmailSender>();
            
            
            //.net5.0
            //services.AddDatabaseDeveloperPageExceptionFilter();

            //Identity Setup
            //TODO: Enable Admin-approval to use account
            services.AddDefaultIdentity<ApplicationUser>(options => options.SignIn.RequireConfirmedAccount = true)
                .AddRoles<IdentityRole>()
                .AddEntityFrameworkStores<ApplicationDbContext>();
                //.AddDefaultTokenProviders()
                //.AddDefaultUI();

            //IdentityServer-setup
            services.AddIdentityServer(options =>
                {
                    options.PublicOrigin = Configuration["PublicOrigin"];
                })
                .AddApiAuthorization<ApplicationUser, ApplicationDbContext>(options =>
                {
                    //Add our own claims to the token. Must also be added in Services.ApplicationUserClaimsPrincipalFactory.cs
                    options.IdentityResources["openid"].UserClaims.Add("role");
                    options.IdentityResources["openid"].UserClaims.Add("FirstName");
                    options.IdentityResources["openid"].UserClaims.Add("LastName");
                    options.ApiResources.Single().UserClaims.Add("role");
                    options.ApiResources.Single().UserClaims.Add("FirstName");
                    options.ApiResources.Single().UserClaims.Add("LastName");
                });
            JwtSecurityTokenHandler.DefaultInboundClaimTypeMap.Remove("role");
            JwtSecurityTokenHandler.DefaultInboundClaimTypeMap.Remove("FirstName");
            JwtSecurityTokenHandler.DefaultInboundClaimTypeMap.Remove("LastName");
            
            // Add custom USerClaimsPrincipalFactory so we can add the above claims to our access token
            services.AddScoped<IUserClaimsPrincipalFactory<ApplicationUser>, ApplicationUserClaimsPrincipalFactory>();

            services.AddAuthentication()
                .AddIdentityServerJwt();

            services.AddControllersWithViews();
            services.AddRazorPages();
            //services.AddScoped<IModalService, ModalService>();


        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseMigrationsEndPoint();
                app.UseWebAssemblyDebugging();
            }
            else
            {
                app.UseExceptionHandler("/Error");
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }

            //app.UseHttpsRedirection();
            app.UseBlazorFrameworkFiles();
            app.UseStaticFiles();
            app.UseCookiePolicy();

            app.UseRouting();
            
            app.UseIdentityServer();
            app.UseAuthentication();
            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapRazorPages();
                endpoints.MapControllers();
                endpoints.MapFallbackToFile("index.html");
            });
        }
    }
}
