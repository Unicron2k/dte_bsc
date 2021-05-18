using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.IO;

// ReSharper disable ClassWithVirtualMembersNeverInherited.Global
// ReSharper disable UnusedAutoPropertyAccessor.Global

namespace StarCake.Shared.Models
{
    public class FileDetail
    {
        public Guid Id { get; set; }
        [Required]
        public DateTime DateEntered { get; set; }
        [Required]
        public bool Deleted { get; set; }
        [Required]
        public string DocumentName { get; set; }
        [Required]
        public string ContentType { get; set; }
        [Required]
        public string RelativePath { get; set; }
        [Required]
        public string DocumentNameServer { get; set; }
        [NotMapped]
        public virtual string FileBase64 { get; set; }
        
        public string FileAsDataUriBase64()
        {
            if (FileBase64==null)
                return "";
            return FileBase64 == null ? null : $"data:{ContentType};base64,{FileBase64}";
        }
    }
}