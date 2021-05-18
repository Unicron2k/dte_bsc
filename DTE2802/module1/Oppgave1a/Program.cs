namespace Oppgave1a
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            var width = 0;
            var length = 0;
            var area = 0;
            var circ = 0;
            
            System.Console.Write("Calculate area an circumference of a rectangle!\nEnter length: ");
            length = System.Int32.Parse(System.Console.ReadLine());
            System.Console.Write("Enter width: ");
            width = System.Int32.Parse(System.Console.ReadLine());
            area = length * width;
            circ = (length + width) * 2;
            System.Console.WriteLine($"The area is {area} and the circumference is {circ}");

        }
    }
}