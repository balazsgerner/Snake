package application.game;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer extends Timer {

  private static final int SNAKE_PROGRESS_SPEED = 250;

  private volatile boolean paused = false;

  public GameTimer() {
    super(true);
  }

  public void start() {
    paused = false;
    scheduleAtFixedRate(new GameTimerTask(), 0, SNAKE_PROGRESS_SPEED);
  }

  public void togglePause() {
    this.paused = !this.paused;
  }

  public boolean isPaused() {
    return paused;
  }

  public class GameTimerTask extends TimerTask {

    private int max = 50;

    private int cycle = 0;

    private Snake snake;

    public GameTimerTask() {
      snake = Manager.getInstance().getSnake();
      init();
    }

    void init() {
      cycle = 0;
      Manager manager = Manager.getInstance();
      manager.deletePoison();
      manager.dropPoison();
    }

    @Override
    public void run() {
      if (paused) {
        return;
      }

      if (cycle++ < max) {
        snake.progress();
      } else {
        init();
      }
    }

  }

}