package ch.xumo.clockfx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;

public class StartEndPoints {

	private final DoubleProperty startX = new SimpleDoubleProperty();
	private final DoubleProperty startY = new SimpleDoubleProperty();
	private final DoubleProperty endX = new SimpleDoubleProperty();
	private final DoubleProperty endY = new SimpleDoubleProperty();

	public void update(final Tuple<Point2D> startEndPoints) {
		startX.set(startEndPoints.getFirstEntry().getX());
		startY.set(startEndPoints.getFirstEntry().getY());
		endX.set(startEndPoints.getSecondEntry().getX());
		endY.set(startEndPoints.getSecondEntry().getY());
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
