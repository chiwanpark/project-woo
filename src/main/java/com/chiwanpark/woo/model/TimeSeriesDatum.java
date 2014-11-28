package com.chiwanpark.woo.model;

import java.util.Comparator;
import java.util.Date;

public class TimeSeriesDatum<T> {
  private Date date;
  private T datum;

  public TimeSeriesDatum(Date date, T datum) {
    this.date = date;
    this.datum = datum;
  }

  public Date getDate() {
    return date;
  }

  public T getDatum() {
    return datum;
  }

  public static class TimeComparator implements Comparator<TimeSeriesDatum> {
    @Override
    public int compare(TimeSeriesDatum o1, TimeSeriesDatum o2) {
      return o1.getDate().compareTo(o2.getDate());
    }
  }
}
