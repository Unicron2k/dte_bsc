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
    public class ComponenTypeControllerTest
    {
        private Mock<IComponentTypeRepository> _mockRepositoryComponent;
        private Mock<UserManager<ApplicationUser>> _mockUserManager;
        private List<ComponentType> _componentType;
        private ComponentTypeController _controller;
        private ApplicationDbContext _dbContext;
       

        [TestInitialize]
        public void SetupContext()
        {
            _mockUserManager = MockHelpers.MockUserManager<ApplicationUser>();
            _mockRepositoryComponent = new Mock<IComponentTypeRepository>();
            _controller = new ComponentTypeController(_dbContext, _mockRepositoryComponent.Object);

            _componentType = new List<ComponentType>
            {
                new ComponentType
                {
                    ComponentTypeId = 1,
                    Name = "Gimbal",
                    IsActive = true
                },
                new ComponentType
                {
                    ComponentTypeId = 2,
                    Name = "Propeller",
                    IsActive = false
                }
            };
        }
        
        //GET ComponentType
        [TestMethod]
        public async Task GetComponentType_GET_ReturnCorrectComponent()
        {
            _mockRepositoryComponent.Setup(x => x.GetComponentType(1)).ReturnsAsync(_componentType[0]);

            var result =  await _controller.GetEntityType(1);
            Assert.AreEqual(result.Value.Name, "Gimbal");
            
        }
        
        //GET ComponentType
        [TestMethod]
        public async Task GetComponentType_NullParameter_ReturnNotfound()
        {
            var result = await  _controller.GetEntityType(null);
            Assert.IsInstanceOfType(result.Result, typeof(NotFoundObjectResult));
        }
        
        //GET ComponentType
        [TestMethod]
        public async Task GetAllComponentType_AllComponentsType_ReturnList()
        {
            _mockRepositoryComponent.Setup(x => x.GetAll()).ReturnsAsync(_componentType);

            var result = await _controller.GetComponentTypes();
            Assert.IsNotNull(result, "View Result is null");
        }
        //GET ComponentType
        [TestMethod]
        public async Task GetComponenType_GETNullFromRepo_ReturnNotFound()
        {
            _mockRepositoryComponent.Setup(x => x.GetComponentType(It.IsAny<int>())).ReturnsAsync(() => null);

            var result = await _controller.GetEntityType(5);
            
            Assert.IsInstanceOfType(result.Result, typeof(NotFoundResult));
        }
        
        //POST ComponentType
        [TestMethod]
        public async Task PostComponentType_PostModelIsNotValid_ReturnBadRequest()
        {
            var componentToPost = new ComponentType {Name = "Testing ComponentTyoe", IsActive = true}; 

            _controller.ModelState.AddModelError("test", "test");
            var result = await _controller.PostComponentType(componentToPost);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }
        
        //POST
        [TestMethod]
        public async Task PostComponentType_ReturnCreateAtAction()
        {
            var componentToPost = new ComponentType {Name = "Testing ComponentTyoe", IsActive = true};

            var result = await _controller.PostComponentType(componentToPost);
            
            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(componentToPost, (result as CreatedAtActionResult)?.Value);
        }
        
        //PUT
        [TestMethod]
        public async Task Put_ModelIsNotValid_returnBadRequest()
        {
            var viewModelToPut = new ComponentTypeViewModel                                     
            {                                                                                   
                Name = "Ikke Gimbal",                                                           
                IsActive = false                                                                
            };   
            _controller.ModelState.AddModelError("Put fail", "Put fail");
            var result = await  _controller.Put(viewModelToPut.ComponentTypeId, viewModelToPut);
            
            Assert.IsNotNull(result);                                           
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult)); 
        }
        
        //PUT
        [TestMethod]
        public async Task Put_PutComponentType_ReturnCreatedAtActionResult()
        {
            _mockRepositoryComponent.Setup(x => x.GetComponentType(It.Is<int>(i => i == 1)))
                .ReturnsAsync(_componentType.First(x => x.ComponentTypeId == 1));

            var viewModelToPut = new ComponentTypeViewModel
            {
                Name = "Ikke Gimbal",
                IsActive = false
            };
            var result = await  _controller.Put(viewModelToPut.ComponentTypeId, viewModelToPut);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(viewModelToPut, (result as CreatedAtActionResult)?.Value);
        }
        
        //PUT
        [TestMethod]
        public async Task Put_IdNotMatch_ReturnBadRequest()
        {
            var firstManufacturer = _componentType[0];
            var viewModel = new ComponentTypeViewModel { ComponentTypeId = 15, Name = firstManufacturer.Name, IsActive = firstManufacturer.IsActive};
            viewModel.Name = "Kyllingsalat";
            
            var result = await _controller.Put(_componentType[0].ComponentTypeId, viewModel);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestResult)); 
            
        }
        
        
    }
}