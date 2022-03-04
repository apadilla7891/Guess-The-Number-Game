package com.ap.guessgame.data;

import com.ap.guessgame.models.Game;
import com.ap.guessgame.TestApplicationConfiguration;
import java.util.List;
import org.junit.Before;
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
public class GuessGameDatabaseDaoTest {
    
    @Autowired
    GuessGameDatabaseDao testGDao;
    
    public GuessGameDatabaseDaoTest() {
    }
    
    @Before
    public void setUp(){
        List<Game> games = testGDao.getAll();
        for(Game game: games){
            testGDao.deleteById(game.getGameId());
        }
    }
    /**
     * Test of getAll method, of class GuessGameDatabaseDao. 
     * Done first to work with a fresh DB since no delete method
     */
    @Test
    public void testGetAll() {
        Game testGame = new Game();
        testGame.setAnswer("0987");
        testGame.setGameStatus(true);
        testGDao.add(testGame);
        
        Game testGame2 = new Game();
        testGame2.setAnswer("3456");
        testGame2.setGameStatus(true);
        testGDao.add(testGame2);
        
        List<Game> games = testGDao.getAll();
        
        assertEquals(2,games.size());
        assertTrue(games.contains(testGame));
        assertTrue(games.contains(testGame2));
    }
    
    /**
     * Test of add method, of class GuessGameDatabaseDao.
     */
    @Test
    public void testAddFindById() {
        Game testGame = new Game();
        testGame.setAnswer("0987");
        testGame.setGameStatus(true);
        
        testGame = testGDao.add(testGame);
        Game testFromDao = testGDao.findById(testGame.getGameId());
        
        assertEquals(testGame, testFromDao);
        
    }

        /**
     * Test of updateGameStatus method, of class GuessGameDatabaseDao.
     */
    @Test
    public void testUpdateGameStatus() {
        Game testGame = new Game();
        testGame.setAnswer("0987");
        testGame.setGameStatus(true);
        testGDao.add(testGame);
        
        Game testFromDao = testGDao.findById(testGame.getGameId());
        assertEquals(testGame, testFromDao);
        
        testGame.setGameStatus(false);
        testGDao.updateGameStatus(testGame);
        
        assertNotEquals(testGame, testFromDao);
        
        testFromDao = testGDao.findById(testGame.getGameId());
        assertEquals(testGame, testFromDao);
    }
}
