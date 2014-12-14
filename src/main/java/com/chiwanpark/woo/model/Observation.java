package com.chiwanpark.woo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Observation {
  private ObservationInfo info;

  private Date dateStart;
  private Date dateEnd;

  private List<TimeSeriesData> data;

  public Observation() {
    data = new ArrayList<>();
    dateStart = null;
    dateEnd = null;
  }

  public ObservationInfo getInfo() {
    return info;
  }

  public void setInfo(ObservationInfo info) {
    this.info = info;
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
