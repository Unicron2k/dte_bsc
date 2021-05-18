using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;
using Microsoft.Extensions.Hosting;
using StarCake.Server.Controllers;
using StarCake.Shared.Models;

namespace StarCake.Server.Models.Repositories
{
    public class EntityRepository : IEntityRepository
    {
        private readonly ApplicationDbContext _db;
        private readonly IHostEnvironment _environment;


        public EntityRepository(ApplicationDbContext db, IHostEnvironment environment)
        {
            _db = db;
            _environment = environment;
        }

        public async Task<IEnumerable<Entity.Entity>> GetAll()
        {
            return await _db.Entities.ToListAsync();
        }
        
        public async Task Save(Entity.Entity entity)
        {
            await _db.Entities.AddAsync(entity);
            await _db.SaveChangesAsync();
        }

        public async Task Update(Entity.Entity entity)
        {
            _db.Entities.Update(entity);
            await _db.SaveChangesAsync();
        }

        public Entity.Entity Get(int? id)
        {
            return _db.Entities.FindAsync(id).Result;
        }

        public async Task<List<EntityViewModel>> GetViewModelsInDepartment(int departmentId, IComponentRepository componentRepository, IFlightLogRepository flightLogRepository)
        {
            var entityList = await _db.Entities
                .Where(x => x.DepartmentId == departmentId)
                .Select(x=>x.EntityId)
                .ToListAsync();
            var entityViewModels = new List<EntityViewModel>();
            foreach (var viewModel in entityList.Select(entityId => GetViewModel(entityId).Result))
            {
                viewModel.Components = componentRepository.AllInEntityViewModel(viewModel.EntityId).Result;
                viewModel.FlightLogs = flightLogRepository.GetViewModelsInEntity(viewModel.EntityId).Result;
                entityViewModels.Add(viewModel);
            }
            return entityViewModels;
        }

        public async Task<EntityViewModel> GetViewModel(int entityId)
        {
            // Return entity with compressed image as Base64 in its virtual FileDetail.cs member
            var entity = await _db.Entities.FindAsync(entityId);
            return entity.MapToViewModel();
            
            
            // Get complete path of Image-file
            //var filePath = FilesController.GetFileDetailGlobalPath(entity.FileDetail);
            // Compress the Image file and generate base64-string from it
            //entity.FileDetail.FileBase64 = Utils.File.ImageEncoding.ToBase64.Compression.ImageToBase64(
            //    globalFilePath: filePath, jpegQuality: Entity.Entity.ImageEncodingQuality);
            //return entity.MapToViewModel();
        }
    }
}