package com.chiwanpark.woo.model.table;

import com.chiwanpark.woo.model.Observation;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class RawObservationTableModel extends AbstractTableModel {
  private List<Observation.Datum> data;
  private String[] columns = {"관측 날짜", "수위 (m)", "수온 (deg C)", "전기 전도도 (uS/cm)"};

  public RawObservationTableModel(Observation observation) {
    data = observation.getData();
  }

  @Override
  public int getRowCount() {
    return data.size();
  }

  @Override
  public int getColumnCount() {
    return columns.length;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Observation.Datum datum = data.get(rowIndex);

    switch (columnIndex) {
      case 0:
        return datum.getDate();
      case 1:
        return datum.getWaterLevel();
      case 2:
        return datum.getTemperature();
      case 3:
        return datum.getConductivity();
      default:
        return null;
    }
  }

  @Override
  public String getColumnName(int column) {
    return columns[column];
  }
}
