package application.collectable;

import java.util.concurrent.ThreadLocalRandom;

import application.Main;
import application.Manager;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Poison extends Collectable {

  enum PoisonColor {
    YELLOW, BLUE, RED
  }

  private PoisonColor type;

  private Image poisonImg;

  public Poison() {
    super();
    int ordinal = ThreadLocalRandom.current().nextInt(PoisonColor.values().length);
    type = PoisonColor.values()[ordinal];
    String imgKey = properties.getProperty("poison_" + type.name().toLowerCase());
    poisonImg = new Image(Main.class.getResourceAsStream(imgKey), ELEMENT_WIDTH, ELEMENT_HEIGHT, true, true);
  }

  @Override
  public void collect() {
    Manager manager = Manager.getInstance();
    manager.killSnake();
  }

  @Override
  public void paint(GraphicsContext ctx, Point2D point) {
    ctx.drawImage(poisonImg, point.getX(), point.getY());
  }
}
