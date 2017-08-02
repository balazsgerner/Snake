package application.collectable;

import java.util.concurrent.ThreadLocalRandom;

import application.Main;
import application.Manager;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Poison extends Collectable {

  enum PoisonColor {
    YELLOW, ORANGE, RED, SPECIAL
  }

  private PoisonColor type;

  private Image poisonImg;

  public Poison() {
    int ordinal = ThreadLocalRandom.current().nextInt(PoisonColor.values().length);
    type = PoisonColor.values()[ordinal];
    if (type == PoisonColor.SPECIAL) {
      poisonImg = new Image(Main.class.getResourceAsStream("../resources/special_poison.png"), ELEMENT_WIDTH, ELEMENT_HEIGHT, true, true);
    }
  }

  @Override
  public void collect() {
    Manager manager = Manager.getInstance();
    manager.killSnake();
  }

  @Override
  public void paint(GraphicsContext ctx, Point2D point) {
    final double x = point.getX();
    final double y = point.getY();

    switch (type) {
    case YELLOW:
      this.color = Color.YELLOW;
      break;
    case ORANGE:
      this.color = Color.ORANGE;
      break;
    case RED:
      this.color = Color.RED;
      break;
    default:
      this.color = null; // SPECIAL
    }

    if (type == PoisonColor.SPECIAL) {
      ctx.drawImage(poisonImg, x, y);
    } else {
      // draw BG
      ctx.setFill(color);
      ctx.fillRoundRect(x, y, ELEMENT_WIDTH, ELEMENT_HEIGHT, ELEMENT_WIDTH / 2, ELEMENT_HEIGHT / 2);

      // draw "X"
      ctx.setStroke(Color.BLACK);
      double lineWidth = (double) ELEMENT_WIDTH / 6.66;
      ctx.setLineWidth(lineWidth);
      ctx.strokeLine(x + ELEMENT_WIDTH / 4, y + ELEMENT_HEIGHT / 4, x + ELEMENT_WIDTH - ELEMENT_WIDTH / 4, y + ELEMENT_HEIGHT - ELEMENT_HEIGHT / 4);
      ctx.strokeLine(x + ELEMENT_WIDTH / 4, y + ELEMENT_HEIGHT - ELEMENT_HEIGHT / 4, x + ELEMENT_WIDTH - ELEMENT_WIDTH / 4, y + ELEMENT_HEIGHT / 4);
    }

  }

}
