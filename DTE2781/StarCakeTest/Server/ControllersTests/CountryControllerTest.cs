using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using StarCake.Server.Controllers;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;
#pragma warning disable 649

namespace StarCakeTest.Server.ControllersTests
{
    [TestClass]
    public class CountryControllerTest
    {
        private Mock<ICountryRepository> _mockRepositoryCountry;
        private List<Country> _countries;
        private CountryController _countryController;
        private readonly ApplicationDbContext _dbContext;

        [TestInitialize]
        public void SetupContext()
        {
            MockHelpers.MockUserManager<ApplicationUser>();
            _mockRepositoryCountry = new Mock<ICountryRepository>();
            _countryController = new CountryController(_mockRepositoryCountry.Object, _dbContext);

            _countries = new List<Country>
            {
                new Country {CountryId = 1, Name = "Sweden", CountryCode = "SWE", IsActive = true},
                new Country {CountryId = 2, Name = "Norway", CountryCode = "NO", IsActive = false}
            };
        }
        
        //GET Country
        [TestMethod]
        public async Task Get_ReturnCorrectCountry()
        {
            _mockRepositoryCountry.Setup(x => x.Get(1)).ReturnsAsync(_countries[0]);

            var result = await _countryController.Get(_countries[0].CountryId);
            
            Assert.AreEqual(_countries[0].Name, result.Value.Name);
        }
        
        //GET Country
        [TestMethod]
        public async Task Get_NullParameter_ReturnsNotFound()
        {
            _mockRepositoryCountry.Setup(x => x.Get(1)).ReturnsAsync(_countries[0]);
            
            var result = await _countryController.Get(null);
            
            Assert.IsInstanceOfType(result.Result, typeof(NotFoundObjectResult));
        }
        
        //GET Country
        [TestMethod]
        public async Task Get_ReturnsNotFoundResultIfModelNotFound()
        {
            _mockRepositoryCountry.Setup(x => x.Get(It.IsAny<int>())).ReturnsAsync(() => null);

            var result = await _countryController.Get(99);
            
            Assert.IsInstanceOfType(result.Result, typeof(NotFoundResult));
        }
        
        //GET Country
        [TestMethod]
        public async Task GetCountries_ReturnsAllCountries()
        {
            _mockRepositoryCountry.Setup(x => x.GetAll()).ReturnsAsync(_countries);

            var result = await _countryController.GetCountries();
            
            Assert.AreEqual(2, result.Count());
        }
        
        //POST Country
        [TestMethod]
        public async Task Post_ReturnsPostedCountryOnSuccess()
        {
            var country = new Country {Name = "Denmark", CountryCode = "DK", IsActive = true};
            _mockRepositoryCountry.Setup(x => x.GetAll()).ReturnsAsync(_countries);
            
            var result = await _countryController.Post(country);

            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(country, (result as CreatedAtActionResult)?.Value);
        }
        
        //POST Country
        [TestMethod]
        public async Task Post_ReturnsBadRequestOnInvalidModelState()
        {
            var country = new Country {Name = "Denmark", CountryCode = "DK", IsActive = true};
            _mockRepositoryCountry.Setup(x => x.GetAll()).ReturnsAsync(_countries);
            
            _countryController.ModelState.AddModelError("test", "test");
            var result = await _countryController.Post(country);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }
        
        //PUT Country
        [TestMethod]
        public async Task Put_ReturnsCountryOnSuccess()
        {
            _mockRepositoryCountry.Setup(x => x.GetAll()).ReturnsAsync(_countries);

            var firstCountry = _countries[0];
            var viewModel = new CountryViewModel 
                {Name = firstCountry.Name, CountryCode = firstCountry.CountryCode, IsActive = firstCountry.IsActive};
            viewModel.Name = "Kyllingsalat";
            var result = await _countryController.Put(viewModel.CountryId, viewModel);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(viewModel, (result as CreatedAtActionResult)?.Value);
        }
        
        //PUT Country
        [TestMethod]
        public async Task Put_ReturnsBadRequestOnInvalidModelState()
        {
            _mockRepositoryCountry.Setup(x => x.GetAll()).ReturnsAsync(_countries);

            var firstCountry = _countries[0];
            var viewModel = new CountryViewModel {Name = firstCountry.Name, CountryCode = firstCountry.CountryCode, IsActive = firstCountry.IsActive};
            viewModel.Name = "Kyllingsalat";
            _countryController.ModelState.AddModelError("test", "test");
            var result = await _countryController.Put(viewModel.CountryId, viewModel);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }
        
        //PUT Country
        [TestMethod]
        public async Task Put_ReturnsBadRequestIfSuppliedIdNotLikeViewModel()
        {
            _mockRepositoryCountry.Setup(x => x.GetAll()).ReturnsAsync(_countries);

            var firstCountry = _countries[0];
            var viewModel = new CountryViewModel {Name = firstCountry.Name, CountryCode = firstCountry.CountryCode, IsActive = firstCountry.IsActive};
            viewModel.Name = "Kyllingsalat";
            _countryController.ModelState.AddModelError("test", "test");
            var result = await _countryController.Put(99999, viewModel);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }
    }
}