package services;

import java.awt.Point;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class FillRegion {
	
	public static ArrayList<Point> fill(Canvas c, Point orig, Color replacementColor){
		
		DoFill doFill = new DoFill(c, orig, replacementColor);
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
