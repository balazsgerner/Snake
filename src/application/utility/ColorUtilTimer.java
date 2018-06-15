package application.utility;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.paint.Color;

public class ColorUtilTimer extends Timer {

  private boolean colorChange;

  public int colorChangeMs;

  private Color snakeColor;

  public ColorUtilTimer() {
    super(true);
    ResourceLoader resourceLoader = ResourceLoader.getInstance();
    colorChange = Boolean.valueOf(resourceLoader.loadGameProperty("color_change"));
    colorChangeMs = Integer.parseInt(resourceLoader.loadGameProperty("color_change_ms"));
  }

  private enum Colors {
    GREEN, YELLOW, RED, PURPLE, BLUE, CYAN;

    private Color col;

    private Colors() {
      col = Color.web(name().toLowerCase());
    }

    private static Colors getNextState(Colors state) {
      Colors[] values = values();
      return values[(state.ordinal() + 1) % values.length];
    }

    private static Colors getRandomColor() {
      Colors[] values = values();
      return values[ThreadLocalRandom.current().nextInt(values.length)];
    }

    public Color getColorObj() {
      return col;
    }

  }

  public void start() {
    if (colorChange) {
      scheduleAtFixedRate(new ColorUtilTimerTask(), 0, colorChangeMs);
    } else {
      snakeColor = Colors.getRandomColor().getColorObj();
    }
  }

  public Color getSnakeColor() {
    return snakeColor;
  }

  public class ColorUtilTimerTask extends TimerTask {

    private int iterations;

    private Colors state;

    public ColorUtilTimerTask() {
      state = Colors.getRandomColor();
      snakeColor = state.getColorObj();
      iterations = state.ordinal() % 2 == 0 ? 25 : 0;
    }

    @Override
    public void run() {
      changeColors();
    }

    private void changeColors() {
      if (state.ordinal() % 2 == 0) {
        addColor();
      } else {
        removeColor();
      }
    }

    private void addColor() {
      if (iterations < 25) {
        double value = iterations++ / 25.0;

        switch (state) {
        case GREEN:
          snakeColor = new Color(value, 1, 0, 1);
          break;
        case RED:
          snakeColor = new Color(1, 0, value, 1);
          break;
        case BLUE:
          snakeColor = new Color(0, value, 1, 1);
          break;
        default:
          break;
        }
      } else {
        state = Colors.getNextState(state);
      }
    }

    private void removeColor() {
      if (iterations > 0) {
        double value = iterations-- / 25.0;

        switch (state) {
        case YELLOW:
          snakeColor = new Color(1, value, 0, 1);
          break;
        case PURPLE:
          snakeColor = new Color(value, 0, 1, 1);
          break;
        case CYAN:
          snakeColor = new Color(0, 1, value, 1);
          break;
        default:
          break;
        }
      } else {
        state = Colors.getNextState(state);
      }
    }

  }
}