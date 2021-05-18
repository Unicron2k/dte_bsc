using System;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using uDev.Models.Entity;
using uDev.Models.ViewModels;
using uDev.Repositories.Interface;

namespace uDev.Controllers
{
    public class CommentController : Controller
    {
        
        private readonly ICommentRepository _repository;
        private readonly ILogger<CommentController> _logger;
        private readonly UserManager<ApplicationUser> _userManager;

        public CommentController(ILogger<CommentController> logger, UserManager<ApplicationUser> userManager, ICommentRepository repository)
        {
            _logger = logger;
            _userManager = userManager;
            _repository = repository;
        }

        [HttpPost]
        [Authorize(Roles = "Customer,Freelancer")]
        public async Task<IActionResult> NewComment([Bind("Content,MissionId")]CommentViewModel comment)
        {
            var mission = await _repository.GetMission(comment.MissionId);
            var user = _userManager.GetUserAsync(User).Result;
            var claimer = mission.Claimers.Count>0?mission.Claimers.Last().ApplicationUser:null;

            if (mission.Completed || user != mission.Owner && user != claimer)
            {
                TempData["error"] = "This post has been locked!";
                return RedirectToAction("Details", "Mission", new {id = mission.MissionId});
            }
            if (ModelState.IsValid)
            {
                comment.Mission = mission;
                comment.Created = DateTime.Now;
                comment.Owner = user;
                await _repository.SaveComment(ToEntity(comment));
                TempData["message"] = "Your comment have been posted";
                return RedirectToAction("Details", "Mission", new {id = mission.MissionId});
            }
            TempData["error"] = "There was an error. Please try again.";
            return RedirectToAction("Details", "Mission", new {id = mission.MissionId});
        }


        public Comment ToEntity(CommentViewModel model)
        {
            return new Comment
            {
                CommentId = model.CommentId,
                Content = model.Content,
                MissionId = model.MissionId,
                Mission = model.Mission,
                Created = model.Created,
                OwnerId = model.OwnerId,
                Owner = model.Owner
            };
        }
    }
}