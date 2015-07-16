package models;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.ImageHDU;
import nom.tam.image.StandardImageTiler;
import nom.tam.util.ArrayFuncs;
import services.WriteImage;
import controllers.GUIController;

public class FitsImage{
	private ImageHDU hdu;
	private Fits fitsFile;
	private StandardImageTiler tiler;
	private int width;
	private int height;
	
	private double[] imageFriendlyData;
	private Color nanColour;
	
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
		setNanColour(controller.getNanColour());
		prepareData();
		
		writeImage();
	}
	
	public void writeImage(){
		WriteImage writeImage = new WriteImage(width, height, imageFriendlyData, hdu, nanColour);
		Thread t = new Thread(writeImage);
		t.setDaemon(true);
		t.start();

		try {
			t.join(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		image =  writeImage.getImage();
	}
	
	private void prepareData(){
		
		double[] img = null;
		try {
			Object dataArray = tiler.getTile(new int[]{0, 0}, hdu.getAxes());
			img = (double[]) ArrayFuncs.convertArray(dataArray, double.class);
			processingFriendlyData =  (double[][]) ArrayFuncs.convertArray(hdu.getKernel(), double.class);
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
	
	public void setNanColour(Color replacement) throws IOException{
		nanColour = replacement;
	}
	
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

	public Color getNanColour() {
		return nanColour;
	}
}
