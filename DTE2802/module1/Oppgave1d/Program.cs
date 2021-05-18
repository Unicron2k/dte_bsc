namespace Oppgave1d
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            double value = 0, converted = 0;
            System.String tokens;
            System.Console.Write("Enter command: ");
            tokens = System.Console.ReadLine();
            System.String[] arrTokens = tokens.Split();
            if (arrTokens[0].ToLower() == "konverter" && System.Double.TryParse(arrTokens[1], out value) &&
                (arrTokens[2].ToLower() == "c" || arrTokens[2].ToLower() == "f")) {
                switch (arrTokens[2].ToLower())
                {
                    case "c":
                        converted = cel2fahr(value);
                        System.Console.WriteLine($"{value} celsius is {converted} fahrenheit");
                        break;
                    case "f":
                        converted = fahr2cel(value);
                        System.Console.WriteLine($"{value} fahrenheit is {converted} celsius");
                        break;
                }
            }
        }

        private static double cel2fahr(double celsius){
            return celsius*1.8+32;
        }
        
        private static double fahr2cel(double fahrenheit) {
            return (fahrenheit - 32) * (5 / 9);
        }
    }
}