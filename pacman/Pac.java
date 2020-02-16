package pacman;

/*
 * Class for pacman, contains pacman specific variables,
 * extends entity for position and movement
 */

public class Pac extends Entity{
	
	//mouth opened state
    private int state;
    
    //spawn location
    private int spawnx, spawny;
    
    //is mouth opening or closing
    private boolean opening;
    
    //constructor
    public Pac(int y, int x) {
        super(y, x, EntityType.PACMAN);
        spawnx = x;
        spawny = y;
        state = 0;
    }
    
    //getter for state
    public int getState(){
        return state;
    }
    
    //changes state to next state in sequence
    public void changeState(){
        if(state == 0){
            state = 1;
            opening = true;
        }
        else if(state == 1 && opening){
            state = 2;
        }
        else if(state == 1 && !opening){
            state = 0;
        }
        else if(state == 2){
            state = 1;
            opening = false;
        }
    }
    
    //checks if pacman is on a tile with a puck or big puck
    @Override
    public void checkSquare(){
        if(super.getXposi() == super.getXposd() && super.getYposi() == super.getYposd()){
            super.checkSquare();
            if(GameClass.board.getBoard()[super.getYposi()][super.getXposi()].getType() == Type.FLOOR){
                if(GameClass.board.getBoard()[super.getYposi()][super.getXposi()].getExtraData() == 1){
                    GameClass.board.getBoard()[super.getYposi()][super.getXposi()].setExtraData(0);
                    GameClass.eatPuck();
                }
                if(GameClass.board.getBoard()[super.getYposi()][super.getXposi()].getExtraData() == 2){
                    GameClass.board.getBoard()[super.getYposi()][super.getXposi()].setExtraData(0);
                    GameClass.eatBigPuck();
                }
            }
        }
    }
    
    //movement, if moved, change mouth state
    @Override
    public boolean moveRight() {
    	boolean r = super.moveRight();
    	if(r) {
    		changeState();
    	}
    	return r;
    }
    
    @Override
    public boolean moveLeft() {
    	boolean r = super.moveLeft();
    	if(r) {
    		changeState();
    	}
    	return r;
    }
    
    @Override
    public boolean moveUp() {
    	boolean r = super.moveUp();
    	if(r) {
    		changeState();
    	}
    	return r;
    }
    
    @Override
    public boolean moveDown() {
    	boolean r = super.moveDown();
    	if(r) {
    		changeState();
    	}
    	return r;
    }
    
    
    //getters for spawn location
    public int getSpawnX(){
        return spawnx;
    }
    
    public int getSpawnY() {
        return spawny;
    }
}
