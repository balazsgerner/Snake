package application.main;

import application.game.Direction;
import application.game.Manager;
import application.utility.CanvasPainter;
import application.utility.ResourceLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

  private CanvasPainter canvasPainter;

  @Override
  public void start(Stage primaryStage) {
    try {
      Manager manager = Manager.getInstance();
      int canvasWidth = manager.getCanvasWidth();
      int canvasHeight = manager.getCanvasHeight();
      Canvas background = new Canvas(canvasWidth, canvasHeight);
      Canvas elements = new Canvas(canvasWidth, canvasHeight);
      StackPane container = new StackPane(background, elements);
      BorderPane root = new BorderPane(container);
      Scene scene = new Scene(root);

      initCanvas(background, elements);
      addEventHandlers(scene);
      manager.startGame();
      startTimeline();

      primaryStage.setTitle("SnakeFX");
      primaryStage.setScene(scene);
      primaryStage.getIcons().add(ResourceLoader.getInstance().loadImage("icon"));
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void initCanvas(Canvas background, Canvas elements) {
    canvasPainter = new CanvasPainter(background, elements);
    canvasPainter.paintBackground();
  }

  private void addEventHandlers(Scene scene) {

    scene.setOnMouseEntered(event -> scene.setCursor(Cursor.NONE));

    scene.setOnKeyPressed(event -> {
      Manager manager = Manager.getInstance();
      Direction dir = null;
      switch (event.getCode()) {
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
        manager.togglePause();
        break;
      case R:
        manager.restartGame(false);
      default:
        break;
      }

      if (dir != null && !manager.isPaused()) {
        manager.getSnake().move(dir);
      }
    });

  }

  private void startTimeline() {
    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(15), ae -> canvasPainter.paintElements()));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();
  }

  public static void main(String[] args) {
    launch(args);
  }

}
