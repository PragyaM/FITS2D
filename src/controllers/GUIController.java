package controllers;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckMenuItem;
import javafx.stage.Stage;
import nom.tam.fits.FitsException;
import views.MainWindow;

public class GUIController{
	private MainWindow ui;

	public GUIController(Application app){
		//this is where previously customized configurations should be applied
	}
	
	//TODO handle uncaught exceptions
	public void start(Stage primaryStage) throws FitsException, IOException{
		ui = new MainWindow(primaryStage);
		ui.addTopMenuBar(this);
		ui.addImageViewBox();
		ui.addToolsAreaBox();
		ui.display();
		ui.getImageViewBox().setOnZoom(ui.zoomImage());
	}
	
	public EventHandler<javafx.event.ActionEvent> openFits(){
		return ui.showFitsFileChooser();
	}
	
	public EventHandler<javafx.event.ActionEvent> toggleImageScrollbars(CheckMenuItem toggle){
		return (final ActionEvent e) -> {
			if (toggle.isSelected()) {
				ui.getImageViewBox().enableScrollBars();
			}
			else  ui.getImageViewBox().disableScrollBars();
		};
	}
}
