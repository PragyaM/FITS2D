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
		
//		ToggleButton viewMode = new ToggleButton("viewing");
        ToggleButton drawToolButton = new ToggleButton("Draw Tool");
        ToggleButton floodFillButton = new ToggleButton("Bucket Tool");
        
        ToggleGroup group = new ToggleGroup();
//        viewMode.setToggleGroup(group);
        drawToolButton.setToggleGroup(group);
        floodFillButton.setToggleGroup(group);
        
//        this.add(viewMode, 1, 1);
        this.add(drawToolButton, 2, 1);
        this.add(floodFillButton, 3, 1);
        
//        viewMode.setOnMouseClicked(app.setImageViewMode());
        drawToolButton.setOnAction(app.toggleDrawMode(drawToolButton));
        floodFillButton.setOnAction(app.toggleDrawMode(null));
        
        CheckBox hideAnnotationsButton = new CheckBox();
        add(hideAnnotationsButton, 2, 2);
        add(new Label("Hide Annotations"), 3, 2);
        hideAnnotationsButton.setOnAction(app.toggleAnnotationsVisible(hideAnnotationsButton));
	}
}
