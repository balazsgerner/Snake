package application.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import application.game.collectable.Fruit;
import application.game.collectable.Poison;
import application.utility.ResourceLoader;
import javafx.geometry.Point2D;

public class Manager {

  private int canvasWidth;

  private int canvasHeight;

  private int mapWidth;

  private int mapHeight;

  private static Manager instance;

  private Snake snake;

  private GameTimer gameTimer;

  private List<Field> map;

  private List<Field> poisonContainers;

  private Field foodContainer;

  private Manager() {
    ResourceLoader resourceLoader = ResourceLoader.getInstance();
    canvasWidth = Integer.parseInt(resourceLoader.loadGameProperty("canvas_width"));
    canvasHeight = Integer.parseInt(resourceLoader.loadGameProperty("canvas_width"));
    mapWidth = Integer.parseInt(resourceLoader.loadGameProperty("map_width"));
    mapHeight = Integer.parseInt(resourceLoader.loadGameProperty("map_height"));
  }

  public static Manager getInstance() {
    if (instance == null) {
      instance = new Manager();
    }
    return instance;
  }

  public void dropFood() {
    foodContainer = getEmptyField();
    foodContainer.addCollectable(new Fruit());
  }

  private Field getEmptyField() {
    Field randomField = getRandomField();
    boolean notEmpty = checkNotEmpty(randomField);
    while (notEmpty) {
      randomField = getRandomField();
      notEmpty = checkNotEmpty(randomField);
    }
    return randomField;
  }

  private boolean checkNotEmpty(Field field) {
    return snake.getContainedFields().contains(field) || poisonContainers.contains(field) || field.equals(foodContainer);
  }

  public void deletePoison() {
    poisonContainers.remove(0).removeCollectable();
  }

  public void dropPoison() {
    Field randomField = getEmptyField();
    randomField.addCollectable(new Poison());
    poisonContainers.add(randomField);
  }

  public Field getRandomField() {
    return map.get(ThreadLocalRandom.current().nextInt(getNumberOfFields()));
  }

  public void restartGame(boolean delay) {
    gameTimer.cancel();
    if (delay) {
      scheduleRestart(2000);
    } else {
      startGame();
    }
  }

  private void scheduleRestart(int delay) {
    new Timer(true).schedule(new TimerTask() {

      @Override
      public void run() {
        startGame();
      }
    }, delay);
  }

  public Field getNeighbour(Field f, Direction dir) {
    int index = map.indexOf(f);
    int newIndex = -1;
    switch (dir) {
    case NORTH:
      newIndex = index / mapWidth == 0 ? index + getNumberOfFields() - mapWidth : index - mapWidth;
      break;
    case WEST:
      newIndex = index % mapHeight == 0 ? index + mapWidth - 1 : index - 1;
      break;
    case EAST:
      newIndex = index % mapWidth == mapWidth - 1 ? index - mapWidth + 1 : index + 1;
      break;
    case SOUTH:
      newIndex = index / mapHeight == mapWidth - 1 ? index - getNumberOfFields() + mapWidth : index + mapWidth;
      break;
    }

    return map.get(newIndex);
  }

  public void startGame() {
    map = IntStream.range(0, getNumberOfFields()).mapToObj(index -> new Field(index)).collect(Collectors.toList());
    poisonContainers = new ArrayList<>();
    snake = new Snake();
    foodContainer = null;
    dropFood();
    dropPoison();
    startSnakeTimer();
  }

  private void startSnakeTimer() {
    gameTimer = new GameTimer();
    gameTimer.start();
  }

  public void togglePause() {
    gameTimer.togglePause();
  }

  public Snake getSnake() {
    return snake;
  }

  public Field getFoodContainer() {
    return foodContainer;
  }

  public List<Field> getPoisonContainer() {
    return poisonContainers;
  }

  public boolean isPaused() {
    return gameTimer.isPaused();
  }

  public int getCanvasWidth() {
    return canvasWidth;
  }

  public int getCanvasHeight() {
    return canvasHeight;
  }

  public int getGridWidth() {
    return canvasWidth / mapWidth;
  }

  public int getGridHeight() {
    return canvasHeight / mapHeight;
  }

  public int getElementWidth() {
    return getGridWidth() - getGridHeight() / 3;
  }

  public int getElementHeight() {
    return getGridHeight() - getGridWidth() / 3;
  }

  public int getMapWidth() {
    return mapWidth;
  }

  public int getMapHeight() {
    return mapHeight;
  }

  public int getNumberOfFields() {
    return mapWidth * mapHeight;
  }

  public Point2D getTopLeftCornerPosByIndex(int index) {
    int rowNum = index / mapHeight;
    int colNum = index % mapWidth;
    return new Point2D(colNum * getGridWidth() + getElementWidth() / 4.0, rowNum * getGridHeight() + getElementHeight() / 4.0);
  }

}
