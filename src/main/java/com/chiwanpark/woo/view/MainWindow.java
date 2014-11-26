package com.chiwanpark.woo.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainWindow extends JFrame {
  private Logger logger = LoggerFactory.getLogger(MainWindow.class);

  private JDesktopPane desktopPane;

  public MainWindow() throws HeadlessException {
    super("Project Woo");

    desktopPane = new JDesktopPane();
    add(desktopPane);

    setJMenuBar(createMenu());

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(640, 480);
    setVisible(true);
  }

  public JMenuBar createMenu() {
    JMenuBar menuBar = new JMenuBar();

    JMenu menuFile = new JMenu("File");
    menuFile.add(createOpenDataItem());
    menuFile.add(createExitItem());

    menuBar.add(menuFile);

    return menuBar;
  }

  public JMenuItem createOpenDataItem() {
    JMenuItem item = new JMenuItem("Open Data");

    item.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        logger.info("Open Data clicked");
        JFileChooser fileChooser = new JFileChooser();
        FileFilter fileFilter = new FileNameExtensionFilter("Excel Files (*.xls, *.xlsx)", "xls", "xlsx");

        fileChooser.addChoosableFileFilter(fileFilter);

        int result = fileChooser.showDialog(desktopPane, "Open Data");
        if (result == JFileChooser.APPROVE_OPTION) {
          File openedFile = fileChooser.getSelectedFile();
          logger.info("File Chosen: " + openedFile.getAbsolutePath());
        }
      }
    });

    return item;
  }

  public JMenuItem createExitItem() {
    JMenuItem item = new JMenuItem("Exit");

    item.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        logger.info("Exit clicked!");
        System.exit(0);
      }
    });

    return item;
  }
}
