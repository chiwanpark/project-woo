package com.chiwanpark.woo.model;

public class ObservationInfo {
  private String name;
  private String type;
  private double height;
  private Tuple3<Integer, Integer, Integer> longitude;
  private Tuple3<Integer, Integer, Integer> latitude;

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
}
