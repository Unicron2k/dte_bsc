using System;

namespace StarCake.Client.Services
{
    public static class DebugTools
    {
        public static bool DebugEnabled = true;

        public static void DumpToConsole(object obj, string name = "Not given")
        {
            if (!DebugEnabled) return;
            if (obj == null)
                Console.WriteLine(
                    "\n\nDateTime: " + DateTime.Now +
                    "\nObject was null"
                );
            else
                Console.WriteLine(
                    "\n\nDateTime: " + DateTime.Now +
                    "\nName: " + name +
                    "\nType: " + obj.GetType() +
                    "\nContent:\n" + System.Text.Json.JsonSerializer.Serialize(obj)
                );
        }
    }
}