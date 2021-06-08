using System;
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
    public class ComponentControllerTest
    {
        private Mock<IComponentRepository> _mockRepositoryComponent;
        private Mock<UserManager<ApplicationUser>> _mockUserManager;
        private List<Component> _components;
        private ComponentController _controller;
        private ApplicationDbContext _dbContext;
       

        [TestInitialize]
        public void SetupContext()
        {
            _mockUserManager = MockHelpers.MockUserManager<ApplicationUser>();
            _mockRepositoryComponent = new Mock<IComponentRepository>();
            _controller = new ComponentController(_mockRepositoryComponent.Object, _dbContext);

            _components = new List<Component>
            {
                new Component {
                    Name = "Component1",
                    DepartmentId = 1,
                    Department = new Department(),
                    SerialNumber = "ABC",
                    TotalFlightCycles = 100,
                    TotalFlightDurationInSeconds = 100,
                    CyclesSinceLastMaintenance = 100,
                    FlightSecondsSinceLastMaintenance = 100,
                    LastMaintenanceDate = DateTime.Today,
                    MaxCyclesBtwMaintenance = 100,
                    MaxDaysBtwMaintenance = 100,
                    MaxFlightSecondsBtwMaintenance = 100,
                    ComponentId = 1,
                    EntityId = 1
                },
                new Component {
                    Name = "Component2",
                    DepartmentId = 1,
                    Department = new Department(),
                    SerialNumber = "DEF",
                    TotalFlightCycles = 200,
                    TotalFlightDurationInSeconds = 200,
                    CyclesSinceLastMaintenance = 200,
                    FlightSecondsSinceLastMaintenance = 200,
                    LastMaintenanceDate = DateTime.Today,
                    MaxCyclesBtwMaintenance = 200,
                    MaxDaysBtwMaintenance = 200,
                    MaxFlightSecondsBtwMaintenance = 200,
                    ComponentId = 2,
                    EntityId = 1
                }
            };
        }
        
        //GET Component
        [TestMethod]
        public async Task GetComponent_GET_ReturnCorrectComponent()
        {
            _mockRepositoryComponent.Setup(x => x.Get(1)).ReturnsAsync(_components[0]);

            var result = await _controller.Get(1);
            
            Assert.AreEqual("Component1", result.Value.Name);
        }
        
        //GET Component
        [TestMethod]
        public async Task GetComponent_NullParameter_ReturnNotfound()
        {
            var result = await  _controller.Get(null);
            Assert.IsInstanceOfType(result.Result, typeof(NotFoundObjectResult));
        }
        
        //GET Component
        [TestMethod]
        public async Task GetAllComponents_AllComponents_ReturnList()
        {
            _mockRepositoryComponent.Setup(x => x.GetAll()).ReturnsAsync(_components);

            var result = await _controller.GetComponents();
            Assert.IsNotNull(result, "View Result is null");
        }
        
        //GET Component
        [TestMethod]
        public async Task GetComponent_GETNullFromRepo_ReturnNotFound()
        {
            _mockRepositoryComponent.Setup(x => x.Get(It.IsAny<int>())).ReturnsAsync(() => null);

            var result = await _controller.Get(5);
            
            Assert.IsInstanceOfType(result.Result, typeof(NotFoundResult));
        }
        
        //POST Component
        [TestMethod]
        public async Task PostComponent_PostModelIsNotValid_ReturnBadRequest()
        {
            var componentToPost = new Component {
                Name = "Component3",
                DepartmentId = 1,
                Department = new Department(),
                SerialNumber = "ABC",
                TotalFlightCycles = 100,
                TotalFlightDurationInSeconds = 100,
                CyclesSinceLastMaintenance = 100,
                FlightSecondsSinceLastMaintenance = 100,
                LastMaintenanceDate = DateTime.Today,
                MaxCyclesBtwMaintenance = 100,
                MaxDaysBtwMaintenance = 100,
                MaxFlightSecondsBtwMaintenance = 100,
                EntityId = 3
            };

            _controller.ModelState.AddModelError("test", "test");
            var result = await _controller.Post(componentToPost);

            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult));
        }
        
        //POST
        [TestMethod]
        public async Task PostComponent_ReturnCreateAtAction()
        {
            var componentToPost = new Component {
                Name = "Component3",
                DepartmentId = 1,
                Department = new Department(),
                SerialNumber = "ABC",
                TotalFlightCycles = 100,
                TotalFlightDurationInSeconds = 100,
                CyclesSinceLastMaintenance = 100,
                FlightSecondsSinceLastMaintenance = 100,
                LastMaintenanceDate = DateTime.Today,
                MaxCyclesBtwMaintenance = 100,
                MaxDaysBtwMaintenance = 100,
                MaxFlightSecondsBtwMaintenance = 100,
                EntityId = 3
            };

            var result = await _controller.Post(componentToPost);
            
            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(componentToPost, (result as CreatedAtActionResult)?.Value);
        }
        
        //PUT
        [TestMethod]
        public async Task Put_ModelIsNotValid_returnBadRequest()
        {
            var modelToPut = new Component {
                Name = "Component3",
                DepartmentId = 1,
                Department = new Department(),
                SerialNumber = "ABC",
                TotalFlightCycles = 100,
                TotalFlightDurationInSeconds = 100,
                CyclesSinceLastMaintenance = 100,
                FlightSecondsSinceLastMaintenance = 100,
                LastMaintenanceDate = DateTime.Today,
                MaxCyclesBtwMaintenance = 100,
                MaxDaysBtwMaintenance = 100,
                MaxFlightSecondsBtwMaintenance = 100,
                EntityId = 3
            };

            _controller.ModelState.AddModelError("Put fail", "Put fail");
            var result = await  _controller.Put(modelToPut.ComponentId, modelToPut);
            
            Assert.IsNotNull(result);                                           
            Assert.IsTrue(result.GetType() == typeof(BadRequestObjectResult)); 
        }
        
        //PUT
        [TestMethod]
        public async Task Put_PutComponent_ReturnCreatedAtActionResult()
        {
            _mockRepositoryComponent.Setup(x => x.Get(It.Is<int>(i => i == 1)))
                .ReturnsAsync(_components.First(x => x.ComponentId == 1));

            var modelToPut = new Component {
                Name = "Component3",
                DepartmentId = 1,
                Department = new Department(),
                SerialNumber = "ABC",
                TotalFlightCycles = 100,
                TotalFlightDurationInSeconds = 100,
                CyclesSinceLastMaintenance = 100,
                FlightSecondsSinceLastMaintenance = 100,
                LastMaintenanceDate = DateTime.Today,
                MaxCyclesBtwMaintenance = 100,
                MaxDaysBtwMaintenance = 100,
                MaxFlightSecondsBtwMaintenance = 100,
                EntityId = 3
            };

            var result = await  _controller.Put(modelToPut.ComponentId, modelToPut);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(CreatedAtActionResult));
            Assert.AreEqual(modelToPut, (result as CreatedAtActionResult)?.Value);
        }
        
        //PUT
        [TestMethod]
        public async Task Put_IdNotMatch_ReturnBadRequest()
        {
            var modelToPut = new Component {
                Name = "Component3",
                DepartmentId = 1,
                Department = new Department(),
                SerialNumber = "ABC",
                TotalFlightCycles = 100,
                TotalFlightDurationInSeconds = 100,
                CyclesSinceLastMaintenance = 100,
                FlightSecondsSinceLastMaintenance = 100,
                LastMaintenanceDate = DateTime.Today,
                MaxCyclesBtwMaintenance = 100,
                MaxDaysBtwMaintenance = 100,
                MaxFlightSecondsBtwMaintenance = 100,
                EntityId = 3
            };
            modelToPut.Name = "Kyllingsalat";
            
            var result = await _controller.Put(_components[0].ComponentId, modelToPut);
            
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(BadRequestResult));
        }
    }
}