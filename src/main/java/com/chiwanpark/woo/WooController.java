package com.chiwanpark.woo;

import com.chiwanpark.woo.model.RawObservation;
import com.chiwanpark.woo.model.TimeSeriesDataset;
import com.chiwanpark.woo.model.TimeSeriesDatum;
import com.chiwanpark.woo.service.ExcelLoaderService;
import com.chiwanpark.woo.view.MainWindow;
import com.chiwanpark.woo.view.ParameterSelectionPanel;
import com.chiwanpark.woo.view.RawDataView;
import com.chiwanpark.woo.view.TimeSeriesChartView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

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
      JOptionPane.showMessageDialog(mainWindow, "파일을 불러오는데 실패했습니다!", "자료 불러오기", JOptionPane.ERROR_MESSAGE);
      LOG.error(e.getMessage());
    }
  }

  public void drawGraphFromRawObservation(RawObservation observation) {
    ParameterSelectionPanel selectionPanel = context.getBean(ParameterSelectionPanel.class, observation.getMinimumDate(), observation.getMaximumDate());
    int result = JOptionPane.showConfirmDialog(mainWindow, selectionPanel, "Parameter 선택", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (result != 0) {
      LOG.info("User cancels drawing graph.");
      return;
    }

    Date rangeStart = selectionPanel.getRangeStart();
    Date rangeEnd = selectionPanel.getRangeEnd();
    LOG.info("Selected date range (" + rangeStart + ", " + rangeEnd + ")");

    List<String> series = new ArrayList<>();

    TimeSeriesDataset dataset = new TimeSeriesDataset();
    if (selectionPanel.getConductivity()) {
      series.add("전기 전도도");
      dataset.insertData("전기 전도도", filterByDate(observation.getConductivityList(), rangeStart, rangeEnd));
    }
    if (selectionPanel.getTemparature()) {
      series.add("온도");
      dataset.insertData("온도", filterByDate(observation.getTemperatureList(), rangeStart, rangeEnd));
    }
    if (selectionPanel.getWaterLevel()) {
      series.add("수위");
      dataset.insertData("수위", filterByDate(observation.getWaterLevelList(), rangeStart, rangeEnd));
    }

    TimeSeriesChartView view = context.getBean(TimeSeriesChartView.class, StringUtils.collectionToDelimitedString(series, ", "), dataset);

    try {
      mainWindow.getDesktop().add(view);
      view.setSelected(true);
    } catch (PropertyVetoException e) {
      JOptionPane.showMessageDialog(mainWindow, "그래프를 그리는데 실패했습니다!", "그래프 그리기", JOptionPane.ERROR_MESSAGE);
      LOG.error(e.getMessage());
    }
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
}
