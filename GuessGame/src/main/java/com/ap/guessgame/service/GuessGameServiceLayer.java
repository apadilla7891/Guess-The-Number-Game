package com.ap.guessgame.service;

import com.ap.guessgame.models.Game;
import com.ap.guessgame.models.Round;
import java.util.List;

/**
 *
 * @author Andy Padilla
 */
public interface GuessGameServiceLayer {
    public int begin();
    
    public List<Game> getAllGame();
    
    public Game findById(int id);
    
    public Round guess(Round round);
    
    public List<Round> getAllRoundsForGame(int id);
    
    public String guessResult(String answer, String guess);
}
