using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using uDev.Data;
using uDev.Models.Entity;
using uDev.Repositories.Interface;

namespace uDev.Repositories
{
    public class TransactionRepository : ITransactionRepository
    {
        private readonly ApplicationDbContext _db;

        public TransactionRepository(ApplicationDbContext db)
        {
            _db = db;
        }
        
        public async Task<List<Transaction>> GetAll()
        {
            return await _db.Transactions.ToListAsync();
        }

        public async Task<Transaction> SaveTransaction(Transaction transaction)
        {
            await _db.Transactions.AddAsync(transaction);
            await _db.SaveChangesAsync();
            return transaction;
        }

        public async Task<Transaction> GetTransaction(int? id)
        {
            if (id == null) return new Transaction();
            return await _db.Transactions.FirstOrDefaultAsync(s => s.TransactionId == id);
        }

        public async Task UpdateTransaction(Transaction transaction)
        {
            _db.Update(transaction);
            await _db.SaveChangesAsync();
        }

        public async Task DeleteTransaction(Transaction transaction)
        {
            _db.Transactions.Remove(transaction);
            await _db.SaveChangesAsync();
        }

        public bool TransactionExists(int? id)
        {
            return _db.Transactions.Any(s => s.TransactionId == id);
        }
    }
}