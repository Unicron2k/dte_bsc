using System;
using System.Collections.Generic;
using System.Dynamic;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Logging;
using uDev.Data;
using uDev.Models.Entity;
using uDev.Models.ViewModels;
using uDev.Repositories.Interface;
using uDev.Services;

namespace uDev.Controllers
{
    [Authorize]
    [AutoValidateAntiforgeryToken]
    public class TransactionController : Controller
    {
        
        private readonly ILogger<TransactionController> _logger;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly RoleManager<IdentityRole> _roleManager;
        private readonly IAuthorizationService _authorizationService;
        private readonly CryptoCoinService _cryptoCoinService;

        public TransactionController(ILogger<TransactionController> logger, UserManager<ApplicationUser> userManager, RoleManager<IdentityRole> roleManager, ITransactionRepository transactionRepository, IAuthorizationService authorizationService = null)
        {
            _logger = logger;
            _userManager = userManager;
            _roleManager = roleManager;
            _authorizationService = authorizationService;
            _cryptoCoinService = new CryptoCoinService(userManager, transactionRepository);
        }

        // GET: Transaction
        public IActionResult Index()
        {
            //TODO: Fix this (less) hacky shit
            _cryptoCoinService.RefreshUserBalance(User);
            var user = _userManager.GetUserAsync(User).Result;
            string[] userData =
            {
                user.CryptoAddress,
                user.CryptoBalanceConfirmed.ToString(CultureInfo.InvariantCulture),
                user.CryptoBalancePending.ToString(CultureInfo.InvariantCulture)
            };
            var model = new Tuple<List<TransactionViewModel>, string[]>(
            _cryptoCoinService.GetUserTransactions(User).Select(ToModel).ToList(),
            userData);
            return View(model);
        }
        
        // GET: Transaction/Create
        public IActionResult Transfer()
        {
            return View();
        }

        // POST: Transaction/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to, for 
        // more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public IActionResult Transfer([Bind("CryptoAddressTo,Value")] TransactionViewModel transactionViewModel)
        {
            
            if (ModelState.IsValid)
            {
                _cryptoCoinService.NewTransfer(User, transactionViewModel.CryptoAddressTo, transactionViewModel.Value);
                return RedirectToAction(nameof(Index));
            }
            return View(transactionViewModel);
        }

        public void Details()
        {
            _cryptoCoinService.RefreshUserBalance(User);
        }

        private static TransactionViewModel ToModel(Transaction transaction)
        {
            return new TransactionViewModel
            {
                TransactionId = transaction.TransactionId,
                DateCreated = transaction.DateCreated,
                Txid = transaction.Txid,
                CryptoAddressFrom = transaction.CryptoAddressFrom,
                CryptoAddressTo = transaction.CryptoAddressTo,
                Value = transaction.Value
            };
        }

        private static Transaction ToEntity(TransactionViewModel transactionViewModel)
        {
            return new Transaction
            {
                TransactionId = transactionViewModel.TransactionId,
                DateCreated = transactionViewModel.DateCreated,
                Txid = transactionViewModel.Txid,
                CryptoAddressFrom = transactionViewModel.CryptoAddressFrom,
                CryptoAddressTo = transactionViewModel.CryptoAddressTo,
                Value = transactionViewModel.Value
            };
        }
    }
}
