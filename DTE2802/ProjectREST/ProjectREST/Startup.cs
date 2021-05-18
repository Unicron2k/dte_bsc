using System.Text;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Authorization;
using Microsoft.AspNetCore.Mvc.ViewFeatures;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.IdentityModel.Tokens;
using Newtonsoft.Json;
using ProjectREST.Authorization;
using ProjectREST.Data;
using ProjectREST.Models.Entities;
using ProjectREST.Models.Interfaces;
using ProjectREST.Repositories;

namespace ProjectREST
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.Configure<CookiePolicyOptions>(options =>
            {
                // This lambda determines whether user consent for non-essential cookies is needed for a given request.
                options.CheckConsentNeeded = context => true;
                options.MinimumSameSitePolicy = SameSiteMode.None;
            });
            services.AddDbContext<ApplicationDbContext>(options => options.UseLazyLoadingProxies().UseMySql(Configuration.GetConnectionString("DefaultConnection")));
            services.AddTransient<IBlogRepository, BlogRepository>();
            services.AddTransient<IBlogPostRepository, BlogPostRepository>();
            services.AddTransient<ICommentRepository, CommentRepository>();
            services.AddSingleton<ITempDataProvider, CookieTempDataProvider>();
            services.AddDefaultIdentity<ApplicationUser>()
                .AddRoles<IdentityRole>()
                .AddRoleManager<RoleManager<IdentityRole>>()
                .AddSignInManager<SignInManager<ApplicationUser>>()
                .AddEntityFrameworkStores<ApplicationDbContext>()
                .AddDefaultTokenProviders();
            
            //Fetch signing-key. Ideally, this should be stored in a secure vault and rotated every now and then...
            var key = Encoding.ASCII.GetBytes(Configuration.GetSection("AppSettings")["SecretKey"]);
            var tokenCookieKeyName = Configuration.GetSection("AppSettings")["JWTTokenName"];
            
            //services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
            services.AddAuthentication(/*x =>
                {
                    x.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
                    x.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
                }*/)
                .AddCookie(cfg => cfg.SlidingExpiration = true)
                //Enables jwt bearer tokens
                .AddJwtBearer(x =>
                {
                    x.RequireHttpsMetadata = false;
                    x.SaveToken = true;
                    // Allows us to send the token as a cookie instead of using an authorization-bearer in the header
                    // Basically, it allows us to partially explore the API through our browser,
                    // and bypasses the problem of not being able to handle the AntiForgery-cookie that's enabled
                    // by default in asp.net core 3.1 due to it being set to httpOnly. Disabling this feature is a bad idea.
                    // LocalStorage is an alternative but is also vulnerable for certain attacks, and does not bypass the
                    // AntiForgery-cookie-problem.
                    // Manually extracting the AntiForgery-token from the cookie might be a viable solution.
                    // See https://github.com/dotnet/aspnetcore/issues/26545 for further discussion
                    x.Events = new JwtBearerEvents
                    {
                        OnMessageReceived = context =>
                        {
                            context.Token = context.Request.Cookies[tokenCookieKeyName];
                            return Task.CompletedTask;
                        }
                    };
                    x.TokenValidationParameters = new TokenValidationParameters
                    {
                        ValidateIssuerSigningKey = true,
                        IssuerSigningKey = new SymmetricSecurityKey(key),
                        ValidateIssuer = false,
                        ValidateAudience = false,

                        //NameClaimType = ClaimTypes.NameIdentifier,
                        //RoleClaimType = ClaimTypes.Role,
                        ValidateLifetime = true
                    };

                })
                // Enable Cookie authentication
                ;//;
            
            services.AddControllersWithViews();
            services.AddRazorPages();
            services.AddMvc(config =>
            {
                // using Microsoft.AspNetCore.Mvc.Authorization;
                // using Microsoft.AspNetCore.Authorization;
                var policy = new AuthorizationPolicyBuilder()
                    .RequireAuthenticatedUser()
                    .Build();
                config.Filters.Add(new AuthorizeFilter(policy));
            }).AddNewtonsoftJson(options => options.SerializerSettings.ReferenceLoopHandling = ReferenceLoopHandling.Serialize)
              .SetCompatibilityVersion(CompatibilityVersion.Version_3_0);

            // Authorization handlers.
            services.AddScoped<IAuthorizationHandler,
                BlogPostOwnerAuthorizationHandler>();
            
            // Register the Swagger generator, defining 1 or more Swagger documents
            services.AddSwaggerGen();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseDatabaseErrorPage();
            }
            else
            {
                app.UseExceptionHandler("/Home/Error");
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }
            
            // Enable middleware to serve generated Swagger as a JSON endpoint.
            app.UseSwagger();

            // Enable middleware to serve swagger-ui (HTML, JS, CSS, etc.),
            // specifying the Swagger JSON endpoint.
            app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "Blog-API V1"));

            app.UseHttpsRedirection();
            app.UseStaticFiles();

            app.UseRouting();

            app.UseCookiePolicy();
            app.UseAuthentication();
            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllerRoute(
                    name: "default",
                    pattern: "{controller=Home}/{action=Index}/{id?}");
                endpoints.MapRazorPages();
            });
        }
    }
}