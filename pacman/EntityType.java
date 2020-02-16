package pacman;

/*
 * enum for the type of entity
 * contains the type of square that type can walk on
 */

public enum EntityType {
	PACMAN(0),
	GHOST(1);
	
	private Type[] walkables;
	
	private EntityType(int num) {
		switch(num) {
		case 0:
			walkables = new Type[] {Type.FLOOR, Type.TP, Type.PLAYER_SPAWN, Type.GHOST_SPAWN};
			break;
		case 1:
			walkables = new Type[] {Type.FLOOR, Type.TP, Type.PLAYER_SPAWN, Type.GHOST_SPAWN, Type.GHOST_DOOR};
			break;
		}
	}
	
	public boolean walkablesContains(Type type) {
		for(int i = 0; i < walkables.length; i++) {
			if(walkables[i] == type) {
				return true;
			}
		}
		return false;
	}
	
	public Type[] getWalkables() {
		return walkables;
	}
}
