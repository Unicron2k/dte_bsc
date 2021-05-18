using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.Immutable;
using System.Linq;
using System.Security.Principal;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Repositories
{
    public class FlightLogRepository : IFlightLogRepository
    {
        private Data.ApplicationDbContext db;

        public FlightLogRepository(ApplicationDbContext db)
        {
            this.db = db;
        }

        public async Task<IEnumerable<FlightLog>> GetAll()
        {
            IEnumerable<FlightLog>
                flightLogs = await db.FlightLogs
                    .ToListAsync();
            return flightLogs;
        }

        public async Task<IEnumerable<FlightLogViewModelTable>> GetFlightLogsInEntityForTable(int? entityId, int numberOfFlightLogs)
        {
            var flightLogIds = db.FlightLogs
                .OrderBy(x => x.DateTakeOff)
                .Where(x => x.EntityId == entityId)
                .Select(x => x.FlightLogId).Take(numberOfFlightLogs).ToList();
            return await GetFlightLogTablesByIds(flightLogIds);; 
        }
        
        public async Task<IEnumerable<FlightLogViewModelTable>> GetFlightLogTablesByIds(IEnumerable<int> flightLogIds)
        {
            var flightLogs = await db.FlightLogs
                .Where(x => flightLogIds.Contains(x.FlightLogId))
                .Select(m => new FlightLogViewModelTable
                {
                    FlightLogId = m.FlightLogId,
                    Address = m.AddressTakeOff,
                    City = "m.City",
                    ZipCode = "m.ZipCode",
                    CountryId = m.CountryId,
                    ApplicationUserIdLogged = m.ApplicationUserIdLogged,
                    ApplicationUserLoggedNameFormal = db.ApplicationUsers
                        .Where(x => x.Id == m.ApplicationUserIdLogged)
                        .Select(x => new string($"{x.LastName}, {x.FirstName}")).FirstOrDefault(),
                    ApplicationUserIdPiloted = m.ApplicationUserIdPiloted,
                    ApplicationUserPilotedNameFormal = db.ApplicationUsers
                        .Where(x => x.Id == m.ApplicationUserIdPiloted)
                        .Select(x => new string($"{x.LastName}, {x.FirstName}")).FirstOrDefault(),
                    DepartmentId = m.DepartmentId,
                    EntityId = m.EntityId,
                    EntityName = db.Entities.Where(x => x.EntityId == m.EntityId).Select(x => x.Name).FirstOrDefault(),
                    FlightLogTypeOfOperations = db.FlightLogTypeOfOperations
                        .Where(x => x.FlightLogId == m.FlightLogId)
                        .Select(x => new FlightLogTypeOfOperationViewModel
                        {
                            TypeOfOperationId = x.TypeOfOperationId,
                            TypeOfOperation = db.TypeOfOperations.Where(t => t.TypeOfOperationId == x.TypeOfOperationId)
                                .Select(t => new TypeOfOperationViewModel
                                {
                                    Name = t.Name,
                                    TypeOfOperationId = t.TypeOfOperationId
                                }).FirstOrDefault()
                        }).ToList(),
                    Date = m.DateTakeOff,
                    FlightDurationInSeconds = m.SecondsFlown,
                    Remarks = m.Remarks,
                    Latitude = "m.Latitude",
                    Longitude = "m.Longitude",
                    HasRemarks = (m.Remarks != null && !m.Remarks.Equals("")),
                }).ToListAsync();
            return flightLogs;
        }
        
        public FlightLog Get(int? flightLogId)
        {
            var flightLog = (from o in db.FlightLogs
                where o.FlightLogId == flightLogId
                select o).FirstOrDefault();
            return flightLog;
        }

        

        /* Search queries */
        public async Task<List<int>> QuerySearchByDepartment(int departmentId, DateTime dateStart, DateTime dateEnd)
        {
            var flightLogIds = await db.FlightLogs
                .Where(x => x.DepartmentId == departmentId)
                .Where(x => x.DateTakeOff >= dateStart && x.DateTakeOff <= dateEnd)
                .OrderByDescending(x => x.DateTakeOff)
                .Select(x => x.FlightLogId).ToListAsync();
            return flightLogIds;
        }
        
        public async Task<List<int>> QuerySearchByEntities(List<int> entityIdList, DateTime dateStart, DateTime dateEnd)
        {
            var flightLogIds = await db.FlightLogs
                .Where(x => entityIdList.Contains(x.EntityId))
                .Where(x=>x.DateTakeOff >= dateStart && x.DateTakeOff <= dateEnd)
                .OrderByDescending(x=>x.DateTakeOff)
                .Select(x=>x.FlightLogId).ToListAsync();
            return flightLogIds;
        }

        public async Task<List<FlightLogViewModel>> GetViewModelsInEntity(int entityId)
        {
            var flightLogIds = db.FlightLogs
                .Where(x => x.EntityId == entityId)
                .Select(x => x.FlightLogId).ToListAsync();

            var flightLogs = new List<FlightLogViewModel>();
            foreach (var id in flightLogIds.Result)
                flightLogs.Add(await GetViewModel(id));
            return flightLogs;
        }

        public async Task<FlightLogViewModel> GetViewModel(int flightLogId)
        {
            var flightLog = await db.FlightLogs
                .Where(x => x.FlightLogId == flightLogId)
                .Select(f => new FlightLogViewModel
                {
                    FlightLogId = f.FlightLogId,
                    ApplicationUserIdPiloted = f.ApplicationUserIdPiloted,
                    UserPiloted = db.ApplicationUsers
                        .Where(x => x.Id == f.ApplicationUserIdPiloted)
                        .Select(piloted => new UserInfo
                        {
                            FirstName = piloted.FirstName,
                            LastName = piloted.LastName
                        }).FirstOrDefault(),
                    ApplicationUserIdLogged = f.ApplicationUserIdLogged,
                    UserLogged = db.ApplicationUsers
                        .Where(x => x.Id == f.ApplicationUserIdPiloted)
                        .Select(logged => new UserInfo
                        {
                            FirstName = logged.FirstName,
                            LastName = logged.LastName
                        }).FirstOrDefault(),
                    TypeOfOperationViewModels = db.FlightLogTypeOfOperations
                        .Where(x => x.FlightLogId == f.FlightLogId)
                        .Select(z => new TypeOfOperationViewModel
                        {
                            TypeOfOperationId = z.TypeOfOperation.TypeOfOperationId,
                            Name = z.TypeOfOperation.Name,
                            IsActive = z.TypeOfOperation.IsActive
                        }).ToList(),
                    EntityId = f.EntityId,
                    EntityName = db.Entities.FirstOrDefault(x => x.EntityId == f.EntityId).Name,
                    CountryId = f.CountryId,
                    DateTakeOff = f.DateTakeOff,
                    AddressTakeOff = f.AddressTakeOff,
                    LatitudeTakeOff = f.LatitudeTakeOff,
                    LongitudeTakeOff = f.LongitudeTakeOff,
                    SecondsFlown = f.SecondsFlown,
                    AddressLanding = f.AddressLanding,
                    LatitudeLanding = f.LatitudeLanding,
                    LongitudeLanding = f.LongitudeLanding,
                    Remarks = f.Remarks
                }).FirstAsync();
            return flightLog;
        }

        public async Task Save(FlightLog flightLog)
        {
            // Add the new FlightLog
            await db.FlightLogs.AddAsync(flightLog);
            
            // Update Entity used in the FlightLog
            var entity = db.Entities.FindAsync(flightLog.EntityId).Result;
            entity.TotalFlightCycles++;
            entity.TotalFlightDurationInSeconds += flightLog.SecondsFlown;
            entity.CyclesSinceLastMaintenance++;
            entity.FlightSecondsSinceLastMaintenance += flightLog.SecondsFlown;

            db.Entities.Update(entity);
            
            // Update all components in the Entity
            var components = db.Components
                .Where(x => x.EntityId == flightLog.EntityId)
                .ToList();
            foreach (var component in components)
            {
                component.TotalFlightCycles++;
                component.TotalFlightDurationInSeconds += flightLog.SecondsFlown;
                component.CyclesSinceLastMaintenance++;
                component.FlightSecondsSinceLastMaintenance += flightLog.SecondsFlown;
            }
            db.Components.UpdateRange(components);
            
            // Save changes 
            await db.SaveChangesAsync();
        }
    }
}