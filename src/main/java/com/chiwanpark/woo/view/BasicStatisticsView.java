package com.chiwanpark.woo.view;

import com.chiwanpark.woo.model.RawObservation;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicStatisticsView extends JInternalFrame {
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private static final String DOUBLE_FORMAT = "%.6f";

  private JPanel pnContents;
  private JTextField txtName;
  private JTextField txtType;
  private JTextField txtHeight;
  private JTextField txtLatitude;
  private JTextField txtLongitude;
  private JTextField txtTimePeriod;
  private JTextField txtMaximum;
  private JTextField txtMinimum;
  private JTextField txtAverage;
  private JTextField txtStdDeviation;

  private RawObservation rawObservation;

  public BasicStatisticsView(RawObservation rawObservation, String parameter, double maximum, double minimum, double average, double stdDeviation) {
    super("기본 통계량 정보 - " + rawObservation.getName() + " (" + parameter + ")");

    this.rawObservation = rawObservation;

    txtName.setText(rawObservation.getName());
    txtType.setText(rawObservation.getType());
    txtHeight.setText(String.valueOf(rawObservation.getHeight()) + "m");
    txtLatitude.setText(rawObservation.getLatitude().toString("도", "분", "초"));
    txtLongitude.setText(rawObservation.getLongitude().toString("도", "분", "초"));
    txtMaximum.setText(String.format(DOUBLE_FORMAT, maximum));
    txtMinimum.setText(String.format(DOUBLE_FORMAT, minimum));
    txtAverage.setText(String.format(DOUBLE_FORMAT, average));
    txtStdDeviation.setText(String.format(DOUBLE_FORMAT, stdDeviation));

    setTimePeriod();

    setContentPane(pnContents);
    setSize(480, 320);
    setMaximizable(true);
    setVisible(true);
    setClosable(true);
    setResizable(true);
  }

  private void setTimePeriod() {
    Date start = rawObservation.getMinimumDate();
    Date end = rawObservation.getMaximumDate();

    txtTimePeriod.setText(DATE_FORMAT.format(start) + " ~ " + DATE_FORMAT.format(end));
  }
}
