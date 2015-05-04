package controllers;

import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import nom.tam.fits.FitsException;

public class Main extends Application{
	
	//TODO handle uncaught exception
	@Override
	public void start(Stage primaryStage) throws FitsException, IOException {
		GUIController guiController = new GUIController(this);
		guiController.start(primaryStage);
	}
	
	public static void main(String[] args) throws FitsException, IOException{
		launch(args);
	}
}
