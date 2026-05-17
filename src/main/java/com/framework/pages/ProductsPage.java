package com.framework.pages;

/**
 * ProductsPage - Page Object Model (Playwright Version)
 */
public class ProductsPage extends BasePage {

    // Locators (CSS selectors)
    private final String pageTitle    = ".title";
    private final String menuButton   = "#react-burger-menu-btn";
    private final String logoutLink   = "#logout_sidebar_link";
    private final String productsList = ".inventory_list";

    // Page title
    public String getProductsTitle() {

        return getText(pageTitle);
    }

    // Check products list visible
    public boolean isProductsListDisplayed() {

        return isDisplayed(productsList);
    }

    // Logout flow
    public LoginPage logout() {

        log.info("Logging out...");

        click(menuButton);
        click(logoutLink);

        return new LoginPage();
    }

    @Override
    public boolean isPageLoaded() {

        return isDisplayed(productsList);
    }
}

