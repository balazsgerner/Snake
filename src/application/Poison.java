package application;

import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.paint.Color;

public class Poison extends Collectable {

	public Poison() {
		int random = ThreadLocalRandom.current().nextInt(2);
		if (random == 1) {
			this.color = Color.YELLOW;
		} else {
			this.color = Color.ORANGE;
		}
	}

	@Override
	public void collect() {
		Manager manager = Manager.getInstance();
		manager.killSnake();
	}

}
