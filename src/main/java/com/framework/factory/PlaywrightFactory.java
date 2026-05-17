package com.framework.factory;

import com.framework.config.ConfigReader;
import com.microsoft.playwright.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * PlaywrightFactory - Factory Pattern
 * Creates Playwright browser instance based on config
 */
public class PlaywrightFactory {

    private static final Logger log =
            LogManager.getLogger(PlaywrightFactory.class);

    private PlaywrightFactory() {}

    public static Browser createBrowser(Playwright playwright) {

        String browser =
                ConfigReader.getInstance()
                        .getBrowser()
                        .toLowerCase()
                        .trim();

        boolean headless =
                ConfigReader.getInstance()
                        .isHeadless();

        int slowMo =
                ConfigReader.getInstance()
                        .getSlowMo();

        log.info(
                "Opening browser: {} | Headless: {} | SlowMo: {}",
                browser,
                headless,
                slowMo
        );

        BrowserType.LaunchOptions options =
                new BrowserType.LaunchOptions()
                        .setHeadless(headless)
                        .setSlowMo(slowMo);

        switch (browser) {

            case "chromium":

                log.info("Chromium browser launched!");

                return playwright.chromium().launch(options);

            case "firefox":

                log.info("Firefox browser launched!");

                return playwright.firefox().launch(options);

            case "webkit":

                log.info("WebKit browser launched!");

                return playwright.webkit().launch(options);

            default:

                log.warn(
                        "Unknown browser '{}' - defaulting to Chromium",
                        browser
                );

                return playwright.chromium().launch(options);
        }
    }
}

