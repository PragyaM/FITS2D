package views.tools_area;

import javafx.geometry.Insets;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import controllers.GUIController;

public class ToolsAreaBox extends GridPane {
	
	private ColourToolBox colourTools;
	private ImageToolBox imageTools;
	
	public ToolsAreaBox(GUIController controller){
		super();
		setVgap(5);
		setHgap(5);
		setPadding(new Insets(10, 15, 10, 15));
		ToggleGroup group = new ToggleGroup();
		
		
		colourTools = new ColourToolBox(controller.getImageController(), group);
		imageTools = new ImageToolBox(controller.getImageController());
		
		GridPane box = new GridPane();
		box.add(imageTools, 0, 0);
		box.add(colourTools, 0, 1);
		box.setVgap(5);
		
		add(box, 0, 1);
		
		add(new AnnotationToolBox(controller.getAnnotationsController(), group), 1, 1);
		add(new RegionExtractionToolBox(controller, group), 2, 1);
		
		this.setId("tool-box-area");
	}
	
	public ColourToolBox getColourToolBox(){
		return colourTools;
	}
	
	public ImageToolBox getImageToolBox(){
		return imageTools;
	}
	
}
