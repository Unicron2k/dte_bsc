namespace StarCake.Client.Pages.FlightLogging
{
    // Class used to store Flight Duration in FlightLogging.razor
    public class DurationItem
    {
        public int Value;
        public int SecondsPerValue;
        public int Max;
        public int Min;
        public string Name;

        public void Add()
        {
            if (Value >= 0 && Value < Max)
            {
                Value++;
            }
        }

        public void Pop()
        {
            if (Value > 0 && Value <= Max)
            {
                Value--;
            }
        }

        public string HelperText()
        {
            return $"Max is {Max}";
        }

        public string LabelText()
        {
            return $"{Name} flown";
        }


        public bool IsValid()
        {
            return Value >= Min && Value <= Max;
        }
    }
}