using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.Extensions.Options;
using StarCake.Server.Models.Entity;

namespace StarCake.Server.Services
{
    public class ApplicationUserClaimsPrincipalFactory : UserClaimsPrincipalFactory<ApplicationUser, IdentityRole>
    {
        public ApplicationUserClaimsPrincipalFactory(
                UserManager<ApplicationUser> userManager,
                RoleManager<IdentityRole> roleManager,
                IOptions<IdentityOptions> optionsAccessor)
                : base(userManager, roleManager, optionsAccessor) {
            }

            protected override async Task<ClaimsIdentity> GenerateClaimsAsync(ApplicationUser user) {
                // Generate claims from inherited
                var identity = await base.GenerateClaimsAsync(user);
                // Add our own claims to the token. Must also be added in Startup.cs
                identity.AddClaim(new Claim("FirstName", user.FirstName ?? ""));
                identity.AddClaim(new Claim("LastName", user.LastName ?? ""));
                return identity;
            }
    }
}