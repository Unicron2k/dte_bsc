using System.Collections.Generic;
using System.Security.Claims;
using System.Threading.Tasks;
using uDev.Models.Entity;

namespace uDev.Repositories.Interface
{
    
    //TODO: Possibly move to CryptoCoinService?
    public interface ITransactionRepository
    {
        public Task<List<Transaction>> GetAll();
        public Task<Transaction> SaveTransaction(Transaction transaction);
        public Task<Transaction> GetTransaction(int? id);
        public Task UpdateTransaction(Transaction transaction);
        public Task DeleteTransaction(Transaction transaction);
        public bool TransactionExists(int? id);
    }
}