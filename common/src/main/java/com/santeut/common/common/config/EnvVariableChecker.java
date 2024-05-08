package com.santeut.common.common.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EnvVariableChecker {

  private static final Logger logger = LoggerFactory.getLogger(EnvVariableChecker.class);

  @Value("${FIREBASE_CONFIG_PATH:Not set}")
  private String firebaseConfigPath;

  @PostConstruct
  public void postConstruct() {
    logger.info("Current FIREBASE_CONFIG_PATH: {}", firebaseConfigPath);
  }
}
