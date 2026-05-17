package com.framework.tests;

import com.framework.config.ConfigReader;
import com.framework.driver.PlaywrightManager;
import com.microsoft.playwright.Page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest - Template Method Pattern (Playwright Version)
 * Handles setup and teardown for all tests
 */
public abstract class BaseTest {

    protected static final Logger log =
            LogManager.getLogger(BaseTest.class);

    protected Page page;

    protected ConfigReader config;

    @BeforeMethod
    public void setUp() {

        log.info("===== Test started =====");

        config = ConfigReader.getInstance();

        page = PlaywrightManager.getPage();

        page.navigate(config.getUrl());

        log.info(
                "Website opened: {}",
                config.getUrl()
        );
    }

    @AfterMethod
    public void tearDown() {

        log.info("===== Test finished =====");

        PlaywrightManager.quitSession();
    }
}