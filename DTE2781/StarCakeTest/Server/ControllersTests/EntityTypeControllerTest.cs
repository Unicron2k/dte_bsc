using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Identity;
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
    public class EntityTypeControllerTest
    {
        private Mock<IEntityTypeRepository> _mockRepositoryEntityType;
        private Mock<UserManager<ApplicationUser>> _mockUserManager;
        private List<EntityType> _entityType;
        private EntityTypeController _controller;
        private ApplicationDbContext _dbContext;


        [TestInitialize]
        public void SetupContext()
        {
            _mockUserManager = MockHelpers.MockUserManager<ApplicationUser>();
            _mockRepositoryEntityType = new Mock<IEntityTypeRepository>();
            _controller = new EntityTypeController(_dbContext, _mockRepositoryEntityType.Object);

            _entityType = new List<EntityType>
            {
                new EntityType
                {
                    EntityTypeId = 1,
                    Name = "Gimbal",
                    IsActive = true
                },
                new EntityType
                {
                    EntityTypeId = 2,
                    Name = "Propeller",
                    IsActive = false
                }
            };
        }

        //GET ComponentType
        [TestMethod]
        public async Task GetEntityType_GET_ReturnCorrectComponent()
        {
            _mockRepositoryEntityType.Setup(x => x.GetEntityType(1)).ReturnsAsync(_entityType[0]);

            var result = await _controller.GetEntityType(1);
            
            Assert.AreEqual("Gimbal", result.Value.Name);
        }

        //GET ComponentType
        [TestMethod]
        public async Task GetEntityType_NullParameter_ReturnNotfound()
        {
            var result = await _controller.GetEntityType(null);
            Assert.IsInstanceOfType(result.Result, typeof(NotFoundResult));
        }

        //GET ComponentType
        [TestMethod]
        public async Task GetAllEntityType_AllComponentsType_ReturnList()
        {
            _mockRepositoryEntityType.Setup(x => x.GetAll()).ReturnsAsync(_entityType);

            var result = await _controller.GetEntityTypes();
            Assert.IsNotNull(result, "View Result is null");
        }

        //GET ComponentType
        [TestMethod]
        public async Task GetEntityType_GETNullFromRepo_ReturnNotFound()
        {
            _mockRepositoryEntityType.Setup(x => x.GetEntityType(It.IsAny<int>())).ReturnsAsync(() => null);

            var result = await _controller.GetEntityType(5);

            Assert.IsInstanceOfType(result.Result, typeof(NotFoundResult));
        }

        //POST ComponentType
        [TestMethod]
        public async Task PostEntityType_PostModelIsNotValid_ReturnBadRequest()
        {
            var componentToPost = new EntityType {Name = "Testing ComponentTyoe", IsActive = true};

            _controller.ModelState.AddModelError("test", "test");
            var result = await _controller.PostEntityType(componentToPost);

            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }

        //POST
        [TestMethod]
        public async Task PostEntityType_ReturnCreateAtAction()
        {
            var componentToPost = new EntityType {Name = "Testing ComponentTyoe", IsActive = true};

            var result = await _controller.PostEntityType(componentToPost);

            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(componentToPost, (result as CreatedAtActionResult)?.Value);
        }

        //PUT
        [TestMethod]
        public async Task Put_ModelIsNotValid_returnBadRequest()
        {
            var viewModelToPut = new EntityTypeViewModel
            {
                Name = "Ikke Gimbal",
                IsActive = false
            };
            _controller.ModelState.AddModelError("Put fail", "Put fail");
            var result = await _controller.Put(viewModelToPut.EntityTypeId, viewModelToPut);

            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }

        //PUT
        [TestMethod]
        public async Task Put_PutEntityType_ReturnCreatedAtActionResult()
        {
            _mockRepositoryEntityType.Setup(x => x.GetEntityType(It.Is<int>(i => i == 1)))
                .ReturnsAsync(_entityType.First(x => x.EntityTypeId == 1));

            var viewModelToPut = new EntityTypeViewModel
            {
                Name = "Ikke Gimbal",
                IsActive = false
            };
            var result = await _controller.Put(viewModelToPut.EntityTypeId, viewModelToPut);

            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(viewModelToPut, (result as CreatedAtActionResult)?.Value);
        }

        //PUT
        [TestMethod]
        public async Task Put_IdNotMatch_ReturnBadRequest()
        {
            var firstEntityType = _entityType[0];
            var viewModel = new EntityTypeViewModel
                {EntityTypeId = 15, Name = firstEntityType.Name, IsActive = firstEntityType.IsActive};
            viewModel.Name = "Kyllingsalat";

            var result = await _controller.Put(_entityType[0].EntityTypeId, viewModel);

            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestResult));

        }


    }
}