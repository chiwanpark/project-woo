package com.chiwanpark.woo;

import com.chiwanpark.woo.model.Observation;
import com.chiwanpark.woo.service.ExcelLoaderService;
import com.chiwanpark.woo.view.MainWindow;
import com.chiwanpark.woo.view.RawDataView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.swing.*;
import java.io.File;

@Controller
public class WooController {
  private @Autowired ExcelLoaderService excelLoaderService;
  private @Autowired MainWindow mainWindow;

  public void loadExcelFile(File file) {
    try {
      Observation observation = excelLoaderService.loadExcelFile(file);

      RawDataView rawDataView = new RawDataView(observation);
      mainWindow.getDesktop().add(rawDataView);
      rawDataView.setSelected(true);
      rawDataView.setVisible(true);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(mainWindow, "Cannot open file!", "Open Data", JOptionPane.ERROR_MESSAGE);
    }
  }
}
