package views;

import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;

public class ImageLoadingProgressBar extends GridPane{

	public ImageLoadingProgressBar(double parentWidth) {
		super();
		
		this.setWidth(this.maxWidthProperty().doubleValue());
		this.setHeight(this.maxHeightProperty().doubleValue());
		
		ProgressBar pb = new ProgressBar();
		pb.setProgress(-1);
		setHalignment(pb, HPos.CENTER);
		
		Label progressLabel = new Label("Loading Image...");
		progressLabel.setStyle("-fx-text-fill: old-pink-light;"
				+ "-fx-font-family: 'Helvetica', Arial, sans-serif;");
		setHalignment(progressLabel, HPos.CENTER);
		
		this.add(progressLabel, 0, 0);
		this.add(pb, 0, 1);
		
		double translateX = (parentWidth/2) - 70;
		this.setStyle("-fx-column-halignment: center; "
				+ "-fx-padding: 200 0 0 " + translateX + ";");
	}
}
