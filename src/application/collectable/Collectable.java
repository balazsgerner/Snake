package application.collectable;

import application.CanvasPainter;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Collectable {

  protected static final double ELEMENT_WIDTH = CanvasPainter.ELEMENT_WIDTH;
  
  protected static final double ELEMENT_HEIGHT = CanvasPainter.ELEMENT_HEIGHT;
    
  protected Color color;

  public abstract void collect();

  public abstract void paint(GraphicsContext ctx, Point2D point);

}
