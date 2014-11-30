package com.chiwanpark.woo.view;

import com.chiwanpark.woo.Config;
import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.TimeSeriesData;

import javax.swing.*;

public class BasicStatisticsView extends JInternalFrame {
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

  public BasicStatisticsView(Observation observation, TimeSeriesData data) {
    super("기본 통계량 정보 - " + observation.getName() + " (" + data.getName() + ")");

    txtName.setText(observation.getName());
    txtType.setText(observation.getType());
    txtHeight.setText(String.valueOf(observation.getHeight()) + "m");
    txtLatitude.setText(observation.getLatitude().toString(Config.POSITION_FORMAT));
    txtLongitude.setText(observation.getLongitude().toString(Config.POSITION_FORMAT));
    txtMaximum.setText(String.format(Config.DOUBLE_FORMAT, data.getMaximum()));
    txtMinimum.setText(String.format(Config.DOUBLE_FORMAT, data.getMinimum()));
    txtAverage.setText(String.format(Config.DOUBLE_FORMAT, data.getAverage()));
    txtStdDeviation.setText(String.format(Config.DOUBLE_FORMAT, data.getStdDeviation()));
    txtTimePeriod.setText(Config.DATE_FORMAT.format(data.getDateStart()) + " ~ " + Config.DATE_FORMAT.format(data.getDateEnd()));

    setContentPane(pnContents);
    setSize(480, 320);
    setMaximizable(true);
    setVisible(true);
    setClosable(true);
    setResizable(true);
  }
}
