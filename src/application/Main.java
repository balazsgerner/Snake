package application;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private CanvasPainter canvasPainter;

	private Manager manager;

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			final InputStream path = Main.class.getResourceAsStream("/resources/icon.png");
			primaryStage.getIcons().add(new Image(path));

			// ------ INIT UI
			Pane container = new Pane();
			ObservableList<Node> children = container.getChildren();
			Canvas background = new Canvas(CanvasPainter.CANVAS_WIDTH, CanvasPainter.CANVAS_HEIGHT);
			Canvas elements = new Canvas(CanvasPainter.CANVAS_WIDTH, CanvasPainter.CANVAS_HEIGHT);
			children.addAll(background, elements);
			root.setCenter(container);
			primaryStage.setTitle("SnakeFX");

			// ------ CANVAS PAINTER
			canvasPainter = new CanvasPainter(background, elements);

			// ------ MANAGER
			manager = Manager.getInstance();

			// ------ MAP[WIDTH * HEIGHT]
			List<Field> map = createMap();
			manager.setMap(map);

			// ------ SNAKE[INITIAL_SIZE]
			Snake snake = new Snake(Manager.INITIAL_SNAKE_SIZE);
			manager.setSnake(snake);

			// ------ START
			manager.startGame();

			// ------
			canvasPainter.paintBackground();
			// ------

			// ----- EVENT HANDLING
			addEventHandlers(root, scene);
			
			// ----- REPAINT TIMER
			startTimer();
			// -----

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addEventHandlers(BorderPane root, Scene scene) {

		// ------ HIDE MOUSE CURSOR
		root.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				scene.setCursor(Cursor.NONE);
			}
		});

		// ------ KEYEVENTHANDLER
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode code = event.getCode();

				Direction dir = null;
				switch (code) {
				case UP:
					dir = Direction.NORTH;
					break;
				case DOWN:
					dir = Direction.SOUTH;
					break;
				case RIGHT:
					dir = Direction.EAST;
					break;
				case LEFT:
					dir = Direction.WEST;
					break;

				case P:
					// PAUSE
					manager.togglePause();
					break;
				case R:
					// RESTART
					manager.restartGame();

				default:
					break;
				}

				if (dir != null && !manager.isPaused()) {
					Snake snake = manager.getSnake();
					snake.move(dir);
				}
			}
		});

	}

	private void startTimer() {
		// ~60FPS
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(15), ae -> canvasPainter.paintElements()));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	private List<Field> createMap() {
		List<Field> map = new ArrayList<>(Manager.NUMBER_OF_FIELDS);
		for (int i = 0; i < Manager.NUMBER_OF_FIELDS; i++) {
			map.add(new Field());
		}
		return map;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
