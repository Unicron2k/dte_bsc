using System;
using System.Collections;
using System.Linq;
using System.Collections.Generic;
using System.Globalization;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using StarCake.Server.Data;
using StarCake.Server.Models.Entity;
using StarCake.Server.Models.Interfaces;
using StarCake.Shared.Models.ViewModels;
using OpenCage.Geocode;
using StarCake.Shared;
using StarCake.Shared.Models;

namespace StarCake.Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    
    public class FlightLogAPIController : ControllerBase
    {
        private readonly ApplicationDbContext context;
        private IFlightLogRepository repository;
        private IEntityRepository entityRepository;
        private IComponentRepository componentRepository;


        public FlightLogAPIController(IFlightLogRepository repository, IEntityRepository entityRepository, IComponentRepository componentRepository)
        {
            this.repository = repository;
            this.entityRepository = entityRepository;
            this.componentRepository = componentRepository;
        }
        
        
        // GET: api/FlightLogAPI
        /// <summary>
        /// Get all flightlog from database
        /// </summary>
        /// <returns>IEnumerable Flightlog</returns>
        [HttpGet]
        public async Task<IEnumerable<FlightLog>> GetFlightLogs()
        {
            return await repository.GetAll();
        }
        
        // GET: api/FlightLogAPI/GetFlightLogsInEntityForTable
        /// <summary>
        /// Get the last flightlogs for specific entity to view table
        /// </summary>
        /// <param name="entityId">int entityId</param>
        /// <param name="numberOfFlightLogs">int, show how many log to show</param>
        /// <returns>IEnumerable FlightLogViewModelTable</returns>
        [HttpGet("GetFlightLogsInEntityForTable/")]
        public async Task<IEnumerable<FlightLogViewModelTable>> GetLastFlightLogsInEntityForTable(
            [FromQuery] int entityId, 
            [FromQuery] int numberOfFlightLogs)
        {
            return await repository.GetFlightLogsInEntityForTable(entityId, numberOfFlightLogs);
        }
        
        // GET: api/FlightLogAPI/{id}
        /// <summary>
        /// Get a specific flightlog by flightlog id
        /// </summary>
        /// <param name="id">int flightlogId</param>
        /// <returns>IActionResult flightlog</returns>
        [HttpGet("{id:int}")]
        public IActionResult Get([FromRoute] int id)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            var flightLog = repository.GetViewModel(id).Result;
            if (flightLog == null)
                return NotFound();
            return Ok(flightLog);
        }
        
        // GET: api/FlightLogAPI/GetAddressFromCoordinate/
        /// <summary>
        /// Get adress from cordinate
        /// </summary>
        /// <param name="latitude">double, latitude</param>
        /// <param name="longitude">double, longitude</param>
        /// <param name="openCageApiKey">openCageApiKey</param>
        /// <returns></returns>
        [HttpGet("GetAddressFromCoordinate/")]
        public async Task<OpenCageResponseReverse> GetAddressFromCoordinate([FromQuery] double latitude, [FromQuery] double longitude, [FromQuery] string openCageApiKey)
        {
            var gc = new Geocoder(openCageApiKey);
            var reverseResult = await gc.ReverseGeocodeAsync(latitude, longitude);
            var locationDictionary = reverseResult.Results.FirstOrDefault()?.ComponentsDictionary;

            //var foo = gc.Geocode("asdf");
            //var faa = foo.Results.FirstOrDefault()?.ComponentsDictionary;

            var openCageResponse = new OpenCageResponseReverse
            {
                StatusCode = reverseResult.Status.Code,
                StatusMessage = reverseResult.Status.Message
            };
            openCageResponse.TryParseDictionaryResponse(locationDictionary);
            return openCageResponse;
        }
        
        // GET: api/FlightLogAPI/GetCoordinateFromAddress/
        /// <summary>
        /// Get Cordinate in latiude and lonitude from a adress
        /// </summary>
        /// <param name="address">string adress</param>
        /// <param name="openCageApiKey">openCageApiKey</param>
        /// <returns></returns>
        [HttpGet("GetCoordinateFromAddress/")]
        public OpenCageResponseForward GetCoordinateFromAddress([FromQuery] string address, [FromQuery] string openCageApiKey)
        {
            var gc = new Geocoder(openCageApiKey);
            var forwardResult = gc.Geocode(address);
            
            var geometry = forwardResult.Results.FirstOrDefault()?.Geometry;
            var openCageResponse = new OpenCageResponseForward()
            {
                StatusCode = forwardResult.Status.Code,
                StatusMessage = forwardResult.Status.Message
            };
            if (geometry != null)
            {
                openCageResponse.Latitude = Convert.ToDecimal(geometry.Latitude);
                openCageResponse.Longitude = Convert.ToDecimal(geometry.Longitude);
            }
            return openCageResponse;
        }
        
        // POST: api/FlightLogAPI
        /// <summary>
        /// Add a new flightlog
        /// </summary>
        /// <param name="flightLogViewModel">Object FlightLogViewModel, passing data to Database</param>
        /// <returns>IActionResult</returns>
        [HttpPost]
        public async Task<IActionResult> Post([FromBody] FlightLogViewModel flightLogViewModel)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var flightLog = new FlightLog
            {
                DepartmentId = flightLogViewModel.DepartmentId,
                ApplicationUserIdPiloted = flightLogViewModel.ApplicationUserIdPiloted,
                ApplicationUserIdLogged = flightLogViewModel.ApplicationUserIdLogged,
                EntityId = flightLogViewModel.EntityId,
                CountryId = flightLogViewModel.CountryId,
                DateTakeOff = flightLogViewModel.DateTakeOff,
                AddressTakeOff = flightLogViewModel.AddressTakeOff,
                LatitudeTakeOff = flightLogViewModel.LatitudeTakeOff,
                LongitudeTakeOff = flightLogViewModel.LongitudeTakeOff,
                SecondsFlown = flightLogViewModel.SecondsFlown,
                AddressLanding = flightLogViewModel.AddressLanding,
                LatitudeLanding = flightLogViewModel.LatitudeLanding,
                LongitudeLanding = flightLogViewModel.LongitudeLanding,
                Remarks = flightLogViewModel.Remarks,
                FlightLogTypeOfOperations = new List<FlightLogTypeOfOperation>()
            };
            foreach (var typeOfOperationViewModel in flightLogViewModel.TypeOfOperationViewModels)
                flightLog.FlightLogTypeOfOperations.Add(new FlightLogTypeOfOperation{TypeOfOperationId = typeOfOperationViewModel.TypeOfOperationId});
            
            await repository.Save(flightLog);
            flightLogViewModel.FlightLogId = flightLog.FlightLogId;
            return CreatedAtAction("Get", new { id = flightLog.FlightLogId }, flightLogViewModel);
        }
        
        /// <summary>
        /// Get all flughtlog by department
        /// </summary>
        /// <param name="base64DepartmentId">departmentId</param>
        /// <param name="base64DateStart">startDate</param>
        /// <param name="base64DateEnd">End date</param>
        /// <returns>IAction</returns>
        [HttpGet("FlightLogsForTable/ByDepartment/")]
        public async Task<IActionResult> GetAllFlightLogsByDepartment(
            [FromQuery] string base64DepartmentId,
            [FromQuery] string base64DateStart,
            [FromQuery] string base64DateEnd)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            var idList = await repository.QuerySearchByDepartment(Coding.Base64.FromBase64<int>(base64DepartmentId),
                Coding.Base64.FromBase64<DateTime>(base64DateStart), Coding.Base64.FromBase64<DateTime>(base64DateEnd));
            var flightLogs = await repository.GetFlightLogTablesByIds(idList);
            if (flightLogs==null)
                return NotFound();
            return Ok(flightLogs);
        }
        
        /// <summary>
        /// Get flightlog by entities
        /// </summary>
        /// <param name="base64EntityIdList">list of all entityId</param>
        /// <param name="base64DateStart">Date start</param>
        /// <param name="base64DateEnd">Date end</param>
        /// <returns>IActionResult</returns>
        [HttpGet("FlightLogsForTable/ByEntities/")]
        public async Task<IActionResult> GetAllFlightLogsByEntities(
            [FromQuery] string base64EntityIdList,
            [FromQuery] string base64DateStart,
            [FromQuery] string base64DateEnd)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            var idList = await repository.QuerySearchByEntities(Coding.Base64.FromBase64<List<int>>(base64EntityIdList),
                Coding.Base64.FromBase64<DateTime>(base64DateStart), Coding.Base64.FromBase64<DateTime>(base64DateEnd));
            var flightLogs = await repository.GetFlightLogTablesByIds(idList);
            if (flightLogs==null)
                return NotFound();
            return Ok(flightLogs);
        }
        
        
    }
}