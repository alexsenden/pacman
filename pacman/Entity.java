package pacman;

/*
 * Abstract class storing data for any entity on screen.
 * inherited by Ghost and Pac.
 * also controls movement of entity.
 */

public abstract class Entity {
	
	//integer positions on board
    protected int xposi, yposi;
    
    //decimal positions on board
    protected double xposd, yposd;
    
    //type of entity
    protected EntityType type;
    
    //movement speed
    protected double speed = 1/8.0;
    
    //constructor, starting position on board
    public Entity(int y, int x, EntityType type) {
        xposi = x;
        xposd = x;
        yposi = y;
        yposd = y;
        this.type = type;
    }
    
    //move right if able
    public boolean moveRight() {
    	boolean r = false;
        if(xposi < 27 && (type.walkablesContains(GameClass.board.getBoard()[yposi][xposi + 1].getType()) || xposd != xposi) && ((yposd == yposi) || (type.walkablesContains(GameClass.board.getBoard()[yposi + 1][xposi + 1].getType())))) {
            xposd += speed;
            r = true;
        }
        xposi = (int)xposd;
        return r;
    }
    
    //move left if able
    public boolean moveLeft() {
    	boolean r = false;
        if(xposi > 0 && (type.walkablesContains(GameClass.board.getBoard()[yposi][xposi - 1].getType()) || xposd != xposi) && ((yposd == yposi) || (type.walkablesContains(GameClass.board.getBoard()[yposi + 1][xposi - 1].getType())))) {
            xposd -= speed;
            r = true;
        }
        xposi = (int)xposd;
        return r;
    }
    
    //move up if able
    public boolean moveUp() {
    	boolean r = false;
        if(yposi > 0 && (type.walkablesContains(GameClass.board.getBoard()[yposi - 1][xposi].getType()) || yposd != yposi) && ((xposd == xposi) || (type.walkablesContains(GameClass.board.getBoard()[yposi + 1][xposi + 1].getType())))) {
            yposd -= speed;
            r = true;
        }
        yposi = (int)yposd;
        return r;
    }
    
    //move down if able
    public boolean moveDown() {
    	boolean r = false;
        if(yposi < 30 && (type.walkablesContains(GameClass.board.getBoard()[yposi + 1][xposi].getType()) || yposd != yposi) && ((xposd == xposi) || (type.walkablesContains(GameClass.board.getBoard()[yposi + 1][xposi + 1].getType())))) {
            yposd += speed;
            r = true;
        }
        yposi = (int)yposd;
        return r;
    }
    
    //checks if square entity is on has any special properties (ex. teleport)
    public void checkSquare(){
        if(xposi == xposd && yposi == yposd){
            if(GameClass.board.getBoard()[yposi][xposi].getType() == Type.TP){
                BoardSquare[][] checkboard = GameClass.board.getBoard();
                int tpid = checkboard[yposi][xposi].getExtraData();
                outer: for(int y = 0; y < 31; y++){
                    for(int x = 0; x < 28; x++){
                        if(checkboard[y][x].getType() == Type.TP && (x != xposi || y != yposi)){
                            if(checkboard[y][x].getExtraData() == tpid){
                                xposi = x;
                                xposd = x;
                                yposi = y;
                                yposd = y;
                                break outer;
                            }
                        }
                    }
                }
            }
        }
    }
    
    //getters and setters
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    public double getSpeed() {
        return speed;
    }
    
    public int getXposi() {
        return xposi;
    }

    public void setXposi(int xposi) {
        this.xposi = xposi;
    }

    public int getYposi() {
        return yposi;
    }

    public void setYposi(int yposi) {
        this.yposi = yposi;
    }

    public double getXposd() {
        return xposd;
    }

    public void setXposd(double xposd) {
        this.xposd = xposd;
    }

    public double getYposd() {
        return yposd;
    }

    public void setYposd(double yposd) {
        this.yposd = yposd;
    }

}
