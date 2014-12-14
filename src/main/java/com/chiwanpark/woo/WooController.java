package com.chiwanpark.woo;

import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.TimeSeriesData;
import com.chiwanpark.woo.model.Tuple3;
import com.chiwanpark.woo.service.BackgroundTaskService;
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
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.Date;
import java.util.concurrent.Callable;

@Controller
public class WooController {
  private static Logger LOG = LoggerFactory.getLogger(WooController.class);

  private @Autowired ApplicationContext context;
  private @Autowired BackgroundTaskService backgroundTaskService;
  private @Autowired ExcelLoaderService excelLoaderService;
  private @Autowired MainWindow mainWindow;

  public void loadExcelFile(final File file) {
    backgroundTaskService.runTaskInBackgroundWithDialog(new Callable<Observation>() {
      @Override
      public Observation call() throws Exception {
        return excelLoaderService.loadExcelFile(file);
      }
    }, new SuccessCallback<Observation>() {
      @Override
      public void onSuccess(Observation observation) {
        RawDataView view = context.getBean(RawDataView.class, observation);

        mainWindow.getDesktop().add(view);

        try {
          view.setSelected(true);
        } catch (PropertyVetoException e) {
          throw new RuntimeException(e);
        }
      }
    }, new FailureCallback() {
      @Override
      public void onFailure(Throwable e) {
        JOptionPane.showMessageDialog(mainWindow, "파일을 불러오는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
        LOG.error("Loading excel file failed.");
      }
    });
  }

  public void drawGraphFromRawObservation(Observation observation) {
    final Tuple3<Integer, Date, Date> parameter = getBasicAnalysisParameter(observation);
    if (parameter == null) {
      return;
    }

    TimeSeriesChartView view = context.getBean(TimeSeriesChartView.class, observation.getInfo(), observation.getData().get(parameter.getV1()));

    try {
      mainWindow.getDesktop().add(view);
      view.setSelected(true);
    } catch (PropertyVetoException e) {
      JOptionPane.showMessageDialog(mainWindow, "그래프를 그리는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      LOG.error("Drawing graph failed.", e);
    }
  }

  private Tuple3<Integer, Date, Date> getBasicAnalysisParameter(Observation observation) {
    ParameterSelectionPanel selectionPanel = context.getBean(ParameterSelectionPanel.class, observation);
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

    int selected = selectionPanel.getSelected();
    if (selected != -1) {
      return new Tuple3<>(selected, rangeStart, rangeEnd);
    } else {
      JOptionPane.showMessageDialog(mainWindow, "Parameter를 선택하지 않았습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      return null;
    }
  }

  public void doAnalysis(Observation observation) {
    Tuple3<Integer, Date, Date> parameter = getBasicAnalysisParameter(observation);
    if (parameter == null) {
      return;
    }

    TimeSeriesData result = observation.getData().get(parameter.getV1()).filterByDate(parameter.getV2(), parameter.getV3()).customAnalysis();

    TimeSeriesChartView view = context.getBean(TimeSeriesChartView.class, observation.getInfo(), result);

    try {
      mainWindow.getDesktop().add(view);
      view.setSelected(true);
    } catch (PropertyVetoException e) {
      JOptionPane.showMessageDialog(mainWindow, "그래프를 그리는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
      LOG.error("Drawing graph failed.", e);
    }
  }
}
