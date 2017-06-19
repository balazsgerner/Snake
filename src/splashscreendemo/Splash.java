package splashscreendemo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import application.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Splash extends Application {

	private static final boolean SHOW_GRID = false;

	/**
	 * Delay between moving and rotation [ms]
	 */
	private static final int DELAY_BETWEEN_ACTIONS = 500;

	/**
	 * PacMan extent in degrees
	 */
	private static final int PACMAN_EXTENT = 280;

	private final static double PACMAN_SIZE = 40;

	private static final double CANVAS_WIDTH = 600;

	private static final double CANVAS_HEIGHT = 600;

	private static final double CENTERX = CANVAS_WIDTH / 2;

	private static final double CENTERY = CANVAS_HEIGHT / 2;

	private static final double PACMANY = CENTERY - PACMAN_SIZE / 2;

	private final static double CIRCLE_SIZE = PACMAN_SIZE / 6;

	private final static int NUMBER_OF_CIRCLES = 10;

	private static final double GAP = 5;

	private static final double TOTAL_WIDTH = 2 * PACMAN_SIZE + (NUMBER_OF_CIRCLES - 1) * GAP
			+ NUMBER_OF_CIRCLES * CIRCLE_SIZE;

	private static final double OFFSET = -TOTAL_WIDTH / 2;

	private GraphicsContext ctx;

	private PacMan pacman;

	private List<Double> circleXPositions;

	private Timeline timeline;

	@Override
	public void start(Stage primaryStage) {

		// INIT UI
		BorderPane root = new BorderPane();
		Pane container = new Pane();
		Canvas background = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		Canvas elements = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
		container.getChildren().addAll(background, elements);
		root.setCenter(container);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Splash Screen Animation");

		// SET ICON
		final InputStream path = Main.class.getResourceAsStream("/resources/icon.png");
		final Image icon = new Image(path);
		primaryStage.getIcons().add(icon);

		// FILL BG
		final GraphicsContext bgCtx = background.getGraphicsContext2D();
		bgCtx.setFill(Color.BLACK);
		bgCtx.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

		if (SHOW_GRID) {
			bgCtx.setStroke(Color.WHITE);
			// center lines
			bgCtx.strokeLine(CENTERX, 0, CENTERX, CANVAS_HEIGHT);
			bgCtx.strokeLine(0, CENTERY, CANVAS_WIDTH, CENTERY);

			bgCtx.setStroke(Color.MAGENTA);
			// vertical lines
			bgCtx.strokeLine(CENTERX + OFFSET, 0, CENTERX + OFFSET, CANVAS_HEIGHT);
			bgCtx.strokeLine(CENTERX + TOTAL_WIDTH / 2, 0, CENTERX + TOTAL_WIDTH / 2, CANVAS_HEIGHT);

			// horizontal lines
			bgCtx.strokeLine(0, PACMANY, CANVAS_WIDTH, PACMANY);
			bgCtx.strokeLine(0, CENTERY + PACMAN_SIZE / 2, CANVAS_WIDTH, CENTERY + PACMAN_SIZE / 2);

		}
		ctx = elements.getGraphicsContext2D();
		pacman = new PacMan(CENTERX + OFFSET);

		timeline = new Timeline(new KeyFrame(Duration.millis(15), ae -> paintElements()));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		primaryStage.show();

		initCirclePositions();

		// START MOVEMENT
		pacman.startMoving();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void initCirclePositions() {
		circleXPositions = new ArrayList<>();
		for (int i = 0; i < 2 * NUMBER_OF_CIRCLES - 4; i++) {
			circleXPositions.add(CENTERX + OFFSET + i * (CIRCLE_SIZE + GAP) + GAP);
		}
	}

	private void paintElements() {
		ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

		// DRAW CIRCLES
		ctx.setFill(Color.WHITE);
		for (Double xPos : circleXPositions) {
			ctx.fillRect(xPos, CENTERY - CIRCLE_SIZE / 2, CIRCLE_SIZE, CIRCLE_SIZE);
		}

		pacman.paintSelf();
	}

	private class PacMan {

		private static final double INITIAL_ROTATION = 45;

		// PASSES LEFT TO RIGHT ONLY ONCE
		private int numberOfPasses = 0;

		private double rotation;

		private double xPos;

		private boolean positiveDirection = true;

		private Timer moveTimer;

		private boolean rotating;

		public PacMan(double xPos) {
			this.xPos = xPos;
			this.rotation = INITIAL_ROTATION;
		}

		public void startMoving() {
			moveTimer = new MoveTimer();
		}

		private void move() {
			if (positiveDirection) {
				xPos += 2;
				if (!circleXPositions.isEmpty()) {
					Double firstCircleXPos = circleXPositions.get(0);
					if (xPos + PACMAN_SIZE / 2 + CIRCLE_SIZE >= firstCircleXPos) {
						circleXPositions.remove(firstCircleXPos);
					}
				}
				if (xPos >= CENTERX + TOTAL_WIDTH / 2) {
					startRotate(true);
				}
			} else {
				xPos -= 2;
				if (!circleXPositions.isEmpty()) {
					int lastIndex = circleXPositions.size() - 1;
					Double lastCircleXPos = circleXPositions.get(lastIndex);
					if (xPos + PACMAN_SIZE / 2 - CIRCLE_SIZE <= lastCircleXPos + CIRCLE_SIZE) {
						circleXPositions.remove(lastCircleXPos);
					}

				}
				if (xPos <= CENTERX + OFFSET - PACMAN_SIZE) {
					startRotate(false);
				}
			}
		}

		protected void startRotate(boolean positive) {
			moveTimer.cancel();

			Timer timer = new Timer(false);
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					if (rotating != true) {
						rotating = true;
					}

					if (positive) {
						rotation += 2;
						if (rotation == INITIAL_ROTATION + 180) {
							timer.cancel();
							positiveDirection = !positiveDirection;
							moveTimer = new MoveTimer();
							rotating = false;
						}
					} else {
						rotation -= 2;
						if (rotation == INITIAL_ROTATION) {
							timer.cancel();
							positiveDirection = !positiveDirection;
							moveTimer = new MoveTimer();
							rotating = false;
							numberOfPasses++;
							if (numberOfPasses == 1) {
								moveTimer.cancel();
							}
						}
					}
				}
			}, DELAY_BETWEEN_ACTIONS, 20);
		}

		public void paintSelf() {
			// DRAW PACMAN
			ctx.setFill(Color.YELLOW);
			ctx.fillArc(xPos, PACMANY, PACMAN_SIZE, PACMAN_SIZE, rotation, PACMAN_EXTENT, ArcType.ROUND);

			if (!rotating) {
				ctx.setFill(Color.BLACK);
				if (positiveDirection) {
					ctx.fillOval(xPos + PACMAN_SIZE / 2, PACMANY + PACMAN_SIZE / 4 - CIRCLE_SIZE / 2, CIRCLE_SIZE,
							CIRCLE_SIZE);
				} else {
					ctx.fillOval(xPos + PACMAN_SIZE / 2 - CIRCLE_SIZE, PACMANY + PACMAN_SIZE / 4 - CIRCLE_SIZE / 2,
							CIRCLE_SIZE, CIRCLE_SIZE);
				}

				if (numberOfPasses == 1) {
					timeline.stop();
				}

			}
		}

		private class MoveTimer extends Timer {

			public MoveTimer() {
				super(true);
				initCirclePositions();

				scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						move();
					}
				}, (int) (1.5 * DELAY_BETWEEN_ACTIONS), 20);
			}
		}

	}

}
