package com.chiwanpark.woo.view;

import javax.swing.*;
import java.awt.*;

public class InProgressDialog extends JDialog {
  private JPanel pnContents;

  public InProgressDialog(Window frame) {
    super(frame, "In Progress...", ModalityType.APPLICATION_MODAL);

    setContentPane(pnContents);
    setLocationRelativeTo(frame);
    pack();
  }
}
