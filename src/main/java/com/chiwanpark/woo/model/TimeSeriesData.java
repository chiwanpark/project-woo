package com.chiwanpark.woo.model;

import java.util.*;

public class TimeSeriesData extends HashMap<Date, Double> {
  private String name;

  public TimeSeriesData(String name) {
    this.name = name;
  }

  public TimeSeriesData(TimeSeriesData c) {
    super(c);
    this.name = c.getName();
  }

  public String getName() {
    return name;
  }

  public Date getDateStart() {
    if (size() == 0) {
      return null;
    }

    Date start = null;
    for (Date date : this.keySet()) {
      if (start == null || start.after(date)) {
        start = date;
      }
    }

    return start;
  }

  public Date getDateEnd() {
    if (size() == 0) {
      return null;
    }

    Date end = null;
    for (Date date : this.keySet()) {
      if (end == null || end.before(date)) {
        end = date;
      }
    }

    return end;
  }

  public double getMaximum() {
    double maximum = Double.MIN_VALUE;

    for (Double datum : this.values()) {
      if (maximum < datum) {
        maximum = datum;
      }
    }

    return maximum;
  }

  public double getMinimum() {
    double minimum = Double.MAX_VALUE;

    for (Double datum : this.values()) {
      if (minimum > datum) {
        minimum = datum;
      }
    }

    return minimum;
  }

  public double getAverage() {
    double sum = 0;

    for (Double datum : this.values()) {
      sum += datum;
    }

    return sum / size();
  }

  public double getStdDeviation() {
    double sumOfSquared = 0;

    for (Double datum : this.values()) {
      double value = datum;
      sumOfSquared += value * value;
    }

    double average = getAverage();
    return sumOfSquared / size() - average * average;
  }

  public TimeSeriesData filterByDate(Date start, Date end) {
    TimeSeriesData result = new TimeSeriesData(name);

    for (Date date : this.keySet()) {
      if (start.compareTo(date) <= 0 && date.compareTo(end) <= 0) {
        result.put(date, get(date));
      }
    }

    return result;
  }
}
