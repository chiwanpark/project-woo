package com.chiwanpark.woo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.*;

public class Launcher {
  public static void main(String... args) throws Exception {
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    new AnnotationConfigApplicationContext(Config.class);
  }
}
