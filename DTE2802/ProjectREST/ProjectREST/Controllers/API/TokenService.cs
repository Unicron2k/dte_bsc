using System;
using System.IdentityModel.Tokens.Jwt;
using System.IO;
using System.Security.Claims;
using System.Text;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using Microsoft.Extensions.Configuration;

namespace ProjectREST.Controllers.API
{
    
    public class TokenService : ControllerBase
    {
        private readonly byte[] _key;
        private readonly string _tokenCookieKeyName;
        private int _expirationDays = 7; //Token expires in 7 days by default

        public TokenService()
        {
            // Self-contained reading of the config-file
            var builder = new ConfigurationBuilder()
                .SetBasePath(Directory.GetCurrentDirectory())
                .AddJsonFile("appsettings.json", optional: true, reloadOnChange: true)
                .AddJsonFile($"appsettings.{Environment.GetEnvironmentVariable("ASPNETCORE_ENVIRONMENT")}.json", optional: true);
            var configuration = builder.Build();
            _tokenCookieKeyName = configuration.GetSection("AppSettings")["JWTTokenName"];
            _key = Encoding.ASCII.GetBytes(configuration.GetSection("AppSettings")["SecretKey"]);
        }

        private string GenerateToken(string username)
        {
            var claims = new[]
            {
                new Claim(ClaimTypes.Name, username),
                new Claim(JwtRegisteredClaimNames.Nbf, new DateTimeOffset(DateTime.Now).ToUnixTimeSeconds().ToString()),
                new Claim(JwtRegisteredClaimNames.Exp,
                    new DateTimeOffset(DateTime.Now.AddDays(_expirationDays)).ToUnixTimeSeconds().ToString()),
            };

            var token = new JwtSecurityToken(
                new JwtHeader(new SigningCredentials(
                    new SymmetricSecurityKey(_key),
                    SecurityAlgorithms.HmacSha256)),
                new JwtPayload(claims));
            return new JwtSecurityTokenHandler().WriteToken(token);
        }

        public void SetTokenCookie(HttpResponse response, string user)
        {
            // We use cookies to avoid the headache of not being able to easily handle the
            // AntiForgery-cookie through javascript.
            // See further comments in startup.cs
            var token = GenerateToken(user);
            var cookieOptions = new CookieOptions
            {
                Path = "/api",
                HttpOnly = true,
                IsEssential = true, //Very important!
                SameSite = SameSiteMode.Strict,
                Expires = DateTime.Now.AddDays(_expirationDays)//Cookie expires at the same time as the token
            };
            response.Cookies.Append(_tokenCookieKeyName, token, cookieOptions);
        }

        public void UnsetTokenCookie(HttpResponse response)
        {
            
            //Ideally, should invalidate the token...
            response.Cookies.Delete(_tokenCookieKeyName);
        }

        public void SetExpirationDays(int expirationDays)
        {
            _expirationDays = expirationDays;
        }
    }
}