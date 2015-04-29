package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;

@SuppressWarnings("serial")
public class ImageView extends JPanel{
	
	private MainWindow parent;
	
	public ImageView(BorderLayout borderLayout, MainWindow parent){
		super(borderLayout);
		this.parent = parent;
		this.setVisible(true);
	}
	
	public Dimension calculateMaximumImageSize(){
		return new Dimension(parent.getWidth(), parent.getHeight() - 50); //TODO: replace 50 with reference to tool-panel height
	}
	
	public void addImage(Fits fitsFile) throws FitsException, IOException{
		JLabel imageLabel = new JLabel();
		FitsImage fitsImage = new FitsImage(fitsFile);
		Dimension maximumImageSize = calculateMaximumImageSize();
		ImageIcon imageIcon = new ImageIcon(fitsImage.getScaledImage(maximumImageSize));
		
	    //add image to the view
		imageLabel.setIcon(imageIcon);
		imageLabel.setSize(new Dimension(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
		
		this.add(imageLabel, BorderLayout.CENTER);
		this.setBackground(Color.WHITE);
	}
}
