import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class TennisTest {
    private static Tennis tennis = new Tennis();

    @ParameterizedTest
    @CsvSource({"0,0 - 0",
            "1,15 - 0",
            "1,30 - 0",
            "2,30 - 15",
            "2,30 - 30",
            "1,40 - 30",
            "2,Deuce",
            "0, 40 - 40",
            "1,Advantage player 1",
            "2,Deuce",
            "2,Advantage player 2",
            "2,Game won by player 2"})
    public void point_Player1Wins_GameWonPlayer1(int player, String result){
        assertEquals(tennis.point(player), result);
    }
}