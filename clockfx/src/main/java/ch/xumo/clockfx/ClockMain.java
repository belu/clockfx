package ch.xumo.clockfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClockMain extends Application {

	@Override
	public void start(final Stage stage) throws Exception {
		final Clock clock = new Clock();

		WindowHelper.makeNodeMovable(stage, clock);
		clock.setOnScroll(event -> clock.applyScrollDelta(event.getDeltaY()));

		final Scene scene = new Scene(clock);
		scene.setFill(Color.TRANSPARENT);

		stage.setScene(scene);
		stage.setAlwaysOnTop(true);
		stage.centerOnScreen();
		stage.initStyle(StageStyle.TRANSPARENT);
		stage.show();
	}

	public static void main(final String[] args) {
		launch(args);
	}
}
