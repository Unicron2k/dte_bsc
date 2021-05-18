using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using AutoMapper;
using Newtonsoft.Json;
using StarCake.Shared.Models.ViewModels;

// ReSharper disable InconsistentNaming
// ReSharper disable ClassWithVirtualMembersNeverInherited.Global
// ReSharper disable UnusedAutoPropertyAccessor.Global

namespace StarCake.Server.Models.Entity
{
    
    public class FlightLog
    {
        private const string LatitudePrecision = "Decimal(8,6)";
        private const string LongitudePrecision = "Decimal(9,6)";

        public int FlightLogId { get; set; }
        
        [Required]
        public int DepartmentId { get; set; }
        [ForeignKey("DepartmentId")]
        [JsonIgnore]
        public virtual Department Department { get; set; }
        
        // PILOT IN COMMAND
        [Required]
        public string ApplicationUserIdPiloted { get; set; }
        [ForeignKey("ApplicationUserIdPiloted")]
        [JsonIgnore]
        public virtual ApplicationUser ApplicationUserPiloted { get; set; }
        
        // LOGGED BY
        [Required]
        public string ApplicationUserIdLogged { get; set; }
        [ForeignKey("ApplicationUserIdLogged")]
        [JsonIgnore]
        public virtual ApplicationUser ApplicationUserLogged { get; set; }
        
        // TYPE OF OPERATION
        public virtual ICollection<FlightLogTypeOfOperation> FlightLogTypeOfOperations { get; set; }
        
        // ENTITY
        [Required]
        public int EntityId { get; set; }
        [ForeignKey("EntityId")]
        [JsonIgnore]
        public virtual Entity Entity { get; set; }
        [NotMapped]
        public virtual string EntityName { get; set; }
        
        //// CITY
        //[Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is {0}/{1}")]
        //public string City { get; set; }
        
        // TAKEOFF COUNTRY
        [Required]
        public int CountryId { get; set; }
        [ForeignKey("CountryId")]
        public virtual Country Country { get; set; }
        
        // TAKEOFF
        [Required]
        public DateTime DateTakeOff { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is {0}/{1}")]
        public string AddressTakeOff { get; set; }
        [Column(TypeName = LatitudePrecision)]
        public decimal? LatitudeTakeOff { get; set; }
        [Column(TypeName = LongitudePrecision)]
        public decimal? LongitudeTakeOff { get; set; }
        
        // LANDING
        //[Required]
        //public DateTime DateLanding { get; set; }
        [Required]
        public int SecondsFlown { get; set; }
        
        
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is {0}/{1}")]
        public string AddressLanding { get; set; }
        [Column(TypeName = LatitudePrecision)]
        public decimal? LatitudeLanding { get; set; }
        [Column(TypeName = LongitudePrecision)]
        public decimal? LongitudeLanding { get; set; }
        
        [StringLength(512, MinimumLength = 1, ErrorMessage = "Min/Max string length is {0}/{1}")] 
        public string Remarks { get; set; }
        
        public static FlightLogViewModel MapToViewModel(FlightLog flightLog)
        {
            var config = new MapperConfiguration(cfg =>
            {;
                cfg.CreateMap<FlightLog, FlightLogViewModel>();
                    //.ForMember(x=>x.FlightLogTypeOfOperations, opt=>opt.Ignore());
            });
            var flightLogViewModel = config.CreateMapper().Map<FlightLogViewModel>(flightLog);
            flightLogViewModel.TypeOfOperationViewModels = new List<TypeOfOperationViewModel>();
            foreach (var typeOfOperation in flightLog.FlightLogTypeOfOperations.Select(x=>x.TypeOfOperation).ToList())
            {
                flightLogViewModel.TypeOfOperationViewModels.Add(TypeOfOperation.MapToViewModel(typeOfOperation));
            }
            // TODO: manually automap flightlogtypeofoperations
            return flightLogViewModel;
        }
    }
    
    /*
    public class FlightLog
    {
        public int FlightLogId { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string Address { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string City { get; set; }
        [Required, StringLength(10, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/75")]
        public string ZipCode { get; set; }
        
        [Required]
        public int CountryId { get; set; }
        [ForeignKey("CountryId")]
        public virtual Country Country { get; set; }

        [Required]
        public string ApplicationUserIdLogged { get; set; }
        [ForeignKey("ApplicationUserIdLogged")]
        [JsonIgnore]
        public virtual ApplicationUser ApplicationUserLogged { get; set; }
        
        [Required]
        public string ApplicationUserIdPiloted { get; set; }
        [ForeignKey("ApplicationUserIdPiloted")]
        [JsonIgnore]
        public virtual ApplicationUser ApplicationUserPiloted { get; set; }

        [Required]
        public int DepartmentId { get; set; }
        [ForeignKey("DepartmentId")]
        [JsonIgnore]
        public virtual Department Department { get; set; }
        
        
        [Required]
        public int EntityId { get; set; }
        [ForeignKey("EntityId")]
        [JsonIgnore]
        public virtual Entity Entity { get; set; }
        

        [Required]
        public DateTime Date { get; set; }
        [Required]
        public int FlightDurationInSeconds { get; set; }
        [Required]
        public int ACCOfEntityAtFlightInSeconds { get; set; }
        public string Remarks { get; set; }
        
        [StringLength(12, MinimumLength = 1, ErrorMessage = "Min/Max length is 1/12")]
        public string Latitude { get; set; }
        [StringLength(12, MinimumLength = 1, ErrorMessage = "Min/Max length is 1/12")]
        public string Longitude { get; set; }

        //[JsonIgnore]
        public virtual ICollection<FlightLogTypeOfOperation> FlightLogTypeOfOperations { get; set; }
        
        public FlightLogViewModel MapToViewModel()
        {
            return new FlightLogViewModel
            {
                FlightLogId = FlightLogId,
                Address = Address,
                City = City,
                ZipCode = ZipCode,
                CountryId = CountryId,
                ApplicationUserIdLogged = ApplicationUserIdLogged,
                ApplicationUserIdPiloted = ApplicationUserIdPiloted,
                DepartmentId = DepartmentId,
                EntityId = EntityId,
                Date = Date,
                FlightDurationInSeconds = FlightDurationInSeconds,
                ACCOfEntityAtFlightInSeconds = ACCOfEntityAtFlightInSeconds,
                Remarks = Remarks,
                Latitude = Latitude,
                Longitude = Longitude,
            };
        }
    }
    */

    public class TypeOfOperation
    {
        public int TypeOfOperationId { get; set; }
        [Required, StringLength(100, MinimumLength = 1, ErrorMessage = "Min/Max string length is 1/100")]
        public string Name { get; set; }
        [Required, DefaultValue(true)]
        public bool IsActive { get; set; }
        
        [JsonIgnore]
        public virtual ICollection<FlightLogTypeOfOperation> FlightLogTypeOfOperations { get; set; }
        
        public static TypeOfOperationViewModel MapToViewModel(TypeOfOperation typeOfOperation)
        {
            var config = new MapperConfiguration(cfg =>
            {
                cfg.CreateMap<TypeOfOperation, TypeOfOperationViewModel>();
            });
            return config.CreateMapper().Map<TypeOfOperationViewModel>(typeOfOperation);
        }
    }

    // Class to allow many-many relationship between FlightLog and TypeOfOperation
    public class FlightLogTypeOfOperation
    {
        public int TypeOfOperationId { get; set; }
        [ForeignKey("TypeOfOperationId")]
        [JsonIgnore]
        public virtual TypeOfOperation TypeOfOperation { get; set; }
        
        public int FlightLogId { get; set; }
        [ForeignKey("FlightLogId")]
        [JsonIgnore]
        public virtual FlightLog FlightLog { get; set; }
        
        public static FlightLogTypeOfOperationViewModel MapToViewModel(FlightLogTypeOfOperation flightLogTypeOfOperation)
        {
            var config = new MapperConfiguration(cfg =>
            {
                cfg.CreateMap<FlightLogTypeOfOperation, FlightLogTypeOfOperationViewModel>();
            });
            return config.CreateMapper().Map<FlightLogTypeOfOperationViewModel>(flightLogTypeOfOperation);
        }
    }
}
