package com.chiwanpark.woo.model;

import java.util.ArrayList;
import java.util.List;

public class TimeSeriesDataset {
  private List<List<TimeSeriesDatum<Double>>> data;
  private List<String> series;

  public TimeSeriesDataset() {
    data = new ArrayList<>();
    series = new ArrayList<>();
  }

  public void insertData(String series, List<TimeSeriesDatum<Double>> data) {
    this.data.add(data);
    this.series.add(series);
  }

  public int size() {
    return data.size();
  }

  public String getSeriesName(int i) {
    return series.get(i);
  }

  public List<TimeSeriesDatum<Double>> getData(int i) {
    return data.get(i);
  }
}
