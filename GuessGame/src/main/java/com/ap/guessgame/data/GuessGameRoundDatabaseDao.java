package com.ap.guessgame.data;

import com.ap.guessgame.models.Round;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
public class GuessGameRoundDatabaseDao implements GuessGameRoundDao{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GuessGameRoundDatabaseDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    //adds new round to database based on guess and game id entered by user, inserts the values
    // and generates the round id based on keys
    @Override
    @Transactional
    public Round newRound(Round round){
        final String sql = "INSERT INTO round(gameId, guess, result) VALUES(?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql, 
                Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, round.getGameId());
            statement.setString(2, round.getGuess());
            statement.setString(3, round.getResult());
            return statement;

        }, keyHolder);

        round.setRoundId(keyHolder.getKey().intValue());
        int roundId = round.getRoundId();
        
        // call helper to prep to return to user
        final String sql2 = "SELECT * FROM round WHERE roundId = ?";
        return jdbcTemplate.queryForObject(sql2, new RoundMapper(), roundId);
    }
    
    // gets all the rounds ordered by time for a specified game and returns to user
    @Override
    public List<Round> getAllRounds(int id){
        final String sql = "SELECT * FROM round WHERE GameId = ? ORDER BY time;";
        return jdbcTemplate.query(sql, new RoundMapper(), id);
    }
    
    //helper class that gets rounds from the database and put it in a format to return to user, also updates the time to a proper time
    public static final class RoundMapper implements RowMapper<Round> {
        
        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setRoundId(rs.getInt("roundId"));
            round.setGameId(rs.getInt("gameId"));
            round.setGuess(rs.getString("guess"));
            
            Timestamp timestamp = rs.getTimestamp("time");
            round.setTime(timestamp.toLocalDateTime());
            
            round.setResult(rs.getString("result"));
            return round;
        }
    }
    
    //just for cleaning test
    @Override 
    public boolean deleteById(int id){
        final String sql = "DELETE FROM round WHERE roundId = ?;";
        return jdbcTemplate.update(sql, id) > 0;
    }
}
