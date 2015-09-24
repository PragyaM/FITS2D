package models;

import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;

public class Histogram {

	private double range;
	private int countMin = Integer.MAX_VALUE;
	private int countMax = Integer.MIN_VALUE;
	private int equalisedMin = Integer.MAX_VALUE;
	private int equalisedMax = Integer.MIN_VALUE;
	private double minValue = Double.MAX_VALUE;
	private double maxValue = Double.MIN_VALUE;
	private int[] histogram;
	private int[] equalisedHistogram;
	private double visibleRangeMin;
	private double visibleRangeMax;
	private int numTotalPixels;
	private TreeMap<Double, Integer> histogramMap;
	private TreeMap<Double, Integer> visibleRangeMap;
	private ValueAxis<Number> xAxis;
	private ValueAxis<Number> yAxis;

	private AreaChart<Number, Number> histogramChart;

	public Histogram(double[] data, int width, int height){
		numTotalPixels = width * height;
		construct(data);
		calculateMinMaxValues();
		System.out.println("Data min: " + minValue + ", data max: " + maxValue);
		setUpDefaultVisibleRange();
		createChart(true);
		System.out.println("Visible min: " + visibleRangeMin + " visible max: " + visibleRangeMax);
	}

	private void construct(double[] data){
		histogramMap = new TreeMap<Double, Integer>();

		for(int i = 0; i < data.length; i++){
			/* populate histogram */
			double key = (double) (int) data[i];
			histogramMap.putIfAbsent(key, null);
			histogramMap.compute(key, (k, v) -> v == null ? 1 : v+1);
		}

		System.out.println("histogram size: " + histogramMap.size());
	}

	private void calculateMinMaxValues() {

		histogramMap.forEach((k, v) -> {

			/* calculate min and max values */
			if (k > maxValue){
				maxValue = k;
			}
			if (k < minValue){
				minValue = k;
			}

			/* calculate min and max counts */
			if (v > countMax){
				countMax = v;
			}
			if (v < countMin){
				countMin = v;
			}

		});

		range = maxValue - minValue;
	}

	private void setUpDefaultVisibleRange(){

		/* Find bottom of range */
		visibleRangeMap = new TreeMap<Double, Integer>();
		visibleRangeMap.putAll(histogramMap.entrySet().stream()
				.filter(p -> p.getValue() == countMax)
				.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue())));
		double bottomKey = visibleRangeMap.keySet().iterator().next();

		TreeMap<Double, Integer> filtered = new TreeMap<Double, Integer>();
		filtered.putAll(histogramMap.tailMap(bottomKey, true));
		int count = 0;
		double topKey = bottomKey;
		for (Entry<Double, Integer> entry : filtered.entrySet()){
			count = count + entry.getValue();
			if (count > (numTotalPixels/8 + filtered.get(bottomKey))){
				topKey = entry.getKey();
				break;
			}
		}
		
		if (topKey == bottomKey) {
			topKey = topKey + 1;
		}

		/* Set visible range entries */
		visibleRangeMap.putAll(filtered.subMap(bottomKey, topKey + 1));

		visibleRangeMin = visibleRangeMap.firstKey();
		visibleRangeMax = visibleRangeMap.lastKey();
	}

	public void createChart(boolean logYAxis){
		createAxes(logYAxis);
		histogramChart = new AreaChart<Number, Number>(xAxis, yAxis);
		histogramChart.setHorizontalGridLinesVisible(false);
		histogramChart.setVerticalGridLinesVisible(false);

		updateChart();
	}

	public void updateChart(){
		XYChart.Series<Number, Number> totalRange = new XYChart.Series<Number, Number>();
		totalRange.setName("Full range");
				histogramMap.forEach((k, v) -> {
					totalRange.getData().add(new XYChart.Data<Number, Number>(k, v));
				});

		XYChart.Series<Number, Number> visibleRange = new XYChart.Series<Number, Number>();
		visibleRange.setName("Visible range");
		visibleRangeMap.forEach((k, v) -> {
			visibleRange.getData().add(new XYChart.Data<Number, Number>(k, v));
		});

		histogramChart.getData().clear();
		histogramChart.getData().setAll(totalRange, visibleRange);
	}
	
	public void createAxes(boolean logYAxis){
		xAxis = new NumberAxis(Math.round(getMinValue()-2), 
				Math.round(maxValue+2), 
				Math.round((maxValue - minValue)/10));
		if (logYAxis){
			yAxis = new LogarithmicAxis();
		} else {
			yAxis = new NumberAxis(countMin, countMax, 
					Math.round((countMax - countMin)/10));
		}
		xAxis.setMinorTickVisible(false);
		
	}

	//	public void createEqualisedHistogram(){
	//		equalisedHistogram = new int[histogram.length];
	//
	//		for (int i = 0; i < histogram.length; i++){
	//			int val = histogram[i];
	//			double cdfVal = 0;
	//			for (int j = 0; j < i; j++){
	//				cdfVal = cdfVal + (histogram[j]/numTotalPixels);
	//			}
	//			int equalisedVal = (int) Math.round((cdfVal - 1)/(numTotalPixels - 1)*250);
	//			equalisedHistogram[i] = equalisedVal;
	//
	//			if (equalisedVal > equalisedMax) equalisedMax = equalisedVal;
	//			if (equalisedVal < equalisedMin) equalisedMin = equalisedVal;
	//
	//		}
	//	}

	public int getEqualisedMin() {
		return equalisedMin;
	}

	public int getEqualisedMax() {
		return equalisedMax;
	}

	public int[] getEqualisedHistogram() {
		return equalisedHistogram;
	}

	public int getNumTotalPixels() {
		return numTotalPixels;
	}

	public int getCountMin() {
		return countMin;
	}

	public int getCountMax() {
		return countMax;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public int[] getHistogram() {
		return histogram;
	}

	public double getVisibleRangeMin() {
		return visibleRangeMin;
	}

	public void setVisibleRangeMin(double min){
		visibleRangeMin = min;
	}

	public double getVisibleRangeMax() {
		return visibleRangeMax;
	}

	public void setVisibleRangeMax(double max) {
		visibleRangeMax = max;
	}

	public AreaChart<Number, Number> getHistogramChart() {
		return histogramChart;
	}

}