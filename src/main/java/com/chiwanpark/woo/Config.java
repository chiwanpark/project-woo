package com.chiwanpark.woo;

import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.TimeSeriesData;
import com.chiwanpark.woo.service.ExcelLoaderService;
import com.chiwanpark.woo.view.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    return new RawDataView(observation);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public TimeSeriesChartView timeSeriesChartView(String title, List<TimeSeriesData> dataset) {
    return new TimeSeriesChartView(title, dataset);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public ParameterSelectionPanel parameterSelectionPanel(Date rangeStart, Date rangeEnd) {
    return new ParameterSelectionPanel(rangeStart, rangeEnd);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public BasicStatisticsView basicStatisticsView(Observation observation, TimeSeriesData data) {
    return new BasicStatisticsView(observation, data);
  }
}
