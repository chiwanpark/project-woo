package com.chiwanpark.woo.view;

import com.chiwanpark.woo.model.TimeSeriesData;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class TimeSeriesChartView extends JInternalFrame {
  private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesChartView.class);

  private @Autowired MainWindow mainWindow;

  private JPanel pnContents;
  private JButton btnSaveGraph;
  private JPanel pnChartWrap;

  private ChartPanel pnChart;

  public TimeSeriesChartView(String title, List<TimeSeriesData> dataset) {
    super("Graph - " + title);

    XYDataset jfreeDataset = createDataSet(dataset);
    pnChart = createChart(title, jfreeDataset);

    pnChartWrap.setLayout(new BoxLayout(pnChartWrap, BoxLayout.PAGE_AXIS));
    pnChartWrap.add(pnChart);

    addSaveGraphAction();

    setContentPane(pnContents);
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
      for (Date date : data.keySet()) {
        series.add(new Second(date), data.get(date));
      }

      collection.addSeries(series);
    }

    return collection;
  }

  private ChartPanel createChart(String title, XYDataset dataset) {
    // create domain axis
    DateAxis dateAxis = new DateAxis("Date");
    dateAxis.setAutoRange(true);

    // create value axis
    NumberAxis valueAxis = new NumberAxis("Value");
    valueAxis.setAutoRangeIncludesZero(false);

    // create item renderer
    XYDotRenderer renderer = new XYDotRenderer();
    renderer.setDotWidth(3);
    renderer.setDotHeight(3);

    // create plot
    XYPlot plot = new XYPlot(dataset, dateAxis, valueAxis, renderer);

    plot.setOrientation(PlotOrientation.VERTICAL);
    plot.setDrawingSupplier(
        new DefaultDrawingSupplier(
            new Paint[]{Color.BLACK, Color.BLUE},
            new Paint[]{Color.BLACK, Color.BLUE},
            DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
            DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE
        )
    );
    plot.setBackgroundPaint(Color.WHITE);

    // create chart and chart panel
    JFreeChart chart = new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
    chart.setBackgroundPaint(Color.WHITE);

    ChartPanel pnChart = new ChartPanel(chart, true);
    pnChart.setPopupMenu(null);

    // change font
    setChartFont(chart);
    LOG.info("Changed chart font: " + chart.getTitle().getFont().getFontName());

    return pnChart;
  }

  private void setChartFont(JFreeChart chart) {
    XYPlot plot = chart.getXYPlot();
    ValueAxis domainAxis = plot.getDomainAxis();
    ValueAxis rangeAxis = plot.getRangeAxis();
    Font font;

    font = chart.getTitle().getFont();
    chart.getTitle().setFont(new Font(Font.SANS_SERIF, font.getStyle(), font.getSize()));

    font = chart.getLegend().getItemFont();
    chart.getLegend().setItemFont(new Font(Font.SANS_SERIF, font.getStyle(), font.getSize()));

    font = domainAxis.getLabelFont();
    domainAxis.setLabelFont(new Font(Font.SANS_SERIF, font.getStyle(), font.getSize()));

    font = domainAxis.getTickLabelFont();
    domainAxis.setTickLabelFont(new Font(Font.SANS_SERIF, font.getStyle(), font.getSize()));

    font = rangeAxis.getLabelFont();
    rangeAxis.setLabelFont(new Font(Font.SANS_SERIF, font.getStyle(), font.getSize()));

    font = rangeAxis.getTickLabelFont();
    rangeAxis.setTickLabelFont(new Font(Font.SANS_SERIF, font.getStyle(), font.getSize()));
  }

  private void addSaveGraphAction() {
    btnSaveGraph.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          pnChart.doSaveAs();
        } catch (IOException ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(mainWindow, "그래프를 저장하는데 실패했습니다!", "오류!", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
  }
}
