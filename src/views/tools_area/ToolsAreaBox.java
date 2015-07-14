package views.tools_area;

import javafx.geometry.Insets;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import controllers.GUIController;

public class ToolsAreaBox extends GridPane {
	
	private ColourToolBox colourTools;
	
	public ToolsAreaBox(GUIController parent){
		super();
		setVgap(5);
		setHgap(5);
		setPadding(new Insets(10, 15, 10, 15));
		ToggleGroup group = new ToggleGroup();
		
		colourTools = new ColourToolBox(parent, group);
		add(colourTools, 0, 1);
		add(new AnnotationToolBox(parent, group), 1, 1);
		add(new MaskToolBox(parent, group), 2, 1);
	}
	
	public ColourToolBox getColourToolBox(){
		return colourTools;
	}
	
}
