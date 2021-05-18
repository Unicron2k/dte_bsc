using System;
using System.Collections;
using System.Collections.Generic;
using System.Security.Principal;
using System.Threading.Tasks;
using StarCake.Server.Models.Entity;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Interfaces
{
    public interface IFlightLogRepository
    {
        Task<IEnumerable<FlightLog>> GetAll();
        Task<IEnumerable<FlightLogViewModelTable>> GetFlightLogsInEntityForTable(int? entityId, int numberOfFlightLogs);
        Task Save(FlightLog flightLog);
        FlightLog Get(int? flightLogId);
        
        
        Task<IEnumerable<FlightLogViewModelTable>> GetFlightLogTablesByIds(IEnumerable<int> flightLogIds);
        Task<List<int>> QuerySearchByDepartment(int departmentId, DateTime dateStart, DateTime dateEnd);
        Task<List<int>> QuerySearchByEntities(List<int> entityIdList, DateTime dateStart, DateTime dateEnd);

        Task<List<FlightLogViewModel>> GetViewModelsInEntity(int entityId);
        Task<FlightLogViewModel> GetViewModel(int entityId);
    }
}