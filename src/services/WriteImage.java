package services;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;

import nom.tam.fits.ImageHDU;

public class WriteImage extends Task<Image> {
	
	private final int width;
	private final int height;
	private final double[] imageData;
	private final ImageHDU hdu;
	private final Color nanColour;
	private Image image;
	
	public WriteImage(int w, int h, double[] data, ImageHDU imghdu, Color nanCol){
		width = w;
		height = h;
		imageData = data;
		hdu = imghdu;
		nanColour = nanCol;
		
		
	}

	@Override
	protected Image call() throws Exception {
BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		WritableRaster raster = im.getRaster();
		setImageColours(imageData, raster);
		
		//convert image to a format that can be displayed
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(im, "png", out);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

		image = new Image(in);
		return image;
	}
	
	
	public Image getImage(){
		return image;
	}
	
	private void setImageColours(double[] imageData, WritableRaster raster){
		double max = hdu.getMaximumValue();
		double cutOff = max * 0.025;
		
		int colBandRange = 250;
		double segmentSize = (double) (cutOff/colBandRange);

		for (int i = 0; i < imageData.length; i++){
			double val = (double) (imageData[i]);
			int x = i % width;
			int y = (int) Math.ceil(i / width);
			if (isNaN(val) || val <= 0) {
				raster.setPixel(x, y, new double[]{nanColour.getRed()*250, nanColour.getGreen()*250, nanColour.getBlue()*250, nanColour.getOpacity()*250});
			} 
			else if (val > cutOff){
				raster.setPixel(x, y, new double[]{250, 250, 250,250});
			}
			else {
				double colVal = val/segmentSize;
				raster.setPixel(x, y, new double[]{colVal, colVal, colVal, 250});
			}
		}
	}
	
	private boolean isNaN(double d){
		return d != d;
	}
	
	/**
	 * @param wavelength - expected to be within the 380 to 780 range (where visible colors can be resolved).
	 * @return a Color containing calculated RGB values
	 */
	public Color getColorFromWavelength(double wavelength){
		double red = 0;
		double green = 0;
		double blue = 0;
		double SSS;
		
		if (wavelength < 380){
			red = 1.0;
	        green = 1.0;
	        blue = 1.0;
		}
		if (wavelength >= 380 && wavelength < 440){
			red = -(wavelength - 440) / (440 - 350);
	        green = 0.1; //0.0;
	        blue = 0.9; //1.0;
		}
		else if (wavelength >= 440 && wavelength < 490){
			red = 0.1; //0.0;
	        green = (wavelength - 440) / (490 - 440);
	        blue = 0.8; //1.0;
		}
		else if (wavelength >= 490 && wavelength < 510){
			red = 0.1; //0.0;
	        green = 0.9; //1.0;
	        blue = -(wavelength - 510) / (510 - 490);
		}
		else if (wavelength >= 510 && wavelength < 580){
			red = (wavelength - 510) / (580 - 510);
	        green = 0.8; //1.0;
	        blue = 0.2;
		}
		else if (wavelength >= 580 && wavelength < 645){
			red = 1.0; //1.0;
	        green = -(wavelength - 645) / (645 - 580);
	        blue = 0.3; //0.0;
		}
		else if (wavelength >= 645 && wavelength <= 780){
			red = 1.0; //1.0;
	        green = 0.6;  //0.0;
	        blue = 0.4; //0.0;
		}
	    else{
	    	red = 0.0;
	        green = 0.0;
	        blue = 0.0;
	    }

	    // intensity correction
	    if (wavelength >= 380 && wavelength < 420){
	    	SSS = 0.3 + 0.7*(wavelength - 350) / (420 - 350);
	    }
	    else if (wavelength >= 420 && wavelength <= 700){
	    	SSS = 1.0;
	    } 
	    else if (wavelength > 700 && wavelength <= 780){
	    	SSS = 0.3 + 0.7*(780 - wavelength) / (780 - 700);
	    } 
	    else{
	    	SSS = 0.0;
	    }
	    SSS *= 255;

	    Color c = Color.rgb((int)(SSS*red), (int)(SSS*green), (int)(SSS*blue));
		
		return c;
	}

}
