using System.Collections;
using System.Collections.Generic;
using System.Security.Principal;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IFlightLogTypeOfOperationRepository
    {
        Task<IEnumerable<FlightLogTypeOfOperation>> GetAll();
        FlightLogTypeOfOperation Get(int? flightLogId);
        Task Add(FlightLogTypeOfOperation c, IPrincipal principal);
        Task Remove(FlightLogTypeOfOperation c);
        Task Save(FlightLogTypeOfOperation flightLogTypeOfOperation);
    }
}