using System;
using System.Collections.Generic;
using StarCake.Server.Models.Entity;

namespace StarCakeTest.Server.ControllersTests
{
    public static class ControllerTestHelper
    {
        public static Entity GetEntity()
        {
            return new Entity
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
                    new Component
                    {
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
                    new Component
                    {
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
        }
    }
}