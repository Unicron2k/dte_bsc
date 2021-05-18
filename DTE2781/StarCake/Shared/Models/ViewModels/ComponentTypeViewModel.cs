using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace StarCake.Shared.Models.ViewModels
{
    public class ComponentTypeViewModel
    {
        public int ComponentTypeId { get; set; }
        [Required, StringLength(75, MinimumLength = 1, ErrorMessage = "Name must be between {0} and {1} characters!")]
        public string Name { get; set; }
        [Required, DefaultValue(true)]
        public bool IsActive { get; set; }

        public ComponentTypeViewModel Clone()
        {
            return (ComponentTypeViewModel) MemberwiseClone();
        }

        public void Update(ComponentTypeViewModel componentType)
        {
            Name = componentType.Name;
            IsActive = componentType.IsActive;
        }
    }
    
    
}
