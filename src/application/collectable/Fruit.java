package application.collectable;

import java.util.Random;
import application.Main;
import application.Manager;
import javafx.scene.image.Image;

public class Fruit extends Collectable {

  private enum FruitType {
    RED_APPLE, GREEN_APPLE, KIWI, LEMON, GRAPE, ORANGE, STRAWBERRY, BANANA, BANANA_SPLIT, BURGER, FRENCH_FRIES, CHERRY, MELON
  }

  private FruitType type;

  private static Random randomColorGenerator = new Random();

  public Fruit() {
    super();
    int random = randomColorGenerator.nextInt(FruitType.values().length);
    this.type = FruitType.values()[random];

    // Store resized image in memory
    String imgKey = properties.getProperty(type.name().toLowerCase());
    image = new Image(Main.class.getResourceAsStream(imgKey), ELEMENT_WIDTH, ELEMENT_HEIGHT, true, true);
  }

  @Override
  public void collect() {
    Manager manager = Manager.getInstance();
    manager.growSnake();
    manager.dropFood();
  }

}
