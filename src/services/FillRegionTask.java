package services;

import java.awt.Point;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.concurrent.Task;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FillRegionTask extends Task<ArrayList<Point>>{

	private final int width;
	private final int height;
	private final WritableImage wImg;
	private final Canvas canvas;
	private final Point origin;
	private final int target = 0;
	private final ArrayList<Point> points;
	private final int[][] canvasState;
	private final int replacement;
	private final Color replacementColor;
	private final Color targetColor;
	private final PixelReader pRead;

	public FillRegionTask(Canvas c, Point orig, Color replaceCol){
		origin = orig;
		points = new ArrayList<Point>();
		replacementColor = replaceCol;

		//setup required information:
		canvas = c;
		width = (int) canvas.getWidth();
		height = (int) canvas.getHeight();
		wImg = new WritableImage(width, height); //FIXME: doesn't take zoom into account
		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		canvas.snapshot(sp, wImg);
		pRead = wImg.getPixelReader();
		targetColor = pRead.getColor(origin.x, origin.y);
		if (targetColor.equals(replacementColor)){
			replacement = target;
		} else replacement = 1;

		canvasState = getCanvasState();
	}

	private ArrayList<Point> doFill(Point p, ArrayList<Point> points){
		ConcurrentLinkedQueue<Point> nodesQueue = new ConcurrentLinkedQueue<Point>();
		nodesQueue.add(p);
		boolean complete = false;
		
		if (isAlreadyFilled(p)){
			complete = true;
		}

		while (!complete){
			for (Point node : nodesQueue){
				if (isOutOfBounds(node) || hasReachedBoundary(node)){
					nodesQueue.remove(node);
				}
				else {
					canvasState[node.x][node.y] = replacement;
					points.add(node);
					nodesQueue.remove(node);
					nodesQueue.add(new Point(node.x, node.y-1));
					nodesQueue.add(new Point(node.x-1, node.y));
					nodesQueue.add(new Point(node.x, node.y+1));
					nodesQueue.add(new Point(node.x+1, node.y));
				}
			}

			if (nodesQueue.isEmpty()){
				complete = true;
			}
		}
		return points;
	}

	private boolean hasReachedBoundary(Point node){
		try{
			return (!(canvasState[node.x][node.y] == target));
		} catch (Exception e) {
			return true;
		}
	}

	private boolean isOutOfBounds(Point node){
		return (node.x < 0 || node.y < 0 || node.x >= width || node.y >= height);
	}

	private boolean isAlreadyFilled(Point node){
		return (target == replacement);
	}

	private int[][] getCanvasState(){

		int[][] cState = new int[width][height];
		for (int x = 0; x < width; x++){
			for (int y = 0; y<height; y++){
				if (pRead.getColor(x, y).equals(replacementColor)){
					cState[x][y] = replacement;
				}
				else cState[x][y] = target;
			}
		}
		return cState;
	}

	public ArrayList<Point> getPoints(){
		return points;
	}

	@Override
	protected ArrayList<Point> call() throws Exception {
		return doFill(origin, points);
	}

}
