package com.chiwanpark.woo;

import com.chiwanpark.woo.model.RawObservation;
import com.chiwanpark.woo.model.TimeSeriesDataset;
import com.chiwanpark.woo.service.ExcelLoaderService;
import com.chiwanpark.woo.view.MainWindow;
import com.chiwanpark.woo.view.RawDataView;
import com.chiwanpark.woo.view.TimeSeriesChartView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.File;

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

  public void drawGraph(String title, TimeSeriesDataset dataset) {
    TimeSeriesChartView view = context.getBean(TimeSeriesChartView.class, title, dataset);

    try {
      mainWindow.getDesktop().add(view);
      view.setSelected(true);
    } catch (PropertyVetoException e) {
      JOptionPane.showMessageDialog(mainWindow, "그래프를 그리는데 실패했습니다!", "그래프 그리기", JOptionPane.ERROR_MESSAGE);
      LOG.error(e.getMessage());
    }
  }
}
