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
 * Regression tests for search behavior - covers scenarios beyond the
 * basic "search returns results" smoke test, including negative cases.
 */
public class SearchRegressionTest {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        homePage = new HomePage(driver);
        homePage.open();
    }

    @Test(groups = "regression")
    public void searchingForNonexistentTerm_showsNoResultsMessage() {
        CategoryPage resultsPage = homePage.searchFor("sdkjqwepoiqwe123456");

        Assert.assertTrue(resultsPage.hasNoResultsMessage(),
                "A clear 'no products found' message should be shown for a search term with no matches");
    }

    @Test(groups = "regression") 
    public void partialSearchTerm_returnsMatchingProducts(){
        CategoryPage resultsPage=homePage.searchFor("kafe");
        Assert.assertTrue(resultsPage.getProductCount()!=0,
                "Searching for a partial term should return matching products");

    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}