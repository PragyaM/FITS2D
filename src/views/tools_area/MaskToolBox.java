package views.tools_area;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import controllers.GUIController;

public class MaskToolBox extends BaseToolBox{
	
	private GUIController app;
	
	public MaskToolBox(GUIController app){
		super("Masking Tools");
		this.app = app;

//		ToggleButton drawToolButton = new ToggleButton("Draw Tool");
//		ToggleButton fillToolButton = new ToggleButton("Bucket Tool");
//
//		ToggleGroup group = new ToggleGroup();
//		drawToolButton.setToggleGroup(group);
//		fillToolButton.setToggleGroup(group);
//
//		this.add(drawToolButton, 2, 1);
//		this.add(fillToolButton, 3, 1);
//
//		drawToolButton.setOnAction(app.toggleDrawMode(drawToolButton));
//		fillToolButton.setOnAction(app.toggleFillMode(fillToolButton));
	}
}
