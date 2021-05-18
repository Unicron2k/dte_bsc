namespace uDev.Models.Entity
{
    public class Rating
    {
        public int RatingId { get; set; }
        
        public ApplicationUser RatedBy { get; set; }
        
        public Mission RatingFor { get; set; }

        public int Score { get; set; }

        public string Comment { get; set; }
    }
}