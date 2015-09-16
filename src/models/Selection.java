package models;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import views.FitsCanvas;
import views.FitsCanvas.Mode;
import controllers.DrawingsController;

public class Selection extends Drawing{

	public Selection(FitsCanvas canvas, DrawingsController controller, Color color){
		super(canvas, controller, color);
	}
	
	@Override
	public void handle(MouseEvent event) {
		if (canvas.mode==Mode.SELECTION_DRAW){
			super.drawAction(event);
		}
		else if (canvas.mode==Mode.SELECTION_FILL){
			super.floodFillAction(event);
		}		
	}
	
	public String toString(){
		StringBuilder selectionString = new StringBuilder("\ns");
		//selectionString.append("\nColour: ");
		//selectionString.append(color.toString());
		for (PixelRegion region : regions){
			selectionString.append('\n');
			selectionString.append(region.toString());
		}
		return selectionString.toString();
	}
}
