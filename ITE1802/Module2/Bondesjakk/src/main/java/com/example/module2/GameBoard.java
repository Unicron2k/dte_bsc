package com.example.module2;

import java.util.Arrays;

public class GameBoard {
    public static int SIZE, WIDTH, HEIGHT; //
    private char[][] gameBoard; //GameBoard[row][cols] ++ --
    private char symbolP1, symbolP2; // ++ --
    private char currentPlayer; // ++ --
    private int roundsPlayed; // ++ --

    GameBoard(int size){
        GameBoard.SIZE = size;
        this.gameBoard = new char[GameBoard.SIZE][GameBoard.SIZE];
        this.symbolP1 = 'X';
        this.symbolP2 = 'O';
        this.currentPlayer = this.symbolP1;
        this.roundsPlayed=0;
    }

    public void setPlayerSymbols(char player1, char player2){
        this.symbolP1 = player1;
        this.symbolP2 = player2;
    }

    public void setDimensions(int width, int height){
        GameBoard.WIDTH = width;
        GameBoard.HEIGHT = height;
    }

    public void setMark(int row, int col, char player){
        gameBoard[row][col] = player;
        roundsPlayed++;
    }

    public void setMark(int row, int col){
        setMark(row, col, getCurrentPlayer());
    }

    public void setCurrentPlayer(char currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    public void setRoundsPlayed(int roundsPlayed){
        this.roundsPlayed = roundsPlayed;
    }

    public void setGameBoard(char[][] gameBoard){
        this.gameBoard = gameBoard;
    }

    public char getMark(int row, int col){
        return gameBoard[row][col];
    }

    public char getCurrentPlayer(){
        return this.currentPlayer;
    }

    public int getRoundsPlayed(){
        return roundsPlayed;
    }

    public char[] getPlayerSymbols(){
        return new char[]{symbolP1, symbolP2};
    }

    public char[][] getGameBoard(){
        return gameBoard;
    }

    public void clearBoard(){
        for (int i=0; i<gameBoard.length; i++) {
            Arrays.fill(gameBoard[i], (char) 0);
        }
        roundsPlayed=0;
    }

    public int checkCols(){
        int p1Marks=0, p2Marks=0, col=0, row=0;
        for (col=0; col< SIZE; col++){
            for(row=0; row< SIZE; row++){
                if(gameBoard[row][col]==symbolP1){
                    p1Marks++;
                } else if(gameBoard[row][col]==symbolP2){
                    p2Marks++;
                }
            }
            if(p1Marks== SIZE || p2Marks== SIZE){
                return col;
            } else {
                p1Marks=0;
                p2Marks=0;
            }
        }
        return -1;
    }

    public int checkRows(){
        int p1Marks=0, p2Marks=0, col=0, row=0;
        for (row=0; row< SIZE; row++){
            for(col=0; col< SIZE; col++){
                if(gameBoard[row][col]==symbolP1){
                    p1Marks++;
                } else if(gameBoard[row][col]==symbolP2){
                    p2Marks++;
                }
            }
            if(p1Marks== SIZE || p2Marks== SIZE){
                return col;
            } else {
                p1Marks=0;
                p2Marks=0;
            }
        }
        return -1;
    }

    public int checkDiags(){
        int p1Marks, p2Marks, rowCol;
        //check forward diagonal
        for(rowCol=0, p1Marks=0, p2Marks=0; rowCol< SIZE; rowCol++){
            if(gameBoard[rowCol][rowCol]==symbolP1){
                p1Marks++;
            } else if(gameBoard[rowCol][rowCol]==symbolP2){
                p2Marks++;
            }
        }
        if(p1Marks== SIZE || p2Marks== SIZE){
            return 0;
        }

        //Check reverse diagonal
        for(rowCol=0, p1Marks=0, p2Marks=0; rowCol< SIZE; rowCol++){
            if(gameBoard[rowCol][SIZE -1-rowCol]==symbolP1){
                p1Marks++;
            } else if(gameBoard[rowCol][SIZE -1-rowCol]==symbolP2){
                p2Marks++;
            }
        }
        if(p1Marks== SIZE || p2Marks== SIZE){
            return 1;
        }
        return -1;
    }

    public void switchPlayers(){
        if (currentPlayer==this.symbolP1){
            currentPlayer=this.symbolP2;
        } else {
            currentPlayer = this.symbolP1;
        }
    }
}
