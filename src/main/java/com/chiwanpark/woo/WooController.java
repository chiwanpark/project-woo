package com.chiwanpark.woo;

import com.chiwanpark.woo.model.RawObservation;
import com.chiwanpark.woo.model.TimeSeriesDataset;
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
    ParameterSelectionPanel selectionPanel = context.getBean(ParameterSelectionPanel.class);
    int result = JOptionPane.showConfirmDialog(mainWindow, selectionPanel, "Parameter 선택", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (result != 0) {
      LOG.info("User cancels drawing graph.");
      return;
    }

    List<String> series = new ArrayList<>();

    TimeSeriesDataset dataset = new TimeSeriesDataset();
    if (selectionPanel.getConductivity()) {
      series.add("전기 전도도");
      dataset.insertData("전기 전도도", observation.getConductivityList());
    }
    if (selectionPanel.getTemparature()) {
      series.add("온도");
      dataset.insertData("온도", observation.getTemperatureList());
    }
    if (selectionPanel.getWaterLevel()) {
      series.add("수위");
      dataset.insertData("수위", observation.getWaterLevelList());
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
}
