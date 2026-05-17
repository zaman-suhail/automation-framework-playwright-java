package com.framework.tests;

import com.framework.pages.LoginPage;
import com.framework.pages.ProductsPage;
import com.framework.utils.ExtentReportManager;
import com.framework.utils.TestDataReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * ProductsTest - Swag Labs Products Page
 */
public class ProductsTest extends BaseTest {

    private static final Logger log = LogManager.getLogger(ProductsTest.class);

    private ProductsPage productsPage;

    // ── Login before every test ──
    @BeforeMethod
    public void login() {
        Object[][] users = TestDataReader.getValidUsers();
        String username  = (String) users[0][0];
        String password  = (String) users[0][1];

        productsPage = new LoginPage()
                .loginSuccessfully(username, password);
    }

    // ── 1. Page loaded ──
    @Test(description = "Products page should load after login")
    public void testProductsPageLoaded() {
        ExtentReportManager.logInfo("Checking products page is loaded");

        Assert.assertTrue(
                productsPage.isPageLoaded(),
                "Products page did not load!"
        );

        ExtentReportManager.logPass("Products page loaded successfully");
        log.info("testProductsPageLoaded PASSED");
    }

    // ── 2. Title check ──
    @Test(description = "Products page title should be 'Products'")
    public void testProductsPageTitle() {
        ExtentReportManager.logInfo("Verifying page title");

        Assert.assertEquals(
                productsPage.getProductsTitle(),
                "Products",
                "Page title is incorrect!"
        );

        ExtentReportManager.logPass("Title verified: Products");
        log.info("testProductsPageTitle PASSED");
    }

    // ── 3. Products list visible ──
    @Test(description = "Products list should be displayed")
    public void testProductsListDisplayed() {
        ExtentReportManager.logInfo("Checking products list visibility");

        Assert.assertTrue(
                productsPage.isProductsListDisplayed(),
                "Products list is not visible!"
        );

        ExtentReportManager.logPass("Products list is visible");
        log.info("testProductsListDisplayed PASSED");
    }

    // ── 4. Logout ──
    @Test(description = "Logout should return to Login page")
    public void testLogout() {
        ExtentReportManager.logInfo("Logging out from Products page");

        LoginPage loginPage = productsPage.logout();

        Assert.assertTrue(
                loginPage.isPageLoaded(),
                "Login page did not load after logout!"
        );

        ExtentReportManager.logPass("Logout successful");
        log.info("testLogout PASSED");
    }
}