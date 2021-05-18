//TODO: Move to a repository

using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using uDev.Models.Entity;

namespace uDev.Controllers
{
    [Authorize(Roles = "Administrator")]
    [AutoValidateAntiforgeryToken]
    public class ClaimsController : Controller
    {
        private UserManager<ApplicationUser> _userManager;
 
        public ClaimsController(UserManager<ApplicationUser> userManager)
        {
            _userManager = userManager;
        }
        
        public ViewResult Index() => View(User?.Claims);
 
        public ViewResult Create() => View();

        [HttpPost]
        public async Task<IActionResult> Create(string claimType, string claimValue)
        {
            var user = await _userManager.GetUserAsync(HttpContext.User);
            var claim = new Claim(claimType, claimValue, ClaimValueTypes.String);
            var result = await _userManager.AddClaimAsync(user, claim);
 
            if (result.Succeeded)
                return RedirectToAction("Index");
            else
                Errors(result);
            return View();
        }

        [HttpPost]
        public async Task<IActionResult> Delete(string claimValues)
        {
            var user = await _userManager.GetUserAsync(HttpContext.User);
 
            var claimValuesArray = claimValues.Split(";");
            string claimType = claimValuesArray[0], claimValue = claimValuesArray[1], claimIssuer = claimValuesArray[2];
 
            var claim = User.Claims.FirstOrDefault(x => x.Type == claimType && x.Value == claimValue && x.Issuer == claimIssuer);
 
            var result = await _userManager.RemoveClaimAsync(user, claim);
 
            if (result.Succeeded)
                return RedirectToAction("Index");
            else
                Errors(result);
 
            return View("Index");
        }
        
        private void Errors(IdentityResult result)
        {
            foreach (var error in result.Errors)
                ModelState.AddModelError("", error.Description);
        }
    }
}