package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.CategoryPage;
import pages.HomePage;
import utils.DriverFactory;

/**
 * Regression tests for category page functionality: sorting, filtering.
 */
public class CategoryRegressionTest {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.createDriver();
        homePage = new HomePage(driver);
        homePage.open();
    }

    @Test(groups = "regression", enabled = false)
    // Disabled - known bug, see bug-reports/application/BUG-002-orderby-price-sort-403-forbidden.md
    public void sortingByPrice_showsSortedProducts() {
        CategoryPage categoryPage = homePage.openCoffeeAppliancesCategory();

        categoryPage.sortByPriceLowToHigh();

        Assert.assertTrue(categoryPage.hasProducts(),
                "Category page should still show products after sorting by price");
    }



    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
