package pacman;

/*
 * Class for ghosts, contains ghost specific variables,
 * extends entity for position and movement
 */

import java.util.ArrayList;

public class Ghost extends Entity {
	
	//booleans for whether the ghost is abe to be eaten, or has been
	//eaten but is yet to respawn
    private boolean edible, eaten;
    
    //type of ghost (colour)
    private GhostType gtype;
    
    //current direction of movement
    private String dir = "r";
    
    //previous direction of movement
    private String lastDir;
    
    //spawn position
    private int spawnx, spawny;
    
    //intended movement speed, speed can only be set on integer positions
    private double intendedSpeed;
    
    //constructor
    public Ghost(int y, int x, GhostType gtype) {
        super(y, x, EntityType.GHOST);
        spawnx = x;
        spawny = y;
        edible = false;
        intendedSpeed = speed;
        this.gtype = gtype;
    }
    
    //chooses a direction to move; 3 different types of movement
    //random, chase, ambush
    public void move() {
        if(gtype == GhostType.ORANGE || gtype == GhostType.BLUE) { //random movement
            if(xposi == xposd && yposi == yposd) {
            	if(eaten) {
            		dir = Node.newPath(yposi, xposi, spawny, spawnx);
            	}
            	else if(!edible){
                    String[] possible = new String[8];
                    int size = 0;
                    if(xposi < 27 && (type.walkablesContains(GameClass.board.getBoard()[yposi][xposi + 1].getType()) || xposd != xposi) && ((yposd == yposi) || (type.walkablesContains(GameClass.board.getBoard()[yposi + 1][xposi + 1].getType())))) {
                        possible[size] = "r";
                        size++;
                    }
                    if(xposi > 0 && (type.walkablesContains(GameClass.board.getBoard()[yposi][xposi - 1].getType()) || xposd != xposi) && ((yposd == yposi) || (type.walkablesContains(GameClass.board.getBoard()[yposi + 1][xposi - 1].getType())))) {
                        possible[size] = "l";
                        size++;
                    }
                    if(yposi > 0 && (type.walkablesContains(GameClass.board.getBoard()[yposi - 1][xposi].getType()) || yposd != yposi) && ((xposd == xposi) || (type.walkablesContains(GameClass.board.getBoard()[yposi + 1][xposi + 1].getType())))) {
                        possible[size] = "u";
                        size++;
                    }
                    if(yposi < 30 && (type.walkablesContains(GameClass.board.getBoard()[yposi + 1][xposi].getType()) || yposd != yposi) && ((xposd == xposi) || (type.walkablesContains(GameClass.board.getBoard()[yposi + 1][xposi + 1].getType())))) {
                        possible[size] = "d";
                        size++;
                    }
                    
                    int count = 0;
                    boolean yed = false;
                    for(int i = 0; i < possible.length; i++) {
                        if(possible[i] != null && possible[i].equals(dir) && !yed) {
                            possible[size] = possible[size+1] = possible[size+2] = possible[size+3] = dir;
                            yed = true;
                        }
                        if(possible[i] != null) {
                            count++;
                        }
                    }
                    dir = possible[(int) (Math.random() * count)];
                }
                else{
                    boolean xpref = false;
                    boolean ypref = false;
                    ArrayList<String> ordMoves = new ArrayList<String>();
                    
                    if(xposi == GameClass.pacman.getXposi()){
                        ordMoves.add(relToPacY());
                        ypref = true;
                    }
                    else if(yposi == GameClass.pacman.getYposi()){
                        ordMoves.add(relToPacX());
                        xpref = true;
                    }
                    
                    if(!ypref){
                        ordMoves.add(relToPacY());
                    }
                    if(!xpref){
                        ordMoves.add(relToPacX());
                    }
                    
                    if(ordMoves.indexOf("l") == -1){
                        ordMoves.add("l");
                    }
                    if(ordMoves.indexOf("r") == -1){
                        ordMoves.add("r");
                    }
                    if(ordMoves.indexOf("u") == -1){
                        ordMoves.add("u");
                    }
                    if(ordMoves.indexOf("d") == -1){
                        ordMoves.add("d");
                    }
                    
                    for(int i = 0; i < 4; i++){
                        if(canMove(ordMoves.get(i))){
                            dir = ordMoves.get(i);
                            break;
                        }
                    }
                }
            }
            
            switch(dir) {
            case "l":
                super.moveLeft();
                break;
            case "r":
                super.moveRight();
                break;
            case "u":
                super.moveUp();
                break;
            case "d":
                super.moveDown();
                break;
            default: 
                super.moveRight();
                break;
            }
            lastDir = dir;
        }
        else if(gtype == GhostType.RED) { //chase movement
            if(xposi == xposd && yposi == yposd) {
            	if(eaten) {
            		dir = Node.newPath(yposi, xposi, spawny, spawnx);
            	}
            	else if(!edible) {
	                int heurdif = Math.abs(xposi - GameClass.pacman.getXposi()) + Math.abs(yposi - GameClass.pacman.getYposi());
	                if(heurdif < 7) {
	                    dir = Node.newPath(yposi, xposi, GameClass.pacman.getYposi(), GameClass.pacman.getXposi());
	                }
	                else {
	                    int[] coords = getlead(GameClass.lastF);
	                    dir = Node.newPath(yposi, xposi, coords[0], coords[1]);
	                    //System.out.println(GameClass.pacman.getXposi() + " " + GameClass.pacman.getYposi() + " " + coords[1] + " " + coords[0]);
	                }
            	}
            	else{
                    boolean xpref = false;
                    boolean ypref = false;
                    ArrayList<String> ordMoves = new ArrayList<String>();
                    
                    if(xposi == GameClass.pacman.getXposi()){
                        ordMoves.add(relToPacY());
                        ypref = true;
                    }
                    else if(yposi == GameClass.pacman.getYposi()){
                        ordMoves.add(relToPacX());
                        xpref = true;
                    }
                    
                    if(!ypref){
                        ordMoves.add(relToPacY());
                    }
                    if(!xpref){
                        ordMoves.add(relToPacX());
                    }
                    
                    if(ordMoves.indexOf("l") == -1){
                        ordMoves.add("l");
                    }
                    if(ordMoves.indexOf("r") == -1){
                        ordMoves.add("r");
                    }
                    if(ordMoves.indexOf("u") == -1){
                        ordMoves.add("u");
                    }
                    if(ordMoves.indexOf("d") == -1){
                        ordMoves.add("d");
                    }
                    
                    for(int i = 0; i < 4; i++){
                        if(canMove(ordMoves.get(i))){
                            dir = ordMoves.get(i);
                            break;
                        }
                    }
                }
            }
            if(dir.equals("last")){
                dir = lastDir;
            }
            switch(dir) {
            case "l":
                super.moveLeft();
                break;
            case "r":
                super.moveRight();
                break;
            case "u":
                super.moveUp();
                break;
            case "d":
                super.moveDown();
                break;
            default: 
                super.moveRight();
                break;
            }
            lastDir = dir;
        }
        else if(gtype == GhostType.PINK) { //ambush movement
            if(xposi == xposd && yposi == yposd) {
            	if(eaten) {
            		dir = Node.newPath(yposi, xposi, spawny, spawnx);
            	}
            	else if(!edible) {
                	dir = Node.newPath(yposi, xposi, GameClass.pacman.getYposi(), GameClass.pacman.getXposi());
            	}
            	else{
                    boolean xpref = false;
                    boolean ypref = false;
                    ArrayList<String> ordMoves = new ArrayList<String>();
                    
                    if(xposi == GameClass.pacman.getXposi()){
                        ordMoves.add(relToPacY());
                        ypref = true;
                    }
                    else if(yposi == GameClass.pacman.getYposi()){
                        ordMoves.add(relToPacX());
                        xpref = true;
                    }
                    
                    if(!ypref){
                        ordMoves.add(relToPacY());
                    }
                    if(!xpref){
                        ordMoves.add(relToPacX());
                    }
                    
                    if(ordMoves.indexOf("l") == -1){
                        ordMoves.add("l");
                    }
                    if(ordMoves.indexOf("r") == -1){
                        ordMoves.add("r");
                    }
                    if(ordMoves.indexOf("u") == -1){
                        ordMoves.add("u");
                    }
                    if(ordMoves.indexOf("d") == -1){
                        ordMoves.add("d");
                    }
                    
                    for(int i = 0; i < 4; i++){
                        if(canMove(ordMoves.get(i))){
                            dir = ordMoves.get(i);
                            break;
                        }
                    }
                }
            }
            if(dir.equals("last")){
                dir = lastDir;
            }
            switch(dir) {
            case "l":
                super.moveLeft();
                break;
            case "r":
                super.moveRight();
                break;
            case "u":
                super.moveUp();
                break;
            case "d":
                super.moveDown();
                break;
            default: 
                super.moveRight();
                break;
            }
            lastDir = dir;
        }
        if(xposi == spawnx && yposi == spawny) {
        	eaten = false;
        	intendedSpeed = 1/8.0;
        }
    }
    
    //used for ambush movement, decided which tile in front of pacman
    //to pathfind to
    public int[] getlead(String lastF) {
        switch(lastF) {
        case "L":
            for(int i = 5; i >= 0; i--) {
                if(!(GameClass.pacman.getXposi() - i < 0) && type.walkablesContains(GameClass.board.getBoard()[GameClass.pacman.getYposi()][GameClass.pacman.getXposi() - i].getType())) {
                    return new int[] {GameClass.pacman.getYposi(), GameClass.pacman.getXposi() - i};
                }
            }
            return getlead("U");
        case "R":
            for(int i = 5; i >= 0; i--) {
                if(!(GameClass.pacman.getXposi() + i > 27) && type.walkablesContains(GameClass.board.getBoard()[GameClass.pacman.getYposi()][GameClass.pacman.getXposi() + i].getType())) {
                    return new int[] {GameClass.pacman.getYposi(), GameClass.pacman.getXposi() + i};
                }
            }
            return getlead("D");
        case "U":
            for(int i = 5; i >= 0; i--) {
                if(!(GameClass.pacman.getYposi() - i < 0) && type.walkablesContains(GameClass.board.getBoard()[GameClass.pacman.getYposi() - i][GameClass.pacman.getXposi()].getType())) {
                    return new int[] {GameClass.pacman.getYposi() - i, GameClass.pacman.getXposi()};
                }
            }
            return getlead("R");
        case "D":
            for(int i = 5; i >= 0; i--) {
                if(!(GameClass.pacman.getYposi() + i > 30) && type.walkablesContains(GameClass.board.getBoard()[GameClass.pacman.getYposi() + i][GameClass.pacman.getXposi()].getType())) {
                    return new int[] {GameClass.pacman.getYposi() + i, GameClass.pacman.getXposi()};
                }
            }
            return getlead("L");
        default:
            return new int[] {1, 1};
        }
    }
    
    //getter and setter for boolean edible
    public boolean getEdible(){
        return edible;
    }
    
    public void setEdible(boolean bool){
        edible = bool;
    }
    
    //determines whether the ghost is left or right of pacman
    public String relToPacX(){
        if(xposi < GameClass.pacman.getXposi()){
            return "l";
        }
        else{
            return "r";
        }
    }
    
    //determines whether the ghost is above or below pacman
    public String relToPacY(){
        if(yposi < GameClass.pacman.getYposi()){
            return "u";
        }
        else{
            return "d";
        }
    }
    
    //returns true if the ghost can move in the input direction
    public boolean canMove(String dir){
        switch(dir){
        case "l":
            if(xposi > 0 && type.walkablesContains(GameClass.board.getBoard()[yposi][xposi - 1].getType())){
                return true;
            }
            return false;
        case "r":
            if(xposi < 27 && type.walkablesContains(GameClass.board.getBoard()[yposi][xposi + 1].getType())){
                return true;
            }
            return false;
        case "u":
            if(yposi > 0 && type.walkablesContains(GameClass.board.getBoard()[yposi - 1][xposi].getType())){
                return true;
            }
            return false;
        case "d":
            if(yposi < 30 && type.walkablesContains(GameClass.board.getBoard()[yposi + 1][xposi].getType())){
                return true;
            }
            return false;
        }
        return false;
    }
    
    //called when ghost is eaten
    public void eaten() {
    	intendedSpeed = 1/4.0;
    	eaten = true;
    	edible = false;
    }
    
    //misc. getters and setters
    public void setIntendedSpeed(double inSpeed) {
    	if(!eaten) {
    		intendedSpeed = inSpeed;
    	}
    }
    
    public double getIntendedSpeed() {
    	return intendedSpeed;
    }
    
    public boolean getEaten() {
    	return eaten;
    }
    
    public int getSpawnX(){
        return spawnx;
    }
    
    public int getSpawnY(){
        return spawny;
    }
}
