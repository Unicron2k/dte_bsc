using Microsoft.AspNetCore.Components.WebAssembly.Authentication;
using Microsoft.AspNetCore.Components.WebAssembly.Authentication.Internal;
using System.Linq;
using System.Security.Claims;
using System.Text.Json;
using System.Threading.Tasks;

namespace StarCake.Client
{
	public class ApplicationUserClaimsPrincipalFactory : AccountClaimsPrincipalFactory<RemoteUserAccount>
	{
		public ApplicationUserClaimsPrincipalFactory(IAccessTokenProviderAccessor accessor)
			: base(accessor)
		{
		}

		public override async ValueTask<ClaimsPrincipal> CreateUserAsync(
			RemoteUserAccount account,
			RemoteAuthenticationUserOptions options)
		{
			var user = await base.CreateUserAsync(account, options);
			var claimsIdentity = (ClaimsIdentity)user.Identity;

			if (account != null)
			{
				MapArrayClaimsToMultipleSeparateClaims(account, claimsIdentity);
			}

			return user;
		}

		private void MapArrayClaimsToMultipleSeparateClaims(RemoteUserAccount account, ClaimsIdentity claimsIdentity)
		{
			foreach (var (key, value) in account.AdditionalProperties)
			{
				if (value != null &&
				    (value is JsonElement element && element.ValueKind == JsonValueKind.Array))
				{
					claimsIdentity.RemoveClaim(claimsIdentity.FindFirst(key));
					var claims = element.EnumerateArray()
						.Select(x => new Claim(key, x.ToString()));
					claimsIdentity.AddClaims(claims);
				}
			}
		}
	}
}
