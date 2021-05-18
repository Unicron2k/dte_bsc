using System;
using System.IO;
using Microsoft.Extensions.Configuration;

namespace uDev.Services
{
    public static class SettingsService
    {
        private static readonly IConfiguration Configuration = new ConfigurationBuilder()
                .SetBasePath(Directory.GetCurrentDirectory())
                .AddJsonFile("appsettings.json", optional: true, reloadOnChange: true)
                .AddJsonFile($"appsettings.{Environment.GetEnvironmentVariable("ASPNETCORE_ENVIRONMENT")}.json", optional: true).Build();

        public static IConfiguration GetConfiguration()
        {
            return Configuration;
        }
        
        public static IConfigurationSection GetAppSettings()
        {
            return Configuration.GetSection("AppSettings");
        }
        
    }
}