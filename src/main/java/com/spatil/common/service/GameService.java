package com.spatil.common.service;

import com.spatil.common.model.*;
import com.spatil.common.util.CrossFinder;

public class GameService {
    String[][] area;
    int totalCount = 0;
    String currentPlayer = null;
    private Game game;

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

    public synchronized boolean validateMove(MyMove move) {
        int x = move.getX();
        int y = move.getY();
        int noOfBoard = this.game.getNoOfboards();
        if(this.game.getPlayerHashMap().size()>1&&this.game.isAgainstComputer()) {
            System.out.println("play with single user as you are playing against computer");
            return false;
        } else if (currentPlayer != null && currentPlayer.equals(move.getPlayerName())) {
            System.out.println("Its other user turn to play!!");
            return false;
        } else if (this.game.getStatus().getStatus().equals(Status.WON)) {
            System.out.println("Game is already in Won status");
            return false;
        } else if (this.game.getStatus().getStatus().equals(Status.DRAW)) {
            System.out.println("Game is already in DRAW status");
            return false;
        } else if (this.area[x][y] != null)
            return false;
        else if (!(x < noOfBoard && x >= 0 && y < noOfBoard && y >= 0))
            return false;
        else
            return true;
    }
/*
    public synchronized Game playWithMe(MyMove move) {
        CurrentStatus currentStatus = this.game.getStatus();
        currentStatus.setStatus(Status.INPROGRESS);
        currentStatus.setPlayerName(move.getPlayerName());
        this.game.setStatus(currentStatus);
        //process move
        this.area[move.getX()][move.getY()] = this.game.getPlayerHashMap().get(move.getPlayerName()).getPlaceHolder();
        this.totalCount++;
        //is win
        if (isItWin()) {
            currentStatus.setStatus(Status.WON);
            this.game.setStatus(currentStatus);
        } else if (isItDraw()) {
            currentStatus.setStatus(Status.DRAW);
            this.game.setStatus(currentStatus);
        }
        currentPlayer = move.getPlayerName();
        return this.game;
    }
*/

    public synchronized Game play(MyMove move) {
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
            this.totalCount++;
            //is win
            if (isItWin()) {
                currentStatus.setStatus(Status.WON);
                this.game.setStatus(currentStatus);
                return this.game;
            } else if (isItDraw()) {
                currentStatus.setStatus(Status.DRAW);
                this.game.setStatus(currentStatus);
                return this.game;
            }
            if (!currentPlayer.equals("COMPUTER") && this.game.isAgainstComputer()) {
                currentPlayerName = computer.getName();
                placeHolder = computer.getPlaceHolder();
                currentStatus.setPlayerName(currentPlayerName);
                String[] loc = this.getComputerMove(this.area, this.game.getPlayerHashMap().get(move.getPlayerName()).getPlaceHolder(), placeHolder).split(" ");
                if (loc == null) {
                    System.out.println("Game has either draw or won!!!");
                    return this.game;
                }
                x = Integer.parseInt(loc[0]);
                y = Integer.parseInt(loc[1]);
            } else {
                isPlay = false;
            }
            currentPlayer = currentPlayerName;
        }

        return this.game;
    }

    private void initializeBoard() {
        int size = this.game.getNoOfboards();
        area = new String[size][size];

    }

    private boolean isItWin() {
        return CrossFinder.rowCrossed(area, this.game.getNoOfboards()) || CrossFinder.columnCrossed(area, this.game.getNoOfboards())
                || CrossFinder.diagonalCrossed(area, this.game.getNoOfboards());
    }

    private boolean isItDraw() {
        return (this.totalCount == (this.game.getNoOfboards() * this.game.getNoOfboards()));
    }

    public CurrentStatus getCurrentGameStatus() {
        return game.getStatus();
    }

    private String getComputerMove(String[][] area, String oppPlaceHolder, String computerPlaceHolder) {

        String attackRowPlace = CrossFinder.rowPlaceToInsert(area, computerPlaceHolder, this.game.getNoOfboards());
        String attackColumnPlace = CrossFinder.columnPlaceToInsert(area, computerPlaceHolder, this.game.getNoOfboards());
        String attackDiagonalPlace = CrossFinder.diagonalPlaceToInsert(area, computerPlaceHolder, this.game.getNoOfboards());

        String defenceRowPlace = CrossFinder.rowPlaceToInsert(area, oppPlaceHolder, this.game.getNoOfboards());
        String defenceColumnPlace = CrossFinder.columnPlaceToInsert(area, oppPlaceHolder, this.game.getNoOfboards());
        String defenceDiagonalPlace = CrossFinder.diagonalPlaceToInsert(area, oppPlaceHolder, this.game.getNoOfboards());

        if (attackRowPlace != null)
            return attackRowPlace;
        else if (attackColumnPlace != null)
            return attackColumnPlace;
        else if (attackDiagonalPlace != null)
            return attackDiagonalPlace;
        else if (defenceRowPlace != null)
            return defenceRowPlace;
        else if (defenceColumnPlace != null)
            return defenceColumnPlace;
        else if (defenceDiagonalPlace != null)
            return defenceDiagonalPlace;
        else
            return CrossFinder.getEmptyPlace(area, this.game.getNoOfboards());

    }

}
