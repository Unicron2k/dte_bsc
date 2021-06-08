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
    public class ManufacturerControllerTest
    {
        private Mock<IManufacturRepository> _mockRepositoryManufacturer;
        private List<Manufacturer> _manufacturers;
        private ManufacturerController _manufacturerController;
        private readonly ApplicationDbContext _dbContext;

        [TestInitialize]
        public void SetupContext()
        {
            MockHelpers.MockUserManager<ApplicationUser>();
            _mockRepositoryManufacturer = new Mock<IManufacturRepository>();
            _manufacturerController = new ManufacturerController(_dbContext, _mockRepositoryManufacturer.Object);

            _manufacturers = new List<Manufacturer>
            {
                new Manufacturer {ManufacturerId = 1, Name = "DJI", IsActive = true},
                new Manufacturer {ManufacturerId = 2, Name = "GoPro", IsActive = false}
            };
        }
        
        //GET Manufacturer
        [TestMethod]
        public async Task Get_ReturnCorrectManufacturer()
        {
            _mockRepositoryManufacturer.Setup(x => x.GetManufacturer(1)).ReturnsAsync(_manufacturers[0]);

            var result = await _manufacturerController.GetManufacturer(_manufacturers[0].ManufacturerId);
            
            Assert.AreEqual(_manufacturers[0].Name, result.Value.Name);
        }
        
        //GET Manufacturer
        [TestMethod]
        public async Task Get_NullParameter_ReturnsNotFound()
        {
            _mockRepositoryManufacturer.Setup(x => x.GetManufacturer(1)).ReturnsAsync(_manufacturers[0]);
            
            var result = await _manufacturerController.GetManufacturer(null);
            
            Assert.IsInstanceOfType(result.Result, typeof(NotFoundResult));
        }
        
        //GET Manufacturer
        [TestMethod]
        public async Task Get_ReturnsNotFoundResultIfModelNotFound()
        {
            _mockRepositoryManufacturer.Setup(x => x.GetManufacturer(It.IsAny<int>())).ReturnsAsync(() => null);

            var result = await _manufacturerController.GetManufacturer(99);
            
            Assert.IsInstanceOfType(result.Result, typeof(NotFoundResult));
        }
        
        //GET Manufacturer
        [TestMethod]
        public async Task GetCountries_ReturnsAllCountries()
        {
            _mockRepositoryManufacturer.Setup(x => x.GetAll()).ReturnsAsync(_manufacturers);

            var result = await _manufacturerController.GetManufacturers();
            
            Assert.AreEqual(2, result.Count());
        }
        
        //POST Manufacturer
        [TestMethod]
        public async Task Post_ReturnsPostedManufacturerOnSuccess()
        {
            var manufacturer = new Manufacturer {Name = "3DS", IsActive = true};
            _mockRepositoryManufacturer.Setup(x => x.GetAll()).ReturnsAsync(_manufacturers);
            
            var result = await _manufacturerController.PostManufacturer(manufacturer);

            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(manufacturer, (result as CreatedAtActionResult)?.Value);
        }
        
        //POST Manufacturer
        [TestMethod]
        public async Task Post_ReturnsBadRequestOnInvalidModelState()
        {
            var manufacturer = new Manufacturer {Name = "3DS", IsActive = true};
            _mockRepositoryManufacturer.Setup(x => x.GetAll()).ReturnsAsync(_manufacturers);
            
            _manufacturerController.ModelState.AddModelError("test", "test");
            var result = await _manufacturerController.PostManufacturer(manufacturer);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }
        
        //PUT Manufacturer
        [TestMethod]
        public async Task Put_ReturnsManufacturerOnSuccess()
        {
            _mockRepositoryManufacturer.Setup(x => x.GetAll()).ReturnsAsync(_manufacturers);

            var firstManufacturer = _manufacturers[0];
            var viewModel = new ManufacturerViewModel {Name = firstManufacturer.Name, IsActive = firstManufacturer.IsActive};
            viewModel.Name = "Kyllingsalat";
            var result = await _manufacturerController.Put(viewModel.ManufacturerId, viewModel);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(viewModel, (result as CreatedAtActionResult)?.Value);
        }
        
        //PUT Manufacturer
        [TestMethod]
        public async Task Put_ReturnsBadRequestOnInvalidModelState()
        {
            _mockRepositoryManufacturer.Setup(x => x.GetAll()).ReturnsAsync(_manufacturers);

            var firstManufacturer = _manufacturers[0];
            var viewModel = new ManufacturerViewModel {Name = firstManufacturer.Name, IsActive = firstManufacturer.IsActive};
            viewModel.Name = "Kyllingsalat";
            _manufacturerController.ModelState.AddModelError("test", "test");
            var result = await _manufacturerController.Put(viewModel.ManufacturerId, viewModel);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }
        
        //PUT Manufacturer
        [TestMethod]
        public async Task Put_ReturnsBadRequestIfSuppliedIdNotLikeViewModel()
        {
            _mockRepositoryManufacturer.Setup(x => x.GetAll()).ReturnsAsync(_manufacturers);

            var firstManufacturer = _manufacturers[0];
            var viewModel = new ManufacturerViewModel {Name = firstManufacturer.Name, IsActive = firstManufacturer.IsActive};
            viewModel.Name = "Kyllingsalat";
            _manufacturerController.ModelState.AddModelError("test", "test");
            var result = await _manufacturerController.Put(99999, viewModel);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }
    }
}