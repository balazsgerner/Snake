package application.utility;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import application.game.Field;
import application.game.Manager;
import application.game.collectable.Collectable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class CanvasPainter {

  private Canvas background;

  private ColorUtilTimer colorUtil;

  private GraphicsContext ctx;

  private boolean showGrid;

  private Boolean bgTexture;

  private String imgPath;

  public CanvasPainter(Canvas background, Canvas elements) {
    ResourceLoader resourceLoader = ResourceLoader.getInstance();
    showGrid = Boolean.valueOf(resourceLoader.loadGameProperty("show_grid"));
    bgTexture = Boolean.valueOf(resourceLoader.loadGameProperty("bg_texture"));
    if (bgTexture) {
      imgPath = resourceLoader.loadProperty("bg_texture_path");
    }
    this.background = background;
    ctx = elements.getGraphicsContext2D();
    colorUtil = new ColorUtilTimer();
    colorUtil.start();
  }

  public void paintBackground() {
    Manager manager = Manager.getInstance();
    GraphicsContext bgCtx = background.getGraphicsContext2D();
    bgCtx.setFill(Color.BLACK);

    int canvasHeight = manager.getCanvasHeight();
    int canvasWidth = manager.getCanvasWidth();
    if (bgTexture) {
      Image bgImg = new Image(imgPath, canvasWidth, canvasHeight, true, true);
      bgCtx.drawImage(bgImg, 0, 0);
    } else {
      bgCtx.fillRect(0, 0, canvasWidth, canvasHeight);
    }

    if (showGrid) {
      bgCtx.setStroke(Color.WHITE);
      bgCtx.setLineWidth(1);
      int gridHeight = manager.getGridHeight();
      int gridWidth = manager.getGridWidth();
      Stream.iterate(gridWidth, x -> x < canvasWidth, x -> x + gridWidth).forEach(x -> bgCtx.strokeLine(x, 0, x, canvasHeight));
      Stream.iterate(gridHeight, y -> y < canvasHeight, y -> y + gridHeight).forEach(y -> bgCtx.strokeLine(0, y, canvasWidth, y));
    }
  }

  public void paintElements() {
    Manager manager = Manager.getInstance();
    ctx.clearRect(0, 0, manager.getCanvasWidth(), manager.getCanvasHeight());
    paintFood();
    paintPoison();
    paintSnake();
  }

  private void paintFood() {
    Manager manager = Manager.getInstance();
    Field foodContainer = manager.getFoodContainer();
    if (foodContainer == null) {
      return;
    }

    Point2D point = manager.getTopLeftCornerPosByIndex(foodContainer.getIndex());
    Collectable fruit = foodContainer.getCollectable();

    if (fruit != null) {
      fruit.paint(ctx, point);
    }
  }

  private void paintPoison() {
    Manager manager = Manager.getInstance();
    manager.getPoisonContainer().forEach(f -> {
      Point2D point = manager.getTopLeftCornerPosByIndex(f.getIndex());
      Collectable poison = f.getCollectable();
      if (poison != null) {
        poison.paint(ctx, point);
      }
    });

  }

  private void paintSnake() {
    Manager manager = Manager.getInstance();
    List<Field> fields = manager.getSnake().getContainedFields();
    IntStream.range(0, fields.size()).forEach(index -> {
      Point2D point = manager.getTopLeftCornerPosByIndex(fields.get(index).getIndex());
      Color col = index == 0 ? colorUtil.getSnakeColor().interpolate(Color.WHITE, 0.9) : colorUtil.getSnakeColor().brighter();
      ctx.setFill(col);
      ctx.fillRect(point.getX(), point.getY(), manager.getElementWidth(), manager.getElementHeight());
    });
  }

}
