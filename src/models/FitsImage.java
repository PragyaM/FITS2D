package models;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;
import nom.tam.image.ImageTiler;

public class FitsImage{
	private ImageHDU hdu;
	private Fits fitsFile;
	private ImageTiler tiler;
	private int width;
	private int height;
	
	private float[] imageFriendlyData;
	private float[][] processingFriendlyData;
	private Image image;
	
	//TODO handle uncaught exceptions
	public FitsImage(Fits fitsFile) throws FitsException, IOException{
		this.fitsFile = fitsFile;
		this.hdu = (ImageHDU) fitsFile.getHDU(0);;
		width = hdu.getAxes()[1];
		height = hdu.getAxes()[0];
		this.tiler = hdu.getTiler();
		prepareData();
		writeImage();
	}
	
	private void prepareData(){
		float[] img = null;
		try {
			img = (float[]) tiler.getTile(new int[]{0, 0}, hdu.getAxes());
			processingFriendlyData = (float[][]) hdu.getKernel();
			imageFriendlyData = new float[width * height];
			for (int h = height - 1; h >= 0; h--){
				for (int w = 0; w < width; w++){
					imageFriendlyData[h * width + w] = (img[(height-1 - h)*width + w]);
				}
			}
		} catch (IOException | FitsException e) {
			e.printStackTrace();
		}
		
		
		System.out.println(processingFriendlyData.length + ", " + processingFriendlyData[0].length);
	}
	
	public Point getDataPosition(Point p){
		Point pos = new Point(height - p.y, p.x);
		System.out.println(pos.x + ", " + pos.y);
		return pos;
	}
	
	public float getValueAt(Point p){
		return processingFriendlyData[p.x][p.y];
	}
	
	public Fits getFitsFile(){
		return fitsFile;
	}
	
	public Image getImage(){
		return image;
	}

	public float[][] getData(){
		return processingFriendlyData;
	}
	
	public ImageHDU getHDU(){
		return hdu;
	}
	
	//SETUP METHODS:
	
	public void writeImage() throws IOException{
		//write image data
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
		
		WritableRaster raster = im.getRaster();
		setImageColours(imageFriendlyData, raster);
		
		//convert image to a format that can be displayed
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(im, "png", out);
		out.flush();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

		image = new Image(in);
	}
	
	private void setImageColours(float[] imageData, WritableRaster raster){
		double max = hdu.getMaximumValue();
		double cutOff = max * 0.01;
		
		int colBandRange = 780 - 380;  //16777215;
		float segmentSize = (float) (cutOff/colBandRange);

		for (int i = 0; i < imageData.length; i++){
			float val = (float) (imageData[i]);
			int x = i % width;
			int y = (int) Math.ceil(i / width);
			if (val == Double.NaN || val <= 0) {
				raster.setSample(x, y, 0, 0);
				raster.setSample(x, y, 1, 0);
				raster.setSample(x, y, 2, 0);
			} 
			else if (val > cutOff){
				raster.setSample(x, y, 0, 255);
				raster.setSample(x, y, 1, 255);
				raster.setSample(x, y, 2, 255);
			}
			else {
				float colVal = val/segmentSize + 380;
				Color c = getColorFromWavelength(colVal);
				raster.setSample(x, y, 0, c.getRed()*255);
				raster.setSample(x, y, 1, c.getGreen()*255);
				raster.setSample(x, y, 2, c.getBlue()*255);
			}
		}
	}
	
	/**
	 * @param wavelength - expected to be within the 380 to 780 range (where visible colors can be resolved).
	 * @return a Color containing calculated RGB values
	 */
	public Color getColorFromWavelength(float wavelength){
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
	
	//TODO add methods for manipulating FITS image data
	
	public void printFitsInfo(){
		float[][] data = (float[][]) hdu.getKernel();
		for (int i = 0; i < data.length; i++){
			for (int j = 0; j < data[i].length; j++){
				System.out.print(data[i][j] + ", ");
			}
			System.out.println();
		}
		
		System.out.println("Number of HDUs: " + fitsFile.getNumberOfHDUs());
		System.out.println("Author: " + hdu.getAuthor());
		try {
			System.out.println("BitPix" + hdu.getBitPix());
		} catch (FitsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
