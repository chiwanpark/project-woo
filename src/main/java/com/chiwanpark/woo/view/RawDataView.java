package com.chiwanpark.woo.view;

import com.chiwanpark.woo.WooController;
import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.table.RawObservationTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Component
public class RawDataView extends JInternalFrame {
  private JTable tblData;
  private JPanel pnContents;
  private JButton btnGraph;
  private JButton btnAnalysis;
  private JPanel pnWrapInfo;
  private JButton btnMovingAverage;

  private @Autowired WooController controller;

  private Observation observation;

  public RawDataView(Observation observation) {
    super("Raw Data -" + observation.getInfo().getName(), true, true, true, true);

    this.observation = observation;

    tblData.setModel(new RawObservationTableModel(observation));

    createBtnGraphAction();
    createBtnAnalysisAction();
    createBtnMovingAverageAction();

    setContentPane(pnContents);
    setSize(640, 480);
    setVisible(true);
  }

  public void setInfoPanel(ObservationInfoPanel infoPanel) {
    pnWrapInfo.setLayout(new BoxLayout(pnWrapInfo, BoxLayout.LINE_AXIS));
    pnWrapInfo.add(infoPanel);
  }

  private void createBtnGraphAction() {
    btnGraph.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.drawGraphFromRawObservation(observation);
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

  private void createBtnMovingAverageAction() {
    btnMovingAverage.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        controller.calculateMovingAverage(observation);
      }
    });
  }
}
