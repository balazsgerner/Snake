package application.game.collectable;

import application.game.Manager;
import application.utility.ResourceLoader;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Collectable {

  protected int elementWidth;

  protected int elementHeight;

  protected double outlineWidth;
  
  protected String imageKey;

  protected Image image;
    
  public Collectable() {
    Manager manager = Manager.getInstance();
    elementWidth = manager.getElementWidth();
    elementHeight = manager.getElementHeight();
    outlineWidth = elementWidth * 0.0375;
  }

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
