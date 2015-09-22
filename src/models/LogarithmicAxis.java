/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013, Christian Schudt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package models;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.chart.ValueAxis;

/**
 * An axis that plots numbers on a logarithmic scale.
 * <p/>
 * <h3>Screenshots</h3>
 * <img src="doc-files/LogarithmicAxis.png" alt="LogarithmicAxis" />
 *
 * @author Christian Schudt
 * adapted at https://community.oracle.com/thread/2491285
 */
public class LogarithmicAxis extends ValueAxis<Number> {   
    
    //Create our LogarithmicAxis class that extends ValueAxis<Number> and define two properties that will represent the log lower and upper bounds of our axis.     
    private final DoubleProperty logUpperBound = new SimpleDoubleProperty();
    private final DoubleProperty logLowerBound = new SimpleDoubleProperty();
    
    //we bind our properties with the default bounds of the value axis. But before, we should verify the given range according to the mathematic logarithmic interval definition.
    public LogarithmicAxis() {
        super(1, 100);
        bindLogBoundsToDefaultBounds();
    }

    public LogarithmicAxis(double lowerBound, double upperBound) {
        super(lowerBound, upperBound);
    try {
        validateBounds(lowerBound, upperBound);
        bindLogBoundsToDefaultBounds();
    } catch (IllegalLogarithmicRangeException e) {
        }
    }

    /**
     * Bind our logarithmic bounds with the super class bounds, consider the base 10 logarithmic scale.
     */
    private void bindLogBoundsToDefaultBounds() {
        logLowerBound.bind(new DoubleBinding() {
            {
                super.bind(lowerBoundProperty());
            }

            @Override
            protected double computeValue() {
                return Math.log10(lowerBoundProperty().get());
            }
        });
        logUpperBound.bind(new DoubleBinding() {
            {
                super.bind(upperBoundProperty());
            }

            @Override
            protected double computeValue() {
                return Math.log10(upperBoundProperty().get());
            }
        });
    }
    
    /**
     * Validate the bounds by throwing an exception if the values are not conform to the mathematics log interval:
     * ]0,Double.MAX_VALUE]
     *
     * @param lowerBound
     * @param upperBound
     * @throws IllegalLogarithmicRangeException
     */
    private void validateBounds(double lowerBound, double upperBound) throws IllegalLogarithmicRangeException {
        if (lowerBound < 0 || upperBound < 0 || lowerBound > upperBound) {
            throw new IllegalLogarithmicRangeException(
                    "The logarithmic range should be include to ]0,Double.MAX_VALUE] and the lowerBound should be less than the upperBound");
        }
    }

    //Now we have to implement all abstract methods of the ValueAxis class.
    //The first one, calculateMinorTickMarks is used to get the list of minor tick marks position that you want to display on the axis. You could find my definition below. It's based on the number of minor tick and the logarithmic formula.
    @Override
    protected List<Number> calculateMinorTickMarks() {
        Number[] range = getRange();
        List<Number> minorTickMarksPositions = new ArrayList<>();
        if (range != null) {
            
            Number lowerBound = range[0];
            Number upperBound = range[1];
            double logUpperBound = Math.log10(upperBound.doubleValue());
            double logLowerBound = Math.log10(lowerBound.doubleValue());
            
            int minorTickMarkCount = getMinorTickCount();

            for (double i = logLowerBound; i <= logUpperBound; i += 1) {
                for (double j = 0; j <= 10; j += (1. / minorTickMarkCount)) {
                    double value = j * Math.pow(10, i);
                    minorTickMarksPositions.add(value);
                }
            }
        }
        return minorTickMarksPositions;
    }
   
    //Then, the calculateTickValues method is used to calculate a list of all the data values for each tick mark in range, represented by the second parameter. The formula is the same than previously but here we want to display one tick each power of 10.
    @Override
    protected List<Number> calculateTickValues(double length, Object range) {
        List<Number> tickPositions = new ArrayList<Number>();
        if (range != null) {
            Number lowerBound = ((Number[]) range)[0];
            Number upperBound = ((Number[]) range)[1];
            double logLowerBound = Math.log10(lowerBound.doubleValue());
            double logUpperBound = Math.log10(upperBound.doubleValue());
//            System.out.println("lower bound is: " + lowerBound.doubleValue());
            
            for (double i = logLowerBound; i <= logUpperBound; i += 1) {
                for (double j = 1; j <= 10; j++) {
                    double value = (j * Math.pow(10, i));
                    tickPositions.add(value);
                }
            }
        }
        return tickPositions;
    }
    
    //The getRange provides the current range of the axis. A basic implementation is to return an array of the lowerBound and upperBound properties defined into the ValueAxis class.
    @Override
    protected Number[] getRange() {
        return new Number[] { lowerBoundProperty().get(), upperBoundProperty().get() };
    }
    
    //The getTickMarkLabel is only used to convert the number value to a string that will be displayed under the tickMark. Here I choose to use a number formatter.
    @Override
    protected String getTickMarkLabel(Number value) {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumIntegerDigits(6);
        formatter.setMinimumIntegerDigits(1);
        return formatter.format(value);
    }
    
    //The method setRange is used to update the range when data are added into the chart. There is two possibilities, the axis is animated or not. The simplest case is to set the lower and upper bound properties directly with the new values.
    @Override    
    protected void setRange(Object range, boolean animate) {
        if (range != null) {
            Number lowerBound = ((Number[]) range)[0];
            Number upperBound = ((Number[]) range)[1];
            try {
                validateBounds(lowerBound.doubleValue(), upperBound.doubleValue());
            } catch (IllegalLogarithmicRangeException e) {
            }

            lowerBoundProperty().set(lowerBound.doubleValue());
            upperBoundProperty().set(upperBound.doubleValue());
        }
    }
    
    //We are almost done but we forgot to override 2 important methods that are used to perform the matching between data and the axis (and the reverse).
    @Override
    public Number getValueForDisplay(double displayPosition) {
        double delta = logUpperBound.get() - logLowerBound.get();
        if (getSide().isVertical()) {
            return Math.pow(10, (((displayPosition - getHeight()) / -getHeight()) * delta) + logLowerBound.get());
        } else {
            return Math.pow(10, (((displayPosition / getWidth()) * delta) + logLowerBound.get()));
        }
    }

    @Override
    public double getDisplayPosition(Number value) {
        double delta = logUpperBound.get() - logLowerBound.get();
        double deltaV = Math.log10(value.doubleValue()) - logLowerBound.get();
        if (getSide().isVertical()) {
            return (1. - ((deltaV) / delta)) * getHeight();
        } else {
            return ((deltaV) / delta) * getWidth();
        }
    }
}