package application;

import application.collectable.Collectable;

public class Field {

	private Collectable collectable;

	private int index;

	public void addCollectable(Collectable c) {
		this.collectable = c;
	}

	public void removeCollectable() {
		collectable = null;
	}

	public void enter() {
		if (collectable != null) {
			collectable.collect();
			collectable = null;
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Collectable getCollectable() {
		return collectable;
	}

}
