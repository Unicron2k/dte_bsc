using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using Microsoft.Extensions.DependencyInjection;
using System;
using System.Net.Http;
using System.Threading.Tasks;
using Blazored.Modal;
using Microsoft.AspNetCore.Components.WebAssembly.Authentication;
using MudBlazor.Services;

namespace StarCake.Client
{
    public class Program
    {
        public static async Task Main(string[] args)
        {
            var builder = WebAssemblyHostBuilder.CreateDefault(args);
            builder.RootComponents.Add<App>("app");

            builder.Services
                .AddHttpClient("StarCake.ServerAPI", client => client.BaseAddress = new Uri(builder.HostEnvironment.BaseAddress))
                .AddHttpMessageHandler<BaseAddressAuthorizationMessageHandler>();

            builder.Services
                .AddScoped(sp => new HttpClient {BaseAddress = new Uri(builder.HostEnvironment.BaseAddress)});
            builder.Services
                .AddScoped(sp => sp.GetRequiredService<IHttpClientFactory>()
                    .CreateClient("StarCake.ServerAPI"));
            
            builder.Services
                .AddSingleton<Services.AppData>();
            
            builder.Services
                .AddApiAuthorization()
                .AddAccountClaimsPrincipalFactory<ApplicationUserClaimsPrincipalFactory>();

            /* External libraries */
            builder.Services.AddBlazoredModal(); // https://github.com/Blazored/Modal
            builder.Services.AddMudServices();   // https://github.com/Garderoben/MudBlazor
            
            await builder.Build().RunAsync();
        }
    }
}
