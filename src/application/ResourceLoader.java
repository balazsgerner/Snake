package application;

import java.io.IOException;
import java.util.Properties;

public class ResourceLoader {

  private static ResourceLoader instance = null;

  private Properties properties;

  private ResourceLoader() {
    properties = new Properties();
    try {
      properties.load(getClass().getResourceAsStream("/resources/images.properties"));
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

  public Properties getProperties() {
    return properties;
  }

  public String loadProperty(String key) {
    return instance.getProperties().getProperty(key);
  }

}
