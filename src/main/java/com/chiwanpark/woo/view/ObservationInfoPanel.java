package com.chiwanpark.woo.view;

import com.chiwanpark.woo.Config;
import com.chiwanpark.woo.model.ObservationInfo;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.Date;

@Component
public class ObservationInfoPanel extends JPanel {
  private JTextField txtName;
  private JTextField txtType;
  private JTextField txtHeight;
  private JTextField txtLatitude;
  private JTextField txtLongitude;
  private JTextField txtTimePeriod;
  private JPanel pnContents;

  public ObservationInfoPanel() {
    super();

    setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
    add(pnContents);
  }

  public void setInfo(ObservationInfo info, Date dateStart, Date dateEnd) {
    txtName.setText(info.getName());
    txtType.setText(info.getType());
    txtHeight.setText(String.format(Config.DOUBLE_FORMAT, info.getHeight()) + "m");
    txtLatitude.setText(info.getLatitude().toString(Config.POSITION_FORMAT));
    txtLongitude.setText(info.getLongitude().toString(Config.POSITION_FORMAT));
    txtTimePeriod.setText(Config.DATE_FORMAT.format(dateStart) + " ~ " + Config.DATE_FORMAT.format(dateEnd));
  }

}
