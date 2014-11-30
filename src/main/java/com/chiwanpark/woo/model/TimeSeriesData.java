package com.chiwanpark.woo.model;

import java.util.ArrayList;
import java.util.Date;

public class TimeSeriesData extends ArrayList<TimeSeriesDatum> {
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

    Date start = get(0).getDate();
    for (TimeSeriesDatum datum : this) {
      if (start.after(datum.getDate())) {
        start = datum.getDate();
      }
    }

    return start;
  }

  public Date getDateEnd() {
    if (size() == 0) {
      return null;
    }

    Date end = get(0).getDate();
    for (TimeSeriesDatum datum : this) {
      if (end.before(datum.getDate())) {
        end = datum.getDate();
      }
    }

    return end;
  }

  public double getMaximum() {
    double maximum = Double.MIN_VALUE;

    for (TimeSeriesDatum datum : this) {
      if (maximum < datum.getDatum()) {
        maximum = datum.getDatum();
      }
    }

    return maximum;
  }

  public double getMinimum() {
    double minimum = Double.MAX_VALUE;

    for (TimeSeriesDatum datum : this) {
      if (minimum > datum.getDatum()) {
        minimum = datum.getDatum();
      }
    }

    return minimum;
  }

  public double getAverage() {
    double sum = 0;

    for (TimeSeriesDatum datum : this) {
      sum += datum.getDatum();
    }

    return sum / size();
  }

  public double getStdDeviation() {
    double sumOfSquared = 0;

    for (TimeSeriesDatum datum : this) {
      double value = datum.getDatum();
      sumOfSquared += value * value;
    }

    double average = getAverage();
    return sumOfSquared / size() - average * average;
  }

  public TimeSeriesData filterByDate(Date start, Date end) {
    TimeSeriesData result = new TimeSeriesData(name);

    for (TimeSeriesDatum datum : this) {
      if (start.compareTo(datum.getDate()) <= 0 && datum.getDate().compareTo(end) <= 0) {
        result.add(datum);
      }
    }

    return result;
  }
}
