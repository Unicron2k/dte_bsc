using System;
using System.Collections.Generic;
using System.Text;

namespace Oppgave5_Debug
{
    class Program
    {
        static void Main(string[] args)
        {
            MyClass1 o1 = new MyClass1();
            string tekst = "Dette er en setning!";
            Console.WriteLine(tekst);

            Console.WriteLine(o1);

            unsafe
            {
                double* myStackTable = stackalloc double[20];

                for (int i = 0; i < 20; i++)
                {
                    *(myStackTable + i) = i * i;
                }
                

                //double x = (double) (myStackTable[22]);
                //Console.WriteLine("{0}", x);
                Console.WriteLine("Writing contents of \"MyStackTable\" via pointer-arithmetic: ");
                for (int i = 0; i < 20; i ++)
                {
                    Console.WriteLine(*(myStackTable+i));
                }
                
                Console.WriteLine("\nWriting contents of \"MyStackTable\" via array-indexing: ");
                for (int i = 0; i < 20; i++)
                {
                    Console.WriteLine(myStackTable[i]);
                }
            }
        }
    }

    class MyClass1
    {
        private string member1 = "Hei fra MyClass1!";
        private int member2 = 1000;

        public MyClass1()
        {
            MyClass2 mc2 = new MyClass2();
            mc2.ToString();
        }

        public override string ToString()
        {
            return base.ToString() + String.Format("{0} {1}", member1, member2);
        }
    }

    class MyClass2
    {
        private string member1 = "Hei fra MyClass2!";
        private int member2 = 2000;

        public MyClass2()
        {
        }

        public override string ToString()
        {
            return base.ToString() + String.Format("{0} {1}", member1, member2);
        }
    }
}