package com.chiwanpark.woo.view;

import com.chiwanpark.woo.Config;
import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.model.TimeSeriesData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.text.ParseException;
import java.util.*;

@Component
public class ParameterSelectionPanel extends JPanel {
  private static final Logger LOG = LoggerFactory.getLogger(ParameterSelectionPanel.class);

  private JPanel pnContents;
  private JSpinner spinStartDate;
  private JSpinner spinEndDate;
  private JPanel pnParameterSelection;
  private List<JRadioButton> radioButtonList;


  public ParameterSelectionPanel(Observation observation) {
    super();

    // data spinner setting
    List<String> dateList = getListOfDate(observation.getDateStart(), observation.getDateEnd());
    spinStartDate.setModel(new SpinnerListModel(dateList));
    spinEndDate.setModel(new SpinnerListModel(dateList));
    spinEndDate.getModel().setValue(dateList.get(dateList.size() - 1));

    ((JSpinner.DefaultEditor) spinStartDate.getEditor()).getTextField().setEditable(false);
    ((JSpinner.DefaultEditor) spinEndDate.getEditor()).getTextField().setEditable(false);

    // create parameter selection radio button
    radioButtonList = new ArrayList<>();
    pnParameterSelection.setLayout(new BoxLayout(pnParameterSelection, BoxLayout.LINE_AXIS));
    for (TimeSeriesData data : observation.getData()) {
      JRadioButton btnRadio = new JRadioButton(data.getName());
      radioButtonList.add(btnRadio);
      pnParameterSelection.add(btnRadio);
    }

    add(pnContents);
  }

  public int getSelected() {
    for (int i = 0; i < radioButtonList.size(); ++i) {
      if (radioButtonList.get(i).isSelected()) {
        return i;
      }
    }

    return -1;
  }

  public Date getRangeStart() {
    try {
      return Config.DATE_FORMAT.parse((String) spinStartDate.getModel().getValue());
    } catch (ParseException e) {
      LOG.warn("Parsing start of date range is failed.", e);
      return null;
    }
  }

  public Date getRangeEnd() {
    try {
      return Config.DATE_FORMAT.parse((String) spinEndDate.getModel().getValue());
    } catch (ParseException e) {
      LOG.warn("Parsing start of date range is failed.", e);
      return null;
    }
  }

  private void createUIComponents() {
    spinStartDate = new JSpinner(new SpinnerListModel());
    spinEndDate = new JSpinner(new SpinnerListModel());
  }

  private List<String> getListOfDate(Date start, Date end) {
    List<String> result = new ArrayList<>();
    Calendar calendar = new GregorianCalendar();

    do {
      result.add(Config.DATE_FORMAT.format(start));
      calendar.setTime(start);
      calendar.add(Calendar.HOUR_OF_DAY, 1);
      start = calendar.getTime();
    } while (start.compareTo(end) <= 0);

    return result;
  }
}
