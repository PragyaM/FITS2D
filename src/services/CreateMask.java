package services;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

import models.FitsImage;
import nom.tam.fits.Fits;
import nom.tam.fits.FitsException;
import nom.tam.fits.FitsFactory;
import nom.tam.util.ArrayFuncs;

public class CreateMask {

	public static Fits mapToFits(ArrayList<Point> maskPoints, FitsImage fitsImage, int cWidth, int cHeight) throws FitsException, IOException {
		//Get corresponding values from FITS data
		Fits maskFits = new Fits();

		maskFits.addHDU(fitsImage.getHDU());

		float[][] oldData = (float[][]) ArrayFuncs.convertArray(fitsImage.getHDU().getKernel(), float.class);

		float[][] newData = new float[oldData.length][oldData[0].length];
		newData = setAll(newData, Float.NaN);
		newData = setMaskValues(newData, maskPoints, fitsImage, cWidth, cHeight);

		maskFits.addHDU(FitsFactory.HDUFactory(newData));


		float[][] img = (float[][]) ArrayFuncs.convertArray(maskFits.getHDU(0).getKernel(), float.class);
		for (int i=0; i<img.length; i += 1) {
			for (int j=0; j<img[i].length; j += 1) {
				img[i][j] = newData[i][j];
			}
		}

		return maskFits;
	}

	private static float[][] setAll(float[][] data, float value){
		for (int i=0; i<data.length; i++){
			for (int j=0; j<data[i].length; j++){
				data[i][j] = value;
			}
		}
		return data;
	}

	private static float[][] setMaskValues(float[][] data, ArrayList<Point> maskPoints, FitsImage fitsImage, int cw, int ch){

		for (Point p : maskPoints){
			ArrayList<Point> dataPositions = fitsImage.getDataPositions(p, cw, ch);
			for (Point dataPos : dataPositions){
				data[dataPos.y][dataPos.x] = fitsImage.getValueAt(dataPos);
			}
		}

		return data;
	}

}
