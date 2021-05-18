using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Text.Encodings.Web;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Identity.UI.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.AspNetCore.WebUtilities;
using Microsoft.Extensions.Logging;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;

namespace StarCake.Server.Areas.Identity.Pages.Account
{
    [Authorize(Roles = "Administrator,Organization Maintainer")]
    public class RegisterModel : PageModel
    {
        private readonly SignInManager<ApplicationUser> _signInManager;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly ILogger<RegisterModel> _logger;
        private readonly IEmailSender _emailSender;
        private readonly IDepartmentRepository _departmentRepository;
        private readonly IOrganizationRepository _organizationRepository;
        private readonly IDepartmentApplicationUserRepository _departmentApplicationUserRepository;
        private readonly ApplicationDbContext _context;

        public RegisterModel(
            UserManager<ApplicationUser> userManager,
            SignInManager<ApplicationUser> signInManager,
            ILogger<RegisterModel> logger,
            IEmailSender emailSender, IOrganizationRepository organizationRepository, IDepartmentRepository departmentRepository, IHttpContextAccessor httpContextAccessor, IDepartmentApplicationUserRepository departmentApplicationUserRepository, ApplicationDbContext context)
        {
            _userManager = userManager;
            _signInManager = signInManager;
            _logger = logger;
            _emailSender = emailSender;
            _organizationRepository = organizationRepository;
            _departmentRepository = departmentRepository;
            _departmentApplicationUserRepository = departmentApplicationUserRepository;
            _context = context;
        }

        [BindProperty]
        public InputModel Input { get; set; }

        public string ReturnUrl { get; set; }

        public IList<AuthenticationScheme> ExternalLogins { get; set; }
        
        public SelectList DepartmentList { get; set; }
        
        public SelectList OrganizationList { get; set; }

        public class InputModel
        {
            [Required]
            [StringLength(100, ErrorMessage = "{0} must be at least {2} and at max {1} characters long.", MinimumLength = 1)]
            [DataType(DataType.Text)]
            [Display(Name = "First name")]
            public string FirstName { get; set; }

            [Required]
            [StringLength(100, ErrorMessage = "{0} must be at least {2} and at max {1} characters long.", MinimumLength = 1)]
            [DataType(DataType.Text)]
            [Display(Name = "Last name")]
            public string LastName { get; set; }
            
            [Required]
            [StringLength(100, ErrorMessage = "{0} must be at least {2} and at max {1} characters long.", MinimumLength = 1)]
            [DataType(DataType.Text)]
            [Display(Name = "Employee-number")]
            public string EmployeeNumber { get; set; }
            
            [Required]
            [EmailAddress]
            [Display(Name = "Email")]
            public string Email { get; set; }
            
            [Required]
            [Phone]
            [Display(Name = "PhoneNumber")]
            public string PhoneNumber { get; set; }
            
            [Required]
            [DataType(DataType.Date)]
            [Display(Name = "BirthDate")]
            public DateTime BirthDate { get; set; }

            [Required]
            [StringLength(100, ErrorMessage = "The {0} must be at least {2} and at max {1} characters long.", MinimumLength = 6)]
            [DataType(DataType.Password)]
            [Display(Name = "Password")]
            public string Password { get; set; }

            [DataType(DataType.Password)]
            [Display(Name = "Confirm password")]
            [Compare("Password", ErrorMessage = "The password and confirmation password do not match.")]
            public string ConfirmPassword { get; set; }
        }

        public async Task OnGetAsync(string returnUrl = null)
        {
            //TODO: Remove this? Not necessary?
            
            
            var organizationEntities = _organizationRepository.GetAll();
            var departmentEntities = await _departmentRepository.GetAll();
            OrganizationList = new SelectList(organizationEntities, "OrganizationId", "Name");
            DepartmentList = new SelectList(departmentEntities, "DepartmentId", "Name");

            ReturnUrl = returnUrl;
            ExternalLogins = (await _signInManager.GetExternalAuthenticationSchemesAsync()).ToList();
        }

        public async Task<IActionResult> OnPostAsync(string returnUrl = null)
        {
            returnUrl ??= Url.Content("~/");
            ExternalLogins = (await _signInManager.GetExternalAuthenticationSchemesAsync()).ToList();
            
            const string employeeNumberRegex = @"^[a-zA-Z0-9]*$";
            var regex = new Regex(employeeNumberRegex);
            if (!regex.IsMatch(Input.EmployeeNumber))
            {
                ModelState.AddModelError("EmployeeNumber", "Employee-number is not valid");
            }
            else
            {
                var user = _context.Users.FirstOrDefault(u => u.EmployeeNumber == Input.EmployeeNumber);
                if(user!=null)
                    ModelState.AddModelError("EmployeeNumber", "Employee-number already exists");
            }
            
            if (ModelState.IsValid)
            {
                var currentUser = await _userManager.FindByNameAsync(User.Identity.Name);
                
                var user = new ApplicationUser {
                    UserName = Input.Email,
                    Email = Input.Email,
                    EmployeeNumber = Input.EmployeeNumber,
                    FirstName = Input.FirstName,
                    LastName = Input.LastName,
                    BirthDate = Input.BirthDate,
                    PhoneNumber = Input.PhoneNumber,
                    CreationDate = DateTime.Now,
                    CurrentLoggedInDepartmentId = currentUser.CurrentLoggedInDepartmentId,
                    LockoutEnabled = true,
                    LockoutEnd = null,
                    IsOrganizationMaintainer = false
                };
                var result = await _userManager.CreateAsync(user, Input.Password);
                if (result.Succeeded)
                {
                    user = await _userManager.FindByIdAsync(user.Id);
                    await _departmentApplicationUserRepository.Add(new DepartmentApplicationUser
                    {
                        ApplicationUserId = user.Id,
                        DepartmentId = currentUser.CurrentLoggedInDepartmentId,
                        IsMaintainer = false
                    });
                    result = await _userManager.UpdateAsync(user);
                    if (!result.Succeeded)
                    {
                        _logger.LogInformation("Unable to add user to department. Check error logs for any errors!");
                        result = await _userManager.DeleteAsync(user);
                        if (!result.Succeeded)
                        {
                            _logger.LogInformation("Unable to remove user. Manual intervention required.");
                        }
                    }
                    _logger.LogInformation("A new user-account with password created.");

                    var code = await _userManager.GenerateEmailConfirmationTokenAsync(user);
                    code = WebEncoders.Base64UrlEncode(Encoding.UTF8.GetBytes(code));
                    var callbackUrl = Url.Page(
                        "/Account/ConfirmEmail",
                        pageHandler: null,
                        values: new { area = "Identity", userId = user.Id, code = code, returnUrl = returnUrl },
                        protocol: Request.Scheme);

                    await _emailSender.SendEmailAsync(Input.Email, "Confirm your email",
                        $"Please confirm your account by <a href='{HtmlEncoder.Default.Encode(callbackUrl)}'>clicking here</a>.");

                    if (_userManager.Options.SignIn.RequireConfirmedAccount)
                    {
                        return RedirectToPage("RegisterConfirmation", new { email = Input.Email, returnUrl = returnUrl });
                    }
                    else
                    {
                        await _userManager.AddToRoleAsync(user, "User");
                        return LocalRedirect(returnUrl);
                    }
                }
                foreach (var error in result.Errors)
                {
                    ModelState.AddModelError(string.Empty, error.Description);
                }
            }
            
            return Page();
        }
    }
}
