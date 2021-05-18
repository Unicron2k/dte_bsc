namespace Oppgave3_1
{
    internal class Program
    {
        public static void Main(string[] args)
        {
             Lifeform[] lifeforms = {
                 new Human(), 
                 new Dog(),
                 new Sheep(),
                 new Monkey(),
                 new Snake(),
                 new Frog(),
                 new Spider(),
                 new Mosquito(),
             };

             foreach (Lifeform lifeform in lifeforms)
             {
                 lifeform.MakeSound();
             }
        }
    }

    public class Lifeform {
        public virtual void MakeSound() {
            System.Console.WriteLine("This is a generic life-form. It currently has no sound...");            
        }
    }

    public class Mammal : Lifeform {
        public override void MakeSound() {
            System.Console.WriteLine("This is a generic mammal. It currently has no sound...");            
        }
    }
    
    public class Amphibian : Lifeform {
        public override void MakeSound() {
            System.Console.WriteLine("This is a generic amphibian. It currently has no sound...");            
        }
    }
    
    public class Insect : Lifeform {
        public override void MakeSound() {
            System.Console.WriteLine("This is a generic insect. It currently has no sound...");            
        }
    }
    
    public class Human : Mammal {
        public override void MakeSound() {
            System.Console.WriteLine("*Hahahaha*");
            System.Media.SoundPlayer player = new System.Media.SoundPlayer(@"./Sounds/human.wav");
            player.PlaySync();
        }
    }
    
    public class Dog : Mammal {
        public override void MakeSound() {
            System.Console.WriteLine("*bark bark*");
            System.Media.SoundPlayer player = new System.Media.SoundPlayer(@"./Sounds/dog.wav");
            player.PlaySync();
        }
    }
    
    public class Sheep : Mammal {
        public override void MakeSound() {
            System.Console.WriteLine("*baaaah*");
            System.Media.SoundPlayer player = new System.Media.SoundPlayer(@"./Sounds/sheep.wav");
            player.PlaySync();
        }
    }
    
    public class Monkey : Mammal {
        public override void MakeSound() {
            System.Console.WriteLine("*Screech screech*");
            System.Media.SoundPlayer player = new System.Media.SoundPlayer(@"./Sounds/monkey.wav");
            player.PlaySync();
        }
    }
    
    public class Snake : Amphibian {
        public override void MakeSound() {
            System.Console.WriteLine("*rattle rattle*");
            System.Media.SoundPlayer player = new System.Media.SoundPlayer(@"./Sounds/snake.wav");
            player.PlaySync();
        }
    }
    
    public class Frog : Amphibian {
        public override void MakeSound() {
            System.Console.WriteLine("*ribbit, ribbit*");
            System.Media.SoundPlayer player = new System.Media.SoundPlayer(@"./Sounds/frog.wav");
            player.PlaySync();
        }
    }
    
    public class Spider : Insect {
        public override void MakeSound() {
            System.Console.WriteLine("*skitter skitter*");
            System.Media.SoundPlayer player = new System.Media.SoundPlayer(@"./Sounds/spider.wav");
            player.PlaySync();
        }
    }
    
    public class Mosquito : Insect {
        public override void MakeSound() {
            System.Console.WriteLine("*bzzzzz bzzzzzz*");
            System.Media.SoundPlayer player = new System.Media.SoundPlayer(@"./Sounds/mosquito.wav");
            player.PlaySync();
        }
    }
    
}