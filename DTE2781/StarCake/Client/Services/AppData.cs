using System;
using System.Collections.Generic;
using System.Linq;
using StarCake.Shared.Models.ViewModels;
using MudBlazor;

namespace StarCake.Client.Services
{
    //Implement OnChangedNotify
    //As explained here: https://wellsb.com/csharp/aspnet/blazor-singleton-pass-data-between-pages/
    public class AppData
    {
        public List<FlightLogViewModel> PendingFlightLogs = new List<FlightLogViewModel>();
        
        public static ICollection<DepartmentViewModel> AvailableDepartments { get; set; }
        
        private static bool _isLoaded;
        public static bool IsLoaded
        {
            get => _isLoaded;
            set {
                _isLoaded = value;
                NotifyDataChanged();
            }
        }

        public static event Action OnChange;
        private static void NotifyDataChanged() => OnChange?.Invoke();
        public static ApplicationUserViewModel CurrentUser { get; set; }
        
        public List<TypeOfOperationViewModel> TypeOfOperations { get; set; }
        public List<DepartmentViewModel> AllDepartments { get; set; }
        public ICollection<ComponentTypeViewModel> ComponentTypes { get; set; }
        public ICollection<ManufacturerViewModel> Manufacturers { get; set; }
        public List<CountryViewModel> Countries { get; set; }
        
        
        public ICollection<EntityTypeViewModel> EntityTypes { get; set; }
        public bool IsEntityTypesNullOrEmpty()
        {
            if (EntityTypes==null)
                return true;
            return !EntityTypes.Any();
        }
        
        public MudTheme CurrentMudTheme = MudThemes.GetThemeNormal();
        public void EnableDarkMode(bool enabled)
        {
            CurrentMudTheme = enabled ? MudThemes.GetThemeDarkMode() : MudThemes.GetThemeNormal();
            ParentRoot.RefreshApp();
        }
        public App ParentRoot { get; set; }
    }
    
    public static class MudThemes
    {
        public static MudTheme GetThemeDarkMode()
        {
            var darkTheme = new MudTheme()
            {
                Palette = new Palette()
                {
                    Black = "#27272f",
                    Background = "#32333d",
                    BackgroundGrey = "#27272f",
                    Surface = "#373740",
                    DrawerBackground = "#27272f",
                    DrawerText = "rgba(255,255,255, 0.50)",
                    DrawerIcon = "rgba(255,255,255, 0.50)",
                    AppbarBackground = "#27272f",
                    AppbarText = "rgba(255,255,255, 0.70)",
                    TextPrimary = "rgba(255,255,255, 0.70)",
                    TextSecondary = "rgba(255,255,255, 0.50)",
                    ActionDefault = "#adadb1",
                    ActionDisabled = "rgba(255,255,255, 0.26)",
                    ActionDisabledBackground = "rgba(255,255,255, 0.12)"
                }
            };
            return darkTheme;
        }

        public static MudTheme GetThemeNormal()
        {
            var defaultTheme = new MudTheme()
            {
                Palette = new Palette()
                {
                    Black = "#272c34"
                }
            };
            return defaultTheme;
        }
    }
}