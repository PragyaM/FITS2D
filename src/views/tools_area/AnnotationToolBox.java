package views.tools_area;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ToggleGroup;
import views.ImageToggleButton;
import controllers.AnnotationsController;

public class AnnotationToolBox extends BaseToolBox{
	
//	private ListView<String> annotationsList = new ListView<String>();
//    private ObservableList<String> items = FXCollections.observableArrayList();
	
	public AnnotationToolBox(AnnotationsController controller, ToggleGroup group){
		super("Annotation Tools");
		
		ImageToggleButton drawToolButton = new ImageToggleButton("/resources/pencil112.png");

        ImageToggleButton fillToolButton = new ImageToggleButton("/resources/paint3.png");

        drawToolButton.setToggleGroup(group);
        fillToolButton.setToggleGroup(group);
        
        this.add(drawToolButton, 0, 1);
        this.add(fillToolButton, 0, 2);
        
        drawToolButton.setOnAction(controller.toggleDrawMode(drawToolButton));
        fillToolButton.setOnAction(controller.toggleFillMode(fillToolButton));
        
        CheckBox hideAnnotationsButton = new CheckBox("Hide all");
        add(hideAnnotationsButton, 1, 1);
        hideAnnotationsButton.setOnAction(controller.toggleAnnotationsVisible(hideAnnotationsButton));
        
//        addAnnotationItem("Test");
//        annotationsList.autosize();
//        annotationsList.setPrefHeight(100);
//        this.add(annotationsList, 1,  1);
	}
	
//	public void addAnnotationItem(String annotationName){
//		items.add(annotationName);
//		annotationsList.setItems(items);
//	}
}
