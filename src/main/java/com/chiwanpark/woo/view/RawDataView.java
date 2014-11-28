package com.chiwanpark.woo.view;

import com.chiwanpark.woo.WooController;
import com.chiwanpark.woo.model.RawObservation;
import com.chiwanpark.woo.model.TimeSeriesDataset;
import com.chiwanpark.woo.model.TimeSeriesDatum;
import com.chiwanpark.woo.model.table.RawObservationTableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class RawDataView extends JInternalFrame {
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

  private JTable tblData;
  private JPanel pnContents;
  private JTextField txtName;
  private JTextField txtType;
  private JTextField txtHeight;
  private JTextField txtLatitude;
  private JTextField txtLongitude;
  private JButton btnGraph;
  private JTextField txtTimePeriod;

  private @Autowired WooController controller;

  private RawObservation rawObservation;

  public RawDataView(RawObservation rawObservation) {
    super("Raw Data -" + rawObservation.getName(), true, true, true, true);

    this.rawObservation = rawObservation;

    txtName.setText(rawObservation.getName());
    txtType.setText(rawObservation.getType());
    txtHeight.setText(String.valueOf(rawObservation.getHeight()) + "m");
    txtLatitude.setText(rawObservation.getLatitude().toString("도", "분", "초"));
    txtLongitude.setText(rawObservation.getLongitude().toString("도", "분", "초"));

    tblData.setModel(new RawObservationTableModel(rawObservation));

    setTimePeriod();

    createBtnGraphAction();

    setContentPane(pnContents);
    setSize(640, 480);
    setVisible(true);
  }

  private void createBtnGraphAction() {
    btnGraph.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        TimeSeriesDataset dataset = new TimeSeriesDataset();
        dataset.insertData("conductivity", rawObservation.getConductivityList());

        controller.drawGraph("Conductivity!", dataset);
      }
    });
  }

  private void setTimePeriod() {
    List<TimeSeriesDatum<Double>> waterLevel = rawObservation.getWaterLevelList();
    Date start = waterLevel.get(0).getDate(), end = waterLevel.get(0).getDate();

    for (TimeSeriesDatum<Double> datum : waterLevel) {
      Date date = datum.getDate();

      if (start.compareTo(date) > 0) {
        start = date;
      }
      if (end.compareTo(date) < 0) {
        end = date;
      }
    }

    txtTimePeriod.setText(DATE_FORMAT.format(start) + " ~ " + DATE_FORMAT.format(end));
  }
}
