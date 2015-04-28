package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controllers.MainController;
import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;

@SuppressWarnings("serial")
public class ImageView extends JPanel{
	
	private Fits fitsFile;
	
	public ImageView(BorderLayout borderLayout, Fits fitsFile){
		super(borderLayout);
		this.fitsFile = fitsFile;
        calculateImageSize();
		this.setVisible(true);
	}
	
	public Dimension calculateImageSize(){
		return new Dimension(MainController.SCREEN_SIZE.width/2, MainController.SCREEN_SIZE.height/2);
	}
	
	public void setUpImage() throws FitsException, IOException{
		JLabel imageLabel = new JLabel();
		FitsImage fitsImage = new FitsImage(fitsFile);
		Dimension requiredImageSize = calculateImageSize();
		ImageIcon imageIcon = new ImageIcon(fitsImage.getScaledImage(requiredImageSize));
		
	    //add image to the view
		imageLabel.setIcon(imageIcon);
		imageLabel.setSize(new Dimension(imageIcon.getIconWidth()/2, imageIcon.getIconHeight()/2));
		
		this.add(imageLabel);
		this.setBackground(Color.WHITE);
	}
}
