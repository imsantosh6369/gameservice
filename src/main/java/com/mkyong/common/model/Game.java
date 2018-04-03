package com.mkyong.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class Game {
    HashMap<String, Player> playerHashMap;
    int noOfboards;
    boolean againstComputer;
    CurrentStatus status;
    int gameID;

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

    public void setStatus(CurrentStatus status) {
        this.status = status;
    }

    public CurrentStatus getStatus() {
        return status;
    }

    public int getNoOfboards() {
        return noOfboards;
    }

    public void setNoOfboards(int noOfboards) {
        this.noOfboards = noOfboards;
    }
}

