package com.mkyong.common.controller;


import com.mkyong.common.service.GameService;
import com.mkyong.common.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Random;

@RestController
@RequestMapping("/game")
public class TicTacToeController {
    HashMap<Integer, GameService> gameServiceHashMap = new HashMap<Integer, GameService>();
    Random random = new Random();

    @PostMapping
    public ResponseEntity<Game> start(@RequestBody Game game) {
        int gameId = random.nextInt(1000);
        game.setGameID(gameId);
        GameService obj = new GameService(game);
        gameServiceHashMap.put(gameId, obj);
        obj.startGame(game);
        System.out.println("Game has started, Waiting for player to move!!!!");
        return ResponseEntity.ok(game);
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<CurrentStatus> getStatus(@RequestHeader("gameID") int gameID) {
        return ResponseEntity.ok(gameServiceHashMap.get(gameID).getCurrentGameStatus());
    }

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public ResponseEntity<String[][]> getBoard(@RequestHeader("gameID") int gameID) {
        return ResponseEntity.ok(gameServiceHashMap.get(gameID).getArea());
    }

    @RequestMapping(value = "/play", method = RequestMethod.POST)
    public ResponseEntity<Game> play(@RequestHeader("gameID") int gameID, @RequestBody MyMove move) {
        if (gameServiceHashMap.get(gameID) == null) {
            System.out.println("Game has not started");
        }
        if (!gameServiceHashMap.get(gameID).validateMove(move)) {
            //validate if user has given wrong
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
        }
        gameServiceHashMap.get(gameID).play(move);
        return ResponseEntity.ok(gameServiceHashMap.get(gameID).getGame());
    }


/*
    @RequestMapping(value = "computer/play", method = RequestMethod.POST)
    public ResponseEntity<Game> playAgainstComputer(@RequestHeader("gameID") int gameID, @RequestBody MyMove move) {
        if (gameServiceHashMap.get(gameID) == null) {
            System.out.println("Game has not started");
        }
        if (!gameServiceHashMap.get(gameID).validateMove(move)) {
            //validate if user has given wrong
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
        }
        gameServiceHashMap.get(gameID).playWithMe(move);
        return ResponseEntity.ok(gameServiceHashMap.get(gameID).getGame());
    }

    */

}
