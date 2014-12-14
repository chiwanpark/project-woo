package com.chiwanpark.woo;

import com.chiwanpark.woo.model.*;
import com.chiwanpark.woo.service.BackgroundTaskService;
import com.chiwanpark.woo.service.ExcelLoaderService;
import com.chiwanpark.woo.view.*;
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

  public void drawGraphFromRawObservation(final Observation observation) {
    final Tuple3<Integer, Date, Date> parameter = getBasicAnalysisParameter(observation);
    if (parameter == null) {
      return;
    }

    backgroundTaskService.runTaskInBackgroundWithDialog(new Callable<Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>>>() {
      @Override
      public Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>> call() throws Exception {
        TimeSeriesData data = observation.getData().get(parameter.getV1()).dataInDateRange(parameter.getV2(), parameter.getV3());

        return new Tuple2<>(data, data.getBasicAnalysis());
      }
    }, new SuccessCallback<Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>>>() {
      @Override
      public void onSuccess(Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>> output) {
        TimeSeriesChartView view = context.getBean(TimeSeriesChartView.class, observation.getInfo(), output.getV1(), output.getV2());
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
        JOptionPane.showMessageDialog(mainWindow, "그래프를 그리는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
        LOG.error("Drawing graph failed.", e);
      }
    });
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

  public void calculateMovingAverage(final Observation observation) {
    final Tuple3<Integer, Date, Date> basicParameter = getBasicAnalysisParameter(observation);

    MovingAverageParameterPanel parameterPanel = context.getBean(MovingAverageParameterPanel.class);
    int result = JOptionPane.showConfirmDialog(mainWindow, parameterPanel, "Set date range", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (result != 0) {
      LOG.info("User cancels selection of parameter.");
      return;
    }

    final Tuple2<Integer, Integer> timeParameter = parameterPanel.getParameter();

    backgroundTaskService.runTaskInBackground(new Callable<Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>>>() {
      @Override
      public Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>> call() throws Exception {
        TimeSeriesData data = observation.getData().get(basicParameter.getV1()).dataInDateRange(basicParameter.getV2(), basicParameter.getV3())
            .movingAverage(timeParameter.getV1(), timeParameter.getV2());

        return new Tuple2<>(data, data.getBasicAnalysis());
      }
    }, new SuccessCallback<Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>>>() {
      @Override
      public void onSuccess(Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>> output) {
        TimeSeriesChartView view = context.getBean(TimeSeriesChartView.class, observation.getInfo(), output.getV1(), output.getV2());
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
        JOptionPane.showMessageDialog(mainWindow, "그래프를 그리는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
        LOG.error("Drawing graph failed.", e);
      }
    });
  }

  public void filterValueRange(final Observation observation, final boolean inRange) {
    final Tuple3<Integer, Date, Date> basicParameter = getBasicAnalysisParameter(observation);
    if (basicParameter == null) {
      LOG.info("User cancels selection of parameter.");
      return;
    }

    backgroundTaskService.runTaskInBackgroundWithDialog(new Callable<Tuple4<Double, Double, Double, Double>>() {
      @Override
      public Tuple4<Double, Double, Double, Double> call() throws Exception {
        return observation.getData().get(basicParameter.getV1()).dataInDateRange(basicParameter.getV2(), basicParameter.getV3()).getBasicAnalysis();
      }
    }, new SuccessCallback<Tuple4<Double, Double, Double, Double>>() {
      @Override
      public void onSuccess(Tuple4<Double, Double, Double, Double> value) {
        final ValueRangeParameterPanel parameterPanel = context.getBean(ValueRangeParameterPanel.class, value);
        int result = JOptionPane.showConfirmDialog(mainWindow, parameterPanel, "Set value range", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result != 0) {
          LOG.info("User cancels selection of parameter.");
          return;
        }

        final Tuple2<Double, Double> parameter = parameterPanel.getParameter();
        backgroundTaskService.runTaskInBackgroundWithDialog(new Callable<Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>>>() {
          @Override
          public Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>> call() throws Exception {
            TimeSeriesData data = observation.getData().get(basicParameter.getV1()).dataInDateRange(basicParameter.getV2(), basicParameter.getV3());

            if (inRange) {
              data = data.dataInValueRange(parameter.getV1(), parameter.getV2());
            } else {
              data = data.dataExceptValueRange(parameter.getV1(), parameter.getV2());
            }

            return new Tuple2<>(data, data.getBasicAnalysis());
          }
        }, new SuccessCallback<Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>>>() {
          @Override
          public void onSuccess(Tuple2<TimeSeriesData, Tuple4<Double, Double, Double, Double>> output) {
            TimeSeriesChartView view = context.getBean(TimeSeriesChartView.class, observation.getInfo(), output.getV1(), output.getV2());
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
            JOptionPane.showMessageDialog(mainWindow, "그래프를 그리는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
            LOG.error("Drawing graph failed.", e);
          }
        });
      }
    }, new FailureCallback() {
      @Override
      public void onFailure(Throwable e) {
        JOptionPane.showMessageDialog(mainWindow, "기본 통계량 계산에 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
        LOG.error("Calculation of basic analysis is failed.", e);
      }
    });
  }

  public void doAnalysis(Observation observation) {
    Tuple3<Integer, Date, Date> parameter = getBasicAnalysisParameter(observation);
    if (parameter == null) {
      return;
    }

    TimeSeriesData result = observation.getData().get(parameter.getV1()).dataInDateRange(parameter.getV2(), parameter.getV3()).customAnalysis();

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
