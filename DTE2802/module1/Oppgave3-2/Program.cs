
namespace Oppgave3_2
{
    internal class Program
    {
        private static int i = 0;
        private static int j = 0;
        
        private static void Swap(int i, int j)
        {
            int temp;
            temp = i;
            Program.i = j;
            Program.j = temp;
        }

        static void Main(string[] args)
        {
            if (args.Length == 2){
                if (!(System.Int32.TryParse(args[0], out i) && System.Int32.TryParse(args[1], out j))){
                    System.Console.Write("Enter i-value: ");
                    System.Int32.TryParse(System.Console.ReadLine(), out i);
                    System.Console.Write("Enter j-value: ");
                    System.Int32.TryParse(System.Console.ReadLine(), out j);
                }
            } else {
                System.Console.Write("Enter i-value: ");
                System.Int32.TryParse(System.Console.ReadLine(), out i);
                System.Console.Write("Enter j-value: ");
                System.Int32.TryParse(System.Console.ReadLine(), out j);
            }

            System.Console.WriteLine($"i is {i}, j is {j}");
            Swap(i,j);
            System.Console.WriteLine($"i is now {i}, and j is {j}");
        }
    }
}