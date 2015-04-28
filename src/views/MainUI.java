package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;

import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;

@SuppressWarnings("serial")
public class MainUI extends JFrame{
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private ImageView imageView;
	private Fits fitsFile;
	
	public MainUI() throws FitsException, IOException{
		this.setTitle("FITS Viewer");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(screenSize);
        
        fitsFile = new Fits("69_scaled_tin69.fits", false);
        imageView = new ImageView(new BorderLayout(), fitsFile);
        imageView.setUpImage();
        
        this.add(imageView);
		this.setVisible(true);
	}
}
