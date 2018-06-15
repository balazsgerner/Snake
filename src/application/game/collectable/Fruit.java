package application.game.collectable;

import java.util.concurrent.ThreadLocalRandom;

import application.game.Manager;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class Fruit extends Collectable {

  protected enum FruitType{
    RED_APPLE(Color.RED, Color.GREEN), 
    GREEN_APPLE(Color.GREENYELLOW, Color.GREEN),
    ORANGE(Color.ORANGE, Color.GREEN), 
    STRAWBERRY(Color.RED, Color.GREEN),
    CHERRY(Color.RED, Color.GREENYELLOW);

    Color fill, stroke;

    FruitType(Color fill, Color stroke) {
      this.fill = fill;
      this.stroke = stroke;
    }
    
    Color getFill() {
      return fill;
    }
    
    public Color getStroke() {
      return stroke;
    }
    
  }

  private FruitType type;

  public Fruit() {
    type = FruitType.values()[ThreadLocalRandom.current().nextInt(FruitType.values().length)];
  }

  @Override
  public void collect() {
    Manager manager = Manager.getInstance();
    manager.getSnake().grow();
    manager.dropFood();
  }

  @Override
  public void paint(GraphicsContext ctx, Point2D point) {
    ctx.setFill(type.getFill());
    ctx.setStroke(type.getStroke());
    double x = point.getX();
    double y = point.getY();
    switch (type) {
    case RED_APPLE:
      ctx.fillRoundRect(x, y + elementHeight * 0.05, elementWidth, elementHeight * 0.95, elementWidth / 3,
          elementHeight / 2);
      ctx.setLineWidth(outlineWidth);
      ctx.strokeRoundRect(x, y + elementHeight * 0.05, elementWidth, elementHeight * 0.95, elementWidth / 3,
          elementHeight / 2);
      ctx.setLineWidth(elementWidth / 6);
      ctx.strokeLine(x + elementWidth * 0.5, y + elementHeight * 0.35, x + elementWidth * 0.55, y);
      break;
    case GREEN_APPLE:
      ctx.fillRoundRect(x, y + elementHeight * 0.05, elementWidth, elementHeight * 0.95, elementWidth / 3,
          elementHeight / 2);
      ctx.setLineWidth(outlineWidth);
      ctx.strokeRoundRect(x, y + elementHeight * 0.05, elementWidth, elementHeight * 0.95, elementWidth / 3,
          elementHeight / 2);
      ctx.setLineWidth(elementWidth / 6);
      ctx.strokeLine(x + elementWidth * 0.45, y + elementHeight * 0.35, x + elementWidth * 0.55, y);
      break;
    case ORANGE:
      ctx.fillRoundRect(x, y + elementHeight * 0.05, elementWidth, elementHeight * 0.95, elementWidth / 3,
          elementHeight / 2);
      ctx.setLineWidth(outlineWidth);
      ctx.strokeRoundRect(x, y + elementHeight * 0.05, elementWidth, elementHeight * 0.95, elementWidth / 3,
          elementHeight / 2);
      ctx.setLineWidth(elementWidth / 6);
      ctx.strokeLine(x + elementWidth * 0.45, y + elementHeight * 0.35, x + elementWidth * 0.55, y);
      break;
    case CHERRY:
      ctx.setLineWidth(elementWidth / 6);
      ctx.strokeLine(x + elementWidth * 0.5, y, x + elementWidth * 0.25, y + elementHeight * 0.33);
      ctx.strokeLine(x + elementWidth * 0.5, y, x + elementWidth * 0.75, y + elementHeight * 0.33);
      ctx.setLineWidth(outlineWidth);
      ctx.fillRoundRect(x + (elementWidth / 2) * 0.01, y + elementHeight * 0.33, (elementWidth / 2) * 0.98,
          elementHeight * 0.75, elementWidth / 3, elementHeight / 3);
      ctx.strokeRoundRect(x + (elementWidth / 2) * 0.01, y + elementHeight * 0.33, (elementWidth / 2) * 0.98,
          elementHeight * 0.75, elementWidth / 3, elementHeight / 3);
      ctx.fillRoundRect(x + (elementWidth / 2) + (elementWidth / 2) * 0.01, y + elementHeight * 0.33,
          (elementWidth / 2) * 0.98, elementHeight * 0.75, elementWidth / 3, elementHeight / 3);
      ctx.strokeRoundRect(x + (elementWidth / 2) + (elementWidth / 2) * 0.01, y + elementHeight * 0.33,
          (elementWidth / 2) * 0.98, elementHeight * 0.75, elementWidth / 3, elementHeight / 3);
      break;
    case STRAWBERRY:
      ctx.setLineJoin(StrokeLineJoin.ROUND);
      ctx.setLineCap(StrokeLineCap.ROUND);
      ctx.setLineWidth(elementWidth / 2);
      ctx.beginPath();
      ctx.moveTo(x + elementWidth * 0.15, y + elementHeight * 0.2);
      ctx.lineTo(x + elementWidth * 0.5, y + elementHeight * 0.8);
      ctx.lineTo(x + elementWidth * 0.85, y + elementHeight * 0.2);
      ctx.closePath();
      ctx.stroke();
      ctx.fill();
      ctx.setLineWidth(elementWidth / 4);
      ctx.strokeLine(x + elementWidth * 0.5, y + elementHeight * 0.3, x + elementWidth * 0.65, y);
      ctx.strokeLine(x + elementWidth * 0.5, y + elementHeight * 0.3, x + elementWidth * 0.35, y);
      break;
    }
  }

}
