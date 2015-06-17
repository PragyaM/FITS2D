package views.tools_area;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import controllers.GUIController;

public class AnnotationToolBox extends BaseToolBox{
	
	public AnnotationToolBox(GUIController controller, ToggleGroup group){
		super("Annotation Tools");
		
        ToggleButton drawToolButton = new ToggleButton("Annotation Draw Tool");
        ToggleButton fillToolButton = new ToggleButton("Annotation Bucket Tool");

        drawToolButton.setToggleGroup(group);
        fillToolButton.setToggleGroup(group);
        
        this.add(drawToolButton, 0, 1);
        this.add(fillToolButton, 1, 1);
        
        drawToolButton.setOnAction(controller.toggleDrawMode(drawToolButton));
        fillToolButton.setOnAction(controller.toggleFillMode(fillToolButton));
        
        CheckBox hideAnnotationsButton = new CheckBox("Hide Annotations");
        add(hideAnnotationsButton, 0, 2);
        hideAnnotationsButton.setOnAction(controller.toggleAnnotationsVisible(hideAnnotationsButton));
	}
}
