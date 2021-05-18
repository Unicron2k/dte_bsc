using Microsoft.AspNetCore.Authorization.Infrastructure;

 namespace Assignment2Blog.Authorization
{
    public static class BlogOperations
    {
        public static OperationAuthorizationRequirement Create =
          new OperationAuthorizationRequirement { Name = Constants.CreateOperationName };
        public static OperationAuthorizationRequirement Read =
          new OperationAuthorizationRequirement { Name = Constants.ReadOperationName };
        public static OperationAuthorizationRequirement Edit =
          new OperationAuthorizationRequirement { Name = Constants.EditOperationName };
        public static OperationAuthorizationRequirement Delete =
          new OperationAuthorizationRequirement { Name = Constants.DeleteOperationName };
    }

    public class Constants
    {
        public static readonly string CreateOperationName = "Create";
        public static readonly string ReadOperationName = "Read";
        public static readonly string EditOperationName = "Edit";
        public static readonly string DeleteOperationName = "Delete";

        public static readonly string BlogAdministratorsRole = "BlogAdministrators";
    }
}