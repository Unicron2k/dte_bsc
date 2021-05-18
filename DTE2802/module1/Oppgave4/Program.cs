namespace Oppgave4
{
    internal class Program
    {
        public static void Main(string[] args) {
            var binTall1 = new BinTall("1100111");
            var binTall2 = new BinTall(3);

            System.Console.WriteLine("BinTall1, init as binary, \"1100111\": " + binTall1.ToBin() + " -> " + binTall1);
            System.Console.WriteLine("BinTall2, init as integer, \"3\": " + binTall2 + " -> " + binTall2.ToBin());
            System.Console.WriteLine("");
            System.Console.WriteLine("BinTall1 + BinTall2: " + (binTall1+binTall2));            
            System.Console.WriteLine("BinTall1 + 5: " + (binTall1+5));            
            System.Console.WriteLine("5 + BinTall2: " + (5+binTall2));
            System.Console.WriteLine("");
            System.Console.WriteLine("BinTall1 - BinTall2: " + (binTall1-binTall2));            
            System.Console.WriteLine("BinTall1 - 5: " + (binTall1-5));            
            System.Console.WriteLine("5 - BinTall2: " + (5-binTall2));
            System.Console.WriteLine("");
            System.Console.WriteLine("BinTall1 * BinTall2: " + (binTall1*binTall2));            
            System.Console.WriteLine("BinTall1 * 5: " + (binTall1*5));            
            System.Console.WriteLine("5 * BinTall2: " + (5*binTall2));
            System.Console.WriteLine("");
            System.Console.WriteLine("Note: integer-division will result in loss of precision...");
            System.Console.WriteLine("BinTall1 / BinTall2: " + (binTall1/binTall2));            
            System.Console.WriteLine("BinTall1 / 5: " + (binTall1/5));
            System.Console.WriteLine("5 / BinTall2: " + (5 / binTall2));
        }
    }


    internal class BinTall
    {
        private readonly int _number;
        
        public BinTall(string s)
        {
            _number = System.Convert.ToInt32(s, 2);
            /*
            // Conversion by hand
            var length = s.Length-1;
            var num = 0;
            foreach (var c in s) {
                num += (c-48) * (int)Math.Pow(2,length);
                length--;
            }
            number = num;
            */
        }
        
        public BinTall(int i) {
            _number = i;
        }
        
        public static BinTall operator+ (BinTall a, BinTall b) {
            return new BinTall(a._number+b._number);
        }
        public static BinTall operator+ (int a, BinTall b) {
            
            return new BinTall(a+b._number);
        }
        public static BinTall operator+ (BinTall a, int b) {
            
            return new BinTall(a._number+b);
        }
        
        public static BinTall operator- (BinTall a, BinTall b) {
            return new BinTall(a._number-b._number);
        }
        public static BinTall operator- (int a, BinTall b) {
            
            return new BinTall(a-b._number);
        }
        public static BinTall operator- (BinTall a, int b) {
            
            return new BinTall(a._number-b);
        }
        
        public static BinTall operator* (BinTall a, BinTall b) {
            return new BinTall(a._number*b._number);
        }
        public static BinTall operator* (int a, BinTall b) {
            
            return new BinTall(a*b._number);
        }
        public static BinTall operator* (BinTall a, int b) {
            
            return new BinTall(a._number*b);
        }
        
        public static BinTall operator/ (BinTall a, BinTall b) {
            return new BinTall(a._number/b._number);
        }
        public static BinTall operator/ (int a, BinTall b) {
            
            return new BinTall(a/b._number);
        }
        public static BinTall operator/ (BinTall a, int b) {
            
            return new BinTall(a._number/b);
        }

        public override string ToString() {
            return _number.ToString();
        }

        public string ToBin() {
            return System.Convert.ToString(_number, 2);
            /*
            // Conversion by hand
            int temp = number;
            string s = "";
            while (temp>=1){
                s = temp % 2 + s;
                temp /= 2;
            }
            return s;
            */
        }
    }
}