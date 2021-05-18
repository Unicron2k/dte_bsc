using System;
using System.Collections;
using System.Collections.Generic;
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
    public class FlightLogTypeOfOperationRepository : IFlightLogTypeOfOperationRepository
    {
        private Data.ApplicationDbContext db;

        public FlightLogTypeOfOperationRepository(ApplicationDbContext db)
        {
            this.db = db;
        }

        public async Task<IEnumerable<FlightLogTypeOfOperation>> GetAll()
        {
            IEnumerable<FlightLogTypeOfOperation>
                flightLogTypeOfOperations = await db.FlightLogTypeOfOperations
                    .ToListAsync();
            return flightLogTypeOfOperations;
        }

        

        public async Task Save(FlightLogTypeOfOperation flightLogTypeOfOperation)
        {
            db.FlightLogTypeOfOperations.Add(flightLogTypeOfOperation);
            db.SaveChanges();
        }
        

        public FlightLogTypeOfOperation Get(int? flightLogId)
        {
            FlightLogTypeOfOperation c = (from o in db.FlightLogTypeOfOperations
                where o.FlightLogId == flightLogId
                select o).FirstOrDefault();
            return c;
        }
        
        public Task Add(FlightLogTypeOfOperation c, IPrincipal principal)
        {
            throw new NotImplementedException();
        }
        
        public async Task Remove(FlightLogTypeOfOperation c)
        {
            db.FlightLogTypeOfOperations.Remove(c);
            await db.SaveChangesAsync();
        }
    }
}