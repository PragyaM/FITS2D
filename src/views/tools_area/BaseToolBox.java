package views.tools_area;

import javafx.scene.layout.GridPane;

public abstract class BaseToolBox extends GridPane{
	private String heading;
	
	public BaseToolBox(String heading){
		this.heading = heading;
		setVgap(5);
		setHgap(5);
	}

}
