using System;

namespace uDev.Models.Entity
{
    public class Transaction
    {
        public int TransactionId { get; set; }
        public DateTime DateCreated  { get; set; }
        public string Txid { get; set; }
        public string CryptoAddressFrom { get; set; }
        public string CryptoAddressTo { get; set; }
        public double Value { get; set; }
    }
}