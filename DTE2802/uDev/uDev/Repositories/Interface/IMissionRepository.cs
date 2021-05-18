using System.Collections.Generic;
using System.Security.Claims;
using System.Security.Principal;
using System.Threading.Tasks;
using uDev.Models.Entity;
using uDev.Models.ViewModels;


namespace uDev.Repositories.Interface
{
    public interface IMissionRepository
    {
        public Task<List<Mission>> GetAll();
        public Task SaveMission(Mission mission);
        public Task<Mission> GetMission(int? id);
        public Task UpdateMission(Mission mission);
        public Task DeleteMission(Mission mission);
        public bool MissionExists(int? id);
        public MissionViewModel GetMissionViewModel(int? id);
    }
}
