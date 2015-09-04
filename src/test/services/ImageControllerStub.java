package test.services;

import javafx.stage.Stage;
import views.FitsImageViewBox;
import views.MainWindow;
import controllers.GUIController;
import controllers.ImageController;

public class ImageControllerStub extends ImageController {
	public ImageControllerStub() {
		
		super(new MainWindow(new Stage(), new GUIController()));
	}
	
	public FitsImageViewBox getImageViewBox(){
		return this.imageViewBox;
	}
}
