using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using uDev.Models.Entity;

namespace uDev.Areas.Identity.Pages.Account.Manage
{
    public partial class IndexModel : PageModel
    {
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly SignInManager<ApplicationUser> _signInManager;

        public IndexModel(
            UserManager<ApplicationUser> userManager,
            SignInManager<ApplicationUser> signInManager)
        {
            _userManager = userManager;
            _signInManager = signInManager;
        }

        public string Username { get; set; }

        [TempData]
        public string StatusMessage { get; set; }

        [BindProperty]
        public InputModel Input { get; set; }

        public class InputModel
        {
            [Phone]
            [Display(Name = "Phone number")]
            public string PhoneNumber { get; set; }
            public bool Freelancer { get; set; }
            public bool Customer { get; set; }
        }

        private async Task LoadAsync(ApplicationUser user)
        {
            var userName = await _userManager.GetUserNameAsync(user);
            var phoneNumber = await _userManager.GetPhoneNumberAsync(user);
            var freelancer = User.IsInRole("Freelancer");
            var customer = User.IsInRole("Customer");

            Username = userName;

            Input = new InputModel
            {
                PhoneNumber = phoneNumber,
                Freelancer = freelancer,
                Customer = customer
            };
        }

        public async Task<IActionResult> OnGetAsync()
        {
            var user = await _userManager.GetUserAsync(User);
            if (user == null)
            {
                return NotFound($"Unable to load user with ID '{_userManager.GetUserId(User)}'.");
            }

            await LoadAsync(user);
            return Page();
        }

        public async Task<IActionResult> OnPostAsync()
        {
            var user = await _userManager.GetUserAsync(User);
            if (user == null)
            {
                return NotFound($"Unable to load user with ID '{_userManager.GetUserId(User)}'.");
            }

            if (!ModelState.IsValid)
            {
                await LoadAsync(user);
                return Page();
            }

            var phoneNumber = await _userManager.GetPhoneNumberAsync(user);
            if (Input.PhoneNumber != phoneNumber)
            {
                var setPhoneResult = await _userManager.SetPhoneNumberAsync(user, Input.PhoneNumber);
                if (!setPhoneResult.Succeeded)
                {
                    StatusMessage = "Unexpected error when trying to set phone number.";
                    return RedirectToPage();
                }
            }
            
            var isFreelancer = await _userManager.IsInRoleAsync(user, "Freelancer");
            if (Input.Freelancer != isFreelancer)
            {
                if (Input.Freelancer)
                {
                    var result = await _userManager.AddToRoleAsync(user, "Freelancer");
                    if (!result.Succeeded)
                    {
                        StatusMessage = "Unexpected error when trying to set role Freelancer.";
                        return RedirectToPage();
                    }
                }
                else
                {
                    var result = await _userManager.RemoveFromRoleAsync(user, "Freelancer");
                    if (!result.Succeeded)
                    {
                        StatusMessage = "Unexpected error when trying to remove role Freelancer.";
                        return RedirectToPage();
                    }
                }
            }
            
            var isCustomer = await _userManager.IsInRoleAsync(user, "Customer");
            if (Input.Customer != isCustomer)
            {
                if (Input.Customer)
                {
                    var result = await _userManager.AddToRoleAsync(user, "Customer");
                    if (!result.Succeeded)
                    {
                        StatusMessage = "Unexpected error when trying to set role Customer.";
                        return RedirectToPage();
                    }
                }
                else
                {
                    var result = await _userManager.RemoveFromRoleAsync(user, "Customer");
                    if (!result.Succeeded)
                    {
                        StatusMessage = "Unexpected error when trying to remove role Customer.";
                        return RedirectToPage();
                    }
                }
            }
            
            await _signInManager.RefreshSignInAsync(user);
            StatusMessage = "Your profile has been updated";
            return RedirectToPage();
        }
    }
}
