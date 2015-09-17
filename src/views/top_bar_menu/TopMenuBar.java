package views.top_bar_menu;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import controllers.GUIController;

public class TopMenuBar extends MenuBar{
	
	GUIController app;
	
	public TopMenuBar(GUIController app){
		super();
		useSystemMenuBarProperty().set(true);
		this.app = app;
		
		addFileMenu();
		addImageMenu();
		addAnnotationMenu();
		addExtractionMenu();
	}
	
	private void addFileMenu(){
		Menu fileMenu = new Menu("File");
		
		MenuItem openFits = new MenuItem("Open FITS file");
		fileMenu.getItems().add(openFits);
		openFits.setOnAction(app.openFits());
		
		this.getMenus().add(fileMenu);
	}
	
	private void addAnnotationMenu(){
		Menu annotationMenu = new Menu("Annotation");
		
		MenuItem saveAnnotations = new MenuItem("Save Annotation");
		annotationMenu.getItems().add(saveAnnotations);
		saveAnnotations.setOnAction(app.getAnnotationsController().save());
		
		MenuItem openAnnotations = new MenuItem("Open Annotation");
		annotationMenu.getItems().add(openAnnotations);
		openAnnotations.setOnAction(app.getAnnotationsController().open());
		
		this.getMenus().add(annotationMenu);
	}
	
	private void addExtractionMenu(){
		Menu extractionMenu = new Menu("Extraction");
		
		MenuItem saveSelection = new MenuItem("Save Selection");
		extractionMenu.getItems().add(saveSelection);
		saveSelection.setOnAction(app.getSelectionsController().save());
		
		MenuItem openSelection = new MenuItem("Open Selection");
		extractionMenu.getItems().add(openSelection);
		openSelection.setOnAction(app.getSelectionsController().open());
		
		this.getMenus().add(extractionMenu);
	}
	
	private void addImageMenu(){
		Menu imageMenu = new Menu("Image");
		
		CheckMenuItem toggleImageScroll = new CheckMenuItem("Show scrollbars");
		imageMenu.getItems().add(toggleImageScroll);
		toggleImageScroll.setOnAction(app.getImageController().toggleImageScrollbars(toggleImageScroll));
		
		this.getMenus().add(imageMenu);
	}
	
}
