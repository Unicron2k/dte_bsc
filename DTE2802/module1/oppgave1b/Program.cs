using System;

namespace oppgave1b
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            var height = 0.0;
            var weight = 0.0;
            var bmi = 0.0;
            
            System.Console.Write("Calculate Your BMI!\nEnter weight (in kilograms): ");
            weight = System.Double.Parse(System.Console.ReadLine());
            System.Console.Write("Enter height (in meters): ");
            height = System.Double.Parse(System.Console.ReadLine());
            bmi = weight / (height * height);
            System.Console.WriteLine($"Your BMI is {bmi}\n");

            if (bmi < 18.5) {
                System.Console.WriteLine("You are underweight!");
            } else if (bmi >= 18.5 && bmi <= 24.9) {
                System.Console.WriteLine("You are normal-weight.");
            } else if (bmi > 24.9 && bmi <= 29.9) {
                System.Console.WriteLine("You are you are overweight!!");
            } else if (bmi >29.9) {
                System.Console.WriteLine("You are obese!!!");
            }
            
        }
    }
}