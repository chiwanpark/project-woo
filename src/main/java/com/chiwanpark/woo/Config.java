package com.chiwanpark.woo;

import com.chiwanpark.woo.model.RawObservation;
import com.chiwanpark.woo.model.TimeSeriesDataset;
import com.chiwanpark.woo.service.ExcelLoaderService;
import com.chiwanpark.woo.view.*;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Date;

@Configuration
public class Config {
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
  public RawDataView rawDataView(RawObservation observation) {
    return new RawDataView(observation);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public TimeSeriesChartView timeSeriesChartView(String title, TimeSeriesDataset dataset) {
    return new TimeSeriesChartView(title, dataset);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public ParameterSelectionPanel parameterSelectionPanel(Date rangeStart, Date rangeEnd) {
    return new ParameterSelectionPanel(rangeStart, rangeEnd);
  }

  @Bean
  @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
  public BasicStatisticsView basicStatisticsView(RawObservation rawObservation, String parameter, double maximum, double minimum, double average, double stdDeviation) {
    return new BasicStatisticsView(rawObservation, parameter, maximum, minimum, average, stdDeviation);
  }
}
