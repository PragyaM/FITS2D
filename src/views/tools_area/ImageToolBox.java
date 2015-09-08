package views.tools_area;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import controllers.ImageController;

public class ImageToolBox extends BaseToolBox{
	
	private Button zoomOutButton;
	private Button zoomInButton;
	private Label zoomLabel;

	public ImageToolBox(ImageController controller) {
		super("Image");
		
		Label zoomPrompt = new Label("Zoom: ");
		zoomOutButton = new Button("-");
		zoomInButton = new Button("+");
		zoomLabel = new Label("100%");
		
		zoomOutButton.setOnAction(controller.zoomOut());
		zoomInButton.setOnAction(controller.zoomIn());
		zoomInButton.setOnAction(controller.zoomIn());
		
		add(zoomPrompt, 0, 1);
		add(zoomOutButton, 1, 1);
		add(zoomLabel, 2, 1);
		add(zoomInButton, 3, 1);
	}

	public void setZoomOutButtonDisabled(boolean disable){
		this.zoomOutButton.setDisable(disable);
	}
	
	public void setZoomLabel(String zoomText){
		this.zoomLabel.setText(zoomText);
	}

}
