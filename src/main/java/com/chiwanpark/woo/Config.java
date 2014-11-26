package com.chiwanpark.woo;

import com.chiwanpark.woo.view.MainWindow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
  @Bean
  public MainWindow mainWindow() {
    return new MainWindow();
  }
}
