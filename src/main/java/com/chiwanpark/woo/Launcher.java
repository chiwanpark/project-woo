package com.chiwanpark.woo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

public class Launcher {
  private static final Logger LOG = LoggerFactory.getLogger(Launcher.class);

  public static void main(String... args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    new AnnotationConfigApplicationContext(Config.class);

    LOG.info("Launcher finished");
  }
}
