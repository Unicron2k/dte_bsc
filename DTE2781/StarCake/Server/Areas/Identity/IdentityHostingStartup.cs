using Microsoft.AspNetCore.Hosting;
using StarCake.Server.Areas.Identity;

[assembly: HostingStartup(typeof(IdentityHostingStartup))]
namespace StarCake.Server.Areas.Identity
{
    public class IdentityHostingStartup : IHostingStartup
    {
        public void Configure(IWebHostBuilder builder)
        {
            builder.ConfigureServices((context, services) => {
            });
        }
    }
}