package com.spatil.common.controller;

import com.spatil.common.model.CurrentStatus;
import com.spatil.common.model.Error;
import com.spatil.common.service.GameService;
import com.spatil.common.model.Game;
import com.spatil.common.model.MyMove;
import com.spatil.common.util.Errors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Random;

@RestController
public class TicTacToeController {
    final static Logger logger = Logger.getLogger(TicTacToeController.class);
    HashMap<Integer, GameService> gameServiceHashMap = new HashMap<Integer, GameService>();
    Random random = new Random();

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public ResponseEntity<Object> start(@RequestBody Game game) {
        int gameId = random.nextInt(1000);
        game.setGameID(gameId);
        GameService obj = new GameService(game);
        obj.startGame(game);
        if (game.isAgainstComputer() && game.getPlayerHashMap().size() > 1) {
            logger.error(Errors.MOREUSERSAGAINSTCOMPUTER);
            return ResponseEntity.ok(new Error(Errors.MOREUSERSAGAINSTCOMPUTER.toString(), Errors.MOREUSERSAGAINSTCOMPUTER.getCode()));
        }
        gameServiceHashMap.put(gameId, obj);
        logger.info("Game has started, Waiting for player to move!!!!");
        return ResponseEntity.ok(game);
    }

    @RequestMapping(value = "/play", method = RequestMethod.POST)
    public ResponseEntity<Object> play(@RequestHeader("gameID") int gameID, @RequestBody MyMove move) {
        logger.info("Entered play method");
        if (gameServiceHashMap.get(gameID) == null) {
            logger.error(Errors.NOTSTARTED);
            return ResponseEntity.ok(new Error(Errors.NOTSTARTED.toString(), Errors.NOTSTARTED.getCode()));
        }
        Error e = gameServiceHashMap.get(gameID).validateMove(move);
        if (e.getErrorCode() != 000) {
            //validate if user has given wrong
            return ResponseEntity.ok(e);
        }
        gameServiceHashMap.get(gameID).play(move);
        return ResponseEntity.ok(gameServiceHashMap.get(gameID).getGame());
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<CurrentStatus> getStatus(@RequestHeader("gameID") int gameID) {
        logger.info("Entered getStatus method");
        return ResponseEntity.ok(gameServiceHashMap.get(gameID).getCurrentGameStatus());
    }

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public ResponseEntity<String[][]> getBoard(@RequestHeader("gameID") int gameID) {
        logger.info("Entered getBoard method");
        return ResponseEntity.ok(gameServiceHashMap.get(gameID).getArea());
    }
}
