using System.Collections.Generic;

namespace StarCake.Shared.Models
{
    public class OpenCageResponseReverse
    {
        public int? StatusCode { get; set; }
        public string StatusMessage { get; set; }
        
        public string Country { get; set; }
        public string CountryCode { get; set; }
        public string Continent {get;set;}
        
        public string Municipality {get;set;}
        public string County { get; set; }
        public string City {get;set;}
        
        public string PostalCity {get;set;}
        public string ZipCode {get;set;}
        public string Road {get;set;}
        public string HouseNumber {get;set;}

        /// <summary>
        /// Parses dictionary returned from https://opencagedata.com/ API into a custom object used for FlightLog.cs
        /// </summary>
        /// <param name="dictionary">Dictionary from result</param>
        public void TryParseDictionaryResponse(Dictionary<string, string> dictionary)
        {
            if (dictionary.TryGetValue("country", out var countryCache)) Country = countryCache;
            if (dictionary.TryGetValue("country_code", out var countryCodeCache)) CountryCode = countryCodeCache;
            if (dictionary.TryGetValue("continent", out var continentCache)) Continent = continentCache;
        
            if (dictionary.TryGetValue("municipality", out var municipalityCache)) Municipality = municipalityCache;
            if (dictionary.TryGetValue("county", out var countyCache)) County = countyCache;
            if (dictionary.TryGetValue("town", out var cityCache)) City = cityCache;
            
            if (dictionary.TryGetValue("postal_city", out var postalCityCache)) PostalCity = postalCityCache;
            if (dictionary.TryGetValue("postcode", out var zipCodeCache)) ZipCode = zipCodeCache;
            if (dictionary.TryGetValue("road", out var roadCache)) Road = roadCache;
            if (dictionary.TryGetValue("house_number", out var houseNumberCache)) HouseNumber = houseNumberCache;
        }
    }
}
