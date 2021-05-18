using System.Collections;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Assignment1.Controllers;
using Assignment1.Models;
using Assignment1.Models.Entities;
using Assignment1.Models.ViewModels;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.ViewFeatures;
using Moq;

//Fra løsningsforslag
namespace ProductControllerTest
{
    [TestClass]
    public class ProductControllerTest
    {
        private Mock<IProductRepository> _mockRepository;
        private List<Product> _products;
        private List<Category> _categories;
        private List<Manufacturer> _manufacturers;
        private Mock<UserManager<IdentityUser>> _mockUserManager;
        
        
        [TestInitialize]
        public void SetupContext() {
            _mockUserManager = MockHelpers.MockUserManager<IdentityUser>();
            _mockRepository = new Mock<IProductRepository>();
            _categories = new List<Category>
                {new Category {CategoryId = 1, Name = "Tools"}, new Category {CategoryId = 2, Name = "Consumables"}};
            _manufacturers = new List<Manufacturer> { new Manufacturer { ManufacturerId = 1, Name = "Maker Inc." } };
            _products = new List<Product>
            {
                new Product{Name="Volvo XC60", Price=620000.00m, CategoryId = 1,ManufacturerId = 1, Description = "Kinesium"},
                new Product{Name="Volvo XC90", Price=990000.00m, CategoryId = 1,ManufacturerId = 1, Description = "MYE kinesium"},
                new Product{Name="Vinkelsliper", Price=1520.00m, CategoryId = 1,ManufacturerId = 1, Description = "Alt er en BFH!"},
                new Product{Name="Hammer", Price=121.50m, CategoryId = 1,ManufacturerId = 1, Description = "Alt er en BFH!"},
                new Product{Name="Brød", Price=25.50m, CategoryId = 2,ManufacturerId = 1, Description = "Spiselig, tror vi..."},
                new Product{Name="Pannekake", Price=99.00m, CategoryId = 2,ManufacturerId = 1, Description = "Runde"},
                new Product{Name="Pannekake", Price=99.99m, CategoryId = 2,ManufacturerId = 1, Description = "Runde"}
            };
        }
        
        [TestMethod]
        public void IndexReturnsNotNullResult(){ 
            var controller = new ProductController(_mockUserManager.Object, _mockRepository.Object);
            var result = (ViewResult) controller.Index();
            Assert.IsNotNull(result, "View Result is null");
        }

        [TestMethod]
        public void IndexReturnsAllProducts()
        {	
            // Arrange
            _mockRepository.Setup(x => x.GetAll()).Returns(_products);
            var controller = new ProductController(_mockUserManager.Object, _mockRepository.Object);

            // Act
            var result = controller.Index() as ViewResult;

            // Assert
            CollectionAssert.AllItemsAreInstancesOfType((ICollection)result.ViewData.Model, typeof(Product));
            Assert.IsNotNull(result, "View Result is null");

            var products = result.ViewData.Model as List<Product>;
            Assert.AreEqual(_products.Count, products.Count, "Got wrong number of products");
        }
        
        
        [TestMethod]
        public void CreateReturnsNotNullResult() {
            // Arrange
            var controller = new ProductController(_mockUserManager.Object, _mockRepository.Object);

            // Act
            var result = (ViewResult)controller.Create();

            // Assert
            Assert.IsNotNull(result, "View Result is null");
        }
        
        
        [TestMethod]
        public async Task SaveIsCalledWhenProductIsCreated()
        {
            _mockRepository.Setup(x => x.Save(It.IsAny<ProductEditViewModel>(), It.IsAny<ClaimsPrincipal>()));
            var controller = new ProductController(_mockUserManager.Object, _mockRepository.Object);

            var result = await controller.Create(new ProductEditViewModel());
            _mockRepository.VerifyAll();
            _mockRepository.Verify(x => x.Save(It.IsAny<ProductEditViewModel>(), It.IsAny<ClaimsPrincipal>()), Times.Exactly(1));
        }
        
        
        [TestMethod]
        public async Task CreateViewIsReturnedWhenInputIsNotValid() {
            // Arrange
            var viewModel = new ProductEditViewModel() {
                Name = "",
                Description = "",
                Price = 0
            };
            var controller = new ProductController(_mockUserManager.Object, _mockRepository.Object);

            // Act
            var validationContext = new ValidationContext(viewModel, null, null);
            var validationResults = new List<ValidationResult>();
            Validator.TryValidateObject(viewModel, validationContext, validationResults, true);
            foreach (var validationResult in validationResults)
                controller.ModelState.AddModelError(validationResult.MemberNames.First(),
                    validationResult.ErrorMessage);

            var result = await controller.Create(viewModel) as ViewResult;

            // Assert
            Assert.IsNotNull(result);
            Assert.IsTrue(validationResults.Count > 0);
        }

        [TestMethod]
        public async Task CreateRedirectActionRedirectsToIndexAction() {
            // Arrange
            var controller = new ProductController(_mockUserManager.Object, _mockRepository.Object) {
                ControllerContext = MockHelpers.FakeControllerContext(false)
            };
            var tempData = 
                new TempDataDictionary(controller.ControllerContext.HttpContext, Mock.Of<ITempDataProvider>());
            controller.TempData = tempData;
            var viewModel = new ProductEditViewModel {
                Price = 100,
                Description = "Description of product"
            };

            // Act
            var result = await controller.Create(viewModel) as RedirectToActionResult;

            // Assert
            Assert.IsNotNull(result, "RedirectToIndex needs to redirect to the Index action");
            Assert.AreEqual("Index", result.ActionName);
        }
        
        [TestMethod]
        public void CreateShouldShowLoginViewFor_Non_AuthorizedUser()
        {
            // Arrange
            var controller = new ProductController(_mockUserManager.Object, _mockRepository.Object);
            controller.ControllerContext = MockHelpers.FakeControllerContext(false);

            // Act
            var result = controller.Create() as ViewResult;

            // Assert
            Assert.IsNotNull(result);
            Assert.IsNull(result.ViewName);

        }
    }
}