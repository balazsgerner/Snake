package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import application.collectable.Fruit;
import application.collectable.Poison;

/**
 * Manager Objektum (Singleton), tartalmazza a pálya mezõit és a kígyót A kígyón mûveletet õ tud végezni
 *
 */
public class Manager {

  public static final int MAP_WIDTH = 20;

  public static final int MAP_HEIGHT = 20;

  public static final int NUMBER_OF_FIELDS = MAP_WIDTH * MAP_HEIGHT;

  /**
   * SNAKE PROGRESS SPEED
   */
  public static final int INITIAL_SNAKE_SIZE = 4;

  static final int SNAKE_PROGRESS_SPEED = 250;

  static final int MIN_POISON = 3;

  static final int MAX_POISON = 8;

  static final int POISON_REFRESH_MIN_LEVEL = 2;

  static final int POISON_REFRESH_MAX_LEVEL = 5;

  static Manager instance = null;

  Snake snake;

  List<Field> map;

  Field foodContainer;

  List<Field> poisonContainers;

  Timer gameTimer;

  int oldPoisonNum = 0;

  int poisonNum;

  int poisonRefreshLevel;

  boolean paused = false;

  protected Manager() {
  }

  public static Manager getInstance() {
    if (instance == null) {
      instance = new Manager();
    }
    return instance;
  }

  public void setSnake(Snake snake) {
    this.snake = snake;
    snake.setManager();
  }

  void setRandomStartPosition() {
    Direction startDirection = Direction.getRandomDirection();
    Direction oppositeDirection = startDirection.getOppositeDirection();
    snake.resetToInitial(startDirection);

    List<Field> fields = new ArrayList<>();
    Field randomField = getRandomField();
    fields.add(randomField);

    for (int i = 1; i < snake.getInitialSize(); i++) {
      Field prevField = fields.get(i - 1);
      Field nextField = getNeighbour(prevField, oppositeDirection);

      fields.add(nextField);
    }

    snake.setLocation(fields);
  }

  public void setMap(List<Field> map) {
    this.map = map;
    for (Field f : map) {
      int index = map.indexOf(f);
      f.setIndex(index);
    }
  }

  public void dropFood() {
    foodContainer = getEmptyField();

    Fruit food = new Fruit();
    foodContainer.addCollectable(food);
  }

  Field getEmptyField() {
    Field randomField = getRandomField();

    boolean notEmpty = checkNotEmpty(randomField);
    while (notEmpty) {
      randomField = getRandomField();

      notEmpty = checkNotEmpty(randomField);
    }
    return randomField;
  }

  boolean checkNotEmpty(Field randomField) {
    List<Field> fields = snake.getContainedFields();
    boolean notEmpty = fields.contains(randomField) || poisonContainers != null && poisonContainers.contains(randomField)
        || randomField == foodContainer;
    return notEmpty;
  }

  void generateNewPoisonNum() {
    // get a new posionNumValue that differs from the previous
    poisonNum = ThreadLocalRandom.current().nextInt(MIN_POISON, MAX_POISON + 1);
    while (poisonNum == oldPoisonNum) {
      poisonNum = ThreadLocalRandom.current().nextInt(MIN_POISON, MAX_POISON + 1);
    }
  }

  public void deletePoison() {
    while (oldPoisonNum > poisonNum) {
      // delete random poison from map
      Field del = poisonContainers.remove(ThreadLocalRandom.current().nextInt(poisonContainers.size()));
      del.removeCollectable();
      oldPoisonNum--;
    }
  }

  public void dropPoison() {
    while (oldPoisonNum < poisonNum) {
      Field randomField = getEmptyField();
      randomField.addCollectable(new Poison());
      poisonContainers.add(randomField);
      oldPoisonNum++;
    }
  }

  public void refreshPoisonRates() {
    poisonRefreshLevel = ThreadLocalRandom.current().nextInt(POISON_REFRESH_MIN_LEVEL, POISON_REFRESH_MAX_LEVEL + 1);
  }

  Field getRandomField() {
    int randomIndex = ThreadLocalRandom.current().nextInt(NUMBER_OF_FIELDS);
    Field field = map.get(randomIndex);
    return field;
  }

  public void growSnake() {
    snake.grow();
  }

  public void killSnake() {
    gameTimer.cancel();

    // SCHEDULE RESTART
    Timer timer = new Timer(true);
    timer.schedule(new TimerTask() {

      @Override
      public void run() {
        startGame();
      }
    }, 2000);
  }

  public Field getNeighbour(Field f, Direction dir) {
    int index = map.indexOf(f);
    int newIndex = -1;
    switch (dir) {
    case NORTH:
      if (index / MAP_WIDTH == 0) {
        newIndex = index + NUMBER_OF_FIELDS - MAP_WIDTH;
      } else {
        newIndex = index - MAP_WIDTH;
      }
      break;
    case WEST:
      if (index % MAP_HEIGHT == 0) {
        newIndex = index + MAP_WIDTH - 1;
      } else {
        newIndex = index - 1;
      }
      break;
    case EAST:
      if (index % MAP_WIDTH == MAP_WIDTH - 1) {
        newIndex = index - MAP_WIDTH + 1;
      } else {
        newIndex = index + 1;
      }
      break;
    case SOUTH:
      if (index / MAP_HEIGHT == MAP_WIDTH - 1) {
        newIndex = index - NUMBER_OF_FIELDS + MAP_WIDTH;
      } else {
        newIndex = index + MAP_WIDTH;
      }
      break;
    }

    return map.get(newIndex);
  }

  public void startGame() {
    // CLEAR MAP
    if (poisonContainers != null && !poisonContainers.isEmpty()) {
      for (Field f : poisonContainers) {
        f.removeCollectable();
      }
    }
    if (foodContainer != null) {
      foodContainer.removeCollectable();
    }

    poisonContainers = new ArrayList<>();

    // SET RANDOM START POS FOR SNAKE
    setRandomStartPosition();

    // DROP FOOD
    dropFood();

    // DROP POISON
    dropPoison();

    // TIMER FOR SNAKE PROGRESS
    startSnakeTimer();
  }

  void startSnakeTimer() {
    gameTimer = new Timer(true);
    GameTimer timerTask = new GameTimer();
    gameTimer.scheduleAtFixedRate(timerTask, 0, SNAKE_PROGRESS_SPEED);
  }

  public void togglePause() {
    this.paused = !this.paused;
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

  public void restartGame() {
    gameTimer.cancel();
    startGame();
  }

  class GameTimer extends TimerTask {

    int max;

    int i = 0;

    public GameTimer() {
      restartWithNewMaximum();
    }

    void restartWithNewMaximum() {
      // No delete needed initially
      generateNewPoisonNum();
      deletePoison();
      dropPoison();
      refreshPoisonRates();
      
      max = poisonRefreshLevel * 20; 
    }

    @Override
    public void run() {
      if (!paused) {
        snake.progress();

        i++;
        if (i == max) {
          restartWithNewMaximum();
          i = 0;
        }
      }
    }
  }

  public boolean isPaused() {
    return paused;
  }

}
