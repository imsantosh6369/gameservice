package com.spatil.common.controller;

import com.spatil.common.couchbase.AdapterImpl;
import com.spatil.common.model.*;
import com.spatil.common.model.Error;
import com.spatil.common.service.GameService;
import com.spatil.common.util.Errors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.log4j.Logger;

import java.util.Random;

@RestController
public class TicTacToeController {
    final static Logger logger = Logger.getLogger(TicTacToeController.class);
    Random random = new Random();
    AdapterImpl adapterImpl = new AdapterImpl();

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
        adapterImpl.set(gameId + "", obj, 20);
        logger.info("Game has started, Waiting for player to move!!!!");
        return ResponseEntity.ok(game);
    }

    @RequestMapping(value = "/play", method = RequestMethod.POST)
    public ResponseEntity<Object> play(@RequestHeader("gameID") int gameID, @RequestBody MyMove move) {
        logger.info("Entered play method");
        GameService gameService = adapterImpl.get(gameID + "");
        if (adapterImpl.get(gameID + "") == null) {
            logger.error(Errors.NOTSTARTED);
            return ResponseEntity.ok(new Error(Errors.NOTSTARTED.toString(), Errors.NOTSTARTED.getCode()));
        }
        Error e = adapterImpl.get(gameID + "").validateMove(move);
        if (e.getErrorCode() != 000) {
            //validate if user has given wrong
            return ResponseEntity.ok(e);
        }
        gameService.play(move);
        if (gameService.getGame().getStatus().getStatus().equals(Status.DRAWN) ||
                gameService.getGame().getStatus().getStatus().equals(Status.WON)) {
            adapterImpl.set(gameID + "", gameService, 1);
        } else {
            adapterImpl.set(gameID + "", gameService, 20);
        }
        return ResponseEntity.ok(gameService.getGame());
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public ResponseEntity<CurrentStatus> getStatus(@RequestHeader("gameID") int gameID) {
        logger.info("Entered getStatus method");
        return ResponseEntity.ok(adapterImpl.get(gameID + "").getCurrentGameStatus());
    }

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public ResponseEntity<String[][]> getBoard(@RequestHeader("gameID") int gameID) {
        logger.info("Entered getBoard method");
        return ResponseEntity.ok(adapterImpl.get(gameID + "").getArea());
    }
}
