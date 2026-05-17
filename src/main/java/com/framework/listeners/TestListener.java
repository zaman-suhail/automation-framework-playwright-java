package com.framework.listeners;

import com.aventstack.extentreports.Status;
import com.framework.driver.PlaywrightManager;
import com.framework.utils.ExtentReportManager;

import com.microsoft.playwright.Page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * TestListener - Observer Pattern
 * Listens to TestNG events and automatically:
 * - Logs pass/fail/skip in HTML report
 * - Takes screenshot on failure
 * - Saves logs
 */
public class TestListener implements ITestListener {

    private static final Logger log =
            LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {

        log.info("========================================");
        log.info("Test Suite started: {}", context.getName());
        log.info("========================================");
    }

    @Override
    public void onTestStart(ITestResult result) {

        String testName =
                result.getMethod().getMethodName();

        String description =
                result.getMethod().getDescription();

        log.info("------ Test started: {} ------", testName);

        ExtentReportManager.createTest(
                testName,
                description == null || description.isEmpty()
                        ? testName
                        : description
        );

        ExtentReportManager.logInfo(
                "Test started: " + testName
        );
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        String testName =
                result.getMethod().getMethodName();

        log.info("PASSED: {}", testName);

        ExtentReportManager.logPass(
                "Test PASSED: " + testName
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {

        String testName =
                result.getMethod().getMethodName();

        String errorMsg =
                result.getThrowable().getMessage();

        log.error(
                "FAILED: {} | Error: {}",
                testName,
                errorMsg
        );

        ExtentReportManager.logFail(
                "Test FAILED: " + testName
        );

        ExtentReportManager.logFail(
                "Error: " + errorMsg
        );

        // Screenshot Capture
        try {

            Page page = PlaywrightManager.getPage();

            Path screenshotPath =
                    Files.createTempFile(
                            "failure-",
                            ".png"
                    );

            page.screenshot(
                    new Page.ScreenshotOptions()
                            .setPath(screenshotPath)
                            .setFullPage(true)
            );

            ExtentReportManager.getTest()
                    .addScreenCaptureFromPath(
                            screenshotPath.toString(),
                            "Failure Screenshot"
                    );

            log.info("Screenshot attached to report");

        } catch (Exception e) {

            log.warn(
                    "Could not capture screenshot: {}",
                    e.getMessage()
            );
        }

        // Attach full stack trace
        ExtentReportManager.getTest()
                .log(Status.FAIL, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        String testName =
                result.getMethod().getMethodName();

        log.warn("SKIPPED: {}", testName);

        ExtentReportManager.logWarning(
                "Test SKIPPED: " + testName
        );
    }

    @Override
    public void onFinish(ITestContext context) {

        log.info("========================================");

        log.info(
                "Suite finished | Pass: {} | Fail: {} | Skip: {}",
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size()
        );

        log.info("========================================");

        // Save report
        ExtentReportManager.flushReports();
    }
}

