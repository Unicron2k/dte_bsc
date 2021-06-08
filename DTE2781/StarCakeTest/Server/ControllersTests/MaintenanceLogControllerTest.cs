using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using StarCake.Server.Controllers;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels.Maintenance;

namespace StarCakeTest.Server.ControllersTests
{
    [TestClass]
    public class MaintenanceLogControllerTest
    {
        private Mock<IMaintenanceLogRepository> _maintenanceLogRepositoryMock;
        private Mock<UserManager<ApplicationUser>> _userManagerMock;
        private Mock<IHttpContextAccessor> _httpContextAccessorMock;
        private Mock<IDepartmentRepository> _departmentRepositoryMock;
        private Mock<IEntityRepository> _entityRepositoryMock;
        private Mock<IComponentRepository> _componentRepositoryMock;
        
        private List<MaintenanceLog> _maintenanceLogs;
        private MaintenanceLogController _maintenanceLogController;
        private ApplicationDbContext _dbContext;
        private ApplicationUser _applicationUser;
        private Entity _entity;
        private Component _component1;

        [TestInitialize]
        public void SetupContext()
        {
            _maintenanceLogRepositoryMock = new Mock<IMaintenanceLogRepository>();
            _userManagerMock = MockHelpers.MockUserManager<ApplicationUser>();
            _httpContextAccessorMock = new Mock<IHttpContextAccessor>();
            _departmentRepositoryMock = new Mock<IDepartmentRepository>();
            _entityRepositoryMock = new Mock<IEntityRepository>();
            _componentRepositoryMock = new Mock<IComponentRepository>();
            
            _maintenanceLogController = new MaintenanceLogController(_maintenanceLogRepositoryMock.Object, _userManagerMock.Object, _httpContextAccessorMock.Object, _departmentRepositoryMock.Object, _entityRepositoryMock.Object, _componentRepositoryMock.Object);

            _maintenanceLogs = new List<MaintenanceLog>
            {
                new MaintenanceLog
                {
                    MaintenanceLogId = 1,
                    Date = DateTime.Today,
                    ACCSeconds = 100,
                    TaskDescription = "Task1",
                    ActionDescription = "Action1",
                    ApplicationUserIdLogged = "1",
                    ApplicationUserLogged = new ApplicationUser(),
                    DepartmentId = 1,
                    Department = new Department(),
                    EntityId = 1,
                    Entity = new Entity(),
                    ComponentId = 1,
                    Component = new Component()
                },
                new MaintenanceLog
                {

                    MaintenanceLogId = 2,
                    Date = DateTime.Today,
                    ACCSeconds = 200,
                    TaskDescription = "Task2",
                    ActionDescription = "Action2",
                    ApplicationUserIdLogged = "2",
                    ApplicationUserLogged = new ApplicationUser(),
                    DepartmentId = 2,
                    Department = new Department(),
                    EntityId = 2,
                    Entity = new Entity(),
                    ComponentId = 2,
                    Component = new Component()
                },
                new MaintenanceLog
                {

                    MaintenanceLogId = 3,
                    Date = DateTime.Today,
                    ACCSeconds = 300,
                    TaskDescription = "Task3",
                    ActionDescription = "Action3",
                    ApplicationUserIdLogged = "3",
                    ApplicationUserLogged = new ApplicationUser(),
                    DepartmentId = 1,
                    Department = new Department(),
                    EntityId = 3,
                    Entity = new Entity(),
                    ComponentId = 3,
                    Component = new Component()
                }
            };
            _applicationUser = new ApplicationUser
            {
                
                Email = "testuser@unittest.com",
                Id = "1",
                NormalizedEmail = "TESTUSER@UNITTEST.COM",
                NormalizedUserName = "TESTUSER@UNITTEST.COM",
                UserName = "testuser@unittest.com",
                FirstName = "Test",
                LastName = "User",
                CurrentLoggedInDepartmentId = 1
            };
            
            _entity = new Entity
            {
                Name = "Entity1",
                DepartmentId = 1,
                Department = new Department(),
                SerialNumber = "123",
                TotalFlightCycles = 100,
                TotalFlightDurationInSeconds = 100,
                CyclesSinceLastMaintenance = 100,
                FlightSecondsSinceLastMaintenance = 100,
                LastMaintenanceDate = DateTime.Today,
                MaxCyclesBtwMaintenance = 10,
                MaxDaysBtwMaintenance = 10,
                MaxFlightSecondsBtwMaintenance = 10,
                EntityId = 1,
                Components = new List<Component>
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
                },
            };
            
            _httpContextAccessorMock
                .Setup(x => x.HttpContext.User
                    .FindFirst(It.IsAny<string>()))
                .Returns(new Claim(ClaimTypes.NameIdentifier, "1"));
            
            _userManagerMock
                .Setup(x => x
                    .FindByIdAsync(It.Is<string>( s => s.Equals("1"))))
                .ReturnsAsync(_applicationUser);
        }

        [TestMethod]
        public async Task GetMaintenanceLogFromIDInCurrentUsersDepartment_GETWithValidID_ReturnsCorrectLog()
        {
            //Arrange
            _maintenanceLogRepositoryMock
                .Setup(x=> x
                    .Get(It.Is<int>(i => i==1)))
                .ReturnsAsync(_maintenanceLogs.First(x => x.MaintenanceLogId == 1));
            
            //Act
            var result = await _maintenanceLogController.Get(1);
            
            //Assert
            Assert.AreEqual(1, result.MaintenanceLogId);
        }
        
        [TestMethod]
        public async Task GetMaintenanceLogFromIDInCurrentUsersDepartment_GETWithInvalidID_ReturnsEmptyLog()
        {
            //Arrange
            _maintenanceLogRepositoryMock
                .Setup(x=> x
                    .Get(It.IsAny<int>()))
                .ReturnsAsync((MaintenanceLog)null);
            
            //Act
            var result = await _maintenanceLogController.Get(1);
            
            //Assert
            Assert.AreNotEqual(1, result.MaintenanceLogId);
        }

        [TestMethod]
        public async Task GetAllMaintenanceLogsInCurrentUsersDepartment_GETWhenNonEmpty_ReturnsListOfLogs()
        {
            //Arrange
            var department = _applicationUser.CurrentLoggedInDepartmentId;
            _maintenanceLogRepositoryMock
                .Setup(x=> x.GetAllInDepartment(It.Is<int>(i => i == department)))
                .ReturnsAsync(_maintenanceLogs.Where(x=> x.DepartmentId == department));
            
            //Act
            var result = await _maintenanceLogController.GetAll();
            
            //Assert
            Assert.AreEqual(2, result.Count());
        }
        
        [TestMethod]
        public async Task GetAllMaintenanceLogsInCurrentUsersDepartment_GETWhenEmpty_ReturnsEmptyistOfLogs()
        {
            //Arrange
            _maintenanceLogs = new List<MaintenanceLog>();
            var department = _applicationUser.CurrentLoggedInDepartmentId;
            _maintenanceLogRepositoryMock
                .Setup(x=> x.GetAllInDepartment(It.Is<int>(i => i == department)))
                .ReturnsAsync(_maintenanceLogs.Where(x=> x.DepartmentId == department));
            
            //Act
            var result = await _maintenanceLogController.GetAll();
            
            //Assert
            Assert.AreEqual(0, result.Count());
        }
        
        [TestMethod]
        public async Task SaveNewMaintenanceLogFromViewModel_POSTEntityNotInUsersDepartment_ShouldFail()
        {
            //Arrange
            var model = new MaintenanceLogItemViewModel
            {
                EntityId = 1,
                ComponentId = 1,
                Name = "Component1",
                SerialNumber = "ABC",
                TaskDescription = "Task1",
                ActionDescription = "Action1"
            };
            _entity.DepartmentId = 3;
            _entityRepositoryMock
                .Setup(x => x
                    .Get(It.Is<int>(i => i == model.EntityId)))
                .Returns(_entity);

            //Act
            var result = await _maintenanceLogController.Save(model);
            //Assert
            Assert.IsTrue(result.GetType() == typeof(UnauthorizedObjectResult));
        }
        
        [TestMethod]
        public async Task SaveNewMaintenanceLogFromViewModel_POSTForComponentOnly_ShouldSucceed()
        {
            //Arrange
            var model = new MaintenanceLogItemViewModel
            {
                EntityId = 1,
                ComponentId = 1,
                Name = "Component1",
                SerialNumber = "ABC",
                TaskDescription = "Task1",
                ActionDescription = "Action1"
            };
            _entityRepositoryMock
                .Setup(x => x
                    .Get(It.Is<int>(i => i == model.EntityId)))
                .Returns(_entity);

            _componentRepositoryMock
                .Setup(x => x
                    .Update(It.IsAny<Component>()));

            _maintenanceLogRepositoryMock
                .Setup(x => x
                    .Save(It.IsAny<MaintenanceLog>()))
                .Callback<MaintenanceLog>(x=>x.MaintenanceLogId=1);
                
            //Act
            var result = await _maintenanceLogController.Save(model);
            
            //Assert
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(OkObjectResult));
            _maintenanceLogRepositoryMock.VerifyAll();
            _maintenanceLogRepositoryMock.Verify(x=>x.Save(It.IsAny<MaintenanceLog>()), Times.Exactly(1));
            _componentRepositoryMock.VerifyAll();
            _componentRepositoryMock.Verify(x => x.Update(It.IsAny<Component>()), Times.Exactly(1));
        }
        
        [TestMethod]
        public async Task SaveNewMaintenanceLogFromViewModel_POSTForEntity_ShouldSucceed()
        {
            //Arrange
            var model = new MaintenanceLogItemViewModel
            {
                EntityId = 1,
                ComponentId = 0,
                Name = "Entity1",
                SerialNumber = "ABC",
                TaskDescription = "Task1",
                ActionDescription = "Action1"
            };
            _entityRepositoryMock
                .Setup(x => x
                    .Get(It.Is<int>(i => i == model.EntityId)))
                .Returns(_entity);

            _componentRepositoryMock
                .Setup(x => x
                    .Update(It.IsAny<Component>()));
            
            _entityRepositoryMock
                .Setup(x => x
                    .Update(It.IsAny<Entity>()));

            _maintenanceLogRepositoryMock
                .Setup(x => x
                    .Save(It.IsAny<MaintenanceLog>()))
                .Callback<MaintenanceLog>(x=>x.MaintenanceLogId=1);
                
            //Act
            var result = await _maintenanceLogController.Save(model);
            
            //Assert
            Assert.IsNotNull(result);
            Assert.IsTrue(result.GetType() == typeof(OkObjectResult));
            _maintenanceLogRepositoryMock.VerifyAll();
            _maintenanceLogRepositoryMock.Verify(x=>x.Save(It.IsAny<MaintenanceLog>()), Times.Exactly(1));
            _componentRepositoryMock.VerifyAll();
            _componentRepositoryMock.Verify(x => x.Update(It.IsAny<Component>()), Times.AtLeast(2));
            _entityRepositoryMock.VerifyAll();
            _entityRepositoryMock.Verify(x => x.Update(It.IsAny<Entity>()), Times.Exactly(1));

        }
        
        [TestMethod]
        public async Task SaveNewMaintenanceLogFromViewModel_POSTSavingToDbFailed_ShouldFailAndNotUpdateEntityOrComponents()
        {
            //Arrange
            var model = new MaintenanceLogItemViewModel
            {
                EntityId = 1,
                ComponentId = 0,
                Name = "Entity1",
                SerialNumber = "ABC",
                TaskDescription = "Task1",
                ActionDescription = "Action1"
            };
            _entityRepositoryMock
                .Setup(x => x
                    .Get(It.Is<int>(i => i == model.EntityId)))
                .Returns(_entity);

            _entityRepositoryMock
                .Setup(x => x
                    .Update(It.IsAny<Entity>()));

            _maintenanceLogRepositoryMock
                .Setup(x => x
                    .Save(It.IsAny<MaintenanceLog>()))
                .Callback<MaintenanceLog>(x=>x.MaintenanceLogId=0);
                
            //Act
            var result = await _maintenanceLogController.Save(model);
            var failResult = result as ObjectResult;
            
            //Assert
            Assert.IsNotNull(result);
            Assert.AreEqual(typeof(ObjectResult), result.GetType());
            Assert.IsNotNull(failResult);
            Assert.AreEqual(500, failResult.StatusCode);
        }

        /*
        [TestMethod]
        public async Task WhatWeAreTryingToDo_GETConditions_ReturnsSomeResult()
        {
            //Arrange
              
            //Act
            
            //Assert
           
        }
        */
    }
}