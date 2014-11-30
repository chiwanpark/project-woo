package com.chiwanpark.woo;

import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.TimeSeriesData;
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
      Observation observation = excelLoaderService.loadExcelFile(file);
      RawDataView view = context.getBean(RawDataView.class, observation);

      mainWindow.getDesktop().add(view);
      view.setSelected(true);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(mainWindow, "파일을 불러오는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      LOG.error(e.getMessage());
    }
  }

  public void drawGraphFromRawObservation(Observation observation) {
    List<TimeSeriesData> dataset = getParameterizedDataSet(observation);
    if (dataset == null) {
      return;
    }

    TimeSeriesChartView view = context.getBean(TimeSeriesChartView.class, dataset.get(0).getName(), dataset);

    try {
      mainWindow.getDesktop().add(view);
      view.setSelected(true);
    } catch (PropertyVetoException e) {
      JOptionPane.showMessageDialog(mainWindow, "그래프를 그리는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      LOG.error(e.getMessage());
    }
  }

  private List<TimeSeriesData> getParameterizedDataSet(Observation observation) {
    ParameterSelectionPanel selectionPanel = context.getBean(ParameterSelectionPanel.class, observation.getDateStart(), observation.getDateEnd());
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

    List<TimeSeriesData> dataset = new ArrayList<>();

    if (selectionPanel.getConductivity()) {
      dataset.add(observation.getConductivityList().filterByDate(rangeStart, rangeEnd));
    } else if (selectionPanel.getTemparature()) {
      dataset.add(observation.getTemperatureList().filterByDate(rangeStart, rangeEnd));
    } else if (selectionPanel.getWaterLevel()) {
      dataset.add(observation.getWaterLevelList().filterByDate(rangeStart, rangeEnd));
    } else {
      JOptionPane.showMessageDialog(mainWindow, "Parameter를 선택하지 않았습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      return null;
    }

    return dataset;
  }

  public void calculateBasicStatistics(Observation observation) {
    List<TimeSeriesData> dataset = getParameterizedDataSet(observation);
    if (dataset == null) {
      return;
    }

    TimeSeriesData data = dataset.get(0);

    BasicStatisticsView view = context.getBean(BasicStatisticsView.class, observation, data);
    try {
      mainWindow.getDesktop().add(view);
      view.setSelected(true);
    } catch (PropertyVetoException e) {
      JOptionPane.showMessageDialog(mainWindow, "기본 통계량을 계산하는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      LOG.error(e.getMessage());
    }
  }
}
