package com.chiwanpark.woo.view;

import com.chiwanpark.woo.model.Tuple2;
import com.chiwanpark.woo.model.Tuple4;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class ValueRangeParameterPanel extends JPanel {
  private JPanel pnContents;
  private JTextField txtStart;
  private JComboBox cbStart;
  private JTextField txtEnd;
  private JComboBox cbEnd;

  private Tuple4<Double, Double, Double, Double> statistics;
  private Map<String, Double> startValueMap;
  private Map<String, Double> endValueMap;

  public ValueRangeParameterPanel(Tuple4<Double, Double, Double, Double> statistics) {
    this.statistics = statistics;

    setValueMap();
    setCbStartHandler();
    setCbEndHandler();

    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    add(pnContents);
  }

  public Tuple2<Double, Double> getParameter() {
    return new Tuple2<>(Double.valueOf(txtStart.getText()), Double.valueOf(txtEnd.getText()));
  }

  @SuppressWarnings("unchecked")
  private void setValueMap() {
    startValueMap = new HashMap<>();
    endValueMap = new HashMap<>();

    startValueMap.put("minimum", statistics.getV1());
    startValueMap.put("maximum", statistics.getV2());
    startValueMap.put("-sigma", statistics.getV3() - statistics.getV4());
    startValueMap.put("-2 sigma", statistics.getV3() - 2 * statistics.getV4());
    startValueMap.put("-3 sigma", statistics.getV3() - 3 * statistics.getV4());
    startValueMap.put("-4 sigma", statistics.getV3() - 4 * statistics.getV4());
    startValueMap.put("-5 sigma", statistics.getV3() - 5 * statistics.getV4());

    endValueMap.put("minimum", statistics.getV1());
    endValueMap.put("maximum", statistics.getV2());
    endValueMap.put("+sigma", statistics.getV3() + statistics.getV4());
    endValueMap.put("+2 sigma", statistics.getV3() + 2 * statistics.getV4());
    endValueMap.put("+3 sigma", statistics.getV3() + 3 * statistics.getV4());
    endValueMap.put("+4 sigma", statistics.getV3() + 4 * statistics.getV4());
    endValueMap.put("+5 sigma", statistics.getV3() + 5 * statistics.getV4());

    ArrayList<String> startKeys = new ArrayList<>(startValueMap.keySet());
    ArrayList<String> endKeys = new ArrayList<>(endValueMap.keySet());

    Collections.sort(startKeys);
    Collections.sort(endKeys);

    cbStart.addItem("");
    for (String key : startKeys) {
      cbStart.addItem(key);
    }

    cbEnd.addItem("");
    for (String key : endKeys) {
      cbEnd.addItem(key);
    }
  }

  @SuppressWarnings("unchecked")
  private void setCbStartHandler() {
    cbStart.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        txtStart.setText(String.valueOf(startValueMap.get(cbStart.getSelectedItem())));
      }
    });
  }

  @SuppressWarnings("unchecked")
  private void setCbEndHandler() {
    cbEnd.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        txtEnd.setText(String.valueOf(endValueMap.get(cbEnd.getSelectedItem())));
      }
    });
  }
}
