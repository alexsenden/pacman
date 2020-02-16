/**
 * Alex Senden
 * AP Computer Science A
 * November 2019
 * Pac-Man Game
 * 
 * Main class, starts game and initializes JFrame object
 * Controls the progression of the game
*/
package pacman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

public class Main {
    
	//used to track high scores
	public static ArrayList<Integer> highScores = new ArrayList<Integer>();
	
	//used to help locate the pictures, other files
    public static String version = "pacman_final_1.2";
    public static String dir;
    
    //when true, outputs some extra debugging info
    public static boolean debug = false;
    
    //how many times per second the main game loop runs
    public static final int TPS = 30;
    
    //window size
    public static final int WINDOW_WIDTH = 720;
    public static final int WINDOW_HEIGHT = 600;
    
    //debugging
    public static int cfps = 0;
    
    //track lives for the entire game
    public static int lives = 3;
    
    public static void main(String[] args) {
    	
        boolean gameActive = true;
        
        //initialize gameclass
        GameClass game = new GameClass();
        
        System.setProperty("user.dir", dir);
        
        //create window, sets basic settings about window
        //this block of code was only slightly adapted from
        //https://www.youtube.com/watch?v=_SqnzvJuKiA&t=1187s
        //since all code for creating a window is similar
        //*START ADAPTED CODE*
        JFrame window = new JFrame();
        window.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setTitle("Pac-Man");
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(game);
        window.setVisible(true);
        //*END ADAPTED CODE*
        
        //store the normal output since it will be changed
        PrintStream console = System.out;
        
        //load high scores so they can be displayed on the side
        loadHighScores();
        
        //variables for keeping track of time
        long lastTime = System.nanoTime();
        long ctime;
        
        int ticks = 0;
        
        long lastTimeMillis = System.currentTimeMillis();
        long ctimeMillis;
        while(true) { //lets the game run again after the player has lost all their lives
	        while(gameActive) { //runs until the player is out of lives
	            ctime = System.nanoTime();
	            if(ctime - lastTime > 1000000000.0 / TPS) { //check if enough time has passed to tick the game again
	                lastTime = ctime;
	                if(!game.areSpeedsCorrect()){ //ensure ghost speeds are correct
	                    game.setSpeeds();
	                }
	                if(game.getTimerActive()) { //if the ghosts are edible, check how much longer
	                	game.checkTimer();
	                }
	                //move the ghosts, pacman
	                game.move(ticks); 
	                
	                //check if pacman has earned an extra life
	                game.checkExtraLives();
	                
	                //paint the screen
	                game.paintComponent(game.getGraphics());
	                
	                //if pacman interacts with a live ghost, remove a life
	                if(game.pacDead()){
	                    if(lives > 1){
	                        lives--;
	                        game.respawnPac();
	                    }
	                    else{
	                    	//if out of lives, end the game
	                        gameActive = false;
	                    }
	                }
	                
	                //check if the level has been completed
	                game.checkCompletion();
	                
	                //increment the tick counter
	                ticks++;
	            }
	            
	            ctimeMillis = System.currentTimeMillis();
	            //every second
	            if(ctimeMillis - lastTimeMillis > 1000) {
	                lastTimeMillis = ctimeMillis;
	                
	                //if debug info enables, print out how many times the game 
	                //ticked in the last second
	                if(debug)
	                	System.out.println("FPS: " + ticks);
	                cfps = ticks;
	                
	                //reset the ticks counter for the next second
	                ticks = 0;
	            }
	        }
	        
	        
	        //once the game is over
	        
	        //update the high score boards if it needs it
	        for(int i = 0; i < 5; i++) {
	        	if(highScores.get(i) < GameClass.score) {
	        		highScores.add(i, Integer.valueOf(GameClass.score));
	        		break;
	        	}
	        }
	        
	        //add the high scores to the highScores.txt file
	        try {
				System.setOut(new PrintStream(new File(dir + "highScores.txt")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
	        
	        for(int i = 0; i < 5; i++) {
	        	System.out.println(highScores.get(i));
	        }
	        
	        //return output to the console
	        System.setOut(console);
	        
	        //output "Game Over" on the screen
	        game.paintGameOver();
	        
	        //restart the game
	        gameActive = true;
	        lives = 3;
	        game.resetSettings();
    	}
    }
    
    public static void loadHighScores() { //get high scores from the file, add to the ArrayList
    	try {
			Scanner hs = new Scanner(new File(dir + "highScores.txt"));
			for(int i = 0; i < 5; i++) {
	    		highScores.add(Integer.valueOf(hs.nextInt()));
	    	}
			hs.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
    }
}
