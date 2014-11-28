package com.chiwanpark.woo.view;

import com.chiwanpark.woo.model.RawObservation;
import com.chiwanpark.woo.model.table.RawObservationTableModel;

import javax.swing.*;

public class RawDataView extends JInternalFrame {
  private JTable tblData;
  private JPanel pnContents;
  private JTextField txtName;
  private JTextField txtType;
  private JTextField txtHeight;
  private JTextField txtLatitude;
  private JTextField txtLongitude;

  public RawDataView(RawObservation rawObservation) {
    super("Raw Data -" + rawObservation.getName(), true, true, true, true);

    txtName.setText(rawObservation.getName());
    txtType.setText(rawObservation.getType());
    txtHeight.setText(String.valueOf(rawObservation.getHeight()) + "m");
    txtLatitude.setText(rawObservation.getLatitude().toString("도", "분", "초"));
    txtLongitude.setText(rawObservation.getLongitude().toString("도", "분", "초"));

    tblData.setModel(new RawObservationTableModel(rawObservation));

    setContentPane(pnContents);
    setSize(640, 480);
  }
}
