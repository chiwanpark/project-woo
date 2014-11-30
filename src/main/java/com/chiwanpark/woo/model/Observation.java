package com.chiwanpark.woo.model;

import java.util.Date;

public class Observation {
  private String name;
  private String type;
  private double height;
  private Tuple3<Integer, Integer, Integer> longitude;
  private Tuple3<Integer, Integer, Integer> latitude;
  private TimeSeriesData waterLevelList;
  private TimeSeriesData temperatureList;
  private TimeSeriesData conductivityList;

  public Observation() {
    waterLevelList = new TimeSeriesData("수위 (m)");
    temperatureList = new TimeSeriesData("온도 (deg C)");
    conductivityList = new TimeSeriesData("전기 전도도 (uS/cm)");
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

  public TimeSeriesData getWaterLevelList() {
    return waterLevelList;
  }

  public void insertWaterLevel(TimeSeriesDatum waterLevel) {
    waterLevelList.add(waterLevel);
  }

  public TimeSeriesData getTemperatureList() {
    return temperatureList;
  }

  public void insertTemperature(TimeSeriesDatum temperature) {
    temperatureList.add(temperature);
  }

  public TimeSeriesData getConductivityList() {
    return conductivityList;
  }

  public void insertConductivity(TimeSeriesDatum conductivity) {
    conductivityList.add(conductivity);
  }

  public Date getDateStart() {
    return waterLevelList.getDateStart();
  }

  public Date getDateEnd() {
    return waterLevelList.getDateEnd();
  }
}
