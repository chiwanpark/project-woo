package com.chiwanpark.woo.model.table;

import com.chiwanpark.woo.Config;
import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.TimeSeriesData;
import com.chiwanpark.woo.model.TimeSeriesDatum;

import javax.swing.table.AbstractTableModel;
import java.util.Collections;

public class RawObservationTableModel extends AbstractTableModel {
  private TimeSeriesData waterLevel;
  private TimeSeriesData temperature;
  private TimeSeriesData conductivity;

  public RawObservationTableModel(Observation observation) {
    waterLevel = new TimeSeriesData(observation.getWaterLevelList());
    temperature = new TimeSeriesData(observation.getTemperatureList());
    conductivity = new TimeSeriesData(observation.getConductivityList());

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
    return 4;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case 0:
        return Config.DATE_FORMAT.format(waterLevel.get(rowIndex).getDate());
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
    switch (column) {
      case 0:
        return "관측 날짜";
      case 1:
        return waterLevel.getName();
      case 2:
        return temperature.getName();
      case 3:
        return conductivity.getName();
      default:
        return null;
    }
  }
}
