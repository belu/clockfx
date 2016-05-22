package ch.xumo.clockfx;

import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

public class Clock extends Region {

	private static final double RADIUS = 400.0;
	private static final double PERIMETER = 2 * RADIUS * Math.PI;

	private static final double HOUR_SECTION_LENGTH = PERIMETER / 12.0;
	private static final double HOUR_WIDTH = RADIUS / 14;

	private static final double MINUTE_SECTION_LENGTH = PERIMETER / 60.0;
	private static final double MINUTE_WIDTH = RADIUS / 40.0;

	private final StartEndPoints seconds = new StartEndPoints();
	private final StartEndPoints minutes = new StartEndPoints();
	private final StartEndPoints hours = new StartEndPoints();

	public Clock() {
		addHoursCircle();
		addMinutesCircle();

		addHourHand();
		addMinuteHand();
		addSecondHand();

		initializeHands();

		new AnimationTimer() {

			private final AtomicBoolean animatedFirstSecond = new AtomicBoolean(false);
			private final Duration duration = Duration.millis(250);

			@Override
			public void handle(final long now) {
				final LocalTime localTime = LocalTime.now();

				switch (localTime.getSecond()) {
				case 0:
					if (animatedFirstSecond.compareAndSet(false, true)) {
						animate(minutes, minutePoints(localTime));
						animate(hours, hourPoints(localTime));
					}
					seconds.update(secondPoints(localTime));
					break;
				default:
					seconds.update(secondPoints(localTime));
					break;
				case 59:
					/*
					 * Second hand will stand still in 59'th second.
					 */
					animatedFirstSecond.set(false);
					break;
				}
			}

			private void animate(final StartEndPoints startEndPoints, final Tuple<Point2D> points) {
				final KeyFrame startX = new KeyFrame(duration,
						new KeyValue(startEndPoints.getStartX(), points.getFirstEntry().getX()));
				final KeyFrame startY = new KeyFrame(duration,
						new KeyValue(startEndPoints.getStartY(), points.getFirstEntry().getY()));
				final KeyFrame endX = new KeyFrame(duration,
						new KeyValue(startEndPoints.getEndX(), points.getSecondEntry().getX()));
				final KeyFrame endY = new KeyFrame(duration,
						new KeyValue(startEndPoints.getEndY(), points.getSecondEntry().getY()));

				final Timeline timeline = new Timeline();
				timeline.getKeyFrames().addAll(startX, startY, endX, endY);
				timeline.play();
			}
		}.start();
	}

	private void initializeHands() {
		final LocalTime localTime = LocalTime.now();
		minutes.update(minutePoints(localTime));
		hours.update(hourPoints(localTime));
	}

	private Tuple<Point2D> secondPoints(final LocalTime localTime) {
		return timeToPoints(localTime, lt -> TimeGeometry.calculateSecondAngleDegrees(lt), 0.2, 0.7);
	}

	private Tuple<Point2D> minutePoints(final LocalTime localTime) {
		return timeToPoints(localTime, lt -> TimeGeometry.calculateMinuteAngleDegrees(lt), 0.15, 0.98);
	}

	private Tuple<Point2D> hourPoints(final LocalTime localTime) {
		return timeToPoints(localTime, lt -> TimeGeometry.calculateHourAngleDegrees(lt), 0.15, 0.75);
	}

	private Tuple<Point2D> timeToPoints(final LocalTime localTime, final Function<LocalTime, Double> toDegrees,
			final double startFactor, final double endFactor) {
		final double secondAngleDegrees = toDegrees.apply(localTime);
		final Point2D secondStartPoint = TimeGeometry.toPointOnUnitCircle(secondAngleDegrees)
				.multiply(-RADIUS * startFactor).add(RADIUS, RADIUS);
		final Point2D secondEndPoint = TimeGeometry.toPointOnUnitCircle(secondAngleDegrees).multiply(RADIUS * endFactor)
				.add(RADIUS, RADIUS);
		return new Tuple<>(secondStartPoint, secondEndPoint);
	}

	private static final Color COLOR_SECOND = Color.FIREBRICK;

	private void addSecondHand() {
		final Line hand = createHand(COLOR_SECOND, RADIUS / 50.0);
		applyShadow(hand, 0.05);
		hand.setStrokeLineCap(StrokeLineCap.ROUND);
		bindStartEndPoints(hand, seconds);

		final Circle secondCircle = new Circle(RADIUS / 10.0, COLOR_SECOND);
		secondCircle.centerXProperty().bind(seconds.getEndX());
		secondCircle.centerYProperty().bind(seconds.getEndY());
		applyShadow(secondCircle, 0.05);

		final Circle clockCenteredCircle = new Circle(RADIUS, RADIUS, RADIUS / 100.0, Color.BEIGE);

		getChildren().addAll(hand, secondCircle, clockCenteredCircle);
	}

	private void addMinuteHand() {
		final Line hand = createHand(Color.BLACK, HOUR_WIDTH);
		applyShadow(hand, 0.05);
		bindStartEndPoints(hand, minutes);
		getChildren().add(hand);
	}

	private void addHourHand() {
		final Line hand = createHand(Color.BLACK, RADIUS / 12.0);
		applyShadow(hand, 0.05);
		bindStartEndPoints(hand, hours);
		getChildren().add(hand);
	}

	private Line createHand(final Color strokeColor, final double strokeWidth) {
		final Line hand = new Line(RADIUS, RADIUS, RADIUS, 0.0);
		hand.setStroke(strokeColor);
		hand.setStrokeLineCap(StrokeLineCap.BUTT);
		hand.setStrokeWidth(strokeWidth);
		return hand;
	}

	private void applyShadow(final Node node, final double offset) {
		final DropShadow dropShadow = new DropShadow();
		dropShadow.setOffsetX(offset);
		dropShadow.setOffsetY(offset);
		node.setEffect(dropShadow);
	}

	private void bindStartEndPoints(final Line hand, final StartEndPoints startEndPoints) {
		hand.startXProperty().bind(startEndPoints.getStartX());
		hand.startYProperty().bind(startEndPoints.getStartY());
		hand.endXProperty().bind(startEndPoints.getEndX());
		hand.endYProperty().bind(startEndPoints.getEndY());
	}

	private void addMinutesCircle() {
		final Circle circle = createCircle(Color.TRANSPARENT, RADIUS / 15.0);
		circle.setStrokeDashOffset(MINUTE_WIDTH / 2.0);
		circle.getStrokeDashArray().addAll(MINUTE_WIDTH, MINUTE_SECTION_LENGTH - MINUTE_WIDTH);
		getChildren().add(circle);
	}

	private void addHoursCircle() {
		final Circle circle = createCircle(Color.WHITE, RADIUS / 5.0);
		circle.setStrokeDashOffset(HOUR_WIDTH / 2.0);
		circle.getStrokeDashArray().addAll(HOUR_WIDTH, HOUR_SECTION_LENGTH - HOUR_WIDTH);
		getChildren().add(circle);
	}

	private Circle createCircle(final Paint fill, final double strokeWidth) {
		final Circle circle = new Circle(RADIUS, RADIUS, RADIUS, fill);
		circle.setStroke(Color.BLACK);
		circle.setStrokeLineCap(StrokeLineCap.BUTT);
		circle.setStrokeType(StrokeType.INSIDE);
		circle.setStrokeWidth(strokeWidth);
		return circle;
	}

	public void applyScrollDelta(final double scrollDelta) {
		final double newScale;
		if (scrollDelta > 0) {
			newScale = Math.max(getScaleX() * 0.9, 0.1);
		} else {
			newScale = Math.min(getScaleX() * 1.1, 1.0);
		}
		setScaleX(newScale);
		setScaleY(newScale);
	}
}
