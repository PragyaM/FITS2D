package models;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;

import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;
import nom.tam.image.StandardImageTiler;
import nom.tam.util.ArrayFuncs;
import controllers.GUIController;

public class FitsImage{
	private ImageHDU hdu;
	private Fits fitsFile;
	private StandardImageTiler tiler;
	private int width;
	private int height;
	
	private double[] imageFriendlyData;
	private double[][] processingFriendlyData;
	private Image image;
	private GUIController controller;
	
	//TODO handle uncaught exceptions
	public FitsImage(Fits fitsFile, GUIController controller) throws FitsException, IOException{
		this.controller = controller;
		this.fitsFile = fitsFile;
		this.hdu = (ImageHDU) fitsFile.getHDU(0);;
		width = hdu.getAxes()[1];
		height = hdu.getAxes()[0];
		this.tiler =  hdu.getTiler();
		prepareData();
		writeImage();
	}
	
	private void prepareData(){
		
		double[] img = null;
		try {
			Object dataArray = tiler.getTile(new int[]{0, 0}, hdu.getAxes());
			img = (double[]) ArrayFuncs.convertArray(dataArray, double.class);
			processingFriendlyData =  (double[][]) ArrayFuncs.convertArray(hdu.getKernel(), double.class);//(float[][]) hdu.getKernel();
			imageFriendlyData = new double[width * height];
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
	
	public ArrayList<Point> getDataPositions(Point p, int cWidth, int cHeight){
		ArrayList<Point> points = new ArrayList<Point>();
		int x0 = (p.x * width)/cWidth;
		int y0 = (p.y * height)/cHeight;
		
		int x1 = x0 + width/cWidth;
		int y1 = y0 + height/cHeight;
		
		for (int x = x0; x <= x1; x++){
			for (int y = y0; y <= y1; y++){
				Point pos = new Point(x, height - y);
				points.add(pos);
			}
		}
		return points;
	}
	
	public double getValueAt(Point p){
		return processingFriendlyData[p.y][p.x];
	}
	
	public Fits getFitsFile(){
		return fitsFile;
	}
	
	public Image getImage(){
		return image;
	}

	public double[][] getData(){
		return processingFriendlyData;
	}
	
	public ImageHDU getHDU(){
		return hdu;
	}
	
	//SETUP METHODS:
	
	public void writeImage() throws IOException{
		//write image data
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		
		WritableRaster raster = im.getRaster();
		setImageColours(imageFriendlyData, raster);
		
		//convert image to a format that can be displayed
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(im, "png", out);
		out.flush();
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

		image = new Image(in);
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
				raster.setSample(x, y, 0, 0);
			} 
			else if (val > cutOff){
				raster.setSample(x, y, 0, 250);
			}
			else {
				double colVal = val/segmentSize;
				raster.setSample(x, y, 0, colVal);
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
