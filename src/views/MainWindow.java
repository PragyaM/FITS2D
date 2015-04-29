package views;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;

import nom.tam.fits.FitsException;
import views.top_bar_menu.TopBarMenu;
import controllers.GUIController;

@SuppressWarnings("serial")
public class MainWindow extends JFrame{
	private ImageView imageView;
	private TopBarMenu topBarMenu;
	
	public MainWindow(){
		super();
		this.setTitle("FITS Viewer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
	}
	
	//TODO handle uncaught exceptions
	public void addImageViewPanel() throws FitsException, IOException{
        imageView = new ImageView(new BorderLayout(), this);
        this.add(imageView, BorderLayout.CENTER);
	}
	
	public void addTopBarMenu(GUIController p){
		topBarMenu = new TopBarMenu(p);
        setJMenuBar(topBarMenu);
	}
	
	public ImageView getImageViewPanel(){
		return imageView;
	}
	
	public TopBarMenu getTopBarMenu(){
		return topBarMenu;
	}
}
