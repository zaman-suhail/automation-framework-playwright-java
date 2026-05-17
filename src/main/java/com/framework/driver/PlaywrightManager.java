package com.framework.driver;

import com.framework.config.ConfigReader;
import com.framework.factory.PlaywrightFactory;
import com.microsoft.playwright.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * PlaywrightManager - Singleton Pattern (Thread Safe)
 * Manages Playwright, Browser, BrowserContext and Page
 */
public class PlaywrightManager {

    private static final Logger log =
            LogManager.getLogger(PlaywrightManager.class);

    private static final ThreadLocal<Playwright> playwrightThread =
            new ThreadLocal<>();

    private static final ThreadLocal<Browser> browserThread =
            new ThreadLocal<>();

    private static final ThreadLocal<BrowserContext> contextThread =
            new ThreadLocal<>();

    private static final ThreadLocal<Page> pageThread =
            new ThreadLocal<>();

    private PlaywrightManager() {}

    public static Page getPage() {

        if (pageThread.get() == null) {

            log.info("Creating Playwright session...");

            Playwright playwright = Playwright.create();

            Browser browser =
                    PlaywrightFactory.createBrowser(playwright);

            BrowserContext context =
                    browser.newContext(
                            new Browser.NewContextOptions()
                                    .setViewportSize(1920, 1080)
                    );

            Page page = context.newPage();

            int timeout =
                    ConfigReader.getInstance()
                            .getTimeout();

            page.setDefaultTimeout(timeout);

            playwrightThread.set(playwright);
            browserThread.set(browser);
            contextThread.set(context);
            pageThread.set(page);

            log.info("Playwright session started successfully!");
        }

        return pageThread.get();
    }

    public static void quitSession() {

        try {

            if (pageThread.get() != null) {
                pageThread.get().close();
                pageThread.remove();
            }

            if (contextThread.get() != null) {
                contextThread.get().close();
                contextThread.remove();
            }

            if (browserThread.get() != null) {
                browserThread.get().close();
                browserThread.remove();
            }

            if (playwrightThread.get() != null) {
                playwrightThread.get().close();
                playwrightThread.remove();
            }

            log.info("Playwright session closed successfully!");

        } catch (Exception e) {

            log.error("Error while closing Playwright session", e);
        }
    }
}

