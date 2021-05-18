using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using StarCake.Shared;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Client.Pages.NewFlightLogging
{
    public class FlightLogViewModelForm
    {
        [Required(ErrorMessage = "Select a pilot")]
        public ApplicationUserViewModel UserPilot { get; set; }
        
        [Required(ErrorMessage = "Select person who logged")]
        public ApplicationUserViewModel UserLogged { get; set; }

        [Required, MinLength(1, ErrorMessage = "Select at least one Type Of Operation")]
        public HashSet<TypeOfOperationViewModel> SelectedTypeOfOperations { get; set; }

        [Required(ErrorMessage = "Select an entity")]
        public EntityViewModel CurrentEntity { get; set; }

        [Required]
        public DateTime DateTakeOff
        {
            get => _dateTakeOff ?? DateTime.UtcNow;
            set => _dateTakeOff = value;
        }
        private DateTime? _dateTakeOff;
        
        [Required]
        public DateTime DateTakeOffClock
        {
            get => _dateTakeOffClock ?? DateTime.UtcNow;
            set => _dateTakeOffClock = value;
        }
        private DateTime? _dateTakeOffClock;
        public const string DateTakeOffClockFormat = "HHmm";

        [Required(ErrorMessage = "Select a country")]
        public CountryViewModel CurrentCountry { get; set; }
        
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Max length for {0] is {1}")]
        public string AddressTakeOff { get; set; }
        
        [RegularExpression(@"^(\+|-)?(?:90(?:(?:\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\.[0-9]{1,6})?))$",
            ErrorMessage = "TakeOff Latitude must be between -90.0 and 90.0. DD.DDDDDD")]
        public string LatitudeTakeOff { get; set; }
        [RegularExpression(@"^(\+|-)?(?:180(?:(?:\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\.[0-9]{1,6})?))$",
            ErrorMessage = "TakeOff Longitude must be between -180.0 and 180.0. DDD.DDDDDD")]
        public string LongitudeTakeOff { get; set; }

        [Required]
        [Range(1, 120, ErrorMessage = "The field {0} must be greater than {1}.")]
        public int MinutesFlown { get; set; }
        
        [StringLength(75, MinimumLength = 1, ErrorMessage = "Max length for {0] is {1}")]
        public string AddressLanding { get; set; }
        
        [RegularExpression(@"^(\+|-)?(?:90(?:(?:\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\.[0-9]{1,6})?))$",
            ErrorMessage = "TakeOff Latitude must be between -90.0 and 90.0. DD.DDDDDD")]
        public string LatitudeLanding { get; set; }
        [RegularExpression(@"^(\+|-)?(?:180(?:(?:\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\.[0-9]{1,6})?))$",
            ErrorMessage = "TakeOff Longitude must be between -180.0 and 180.0. DDD.DDDDDD")]
        public string LongitudeLanding { get; set; }
        
        [DisplayName("REMARKS")]
        [StringLength(512, MinimumLength = 0, ErrorMessage = "Max length for {0} is {1}")]
        public string Remarks { get; set; }


        public FlightLogViewModel FormToViewModel()
        {
            
            var flightLogViewModel = new FlightLogViewModel();
            flightLogViewModel.ApplicationUserIdPiloted = UserPilot.Id;
            flightLogViewModel.UserPiloted = new UserInfo {FirstName = UserPilot.FirstName, LastName = UserPilot.LastName};
            
            flightLogViewModel.ApplicationUserIdLogged = UserLogged.Id;
            flightLogViewModel.UserLogged = new UserInfo {FirstName = UserLogged.FirstName, LastName = UserLogged.LastName};

            flightLogViewModel.TypeOfOperationViewModels = SelectedTypeOfOperations.ToList();

            flightLogViewModel.EntityId = CurrentEntity.EntityId;
            flightLogViewModel.EntityName = CurrentEntity.Name;

            flightLogViewModel.CountryId = CurrentCountry.CountryId;

            flightLogViewModel.DateTakeOff = DateTakeOff;

            flightLogViewModel.DateTakeOff = new DateTime(
                DateTakeOff.Year, DateTakeOff.Month, DateTakeOff.Day, DateTakeOffClock.Hour, DateTakeOffClock.Minute, 0);

            flightLogViewModel.AddressTakeOff = AddressTakeOff;

            if (!Utils.BuiltIns.IsAnyStringNullOrEmpty(LatitudeTakeOff, LongitudeTakeOff))
            {
                flightLogViewModel.LatitudeTakeOff = Convert.ToDecimal(LatitudeTakeOff);
                flightLogViewModel.LongitudeTakeOff = Convert.ToDecimal(LongitudeTakeOff);
            }
            
            flightLogViewModel.SecondsFlown = MinutesFlown * 60;

            if (Utils.BuiltIns.IsAnyStringNullOrEmpty(AddressLanding, LatitudeLanding, LongitudeLanding))
            {
                flightLogViewModel.AddressLanding = flightLogViewModel.AddressTakeOff;
                flightLogViewModel.LatitudeLanding = flightLogViewModel.LatitudeTakeOff;
                flightLogViewModel.LongitudeLanding = flightLogViewModel.LongitudeTakeOff;
            }
            else
            {
                flightLogViewModel.AddressLanding = AddressLanding;
                flightLogViewModel.LatitudeLanding = Convert.ToDecimal(LatitudeLanding);
                flightLogViewModel.LongitudeLanding = Convert.ToDecimal(LongitudeLanding);
            }

            flightLogViewModel.Remarks = Remarks;
            
            return flightLogViewModel;
        }
    }
}