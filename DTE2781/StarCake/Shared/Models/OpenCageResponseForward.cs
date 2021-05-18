using System.Collections.Generic;

namespace StarCake.Shared.Models
{
    public class OpenCageResponseForward
    {
        public int? StatusCode { get; set; }
        public string StatusMessage { get; set; }

        public decimal Latitude { get; set; }
        public decimal Longitude { get; set; }
    }
}
