using System.Collections.Generic;
using Microsoft.AspNetCore.Identity;

namespace uDev.Models.Entity
{
    public class ApplicationUser : IdentityUser
    {
       
        public virtual List<Specialties> Specialties {get; set;}
        //TODO: Implement this
        //public virtual List<Rating> Ratings {get; set;}
        public virtual List<Transaction> Transactions  {get; set;}
        public string CryptoAddress { get; set; }
        public string CryptoLabel { get; set; }
        public double CryptoBalanceConfirmed { get; set; }
        public double CryptoBalancePending { get; set; }
    }
}