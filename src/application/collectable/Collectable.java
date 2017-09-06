package application.collectable;

import application.CanvasPainter;
import application.ResourceLoader;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Collectable {

  protected static final double ELEMENT_WIDTH = CanvasPainter.ELEMENT_WIDTH;

  protected static final double ELEMENT_HEIGHT = CanvasPainter.ELEMENT_HEIGHT;

  protected Color color;

  protected String imageKey;

  protected Image image;

  public abstract void loadImage();

  public abstract void collect();

  protected boolean isLandscape() {
    return Boolean.valueOf(ResourceLoader.getInstance().loadProperty(imageKey + "_landscape"));
  }

  public void paint(GraphicsContext ctx, Point2D point) {
    ctx.setEffect(new DropShadow(4, Color.WHITE));
    ctx.drawImage(image, point.getX(), point.getY());
    ctx.setEffect(null);
  }

}
