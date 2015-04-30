package views.top_bar_menu;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import controllers.GUIController;

public class TopMenuBar extends MenuBar{
	
	public TopMenuBar(GUIController p){
		Menu fileMenu = new Menu("File");
		MenuItem openFits = new MenuItem("Open FITS file");
		fileMenu.getItems().add(openFits);
		openFits.setOnAction(p.openFits());
		this.getMenus().add(fileMenu);
	}
	
}
