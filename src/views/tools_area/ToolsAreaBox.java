package views.tools_area;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;

public class ToolsAreaBox extends GridPane {
	
	public ToolsAreaBox(){
		super();
		setVgap(5);
		setHgap(5);
		setPadding(new Insets(10, 15, 10, 15));
		add(new ColourToolBox(), 0, 1);
	}
	
}
