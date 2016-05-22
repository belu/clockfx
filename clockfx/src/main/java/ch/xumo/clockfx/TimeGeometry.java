package ch.xumo.clockfx;

import java.time.LocalTime;

import javafx.geometry.Point2D;

public class TimeGeometry {

	public static double calculateSecondAngleDegrees(final LocalTime now) {
		double result = now.getSecond();
		result += (now.getNano() / 1_000_000_000.0);
		result *= 60.0 / 59.0;
		return result * 6.0;
	}

	public static double calculateHourAngleDegrees(final LocalTime now) {
		double result = now.getHour() % 12;
		result += (now.getMinute() / 60.0);
		return result * 30.0;
	}

	public static double calculateMinuteAngleDegrees(final LocalTime now) {
		return now.getMinute() * 6.0;
	}

	public static Point2D toPointOnUnitCircle(final double degrees) {
		final double radians = Math.toRadians(degrees);

		final double x = Math.sin(radians);
		final double y = -Math.cos(radians);

		return new Point2D(x, y);
	}
}
