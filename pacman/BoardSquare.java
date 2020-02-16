/**
 * Alex Senden
 * AP Computer Science A
 * November 2019
 * Pac-Man Game
 * 
 * Class for individual squares on the board 
 * stores type and any extra data
 * if no extra data exists, it remains null
*/

package pacman;

public class BoardSquare {
	
	private Type type;
	private int extraData;
	
	//constructor for no extraData
	public BoardSquare(int typeOrdinal) {
		switch(typeOrdinal) {
		case 0:
			type = Type.FLOOR;
			extraData = 1;
			break;
		case 1:
			type = Type.WALL;
			break;
		case 2:
			type = Type.GHOST_SPAWN;
			break;
		case 3:
			type = Type.GHOST_DOOR;
			break;
		case 4:
			type = Type.PLAYER_SPAWN;
			break;
		case 5:
			type = Type.TP;
			break;
		case 6:
			type = Type.INVIS_WALL;
			break;
		case 7:
			type = Type.GHOST_SPAWN_WALL;
			break;
		}
	}
	
	//constructor for extraData
	public BoardSquare(int typeOrdinal, int extraData) {
		switch(typeOrdinal) {
		case 0:
			type = Type.FLOOR;
			break;
		case 1:
			type = Type.WALL;
			break;
		case 2:
			type = Type.GHOST_SPAWN;
			break;
		case 3:
			type = Type.GHOST_DOOR;
			break;
		case 4:
			type = Type.PLAYER_SPAWN;
			break;
		case 5:
			type = Type.TP;
			break;
		case 6:
			type = Type.INVIS_WALL;
			break;
		case 7:
			type = Type.GHOST_SPAWN_WALL;
			break;
		}
		
		this.extraData = extraData;
	}
	
	
	//getters
	public Type getType() {
		return type;
	}
	
	public int getExtraData() {
		return extraData;
	}
	
	//setters
	public void setExtraData(int data) {
		extraData = data;
	}
	
}
