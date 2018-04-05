package com.spatil.common.service;

import com.spatil.common.model.*;
import com.spatil.common.model.Error;
import com.spatil.common.util.Errors;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.log4j.Logger;


public class GameService {

    final static Logger logger = Logger.getLogger(GameService.class);
    private String[][] area;
    private int totalCount = 0;
    private String currentPlayer = null;
    private Game game;

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public GameService() {
    }

    public GameService(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public String[][] getArea() {
        return area;
    }

    public void startGame(Game game) {
        String players = null;
        for (String key : this.getGame().getPlayerHashMap().keySet()) {
            players += this.getGame().getPlayerHashMap().get(key).getName() + " ";
        }
        CurrentStatus currentStatus = this.game.getStatus();
        currentStatus.setStatus(Status.STARTED);
        this.game.setStatus(currentStatus);
        initializeBoard();
        System.out.println("Hello " + players + "!!! Lets start game");
    }

    public synchronized Error validateMove(MyMove move) {
        int x = move.getX();
        int y = move.getY();
        int noOfBoard = this.game.getNoOfboards();
        if (this.getTotalCount() == 0 && !this.getGame().getStatus().getPlayerName().equals(move.getPlayerName())) {
            logger.error(Errors.FIRSTMOVEERROR.getDescription());
            return new Error(Errors.FIRSTMOVEERROR.getDescription(), Errors.FIRSTMOVEERROR.getCode());
        }
        if (this.getCurrentPlayer() != null && this.getCurrentPlayer().equals(move.getPlayerName())) {
            logger.error(Errors.OTHERUSERTURN.getDescription());
            return new Error(Errors.OTHERUSERTURN.getDescription(), Errors.OTHERUSERTURN.getCode());
        } else if (this.game.getStatus().getStatus().equals(Status.WON)) {
            logger.error(Errors.ALREADYWON.getDescription());
            return new Error(Errors.ALREADYWON.getDescription(), Errors.ALREADYWON.getCode());
        } else if (this.game.getStatus().getStatus().equals(Status.DRAWN)) {
            logger.error(Errors.ALREADYDRAWN.getDescription());
            return new Error(Errors.ALREADYDRAWN.getDescription(), Errors.ALREADYDRAWN.getCode());
        } else if (this.area[x][y] != null) {
            logger.error(Errors.ALREADYPRESENT.getDescription());
            return new Error(Errors.ALREADYPRESENT.getDescription(), Errors.ALREADYPRESENT.getCode());
        } else if (!(x < noOfBoard && x >= 0 && y < noOfBoard && y >= 0)) {
            logger.error(Errors.OUTOFBOUNDLOC.getDescription());
            return new Error(Errors.OUTOFBOUNDLOC.getDescription(), Errors.OUTOFBOUNDLOC.getCode());
        } else
            return new Error(Errors.SUCCESS.getDescription(), Errors.SUCCESS.getCode());
    }

    public synchronized Game play(MyMove move) {
        logger.info("Inside play method");

        CurrentStatus currentStatus = this.game.getStatus();
        currentStatus.setStatus(Status.INPROGRESS);
        currentStatus.setPlayerName(move.getPlayerName());
        currentPlayer = currentStatus.getPlayerName();
        this.game.setStatus(currentStatus);

        //computer object
        Player computer = new Player();
        String computerPlaceHolder = this.game.getPlayerHashMap().get(move.getPlayerName()).getPlaceHolder().
                equalsIgnoreCase("$") ? "@" : "$";
        computer.setPlaceHolder(computerPlaceHolder);
        computer.setName("COMPUTER");
        String currentPlayerName = move.getPlayerName();

        int x = move.getX();
        int y = move.getY();
        String placeHolder = this.game.getPlayerHashMap().get(move.getPlayerName()).getPlaceHolder();
        boolean isPlay = true;

        while (isPlay) {
            //process move
            this.area[x][y] = placeHolder;
            this.setTotalCount(this.getTotalCount() + 1);
            //is win
            String val = x + " " + y;
            if (isItWin(val)) {
                logger.info("Game has own by" + currentPlayer);
                currentStatus.setStatus(Status.WON);
                this.game.setStatus(currentStatus);
                return this.game;
            } else if (isItDraw()) {
                logger.info("Game has drawn");
                currentStatus.setStatus(Status.DRAWN);
                this.game.setStatus(currentStatus);
                return this.game;
            }
            if (!this.getCurrentPlayer().equals("COMPUTER") && this.game.isAgainstComputer()) {
                logger.info("Computer started playing");
                currentPlayerName = computer.getName();
                placeHolder = computer.getPlaceHolder();
                currentStatus.setPlayerName(currentPlayerName);
                String attacking = this.getComputerMove(placeHolder);
                String defensive = this.getComputerMove(this.game.getPlayerHashMap().get(move.getPlayerName()).getPlaceHolder());

                String[] attArr = attacking.split(" ");
                String[] defArr = defensive.split(" ");


                if (attacking.equals("") && defensive.equals("") && emptyPlace() == null) {
                    logger.info("Game has either drawn or won!!!");
                    return this.game;
                }
                if (!attacking.equals("")) {
                    x = Integer.parseInt(attArr[0]);
                    y = Integer.parseInt(attArr[1]);
                } else if (!defensive.equals("")) {
                    x = Integer.parseInt(defArr[0]);
                    y = Integer.parseInt(defArr[1]);
                } else {
                    String[] temp = emptyPlace().split(" ");
                    x = Integer.parseInt(temp[0]);
                    y = Integer.parseInt(temp[1]);
                }
            } else {
                isPlay = false;
            }
            this.setCurrentPlayer(currentPlayerName);
        }
        return this.game;
    }

    private void initializeBoard() {
        int size = this.game.getNoOfboards();
        area = new String[size][size];
    }


    private String getComputerMove(String oppPlaceHolder) {

        int counter = 0;
        //horizontal
        int N = this.getGame().getNoOfboards();
        String[][] board = this.getArea();
        String temp = "";
        int nullCount = 0;
        for (int i = 0; i < N; i++) {
            counter = 0;
            nullCount = 0;
            for (int j = 0; j < N; j++) {
                if (board[i][j] != null && board[i][j].equals(oppPlaceHolder)) {
                    counter = counter + 1;
                } else if (board[i][j] == null) {
                    nullCount++;
                    temp = i + " " + j;
                }
            }
            if (counter == N - 1 && nullCount == 1) {
                return temp;
            }
            temp = "";
        }

        temp = "";
        //vertical
        for (int i = 0; i < N; i++) {
            counter = 0;
            nullCount = 0;
            for (int j = 0; j < N; j++) {
                if (board[j][i] != null && board[j][i].equals(oppPlaceHolder)) {
                    counter = counter + 1;
                } else if (board[j][i] == null) {
                    nullCount++;
                    temp = j + " " + i;
                }
            }
            if (counter == N - 1 && nullCount == 1) {
                return temp;
            }
            temp = "";
        }

        temp = "";
        //diagonal from left-top to right-bottom
        counter = 0;
        nullCount = 0;
        for (int i = 0; i < N; i++) {
            if (board[i][i] != null && board[i][i].equals(oppPlaceHolder)) {
                counter = counter + 1;
            } else if (board[i][i] == null) {
                nullCount++;
                temp = i + " " + i;
            }
            //counter = 1;
        }
        if (counter == N - 1 && nullCount == 1) {
            return temp;
        }

        temp = "";
        counter = 0;
        nullCount = 0;
        //diagonal from right-top to left-bottom
        for (int i = 0; i < N; i++) {
            if (board[i][N - 1 - i] != null && board[i][N - 1 - i].equals(oppPlaceHolder)) {
                counter = counter + 1;
            } else if (board[i][N - 1 - i] == null) {
                nullCount++;
                int l = i;
                int m = N - 1 - i;
                temp = l + " " + m;
            }
            //counter = 1;
        }
        if (counter == N - 1 && nullCount == 1) {
            return temp;
        }
        temp = "";
        return temp;
    }

    private String emptyPlace() {
        String[][] board = this.getArea();
        int n = this.getGame().getNoOfboards();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == null)
                    return i + " " + j;
            }
        }
        return null;
    }

    private boolean isItWin(String val) {
        String[] value = val.split(" ");
        int x = Integer.parseInt(value[0]);
        int y = Integer.parseInt(value[1]);

        int counter = 1;
        int N = this.getGame().getNoOfboards();
        String[][] board = this.getArea();

        //horizontal
        int i = x;
        for (int j = 0; j < N - 1; j++) {
            if (board[i][j] != null && board[i][j].equals(board[i][j + 1])) {
                counter = counter + 1;
            }
            if (counter == N) {
                return true;
            }
        }
        counter = 1;

        //vertical
        i = y;
        for (int j = 0; j < N - 1; j++) {
            if (board[j][i] != null && board[j][i].equals(board[j + 1][i])) {
                counter = counter + 1;
            }
            if (counter == N) {
                return true;
            }
        }

        //diagonal from left-top to right-bottom
        counter = 1;
        for (i = 0; i < N - 1; i++) {
            if (board[i][i] != null && board[i][i].equals(board[i + 1][i + 1])) {
                counter = counter + 1;
            }
            if (counter == N) {
                return true;
            }
        }

        //diagonal from right-top to left-bottom
        counter = 1;
        for (i = 0; i < N - 1; i++) {
            if (board[i][N - 1 - i] != null && board[i][N - 1 - i].equals(board[i + 1][N - 1 - (i + 1)])) {
                counter = counter + 1;
            }
            if (counter == N) {
                return true;
            }
        }
        return false;
    }

    private boolean isItDraw() {
        return (this.getTotalCount() == (this.game.getNoOfboards() * this.game.getNoOfboards()));
    }

    @Ignore
    public CurrentStatus getCurrentGameStatus() {
        return game.getStatus();
    }

}
