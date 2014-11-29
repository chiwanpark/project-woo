package com.chiwanpark.woo.view;

import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class ParameterSelectionPanel extends JPanel {
  private JCheckBox chkWaterLevel;
  private JCheckBox chkTemparature;
  private JCheckBox chkConductivity;
  private JPanel pnContents;

  public ParameterSelectionPanel() {
    super();

    add(pnContents);
  }

  public boolean getWaterLevel() {
    return chkWaterLevel.isSelected();
  }

  public boolean getTemparature() {
    return chkTemparature.isSelected();
  }

  public boolean getConductivity() {
    return chkConductivity.isSelected();
  }
}
