package com.chiwanpark.woo.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ParameterSelectionPanel extends JPanel {
  private static final Logger LOG = LoggerFactory.getLogger(ParameterSelectionPanel.class);
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private JPanel pnContents;
  private JSpinner spinStartDate;
  private JSpinner spinEndDate;
  private JRadioButton radioWaterLavel;
  private JRadioButton radioTemperature;
  private JRadioButton radioConductivity;

  private Date rangeStart;
  private Date rangeEnd;

  public ParameterSelectionPanel(Date rangeStart, Date rangeEnd) {
    super();

    this.rangeStart = rangeStart;
    this.rangeEnd = rangeEnd;

    add(pnContents);
  }

  public boolean getWaterLevel() {
    return radioWaterLavel.isSelected();
  }

  public boolean getTemparature() {
    return radioTemperature.isSelected();
  }

  public boolean getConductivity() {
    return radioConductivity.isSelected();
  }

  public Date getRangeStart() {
    try {
      return DATE_FORMAT.parse((String) spinStartDate.getModel().getValue());
    } catch (ParseException e) {
      LOG.warn("Parsing start of date range is failed.", e);
      return null;
    }
  }

  public Date getRangeEnd() {
    try {
      return DATE_FORMAT.parse((String) spinEndDate.getModel().getValue());
    } catch (ParseException e) {
      LOG.warn("Parsing start of date range is failed.", e);
      return null;
    }
  }

  private void createUIComponents() {
    LOG.info("Candidate of date range (" + rangeStart + ", " + rangeEnd + ")");

    List<String> dates = getListOfDate(rangeStart, rangeEnd);

    spinStartDate = new JSpinner(new SpinnerListModel(dates));
    ((JSpinner.DefaultEditor) spinStartDate.getEditor()).getTextField().setEditable(false);

    spinEndDate = new JSpinner(new SpinnerListModel(dates));
    ((JSpinner.DefaultEditor) spinEndDate.getEditor()).getTextField().setEditable(false);
    spinEndDate.getModel().setValue(dates.get(dates.size() - 1));
  }

  private List<String> getListOfDate(Date start, Date end) {
    List<String> result = new ArrayList<>();
    Calendar calendar = new GregorianCalendar();

    do {
      result.add(DATE_FORMAT.format(start));
      calendar.setTime(start);
      calendar.add(Calendar.HOUR_OF_DAY, 1);
      start = calendar.getTime();
    } while (start.compareTo(end) <= 0);

    return result;
  }
}
