package application.utility;

import java.io.IOException;
import java.util.Properties;

import javafx.scene.image.Image;

public class ResourceLoader {

  private static ResourceLoader instance = null;

  private Properties properties;

  private Properties gameProperties;

  private ResourceLoader() {
    properties = new Properties();
    gameProperties = new Properties();
    try {
      properties.load(getClass().getResourceAsStream("/resources/images.properties"));
      gameProperties.load(getClass().getResourceAsStream("/resources/game.properties"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static ResourceLoader getInstance() {
    if (instance == null) {
      instance = new ResourceLoader();
    }
    return instance;
  }

  public String loadProperty(String key) {
    return properties.getProperty(key);
  }

  public String loadGameProperty(String key) {
    return gameProperties.getProperty(key);
  }

  public Image loadImage(String key) {
    return new Image(getClass().getResourceAsStream(loadProperty(key)));
  }

}
