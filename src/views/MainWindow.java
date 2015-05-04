package views;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
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
	private GridPane root;
	private Stage stage;
	
	public MainWindow(Stage primaryStage){
		super();
		
		stage = primaryStage;
		stage.setTitle("FITS Image Viewer");
		stage.setResizable(true);
		try {
			root = new GridPane();
			root.setHgap(10);
			root.setVgap(10);
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
            System.out.println("Opening: " + file.getName());
            Fits fitsFile;
			try {
				fitsFile = new Fits(file);
				getImageViewBox().addImage(fitsFile);
				getImageViewBox().setVisible(true);
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
        Group g1 = new Group();
        g1.getChildren().add(imageViewBox);
        root.add(imageViewBox, 0, 2);
        GridPane.setValignment(g1, VPos.CENTER);
		imageViewBox.setPrefSize(scene.getWidth(), scene.getHeight()/1.5);
        stage.show();
	}
	
	public void addTopMenuBar(GUIController p){
		topMenuBar = new TopMenuBar(p);
        root.getChildren().add(new Pane(topMenuBar));
        GridPane.setValignment(topMenuBar, VPos.TOP);
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
			double zoomFactor = e.getZoomFactor();
			Scale scale = new Scale();
			scale.setX(zoomFactor);
			scale.setY(zoomFactor);
			imageViewBox.getImageView().getTransforms().add(scale);
		};
	}
}
