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

  public TimeSeriesData between2sigma() {
    //2시그마 이내의 데이터값
    TimeSeriesData data = new TimeSeriesData("Custom Analysis");

    double average = getAverage();
    double sigma = getStdDeviation();

    for (Date date : keySet()) {
      double v = get(date);
      if (average - 2 * sigma <= v && v <= average + 2 * sigma) {
        data.put(date, v);
      }
    }

    return data;
  }

  public TimeSeriesData above2simga() {
    //2시그마를 넘는 데이터값
    TimeSeriesData data = new TimeSeriesData("Custom Analysis");

    double average = getAverage();
    double sigma = getStdDeviation();

    for (Date date : keySet()) {
      double above2s = get(date);
      if (average - 2 * sigma < above2s && above2s > average + 2 * sigma) {
        data.put(date, above2s);
      }
    }

    return data;
  }

  public TimeSeriesData between5sigma() {
    //5시그마 이내의 데이터값
    TimeSeriesData data = new TimeSeriesData("Custom Analysis");

    double average = getAverage();
    double sigma = getStdDeviation();

    for (Date date : keySet()) {
      double above5s = get(date);
      if (average - 5 * sigma <= above5s && above5s <= average + 5 * sigma) {
        data.put(date, above5s);
      }
    }

    return data;
  }

  public TimeSeriesData above5simga() {
    //5시그마를 넘어가는 데이터값
    TimeSeriesData data = new TimeSeriesData("Custom Analysis");

    double average = getAverage();
    double sigma = getStdDeviation();

    for (Date date : keySet()) {
      double above5s = get(date);
      if (average - 5 * sigma < above5s && above5s > average + 5 * sigma) {
        data.put(date, above5s);
      }
    }

    return data;
  }

  public TimeSeriesData MovingAverage3day(){
    // 3일치 이동평균
    TimeSeriesData data = new TimeSeriesData("Custom Analysis");

    Calendar calendar = Calendar.getInstance();
    for (Date date : keySet()) {
      calendar.setTime(date);
      calendar.add(Calendar.DAY_OF_WEEK, 3);

      Date endDate = calendar.getTime();

      double v = filterByDate(date, endDate).getAverage();
      data.put(date, v);
    }

    return data;

  }

  public TimeSeriesData customAnalysis() {
    TimeSeriesData data = new TimeSeriesData("Custom Analysis");

    return data;
  }
}
