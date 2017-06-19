package application;

import java.util.Random;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineJoin;

public class Fruit extends Collectable {

	private static final int ELEMENT_HEIGHT = CanvasPainter.ELEMENT_HEIGHT;

	private static final int ELEMENT_WIDTH = CanvasPainter.ELEMENT_WIDTH;

	private enum FruitType {
		REDAPPLE, GREENAPPLE, REDGRAPE, ORANGE, STRAWBERRY, BANANA
	}

	private static final int NUMBER_OF_FRUITS = FruitType.values().length;

	private FruitType type;

	private static Random randomColorGenerator = new Random();

	public Fruit() {
		int random = randomColorGenerator.nextInt(NUMBER_OF_FRUITS);
		this.type = FruitType.values()[random];

		switch (type) {
		case ORANGE:
			this.color = Color.ORANGE;
			break;
		case REDAPPLE:
		case STRAWBERRY:
			this.color = Color.RED;
			break;
		case GREENAPPLE:
			this.color = Color.LAWNGREEN;
			break;
		case REDGRAPE:
			// GRAPE COLOR
			this.color = Color.web("#8A2BE2").interpolate(Color.WHITE, 0.3);
			break;
		case BANANA:
			this.color = Color.YELLOW;
		default:
			break;
		}

	}

	@Override
	public void collect() {
		Manager manager = Manager.getInstance();
		manager.growSnake();
		manager.dropFood();
	}

	public void paintSelf(GraphicsContext ctx, Point2D point) {
		ctx.setFill(this.color);
		ctx.setStroke(this.color);
		double y = point.getY();
		double x = point.getX();
		if (type == FruitType.REDGRAPE) {
			paintFG(ctx, y, x);
			paintBG(ctx, y, x);
		} else if (type == FruitType.STRAWBERRY) {
			ctx.setStroke(Color.RED);
			ctx.setLineWidth(ELEMENT_WIDTH / 2);
			ctx.setLineJoin(StrokeLineJoin.ROUND);
			ctx.strokePolygon(
					new double[] { x + ELEMENT_WIDTH / 4, x + ELEMENT_WIDTH - ELEMENT_WIDTH / 4,
							x + ELEMENT_WIDTH / 2 },
					new double[] { y + ELEMENT_HEIGHT / 4, y + ELEMENT_HEIGHT / 4,
							y + ELEMENT_HEIGHT - ELEMENT_HEIGHT / 4 },
					3);

			ctx.setLineWidth(3);
			ctx.setStroke(Color.DARKGREEN);
			ctx.strokeLine(x + ELEMENT_WIDTH / 2, y + ELEMENT_WIDTH / 4, x + ELEMENT_WIDTH / 2 + 2, y);
			ctx.strokeLine(x + ELEMENT_WIDTH / 2, y + ELEMENT_WIDTH / 4, x + ELEMENT_WIDTH / 2 - 2, y);

		} else if (type == FruitType.BANANA) {
			paintBanana(ctx, point);
		} else {
			paintBG(ctx, y, x);
			paintFG(ctx, y, x);
		}
	}

	private void paintBanana(GraphicsContext ctx, Point2D point) {
		double x = point.getX();
		double y = point.getY();

		ctx.setLineWidth(3.0);
		ctx.beginPath();
		ctx.moveTo(x + ELEMENT_WIDTH / 4, y + 3);
		ctx.bezierCurveTo(x, y + 3, x, y + ELEMENT_HEIGHT, x + ELEMENT_WIDTH * 0.7, y + ELEMENT_HEIGHT);

		ctx.stroke();
		ctx.fill();
		ctx.setStroke(Color.YELLOWGREEN);
		ctx.strokeLine(x + ELEMENT_WIDTH / 4, y + 3, x + ELEMENT_WIDTH / 4 + 2, y);

	}

	private void paintBG(GraphicsContext ctx, double y, double x) {
		switch (type) {
		case GREENAPPLE:
		case REDAPPLE:
			paintAppleBG(ctx, y, x);
			break;
		case REDGRAPE:
			paintGrapeBG(ctx, y, x);
			break;
		case ORANGE:
			paintOrangeBG(ctx, y, x);
		default:
			break;
		}
	}

	private void paintAppleBG(GraphicsContext ctx, double y, double x) {
		// draw BG
		ctx.fillRoundRect(x, y, ELEMENT_WIDTH, ELEMENT_HEIGHT, 10, 10);
	}

	private void paintOrangeBG(GraphicsContext ctx, double y, double x) {
		ctx.fillOval(x, y, ELEMENT_WIDTH, ELEMENT_HEIGHT);
		ctx.setStroke(Color.DARKORANGE);
		ctx.setLineWidth(0.5);
		ctx.strokeOval(x, y, ELEMENT_WIDTH, ELEMENT_HEIGHT);
	}

	private void paintGrapeBG(GraphicsContext ctx, double y, double x) {
		final double grapeWidth = ELEMENT_WIDTH / 3.0;
		final double grapeHeight = ELEMENT_HEIGHT / 3.0;
		ctx.setStroke(this.color.darker());
		ctx.setLineWidth(1);
		for (int i = 0; i < 3; i++) {
			ctx.strokeOval(x + i * grapeWidth, y, grapeWidth, grapeHeight);
			ctx.fillOval(x + i * grapeWidth, y, grapeWidth, grapeHeight);
		}

		ctx.strokeOval(x + ELEMENT_WIDTH / 2 - grapeWidth, y + grapeHeight, grapeWidth, grapeHeight);
		ctx.fillOval(x + ELEMENT_WIDTH / 2 - grapeWidth, y + grapeHeight, grapeWidth, grapeHeight);

		ctx.strokeOval(x + ELEMENT_WIDTH / 2, y + grapeHeight, grapeWidth, grapeHeight);
		ctx.fillOval(x + ELEMENT_WIDTH / 2, y + grapeHeight, grapeWidth, grapeHeight);

		ctx.strokeOval(x + ELEMENT_WIDTH / 2 - grapeWidth / 2, y + 2 * grapeHeight, grapeWidth, grapeHeight);
		ctx.fillOval(x + ELEMENT_WIDTH / 2 - grapeWidth / 2, y + 2 * grapeHeight, grapeWidth, grapeHeight);
	}

	private void paintFG(GraphicsContext ctx, double y, double x) {
		// draw FG
		Color col = null;
		if (type == FruitType.REDGRAPE) {
			col = this.color;
		} else {
			col = Color.DARKGREEN;
		}
		ctx.setStroke(col);
		ctx.setLineWidth(3.0);
		if (type != FruitType.ORANGE) {
			ctx.strokeLine(x + ELEMENT_WIDTH / 2, y + ELEMENT_HEIGHT / 4, x + ELEMENT_WIDTH / 2 + ELEMENT_WIDTH / 10,
					y - ELEMENT_HEIGHT / 10);
		} else {
			ctx.strokeLine(x + ELEMENT_WIDTH / 2, y + 3, x + ELEMENT_WIDTH / 2 + ELEMENT_WIDTH / 10, y);
		}
	}

}
