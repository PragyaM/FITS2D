package views;

import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class ImageToggleButton extends ToggleButton{
	
	
	
	public ImageToggleButton(String imageUrl){
		super();
		Image image = new Image(getClass().getResourceAsStream(imageUrl));
		ImageView imageView = new ImageView(image);
		imageView.setScaleX(0.75);
		imageView.setScaleY(0.75);
		this.setGraphic(imageView);
        this.setShape(new Circle(30));
	}

}
