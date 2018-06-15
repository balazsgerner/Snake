package application.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Snake {

  public static final int INITIAL_SNAKE_SIZE = 4;

  private int initialSize;

  private List<Field> containedFields;

  private List<Field> growPositions;

  private Direction direction;

  private volatile boolean changedDirecton;

  public Snake() {
    this(INITIAL_SNAKE_SIZE);
  }

  public Snake(int size) {
    this.initialSize = size;
    resetToInitial();
    setStartFields();
  }

  private void setStartFields() {
    Manager manager = Manager.getInstance();
    containedFields.add(manager.getRandomField());
    IntStream.range(1, initialSize).forEach(index -> containedFields.add(manager.getNeighbour(containedFields.get(index - 1), direction.opposite)));
  }

  public void resetToInitial() {
    direction = Direction.randomDirection();
    growPositions = new ArrayList<>();
    changedDirecton = false;
    containedFields = new ArrayList<>();
  }

  public void move(Direction dir) {
    if (dir == this.direction || dir == this.direction.opposite) {
      return;
    }
    if (!changedDirecton) {
      this.direction = dir;
      changedDirecton = true;
    }
  }

  public void progress() {
    Manager manager = Manager.getInstance();
    Field headPosition = containedFields.get(0);
    Field neighbour = manager.getNeighbour(headPosition, this.direction);

    if (containedFields.contains(neighbour)) {
      // --life
      manager.restartGame(true);
    }

    shift(neighbour);
    changedDirecton = false;
  }

  private void shift(Field neighbour) {
    int lastIndex = containedFields.size() - 1;
    for (int i = lastIndex; i > 0; i--) {
      Field prevField = containedFields.get(i - 1);
      containedFields.set(i, prevField);
      Field tailPos = containedFields.get(containedFields.size() - 1);

      if (!growPositions.isEmpty()) {
        if (i == lastIndex && tailPos == growPositions.get(0)) {
          containedFields.add(tailPos);
          growPositions.remove(0);
        }
      }
    }
    containedFields.set(0, neighbour);
    neighbour.enter();
  }

  public void setLocation(List<Field> fields) {
    this.containedFields = fields;
    this.growPositions = new ArrayList<>();
  }

  public List<Field> getContainedFields() {
    return containedFields;
  }

  public void grow() {
    growPositions.add(containedFields.get(0));
  }

  public int getInitialSize() {
    return initialSize;
  }

}
