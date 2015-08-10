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

	public GUIController(Application app){
		//this is where previously customized configurations should be applied
	}

	//TODO handle uncaught exceptions
	public void start(Stage primaryStage) throws FitsException, IOException{
		ui = new MainWindow(primaryStage, this);
		annotationsController = new AnnotationsController(ui);
		ui.addTopMenuBar(this);
		ui.addImageViewBox();
		ui.addToolsAreaBox(this);
		ui.display();
		ui.getImageViewBox().setOnZoom(ui.zoomImage(this));
	}
	
	public AnnotationsController getAnnotationsController(){
		return annotationsController;
	}

	public EventHandler<javafx.event.ActionEvent> openFits(){
		return (final ActionEvent e) -> {
			//set up file chooser
			File file = ui.openFile("Select a FITS image file", "FITS");
            Fits fitsFile;
			try {
				fitsFile = new Fits(file);
				ui.getImageViewBox().addImage(fitsFile);
				ui.getImageViewBox().setVisible(true);
				annotationsController.initialise(this, ui.getImageViewBox().getAnnotationLayer(), ui.getImageViewBox());
			} catch (FitsException e2) {
				// TODO Notify user that the selected file is not a FITS file with image data
				System.out.println(e2.getMessage());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Cancelled.");
			}
		};
	}

	public EventHandler<javafx.event.ActionEvent> toggleImageScrollbars(CheckMenuItem toggle){
		return (final ActionEvent e) -> {
			if (toggle.isSelected()) {
				ui.getImageViewBox().enableScrollBars();
			}
			else  ui.getImageViewBox().disableScrollBars();
		};
	}
	
	public void handle(Exception e) {
		ui.displayMessage(e.getMessage());
	}

	public EventHandler<ActionEvent> changeNanColour() {
		// TODO Auto-generated method stub
		return (final ActionEvent e) -> {
			Color nanColour = ((ColorPicker) e.getSource()).getValue();
			try {
				ui.getImageViewBox().getFitsImage().setNanColour(nanColour);
				refreshImage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		};
	}
	
	public void refreshImage(){
		try {
			ui.getImageViewBox().getFitsImage().writeImage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ui.getImageViewBox().refreshImage();
	}

	public Color getNanColour() {
		// TODO Auto-generated method stub
		return ui.getToolsAreaBox().getColourToolBox().getNanColourPickerColour();
	}
}
