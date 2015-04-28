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
	
	public ImageView(BorderLayout borderLayout){
		super(borderLayout);
        calculateImageSize();
		this.setVisible(true);
	}
	
	public Dimension calculateImageSize(){
		return new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/2, 
				Toolkit.getDefaultToolkit().getScreenSize().height/2);
	}
	
	public void addImage(Fits fitsFile) throws FitsException, IOException{
		JLabel imageLabel = new JLabel();
		FitsImage fitsImage = new FitsImage(fitsFile);
		Dimension requiredImageSize = calculateImageSize();
		ImageIcon imageIcon = new ImageIcon(fitsImage.getScaledImage(requiredImageSize));
		
	    //add image to the view
		imageLabel.setIcon(imageIcon);
		imageLabel.setSize(new Dimension(imageIcon.getIconWidth()/2, imageIcon.getIconHeight()/2));
		
		this.add(imageLabel, BorderLayout.CENTER);
		this.setBackground(Color.WHITE);
	}
}
