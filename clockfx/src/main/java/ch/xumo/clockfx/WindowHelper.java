package ch.xumo.clockfx;

import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.util.concurrent.AtomicDouble;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WindowHelper {

	private WindowHelper() {
		throw new UnsupportedOperationException();
	}

	public static void makeNodeMovable(final Stage stage, final Node node) {
		final AtomicDouble xOffset = new AtomicDouble();
		final AtomicDouble yOffset = new AtomicDouble();

		final AtomicBoolean fadeOutInProgress = new AtomicBoolean(false);

		node.setOnMousePressed(event -> {
			if (event.isSecondaryButtonDown()) {
				if (fadeOutInProgress.compareAndSet(false, true)) {
					fadeOutAndExit(node);
				}
			}
			xOffset.set(event.getSceneX());
			yOffset.set(event.getSceneY());
		});
		node.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - xOffset.get());
			stage.setY(event.getScreenY() - yOffset.get());
		});
	}

	private static void fadeOutAndExit(final Node node) {
		final FadeTransition ft = new FadeTransition(Duration.millis(1000), node);
		ft.setFromValue(1.0);
		ft.setToValue(0.0);
		ft.setOnFinished(actionEvent -> Platform.exit());
		ft.play();
	}
}
