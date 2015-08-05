package views;

import java.awt.Toolkit;
import java.io.File;

import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import views.tools_area.ToolsAreaBox;
import views.top_bar_menu.TopMenuBar;
import controllers.GUIController;

public class MainWindow{
	private FitsImageViewBox imageViewBox;
	private TopMenuBar topMenuBar;
	private ToolsAreaBox toolsArea;
	
	private Scene scene;
	private GridPane root;
	private Stage stage;
	private GUIController controller;
	
	public MainWindow(Stage primaryStage, GUIController controller){
		super();
		this.controller = controller;
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
	
	public GUIController getController() {
		return controller;
	}
	
	public File openFile(String title, String type) {
			//set up file chooser
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle(title);
			if (!type.isEmpty()){
				fileChooser.getExtensionFilters().add(
		                new FileChooser.ExtensionFilter(type + " files", "*." + type.toLowerCase()));
			}
	                
			//fetch selected file and handle appropriately
			File file = fileChooser.showOpenDialog(stage);
	        System.out.println("Opening: " + file.getName());
			return file;
	}
	
	public void addImageViewBox(){
        imageViewBox = new FitsImageViewBox(this);
        Group g1 = new Group();
        g1.getChildren().add(imageViewBox);
        
//        TabPane imageLayerControl = new TabPane();
//        Tab tab = new Tab();
//        tab.setText("new tab");
//        imageLayerControl.getTabs().add(tab);
//        g1.getChildren().add(imageLayerControl);
        
        root.add(imageViewBox, 0, 3);
//        root.add(imageLayerControl, 0, 2);
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
	
	public void addToolsAreaBox(GUIController p){
		toolsArea = new ToolsAreaBox(p);
		root.add(toolsArea, 0, 4);
		GridPane.setValignment(toolsArea, VPos.BASELINE);
		toolsArea.setVisible(true);
	}
	
	public FitsImageViewBox getImageViewBox(){
		return imageViewBox;
	}
	
	public TopMenuBar getTopMenuBar(){
		return topMenuBar;
	}
	
	public ToolsAreaBox getToolsAreaBox(){
		return toolsArea;
	}

	public EventHandler<? super ZoomEvent> zoomImage(GUIController app) {
		return (final ZoomEvent e) -> {
			double zoomFactor = e.getZoomFactor();
			double pivotX = e.getX();
			double pivotY = e.getY();
			Scale scale = new Scale(zoomFactor, zoomFactor, pivotX, pivotY); //FIXME setting a pivot point appears to have no effect
			imageViewBox.getImageView().getTransforms().add(scale);
			imageViewBox.getAnnotationLayer().getTransforms().add(scale);
			imageViewBox.getAnnotationLayer().getGraphicsContext2D().scale(zoomFactor, zoomFactor);
		};
	}

	public File showSaveDialog(String type) {
		FileChooser fileChooser = new FileChooser();
		  
        //Set extension filter
		String ext = "*." + type.toLowerCase();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(type + " files (" + ext + ")", ext);
        fileChooser.getExtensionFilters().add(extFilter);
        
        //Show save file dialog
        File file = fileChooser.showSaveDialog(stage);
        
        if(file != null){
            return file;
        }
		return null;
	}

	public void displayMessage(String message) {
		Stage dialog = new Stage();
		dialog.initStyle(StageStyle.UTILITY);
		Scene scene = new Scene(new Group(new Text(25, 25, message)));
		dialog.setScene(scene);
		dialog.show();
	}
}
