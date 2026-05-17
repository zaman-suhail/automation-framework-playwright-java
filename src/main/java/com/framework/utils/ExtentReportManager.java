package com.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import com.framework.config.ConfigReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportManager - Singleton Pattern
 * Creates HTML report after every execution
 */
public class ExtentReportManager {

    private static final Logger log =
            LogManager.getLogger(ExtentReportManager.class);

    private static ExtentReports extentReports;

    private static final ThreadLocal<ExtentTest> testThread =
            new ThreadLocal<>();

    private ExtentReportManager() {}

    // Create report instance only once
    public static ExtentReports getInstance() {

        if (extentReports == null) {

            String timestamp =
                    LocalDateTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "yyyy-MM-dd_HH-mm-ss"
                                    )
                            );

            String reportsFolder =
                    ConfigReader.getInstance()
                            .getReportsFolder();

            new File(reportsFolder).mkdirs();

            String reportPath =
                    reportsFolder
                            + "PlaywrightReport_"
                            + timestamp
                            + ".html";

            ExtentSparkReporter sparkReporter =
                    new ExtentSparkReporter(reportPath);

            sparkReporter.config().setTheme(Theme.DARK);

            sparkReporter.config().setDocumentTitle(
                    "Playwright Automation Report"
            );

            sparkReporter.config().setReportName(
                    "Playwright Test Results"
            );

            sparkReporter.config().setTimeStampFormat(
                    "yyyy-MM-dd HH:mm:ss"
            );

            extentReports = new ExtentReports();

            extentReports.attachReporter(sparkReporter);

            extentReports.setSystemInfo(
                    "OS",
                    System.getProperty("os.name")
            );

            extentReports.setSystemInfo(
                    "Java",
                    System.getProperty("java.version")
            );

            extentReports.setSystemInfo(
                    "Browser",
                    ConfigReader.getInstance().getBrowser()
            );

            extentReports.setSystemInfo(
                    "URL",
                    ConfigReader.getInstance().getUrl()
            );

            log.info("Extent Report created: {}", reportPath);
        }

        return extentReports;
    }

    // Create new test in report
    public static ExtentTest createTest(
            String testName,
            String description
    ) {

        ExtentTest test =
                getInstance().createTest(
                        testName,
                        description
                );

        testThread.set(test);

        log.info(
                "Test started in report: {}",
                testName
        );

        return test;
    }

    // Get current test
    public static ExtentTest getTest() {
        return testThread.get();
    }

    // Remove current test
    public static void removeTest() {
        testThread.remove();
    }

    // Save report
    public static void flushReports() {

        if (extentReports != null) {

            extentReports.flush();

            log.info("Report saved successfully!");
        }
    }

    // Logging Helpers

    public static void logInfo(String message) {

        if (getTest() != null) {
            getTest().info(message);
        }

        log.info(message);
    }

    public static void logPass(String message) {

        if (getTest() != null) {
            getTest().pass(message);
        }

        log.info("PASS: {}", message);
    }

    public static void logFail(String message) {

        if (getTest() != null) {
            getTest().fail(message);
        }

        log.error("FAIL: {}", message);
    }

    public static void logWarning(String message) {

        if (getTest() != null) {
            getTest().warning(message);
        }

        log.warn("WARN: {}", message);
    }
}

