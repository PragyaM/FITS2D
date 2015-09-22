package models;

public class Histogram {

	private int countMin = Integer.MAX_VALUE;
	private int countMax = Integer.MIN_VALUE;
	private int equalisedMin = Integer.MAX_VALUE;
	private int equalisedMax = Integer.MIN_VALUE;
	private double minValue;
	private double maxValue;
	private int[] histogram;
	private int[] equalisedHistogram;
	private double visibleRangeMin;
	private int numTotalPixels;
	private double visibleRangeMax;

	public Histogram(double[] data, double min, double max, int width, int height){
		this.minValue = min;
		this.maxValue = max;
		System.out.println("Data min: " + min + ", data max: " + max);
		numTotalPixels = width * height;
		histogram = constructGraph(data);
		calculateMinMaxCounts();
		setUpDefaultVisibleRange();
		createEqualisedHistogram();
		System.out.println("Visible min: " + visibleRangeMin + " visible max: " + visibleRangeMax);
	}

	private void calculateMinMaxCounts() {

		for (int i = 0; i < histogram.length; i++){
			if (histogram[i] > countMax){
				countMax = histogram[i];
			}
		}

		for (int i = 0; i < histogram.length; i++){
			if (histogram[i] < countMin){
				countMin = histogram[i];
				if (countMin == 0) break;
			}
		}
	}

	private int[] constructGraph(double[] data){
		int range = (int) (maxValue - minValue);

		int[] distribution = new int[range + 1];

		for(int i = 0; i < data.length; i++){
			int bucket = (int) (data[i] - minValue);
			distribution[bucket] = distribution[bucket] + 1;
		}

		return distribution;
	}

	private void setUpDefaultVisibleRange(){
		//find bottom of range:
		for (int i = 0; i < histogram.length; i++){
			if (histogram[i] == countMax){
				visibleRangeMin = i + minValue;
				break;
			}
		}
		
		visibleRangeMax = minValue;
		//find top of range
		for (int i = histogram.length-1; i >= 0; i--){
			if (histogram[i] > countMax/32){
				visibleRangeMax = i + minValue;
				break;
			}
		}
	}

	public void createEqualisedHistogram(){
		equalisedHistogram = new int[histogram.length];

		for (int i = 0; i < histogram.length; i++){
			int val = histogram[i];
			double cdfVal = 0;
			for (int j = 0; j < i; j++){
				cdfVal = cdfVal + (histogram[j]/numTotalPixels);
			}
			int equalisedVal = (int) Math.round((cdfVal - 1)/(numTotalPixels - 1)*250);
			equalisedHistogram[i] = equalisedVal;

			if (equalisedVal > equalisedMax) equalisedMax = equalisedVal;
			if (equalisedVal < equalisedMin) equalisedMin = equalisedVal;

		}
	}

	public int getEqualisedMin() {
		return equalisedMin;
	}

	public void setEqualisedMin(int equalisedMin) {
		this.equalisedMin = equalisedMin;
	}

	public int getEqualisedMax() {
		return equalisedMax;
	}

	public void setEqualisedMax(int equalisedMax) {
		this.equalisedMax = equalisedMax;
	}

	public int[] getEqualisedHistogram() {
		return equalisedHistogram;
	}

	public void setEqualisedHistogram(int[] equalisedHistogram) {
		this.equalisedHistogram = equalisedHistogram;
	}

	public int getNumTotalPixels() {
		return numTotalPixels;
	}

	public void setNumTotalPixels(int numTotalPixels) {
		this.numTotalPixels = numTotalPixels;
	}

	public int getCountMin() {
		return countMin;
	}

	public void setCountMin(int countMin) {
		this.countMin = countMin;
	}

	public int getCountMax() {
		return countMax;
	}

	public void setCountMax(int countMax) {
		this.countMax = countMax;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public int[] getHistogram() {
		return histogram;
	}

	public void setHistogram(int[] histogram) {
		this.histogram = histogram;
	}

	public double getVisibleRangeMin() {
		return visibleRangeMin;
	}

	public void setVisibleRangeMin(double visibleRangeMin) {
		this.visibleRangeMin = visibleRangeMin;
	}

	public double getVisibleRangeMax() {
		return visibleRangeMax;
	}

	public void setVisibleRangeMax(double visibleRangeMax) {
		this.visibleRangeMax = visibleRangeMax;
	}

}
