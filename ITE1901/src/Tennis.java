public class Tennis {
    private int player1Score;
    private int player2Score;

    Tennis(){
        player1Score=0;
        player2Score=0;
    }

    public  String point(int player){
        if(player==1){
            if(player1Score<30) {
                player1Score += 15;
            } else {
                player1Score += 10;
            }
        } else if(player==2){
            if(player2Score<30) {
                player2Score += 15;
            } else {
                player2Score += 10;
            }
        } else {
            return player1Score + " - " + player2Score;
        }

        return getScore();
    }

    public String getScore(){
        if(player1Score==player2Score && (player1Score>=40 || player2Score>=40)) {
            return "Deuce";
        }
        if(player1Score<=40 && player2Score<=40){
            return player1Score + " - " + player2Score;
        }
        int delta=player1Score-player2Score;
        if(delta>=20){
            return "Game won by player 1";
        }
        if(delta>=10){
            return "Advantage player 1";
        }
        if(delta>=-10){
            return "Advantage player 2";
        }
        if(delta>=-20){
            return "Game won by player 2";
        }
        else return player1Score + " - " + player2Score;
    }
}
