package application.game;

import application.game.collectable.Collectable;

public class Field {

  private Collectable collectable;

  private int index;

  public Field(int index) {
    this.index = index;
  }

  public void addCollectable(Collectable c) {
    this.collectable = c;
  }

  public void removeCollectable() {
    collectable = null;
  }

  public void enter() {
    if (collectable != null) {
      collectable.collect();
      removeCollectable();
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

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Field) {
      return index == ((Field) obj).index;
    }
    return false;
  }

}
