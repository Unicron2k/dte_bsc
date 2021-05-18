using System;
using System.Windows.Forms;
using System.Text.RegularExpressions;

namespace Oppgave7
{
    public partial class formRegEx : Form
    {
        public formRegEx()
        {
            InitializeComponent();
        }

        private void buttonSplit_Click(object sender, EventArgs e)
        {
            string tekst = richTextBoxFrom.Text;
            string re = textBoxRegEx.Text;

            Regex regex = new Regex(@re /*"\d+|\s+|, +|,|\. +|\."*/);
            string[] result = regex.Split(tekst);
            foreach (string substr in result) 
                richTextBoxTo.AppendText(substr + "\n");
        }

        private void buttonMatches_Click(object sender, EventArgs e)
        {
            string tekst = richTextBoxFrom.Text;
            string re = textBoxRegEx.Text;

            Regex regex = new Regex(@re);
            
            MatchCollection result = regex.Matches(tekst);

            foreach (Match myMatch in result)
                richTextBoxTo.AppendText(myMatch.ToString() + "\n");
        }

        private void buttonClear_Click(object sender, EventArgs e)
        {
            richTextBoxTo.Clear();
        }
        
        private void buttonReplace_Click(object sender, EventArgs e)
        {
            string tekst = richTextBoxFrom.Text;
            string re = textBoxRegEx.Text;

            Regex regex = new Regex(@re /*"\d+|\s+|, +|,|\. +|\."*/);
            string result = regex.Replace(tekst, "TALL", Int32.MaxValue);
            richTextBoxTo.AppendText(result + "\n");
        }
    }
}