using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Reflection.Emit;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Security.Claims;
using System.Threading.Tasks;
using BlockIoLib;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using uDev.Models.Entity;
using uDev.Repositories.Interface;

namespace uDev.Services
{
    public class CryptoCoinService
    {
        private readonly ITransactionRepository _repository;
        private readonly UserManager<ApplicationUser> _userManager;
        private readonly BlockIo _blockIo;

        public CryptoCoinService(UserManager<ApplicationUser> userManager, ITransactionRepository repository)
        {
            _userManager = userManager;
            _repository = repository;
            _blockIo = new BlockIo(SettingsService.GetAppSettings()["DGCAPIKey"], SettingsService.GetAppSettings()["SecretPin"]);
        }

        public IEnumerable<Transaction> GetUserTransactions(ClaimsPrincipal claimsPrincipal)
        {
            RefreshUserBalance(claimsPrincipal);
            var user = _userManager.GetUserAsync(claimsPrincipal).Result;
            return user.Transactions ?? new List<Transaction>();
        }

        public bool NewTransfer(ClaimsPrincipal claimsPrincipal, string addressTo, double value)
        {
            var user = _userManager.GetUserAsync(claimsPrincipal).Result;
            var response = _blockIo.WithdrawFromAddress(new{amount=value.ToString(CultureInfo.InvariantCulture), from_addresses=user.CryptoAddress, to_addresses=addressTo});
            if (!response.Status.Equals("success")) return false;
            var t = new Transaction
            {
                DateCreated = DateTime.Now,
                Txid = response.Data["txid"],
                CryptoAddressFrom = user.CryptoAddress,
                CryptoAddressTo = addressTo,
                Value = value
            };
            t = _repository.SaveTransaction(t).Result;
            user.Transactions.Add(t);
            var result = _userManager.UpdateAsync(user).Result;
            return result.Succeeded;
        }

        public string[] NewAddress()
        {
            var response = _blockIo.GetNewAddress();
            if (!response.Status.Equals("success")) return null;
            string[] items =
            {
                response.Data["address"],
                response.Data["label"]
            };
            return items;
        }

        public bool RefreshUserBalance(ClaimsPrincipal claimsPrincipal)
        {
            var user = _userManager.GetUserAsync(claimsPrincipal).Result;
            var response = _blockIo.GetAddressBalance(new {addresses = user.CryptoAddress});
            if (!response.Status.Equals("success")) return false;
            user.CryptoBalanceConfirmed = response.Data["available_balance"];
            user.CryptoBalancePending = response.Data["pending_received_balance"];
            var result = _userManager.UpdateAsync(user).Result;
            return result.Succeeded;
        }
    }
}