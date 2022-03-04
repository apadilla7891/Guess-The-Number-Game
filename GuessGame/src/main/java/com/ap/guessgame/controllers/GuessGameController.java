package com.ap.guessgame.controllers;

import com.ap.guessgame.models.Game;
import com.ap.guessgame.models.Round;
import com.ap.guessgame.service.GuessGameServiceLayerImpl;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Andy Padilla
 */
@RestController
@RequestMapping("/api/guessgame")
public class GuessGameController {
    private final GuessGameServiceLayerImpl service;
    
    public GuessGameController(GuessGameServiceLayerImpl service){
        this.service = service;
    }
    
    
    //creates a new game to be played, returns 201 if it works successfully
    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public int begin(){
        return service.begin();
    }
    
    // gets all the games in the database
    @GetMapping("/game")
    public List<Game> allGames(){
        return service.getAllGame();
    }
    
    // uses user input to return the status of a specific game
    @GetMapping("/game/{gameId}")
    public Game getGameById(@PathVariable int gameId){
        return service.findById(gameId);
    }
    
    //takes users guess and returns results of the round
    @PostMapping("/guess")
    public Round guess(@RequestBody Round round){
        return service.guess(round);
    }
    
    //returns all rounds for specificed game
    @GetMapping("/rounds/{gameId}")
    public List<Round> allRounds(@PathVariable int gameId){
        return service.getAllRoundsForGame(gameId);
    }
}
