package views.tools_area;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import controllers.GUIController;

public class AnnotationToolBox extends BaseToolBox{
	
	public AnnotationToolBox(GUIController controller){
		super("Annotation Tools");
		
        ToggleButton drawToolButton = new ToggleButton("Annotation Draw Tool");
        ToggleButton fillToolButton = new ToggleButton("Annotation Bucket Tool");
		ToggleButton drawMaskToolButton = new ToggleButton("Mask Draw Tool");
		ToggleButton fillMaskToolButton = new ToggleButton("Mask Bucket Tool");
		Button createMaskButton = new Button("Create Mask From Selection");
        
        ToggleGroup group = new ToggleGroup();
        drawToolButton.setToggleGroup(group);
        fillToolButton.setToggleGroup(group);
		drawMaskToolButton.setToggleGroup(group);
		fillMaskToolButton.setToggleGroup(group);
        
        this.add(drawToolButton, 0, 1);
        this.add(fillToolButton, 1, 1);
		this.add(drawMaskToolButton, 4, 1);
		this.add(fillMaskToolButton, 5, 1);
		this.add(createMaskButton, 6, 1);
        
        drawToolButton.setOnAction(controller.toggleDrawMode(drawToolButton));
        fillToolButton.setOnAction(controller.toggleFillMode(fillToolButton));
		drawMaskToolButton.setOnAction(controller.toggleMaskDrawMode(drawMaskToolButton));
		fillMaskToolButton.setOnAction(controller.toggleMaskFillMode(fillMaskToolButton));
		createMaskButton.setOnAction(controller.createMaskFromSelection());
        
        CheckBox hideAnnotationsButton = new CheckBox("Hide Annotations");
        add(hideAnnotationsButton, 0, 2);
        hideAnnotationsButton.setOnAction(controller.toggleAnnotationsVisible(hideAnnotationsButton));
	}
}
