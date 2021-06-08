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
    public class TypeOfOperationControllerTest
    {
        private Mock<ITypeOfOperationRepository> _mockRepositoryTypeOfOperation;
        private List<TypeOfOperation> _typeOfOperations;
        private TypeOfOperationController _typeOfOperationController;
        private readonly ApplicationDbContext _dbContext;

        [TestInitialize]
        public void SetupContext()
        {
            MockHelpers.MockUserManager<ApplicationUser>();
            _mockRepositoryTypeOfOperation = new Mock<ITypeOfOperationRepository>();
            _typeOfOperationController = new TypeOfOperationController(_mockRepositoryTypeOfOperation.Object, _dbContext);

            _typeOfOperations = new List<TypeOfOperation>
            {
                new TypeOfOperation {TypeOfOperationId = 1, Name = "Radar Flight", IsActive = true},
                new TypeOfOperation {TypeOfOperationId = 2, Name = "Assertion Flight", IsActive = false}
            };
        }
        
        //GET TypeOfOperation
        [TestMethod]
        public async Task Get_ReturnCorrectTypeOfOperation()
        {
            _mockRepositoryTypeOfOperation.Setup(x => x.Get(1)).ReturnsAsync(_typeOfOperations[0]);

            var result = await _typeOfOperationController.Get(_typeOfOperations[0].TypeOfOperationId);
            
            Assert.AreEqual(_typeOfOperations[0].Name, result.Value.Name);
        }
        
        //GET TypeOfOperation
        [TestMethod]
        public async Task Get_NullParameter_ReturnsNotFound()
        {
            _mockRepositoryTypeOfOperation.Setup(x => x.Get(1)).ReturnsAsync(_typeOfOperations[0]);
            
            var result = await _typeOfOperationController.Get(null);
            
            Assert.IsInstanceOfType(result.Result, typeof(NotFoundObjectResult));
        }
        
        //GET TypeOfOperation
        [TestMethod]
        public async Task Get_ReturnsNotFoundResultIfModelNotFound()
        {
            _mockRepositoryTypeOfOperation.Setup(x => x.Get(It.IsAny<int>())).ReturnsAsync(() => null);

            var result = await _typeOfOperationController.Get(99);
            
            Assert.IsInstanceOfType(result.Result, typeof(NotFoundResult));
        }
        
        //GET TypeOfOperation
        [TestMethod]
        public async Task GettypeOfOperations_ReturnsAlltypeOfOperations()
        {
            _mockRepositoryTypeOfOperation.Setup(x => x.GetAll()).ReturnsAsync(_typeOfOperations);

            var result = await _typeOfOperationController.GetTypeOfOperations();
            
            Assert.AreEqual(2, result.Count());
        }
        
        //POST TypeOfOperation
        [TestMethod]
        public async Task Post_ReturnsPostedTypeOfOperationOnSuccess()
        {
            var typeOfOperation = new TypeOfOperation {Name = "Cool Flight", IsActive = true};
            _mockRepositoryTypeOfOperation.Setup(x => x.GetAll()).ReturnsAsync(_typeOfOperations);
            
            var result = await _typeOfOperationController.Post(typeOfOperation);

            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(typeOfOperation, (result as CreatedAtActionResult)?.Value);
        }
        
        //POST TypeOfOperation
        [TestMethod]
        public async Task Post_ReturnsBadRequestOnInvalidModelState()
        {
            var typeOfOperation = new TypeOfOperation {Name = "Bas Flight", IsActive = true};
            _mockRepositoryTypeOfOperation.Setup(x => x.GetAll()).ReturnsAsync(_typeOfOperations);
            
            _typeOfOperationController.ModelState.AddModelError("test", "test");
            var result = await _typeOfOperationController.Post(typeOfOperation);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }
        
        //PUT TypeOfOperation
        [TestMethod]
        public async Task Put_ReturnsTypeOfOperationOnSuccess()
        {
            _mockRepositoryTypeOfOperation.Setup(x => x.GetAll()).ReturnsAsync(_typeOfOperations);

            var firstTypeOfOperation = _typeOfOperations[0];
            var viewModel = new TypeOfOperationViewModel 
                {Name = firstTypeOfOperation.Name, IsActive = firstTypeOfOperation.IsActive};
            viewModel.Name = "Foo Flight";
            var result = await _typeOfOperationController.Put(viewModel.TypeOfOperationId, viewModel);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(viewModel, (result as CreatedAtActionResult)?.Value);
        }
        
        //PUT TypeOfOperation
        [TestMethod]
        public async Task Put_ReturnsBadRequestOnInvalidModelState()
        {
            _mockRepositoryTypeOfOperation.Setup(x => x.GetAll()).ReturnsAsync(_typeOfOperations);

            var firstTypeOfOperation = _typeOfOperations[0];
            var viewModel = new TypeOfOperationViewModel {Name = firstTypeOfOperation.Name, IsActive = firstTypeOfOperation.IsActive};
            viewModel.Name = "Kyllingsalat";
            _typeOfOperationController.ModelState.AddModelError("test", "test");
            var result = await _typeOfOperationController.Put(viewModel.TypeOfOperationId, viewModel);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }
        
        //PUT TypeOfOperation
        [TestMethod]
        public async Task Put_ReturnsBadRequestIfSuppliedIdNotLikeViewModel()
        {
            _mockRepositoryTypeOfOperation.Setup(x => x.GetAll()).ReturnsAsync(_typeOfOperations);

            var firstTypeOfOperation = _typeOfOperations[0];
            var viewModel = new TypeOfOperationViewModel {Name = firstTypeOfOperation.Name, IsActive = firstTypeOfOperation.IsActive};
            viewModel.Name = "Foo Flight";
            _typeOfOperationController.ModelState.AddModelError("test", "test");
            var result = await _typeOfOperationController.Put(99999, viewModel);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }
    }
}