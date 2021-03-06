package com.spatil.common.model;

import java.util.HashMap;

public class Game {
    private HashMap<String, Player> playerHashMap;
    private int noOfboards;
    private boolean againstComputer;
    private CurrentStatus status;
    private int gameID;

    public boolean isAgainstComputer() {
        return againstComputer;
    }

    public void setAgainstComputer(boolean againstComputer) {
        this.againstComputer = againstComputer;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public HashMap<String, Player> getPlayerHashMap() {
        return playerHashMap;
    }

    public void setPlayerHashMap(HashMap<String, Player> playerHashMap) {
        this.playerHashMap = playerHashMap;
    }

    public CurrentStatus getStatus() {
        return status;
    }

    public void setStatus(CurrentStatus status) {
        this.status = status;
    }

    public int getNoOfboards() {
        return noOfboards;
    }

    public void setNoOfboards(int noOfboards) {
        this.noOfboards = noOfboards;
    }
}

