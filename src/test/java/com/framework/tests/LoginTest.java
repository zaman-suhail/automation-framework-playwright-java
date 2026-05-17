package com.framework.tests;

import com.framework.pages.LoginPage;
import com.framework.pages.ProductsPage;
import com.framework.utils.ExtentReportManager;
import com.framework.utils.TestDataReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * LoginTest - Playwright + TestNG + Data Driven Framework
 */
public class LoginTest extends BaseTest {

    private static final Logger log =
            LogManager.getLogger(LoginTest.class);

    // Valid users
    @DataProvider(name = "validUsers")
    public Object[][] validUsers() {
        return TestDataReader.getValidUsers();
    }

    // Invalid users
    @DataProvider(name = "invalidUsers")
    public Object[][] invalidUsers() {
        return TestDataReader.getInvalidUsers();
    }

    @Test(
            description = "Valid credentials should navigate to products page",
            dataProvider = "validUsers"
    )
    public void testValidLogin(
            String username,
            String password,
            String description
    ) {

        ExtentReportManager.logInfo(
                "Testing: " + description
        );

        ExtentReportManager.logInfo(
                "Username: " + username
        );

        ProductsPage productsPage =
                new LoginPage()
                        .loginSuccessfully(username, password);

        Assert.assertTrue(
                productsPage.isPageLoaded(),
                "Products page did not load!"
        );

        Assert.assertEquals(
                productsPage.getProductsTitle(),
                "Products",
                "Page title incorrect!"
        );

        ExtentReportManager.logPass(
                "Login successful → Products page loaded"
        );

        log.info(
                "Valid login PASSED: {}",
                username
        );
    }

    @Test(
            description = "Invalid credentials should show error message",
            dataProvider = "invalidUsers"
    )
    public void testInvalidLogin(
            String username,
            String password,
            String expectedError
    ) {

        ExtentReportManager.logInfo(
                "Testing invalid login: '" + username + "'"
        );

        LoginPage loginPage =
                new LoginPage()
                        .enterUsername(username)
                        .enterPassword(password)
                        .clickLogin();

        Assert.assertTrue(
                loginPage.isErrorDisplayed(),
                "Error message was not shown!"
        );

        String actualError =
                loginPage.getErrorMessage();

        Assert.assertTrue(
                actualError.contains(expectedError),
                "Wrong error message: " + actualError
        );

        ExtentReportManager.logPass(
                "Correct error shown: " + actualError
        );

        log.info(
                "Invalid login PASSED: {}",
                username
        );
    }

    @Test(description = "User should be able to logout successfully")
    public void testLogout() {

        ExtentReportManager.logInfo(
                "Login with standard_user then logout"
        );

        Object[][] users =
                TestDataReader.getValidUsers();

        String username =
                (String) users[0][0];

        String password =
                (String) users[0][1];

        ProductsPage productsPage =
                new LoginPage()
                        .loginSuccessfully(username, password);

        Assert.assertTrue(
                productsPage.isPageLoaded(),
                "Login failed!"
        );

        LoginPage loginPage =
                productsPage.logout();

        Assert.assertTrue(
                loginPage.isPageLoaded(),
                "Logout failed!"
        );

        ExtentReportManager.logPass(
                "Logout successful → back on Login page"
        );

        log.info("Logout PASSED");
    }
}
