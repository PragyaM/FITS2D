package services;

import java.awt.Point;
import java.util.ArrayList;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import controllers.FitsCanvasController;

public class FillRegion {
	
	public static ArrayList<Point> fill(Canvas c, FitsCanvasController controller, Point orig, Color replacementColor){
		controller.resetZoom();
		
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
