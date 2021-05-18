using System.Net.Mime;
using uDev.Services;

namespace uDev.Models.Entity
{
    public class Claimer
    {
        public int ClaimerId { get; set; }
        public virtual Mission Mission { get; set; }
        public virtual ApplicationUser ApplicationUser { get; set; }
    }
}