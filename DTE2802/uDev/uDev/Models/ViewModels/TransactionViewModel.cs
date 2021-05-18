using System;
using System.ComponentModel.DataAnnotations;

namespace uDev.Models.ViewModels
{
    public class TransactionViewModel
    {
        [Key]
        public int TransactionId { get; set; }
        public DateTime DateCreated  { get; set; }
        public string Txid { get; set; }
        public string CryptoAddressFrom { get; set; }
        [Required]
        public string CryptoAddressTo { get; set; }
        [Required]
        public double Value { get; set; }
    }
}