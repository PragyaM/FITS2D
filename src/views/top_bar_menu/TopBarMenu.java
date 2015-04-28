package views.top_bar_menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controllers.GUIController;

@SuppressWarnings("serial")
public class TopBarMenu extends JMenuBar{
	
	public TopBarMenu(GUIController p){
		JMenu fileMenu = new JMenu("File");
		
		final JMenuItem openFits = new JMenuItem("Open FITS file");
		openFits.addActionListener(p);
		
		fileMenu.add(openFits);
		this.add(fileMenu);
	}
	
}
