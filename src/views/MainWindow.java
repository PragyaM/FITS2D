package views;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import views.top_bar_menu.TopMenuBar;
import controllers.GUIController;

public class MainWindow{
	private FitsImageViewBox imageViewBox;
	private TopMenuBar topMenuBar;
	
	private Scene scene;
	private StackPane root;
	private Stage stage;
	
	public MainWindow(Stage primaryStage){
		super();
		
		stage = primaryStage;
		stage.setTitle("FITS Image Viewer");
		stage.setResizable(true);
		try {
			root = new StackPane();
			scene = new Scene(root,Toolkit.getDefaultToolkit().getScreenSize().width, 
					Toolkit.getDefaultToolkit().getScreenSize().height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void display(){
		stage.setScene(scene);
		stage.show();
	}
	
	public EventHandler<javafx.event.ActionEvent> showFitsFileChooser(){
		return (final ActionEvent e) -> {
			//set up file chooser
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select a FITS image file");
			fileChooser.getExtensionFilters().add(
	                new FileChooser.ExtensionFilter("FITS files", "*.fits"));
	                
			//fetch selected file and handle appropriately
			File file = fileChooser.showOpenDialog(stage);
            System.out.println("Opening: " + file.getName() + ".");
            Fits fitsFile;
			try {
				fitsFile = new Fits(file);
				getImageViewBox().addImage(fitsFile);
			} catch (FitsException e2) {
				// TODO Notify user that the selected file is not a FITS file with image data
				e2.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		};
	}
	
	public void addImageViewBox(){
        imageViewBox = new FitsImageViewBox();
        root.getChildren().add(imageViewBox);
        StackPane.setAlignment(imageViewBox, Pos.TOP_CENTER);
        imageViewBox.setVisible(true);
        stage.show();
	}
	
	public void addTopMenuBar(GUIController p){
		topMenuBar = new TopMenuBar(p);
        root.getChildren().add(topMenuBar);
        StackPane.setAlignment(topMenuBar, Pos.TOP_LEFT);
        topMenuBar.setVisible(true);
	}
	
	public FitsImageViewBox getImageViewBox(){
		return imageViewBox;
	}
	
	public TopMenuBar getTopMenuBar(){
		return topMenuBar;
	}

	public EventHandler<? super ZoomEvent> zoomImage() {
		return (final ZoomEvent e) -> {
			double zoomFactor = e.getTotalZoomFactor();
			imageViewBox.setScaleX(zoomFactor);
			imageViewBox.setScaleY(zoomFactor);
		};
	}
}
