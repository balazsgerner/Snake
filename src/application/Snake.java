package application;

import java.util.ArrayList;
import java.util.List;

public class Snake {

	private Manager manager;

	private int initialSize;

	private List<Field> containedFields;

	private List<Field> growPositions;

	private Direction direction;

	private boolean changedDirecton;

	public Snake(int size) {
		this.initialSize = size;
	}

	public void resetToInitial(Direction dir) {
		this.direction = dir;
		this.growPositions = null;
		this.changedDirecton = false;
	}

	public void setManager() {
		this.manager = Manager.getInstance();
	}

	public void move(Direction dir) {
		Direction oppositeDirection = this.direction.getOppositeDirection();
		if (dir == this.direction || dir == oppositeDirection) {
			return;
		}
		if (!changedDirecton) {
			this.direction = dir;
			changedDirecton = true;
		}
	}

	public void progress() {
		Field headPosition = containedFields.get(0);
		Field neighbour = manager.getNeighbour(headPosition, this.direction);

		if (containedFields.contains(neighbour)) {
			manager.killSnake();
		}

		// ++++++++
		shift(neighbour);

		changedDirecton = false;
	}

	private void shift(Field neighbour) {

		int lastIndex = containedFields.size() - 1;

		for (int i = lastIndex; i > 0; i--) {
			Field prevField = containedFields.get(i - 1);

			Field tailPos = containedFields.get(containedFields.size() - 1);
			containedFields.set(i, prevField);

			boolean grow = growPositions != null && !growPositions.isEmpty();
			if (grow) {
				boolean tailReachedGrowPosition = tailPos == growPositions.get(0);

				if (i == lastIndex && tailReachedGrowPosition) {
					containedFields.add(tailPos);
					growPositions.remove(0);
				}
			}
		}
		containedFields.set(0, neighbour);
		neighbour.enter();
	}

	public void setLocation(List<Field> fields) {
		this.containedFields = fields;
		this.growPositions = null;
	}

	public List<Field> getContainedFields() {
		return containedFields;
	}

	public void grow() {
		Field headPosition = containedFields.get(0);
		if (growPositions == null) {
			growPositions = new ArrayList<>();
		}
		growPositions.add(headPosition);
	}

	public int getInitialSize() {
		return initialSize;
	}

}
