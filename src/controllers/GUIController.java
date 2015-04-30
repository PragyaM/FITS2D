package controllers;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.stage.Stage;
import nom.tam.fits.FitsException;
import views.MainWindow;

public class GUIController{
	private MainWindow ui;

	public GUIController(){
		//this is where previously customized configurations should be applied
	}
	
	//TODO handle uncaught exceptions
	public void start(Stage primaryStage) throws FitsException, IOException{
		ui = new MainWindow(primaryStage);
		ui.addTopMenuBar(this);
		ui.addImageViewBox();
		ui.display();
	}
	
	public EventHandler<javafx.event.ActionEvent> openFits(){
		return ui.showFitsFileChooser();
	}
}
