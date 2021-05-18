using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using StarCake.Shared.Models.ViewModels;

/**
 * This namespace includes ONLY the
 * data needed in StarCake/Client/Pages/FlightLogging/FlightLogging.razor
 */


namespace StarCake.Shared.Models.ViewModels.FlightLogging
{
    /*public class DepartmentViewModel
    {
        public int DepartmentId { get; set; }
        public string City { get; set; }
        
        public virtual ICollection<EntityViewModel> Entities { get; set; }
        public virtual ICollection<DepartmentApplicationUserViewModel> DepartmentApplicationUsers { get; set; }    
    }
    
    public class DepartmentApplicationUserViewModel
    {
        public string ApplicationUserId { get; set; }
        [ForeignKey("ApplicationUserId")]
        public virtual ApplicationUserViewModel ApplicationUser { get; set; }
    }
    
    public class ApplicationUserViewModel
    {
        public string Id { get; set; }
        public string UserName { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }

        public string GetNameFormal()
        {
            return $"{LastName}, {FirstName}";
        }
    }
    
    public class EntityViewModel
    {
        public int EntityId { get; set; }
        public string Name { get; set; }
        public DateTime CreationDate { get; set; }
        public int TotalFlightCycles { get; set; }
        public int TotalFlightDurationInSeconds { get; set; }
        
        public int FlightLogCount { get; set; }
        
        public virtual EntityTypeViewModel EntityTypeViewModel { get; set; }
        public virtual ICollection<ComponentViewModel> Components { get; set; }
    }
    
    public class EntityTypeViewModel
    {
        public string Name { get; set; }
    }

    public class ComponentViewModel
    {
        public int ComponentId { get; set; }
        public string Name { get; set; }
        public int EntityId { get; set; }
        public long TotalFlightCycles { get; set; }
        public long TotalFlightDurationInSeconds { get; set; }
        public int CyclesSinceLastMaint { get; set; }
        public int FlightSecondsSinceLastMaint { get; set; } = 0;

        public int MaxCyclesBtwMaint { get; set; }
        public int MaxDaysBtwMaint { get; set; }
        public int MaxFlightSecondsBtwMaint { get; set; }
        public string SerialNumber { get; set; }

        public DateTime CreationDate { get; set; }
        public DateTime LatestMaintenanceDate { get; set; }

        public string ManufacturerName { get; set; }
        public string ComponentTypeName { get; set; }
    }

    public class FlightLogViewModel 
    {
        public DateTime Date { get; set; }
        public virtual ApplicationUserViewModel ApplicationUserPiloted { get; set; }
        public virtual ICollection<FlightLogTypeOfOperationViewModel> FlightLogTypeOfOperations { get; set; }
        public string Address { get; set; }
        public string City { get; set; }
        public virtual CountryViewModel CountryViewModel { get; set; }
        public string Remarks { get; set; }
        public virtual ApplicationUserViewModel ApplicationUserLogged { get; set; }
        public int FlightDurationInSeconds { get; set; }
        public int ACCOfEntityAtFlightInSeconds { get; set; }

        public string TypesOfOperationsToString()
        {
            var names =  FlightLogTypeOfOperations.Select(x => x.TypeOfOperation.Name);
            return names.ToList().Aggregate("", (current, name) => current + $"|{name}|");
        }
        
        public string DateToYYMMDD()
        {
            return Date.ToString("yyMMdd");
        }
        
        public string DateToHHMM()
        {
            return $"{Date:HH}:{Date:mm}";
        }
        
        public string GetPLACE()
        {
            return $"{Address} / {City}";
        }

        
        public string SecondsToHHMM(int seconds)
        {
            TimeSpan t = TimeSpan.FromSeconds(seconds);
            return $"{t.Hours:D2}h:{t.Minutes:D2}m";
        }

        public string SecondsToHMM(int seconds)
        {
            TimeSpan t = TimeSpan.FromSeconds(seconds);
            return $"{t.Hours:D1}h:{t.Minutes:D2}m";
        }
    }
    
    public class FlightLogTypeOfOperationViewModel
    {
        public virtual TypeOfOperationViewModel TypeOfOperation { get; set; }
    }
    
    public class TypeOfOperationViewModel
    {
        public string Name { get; set; }
    }
    
    public class CountryViewModel
    {
        public string Name { get; set; }
        public string CountryCode { get; set; }
    }*/
}