package services;

import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import javafx.scene.shape.Rectangle;
import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.Header;
import nom.tam.util.ArrayFuncs;

public class CreateMask {

	private static Rectangle boundingBox;

	public static Fits mapToFits(ArrayList<Point> maskPoints, FitsImage fitsImage, 
			int cWidth, int cHeight) throws FitsException, IOException {
		//Get corresponding values from FITS data
		Fits maskFits = new Fits();

		boundingBox = getBoundingBox(fitsImage, maskPoints, cWidth, cHeight);
		System.out.println("Region size: " + boundingBox.getWidth() + ", " + boundingBox.getHeight());
		System.out.println("Region position: " + boundingBox.getX() + ", " + boundingBox.getY());

		double[][] newData = new double[(int) boundingBox.getHeight()][(int) boundingBox.getWidth()];
		newData = setAll(newData, Double.NaN);
		newData = setMaskValues(newData, maskPoints, fitsImage, cWidth, cHeight);

		//get base type of original data
		Class type = ArrayFuncs.getBaseClass(fitsImage.getHDU().getKernel());

		Object data = newData;
		ArrayFuncs.convertArray(data, type);
		maskFits.addHDU(Fits.makeHDU(data));
		
		//set WCS reference coordinate values:
		setWCSRefs(maskFits, fitsImage);

		return maskFits;
	}
	
	private static void setWCSRefs(Fits newFits, FitsImage oldFits){
		Header oldHeader = oldFits.getHDU().getHeader();
		for (int i = 0; i < oldHeader.getNumberOfCards(); i++){
			try {
				newFits.getHDU(0).getHeader().addLine(oldHeader.nextCard());
			} catch (FitsException | IOException e) {
				e.printStackTrace();
			}
		}
		//now adjust WCS values:
		try {
			newFits.getHDU(0).addValue("CRPIX1", 
					oldHeader.getDoubleValue("CRPIX1") - boundingBox.getX(),
					"X pixel of tangent point");
			newFits.getHDU(0).addValue("CRPIX2", 
					oldHeader.getDoubleValue("CRPIX2") - boundingBox.getY(),
					"Y pixel of tangent point");
		} catch (FitsException | IOException e) {
			e.printStackTrace();
		}
	}

	private static Rectangle getBoundingBox(FitsImage fitsImage, 
			ArrayList<Point> maskPoints, int width, int height){
		int minX = Integer.MAX_VALUE;
		int maxX = 0;
		int minY = Integer.MAX_VALUE;
		int maxY = 0;

		for (Point p : maskPoints){
			ArrayList<Point> dataPoints = fitsImage.getDataPositions(p, width, height);
			for (Point dataPoint : dataPoints){
				if (dataPoint.x < minX) minX = dataPoint.x;
				if (dataPoint.x > maxX) maxX = dataPoint.x;
				if (dataPoint.y < minY) minY = dataPoint.y;
				if (dataPoint.y > maxY) maxY = dataPoint.y;
			}

		}

		return new Rectangle(minX, minY, maxX-minX + 1, maxY-minY + 1);
	}

	private static double[][] setAll(double[][] data, double value){

		for (int i=0; i<data.length; i++){
			for (int j=0; j<data[i].length; j++){
				data[i][j] = value;
			}
		}

		return data;
	}

	private static double[][] setMaskValues(double[][] data, ArrayList<Point> maskPoints, 
			FitsImage fitsImage, int cw, int ch){

		for (Point p : maskPoints){
			ArrayList<Point> dataPositions = fitsImage.getDataPositions(p, cw, ch);
			for (Point dataPos : dataPositions){
				try {
					data[dataPos.y - (int) boundingBox.getY()][dataPos.x - (int) boundingBox.getX()] 
							= fitsImage.getValueAt(dataPos);
				} catch (Exception e) {
					System.out.println(e.getMessage() + " out of bounds in (" 
							+ (dataPos.y - (int) boundingBox.getY()) + ", " 
							+ (dataPos.x - (int) boundingBox.getX()) + ")");
				}
			}
		}

		return data;
	}

}
