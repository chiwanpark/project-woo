package com.chiwanpark.woo.view;

import com.chiwanpark.woo.Config;
import com.chiwanpark.woo.WooController;
import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.table.RawObservationTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

@Component
public class RawDataView extends JInternalFrame {
  private JTable tblData;
  private JPanel pnContents;
  private JTextField txtName;
  private JTextField txtType;
  private JTextField txtHeight;
  private JTextField txtLatitude;
  private JTextField txtLongitude;
  private JButton btnGraph;
  private JTextField txtTimePeriod;
  private JButton btnStatistics;
  private JButton btnAnalysis;

  private @Autowired WooController controller;

  private Observation observation;

  public RawDataView(Observation observation) {
    super("Raw Data -" + observation.getName(), true, true, true, true);

    this.observation = observation;

    txtName.setText(observation.getName());
    txtType.setText(observation.getType());
    txtHeight.setText(String.valueOf(observation.getHeight()) + "m");
    txtLatitude.setText(observation.getLatitude().toString(Config.POSITION_FORMAT));
    txtLongitude.setText(observation.getLongitude().toString(Config.POSITION_FORMAT));

    tblData.setModel(new RawObservationTableModel(observation));

    setTimePeriod();

    createBtnGraphAction();
    createBtnStatisticsAction();
    createBtnAnalysisAction();

    setContentPane(pnContents);
    setSize(640, 480);
    setVisible(true);
  }

  private void createBtnGraphAction() {
    btnGraph.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.drawGraphFromRawObservation(observation);
      }
    });
  }

  private void createBtnStatisticsAction() {
    btnStatistics.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.calculateBasicStatistics(observation);
      }
    });
  }

  private void createBtnAnalysisAction() {
    btnAnalysis.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.doAnalysis(observation);
      }
    });
  }

  private void setTimePeriod() {
    Date start = observation.getDateStart();
    Date end = observation.getDateEnd();

    txtTimePeriod.setText(Config.DATE_FORMAT.format(start) + " ~ " + Config.DATE_FORMAT.format(end));
  }
}
