package views.tools_area;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import controllers.GUIController;

public class AnnotationToolBox extends BaseToolBox{
	
	private GUIController app;
	
	public AnnotationToolBox(GUIController app){
		super("Annotation Tools");
		this.app = app;
		
        ToggleButton drawToolButton = new ToggleButton("Draw Tool");
        ToggleButton fillToolButton = new ToggleButton("Bucket Tool");
        
        ToggleGroup group = new ToggleGroup();
        drawToolButton.setToggleGroup(group);
        fillToolButton.setToggleGroup(group);
        
        this.add(drawToolButton, 2, 1);
        this.add(fillToolButton, 3, 1);
        
        drawToolButton.setOnAction(app.toggleDrawMode(drawToolButton));
        fillToolButton.setOnAction(app.toggleFillMode(fillToolButton));
        
        CheckBox hideAnnotationsButton = new CheckBox();
        add(hideAnnotationsButton, 2, 2);
        add(new Label("Hide Annotations"), 3, 2);
        hideAnnotationsButton.setOnAction(app.toggleAnnotationsVisible(hideAnnotationsButton));
	}
}
