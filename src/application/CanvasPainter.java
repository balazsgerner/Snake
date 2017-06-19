package application;

import java.util.List;
import java.util.Timer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasPainter {

	public static final int CANVAS_HEIGHT = 600;

	public static final int CANVAS_WIDTH = 600;

	private static final int GRID_HEIGHT = CANVAS_HEIGHT / (Manager.MAP_HEIGHT);

	private final static int GRID_WIDTH = CANVAS_WIDTH / (Manager.MAP_WIDTH);

	public static final int ELEMENT_WIDTH = GRID_WIDTH - GRID_HEIGHT / 3;

	public static final int ELEMENT_HEIGHT = GRID_HEIGHT - GRID_WIDTH / 3;

	private static final boolean SHOWGRID = false;

	private Canvas background;

	private ColorUtil colorUtil;

	private Manager manager;

	private GraphicsContext ctx;

	public CanvasPainter(Canvas background, Canvas elements) {
		this.background = background;
		this.ctx = elements.getGraphicsContext2D();
		this.manager = Manager.getInstance();

		// ------ COLOR CHANGE TIMER
		if (ColorUtil.COLOR_CHANGE) {
			Timer timer = new Timer(true);
			colorUtil = new ColorUtil();
			timer.scheduleAtFixedRate(colorUtil, 0, ColorUtil.COLOR_CHANGE_MS);
		}
	}

	public void paintBackground() {
		GraphicsContext backgroundContext = background.getGraphicsContext2D();
		backgroundContext.setFill(Color.BLACK);
		backgroundContext.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

		if (SHOWGRID) {
			backgroundContext.setStroke(Color.WHITE);
			backgroundContext.setLineWidth(1);

			for (int x = GRID_WIDTH; x < CANVAS_WIDTH; x += GRID_WIDTH) {
				backgroundContext.strokeLine(x, 0, x, CANVAS_HEIGHT);
			}

			for (int y = GRID_HEIGHT; y < CANVAS_HEIGHT; y += GRID_HEIGHT) {
				backgroundContext.strokeLine(0, y, CANVAS_WIDTH, y);
			}
		}
	}

	public void paintElements() {
		// CLEAR ELEMENTS
		ctx.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

		// REPAINT ELEMENTS
		paintFood();
		paintPoison();
		paintSnake();
	}

	private void paintFood() {
		Field foodContainer = manager.getFoodContainer();
		if (foodContainer == null) {
			return;
		}

		int index = foodContainer.getIndex();
		Point2D point = getTopLeftCornerPosByIndex(index);
		Fruit fruit = (Fruit) foodContainer.getCollectable();

		if (fruit != null) {

			fruit.paintSelf(ctx, point);
		}
	}

	private void paintPoison() {
		List<Field> poisonContainers = manager.getPoisonContainer();
		if (poisonContainers == null || poisonContainers.isEmpty()) {
			return;
		}

		int index;
		for (Field f : poisonContainers) {
			index = f.getIndex();
			Point2D point = getTopLeftCornerPosByIndex(index);
			Collectable poison = f.getCollectable();

			if (poison != null) {
				Color poisonColor = poison.getColor();

				// draw BG
				ctx.setFill(poisonColor);
				ctx.fillRoundRect(point.getX(), point.getY(), ELEMENT_WIDTH, ELEMENT_HEIGHT, (double) ELEMENT_WIDTH / 2,
						(double) ELEMENT_HEIGHT / 2);

				// draw "X"
				ctx.setStroke(Color.BLACK);
				double lineWidth = (double) ELEMENT_WIDTH / 6.66;
				ctx.setLineWidth(lineWidth);
				ctx.strokeLine(point.getX() + ELEMENT_WIDTH / 4, point.getY() + ELEMENT_HEIGHT / 4,
						point.getX() + ELEMENT_WIDTH - ELEMENT_WIDTH / 4,
						point.getY() + ELEMENT_HEIGHT - ELEMENT_HEIGHT / 4);
				ctx.strokeLine(point.getX() + ELEMENT_WIDTH / 4, point.getY() + ELEMENT_HEIGHT - ELEMENT_HEIGHT / 4,
						point.getX() + ELEMENT_WIDTH - ELEMENT_WIDTH / 4, point.getY() + ELEMENT_HEIGHT / 4);
			}
		}
	}

	private void paintSnake() {
		Snake snake = manager.getSnake();
		List<Field> fields = snake.getContainedFields();

		if (fields == null) {
			return;
		}

		Point2D point;
		Color col = colorUtil.getSnakeColor();
		ctx.setFill(col);

		int index;
		int lastIndex = fields.size() - 1;
		for (int i = lastIndex; i >= 0; i--) {
			Field f = fields.get(i);
			index = f.getIndex();

			point = getTopLeftCornerPosByIndex(index);

			if (i == 0) {
				col = col.interpolate(Color.WHITE, 0.9);
				ctx.setFill(col);
			} else {
				ctx.setFill(col);
				col = col.brighter();
			}
			ctx.fillRect(point.getX(), point.getY(), ELEMENT_WIDTH, ELEMENT_HEIGHT);
		}
	}

	private Point2D getTopLeftCornerPosByIndex(int index) {
		int rowNum = index / Manager.MAP_HEIGHT;
		int colNum = index % Manager.MAP_WIDTH;

		double x = (double) colNum * GRID_WIDTH + ELEMENT_WIDTH / 4;
		double y = (double) rowNum * GRID_HEIGHT + ELEMENT_HEIGHT / 4;
		return new Point2D(x, y);
	}

}
