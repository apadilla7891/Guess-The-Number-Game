package com.ap.guessgame.data;

import com.ap.guessgame.models.Game;
import java.util.List;

/**
 *
 * @author Andy Padilla
 */
public interface GuessGameDao {
    
    Game add(Game game);
    
    List<Game> getAll();
    
    Game findById(int id);
    
    public void updateGameStatus(Game game);
    
    public boolean deleteById(int id);
}
