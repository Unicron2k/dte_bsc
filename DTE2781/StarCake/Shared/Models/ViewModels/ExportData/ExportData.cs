using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using StarCake.Shared.Models.ViewModels;


namespace StarCake.Shared.Models.ViewModels.ExportData
{
    public class FlightLogViewModel 
    {
        public int FlightLogId { get; set; }
        public DateTime Date { get; set; }
        public string UserPilotedNameFormal { get; set; }
        public string TypesOfOperationCommaSeparated { get; set; }
        public string PlaceCityAddress { get; set; }
        public string Address { get; set; }
        public string City { get; set; }
        public virtual ApplicationUserViewModel ApplicationUserLogged { get; set; }
        public int FlightDurationInSeconds { get; set; }
        public string UserLoggedNameFormal { get; set; }
        public int EntityId { get; set; }
        public bool HasRemarks { get; set; }
        public string EntityName { get; set; }
        public string Remarks { get; set; }
    }

    public class FlightLogViewModelForFile : Shared.Models.ViewModels.FlightLogViewModel
    {
        public string CountryCode { get; set; }
        public string OrganizationName { get; set; }
        public string DepartmentName { get; set; }

        public string coordinatesTakeOffToString()
        {
            return $"{LatitudeTakeOff}, {LongitudeTakeOff}";
        }

        public string coordinatesLandingToString()
        {
            return $"{LatitudeLanding}, {LongitudeLanding}";
        }



        // public int FlightLogId { get; set; }
        // public DateTime Date { get; set; }
        // public string OrganizationName { get; set; }
        // public string DepartmentName { get; set; }
        // public string CountryCode { get; set; }
        // public string City { get; set; }
        // public string Address { get; set; }
        // public string ZipCode { get; set; }
        // public string EntityName { get; set; }
        // public string UserPilotedNameFormal { get; set; }
        // public string UserLoggedNameFormal { get; set; }
        // public int FlightDurationInSeconds { get; set; }
        // public string Latitude { get; set; }
        // public string Longitude { get; set; }
        // public string Remarks { get; set; }
        // public string TypesOfOperationCommaSeparated { get; set; }
    }

    
    public class ApplicationUserViewModel
    {
        public string FirstName { get; set; }
        public string LastName { get; set; }

        public string GetNameFormal()
        {
            return $"{LastName}, {FirstName}";
        }
    }
}