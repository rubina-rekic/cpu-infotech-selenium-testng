package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.HomePage;
import pages.LoginPage;
import utils.ConfigReader;
import utils.DriverFactory;

/**
 * Regression tests for account functionality beyond the basic login smoke test.
 */
public class AccountRegressionTest {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        homePage = new HomePage(driver);
        homePage.open();
    }

    @Test(groups = "regression")
    public void userCanLogOutSuccessfully() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.openLoginForm();
        loginPage.loginAs(ConfigReader.getLogin(), ConfigReader.getPassword());
        Assert.assertTrue(loginPage.isLoggedIn(),
                "Precondition failed: user should be logged in before testing logout");

        loginPage.logout();

        Assert.assertTrue(loginPage.isLoggedOut(),
                "Log In link should reappear after logging out, confirming the session ended");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}