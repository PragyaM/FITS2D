package views.tools_area;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import views.ImageToggleButton;
import controllers.GUIController;

public class RegionExtractionToolBox extends BaseToolBox{
	
	public RegionExtractionToolBox(GUIController controller, ToggleGroup group){
		super("Region Extraction Tools");
        
		ImageToggleButton drawToolButton = new ImageToggleButton("/resources/pencil112.png");

        ImageToggleButton fillToolButton = new ImageToggleButton("/resources/paint3.png");
		
		Button extractRegionButton = new Button("Extract selection to FITS");
		
		drawToolButton.setToggleGroup(group);
		fillToolButton.setToggleGroup(group);
		
		this.add(drawToolButton, 0, 1);
		this.add(fillToolButton, 0, 2);
		this.add(extractRegionButton, 1, 1);
		
		drawToolButton.setOnAction(controller.getAnnotationsController().toggleMaskDrawMode(drawToolButton));
		fillToolButton.setOnAction(controller.getAnnotationsController().toggleMaskFillMode(fillToolButton));
		extractRegionButton.setOnAction(controller.getAnnotationsController().createMaskFromSelection());
	}
}
