package com.chiwanpark.woo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Observation {
  private String name;
  private String type;
  private double height;
  private Tuple3<Integer, Integer, Integer> longitude;
  private Tuple3<Integer, Integer, Integer> latitude;

  private Date dateStart;
  private Date dateEnd;

  private List<TimeSeriesData> data;

  public Observation() {
    data = new ArrayList<>();
    dateStart = null;
    dateEnd = null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public double getHeight() {
    return height;
  }

  public void setHeight(double height) {
    this.height = height;
  }

  public Tuple3<Integer, Integer, Integer> getLongitude() {
    return longitude;
  }

  public void setLongitude(Tuple3<Integer, Integer, Integer> longitude) {
    this.longitude = longitude;
  }

  public Tuple3<Integer, Integer, Integer> getLatitude() {
    return latitude;
  }

  public void setLatitude(Tuple3<Integer, Integer, Integer> latitude) {
    this.latitude = latitude;
  }

  public void insertData(TimeSeriesData data) {
    this.data.add(data);

    if (dateStart == null || dateStart.after(data.getDateStart())) {
      dateStart = data.getDateStart();
    }
    if (dateEnd == null || dateEnd.before(data.getDateEnd())) {
      dateEnd = data.getDateEnd();
    }
  }

  public List<TimeSeriesData> getData() {
    return data;
  }

  public Date getDateStart() {
    return dateStart;
  }

  public Date getDateEnd() {
    return dateEnd;
  }
}
