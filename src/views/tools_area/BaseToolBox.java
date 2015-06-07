package views.tools_area;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public abstract class BaseToolBox extends GridPane{
	private String heading;
	
	public BaseToolBox(String heading){
		this.heading = heading;
		setVgap(5);
		setHgap(5);
		
		add(new Label(heading), 0, 0);
		
		this.setStyle("-fx-border: 2px solid; -fx-border-color: orange;");
	}

}
