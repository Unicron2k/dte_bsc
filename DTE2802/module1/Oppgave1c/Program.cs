using System;
using System.Drawing;
using System.Windows.Forms;

namespace Oppgave1c
{
    internal class Program : Form
    {
        private double bmi = 0;
        private double height = 0;
        private double weight = 0;
        
        public static void Main(string[] args)
        {
            Application.Run(new Program());
        }

        public Program()
        {
            Button calculate = new Button();
            calculate.Text = "Calculate BMI";
            calculate.Click += Button_Click;
            Controls.Add(calculate);
        }

        private void Button_Click(object sender, EventArgs e)
        {
            MessageBox.Show($"Your BMI is {bmi}");
        }
    }
}







