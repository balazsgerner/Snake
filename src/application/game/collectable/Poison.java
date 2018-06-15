package application.game.collectable;

import application.game.Manager;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Poison extends Collectable {

  enum PoisonColor {
    YELLOW, BLUE, RED
  }

  private PoisonColor type = PoisonColor.RED;

  @Override
  public void paint(GraphicsContext ctx, Point2D point) {
    switch (type) {
    case RED:
      ctx.setFill(Color.RED);
      ctx.fillRoundRect(point.getX(), point.getY(), elementWidth, elementHeight, elementWidth / 2, elementHeight / 2);
      Color dark = new Color(0, 0, 0, 1);
      ctx.setStroke(dark);
      ctx.setLineWidth(0.5);
      ctx.strokeRoundRect(point.getX(), point.getY(), elementWidth, elementHeight, elementWidth / 2, elementHeight / 2);
      ctx.setLineWidth(elementWidth / 6);
      ctx.setStroke(Color.BLACK);
      ctx.strokeLine(point.getX() + elementWidth * 0.2, point.getY() + elementHeight * 0.8, point.getX() + elementWidth * 0.8,
          point.getY() + elementHeight * 0.2);
      ctx.strokeLine(point.getX() + elementWidth * 0.2, point.getY() + elementHeight * 0.2, point.getX() + elementWidth * 0.8,
          point.getY() + elementHeight * 0.8);
      break;

    default:
      break;
    }
  }

  @Override
  public void collect() {
    // --life
    Manager.getInstance().restartGame(true);
  }

}
