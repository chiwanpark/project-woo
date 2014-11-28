package com.chiwanpark.woo.model.table;

import com.chiwanpark.woo.model.RawObservation;
import com.chiwanpark.woo.model.TimeSeriesDatum;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RawObservationTableModel extends AbstractTableModel {
  private List<TimeSeriesDatum<Double>> waterLevel;
  private List<TimeSeriesDatum<Double>> temperature;
  private List<TimeSeriesDatum<Double>> conductivity;

  private String[] columns = {"관측 날짜", "수위 (m)", "수온 (deg C)", "전기 전도도 (uS/cm)"};

  public RawObservationTableModel(RawObservation observation) {
    waterLevel = new ArrayList<>(observation.getWaterLevelList());
    temperature = new ArrayList<>(observation.getTemperatureList());
    conductivity = new ArrayList<>(observation.getConductivityList());

    TimeSeriesDatum.TimeComparator comparator = new TimeSeriesDatum.TimeComparator();
    Collections.sort(waterLevel, comparator);
    Collections.sort(temperature, comparator);
    Collections.sort(conductivity, comparator);
  }

  @Override
  public int getRowCount() {
    return waterLevel.size();
  }

  @Override
  public int getColumnCount() {
    return columns.length;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case 0:
        return waterLevel.get(rowIndex).getDate();
      case 1:
        return waterLevel.get(rowIndex).getDatum();
      case 2:
        return temperature.get(rowIndex).getDatum();
      case 3:
        return conductivity.get(rowIndex).getDatum();
      default:
        return null;
    }
  }

  @Override
  public String getColumnName(int column) {
    return columns[column];
  }
}
