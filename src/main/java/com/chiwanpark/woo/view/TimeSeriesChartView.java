package com.chiwanpark.woo.view;

import com.chiwanpark.woo.model.TimeSeriesData;
import com.chiwanpark.woo.model.TimeSeriesDatum;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;

@Component
public class TimeSeriesChartView extends JInternalFrame {
  private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesChartView.class);

  public TimeSeriesChartView(String title, List<TimeSeriesData> dataset) {
    super("Graph - " + title);

    XYDataset jfreeDataset = createDataSet(dataset);
    ChartPanel pnChart = createChart(title, jfreeDataset);

    setContentPane(pnChart);
    setSize(640, 320);
    setMaximizable(true);
    setVisible(true);
    setClosable(true);
    setResizable(true);
  }

  private XYDataset createDataSet(List<TimeSeriesData> dataset) {
    TimeSeriesCollection collection = new TimeSeriesCollection();

    for (TimeSeriesData data : dataset) {
      TimeSeries series = new TimeSeries(data.getName());
      for (TimeSeriesDatum datum : data) {
        series.add(new Second(datum.getDate()), datum.getDatum());
      }

      collection.addSeries(series);
    }

    return collection;
  }

  private ChartPanel createChart(String title, XYDataset dataset) {
    JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Date", "Value", dataset);
    ChartPanel pnChart = new ChartPanel(chart, true);
    pnChart.setPopupMenu(null);

    LOG.info("Chart Font: " + chart.getTitle().getFont().getFontName());

    return pnChart;
  }
}
