package application;

import javafx.scene.paint.Color;

public abstract class Collectable {

	protected Color color;

	public abstract void collect();

	public Color getColor() {
		return this.color;
	}

}
