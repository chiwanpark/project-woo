package com.chiwanpark.woo;

import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.ObservationInfo;
import com.chiwanpark.woo.model.TimeSeriesData;
import com.chiwanpark.woo.service.ExcelLoaderService;
import com.chiwanpark.woo.view.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

@Configuration
public class Config {
  public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public static final Pattern POSITION_PATTERN = Pattern.compile("([0-9]+)도 ([0-9]+)분 ([0-9]+)초");
  public static final String DOUBLE_FORMAT = "%.6f";
  public static final String POSITION_FORMAT = "%d도 %d분 %d초";

  @Bean
  public MainWindow mainWindow() {
    return new MainWindow();
  }

  @Bean
  public ExcelLoaderService excelLoaderService() {
    return new ExcelLoaderService();
  }

  @Bean
  public WooController wooController() {
    return new WooController();
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public RawDataView rawDataView(Observation observation) {
    ObservationInfoPanel infoPanel = observationInfoPanel();
    infoPanel.setInfo(observation.getInfo(), observation.getDateStart(), observation.getDateEnd());

    RawDataView view = new RawDataView(observation);
    view.setInfoPanel(infoPanel);

    return view;
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public TimeSeriesChartView timeSeriesChartView(ObservationInfo info, TimeSeriesData data) {
    ObservationInfoPanel infoPanel = observationInfoPanel();
    infoPanel.setInfo(info, data.getDateStart(), data.getDateEnd());

    TimeSeriesChartView view = new TimeSeriesChartView(data);
    view.setInfoPanel(infoPanel);

    return view;
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public ParameterSelectionPanel parameterSelectionPanel(Observation observation) {
    return new ParameterSelectionPanel(observation);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public ObservationInfoPanel observationInfoPanel() {
    return new ObservationInfoPanel();
  }
}
