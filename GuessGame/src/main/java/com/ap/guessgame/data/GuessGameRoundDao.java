package com.ap.guessgame.data;

import com.ap.guessgame.models.Round;
import java.util.List;

/**
 *
 * @author Andy Padilla
 */
public interface GuessGameRoundDao {
    public Round newRound(Round round);
    
    public List<Round> getAllRounds(int id);
    
    public boolean deleteById(int id);
}
