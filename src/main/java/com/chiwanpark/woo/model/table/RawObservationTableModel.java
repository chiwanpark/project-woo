package com.chiwanpark.woo.model.table;

import com.chiwanpark.woo.Config;
import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.TimeSeriesData;

import javax.swing.table.AbstractTableModel;
import java.util.*;

public class RawObservationTableModel extends AbstractTableModel {
  private Observation observation;
  private int rowCount;
  private List<Date> dateList;

  public RawObservationTableModel(Observation observation) {
    this.observation = observation;

    Set<Date> dateSet = new HashSet<>();

    for (TimeSeriesData data : observation.getData()) {
      if (rowCount < data.size()) {
        rowCount = data.size();
      }

      dateSet.addAll(data.keySet());
    }

    dateList = new ArrayList<>(dateSet);
    Collections.sort(dateList);
  }

  @Override
  public int getRowCount() {
    return rowCount;
  }

  @Override
  public int getColumnCount() {
    return observation.getData().size() + 1;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex) {
      case 0:
        return Config.DATE_FORMAT.format(dateList.get(rowIndex));
      case 1:
      case 2:
      case 3:
      case 4:
        Double value = observation.getData().get(columnIndex - 1).get(dateList.get(rowIndex));
        if (value == null) {
          return "";
        }
        return value;
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
      case 2:
      case 3:
      case 4:
        return observation.getData().get(column - 1).getName();
      default:
        return null;
    }
  }
}
