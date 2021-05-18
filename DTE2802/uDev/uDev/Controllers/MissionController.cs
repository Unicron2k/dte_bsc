using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using uDev.Models.Entity;
using uDev.Models.ViewModels;
using uDev.Repositories.Interface;

namespace uDev.Controllers
{
    [Authorize]
    [AutoValidateAntiforgeryToken]
    public class MissionController : Controller
    {
        private readonly IMissionRepository _repository;
        private readonly ISpecialtiesLanguageRepository _specialtiesLanguageRepository;
        private readonly ILogger<MissionController> _logger;
        private readonly UserManager<ApplicationUser> _userManager;

        public MissionController(ILogger<MissionController> logger, UserManager<ApplicationUser> userManager,
            IMissionRepository repository, ISpecialtiesLanguageRepository repositorySpecialtiesLanguageRepository)
        {
            _logger = logger;
            _userManager = userManager;
            _repository = repository;
            _specialtiesLanguageRepository = repositorySpecialtiesLanguageRepository;
           
        }

        //GET: /Mission
        [AllowAnonymous]
        public IActionResult Index()
        {
            return View(_repository.GetAll().Result.FindAll(m => !m.Claimed && !m.Completed));
        }

        // GET: Mission/Details/5
        [AllowAnonymous]
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound("ID parameter is null");
            }

            var mission = await _repository.GetMission(id);
            
           if (mission == null)
           {
                return NotFound("Bad ID parameter");
            }
            return View(ToViewModel(mission));
        }

        // GET: Mission/Create
        [Authorize(Roles = "Customer")]
        public IActionResult Create()
        {
            var missionViewModel = _repository.GetMissionViewModel(null);
            return View(missionViewModel);
        }

        // POST: Mission/Create
        // To protect from over-posting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [Authorize(Roles = "Customer")]
        public async Task<IActionResult> Create([Bind("Title,Content, CategoryId, SpecialtyLanguageId")] MissionViewModel missionViewModel)
        {
            if (ModelState.IsValid)
            {
                missionViewModel.Created = DateTime.Now;
                missionViewModel.Modified=missionViewModel.Created;
                missionViewModel.Owner = await _userManager.FindByNameAsync(User.Identity.Name);

                await _repository.SaveMission(ToEntity(missionViewModel));
                
                TempData["message"] = $"\"{missionViewModel.Title}\" have been created!";
                return RedirectToAction(nameof(Index));

               ;
            }
            TempData["error"] = "There was an error. Please try again.";
            return View(missionViewModel);
        }
        
        [HttpPost]
        [Authorize(Roles = "Freelancer")]
        public async Task<IActionResult> Claim(int missionId)
        {
            bool claimedByUser = false,
                alreadyClaimedByUser = false,
                claimedByOther = false;
            var mission = _repository.GetMission(missionId).Result;
            var user = _userManager.GetUserAsync(User).Result;
            var claimingOwn = mission.Owner == user;
            if(!claimingOwn){
                if (!mission.Claimed )
                {
                    claimedByUser = true;
                    mission.Claimed = true;
                    mission.Claimers.Add(new Claimer
                    {
                        Mission = mission,
                        ApplicationUser = user
                    });
                    await _repository.UpdateMission(mission);
                }
                else
                {
                    var lastClaimer = mission.Claimers[^1].ApplicationUser;
                    if (lastClaimer != null)
                    {
                        if (lastClaimer.Id == user.Id && mission.Claimed)
                            alreadyClaimedByUser = true;
                        else
                            claimedByOther = true;
                    }
                }
            }
            
            ViewBag.claimedByUser = claimedByUser;
            ViewBag.alreadyClaimedByUser = alreadyClaimedByUser;
            ViewBag.claimedByOther = claimedByOther;
            ViewBag.ClaimingOwn = claimingOwn;
            return View(ToViewModel(mission));
        }
        
        [HttpPost]
        [Authorize(Roles = "Freelancer")]
        public async Task<IActionResult> RelinquishClaim(int missionId)
        {
            var mission = await _repository.GetMission(missionId);
            mission.Claimed = false;
            await _repository.UpdateMission(mission);
            return RedirectToAction(nameof(MyMissions));
        }
        
        [Authorize]
        public IActionResult MyMissions()
        {
            var user = _userManager.GetUserAsync(User).Result;
            var missions = _repository.GetAll().Result;
            var ownMissions = missions.FindAll(m => m.Owner == user);
            var claimedMissions = missions.FindAll(m => m.Claimed && !m.Completed && (m.Claimers.Count > 0 ? m.Claimers.Last().ApplicationUser.Id : "") == user.Id);
        
            return View(new Tuple<List<Mission>, List<Mission>>(ownMissions, claimedMissions));
        }

        // GET: Mission/Edit/5
        [Authorize(Roles = "Customer")]
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound("ID parameter is null");
            }

            var missionViewModel = _repository.GetMissionViewModel(id);
            if (missionViewModel == null)
            {
                return NotFound("Bad parameter");
            }

            if (IsOwner(ToEntity(missionViewModel)))
            {
                return View(missionViewModel);
            }
            else
            {
                TempData["message"] = $"You have not access to edit {missionViewModel.Title}.";
                return RedirectToAction("Index");
            }
            
            
        }
        // POST: Mission/Edit/5
        // To protect from over-posting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [Authorize(Roles = "Customer")]
        public async Task<IActionResult> Edit(int id, [Bind("MissionId,Title,Content,Claimed,SpecialtyLanguageId")] MissionViewModel missionViewModel)
        {
            //TODO: Add security-checks
            if (id != missionViewModel.MissionId)
            {
                return NotFound("ID parameter is null");
            }
            if (ModelState.IsValid)
            {
                try
                {
                    var mission = _repository.GetMission(id).Result;
                    if (mission == null || !IsOwner(mission))
                    {
                        return NotFound();
                    }

                    var specialty = _specialtiesLanguageRepository.GetSpecialtyLanguage(mission.SpecialtyLanguageId).Result;

                    mission.Title = missionViewModel.Title;
                    mission.Content = missionViewModel.Content;
                    mission.Claimed = missionViewModel.Claimed;
                    mission.SpecialtyLanguageId = missionViewModel.SpecialtyLanguageId;
                    mission.Modified = DateTime.Now;
                    mission.Specialtieses.Add(new Specialties
                        {
                            Mission = mission,
                            SpecialtyLanguage = specialty
                        });

                        await _repository.UpdateMission(mission);
                    
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (MissionExist(missionViewModel.MissionId)) throw;
                    TempData["error"] = "There was an error updating your blog";
                    return NotFound("Bad databaseupdate");
                }
                TempData["message"] = $"\"{missionViewModel.Title}\" have been updated!";
                return RedirectToAction(nameof(Index));
            }
            TempData["error"] = $"There was an error updating \"{missionViewModel.Title}\"";
            return View(missionViewModel);
        }

        // GET: Mission/Delete/5
        [Authorize(Roles = "Customer")]
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound("ID parameter is null");
            }

            var mission = await _repository.GetMission(id);
            if (mission == null)
            {
                return NotFound("Bad parameter or Owner");
            }

            if (IsOwner(mission))
            {
                return View(ToViewModel(mission));
            }
            else
            {
                TempData["message"] = $"You have not acess to delete {mission.Title}.";
                return RedirectToAction("Index");
            }
            
        }
        
        // POST: Mission/Delete/5
        [Authorize(Roles = "Customer")]
        [HttpPost, ActionName("Delete")]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var mission = _repository.GetMission(id).Result;
            if (mission == null || !IsOwner(mission))
                TempData["message"] = "Unauthorized!";
            else
            {
                await _repository.DeleteMission(mission);
                TempData["message"] = "Mission deleted!";
            }

            return RedirectToAction(nameof(Index));
        }
        
        private bool MissionExist(int? id)
        {
            return _repository.MissionExists(id);
        }
        
        //TODO: Set up automapper?
        private static MissionViewModel ToViewModel(Mission mission)
        {
            return new MissionViewModel
            {
                MissionId = mission.MissionId,
                Title = mission.Title,
                Content = mission.Content,
                Owner = mission.Owner,
                Created = mission.Created,
                Modified = mission.Modified,
                Claimed = mission.Claimed,
                Claimers = mission.Claimers,
                Completed = mission.Completed,
                Comments = mission.Comments,
                CategoryId = mission.CategoryId,
                SpecialtyLanguageId = mission.SpecialtyLanguageId,
                
                //might throw some shade...
                };
        }
        
        private static Mission ToEntity(MissionViewModel missionViewModel)
        {
            return new Mission
            {
                MissionId = missionViewModel.MissionId,
                Title = missionViewModel.Title,
                Content = missionViewModel.Content,
                Owner = missionViewModel.Owner,
                Created = missionViewModel.Created,
                Modified = missionViewModel.Modified,
                Claimed = missionViewModel.Claimed,
                Claimers = missionViewModel.Claimers,
                Completed = missionViewModel.Completed,
                Comments = missionViewModel.Comments,
                CategoryId = missionViewModel.CategoryId,
                Category = missionViewModel.Category,
                SpecialtyLanguageId = missionViewModel.SpecialtyLanguageId,
                //Category = missionViewModel.Categories.Find(c => c.CategoryId == missionViewModel.CategoryId ) //may also throw some shade...
            };
        }

        private bool IsOwner(Mission mission)
        {
            return mission.Owner.Id == _userManager.GetUserId(User);
        }

    }
}
