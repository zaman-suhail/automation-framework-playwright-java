package com.framework.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader - Singleton Pattern
 * Reads all settings from config.properties file
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);

    private static ConfigReader instance;
    private Properties properties;

    private static final String CONFIG_PATH =
            "src/test/resources/config/config.properties";

    private ConfigReader() {
        loadProperties();
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    private void loadProperties() {

        try {
            properties = new Properties();

            FileInputStream fis = new FileInputStream(CONFIG_PATH);

            properties.load(fis);

            log.info("Config file loaded successfully!");

        } catch (IOException e) {

            log.error("Config file not found at: {}", CONFIG_PATH);

            throw new RuntimeException("Config file not found!", e);
        }
    }

    public String getBrowser() {

        String browser = System.getProperty("browser");

        return (browser != null)
                ? browser
                : properties.getProperty("browser", "chromium");
    }

    public String getUrl() {
        return properties.getProperty("url");
    }

    public int getTimeout() {

        return Integer.parseInt(
                properties.getProperty("timeout", "30000")
        );
    }

    public boolean isHeadless() {

        String headless = System.getProperty("headless");

        return (headless != null)
                ? Boolean.parseBoolean(headless)
                : Boolean.parseBoolean(
                properties.getProperty("headless", "false")
        );
    }

    public int getSlowMo() {

        return Integer.parseInt(
                properties.getProperty("slowmo", "0")
        );
    }

    public boolean isScreenshotOnFailure() {

        return Boolean.parseBoolean(
                properties.getProperty("screenshot.on.failure", "true")
        );
    }

    public String getReportsFolder() {

        return properties.getProperty(
                "reports.folder",
                "reports/"
        );
    }

    public String getLogsFolder() {

        return properties.getProperty(
                "logs.folder",
                "logs/"
        );
    }
}

