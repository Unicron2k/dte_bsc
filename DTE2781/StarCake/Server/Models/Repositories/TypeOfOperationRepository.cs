using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;

namespace StarCake.Server.Models.Repositories
{
    public class TypeOfOperationRepository : ITypeOfOperationRepository
    {
        private readonly ApplicationDbContext _db;

        public TypeOfOperationRepository(ApplicationDbContext db)
        {
            _db = db;
        }

        public async Task<IEnumerable<TypeOfOperation>> GetAll()
        {
            return await _db.TypeOfOperations
                .ToListAsync();;
        }

        public async Task Save(TypeOfOperation typeOfOperation)
        {
            _db.TypeOfOperations.Add(typeOfOperation);
            await _db.SaveChangesAsync();
        }

        public async Task Update(TypeOfOperationViewModel typeOfOperation)
        {
            var c = new TypeOfOperation
            {
                TypeOfOperationId = typeOfOperation.TypeOfOperationId, 
                Name = typeOfOperation.Name,
                IsActive = typeOfOperation.IsActive
            };
            _db.TypeOfOperations.Update(c);
            await _db.SaveChangesAsync();
        }

        

        public TypeOfOperation Get(int? id)
        {
            var c = (from o in _db.TypeOfOperations
                where o.TypeOfOperationId == id
                select o).FirstOrDefault();
            return c;
        }
    }
}