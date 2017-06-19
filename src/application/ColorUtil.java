package application;

import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import javafx.scene.paint.Color;

public class ColorUtil extends TimerTask {

	public static final boolean COLOR_CHANGE = true;

	public static final int COLOR_CHANGE_MS = 200;

	private int iterations;

	private Color snakeColor;

	private enum Colors {
		GREEN, YELLOW, RED, PURPLE, BLUE, CYAN;

		static Colors getRandomColor() {
			int rIndex = ThreadLocalRandom.current().nextInt(Colors.values().length);
			return values()[rIndex];
		}
	}

	private Colors state;

	public ColorUtil() {
		setRandomStartColor();
	}

	@Override
	public void run() {
		changeColors();
	}

	private void changeColors() {

		int ordinal = state.ordinal();
		if (ordinal % 2 == 0) {
			addColor();
		} else {
			removeColor();
		}

	}

	private void setRandomStartColor() {
		state = Colors.getRandomColor();
		if (state.ordinal() % 2 == 0) {
			iterations = 25;
		} else {
			iterations = 0;
		}
		switch (state) {
		case GREEN:
			snakeColor = Color.GREEN;
			break;
		case YELLOW:
			snakeColor = Color.YELLOW;
			break;
		case RED:
			snakeColor = Color.RED;
			break;
		case PURPLE:
			snakeColor = Color.PURPLE;
			break;
		case BLUE:
			snakeColor = Color.BLUE;
			break;
		case CYAN:
			snakeColor = Color.CYAN;
			break;
		}
	}

	private void addColor() {
		if (iterations < 25) {
			double value = (double) iterations / 25;

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

			iterations++;
		} else {
			int ordinal = state.ordinal();
			moveToNextState(ordinal);
		}
	}

	private void removeColor() {
		if (iterations > 0) {
			double value = (double) iterations / 25;

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

			iterations--;
		} else {
			int ordinal = state.ordinal();
			moveToNextState(ordinal);
		}
	}

	private void moveToNextState(int ordinal) {
		int numberOfColors = Colors.values().length;
		int nextColorIndex = (++ordinal) % numberOfColors;

		state = Colors.values()[nextColorIndex];
	}

	public Color getSnakeColor() {
		return snakeColor;
	}
}