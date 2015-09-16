package views.tools_area;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import views.ImageToggleButton;
import controllers.FitsCanvasController;

public class RegionExtractionToolBox extends BaseToolBox{
	
	private Button undoButton;
	
	public RegionExtractionToolBox(FitsCanvasController controller, ToggleGroup group){
		super("Region Extraction");
        
		ImageToggleButton drawToolButton = new ImageToggleButton("/resources/pencil112.png");

        ImageToggleButton fillToolButton = new ImageToggleButton("/resources/paint3.png");
		
		Button extractRegionButton = new Button("Extract selection\nto FITS");
		
		undoButton = new Button("Undo");
		
		drawToolButton.setToggleGroup(group);
		fillToolButton.setToggleGroup(group);
		
		this.add(drawToolButton, 0, 1);
		this.add(fillToolButton, 0, 2);
		this.add(extractRegionButton, 1, 1);
		this.add(undoButton, 1, 2);
		
		drawToolButton.setOnAction(controller.getSelectionsController().toggleDrawMode(drawToolButton));
		fillToolButton.setOnAction(controller.getSelectionsController().toggleFillMode(fillToolButton));
		extractRegionButton.setOnAction(controller.createMaskFromSelection());
		undoButton.setOnAction(controller.undoSelectionStroke());
	}
	
	public void setUndoButtonDisabled(boolean disable){
		undoButton.setDisable(disable);
	}
}
