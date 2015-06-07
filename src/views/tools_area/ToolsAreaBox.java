package views.tools_area;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import controllers.GUIController;

public class ToolsAreaBox extends GridPane {
	
	public ToolsAreaBox(GUIController parent){
		super();
		setVgap(5);
		setHgap(5);
		setPadding(new Insets(10, 15, 10, 15));
//		add(new ColourToolBox(parent), 0, 1);
		add(new AnnotationToolBox(parent), 1, 1);
//		add(new MaskToolBox(parent), 2, 1);
	}
	
}
