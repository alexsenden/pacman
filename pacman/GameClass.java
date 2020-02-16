/**
 * Alex Senden
 * AP Computer Science A
 * November 2019
 * Pac-Man Game
 * 
 * Each instance of this class is everything associated with an
 * individual level (when you beat a level, or restart, new instance
 * is created). Also provides all graphical outputs (see paintComponent(Graphics g)).
*/

package pacman;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GameClass extends JPanel{
    
	//the board all other things will base themselves off
    public static GameBoard board;
    
    //background image to be painted
    public static BufferedImage background;
    
    //last direction pacman faced, used when no key is input
    public static String lastF = "R";
    public String imagePath = null;
    
    //used to place images on correct 16x16 tile on screen
    public static int squareHeight = 16;
    public static int squareWidth = squareHeight;
    
    //keeps track of what level you are on
    public static int gamenum = 1;
    
    //pacman object
    public static Pac pacman;
    
    //ghost objects
    public static Ghost orangeGuy;
    public static Ghost redGuy;
    public static Ghost blueGuy;
    public static Ghost pinkGuy;
    
    //multiplier for points when eating ghosts
    public static int ghostPow = 1;
    
    //score, score needed for next free life
    public static int score;
    public static int nlscore = 25000;
    
    //amount of pucks eaten
    public static int eaten;
    
    //time elapsed since pacman ate big puck (big puck allows him to eat the ghosts)
    public static long timerStart;
    
    //keeps track of directioon pacman is facing
    public static boolean left, right, up, down;
    
    //once too much time has elapsed since big puck eaten, set to false
    public static boolean timerActive;
    
    //is spacebar pressed?
    public static boolean spacebar;
    
    //ensures all ghosts move on seperate ticks, otherwise leaves potential for
    //lag from pathfinding
    public static boolean offset = false;
    
    //true if all ghosts are moving at intended speeds
    public static boolean speedsCorrect = true;
    
    public static double speed;
    
    public static Font pixelFont;
    
    //it got mad when i didnt have this, auto generated
    private static final long serialVersionUID = 1L;
    
    //constructor
    public GameClass() {
        board = new GameBoard(0);
        saveBackground();
        score = 0;
        eaten = 0;
        left = right = down = up = spacebar = false;
        for(int y = 0; y < 31; y++) {
            for(int x = 0; x < 28; x++) {
                if(board.getSquareType(y, x) == Type.PLAYER_SPAWN) {
                    pacman = new Pac(y, x);
                }
                else if(board.getSquareType(y, x) == Type.GHOST_SPAWN) {
                    if(board.getBoard()[y][x].getExtraData() == 1) {
                        orangeGuy = new Ghost(y, x, GhostType.ORANGE);
                    }
                    else if(board.getBoard()[y][x].getExtraData() == 2) {
                        redGuy = new Ghost(y, x, GhostType.RED);
                    }
                    else if(board.getBoard()[y][x].getExtraData() == 3) {
                        blueGuy = new Ghost(y, x, GhostType.BLUE);
                    }
                    else if(board.getBoard()[y][x].getExtraData() == 4) {
                        pinkGuy = new Ghost(y, x, GhostType.PINK);
                    }
                }
            }
        }
        Listeners listener = new Listeners();
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(listener);
        
        try {
        	String fs = System.getProperty("file.separator");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, new File(Main.dir + fs + "fonts" + fs + "ARCADECLASSIC.ttf"));
       } catch (Exception e) {
    	   e.printStackTrace();
       }
    }
    
    //once board is read in from file, constructs graphics for it, and 
    //saves it so it does not need to be reconstructed each frame
    public void saveBackground() {
        background = new BufferedImage(Main.WINDOW_WIDTH, Main.WINDOW_WIDTH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = background.createGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        
        for(int y = 0; y < 31; y++) {
            for(int x = 0; x < 28; x++) {
                g.drawImage(imageGetter(imageDeterminer(y, x)), x * squareWidth, y * squareHeight, this);
            }
        }
        try {
            String fs = System.getProperty("file.separator");
            File backgroundImage = new File(Main.dir + "images" + fs + "background.png");
            ImageIO.write(background, "png", backgroundImage);
        } catch (IOException e) {
            System.err.println("could not create background image, terminating program");
            System.exit(1);
        }
    }
    
    //outputs all graphics
    public void paintComponent(Graphics g) {
        BufferedImage frame = bufferFrame();
        g.drawImage(frame, 0, 0, this);
    }
    
    //constructs the graphics, they must be constructed before being output
    //to ensure they are output simultaneously
    public BufferedImage bufferFrame(){
        BufferedImage buffImg = new BufferedImage(Main.WINDOW_WIDTH, Main.WINDOW_WIDTH, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.createGraphics();
        
        FontMetrics pixelMetrics;
        
        g.drawImage(background, 0, 0, this);
        
        if(orangeGuy.getEaten()) {
        	g.drawImage(imageGetter("GHOST_EYES"), (int)(orangeGuy.getXposd() * squareWidth), (int)(orangeGuy.getYposd() * squareHeight), this);
        }
        else if(!orangeGuy.getEdible()){
            g.drawImage(imageGetter("GHOST_ORANGE"), (int)(orangeGuy.getXposd() * squareWidth), (int)(orangeGuy.getYposd() * squareHeight), this);
        }
        else{
        	if(timerStart - System.currentTimeMillis() < -7000 && ((timerStart - System.currentTimeMillis()) / -125) % 2 == 0) {
        		g.drawImage(imageGetter("GHOST_BLINK"), (int)(orangeGuy.getXposd() * squareWidth), (int)(orangeGuy.getYposd() * squareHeight), this);
        	}
        	else {
        		g.drawImage(imageGetter("GHOST_EDIBLE"), (int)(orangeGuy.getXposd() * squareWidth), (int)(orangeGuy.getYposd() * squareHeight), this);
        	}
        }
        if(redGuy.getEaten()) {
        	g.drawImage(imageGetter("GHOST_EYES"), (int)(redGuy.getXposd() * squareWidth), (int)(redGuy.getYposd() * squareHeight), this);
        }
        else if(!redGuy.getEdible()){
            g.drawImage(imageGetter("GHOST_RED"), (int)(redGuy.getXposd() * squareWidth), (int)(redGuy.getYposd() * squareHeight), this);
        }
        else{
        	if(timerStart - System.currentTimeMillis() < -7000 && ((timerStart - System.currentTimeMillis()) / -125) % 2 == 0) {
        		g.drawImage(imageGetter("GHOST_BLINK"), (int)(redGuy.getXposd() * squareWidth), (int)(redGuy.getYposd() * squareHeight), this);
        	}
        	else {
        		g.drawImage(imageGetter("GHOST_EDIBLE"), (int)(redGuy.getXposd() * squareWidth), (int)(redGuy.getYposd() * squareHeight), this);
        	}        
    	}
        if(blueGuy.getEaten()) {
        	g.drawImage(imageGetter("GHOST_EYES"), (int)(blueGuy.getXposd() * squareWidth), (int)(blueGuy.getYposd() * squareHeight), this);
        }
        else if(!blueGuy.getEdible()){
            g.drawImage(imageGetter("GHOST_BLUE"), (int)(blueGuy.getXposd() * squareWidth), (int)(blueGuy.getYposd() * squareHeight), this);
        }
        else{
        	if(timerStart - System.currentTimeMillis() < -7000 && ((timerStart - System.currentTimeMillis()) / -125) % 2 == 0) {
        		g.drawImage(imageGetter("GHOST_BLINK"), (int)(blueGuy.getXposd() * squareWidth), (int)(blueGuy.getYposd() * squareHeight), this);
        	}
        	else {
        		g.drawImage(imageGetter("GHOST_EDIBLE"), (int)(blueGuy.getXposd() * squareWidth), (int)(blueGuy.getYposd() * squareHeight), this);
        	}  
        }
        if(pinkGuy.getEaten()) {
        	g.drawImage(imageGetter("GHOST_EYES"), (int)(pinkGuy.getXposd() * squareWidth), (int)(pinkGuy.getYposd() * squareHeight), this);
        }
        else if(!pinkGuy.getEdible()){
            g.drawImage(imageGetter("GHOST_PINK"), (int)(pinkGuy.getXposd() * squareWidth), (int)(pinkGuy.getYposd() * squareHeight), this);
        }
        else{
        	if(timerStart - System.currentTimeMillis() < -7000 && ((timerStart - System.currentTimeMillis()) / -125) % 2 == 0) {
        		g.drawImage(imageGetter("GHOST_BLINK"), (int)(pinkGuy.getXposd() * squareWidth), (int)(pinkGuy.getYposd() * squareHeight), this);
        	}
        	else {
        		g.drawImage(imageGetter("GHOST_EDIBLE"), (int)(pinkGuy.getXposd() * squareWidth), (int)(pinkGuy.getYposd() * squareHeight), this);
        	}  
        }
        
        g.drawImage(imageGetter(imageDeterminerPac()), (int)(pacman.getXposd() * squareWidth), (int)(pacman.getYposd() * squareHeight), this);
        
        g.setColor(new Color(255, 220, 68, 255));
        
        g.setFont(pixelFont.deriveFont(64f));
        g.drawString("PACMAN", 470, 60);
        
        g.setFont(pixelFont.deriveFont(48f));
        g.drawString("Score", 510, 110);
        
        g.setColor(Color.WHITE);
        g.setFont(pixelFont.deriveFont(32f));
        pixelMetrics = g.getFontMetrics(pixelFont.deriveFont(32f));
        g.drawString(Integer.toString(score), 500 + (150 - (pixelMetrics.stringWidth(Integer.toString(score))))/2, 135);
        
        g.setColor(new Color(255, 220, 68, 255));
        g.drawString("next life", 500, 180);
        
        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(nlscore - score), 500 + (150 - (pixelMetrics.stringWidth(Integer.toString(nlscore - score))))/2, 200);
        
        g.setColor(new Color(255, 220, 68, 225));
        for(int i = 0; i < 5; i++) {
        	g.drawString("High Score " + (i + 1), 480, 250 + 50 * i);
        }
        
        g.setColor(Color.WHITE);
        for(int i = 0; i < 5; i++) {
        	g.drawString(Main.highScores.get(i).toString(), 500 + (150 - (pixelMetrics.stringWidth(Main.highScores.get(i).toString())))/2, 270 + 50 * i);
        }
        
        for(int i = 0; i < Main.lives; i++) {
        	g.drawImage(imageGetter("PAC_LIVES"), i * 64, 496, this);
        }
        
        if(Main.debug) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("SansSerif", Font.PLAIN, 16));
            g.drawString(String.valueOf(Main.cfps) + "FPS", 3, 15);
        }
        
        return buffImg;
    }
    
    //starts timer when big puck is eaten
    public static void startTimer() {
    	timerActive = true;
    	timerStart = System.currentTimeMillis();
    }
    
    //getter for boolean timerActive
    public boolean getTimerActive() {
    	return timerActive;
    }
    
    //checks if timer has run for 10 seconds, if it has, make ghosts inedible,
    //sets intended speed back into regular speed
    public void checkTimer() {
    	if(timerStart - System.currentTimeMillis() < -10000) {
    		timerActive = false;
    		
    		redGuy.setEdible(false);
    		orangeGuy.setEdible(false);
    		pinkGuy.setEdible(false);
    		blueGuy.setEdible(false);
    		
    		redGuy.setIntendedSpeed(1/8.0);
    		orangeGuy.setIntendedSpeed(1/8.0);
    		pinkGuy.setIntendedSpeed(1/8.0);
    		blueGuy.setIntendedSpeed(1/8.0);
    	}
    }
    
    //checks if player has earned a free life
    public void checkExtraLives() {
    	if(score > nlscore) {
    		nlscore += 25000;
    		Main.lives++;
    	}
    }
    
    //sets all variables to constructor defaults
    public void resetSettings() {
    	score = 0;
    	nlscore = 25000;
    	gamenum = 1;
    	lastF = "R";
    	board = new GameBoard(0);
		saveBackground();
		eaten = 0;
		respawnPac();
    }
    
    //outputs "game over" screen
    public void paintGameOver() {
    	Graphics g = this.getGraphics();
    	g.setFont(pixelFont.deriveFont(64f));
    	g.setColor(new Color(255, 220, 68, 255));
    	g.drawString("Game Over!", 65, 250);
    	
    	g.setFont(pixelFont.deriveFont(32f));
    	g.setColor(Color.WHITE);
    	g.drawString("Press Space to Play Again", 35, 275);
    	
    	while(true) {
    		try {
				Thread.sleep(50L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		if(spacebar) {
    			break;
    		}
    	}
    }
    
    //checks for level to be complete
    public void checkCompletion() {
    	if(board.getToEat() <= eaten) {
    		board = new GameBoard(gamenum % 2);
    		gamenum++;
    		saveBackground();
    		eaten = 0;
    		respawnPac();
    	}
    }
    
    //eats puck, adds points, and removes from board
    public static void eatPuck(){
    	eaten++;
        score += 100;
        Graphics g = background.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(pacman.getXposi() * squareWidth, pacman.getYposi() * squareHeight, squareWidth, squareHeight);
    }
    
    //eats big puck, starts timer, slows down ghosts, makes them edible
    public static void eatBigPuck() {
    	ghostPow = 1;
        score += 900;
        eatPuck();
        
        redGuy.setIntendedSpeed(1/16.0);
        orangeGuy.setIntendedSpeed(1/16.0);
        pinkGuy.setIntendedSpeed(1/16.0);
        blueGuy.setIntendedSpeed(1/16.0);
        
        redGuy.setEdible(true);
        orangeGuy.setEdible(true);
        pinkGuy.setEdible(true);
        blueGuy.setEdible(true);
        
        speedsCorrect = false;
        
        startTimer();
    }
    
    //checks if ghosts are moving at intended speed
    public boolean areSpeedsCorrect(){
        if(redGuy.getSpeed() == redGuy.getIntendedSpeed() && blueGuy.getSpeed() == blueGuy.getIntendedSpeed() && orangeGuy.getSpeed() == orangeGuy.getIntendedSpeed() && pinkGuy.getSpeed() == pinkGuy.getIntendedSpeed()) {
        	return true;
        }
        return false;
    }
    
    //sets speeds if ghost is on an integer position and speed is not intended
    public void setSpeeds(){
        if(redGuy.getXposd() == redGuy.getXposi() && redGuy.getYposd() == redGuy.getYposi()){
            redGuy.setSpeed(redGuy.getIntendedSpeed());
        }
        if(orangeGuy.getXposd() == orangeGuy.getXposi() && orangeGuy.getYposd() == orangeGuy.getYposi()){
            orangeGuy.setSpeed(orangeGuy.getIntendedSpeed());
        }
        if(blueGuy.getXposd() == blueGuy.getXposi() && blueGuy.getYposd() == blueGuy.getYposi()){
            blueGuy.setSpeed(blueGuy.getIntendedSpeed());
        }
        if(pinkGuy.getXposd() == pinkGuy.getXposi() && pinkGuy.getYposd() == pinkGuy.getYposi()){
            pinkGuy.setSpeed(pinkGuy.getIntendedSpeed());
        }
    }
    
    //checks if ghost and pacman are in contact
    public boolean pacDead() {
        Ghost[] ghosts = {orangeGuy, redGuy, blueGuy, pinkGuy}; //add ghosts to this array
        for(int i = 0; i < ghosts.length; i++) {
            if(ghosts[i].getXposd() - pacman.getXposd() > -0.3 && (ghosts[i].getXposd() - pacman.getXposd() < 0.3) && ghosts[i].getYposd() - pacman.getYposd() > -0.3 && (ghosts[i].getYposd() - pacman.getYposd() < 0.3)) {
                if(!ghosts[i].getEdible() && !ghosts[i].getEaten())
            		return true;
                else if(ghosts[i].getEdible()){
                	ghosts[i].eaten();
                	score += 100 * Math.pow(2, ghostPow);
                	ghostPow++;
                }
            }
        }
        return false;
    }
    
    //respawns pacman and ghosts if pacman is eaten
    public void respawnPac(){
        pacman = new Pac(pacman.getSpawnY(), pacman.getSpawnX());
        orangeGuy = new Ghost(orangeGuy.getSpawnY(), orangeGuy.getSpawnX(), GhostType.ORANGE);
        redGuy = new Ghost(redGuy.getSpawnY(), redGuy.getSpawnX(), GhostType.RED);
        pinkGuy = new Ghost(pinkGuy.getSpawnY(), pinkGuy.getSpawnX(), GhostType.PINK);
        blueGuy = new Ghost(blueGuy.getSpawnY(), blueGuy.getSpawnX(), GhostType.BLUE);
    }
    
    //moves pacman, ghosts
    public void move(int tick) {
        if(left) {
            if(pacman.moveLeft()) {
                lastF = "L";
            }
            pacman.checkSquare();
        }
        if(right) {
            if(pacman.moveRight()) {
                lastF = "R";
            }
            pacman.checkSquare();
        }
        if(up) {
            if(pacman.moveUp()) {
                lastF = "U";
            }
            pacman.checkSquare();
        }
        if(down) {
            if(pacman.moveDown()) {
                lastF = "D";
            }
            pacman.checkSquare();
        }
        if(offset || tick % 4 == 0) {
            orangeGuy.move();
        }
        if(offset || tick % 4 == 1) {
            redGuy.move();
        }
        if(offset || tick % 4 == 2) {
            blueGuy.move();
        }
        if(offset || tick % 4 == 3){
            pinkGuy.move();
            offset = true;
        }
    }
    
    //determines what image to use for pacman depending on direction and mouth state
    public String imageDeterminerPac() {
        if(pacman.getState() == 2){
            return "PAC_CIRC";
        }
        else {
            return "PAC_" + lastF + "_" + pacman.getState();
        }
    }
    
    //determines what image to use when constructing board graphics
    public String imageDeterminer(int y, int x) {
        Type type = board.getSquareType(y,x);
        switch(type) {
        case FLOOR:
            if(board.getBoard()[y][x].getExtraData() == 1) {
                return "FLOOR_1";
            }
            else if(board.getBoard()[y][x].getExtraData() == 2) {
                return "FLOOR_2";
            }
            else {
                return "FLOOR_0";
            }
        case WALL:
            boolean u, d, r, l, u2, d2;
            u = d = r = l = u2 = d2 = false; //connections
            
            if(y != 0 && board.getBoard()[y-1][x].getType() != Type.WALL) {
                l = r = true;
                u2 = true;
            }
            if(y != 30 && board.getBoard()[y+1][x].getType() != Type.WALL ) {
                l = r = true;
                d2 = true;
            }
            if(x != 0 && board.getBoard()[y][x-1].getType() != Type.WALL) {
                l = false;
                if(!u2) {
                    u = true;
                }
                if(!d2) {
                    d = true;
                }
            }
            if(x != 27 && board.getSquareType(y, x + 1) != Type.WALL) {
                r = false;
                if(!u2) {
                    u = true;
                }
                if(!d2) {
                    d = true;
                }
            }
            
            if(!(l || r || u || d)) {
                if(y != 0 && x != 0 && board.getSquareType(y-1, x-1) != Type.WALL) {
                    u = l = true;
                }
                if(y != 0 && x != 27 && board.getSquareType(y-1, x+1) != Type.WALL) {
                    u = r = true;
                }
                if(y != 30 && x != 0 && board.getSquareType(y+1, x-1) != Type.WALL) {
                    d = l = true;
                }
                if(y != 30 && x != 27 && board.getSquareType(y+1, x+1) != Type.WALL) {
                    d = r = true;
                }
            }
            
            if(l || r || u || d) {
                String returner = "WALL_";
                if(l) {
                    returner += "L";
                }
                if(r) {
                    returner += "R";
                }
                if(u) {
                    returner += "U";
                }
                if(d) {
                    returner += "D";
                }
                return returner;
            }
            else {
                return "FLOOR_0";
            }
        case GHOST_SPAWN:
            return "GHOST_SPAWN";
        case GHOST_DOOR:
            boolean u1, d1, r1, l1;
            u1 = d1 = r1 = l1 = true; //connections
            
            if(y == 0 || (board.getSquareType(y-1, x) != Type.GHOST_DOOR && board.getSquareType(y-1, x) != Type.GHOST_SPAWN_WALL)) {
                u1 = false;
            }
            if(y == 31 || (board.getSquareType(y+1, x) != Type.GHOST_DOOR && board.getSquareType(y+1, x) != Type.GHOST_SPAWN_WALL)) {
                d1 = false;
            }
            if(x == 0 || (board.getSquareType(y, x-1) != Type.GHOST_DOOR && board.getSquareType(y, x-1) != Type.GHOST_SPAWN_WALL)) {
                l1 = false;
            }
            if(x == 30 || (board.getSquareType(y, x+1) != Type.GHOST_DOOR && board.getSquareType(y, x+1) != Type.GHOST_SPAWN_WALL)) {
                r1 = false;
            }
            
            if(l1 || r1 || u1 || d1) {
                String returner = "GHOST_DOOR_";
                if(l1) {
                    returner += "L";
                }
                if(r1) {
                    returner += "R";
                }
                if(u1) {
                    returner += "U";
                }
                if(d1) {
                    returner += "D";
                }
                return returner;
            }
            else {
                return "FLOOR_0";
            }
        case GHOST_SPAWN_WALL:
            boolean u3, d3, r3, l3;
            u3 = d3 = r3 = l3 = true; //connections
            
            if(y == 0 || (board.getSquareType(y-1, x) != Type.GHOST_DOOR && board.getSquareType(y-1, x) != Type.GHOST_SPAWN_WALL)) {
                u3 = false;
            }
            if(y == 30 || (board.getSquareType(y+1, x) != Type.GHOST_DOOR && board.getSquareType(y+1, x) != Type.GHOST_SPAWN_WALL)) {
                d3 = false;
            }
            if(x == 0 || (board.getSquareType(y, x-1) != Type.GHOST_DOOR && board.getSquareType(y, x-1) != Type.GHOST_SPAWN_WALL)) {
                l3 = false;
            }
            if(x == 27 || (board.getSquareType(y, x+1) != Type.GHOST_DOOR && board.getSquareType(y, x+1) != Type.GHOST_SPAWN_WALL)) {
                r3 = false;
            }
            
            if(l3 || r3 || u3 || d3) {
                String returner = "GHOST_WALL_";
                if(l3) {
                    returner += "L";
                }
                if(r3) {
                    returner += "R";
                }
                if(u3) {
                    returner += "U";
                }
                if(d3) {
                    returner += "D";
                }
                return returner;
            }
            else {
                return "FLOOR_0";
            }
        case INVIS_WALL:
            return "FLOOR_0";
        case PLAYER_SPAWN:
            return "FLOOR_0";
        case TP:
            return "FLOOR_0";
        default:
            return "null";
            
            
            
        }
    }
    
    //returns image from file by string name
    public BufferedImage imageGetter(String name) {
        if(imagePath == null) {
            
            String testPath = Main.dir;
            
            String fileSeparator = System.getProperty("file.separator");
            
            if(new File(testPath + "src" + fileSeparator + "images" + fileSeparator + "testImage.png").exists()) {
                imagePath = testPath + "src" + fileSeparator + "images" + fileSeparator;
            }
            else if(new File(testPath + "images" + fileSeparator + "testImage.png").exists()) {
                imagePath = testPath + "images" + fileSeparator;
            }
            else {
                System.out.println("AAAAAAAAHHHHHHHHHHHH"); //testing
            }
        }
        
        BufferedImage image = null;
        
        try {                
              image = ImageIO.read(new File(imagePath + name + ".png"));
           } catch (IOException ex) {
                System.out.println(imagePath + name + ".png");
                System.exit(1);
           }
        return image;
    }
    
    //testing
    public Color colorGetter(int ordinal) {
        switch(ordinal) {
        case 0:
            return Color.DARK_GRAY;
        case 1:
            return Color.BLUE;
        case 2:
            return Color.GREEN;
        case 3:
            return Color.CYAN;
        case 4:
            return Color.YELLOW;
        case 5:
            return Color.RED;
        case 6:
            return Color.ORANGE;
        case 7:
            return Color.MAGENTA;
        }
        return Color.BLACK;
    }
    
    //testing
    public void draw() {
        repaint();
    }
}
