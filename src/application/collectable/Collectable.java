package application.collectable;

import java.util.Properties;

import application.CanvasPainter;
import application.Main;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Collectable {

  protected static final double ELEMENT_WIDTH = CanvasPainter.ELEMENT_WIDTH;

  protected static final double ELEMENT_HEIGHT = CanvasPainter.ELEMENT_HEIGHT;

  protected Color color;

  protected Properties properties;

  protected Image image;

  public Collectable() {
    properties = new Properties();
    try {
      properties.load(Main.class.getResourceAsStream("../resources/images.properties"));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public abstract void collect();

  public void paint(GraphicsContext ctx, Point2D point) {
    ctx.drawImage(image, point.getX(), point.getY());
  }

}
