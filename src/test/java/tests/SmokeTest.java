package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.HomePage;
import utils.DriverFactory;


public class SmokeTest {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        homePage = new HomePage(driver);
        homePage.open();
    }

    @Test(groups = "smoke")
    public void homepageLoads_andMenuIsVisible() {
        Assert.assertTrue(homePage.isMenuVisible(),
                "Main menu should be visible on the homepage");
    }

    @Test(groups = "smoke")
    public void searchingForKnownTerm_navigatesToResults() {
        homePage.searchFor("iphone");

        Assert.assertTrue(driver.getCurrentUrl().contains("s=iphone"),
                "URL should contain the search query parameter after searching");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}