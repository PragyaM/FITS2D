package views.tools_area;

import javafx.geometry.Insets;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import models.Histogram;
import models.LogarithmicAxis;
import controllers.ImageController;

public class HistogramToolBox extends BaseToolBox{

	private ImageController controller;
	private AreaChart<Number, Number> histogramChart;
	private Button hideButton;
	private Label minLabel;
	private Label maxLabel;
	private TextField visibleRangeMinInput;
	private TextField visibleRangeMaxInput;
	
	public HistogramToolBox(ImageController controller) {
		super("Data");
		this.controller = controller;
	}
	
	@SuppressWarnings("unchecked")
	public void setUpHistogram(Histogram histogram){
		this.getChildren().clear();
		ValueAxis<Number> xAxis = new NumberAxis(Math.round(histogram.getMinValue()-2), 
				Math.round(histogram.getMaxValue()+2), 
				Math.round((histogram.getMaxValue() - histogram.getMinValue())/10));
		ValueAxis<Number> yAxis = new LogarithmicAxis();
		xAxis.setMinorTickVisible(false);
		histogramChart = new AreaChart<Number, Number>(xAxis, yAxis);
		histogramChart.setHorizontalGridLinesVisible(false);
		histogramChart.setVerticalGridLinesVisible(false);
		
		XYChart.Series<Number, Number> totalRange = new XYChart.Series<Number, Number>();
		totalRange.setName("Full range");
		for (int i = 0; i < histogram.getHistogram().length; i++){
			totalRange.getData().add(new XYChart.Data<Number, Number>((i + histogram.getMinValue()), histogram.getHistogram()[i]));
		}
		
		XYChart.Series<Number, Number> visibleRange = new XYChart.Series<Number, Number>();
		visibleRange.setName("Visible range");
		for (int i = (int) histogram.getVisibleRangeMin(); i <= histogram.getVisibleRangeMax(); i++){
			visibleRange.getData().add(new XYChart.Data<Number, Number>((i), histogram.getHistogram()[i - (int) histogram.getMinValue()]));
		}
		histogramChart.getData().addAll(totalRange, visibleRange);
		
		hideButton = new Button("Hide Histogram");
		hideButton.setOnAction(controller.toggleHistogramVisible(hideButton));
		
		histogramChart.setPrefSize(200, 80);
		add(histogramChart, 1, 0);
		
		minLabel = new Label("Minimum value: " + histogram.getMinValue());
		maxLabel = new Label("Maximum value: " + histogram.getMaxValue());
		
		Label visibleMinMaxLabel = new Label("Set minimum and maximum values of visible range:");
		visibleMinMaxLabel.setPadding(new Insets(10, 0, 0, 0));
		
		visibleRangeMinInput = new TextField();
		visibleRangeMinInput.setPromptText("Visible range minimum");
		visibleRangeMinInput.setText("" + histogram.getVisibleRangeMin());
		visibleRangeMinInput.setOnAction(controller.updateVisibleRangeMinimum(visibleRangeMinInput));
		
		visibleRangeMaxInput = new TextField();
		visibleRangeMaxInput.setPromptText("Visible range maximum");
		visibleRangeMaxInput.setText("" + histogram.getVisibleRangeMax());
		visibleRangeMaxInput.setOnAction(controller.updateVisibleRangeMaximum(visibleRangeMaxInput));
		
		
		GridPane box = new GridPane();
		box.setHgap(2);
		box.setVgap(2);
		this.getChildren().remove(headingLabel);
		box.add(headingLabel, 0, 0);
		box.add(minLabel, 0, 1);
		box.add(maxLabel, 0, 2);
		box.add(visibleMinMaxLabel, 0, 3);
		GridPane miniBox = new GridPane();
		miniBox.add(visibleRangeMinInput, 0, 0);
		miniBox.add(visibleRangeMaxInput, 1, 0);
		box.add(miniBox, 0, 4);
		box.add(hideButton, 0, 5);
		add(box, 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public void updateHistogram(Histogram histogram){
		XYChart.Series<Number, Number> imageData = new XYChart.Series<Number, Number>();
		imageData.setName("Full range");
		for (int i = 0; i < histogram.getHistogram().length; i++){
			imageData.getData().add(new XYChart.Data<Number, Number>((i + histogram.getMinValue()), histogram.getHistogram()[i]));
		}
		
		XYChart.Series<Number, Number> visibleRange = new XYChart.Series<Number, Number>();
		visibleRange.setName("Visible range");
		for (int i = (int) histogram.getVisibleRangeMin(); i <= histogram.getVisibleRangeMax(); i++){
			visibleRange.getData().add(new XYChart.Data<Number, Number>((i), histogram.getHistogram()[i - (int) histogram.getMinValue()]));
		}
		
		histogramChart.getData().clear();
		histogramChart.getData().setAll(imageData, visibleRange);
	}
	
	public void toggleLogYAxis(){
		ValueAxis<Number> xAxis = new NumberAxis();
		ValueAxis<Number> yAxis = new LogarithmicAxis();
		histogramChart = new AreaChart<Number, Number>(xAxis, yAxis);
		updateHistogram(controller.getFitsImage().getHistogram());
	}
	
	public void hide(){
		getChildren().remove(histogramChart);
		hideButton.setText("Show Histogram");
	}
	
	public void show(){
		getChildren().add(histogramChart);
		histogramChart.setVisible(true);
		hideButton.setText("Hide Histogram");
	}

}
