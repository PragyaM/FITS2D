package models;

public class Histogram {

	private int countMin = Integer.MAX_VALUE;
	private int countMax = Integer.MIN_VALUE;
	private double minValue;
	private double maxValue;
	private int[] histogram;
	private double visibleRangeMin;

	private double visibleRangeMax;

	public Histogram(double[] data, double min, double max){
		this.minValue = min;
		this.maxValue = max;

		histogram = constructGraph(data);
		calculateMinMaxCounts();
		setUpDefaultVisibleRange();
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
		//find top of range:
		for (int i = 0; i < histogram.length; i++){
			if (histogram[i] == countMax){
				visibleRangeMax = i;
				break;
			}
		}
		
		visibleRangeMin = minValue;
		//find bottom of range
		for (int i = 0; i < histogram.length; i++){
			if (histogram[i] > 1){
				visibleRangeMin = i;
				break;
			}
		}
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
