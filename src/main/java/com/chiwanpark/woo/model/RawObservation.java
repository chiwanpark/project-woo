package com.chiwanpark.woo.model;

import java.util.ArrayList;
import java.util.List;

public class RawObservation {
  private String name;
  private String type;
  private double height;
  private Tuple<Integer, Integer, Integer> longitude;
  private Tuple<Integer, Integer, Integer> latitude;
  private List<TimeSeriesDatum<Double>> waterLevelList;
  private List<TimeSeriesDatum<Double>> temperatureList;
  private List<TimeSeriesDatum<Double>> conductivityList;

  public RawObservation() {
    waterLevelList = new ArrayList<>();
    temperatureList = new ArrayList<>();
    conductivityList = new ArrayList<>();
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

  public Tuple<Integer, Integer, Integer> getLongitude() {
    return longitude;
  }

  public void setLongitude(Tuple<Integer, Integer, Integer> longitude) {
    this.longitude = longitude;
  }

  public Tuple<Integer, Integer, Integer> getLatitude() {
    return latitude;
  }

  public void setLatitude(Tuple<Integer, Integer, Integer> latitude) {
    this.latitude = latitude;
  }

  public List<TimeSeriesDatum<Double>> getWaterLevelList() {
    return waterLevelList;
  }

  public void insertWaterLevel(TimeSeriesDatum<Double> waterLevel) {
    waterLevelList.add(waterLevel);
  }

  public List<TimeSeriesDatum<Double>> getTemperatureList() {
    return temperatureList;
  }

  public void insertTemperature(TimeSeriesDatum<Double> temperature) {
    temperatureList.add(temperature);
  }

  public List<TimeSeriesDatum<Double>> getConductivityList() {
    return conductivityList;
  }

  public void insertConductivity(TimeSeriesDatum<Double> conductivity) {
    conductivityList.add(conductivity);
  }
}
