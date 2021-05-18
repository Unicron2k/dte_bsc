using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;

namespace StarCake.Shared.Models.ViewModels
{
    public class UserInfo
    {
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string GetNameFormal()
        {
            return $"{LastName}, {FirstName}";
        }
    }

    
    public class FlightLogViewModel
    {
        private const string LatitudePrecision = "Decimal(8,6)";
        private decimal LatitudeDefaultValue = 00.000000m;
        private const string LongitudePrecision = "Decimal(9,6)";
        private decimal LongitudeDefaultValue = 000.000000m;

        public int FlightLogId { get; set; }
        [Required]
        public int DepartmentId { get; set; }

        // PILOT IN COMMAND
        [Required] public string ApplicationUserIdPiloted { get; set; }
        public virtual UserInfo UserPiloted { get; set; }

        // LOGGED BY
        [Required] public string ApplicationUserIdLogged { get; set; }
        public virtual UserInfo UserLogged { get; set; }

        // TYPE OF OPERATION
        //public virtual List<FlightLogTypeOfOperationViewModel> FlightLogTypeOfOperations { get; set; }
        public virtual List<TypeOfOperationViewModel> TypeOfOperationViewModels { get; set; }
        public string TypeOfOperationsCommaSeparated()
        {
            return string.Join(", ", TypeOfOperationViewModels.OrderBy(x=>x.Name).Select(x=>x.Name));
        }

        // ENTITY
        [Required] public int EntityId { get; set; }
        [NotMapped] public virtual string EntityName { get; set; }

        // TAKEOFF COUNTRY
        [Required] public int CountryId { get; set; }

        // TAKEOFF POSITION
        [Required] public DateTime DateTakeOff { get; set; }
        public string DateTakeOffyymmdd() { return DateTimeToyymmdd(DateTakeOff); }
        public string DateTakeOffClock() { return DateTakeOff.ToString("HH:mm"); }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is {0}/{1}")]
        public string AddressTakeOff { get; set; }
        [Column(TypeName = LatitudePrecision)]
        public decimal? LatitudeTakeOff { get; set; }
        [Column(TypeName = LongitudePrecision)]
        public decimal? LongitudeTakeOff { get; set; }

        [Required]
        public int SecondsFlown { get; set; }

        public string DateLandingyymmdd()
        {
            var newDate = DateTakeOff.AddSeconds(SecondsFlown);
            return DateTimeToyymmdd(newDate);
        }
        
        // LANDING POSITION
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is {0}/{1}")]
        public string AddressLanding { get; set; }
        [Column(TypeName = LatitudePrecision)]
        public decimal? LatitudeLanding { get; set; }
        [Column(TypeName = LongitudePrecision)]
        public decimal? LongitudeLanding { get; set; }
        [StringLength(512, MinimumLength = 1, ErrorMessage = "Min/Max string length is {0}/{1}")] 
        public string Remarks { get; set; }

        
        
        
        
        private static string DateTimeToyymmdd(DateTime dateTime)
        {
            return dateTime.ToString("yyMMdd");
        }
        public FlightLogViewModel Clone()
        {
            return (FlightLogViewModel) MemberwiseClone();
        }
    }
    

    
    
    public class FlightLogViewModelTable
    {
        public int FlightLogId { get; set; }
        public string Address { get; set; }
        public string City { get; set; }
        public string ZipCode { get; set; }
        public int CountryId { get; set; }
        public string ApplicationUserIdLogged { get; set; } = null;
        public string ApplicationUserLoggedNameFormal { get; set; } = null;
        public string ApplicationUserIdPiloted { get; set; }
        public string ApplicationUserPilotedNameFormal { get; set; }
        public int DepartmentId { get; set; }
        public int EntityId { get; set; }
        public string EntityName { get; set; }
        public virtual ICollection<FlightLogTypeOfOperationViewModel> FlightLogTypeOfOperations { get; set; }
        public DateTime Date { get; set; }
        public int FlightDurationInSeconds { get; set; }
        public int ACCOfEntityAtFlightInSeconds { get; set; }
        public string Remarks { get; set; }
        public string Latitude { get; set; }
        public string Longitude { get; set; }
        public bool HasRemarks { get; set; }


        public string TypesOfOperationsCommaSeparated() {
            return string.Join(", ", FlightLogTypeOfOperations.Select(x => x.TypeOfOperation.Name).ToList());
        }

        public string DateToHHMM() {
            return $"{Date:HH}:{Date:mm}";
        }
        public string GetPlace() {
            return $"{Address} / {City}";
        }
    }
    
    
    

    public class TypeOfOperationViewModel
    {
        public int TypeOfOperationId { get; set; }
        [Required, StringLength(100, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/100")]
        public string Name { get; set; }
        [Required, DefaultValue(true)]
        public bool IsActive { get; set; }
        
        public TypeOfOperationViewModel Clone()
        {
            return (TypeOfOperationViewModel) MemberwiseClone();
        }
        
        public void Update(TypeOfOperationViewModel typeOfOperation)
        {
            Name = typeOfOperation.Name;
            IsActive = typeOfOperation.IsActive;
        }
    }
    
    // Class to allow many-many relationship between FlightLog and TypeOfOperation
    public class FlightLogTypeOfOperationViewModel
    {
        public int TypeOfOperationId { get; set; }
        [ForeignKey("TypeOfOperationId")]
        public virtual TypeOfOperationViewModel TypeOfOperation { get; set; }
        
        //public int FlightLogId { get; set; }
    }
}
