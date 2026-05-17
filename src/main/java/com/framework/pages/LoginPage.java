package com.framework.pages;

import com.microsoft.playwright.Locator;

/**
 * LoginPage - Page Object Model + Fluent Interface (Playwright Version)
 */
public class LoginPage extends BasePage {

    // Locators
    private final String usernameField = "#user-name";
    private final String passwordField = "#password";
    private final String loginButton   = "#login-button";
    private final String errorMessage  = "[data-test='error']";

    public LoginPage enterUsername(String username) {

        log.info("Entering username: {}", username);

        if (!username.isEmpty()) {
            type(usernameField, username);
        }

        return this;
    }

    public LoginPage enterPassword(String password) {

        log.info("Entering password");

        if (!password.isEmpty()) {
            type(passwordField, password);
        }

        return this;
    }

    // Click login button - Fluent
    public LoginPage clickLogin() {

        log.info("Clicking login button");

        click(loginButton);

        return this;
    }

    // Full login flow - success scenario
    public ProductsPage loginSuccessfully(
            String username,
            String password
    ) {

        enterUsername(username)
                .enterPassword(password)
                .clickLogin();

        return new ProductsPage();
    }

    // Error message text
    public String getErrorMessage() {

        return getText(errorMessage);
    }

    // Error visibility
    public boolean isErrorDisplayed() {

        return isDisplayed(errorMessage);
    }

    @Override
    public boolean isPageLoaded() {

        return isDisplayed(loginButton);
    }
}

