package com.test.tictactoeapi.controller;


import com.test.tictactoeapi.dto.PlayerDto;
import com.test.tictactoeapi.exception.RequestException;
import com.test.tictactoeapi.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/play")
public class GameController {

    @Autowired
    GameService gameService;

    @RequestMapping(value = "/new-game/{size}",method = RequestMethod.GET)
    public ResponseEntity<String> newGame(@PathVariable("size") String size) {
        String response;
        try {
            response=gameService.newGame(size);
        } catch (RequestException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    @RequestMapping(value = "/move/",method = RequestMethod.POST)
    public ResponseEntity<String> playerMove(@RequestBody PlayerDto playerDto) {
        String response;
        try {
            gameService.validateBoard();
            response=gameService.inputTurn(playerDto);
        } catch (RequestException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

}
