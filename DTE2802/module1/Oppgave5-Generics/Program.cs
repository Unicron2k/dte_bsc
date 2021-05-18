using System;
using System.Collections.Generic;

namespace Oppgave5_Generics
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            var mediaList= new MyMediaList<IMyMedia>();
            
            mediaList.Add(new MyAudio("Ramstein.mp3"));
            mediaList.Add(new MyAudio("Mozart.mp3"));
            mediaList.Add(new MyAudio("Blue_Sthali.mp3"));
            
            mediaList.Add(new MySlides("Frogs.ppt"));
            mediaList.Add(new MySlides("Kittens.ppt"));
            mediaList.Add(new MySlides("Vikings.ppt"));
            
            mediaList.Add(new MyVideo("LotR-FotR.mp4"));
            mediaList.Add(new MyVideo("ReadyPlayer1.mp4"));
            mediaList.Add(new MyVideo("HowToTameYourDragon.mp4"));
            
            mediaList.Current().Play();
            mediaList.Next().Play();
            mediaList.Next().Play();
            mediaList.Next().Play();
            mediaList.Last().Play();
            mediaList.Current().Pause();
            mediaList.Current().Stop();
            mediaList.First().Play();
            mediaList.Delete(3);
            mediaList.Get(3).Play();
            mediaList.Previous().Play();
            
        }
    }

    internal class MyMediaList<T>
    {
        private readonly List<T> _mediaList = new List<T>();
        private int _index;

        public T Next()
        {
            _index++;
            if (_index >= _mediaList.Count) _index = 0;
            return _mediaList[_index];
        }

        public T Previous()
        {
            _index--;
            if (_index < 0)
                _index = _mediaList.Count-1;
            return _mediaList[_index];
        }

        public T First()
        {
            _index = 0;
            return _mediaList[_index];
        }

        public T Last()
        {
            _index = _mediaList.Count-1;
            return _mediaList[_index];
        }

        public T Current()
        {
            return _mediaList[_index];
        }

        public T Get(int index)
        {
            if (index < 0)
                _index = 0;
            else if (index >= _mediaList.Count)
                _index = _mediaList.Count - 1;
            else _index = index;
            return _mediaList[index];
        }

        public void Add(T item)
        {
            _mediaList.Add(item);
        }

        public bool Delete(int index)
        { 
            return _mediaList.Remove(Get(index));
        }
        
    }

  
    
    
    internal class MyAudio : IMyMedia
    {
        private readonly string _name;

        public MyAudio(string name)
        {
            _name = name;
        }
        public void Play()
        {
            Console.WriteLine("Now Playing: " + _name);
        }

        public void Stop()
        {
            Console.WriteLine("Now stopping: " + _name);
        }

        public void Pause()
        {
            Console.WriteLine("Now Pausing: " + _name);
        } 

    }
    
    internal class MyVideo : IMyMedia
    {
        private readonly string _name;

        public MyVideo(string name)
        {
            _name = name;
        }
        public void Play()
        {
            Console.WriteLine("Now Playing: " + _name);
        }

        public void Stop()
        {
            Console.WriteLine("Now stopping: " + _name);
        }

        public void Pause()
        {
            Console.WriteLine("Now Pausing: " + _name);
        } 

    }

    internal class MySlides : IMyMedia
    {
        private readonly string _name;

        public MySlides(string name)
        {
            _name = name;
        }
        public void Play()
        {
            Console.WriteLine("Now Playing: " + _name);
        }

        public void Stop()
        {
            Console.WriteLine("Now stopping: " + _name);
        }

        public void Pause()
        {
            Console.WriteLine("Now Pausing: " + _name);
        }  
        
    }
    
    public interface IMyMedia
    {
        void Play(); 
        void Stop(); 
        void Pause(); 
    }
}