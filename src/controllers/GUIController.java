package controllers;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import views.MainWindow;

public class GUIController{
	private MainWindow ui;
	private AnnotationsController annotationsController;
	private ImageController imageController;

	public GUIController(Application app){
		//this is where previously customized configurations should be applied
	}

	//TODO handle uncaught exceptions
	public void start(Stage primaryStage) throws FitsException, IOException{
		ui = new MainWindow(primaryStage, this);
		imageController = new ImageController(ui);
		annotationsController = new AnnotationsController(ui);
		ui.addTopMenuBar(this);
		ui.addToolsAreaBox(this);
		ui.display();
	}
	
	public AnnotationsController getAnnotationsController(){
		return annotationsController;
	}
	
	public ImageController getImageController(){
		return imageController;
	}

	public EventHandler<javafx.event.ActionEvent> openFits(){
		return (final ActionEvent e) -> {
			//set up file chooser
			File file = ui.openFile("Select a FITS image file", "FITS");
            Fits fitsFile;
			try {
				fitsFile = new Fits(file);
				imageController.addImage(fitsFile);
				annotationsController.initialise(ui.getImageViewBox());
			} catch (FitsException e2) {
				// TODO Notify user that the selected file is not a FITS file with image data
				System.out.println(e2.getMessage());
			}
		};
	}
	
	public void handle(Exception e) {
		ui.displayMessage(e.getMessage());
	}
}
