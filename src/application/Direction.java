package application;

import java.util.concurrent.ThreadLocalRandom;

public enum Direction {
	NORTH,WEST,SOUTH,EAST;
	
	private static final int NUMBER_OF_DIRECTIONS = Direction.values().length;
	
    private Direction opposite;
	
    static {
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
        EAST.opposite = WEST;
        WEST.opposite = EAST;
    }
    
    public Direction getOppositeDirection() {
        return opposite;
    }
    
    public static Direction getRandomDirection(){
		int ordinal = ThreadLocalRandom.current().nextInt(NUMBER_OF_DIRECTIONS);
    	return Direction.values()[ordinal];
    }
}
