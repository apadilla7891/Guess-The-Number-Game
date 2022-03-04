package com.ap.guessgame.data;

import com.ap.guessgame.models.Game;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Andy Padilla
 */
@Repository
public class GuessGameDatabaseDao implements GuessGameDao{
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GuessGameDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    // adds passed game into the database. We insert and update the answer while using te default value for GameStatus
    // and let the keys be used to set the next game Id
    @Override
    @Transactional
    public Game add(Game game) {
        final String sql = "INSERT INTO game(answer) VALUES(?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql, 
                Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, game.getAnswer());
            return statement;

        }, keyHolder);

        game.setGameId(keyHolder.getKey().intValue());
        
        return game;
    }
    
    // Calls a helper method to make a list containing all games before returning the list
    @Override
    public List<Game> getAll() {
        final String sql = "SELECT GameId, answer, GameStatus FROM game;";
        return jdbcTemplate.query(sql, new GameMapper());
    }
    
    // uses a helper method to get a game specified by the id passed
    @Override
    public Game findById(int id){
        final String sql = "SELECT GameId, answer, GameStatus From game WHERE GameId = ?;";
        return jdbcTemplate.queryForObject(sql, new GameMapper(), id);
    }
    
    //used to update the game status
    @Override
    public void updateGameStatus(Game game){
        final String sql = "UPDATE game SET gameStatus = ? WHERE gameId = ?;";
        jdbcTemplate.update(sql, game.isGameStatus(),game.getGameId());
    }
    
    //helper class that gets the games from the database and puts it in a format to return to user 
    private static final class GameMapper implements RowMapper<Game> {
        
        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game gm = new Game();
            gm.setGameId(rs.getInt("GameId"));
            gm.setGameStatus(rs.getBoolean("GameStatus"));
            gm.setAnswer(rs.getString("answer"));
            return gm;
        }
    }
    
    //just for cleaning test
    @Override 
    public boolean deleteById(int id){
        final String sql = "DELETE FROM game WHERE gameId = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }
}
