using System.Collections;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Net;
using System.Security.Claims;
using System.Threading.Tasks;
using Assignment2Blog.Controllers;
using Assignment2Blog.Models.Entities;
using Assignment2Blog.Models.Interfaces;
using Assignment2Blog.Models.ViewModels;
using Microsoft.AspNetCore.DataProtection;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.ViewFeatures;
using Microsoft.Extensions.Logging;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;

namespace BlogTest
{
    [TestClass]
    public class UnitTest1
    {
        
        private Mock<IBlogRepository> _mockRepository;
        private Mock<UserManager<IdentityUser>> _mockUserManager;
        private Mock<ILogger<HomeController>> _mockLogger;
        private List<Blog> _blogs;
        private HomeController _controller;

        [TestInitialize]
        public void SetupContext() {
            _mockUserManager = MockHelpers.MockUserManager<IdentityUser>();
            _mockRepository = new Mock<IBlogRepository>();
            _mockLogger = new Mock<ILogger<HomeController>>();
            _controller = new HomeController(_mockLogger.Object, _mockUserManager.Object, _mockRepository.Object);
            _blogs = new List<Blog>
            {
                new Blog {BlogId = 1, Name = "test1", Description = "test1", Owner = new IdentityUser(), CreationDate = "2020-10-04", BlogLocked = false},
                new Blog {BlogId = 2, Name = "test2", Description = "test2", Owner = new IdentityUser(), CreationDate = "2020-10-04", BlogLocked = false},
                new Blog {BlogId = 3, Name = "test3", Description = "test3", Owner = new IdentityUser(), CreationDate = "2020-10-04", BlogLocked = false},
            };
        }
        
        
        [TestMethod]
        public void IndexReturnsNotNullResult(){ 
            //var controller = new HomeController(_mockLogger.Object, _mockUserManager.Object, _mockRepository.Object);
            var result = (ViewResult) _controller.Index().Result;
            Assert.IsNotNull(result, "View Result is null");
        }

        [TestMethod]
        public void IndexReturnsAllProducts()
        {	
            // Arrange
            _mockRepository.Setup(x => x.GetAll()).Returns(Task.FromResult(_blogs));
            //var controller = new HomeController(_mockLogger.Object, _mockUserManager.Object, _mockRepository.Object);

            // Act
            var result = _controller.Index().Result as ViewResult;

            // Assert
            CollectionAssert.AllItemsAreInstancesOfType((ICollection)result.ViewData.Model, typeof(Blog));
            Assert.IsNotNull(result, "View Result is null");

            var blogs = result.ViewData.Model as List<Blog>;
            Assert.AreEqual(_blogs.Count, blogs.Count, "Got wrong number of products");
        }

        [TestMethod]
        public async Task SaveIsCalledWhenBlogIsCreated()
        {
            var model = new BlogViewModel();
            
            _mockRepository.Setup(x => x.SaveBlog(It.IsAny<BlogViewModel>(), It.IsAny<ClaimsPrincipal>()));
            _controller.TempData = new Mock<ITempDataDictionary>().Object;
            
            var result = await _controller.Create(model);
            _mockRepository.VerifyAll();
            _mockRepository.Verify(x => x.SaveBlog(It.IsAny<BlogViewModel>(), It.IsAny<ClaimsPrincipal>()), Times.Exactly(1));
        }
        
        [TestMethod]
        public async Task SaveIsNotCalledWhenBlogIsCreatedAndModelStateIsInvalid()
        {
            var model = new BlogViewModel();
            
            _controller.TempData = new Mock<ITempDataDictionary>().Object;
            _controller.ModelState.AddModelError("key", "error message");
            
            var result = await _controller.Create(model);
            Assert.IsNotNull(result, "View Result is null");
        }

        [TestMethod]
        public void CreateReturnsNotNullResult() {
            // Arrange
            //var controller = new HomeController(_mockLogger.Object, _mockUserManager.Object, _mockRepository.Object);

            // Act
            var result = (ViewResult)_controller.Create();

            // Assert
            Assert.IsNotNull(result, "View Result is null");
        }
        
        
        
        [TestMethod]
        public async Task CreateViewIsReturnedWhenInputIsNotValid() {
            // Arrange
            var viewModel = new BlogViewModel
            {
                Name = "",
                Description = "",
                CreationDate = "",
                BlogLocked = false
            };
            _controller.TempData = new Mock<ITempDataDictionary>().Object;
            _controller.ModelState.AddModelError("key", "error message");

            // Act
            var validationContext = new ValidationContext(viewModel, null, null);
            var validationResults = new List<ValidationResult>();
            Validator.TryValidateObject(viewModel, validationContext, validationResults, true);
            foreach (var validationResult in validationResults)
                _controller.ModelState.AddModelError(validationResult.MemberNames.First(),
                    validationResult.ErrorMessage);

            var result = await _controller.Create(viewModel) as ViewResult;

            // Assert
            Assert.IsNotNull(result);
            Assert.IsTrue(validationResults.Count > 0);
        }

        [TestMethod]
        public async Task CreateRedirectActionRedirectsToIndexAction() {
            // Arrange
            var controller = new HomeController(_mockLogger.Object, _mockUserManager.Object, _mockRepository.Object) {
                ControllerContext = MockHelpers.FakeControllerContext(false)
            };
            var tempData = new TempDataDictionary(controller.ControllerContext.HttpContext, Mock.Of<ITempDataProvider>());
            controller.TempData = tempData;
            var viewModel = new BlogViewModel {
                Name = "",
                Description = "",
                CreationDate = "",
                BlogLocked = false
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
            var controller = new HomeController(_mockLogger.Object, _mockUserManager.Object, _mockRepository.Object) {
                ControllerContext = MockHelpers.FakeControllerContext(false)
            };

            // Act
            var result = controller.Create() as ViewResult;

            // Assert
            Assert.IsNotNull(result);
            Assert.IsNull(result.ViewName);

        }
        
        [TestMethod]
        public void DetailsShouldReturnNotFoundWhenIdInvalid()
        {
            // Arrange
            
            // Act
            var result = _controller.Details(null).Result;
            var result2 = _controller.Details(5).Result;

            // Assert
            Assert.IsInstanceOfType(result, typeof(NotFoundResult));
            Assert.IsInstanceOfType(result2, typeof(NotFoundResult));
        }
        
        [TestMethod]
        public void DetailsShouldReturnViewWhenValidId()
        {
            // Arrange
            _mockRepository.Setup(x => x.GetBlog(1)).Returns(Task.FromResult(_blogs[0]));
            
            // Act
            var result = _controller.Details(1).Result;

            // Assert
            Assert.IsInstanceOfType(result, typeof(ViewResult));
        }
        
    }
}