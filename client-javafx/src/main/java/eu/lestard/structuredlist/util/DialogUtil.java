package eu.lestard.structuredlist.util;

import javafx.application.Platform;
import javafx.scene.control.Dialog;
import javafx.stage.Window;

/**
 * @author manuel.mauky
 */
public class DialogUtil {
	private static Window applicationWindow = null;
	
	public static void setApplicationWindow(Window window) {
		applicationWindow = window;
	}

	public static void initDialogPositioning(Dialog dialog) {

		final double windowX = applicationWindow == null ? 0.0 : applicationWindow.getX();
		final double windowY = applicationWindow == null ? 0.0 : applicationWindow.getY();

		// initially position the dialog on the window
		dialog.setX(windowX);
		dialog.setY(windowY);

		// after the dialog is visible we can move it to the middle of the window.
		dialog.setOnShown(e -> Platform.runLater(() -> {

			final double windowWidth = applicationWindow == null ? 1000 : applicationWindow.getWidth();
			final double windowHeight = applicationWindow == null ? 500 : applicationWindow.getHeight();

			final double dialogWidth = dialog.getWidth();
			final double dialogHeight = dialog.getHeight();

			final double offsetX = (windowWidth - dialogWidth) / 2;
			final double offsetY = (windowHeight - dialogHeight) / 2;

			final double dialogX = windowX + offsetX;
			final double dialogY = windowY + offsetY;

			dialog.setX(dialogX);
			dialog.setY(dialogY);
		}));
	}
	
}
