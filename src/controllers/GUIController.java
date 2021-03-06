package controllers;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import nom.tam.fits.FitsException;
import views.MainWindow;

public class GUIController{
	private MainWindow ui;
	private FitsCanvasController fitsCanvasController;
	private ImageController imageController;

	public GUIController() {

	}

	public GUIController(Application app){
		//this is where previously customized configurations should be applied
	}

	//TODO handle uncaught exceptions
	public void start(Stage primaryStage) throws FitsException, IOException{
		ui = new MainWindow(primaryStage, this);
		imageController = new ImageController(ui);
		fitsCanvasController = new FitsCanvasController(ui, imageController);
		imageController.getImageViewBox().addCanvas(fitsCanvasController.getCanvas());
		ui.addTopMenuBar(this);
		ui.addToolsAreaBox(this);
		ui.display();
	}

	public FitsCanvasController getFitsCanvasController(){
		return fitsCanvasController;
	}
	
	public AnnotationsController getAnnotationsController(){
		return fitsCanvasController.getAnnotationsController();
	}
	
	public SelectionsController getSelectionsController(){
		return fitsCanvasController.getSelectionsController();
	}

	public ImageController getImageController(){
		return imageController;
	}

	public EventHandler<javafx.event.ActionEvent> openFits(){
		return (final ActionEvent e) -> {
			//set up file chooser
			File file = ui.openFile("Select a FITS image file", "FITS");
			imageController.addImageFromFile(file, fitsCanvasController);

			e.consume();
		};
	}

	public void handle(Exception e) {
		ui.displayMessage(e.getMessage());
	}
}
