namespace StarCake.Shared
{
    public static class Roles
    {
        public const string Admin = "Administrator";
        public const string OrganizationMaintainer = "Organization Maintainer";
        public const string DepartmentMaintainer = "Department Maintainer";
        public const string User = "User";

        public static string GetDepartmentMaintainerAndUp()
        {
            return ToCommaSeparated(new[]
            {
                Admin,
                OrganizationMaintainer,
                DepartmentMaintainer
            });
        }

        public static string GetOrganizationMaintainerAndUp()
        {
            return ToCommaSeparated(new[]
            {
                Admin,
                OrganizationMaintainer
            });
        }

        private static string ToCommaSeparated(string[] stringArray)
        {
            return string.Join(", ", stringArray);
        }
    }
}