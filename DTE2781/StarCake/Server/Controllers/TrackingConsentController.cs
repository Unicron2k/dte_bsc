using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Http.Features;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using StarCake.Server.Models.Entity;

namespace StarCake.Server.Controllers
{
    [ApiController]
    [Route("consent")]
    public class TrackingConsentController : ControllerBase
    {
        private readonly IHttpContextAccessor _httpContextAccessor;
        private readonly ITrackingConsentFeature _consentFeature;
        private readonly ILogger<TrackingConsentController> _logger;
        private readonly UserManager<ApplicationUser> _userManager;

        public TrackingConsentController(
            ILogger<TrackingConsentController> logger,
            UserManager<ApplicationUser> userManager,
            IHttpContextAccessor httpContextAccessor
            )
        {
            _logger = logger;
            _userManager = userManager;
            _httpContextAccessor = httpContextAccessor;
            _consentFeature = _httpContextAccessor.HttpContext.Features.Get<ITrackingConsentFeature>();
        }
        
        // GET
        [Route("grant")]
        [Route("")]
        [HttpGet]
        public async Task Grant()
        {
            _consentFeature?.GrantConsent();
            if (_consentFeature?.CanTrack == true)
            {
                var userId =  _httpContextAccessor.HttpContext.User.FindFirst(ClaimTypes.NameIdentifier).Value;
                var currentUser = await _userManager.FindByIdAsync(userId);
                if (currentUser != null)
                {
                    currentUser.CanTrack = true;
                    await _userManager.UpdateAsync(currentUser);
                }
                _logger.Log(LogLevel.Information, "Tracking consent granted");
            }
            else
            {
                _logger.Log(LogLevel.Information, "Failed to apply tracking consent");
            }
        }
        
        // GET
        [Route("withdraw")]
        [HttpGet]
        public async Task<IActionResult> Withdraw()
        {
            _consentFeature?.WithdrawConsent();
            if (_consentFeature?.CanTrack == false)
            {
                var userId = _httpContextAccessor.HttpContext.User.FindFirst(ClaimTypes.NameIdentifier).Value;
                var currentUser = await _userManager.FindByIdAsync(userId);
                if (currentUser != null)
                {
                    currentUser.CanTrack = false;
                    await _userManager.UpdateAsync(currentUser);
                }
                _logger.Log(LogLevel.Information, "Tracking consent withdrawn");
                return Ok();
            }

            _logger.Log(LogLevel.Information, "Failed to revoke tracking consent");
            return StatusCode(500);
        }
    }
}