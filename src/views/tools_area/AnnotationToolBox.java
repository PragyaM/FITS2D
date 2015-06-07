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
		
        ToggleButton drawToolButton = new ToggleButton("Annotation Draw Tool");
        ToggleButton fillToolButton = new ToggleButton("Annotation Bucket Tool");
		ToggleButton drawMaskToolButton = new ToggleButton("Mask Draw Tool");
		ToggleButton fillMaskToolButton = new ToggleButton("Mask Bucket Tool");
        
        ToggleGroup group = new ToggleGroup();
        drawToolButton.setToggleGroup(group);
        fillToolButton.setToggleGroup(group);
		drawMaskToolButton.setToggleGroup(group);
		fillMaskToolButton.setToggleGroup(group);
        
        this.add(drawToolButton, 2, 1);
        this.add(fillToolButton, 3, 1);
		this.add(drawMaskToolButton, 4, 1);
		this.add(fillMaskToolButton, 5, 1);
        
        drawToolButton.setOnAction(app.toggleDrawMode(drawToolButton));
        fillToolButton.setOnAction(app.toggleFillMode(fillToolButton));
		drawMaskToolButton.setOnAction(app.toggleMaskDrawMode(drawMaskToolButton));
		fillMaskToolButton.setOnAction(app.toggleMaskFillMode(fillMaskToolButton));
        
        CheckBox hideAnnotationsButton = new CheckBox("Hide Annotations");
        add(hideAnnotationsButton, 2, 2);
        hideAnnotationsButton.setOnAction(app.toggleAnnotationsVisible(hideAnnotationsButton));
	}
}
