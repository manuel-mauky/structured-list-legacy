package eu.lestard.structuredlist.ui.itemoverview;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ItemInputDialog extends Dialog<String> {
	
	public ItemInputDialog(String headerText, String preFilledContent) {
		setTitle("Item Dialog");
		setHeaderText(headerText);
		setResizable(true);

		

		TextArea textArea = new TextArea();

		textArea.setWrapText(true);
		textArea.setText(preFilledContent);
		Platform.runLater(textArea::requestFocus);

		
		
		VBox root = new VBox();
		root.setPadding(new Insets(5));
		root.getChildren().add(textArea);
		VBox.setVgrow(textArea, Priority.ALWAYS);
		getDialogPane().setContent(root);
		

		
		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		setResultConverter(buttonType -> {
			if (buttonType == ButtonType.OK) {
				return textArea.getText();
			}

			return null;
		});
	}
	
	public ItemInputDialog(String headerText) {
		this(headerText, null);
	}
	
	
}
