package com.chiwanpark.woo.view;

import com.chiwanpark.woo.WooController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainWindow extends JFrame {
  private static final Logger LOG = LoggerFactory.getLogger(MainWindow.class);

  private JDesktopPane desktopPane;

  private @Autowired WooController wooController;

  public MainWindow() throws HeadlessException {
    super("Project Woo");

    desktopPane = new JDesktopPane() {
      @Override
      protected void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
      }
    };

    add(desktopPane);

    setJMenuBar(createMenu());

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(1024, 768);
    setVisible(true);
  }

  public JDesktopPane getDesktop() {
    return desktopPane;
  }

  public JMenuBar createMenu() {
    JMenuBar menuBar = new JMenuBar();

    JMenu menuFile = new JMenu("파일");
    menuFile.add(createOpenDataItem());
    menuFile.add(createExitItem());

    menuBar.add(menuFile);

    return menuBar;
  }

  public JMenuItem createOpenDataItem() {
    JMenuItem item = new JMenuItem("자료 불러오기");

    item.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        LOG.info("Open Data clicked");
        JFileChooser fileChooser = new JFileChooser();
        FileFilter fileFilter = new FileNameExtensionFilter("Excel Files (*.xls, *.xlsx)", "xls", "xlsx");

        fileChooser.addChoosableFileFilter(fileFilter);

        int result = fileChooser.showDialog(desktopPane, "자료 불러오기");
        if (result == JFileChooser.APPROVE_OPTION) {
          File openedFile = fileChooser.getSelectedFile();
          LOG.info("File Chosen: " + openedFile.getAbsolutePath());

          wooController.loadExcelFile(openedFile);
        }
      }
    });

    return item;
  }

  public JMenuItem createExitItem() {
    JMenuItem item = new JMenuItem("종료");

    item.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        LOG.info("Exit clicked!");
        System.exit(0);
      }
    });

    return item;
  }
}
