package com.ap.guessgame.service;

import com.ap.guessgame.data.GuessGameDatabaseDao;
import com.ap.guessgame.data.GuessGameRoundDatabaseDao;
import com.ap.guessgame.models.Game;
import com.ap.guessgame.models.Round;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Andy Padilla
 */
@Service
public class GuessGameServiceLayerImpl implements GuessGameServiceLayer{
    
    GuessGameDatabaseDao gDao;
    GuessGameRoundDatabaseDao rDao;
    
    @Autowired
    public GuessGameServiceLayerImpl(GuessGameDatabaseDao gDao,GuessGameRoundDatabaseDao rDao){
        this.gDao = gDao;
        this.rDao = rDao;
    }
    
    // when called it makes a new game. Creates a new game object and then calls a helper method to generate a random answer 
    // before uploading to the database and returning the game id to the user so that they may access the game 
    @Override
    public int begin() {
        Game game = new Game();
        String answer = generateAnswer();
        game.setAnswer(answer);
        game = gDao.add(game);
        return game.getGameId();
    }
    
    //when called returns a list containing all the games in the database
    //checks if game is in progress and if so hides answer
    @Override
    public List<Game> getAllGame(){
        List<Game> games = gDao.getAll();
        for(int i = 0; i < games.size();i++){
            if(games.get(i).isGameStatus()== true){
                games.get(i).setAnswer("In Progress");
            }
        }
        return games;
    }
    
    //when called returns the game specified by the id if it exists
    //checks to see if its in progress and if so hides the answer
    @Override
    public Game findById(int id){
        Game gm = gDao.findById(id);
        if(gm.isGameStatus()==true){
            gm.setAnswer("In Progress");
        }
        return gm;
    }
    
    //takes a user guess and makes a round of guessing
    @Override
    public Round guess(Round round){
        // gets game id to get game
        int id = round.getGameId();
        Game gm = gDao.findById(id);
        String trueAnswer = gm.getAnswer();
        String userGuess = round.getGuess();
        // calculates the match between answer and guess
        String result = guessResult(trueAnswer, userGuess);
        round.setResult(result);
        // checks to see if its a perfect match and if so mark game as complete
        if(trueAnswer.equals(userGuess)){
            gm.setGameStatus(false);
            gDao.updateGameStatus(gm);
        }
        
        round = rDao.newRound(round);
        return round;
    }
    
    //Gets all the rounds for the user based on the entered game id
    @Override
    public List<Round> getAllRoundsForGame(int id){
        List<Round> rounds = rDao.getAllRounds(id);
        return rounds;
    }
    
    //generates a 4-digit number with no duplicate digits
    private String generateAnswer(){
        //initialize 
        Random rand = new Random();
        String answer = "";
        
        // set first number to a random int 0-9
        int num1 = rand.nextInt(10);
        
        // set second number to a random int 0-9
        int num2 = rand.nextInt(10);
        //check to see its not the same as first number and if so loop until its not
        while (num2 == num1) {
            num2 = rand.nextInt(10);
        }
        
        // set third number to a random int 0-9
        int num3 = rand.nextInt(10);
        //check to see its not the same as previous numbers and if so loop until its not
        while (num3 == num2 || num3 == num1) {
            num3 = rand.nextInt(10);
        }

        // set fourth number to a random int 0-9
        int num4 = rand.nextInt(10);
        //check to see its not the same as previous numbers and if so loop until its not
        while (num4 == num3 || num4 == num2 || num4 == num1) {
            num4 = rand.nextInt(10);
        }
        
        // adds the generated ints into a string and returns
        answer = String.valueOf(num1) + String.valueOf(num2) + String.valueOf(num3) + String.valueOf(num4);
        
        return answer;
    }
    
    // used to comapre guess to actual answer and return the munber of matches
    @Override
    public String guessResult(String answer, String guess){
        char[] answerSet = answer.toCharArray();
        char[] guessSet = guess.toCharArray();
        int partial = 0;
        int exact = 0;
        String result = "";
        
        //cycles through all the postions in the guess set
        for(int i =0; i <guessSet.length; i++){
            if(answerSet[i] == guessSet[i]){
                    exact++;
                    continue;
                }
            //checks to see if the index in guess set is in answer and if not it 
            //moves onto the next otherwise determines if exact or partial match
            for(int j = 0; j < guessSet.length;j++){    
            if(answerSet[i] == guessSet[j]){
                    partial++;
                    break;
                }
            }
            
        }
        
        result = "e:" + exact + ":p:" + partial;
        return result;
    }
}
