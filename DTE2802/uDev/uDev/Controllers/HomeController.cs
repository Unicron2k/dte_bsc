using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using uDev.Models;
using uDev.Models.Entity;
using uDev.Models.ViewModels;
using uDev.Repositories.Interface;

namespace uDev.Controllers
{
    public class HomeController : Controller
    {
        private readonly IMissionRepository _repository;
        private readonly ILogger<HomeController> _logger;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly IAuthorizationService _authorizationService;

        public HomeController(ILogger<HomeController> logger, UserManager<ApplicationUser> userManager, IMissionRepository repository, IAuthorizationService authorizationService = null)
        {
            _logger = logger;
            _userManager = userManager;
            _repository = repository;
            _authorizationService = authorizationService;
        }

        [AllowAnonymous]
        public IActionResult Index()
        {
            var list = _repository.GetAll().Result.FindAll(m => !m.Claimed && !m.Completed).ToList();
            var randomList = new List<Mission>();
            var rnd = new Random();
            var max = list.Count > 6 ? 6 : list.Count;
            for (var i = 0; i < max; i++)
            {
                randomList.Add(list[rnd.Next(0, list.Count - 1)]);
            }
            return View(randomList);
        }
        
        public IActionResult Privacy()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel {RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier});
        }
    }
}