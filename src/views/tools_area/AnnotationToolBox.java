package views.tools_area;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import controllers.GUIController;
import controllers.AnnotationsController;

public class AnnotationToolBox extends BaseToolBox{
	
	private ListView<String> annotationsList = new ListView<String>();
    private ObservableList<String> items = FXCollections.observableArrayList();
	
	public AnnotationToolBox(AnnotationsController controller, ToggleGroup group){
		super("Annotation Tools");
		
		Image imageDraw = new Image(getClass().getResourceAsStream("/resources/pencil112.png"));
		imageDraw.isPreserveRatio();
        ToggleButton drawToolButton = new ToggleButton(null, new ImageView(imageDraw));
        
        Image imageFill = new Image(getClass().getResourceAsStream("/resources/paint3.png"));
        imageFill.isPreserveRatio();
        ToggleButton fillToolButton = new ToggleButton(null, new ImageView(imageFill));

        drawToolButton.setToggleGroup(group);
        fillToolButton.setToggleGroup(group);
        
        this.add(drawToolButton, 0, 1);
        this.add(fillToolButton, 0, 2);
        
        drawToolButton.setOnAction(controller.toggleDrawMode(drawToolButton));
        fillToolButton.setOnAction(controller.toggleFillMode(fillToolButton));
        
        CheckBox hideAnnotationsButton = new CheckBox("Hide all");
        add(hideAnnotationsButton, 0, 3);
        hideAnnotationsButton.setOnAction(controller.toggleAnnotationsVisible(hideAnnotationsButton));
        
        addAnnotationItem("Test");
        annotationsList.autosize();
        annotationsList.setPrefHeight(100);
        this.add(annotationsList, 1,  2);
	}
	
	public void addAnnotationItem(String annotationName){
		items.add(annotationName);
		annotationsList.setItems(items);
	}
}
