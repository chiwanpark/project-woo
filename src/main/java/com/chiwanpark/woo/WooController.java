package com.chiwanpark.woo;

import com.chiwanpark.woo.model.RawObservation;
import com.chiwanpark.woo.model.TimeSeriesDataset;
import com.chiwanpark.woo.model.TimeSeriesDatum;
import com.chiwanpark.woo.service.ExcelLoaderService;
import com.chiwanpark.woo.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class WooController {
  private static Logger LOG = LoggerFactory.getLogger(WooController.class);

  private @Autowired ApplicationContext context;
  private @Autowired ExcelLoaderService excelLoaderService;
  private @Autowired MainWindow mainWindow;

  public void loadExcelFile(File file) {
    try {
      RawObservation observation = excelLoaderService.loadExcelFile(file);
      RawDataView view = context.getBean(RawDataView.class, observation);

      mainWindow.getDesktop().add(view);
      view.setSelected(true);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(mainWindow, "파일을 불러오는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      LOG.error(e.getMessage());
    }
  }

  public void drawGraphFromRawObservation(RawObservation observation) {
    TimeSeriesDataset dataset = getParameterizedDataSet(observation);
    TimeSeriesChartView view = context.getBean(TimeSeriesChartView.class, "Graph", dataset);

    try {
      mainWindow.getDesktop().add(view);
      view.setSelected(true);
    } catch (PropertyVetoException e) {
      JOptionPane.showMessageDialog(mainWindow, "그래프를 그리는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      LOG.error(e.getMessage());
    }
  }

  private TimeSeriesDataset getParameterizedDataSet(RawObservation observation) {
    ParameterSelectionPanel selectionPanel = context.getBean(ParameterSelectionPanel.class, observation.getMinimumDate(), observation.getMaximumDate());
    int result = JOptionPane.showConfirmDialog(mainWindow, selectionPanel, "Parameter 선택", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (result != 0) {
      LOG.info("User cancels selection of parameter.");
      return null;
    }

    Date rangeStart = selectionPanel.getRangeStart();
    Date rangeEnd = selectionPanel.getRangeEnd();
    LOG.info("Selected date range (" + rangeStart + ", " + rangeEnd + ")");
    if (rangeStart == null || rangeEnd == null || rangeStart.compareTo(rangeEnd) > 0) {
      JOptionPane.showMessageDialog(mainWindow, "날짜 범위가 잘못 되었습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      return null;
    }

    TimeSeriesDataset dataset = new TimeSeriesDataset();

    if (selectionPanel.getConductivity()) {
      dataset.insertData("전기 전도도", filterByDate(observation.getConductivityList(), rangeStart, rangeEnd));
    } else if (selectionPanel.getTemparature()) {
      dataset.insertData("온도", filterByDate(observation.getTemperatureList(), rangeStart, rangeEnd));
    } else if (selectionPanel.getWaterLevel()) {
      dataset.insertData("수위", filterByDate(observation.getWaterLevelList(), rangeStart, rangeEnd));
    } else {
      JOptionPane.showMessageDialog(mainWindow, "Parameter를 선택하지 않았습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      return null;
    }

    return dataset;
  }

  private List<TimeSeriesDatum<Double>> filterByDate(List<TimeSeriesDatum<Double>> data, Date rangeStart, Date rangeEnd) {
    List<TimeSeriesDatum<Double>> result = new ArrayList<>();

    for (TimeSeriesDatum<Double> datum : data) {
      if (rangeStart.compareTo(datum.getDate()) <= 0 && datum.getDate().compareTo(rangeEnd) <= 0) {
        result.add(datum);
      }
    }

    return result;
  }

  public void calculateBasicStatistics(RawObservation observation) {
    TimeSeriesDataset dataset = getParameterizedDataSet(observation);

    List<TimeSeriesDatum<Double>> data = dataset.getData(0);
    double sumOfData = 0;
    double sumOfDataSquared = 0;
    double maximum = Double.MIN_VALUE;
    double minimum = Double.MAX_VALUE;

    for (TimeSeriesDatum<Double> datum : data) {
      double value = datum.getDatum();

      sumOfData += value;
      sumOfDataSquared += value * value;

      if (maximum < value) {
        maximum = value;
      }
      if (minimum > value) {
        minimum = value;
      }
    }

    double average = sumOfData / data.size();
    double stdDeviation = Math.sqrt(sumOfDataSquared / data.size() - average * average);

    BasicStatisticsView view = context.getBean(BasicStatisticsView.class, observation, dataset.getSeriesName(0), maximum, minimum, average, stdDeviation);
    try {
      mainWindow.getDesktop().add(view);
      view.setSelected(true);
    } catch (PropertyVetoException e) {
      JOptionPane.showMessageDialog(mainWindow, "기본 통계량을 계산하는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      LOG.error(e.getMessage());
    }
  }
}
