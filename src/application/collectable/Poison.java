package application.collectable;

import java.util.concurrent.ThreadLocalRandom;

import application.Main;
import application.Manager;
import javafx.scene.image.Image;

public class Poison extends Collectable {

  enum PoisonColor {
    YELLOW, BLUE, RED
  }

  private PoisonColor type;

  public Poison() {
    super();
    int ordinal = ThreadLocalRandom.current().nextInt(PoisonColor.values().length);
    type = PoisonColor.values()[ordinal];
    loadImage();
  }

  @Override
  public void loadImage() {
    imageKey = properties.getProperty("poison_" + type.name().toLowerCase());
    image = isLandscape() ?
    new Image(Main.class.getResourceAsStream(imageKey), ELEMENT_WIDTH, 0, true, true) :
    new Image(Main.class.getResourceAsStream(imageKey), 0, ELEMENT_HEIGHT, true, true);
  }

  @Override
  public void collect() {
    Manager manager = Manager.getInstance();
    manager.killSnake();
  }

}
