# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html). \
Adheres to the [GitLab Flavoured Markdown (GFM)](https://docs.gitlab.com/ee/user/markdown.html)






## [Released]
## [1.0.0] - 2021-05-10
- Final version released

## [Unreleased]
## [0.3.9] - 2021-05-10
### ADDED
- Misc. tests

## [0.3.8] - 2021-05-10
### ADDED
- Exporting of FlightLogs for PDF
### CHANGED
- New layout for Log Flight page
- Added new values for FlightLogs as of new specification from Fly Lavt AS
- Added forward geolocation search (Address to coordinates)
- Added reverse geolocation search (Coordinates to address)

## [0.3.7] - 2021-05-10 
### Added
- Organization maintainer kan now put a user in different departments
- Organization maintainer can now choose departmens to se user and give maintainer acess
- Only show Departments is active in selectlist and same organization.

## [0.3.6] - 2021-05-04
### Added
- Organization maintainer kan now disable and activate a department.

## [0.3.5] - 2021-04-29
### Added
- Maintenance-status-list now contains a clickable icon
  - When clicked, users will be navigated to "Record new maintenance"-page with \
    the entity pre-selected and relevant filtering applied

## [0.3.4] - 2021-04-29
### Added
- OrganizationMaintainer can create a new Department.
- Organization maintainer kan now disable and activate a department.
- Will se two tables by available departments and one table for member of department.
### Changed
- Minor UI changes to Users table, centred.
- In organization user tables, remove the current user showing the table.

## [0.3.3] - 2021-04-29
### Added
- Implemented EmailSender
  - Users must now verify their email by clicking on the link in the received email before they can use the system
  - Users can now reset forgotten passwords
- Employee-number is now a required property during account-creation
- Users can now sign in by using either their employee-number or email
### Changed
- Organization/department-selection during account-creation have been removed.\
  Created Account will now be registered in the department the administrator/maintainer was logged into when account-creation started

## [0.3.2] - 2021-04-28
### Added
- IsActive state for ComponentType, EntityTypes and Manufactures. When item is not active, cannot add new entities with disabled xxxTypes. 
- OrganizationMaintainer can now update the organization info
- Can change maintenance values for current department
- Authorizes required to some API Controllers
### Changed
- Minor UI-changes for ComponentType, EntityTypes and Manufactures.
- Fill necessary points to ApplicationDbContext

## [0.3.1] - 2021-04-28
### Added
- Added maintenance-log export functionality to backend
  - Users can now export logs as PDF, Excel, CSV and JSON-data

## [0.3.0] - 2021-04-27
### Added
- Added overview of entities and components in need of maintenance
- Added UI for recording a maintenance-log
- Added UI for viewing of previous logs
### Changed
- Minor UI-changes

## [0.2.8] - 2021-04-25
### Changed
- FlightLog model in database is changed due to new Specification from Fly Lavt AS
- Path structure for Files. Server now uses only environment variables to access Files
    - Server will now run on both Unix and Windows systems
  
## [0.2.7] - 2021-04-24
### Added
- More functionality for statistics view when exporting FlightLogs
- Entities are now displayed with pictures from the server under Entity tab in Administrate-page

## [0.2.6] - 2021-04-23
### Added
- Added IsActive state in TypeOfOperation
- Added functionality to add, edit and change active state of all TypeOfOperations
- Added functionality to get an image file from /Server as Base64 either compressed or in RAW format

## [0.2.5] - 2021-04-18
### Added
- File uploading functionality in Server. Files are stored in their respective folder corresponding with its extension.
- Started implementing Image uploading and display functionality when adding a new entity.
- When exporting FlightLogs, users can now view exported FlightLogs in.
  - User can group FlightLogs by the person who logged it, piloted it and group by which type the FlightLog was.
  - User can choose to view data with interpolation.
- Ported adding and updating countries to MudBlazor framework and added it to Administrate site.
- Added new members on Entity.cs for Image file.
- Added functionality in ApplicationDbContext.cs for seeding random data to the database. 
  - Added Static class 'DbTestBuilder' in ApplicationDbContext.cs for generating various random members 
  - Number of random FlightLogs to be seeded can be defined.
### Changed
- Moved project from MudBlazor 2.0.3 to 2.0.7
- Ported various variables from Entity.cs and Component.cs to MasterMaintenance.cs with inheritance.
- More data is now loaded in index.razor to AppData-singleton object.

## [0.2.5] - 2021-04-22
### ADDED
- Department/Organization maintainer can now given a account maintainer role for current Department and current Organization

## [0.2.4] - 2021-04-11
### Added
- Department maintainer can now change info for current department
###Changed
- Modal in EntityTypes, ComponentType and Manufacturer changed to use MudDialog. 

## [0.2.3] - 2021-04-07
### Added
- Department Maintainer can now Activate or disable a account in current Department
####Older stuff
- Department Maintainer/Organization Maintainer can now Add and edit an EntityTypes, Manufacturer
  and ComponentTypes
  
## [0.2.2] - 2021-04-06
### Added
- Implemented support for DarkMode through the entire app
### Changed
- Changed layout for changing department

## [0.2.1] - 2021-04-05
### Added
- Live UTC-clock on NavBar
### Changed
- Changed the NavBar to Mud-framework
- Minor cleanups in various files

## [0.2.0] - 2021-04-03
### Added
- Users can now un-arhcive an entity

## [0.1.9] - 2021-04-03
### Added
- Users can now assign an entity to an archived component
- Users can now archive both entities and components
- Users can now edit components

## [0.1.8] - 2021-03-29
### Added
- Users can now automatically get City/Zipcode/Address when logging a flight using their GPS-coordinate
- Users can now archive entities

## [0.1.7] - 2021-03-28
### Added
- Users can now add entities with corresponding components
### Changed
- Started implementing archiving functionality

## [0.1.6] - 2021-03-23 
### Added
- Users can now change departments in every razor page
- Only Administrative users can now register new users
- Data preload on index.razor
- Users can now download FlightLogs as CSV file
- Preliminary Administrative page added
- Boat is still floating
### Changed
- Merged name2 and name_work2 into Master
### Removed
- Some unused API calls

## [0.1.5] - 2021-03-18 
### Added
- Implemented Exporting page
    - Users can now download flightlogs as Excel file

## [0.1.4] - 2021-03-16
### Added
- Implemented FlightLogging
- Implemented GPS geolocation for Flightlogging
###Changed
- Fixed relationships in Entity Framework

## [0.1.3] - 2021-03-05
### Added
- Support for Modal in Blazor components
- Support for MufBlazor
- New icons -> bootstrap-icons-1.4.0
- More seed data in applicationDbContext.cs
###Changed
- Fixed relationships in Entity Framework

## [0.1.2] - 2021-03-03
### Added
- Possibility to add claim-data to OIDC Auth-token. See [StarCake.Server.Startup.cs#60](StarCake/Server/Startup.cs)
- Added First/Last-name to Database/registration/webpage

## [0.1.1] - 2021-02-24
### Added
- About-page
- Privacy-page
- Cookie-Control
- Basic GDPR consent-approval/revoke-functionality
- Settings-service

## [0.1.0] - 2021-02-22
### Added
- Moved to using Blazor WASM frontend
### Changed
- Default UI is now based on the default Blazor-UI
### Removed
(To be re-implemented at a later date)
- About-page
- Basic GDPR consent-approval/revoke-functionality
- Settings-service
- Tag-helper for "i-role" tag-attribute

## [0.0.1] - 2021-02-01
### Added
- Basic page
- Custom IdentityUser
- About-page
- Roles
- Claims
- Basic GDPR consent/withdraw-functionality
- Settings-service
- Tag-helper for "i-role" tag-attribute
- .gitignore
- Licensing-information
- Changelog
- Readme
- Test-project
- Seed-data for Admin-user (Subject to change)
