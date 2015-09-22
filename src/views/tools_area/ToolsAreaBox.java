package views.tools_area;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import controllers.GUIController;

public class ToolsAreaBox extends GridPane {
	
	private ColourToolBox colourTools;
	private ImageToolBox imageTools;
	private AnnotationToolBox annotationTools;
	private RegionExtractionToolBox regionExtractionTools;
	private HistogramToolBox histogramTools;

	public ToolsAreaBox(GUIController controller){
		super();
		setVgap(5);
		setHgap(5);
		setPadding(new Insets(10, 15, 10, 15));
		ToggleGroup group = new ToggleGroup();
		
		
		colourTools = new ColourToolBox(controller.getImageController(), group);
		imageTools = new ImageToolBox(controller.getImageController());
		annotationTools = new AnnotationToolBox(controller.getFitsCanvasController(), group);
		regionExtractionTools = new RegionExtractionToolBox(controller.getFitsCanvasController(), group);
		histogramTools = new HistogramToolBox(controller.getImageController());
		
		GridPane box = new GridPane();
		box.add(imageTools, 0, 0);
		box.add(colourTools, 0, 1);
		box.setVgap(5);
		
		add(box, 0, 0);
		
		add(annotationTools, 1, 0);
		add(regionExtractionTools, 2, 0);
		
		GridPane histBox = new GridPane();
		histBox.add(histogramTools, 0, 0);
		
		add(histBox, 3, 0);
		
		this.setId("tool-box-area");
	}
	
	public ColourToolBox getColourToolBox(){
		return colourTools;
	}
	
	public ImageToolBox getImageToolBox(){
		return imageTools;
	}
	
	public AnnotationToolBox getAnnotationToolBox(){
		return annotationTools;
	}
	
	public RegionExtractionToolBox getRegionExtractionToolBox(){
		return regionExtractionTools;
	}
	
	public HistogramToolBox getHistogramToolBox() {
		return histogramTools;
	}
	
}
