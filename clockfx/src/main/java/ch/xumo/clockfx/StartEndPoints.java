package ch.xumo.clockfx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.util.Pair;

public class StartEndPoints {

	private final DoubleProperty startX = new SimpleDoubleProperty();
	private final DoubleProperty startY = new SimpleDoubleProperty();
	private final DoubleProperty endX = new SimpleDoubleProperty();
	private final DoubleProperty endY = new SimpleDoubleProperty();

	public void update(final Pair<Point2D, Point2D> startEndPoints) {
		startX.set(startEndPoints.getKey().getX());
		startY.set(startEndPoints.getKey().getY());
		endX.set(startEndPoints.getValue().getX());
		endY.set(startEndPoints.getValue().getY());
	}

	public DoubleProperty getStartX() {
		return startX;
	}

	public DoubleProperty getStartY() {
		return startY;
	}

	public DoubleProperty getEndX() {
		return endX;
	}

	public DoubleProperty getEndY() {
		return endY;
	}
}
