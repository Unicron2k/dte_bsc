using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Security.Principal;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.CodeAnalysis.CSharp.Syntax;
using Microsoft.EntityFrameworkCore;
using uDev.Data;
using uDev.Models.Entity;
using uDev.Models.ViewModels;
using uDev.Repositories.Interface;

namespace uDev.Repositories
{
    public class MissionRepository : IMissionRepository
    {
        private readonly ApplicationDbContext _db;
        private readonly UserManager<ApplicationUser> _userManager;

        public MissionRepository(UserManager<ApplicationUser> userManager, ApplicationDbContext db)
        {
            _userManager = userManager;
            _db = db;
        }

        public async Task DeleteMission(Mission mission)
        {
            _db.Missions.Remove(mission);
            await _db.SaveChangesAsync();
        }

        public async Task<List<Mission>> GetAll()
        {
            return await _db.Missions.ToListAsync();
        }

        public async Task<Mission> GetMission(int? id)
        {
            if (id == null) return new Mission();
            return await _db.Missions.FirstOrDefaultAsync(m => m.MissionId == id);
        }

        public bool MissionExists(int? id)
        {
            return _db.Missions.Any(m => m.MissionId == id);
        }

        public async Task SaveMission(Mission mission)
        {
            await _db.Missions.AddAsync(mission);
            await _db.SaveChangesAsync();
        }

        public async Task UpdateMission(Mission mission)
        {
            _db.Update(mission);
            await _db.SaveChangesAsync();
        }

        public MissionViewModel GetMissionViewModel(int? id)
        {
            MissionViewModel m;
            if (id == null)
                m = new MissionViewModel();
            else
            {
                m = (from o in _db.Missions
                        where o.MissionId == id
                        select new MissionViewModel()
                        {
                            MissionId = o.MissionId,
                            CategoryId = o.Category.CategoryId,
                            Title = o.Title,
                            Owner = o.Owner,
                            Content = o.Content,
                            Category = o.Category,
                            Created = o.Created,
                            Modified = o.Modified,
                            SpecialtyLanguageId = o.SpecialtyLanguageId
                        }
                    ).FirstOrDefault();
            }

            if (m != null)
            {
                m.Categories = _db.Categories.ToList();
                m.SpecialtyLanguages = _db.SpecialtyLanguages.ToList();
            }

            return m;
        }
    }
}
