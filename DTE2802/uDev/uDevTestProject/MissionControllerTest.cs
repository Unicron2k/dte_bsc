using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.ViewFeatures;
using Microsoft.Extensions.Logging;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Moq;
using uDev.Controllers;
using uDev.Models.Entity;
using uDev.Models.ViewModels;
using uDev.Repositories.Interface;

namespace uDevTestProject
{
    [TestClass]
    public class MissionControllerTest
    {
        private Mock<IMissionRepository> _mockRepositoryMission;
        private Mock<ISpecialtiesLanguageRepository> _mockRepositorySpecialty;
        private Mock<UserManager<ApplicationUser>> _mockUserManagerLoggedIn;

        private Mock<UserManager<ApplicationUser>> _mockUserManager;
        private List<Mission> _missions;
        private MissionController _controller;
        private Mock<ILogger<MissionController>> _mockLogger;

        [TestInitialize]
        public void SetupContext()
        {
            _mockUserManagerLoggedIn = new Mock<UserManager<ApplicationUser>>();
            _mockUserManagerLoggedIn = MockHelpers.MockUserManager<ApplicationUser>();
            _mockUserManagerLoggedIn.Setup(x => x.GetUserId(It.IsAny<ClaimsPrincipal>()))
                .Returns("2");

            _mockUserManager = MockHelpers.MockUserManager<ApplicationUser>();
            _mockRepositoryMission = new Mock<IMissionRepository>();
            _mockRepositorySpecialty = new Mock<ISpecialtiesLanguageRepository>();
            _mockLogger = new Mock<ILogger<MissionController>>();
            _controller = new MissionController(_mockLogger.Object, _mockUserManagerLoggedIn.Object, _mockRepositoryMission.Object, _mockRepositorySpecialty.Object);
            _missions = new List<Mission>
            {
                new Mission
                {
                    MissionId = 1,
                    Title = "Mission 1",
                    Content = "Content Mission 1",
                    CategoryId = 1,
                    Owner = new ApplicationUser{Id = "1"},
                    Claimed = false,
                    SpecialtyLanguageId = 1


                },
                new Mission
                {
                    MissionId = 2,
                    Title = "Mission 2",
                    Content = "Content Mission 2",
                    CategoryId = 2,
                    Owner = new ApplicationUser{Id = "2"},
                    Claimed = false,
                    SpecialtyLanguageId = 2
                },
                new Mission{
                    MissionId = 3,
                    Title = "Mission 3",
                    Content = "Content Mission 3",
                    CategoryId = 3,
                    Owner = new ApplicationUser{Id = "2"},
                    Claimed = true
                }
            };
        }

        //Index - GET
        [TestMethod]
        public async Task IndexReturnsAllMission()
        {
            _mockRepositoryMission.Setup(x => x.GetAll()).ReturnsAsync(_missions);

            var controller = new MissionController(_mockLogger.Object, _mockUserManager.Object, _mockRepositoryMission.Object, _mockRepositorySpecialty.Object);

            var result = (ViewResult) controller.Index();

            CollectionAssert.AllItemsAreInstancesOfType((ICollection) result.ViewData.Model, typeof(Mission));

            Assert.IsNotNull(result, "View Result is null");
            var mission = result.ViewData.Model as List<Mission>;
            Assert.AreEqual(2, mission.Count, "Got wrong number of products");
        }

        [TestMethod]
        public async Task IndexReturnsNotNullResult()
        {
            _mockRepositoryMission.Setup(x => x.GetAll()).ReturnsAsync(_missions);
            var controller = new MissionController(_mockLogger.Object, _mockUserManager.Object, _mockRepositoryMission.Object, _mockRepositorySpecialty.Object);
            var result = (ViewResult) controller.Index();
            Assert.IsNotNull(result, "View Result is null");
        }

    
        //Create - GET
        [TestMethod]
        public void Create_Get_NullParameter_ReturnsNotNull()
        {
            var result = (ViewResult) _controller.Create();
            Assert.IsNotNull(result, "View Result is null");
        }

        //Create - POST
        [TestMethod]
        public async Task Create_Post_RedirectActionRedirectToIndexAction()
        {
            _controller.ControllerContext = MockHelpers.FakeControllerContext(false);
            var tempData =
                new TempDataDictionary(_controller.ControllerContext.HttpContext, Mock.Of<ITempDataProvider>());
            _controller.TempData = tempData;

            var viewModel = new MissionViewModel()
            {
                Title = "Mission 1",
                Content = "Content 1",
                CategoryId = 1,
                Created = DateTime.Now
            };

            var result = await _controller.Create(viewModel) as RedirectToActionResult;
            Assert.IsNotNull(result, "RedirectToIndex needs to redirect to the Index action");
            Assert.AreEqual("Index", result.ActionName);
        }
        [TestMethod]
        public async Task Create_Post_ViewIsReturned_InputIsNotValid()
        {
            var viewModel = new MissionViewModel
            {
                Title = "",
                Content = "",
                Claimed = false
            };
            _controller.TempData = new Mock<ITempDataDictionary>().Object;
            _controller.ModelState.AddModelError("key", "error message");

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

        //DeleteComfirmed - POST
        [TestMethod]
        public void DeleteConfirmed_Post_DeleteMissionComfirmedRepository()
        {
            _mockRepositoryMission.Setup(x => x.GetMission(2)).ReturnsAsync(_missions[1]);
            _controller.ControllerContext = MockHelpers.FakeControllerContext(true, "2");


            var tempData = new TempDataDictionary(_controller.ControllerContext.HttpContext, Mock.Of<ITempDataProvider>());
            _controller.TempData = tempData;


            var result = _controller.DeleteConfirmed(2);
            Assert.AreEqual("Mission deleted!", _controller.TempData["message"]);
        }
        [TestMethod]
        public void DeleteConfirmed_Post_UserIsNotOwner_TmpDataReturn()
        {
            _mockRepositoryMission.Setup(x => x.GetMission(1)).ReturnsAsync(_missions[0]);
            _controller.ControllerContext = MockHelpers.FakeControllerContext(false, "2");


            var tempData = new TempDataDictionary(_controller.ControllerContext.HttpContext, Mock.Of<ITempDataProvider>());
            _controller.TempData = tempData;


            var result = _controller.DeleteConfirmed(1);
            Assert.AreEqual("Unauthorized!", _controller.TempData["message"]);
        }

        //Delete - GET
        [TestMethod]
        public void Delete_Get_IllegalAndNotValidParameter_ReturnNotfoundResult()
        {
            var result = _controller.Delete(null).Result;
            var result2 = _controller.Delete(5).Result;

            Assert.IsInstanceOfType(result, typeof(NotFoundObjectResult));
            Assert.IsInstanceOfType(result2, typeof(NotFoundObjectResult));
        }
        [TestMethod]
        public async Task Delete_Get_ReturnToViewModel()
        {

            _mockRepositoryMission.Setup(x => x.GetMission(2)).ReturnsAsync(_missions[1]);
            _controller.ControllerContext = MockHelpers.FakeControllerContext(true, "2");

            var result = (ViewResult)await _controller.Delete(2);
            Assert.AreEqual(_missions[1].Title, "Mission 2");
        }
        [TestMethod]
        public async Task Delete_Get_UserIsNotOwnerMission_TmpDataAndReturnToIndex()
        {

            _mockRepositoryMission.Setup(x => x.GetMission(1)).ReturnsAsync(_missions[0]);
            var controller = new MissionController(_mockLogger.Object, _mockUserManager.Object, _mockRepositoryMission.Object, _mockRepositorySpecialty.Object);
            controller.ControllerContext = MockHelpers.FakeControllerContext(false);
            var tempData = new TempDataDictionary(controller.ControllerContext.HttpContext, Mock.Of<ITempDataProvider>());
            controller.TempData = tempData;
            var result = await controller.Delete(1) as RedirectToActionResult;

            Assert.IsNotNull(result, "RedirectToIndex needs to redirect to the Index action");
            Assert.AreEqual("Index", result.ActionName);
            Assert.AreEqual($"You have not acess to delete {_missions[0].Title}.", controller.TempData["message"]);
        }
        
        
        [TestMethod]
        public async Task SaveIsNotCalledWhenBlogIsCreatedAndModelStateIsInValid()
        {
            var model = new MissionViewModel();

            _controller.TempData = new Mock<ITempDataDictionary>().Object;
            _controller.ModelState.AddModelError("key", "error message");

            var result = await _controller.Create(model);
            Assert.IsNotNull(result, "View Result is null");
        }
        

        //Details - GET
        [TestMethod]
        public void Details_GET_WithNoArgumentsReturnsANotFoundResult()
        {
            var result = _controller.Details(null).Result;
            var result2 = _controller.Details(3).Result;

            Assert.IsInstanceOfType(result, typeof(NotFoundObjectResult));
            Assert.IsInstanceOfType(result2, typeof(NotFoundObjectResult));
        }
        [TestMethod]
        public async Task Details_GET_ReturnAMission()
        {
            _mockRepositoryMission.Setup(x => x.GetMission(1)).ReturnsAsync((_missions[0]));

            var result = (ViewResult)await _controller.Details(1);
            Assert.AreEqual(_missions[0].Title, "Mission 1");
            Assert.AreNotEqual(_missions[0].Title, "Mission 2");

        }

        [TestMethod]
        public void Details_GET_ReturnViewWhenValidId()
        {
            _mockRepositoryMission.Setup(x => x.GetMission(1)).ReturnsAsync(_missions[0]);

            var result = _controller.Details(1).Result;

            Assert.IsInstanceOfType(result, typeof(ViewResult));
        }
        
        //EDIT - GET
        [TestMethod]
        public async Task Edit_Get_CalledWithNoArgumentsReturnsANotFoundRsult()
        {
            var result = _controller.Edit(null).Result;
            var result2 =  _controller.Edit(5).Result;

            Assert.IsInstanceOfType(result, typeof(NotFoundObjectResult));
            Assert.IsInstanceOfType(result2, typeof(NotFoundObjectResult));
        }
        [TestMethod]
        public async Task Edit_Get_WitoutUserReturnIndex()
        {
            var viewModel = new MissionViewModel()
            {
                Title = "Mission 1",
                Content = "Content 1",
                CategoryId = 1,
                Created = DateTime.Now,
                Owner = new ApplicationUser { Id = "2" }
            };
            _mockRepositoryMission.Setup(x => x.GetMissionViewModel(1)).Returns((viewModel));

            var controller = new MissionController(_mockLogger.Object, _mockUserManager.Object, _mockRepositoryMission.Object, _mockRepositorySpecialty.Object);
            controller.ControllerContext = MockHelpers.FakeControllerContext(false);
            var tempData = new TempDataDictionary(controller.ControllerContext.HttpContext, Mock.Of<ITempDataProvider>());
            controller.TempData = tempData;
            var result = await controller.Edit(1) as RedirectToActionResult;
            Assert.IsNotNull(result, "RedirectToIndex needs to redirect to the Index action");
            Assert.AreEqual("Index", result.ActionName);
        }
        [TestMethod]
        public async Task Edit_GET_WithCorrectUserReturnMission()
        {

            var viewModel = new MissionViewModel()
            {
                Title = "Mission 1",
                Content = "Content 1",
                CategoryId = 1,
                Created = DateTime.Now,
                Owner = new ApplicationUser { Id = "2" }
            };
            _mockRepositoryMission.Setup(x => x.GetMissionViewModel(2)).Returns(viewModel);
            _controller.ControllerContext = MockHelpers.FakeControllerContext(true, "2");

            var result = (ViewResult)await _controller.Edit(2);
            var missionReturn = result.ViewData.Model as MissionViewModel;

            Assert.AreEqual(_missions[0].Title, "Mission 1");
        }
       
        //EDIT - POST
        [TestMethod]
        public async Task Edit_Post_InValidIdparameter_ReturnNotFound()
        {
            var result = _controller.Edit(4, new MissionViewModel()).Result;

            Assert.IsInstanceOfType(result, typeof(NotFoundObjectResult));
        }

        [TestMethod]
        public async Task Edit_post_OwnerNotLoggedIn_ReturnNotFound()
        {
            var viewModel = new MissionViewModel()
            {
                MissionId = 1,
                Title = "Mission 1",
                Content = "Content 1",
                CategoryId = 1,
                Created = DateTime.Now,
                Owner = new ApplicationUser { Id = "2" }
            };
            
            var controller = new MissionController(_mockLogger.Object, _mockUserManager.Object, _mockRepositoryMission.Object, _mockRepositorySpecialty.Object);

            var result = await controller.Edit(1, viewModel);
            Assert.IsInstanceOfType(result, typeof(NotFoundResult));
        }

        //TODO: Notworking
        [TestMethod]
        public async Task Edit_post_ReturnView()
        {
            var viewModel = new MissionViewModel()
            {
                MissionId = 2,
                Title = "Mission 1",
                Content = "Content 1",
                CategoryId = 1,
                Created = DateTime.Now,
                SpecialtyLanguageId = 2
            };

            var specialtiyLanguage = new SpecialtyLanguage
            {
                SpecialtyLanguageId = 2,
                Name = "C#"
            };
            _mockRepositoryMission.Setup(x => x.GetMission(2)).ReturnsAsync(_missions[1]);
            _mockRepositorySpecialty.Setup(x => x.GetSpecialtyLanguage(2)).ReturnsAsync(specialtiyLanguage);

            _controller.ControllerContext = MockHelpers.FakeControllerContext(false);
            var tempData =
                new TempDataDictionary(_controller.ControllerContext.HttpContext, Mock.Of<ITempDataProvider>());
            _controller.TempData = tempData;



            var result = await _controller.Edit(2, viewModel);
            Assert.IsNotNull(result, "RedirectToIndex needs to ");
        }
        [TestMethod]
        public async Task Edit_post_ModelStateIsNotValid_ReturnTempData()
        {
            var viewModel = new MissionViewModel()
            {
                MissionId = 2,
                Title = "Mission 1",
                Content = "Content 1",
                CategoryId = 1,
                Created = DateTime.Now,
                SpecialtyLanguageId = 2
            };

            _mockRepositoryMission.Setup(x => x.GetMission(2)).ReturnsAsync(_missions[1]);

            _controller.ControllerContext = MockHelpers.FakeControllerContext(false);
            var tempData =
                new TempDataDictionary(_controller.ControllerContext.HttpContext, Mock.Of<ITempDataProvider>());
            _controller.TempData = tempData;
            _controller.ModelState.AddModelError("key", "error message");

            var result = await _controller.Edit(2, viewModel);
            Assert.IsNotNull(result, "RedirectToIndex needs to ");
            Assert.AreEqual($"There was an error updating \"{viewModel.Title}\"", _controller.TempData["error"]);
        }

        //RelinquishClaim - POST
        [TestMethod]
        public async Task RelinquishClaim_POST_ReturnRedirectToAction_My_Missions()
        {
            _mockRepositoryMission.Setup(x => x.GetMission(3)).ReturnsAsync(_missions[2]);
            
            var result = _controller.RelinquishClaim(3);

            Assert.IsNotNull(result, "RedirectToIndex needs to redirect to the Index action");
           // Assert.AreEqual("Index", result.ActionName);
        }
        



    }
}
