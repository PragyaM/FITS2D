package views.top_bar_menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import controllers.GUIController;

public class TopMenuBar extends MenuBar{
	
	GUIController app;
	
	public TopMenuBar(GUIController app){
		super();
		useSystemMenuBarProperty().set(true);
		this.app = app;
		
		addFileMenu();
		addWindowMenu();
	}
	
	private void addFileMenu(){
		Menu fileMenu = new Menu("File");
		MenuItem openFits = new MenuItem("Open FITS file");
		fileMenu.getItems().add(openFits);
		openFits.setOnAction(app.openFits());
		this.getMenus().add(fileMenu);
	}
	
	private void addWindowMenu(){
		Menu windowMenu = new Menu("Window");
		MenuItem toggleImageScroll = new MenuItem("Show image scrollbars");
		windowMenu.getItems().add(toggleImageScroll);
		toggleImageScroll.setOnAction(app.setImageScrollBarsEnabled(true));
		this.getMenus().add(windowMenu);
	}
	
}
