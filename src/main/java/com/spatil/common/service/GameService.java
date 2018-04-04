package com.spatil.common.service;

import com.spatil.common.model.*;
import com.spatil.common.model.Error;
import com.spatil.common.util.CrossFinder;
import com.spatil.common.util.Errors;
import org.apache.log4j.Logger;


public class GameService {

    final static Logger logger = Logger.getLogger(GameService.class);
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

    public synchronized Error validateMove(MyMove move) {
        int x = move.getX();
        int y = move.getY();
        int noOfBoard = this.game.getNoOfboards();
        if (currentPlayer != null && currentPlayer.equals(move.getPlayerName())) {
            logger.error(Errors.OTHERUSERTURN.getDescription());
            return new Error(Errors.OTHERUSERTURN.getDescription(), Errors.OTHERUSERTURN.getCode());
        } else if (this.game.getStatus().getStatus().equals(Status.WON)) {
            logger.error(Errors.ALREADYWON.getDescription());
            return new Error(Errors.ALREADYWON.getDescription(), Errors.ALREADYWON.getCode());
        } else if (this.game.getStatus().getStatus().equals(Status.DRAW)) {
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
            this.totalCount++;
            //is win
            if (isItWin()) {
                logger.info("Game has own by"+currentPlayer);
                currentStatus.setStatus(Status.WON);
                this.game.setStatus(currentStatus);
                return this.game;
            } else if (isItDraw()) {
                logger.info("Game has drawn");
                currentStatus.setStatus(Status.DRAW);
                this.game.setStatus(currentStatus);
                return this.game;
            }
            if (!currentPlayer.equals("COMPUTER") && this.game.isAgainstComputer()) {
                logger.info("Computer started playing");
                currentPlayerName = computer.getName();
                placeHolder = computer.getPlaceHolder();
                currentStatus.setPlayerName(currentPlayerName);
                String[] loc = this.getComputerMove(this.area, this.game.getPlayerHashMap().get(move.getPlayerName()).getPlaceHolder(), placeHolder).split(" ");
                if (loc == null) {
                    logger.info("Game has either drawn or won!!!");
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
        logger.info("Generating computer move");
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
