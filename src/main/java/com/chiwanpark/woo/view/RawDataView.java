package com.chiwanpark.woo.view;

import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.table.RawObservationTableModel;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class RawDataView extends JInternalFrame {
  private JTable tblData;
  private JPanel pnContents;
  private JTextField txtName;
  private JTextField txtType;
  private JTextField txtHeight;
  private JTextField txtLatitude;
  private JTextField txtLongitude;

  public RawDataView(Observation observation) {
    super("Raw Data -" + observation.getName(), true, true, true, true);

    txtName.setText(observation.getName());
    txtType.setText(observation.getType());
    txtHeight.setText(String.valueOf(observation.getHeight()) + "m");
    txtLatitude.setText(observation.getLatitude().toString("도", "분", "초"));
    txtLongitude.setText(observation.getLongitude().toString("도", "분", "초"));

    TableModel tblModel = new RawObservationTableModel(observation);
    tblData.setModel(tblModel);

    setContentPane(pnContents);
    setSize(640, 480);
  }
}
