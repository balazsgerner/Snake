package application.game;

import java.util.concurrent.ThreadLocalRandom;

public enum Direction {
  NORTH, WEST, SOUTH, EAST;

  public Direction opposite;

  static {
    NORTH.opposite = SOUTH;
    SOUTH.opposite = NORTH;
    EAST.opposite = WEST;
    WEST.opposite = EAST;
  }

  public static Direction randomDirection() {
    return Direction.values()[ThreadLocalRandom.current().nextInt(Direction.values().length)];
  }
}
