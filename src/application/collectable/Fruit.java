package application.collectable;

import java.util.Random;

import application.Manager;
import application.ResourceLoader;
import javafx.scene.image.Image;

public class Fruit extends Collectable {

  private enum FruitType {
    RED_APPLE,
    GREEN_APPLE,
    KIWI, 
    LEMON, 
    GRAPE, 
    ORANGE, 
    STRAWBERRY, 
    BANANA, 
    BANANA_SPLIT, 
    BURGER, 
    FRENCH_FRIES, 
    CHERRY, 
    MELON, 
    CHICKEN
  }

  private FruitType type;

  private static Random randomColorGenerator = new Random();

  public Fruit() {
    super();
    int random = randomColorGenerator.nextInt(FruitType.values().length);
    this.type = FruitType.values()[random];
    loadImage();
  }

  public void loadImage() {
    imageKey = ResourceLoader.getInstance().loadProperty(type.name().toLowerCase());
//    image = isLandscape() ?
//    new Image(Main.class.getResourceAsStream(imageKey), ELEMENT_WIDTH, 0, true, true) :
//    new Image(Main.class.getResourceAsStream(imageKey), 0, ELEMENT_HEIGHT, true, true);
    image = new Image(ResourceLoader.class.getResourceAsStream(imageKey), ELEMENT_WIDTH, ELEMENT_HEIGHT, true, true);
  }

  @Override
  public void collect() {
    Manager manager = Manager.getInstance();
    manager.growSnake();
    manager.dropFood();
  }

}
