package views.tools_area;


import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import controllers.ImageController;

public class ImageToolBox extends BaseToolBox{
	
	private Button zoomOutButton;
	private Button zoomInButton;
	private Label zoomLabel;
	private ImageController controller;

	public ImageToolBox(ImageController controller) {
		super("Image");
		
		this.controller = controller;
		
		GridPane zoomGroup = new GridPane();
		Label zoomPrompt = new Label("Zoom: ");
		zoomOutButton = new Button("-");
		zoomInButton = new Button("+");
		zoomLabel = new Label("100%");
		
		zoomOutButton.setOnAction(controller.zoomOut());
		zoomInButton.setOnAction(controller.zoomIn());
		zoomInButton.setOnAction(controller.zoomIn());
		
		zoomGroup.add(zoomPrompt, 0, 0);
		zoomGroup.add(zoomOutButton, 1, 0);
		zoomGroup.add(zoomLabel, 2, 0);
		zoomGroup.add(zoomInButton, 3, 0);
		
		add(zoomGroup, 0, 1);
	}

	public void setZoomOutButtonDisabled(boolean disable){
		this.zoomOutButton.setDisable(disable);
	}
	
	public void setZoomLabel(String zoomText){
		this.zoomLabel.setText(zoomText);
	}

}
