using System;
using System.IO;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Hosting;
using StarCake.Server.Data;
using StarCake.Shared;
using StarCake.Shared.Models;

namespace StarCake.Server.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class FilesController : ControllerBase
    {
        // ReSharper disable once InconsistentNaming
        private static readonly string RootPath = Path.Combine("wwwroot", "Files");
        private readonly IHostEnvironment _environment;
        private readonly ApplicationDbContext _dbContext;

        public FilesController(IHostEnvironment environment, ApplicationDbContext dbContext)
        {
            _environment = environment;
            _dbContext = dbContext;
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromForm]IFormFile file)
        {
            // Check if file is null or empty
            if (file == null || file.Length == 0)
                return BadRequest("Upload a file");
            // Get its filename and check if the extension is legal
            var fileName = file.FileName;
            var relPath = FileTypeEnums.FileUploads.GetRelativeFilePath(fileName);
            if (relPath==null)
                return BadRequest("File extension is illegal!");
            
            // Get extension of file
            var extension = Path.GetExtension(fileName);
            
            // Generate a new fileName and save it on the Server
            var newFileName = $"{Guid.NewGuid()}{extension}";
            var filePath = Path.Combine(_environment.ContentRootPath, RootPath, relPath, newFileName);
            await using (var fileStream = new FileStream(filePath, FileMode.Create, FileAccess.Write))
                await file.CopyToAsync(fileStream);

            // Generate a FileDetail.cs object and save the File's information on the server
            var fileDetail = new FileDetail
            {
                DateEntered = DateTime.Now,
                Deleted = false,
                DocumentName = fileName,
                ContentType = file.ContentType,
                RelativePath = relPath,
                DocumentNameServer = newFileName
            };
            await _dbContext.FileDetails.AddAsync(fileDetail);
            await _dbContext.SaveChangesAsync();
            
            // Return as fileDetail stored in database
            return Ok(fileDetail);
        }
        
        [HttpGet("{id:int}")]
        public IActionResult GetFileDetail([FromRoute] int id)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);
            var entity = _dbContext.FileDetails.FindAsync(id);
            return Ok(entity);
        }

        public static string GetFileDetailGlobalPath(FileDetail fileDetail)
        {
            return Path.Combine(
                RootPath,
                fileDetail.RelativePath,
                fileDetail.DocumentNameServer
            );
        }

        /*
        [HttpGet]
        public IActionResult Get([FromRoute] )
        {
            var fileDetail = _context.FileDetail
                .Where(x => x.Id == id)
                .FirstOrDefault();
            if(fileDetail != null)
            {
                System.Net.Mime.ContentDisposition cd = new System.Net.Mime.ContentDisposition
                {
                    FileName = fileDetail.DocumentName,
                    Inline = false
                };
                Response.Headers.Add("Content-Disposition", cd.ToString());

                //get physical path
                var path = _env.ContentRootPath;
                var fileReadPath = Path.Combine(path, "Files", fileDetail.Id.ToString() + fileDetail.DocType);

                var file = System.IO.File.OpenRead(fileReadPath);
                return File(file, fileDetail.DocType);
            }
            else
            {
                return StatusCode(404);
            }
        }*/
    }
}