package com.chiwanpark.woo.view;

import com.chiwanpark.woo.model.Tuple2;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component
public class MovingAverageParameterPanel extends JPanel {
  private JPanel pnContents;
  private JTextField txtValue;
  private JComboBox cbTimeUnit;

  private Map<String, Integer> timeUnitMap;

  public MovingAverageParameterPanel() {
    setTimeUnitMap();

    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    add(pnContents);
  }

  public Tuple2<Integer, Integer> getParameter() {
    return new Tuple2<>(Integer.valueOf(txtValue.getText()), timeUnitMap.get(cbTimeUnit.getSelectedItem()));
  }

  private void setTimeUnitMap() {
    timeUnitMap = new HashMap<>();

    timeUnitMap.put("seconds", Calendar.SECOND);
    timeUnitMap.put("minutes", Calendar.MINUTE);
    timeUnitMap.put("hours", Calendar.HOUR_OF_DAY);
    timeUnitMap.put("days", Calendar.DAY_OF_MONTH);
    timeUnitMap.put("months", Calendar.MONTH);
    timeUnitMap.put("years", Calendar.YEAR);

    for (String key : timeUnitMap.keySet()) {
      cbTimeUnit.addItem(key);
    }
  }

  private void createUIComponents() {
    txtValue = new JFormattedTextField(new NumberFormatter());
  }
}
