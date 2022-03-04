package com.ap.guessgame.data;

import com.ap.guessgame.TestApplicationConfiguration;
import com.ap.guessgame.models.Game;
import com.ap.guessgame.models.Round;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Andy Padilla
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GuessGameRoundDatabaseDaoTest {
    
    @Autowired
    GuessGameRoundDatabaseDao testRDao;    
    
    @Autowired
    GuessGameDatabaseDao testGDao;
    
    public GuessGameRoundDatabaseDaoTest() {
    }

    /**
     * Test of newRound method, of class GuessGameRoundDatabaseDao.
     */
    @Test
    public void testNewGetAllRound() {
        Game testGame = new Game();
        testGame.setAnswer("0987");
        testGame.setGameStatus(true);
        testGDao.add(testGame);
        
        Round testRound = new Round();
        testRound.setGameId(testGame.getGameId());
        testRound.setGuess("1234");
        testRound.setResult("e:0:p:0");
        testRDao.newRound(testRound);
        
        Round testRound2 = new Round();
        testRound2.setGameId(testGame.getGameId());
        testRound2.setGuess("0987");
        testRound2.setResult("e:4:p:0");
        testRDao.newRound(testRound2);
        
        List<Round> rounds = testRDao.getAllRounds(testGame.getGameId());
        
        assertEquals(2,rounds.size());
    }


}
