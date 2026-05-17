package com.framework.pages;

import com.framework.driver.PlaywrightManager;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * BasePage - Template Method Pattern (Playwright Version)
 * Parent class for all page objects
 */
public abstract class BasePage {

    protected static final Logger log =
            LogManager.getLogger(BasePage.class);

    protected Page page;

    public BasePage() {

        this.page = PlaywrightManager.getPage();
    }

    // Click action
    protected void click(String selector) {

        log.info("Click: {}", selector);

        page.locator(selector)
                .click();
    }

    // Type action
    protected void type(String selector, String text) {

        log.info(
                "Type '{}' into: {}",
                text,
                selector
        );

        Locator element =
                page.locator(selector);

        element.fill(text);
    }

    // Get text
    protected String getText(String selector) {

        String text =
                page.locator(selector)
                        .innerText();

        log.debug(
                "Got text '{}' from: {}",
                text,
                selector
        );

        return text;
    }

    // Check element visible
    protected boolean isDisplayed(String selector) {

        try {

            return page.locator(selector)
                    .isVisible();

        } catch (Exception e) {

            log.debug(
                    "Element not found: {}",
                    selector
            );

            return false;
        }
    }

    // Page title
    public String getPageTitle() {
        return page.title();
    }

    // Current URL
    public String getCurrentUrl() {
        return page.url();
    }

    // Force page load wait (optional helper)
    protected void waitForSelector(String selector) {

        page.waitForSelector(selector);
    }

    public abstract boolean isPageLoaded();
}
