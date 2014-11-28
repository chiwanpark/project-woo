package com.chiwanpark.woo.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Observation {
  private String name;
  private String type;
  private double height;
  private Tuple<Integer, Integer, Integer> longitude;
  private Tuple<Integer, Integer, Integer> latitude;

  public Observation() {
    data = new LinkedList<>();
  }

  public static class Datum {
    private Date date;
    private double waterLevel;
    private double temperature;
    private double conductivity;

    public Datum(Date date, double waterLevel, double temperature, double conductivity) {
      this.date = date;
      this.waterLevel = waterLevel;
      this.temperature = temperature;
      this.conductivity = conductivity;
    }

    public Date getDate() {
      return date;
    }

    public double getWaterLevel() {
      return waterLevel;
    }

    public double getTemperature() {
      return temperature;
    }

    public double getConductivity() {
      return conductivity;
    }
  }

  private List<Datum> data;

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public double getHeight() {
    return height;
  }

  public Tuple<Integer, Integer, Integer> getLongitude() {
    return longitude;
  }

  public Tuple<Integer, Integer, Integer> getLatitude() {
    return latitude;
  }

  public List<Datum> getData() {
    return data;
  }

  public void insertDatum(Datum datum) {
    data.add(datum);
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setHeight(double height) {
    this.height = height;
  }

  public void setLongitude(Tuple<Integer, Integer, Integer> longitude) {
    this.longitude = longitude;
  }

  public void setLatitude(Tuple<Integer, Integer, Integer> latitude) {
    this.latitude = latitude;
  }
}
