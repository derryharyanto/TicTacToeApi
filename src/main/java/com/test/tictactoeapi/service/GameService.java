package com.test.tictactoeapi.service;

import com.test.tictactoeapi.dto.PlayerDto;
import com.test.tictactoeapi.exception.RequestException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GameService {


    private Integer boardSize;
    private String[][] dimension;
    public static String winningStatePlayerX;
    public static String winningStatePlayerO;
    public static String currentPlayer="X";
    public static int emptySpace;
    public boolean winnerState=false;
    public GameService() {

    }

    private void initializeBoard(int input,String[][] dimension) {
        for (int i=0;i<input;i++){
            for (int j=0;j<input;j++){
                dimension[i][j]="-";
            }
            winningStatePlayerX=winningStatePlayerX+"X";
            winningStatePlayerO=winningStatePlayerO+"O";
        }
    }

    public String newGame(String size) throws RequestException {
        Pattern pattern = Pattern.compile("[A-Za-z@~`!@#$%^&*()_=+';:?,>.<]");
        Matcher matcher = pattern.matcher(size);
        if (matcher.find())
            throw new RequestException("Please Input Number Only");

        boardSize= Integer.valueOf(size);
        validateBoard();
        dimension = new String[boardSize][boardSize];
        winningStatePlayerO="";
        winningStatePlayerX="";
        emptySpace=boardSize*boardSize;
        winnerState=false;
        currentPlayer="X";
        initializeBoard(boardSize,dimension);
        return printBoard(boardSize,dimension)+"Next Player is "+currentPlayer;
    }

    private  String printBoard(int input,String[][] dimension){
        String result="";
        for (int i = 0; i < input; i++) {
            result=result+"||";
            for (int j = 0; j < input; j++) {
                result=result+dimension[i][j]+" ";

            }
            result=result+"||";
            result=result+"\n";
        }
        return result;
    }


    public String inputTurn(PlayerDto playerDto) throws RequestException {
        validateNumber(playerDto.getCoordinate());

        String[] coordinate=playerDto.getCoordinate().split(",");
        Integer x=Integer.valueOf(coordinate[0]);
        Integer y=Integer.valueOf(coordinate[1]);

        String response="";
        inputValidation(x,y,playerDto);

        response=inputNode(x,y,response);


        String winner=checkWinner(dimension,boardSize);
        if (winner.equals(""))
            response+=printBoard(boardSize,dimension)+"Next Player is "+currentPlayer;
        else{
            response=winner+"\n"+printBoard(boardSize,dimension);
            winnerState=true;
        }
        return response;
    }

    public void validateNumber(String input) throws RequestException {
        Pattern pattern = Pattern.compile("[A-Za-z@~`!@#$%^&*()_=+';:?>.<]");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find())
            throw new RequestException("Please Input the correct Format (x,y). e.g: 2,2 \n"+printBoard(boardSize,dimension));
    }
    private String inputNode(int x,int y,String response){
        if (dimension[x - 1][y - 1].equals("X")||dimension[x - 1][y - 1].equals("O")){
            response+="The node is already filled! \n";
        }else {
            dimension[x - 1][y - 1] = currentPlayer;
            emptySpace--;
            currentPlayer = (currentPlayer == "X") ? "O" : "X";
        }
        return response;
    }

    private void inputValidation(int x,int y,PlayerDto playerDto) throws RequestException {
        if (winnerState)
            throw new RequestException("Winner Decided, Please Create New Game");
        if (x>boardSize || y>boardSize)
            throw new RequestException("Input out of Bound! \n"+printBoard(boardSize,dimension));

        if (!playerDto.getPlayerMark().equals(currentPlayer))
            throw new RequestException("This is not Your Turn! \n"+printBoard(boardSize,dimension));
    }

    public void validateBoard() throws RequestException {
        if (boardSize==null)
            throw new RequestException("Please Initialize The Game first");
        else if(boardSize%2==0 || boardSize<3)
            throw new RequestException("Board must be an odd number and more equals to 3");
    }


    private String checkWinner(String[][] dimension, int size){
        String resultRow="";
        String resultColumn="";
        String resultDiagonal1="";
        String resultDiagonal2="";
        for (int i=0;i<size;i++){
            for (int j=0;j<size;j++){
                if (i==j){
                    resultDiagonal1=resultDiagonal1+dimension[i][j];
                }
                if (i+j==size-1){
                    resultDiagonal2=resultDiagonal2+dimension[i][j];
                }
                resultRow=resultRow+dimension[i][j];
                resultColumn=resultColumn+dimension[j][i];
            }

            if (checkWinnerValue(resultColumn,resultRow,winningStatePlayerX)){
                return "Player X wins";
            } else if(checkWinnerValue(resultColumn,resultRow,winningStatePlayerO)){
                return "Player O wins";
            }
            resultColumn="";
            resultRow="";
        }
        return checkDiagonalAndTie(resultDiagonal1,resultDiagonal2);
    }

    private String checkDiagonalAndTie(String resultDiagonal1,String resultDiagonal2){
        if (checkWinnerValue(resultDiagonal1,resultDiagonal2,winningStatePlayerX)){
            return "Player X wins";
        }
        else if(checkWinnerValue(resultDiagonal1,resultDiagonal2,winningStatePlayerO)){
            return "Player O wins";
        }
        if (emptySpace==0)
            return "Tie! Please Create A New Game";
        return "";
    }

    private boolean checkWinnerValue(String winState1,String winState2,String player){
        if(winState1.equals(player)||winState2.equals(player))
            return true;
        return false;
    }
}
