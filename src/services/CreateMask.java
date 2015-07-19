package services;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.FitsFactory;
import nom.tam.fits.Header;
import nom.tam.util.ArrayFuncs;

public class CreateMask {

	public static Fits mapToFits(ArrayList<Point> maskPoints, FitsImage fitsImage, int cWidth, int cHeight) throws FitsException, IOException {
		//Get corresponding values from FITS data
		Fits maskFits = new Fits();
		maskFits.addHDU(fitsImage.getHDU());
		double[][] oldData = (double[][]) ArrayFuncs.convertArray(fitsImage.getHDU().getKernel(), double.class);
		double[][] newData = new double[oldData.length][oldData[0].length];
		newData = setAll(newData, Double.NaN);
		newData = setMaskValues(newData, maskPoints, fitsImage, cWidth, cHeight);
		
		//get base type of original data
		Class type = ArrayFuncs.getBaseClass(fitsImage.getHDU().getKernel());
		
		if (type == float.class){
			float[][] floatArray = (float[][]) maskFits.getHDU(0).getKernel();
			for (int i=0; i<floatArray.length; i += 1) {
				for (int j=0; j<floatArray[i].length; j += 1) {
					floatArray[i][j] = (float) newData[i][j];
				}
			}
		}
		else if (type == double.class){
			double[][] doubleArray = (double[][]) maskFits.getHDU(0).getKernel();
			for (int i=0; i<doubleArray.length; i += 1) {
				for (int j=0; j<doubleArray[i].length; j += 1) {
					doubleArray[i][j] = (double) newData[i][j];
				}
			}
		}
		else if (type == short.class){
			short[][] shortArray = (short[][]) maskFits.getHDU(0).getKernel();
			for (int i=0; i<shortArray.length; i += 1) {
				for (int j=0; j<shortArray[i].length; j += 1) {
					shortArray[i][j] = (short) newData[i][j];
				}
			}
		}
		else if (type == long.class){
			long[][] longArray = (long[][]) maskFits.getHDU(0).getKernel();
			for (int i=0; i<longArray.length; i += 1) {
				for (int j=0; j<longArray[i].length; j += 1) {
					longArray[i][j] = (long) newData[i][j];
				}
			}
		}

		return maskFits;
	}

	private static double[][] setAll(double[][] data, double value){
		
		for (int i=0; i<data.length; i++){
			for (int j=0; j<data[i].length; j++){
				data[i][j] = value;
			}
		}
		
		return data;
	}

	private static double[][] setMaskValues(double[][] data, ArrayList<Point> maskPoints, FitsImage fitsImage, int cw, int ch){

		for (Point p : maskPoints){
			ArrayList<Point> dataPositions = fitsImage.getDataPositions(p, cw, ch);
			for (Point dataPos : dataPositions){
				data[dataPos.y][dataPos.x] = fitsImage.getValueAt(dataPos);
			}
		}

		return data;
	}

}
