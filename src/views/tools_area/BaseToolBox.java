package views.tools_area;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public abstract class BaseToolBox extends GridPane{
	private String heading;
	
	public BaseToolBox(String heading){
		this.heading = heading;
		setPadding(new Insets(10, 15, 10, 15));
		setVgap(5);
		setHgap(5);
		
		Label headingLabel = new Label(heading);
		add(headingLabel, 0, 0);
		headingLabel.setPadding(new Insets(0, 0, 5, 0));
		headingLabel.setId("heading");
		
		//TODO: add help button and write-up
		
		this.setId("tool-box");
	}

}
