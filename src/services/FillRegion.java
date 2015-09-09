package services;

import java.awt.Point;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import views.FitsImageViewBox;

public class FillRegion {
	
	public static ArrayList<Point> fill(Canvas c, FitsImageViewBox imageViewBox, Point orig, Color replacementColor){
		c.setScaleX(1);
		c.setScaleY(1);
		
		imageViewBox.getImageView().setScaleX(1);
		imageViewBox.getImageView().setScaleY(1);
		
		FillRegionTask doFill = new FillRegionTask(c, orig, replacementColor);
		Thread t = new Thread(doFill);
		t.setDaemon(true);
		t.start();

		try {
			t.join(0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return doFill.getPoints();
		
	}
}
