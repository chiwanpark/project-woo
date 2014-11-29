package com.chiwanpark.woo;

import com.chiwanpark.woo.model.RawObservation;
import com.chiwanpark.woo.model.TimeSeriesDataset;
import com.chiwanpark.woo.service.ExcelLoaderService;
import com.chiwanpark.woo.view.MainWindow;
import com.chiwanpark.woo.view.ParameterSelectionPanel;
import com.chiwanpark.woo.view.RawDataView;
import com.chiwanpark.woo.view.TimeSeriesChartView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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
  public ParameterSelectionPanel parameterSelectionPanel() {
    return new ParameterSelectionPanel();
  }
}
