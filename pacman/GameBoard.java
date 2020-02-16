/**
 * Alex Senden
 * AP Computer Science A
 * November 2019
 * Pac-Man Game
 * 
 * creates board that will be referenced throughout each level.
 * contains a 2d array of BoardSquares, essentially a grid of tiles
 * that can be interacted with.
*/

package pacman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class GameBoard {
    
    private int toEat = 0;
	
    //board used for all environmental aspects
    private BoardSquare[][] board = new BoardSquare[31][28];
    
    //scanner used for getting board data from file, since boards are stored
    //as strings of numbers in .pacboard files and must be constructed
    //as a side note, the graphics for each board are also dynamically constructed
    //from the information read in here
    private Scanner boardGetter;
    
    public GameBoard(int boardNum) {
        //test file to determine the proper path to the boards folder
        File file = new File("test.txt");
        try {
            file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        
        String path = file.getAbsolutePath();
        String fileSeparator = System.getProperty("file.separator");
    	path = getPath(path, Main.version, 0) + "pacman" + fileSeparator + "src" + fileSeparator;
        Main.dir = path;
        
        
        try {
			boardGetter = new Scanner(new File(path + "boards" + fileSeparator + "board" + boardNum + ".pacboard"));
		} catch (FileNotFoundException e) {
			System.out.println(path + "boards" + fileSeparator + "board" + boardNum + ".pacboard");
			System.err.println("board" + boardNum + ".pacboard was not found, terminating program.");
		}
        
        //vars for tracking 
        int row = 0;
        int column = 0;
        
        //vars to store read-in data before BoardSquare object is initialized
        int type;
        int extraData;
        
        //load .pacboard file into 2d array 
        while(boardGetter.hasNext()) {
            
            type = boardGetter.nextInt();
            
            if(boardGetter.hasNext(":")){
                boardGetter.next();
                extraData = boardGetter.nextInt();
                board[row][column] = new BoardSquare(type, extraData);
                //System.out.print(type + ", " + extraData + " "); //testing, used to output board if issue arise during read-in
            } 
            else {
                board[row][column] = new BoardSquare(type);
                //System.out.print(type + " "); //testing, used to output board if issue arise during read-in
            }
            
            if(board[row][column].getType() == Type.FLOOR && board[row][column].getExtraData() != 0) {
            	toEat++;
            }
            else if(GameClass.redGuy != null && board[row][column].getType() == Type.GHOST_SPAWN) {
            	switch(board[row][column].getExtraData()) {
            	case 1:
            		GameClass.orangeGuy = new Ghost(row, column, GhostType.ORANGE);
            		break;
            	case 2:
            		GameClass.redGuy = new Ghost(row, column, GhostType.RED);
            		break;
            	case 3:
            		GameClass.blueGuy = new Ghost(row, column, GhostType.BLUE);
            		break;
            	case 4: 
            		GameClass.pinkGuy = new Ghost(row, column, GhostType.PINK);
            		break;
            	}
            }
        	
            if(column < 27) {
                column++;
            }
            else {
                column = 0;
                row++;
                //System.out.println(); //testing, used to output board if issue arise during read-in
            }
        }
    }
    
    public String getPath(String path, String name, int index) {
    	//System.out.println(path.indexOf(name, index));
    	if(path.indexOf(name, index) == -1) {
    		return path.substring(0, index + name.length());
    	}
    	else {
    		return getPath(path, name, path.indexOf(name, index) + 1);
    	}
    }
    
    //testing, outputs board without extraData
    public void outputSquare(int row, int column) {
        System.out.print(board[row][column].getType().ordinal());
        if(column == 27)
            System.out.println();
    }
    
    public Type getSquareType(int y, int x) {
    	return board[y][x].getType();
    }
    
    public BoardSquare[][] getBoard() {
    	return board;
    }
    
    public int getToEat() {
    	return toEat;
    }
}
