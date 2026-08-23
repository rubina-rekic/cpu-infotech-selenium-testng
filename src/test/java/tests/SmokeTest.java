package tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.CartPage;
import pages.CategoryPage;
import pages.HomePage;
import pages.ProductPage;
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

    @Test(groups = "smoke")
    public void categoryPageShowsProducts() {
        CategoryPage categoryPage = homePage.openCoffeeAppliancesCategory();

        Assert.assertTrue(categoryPage.hasProducts(),
                "Category page should display at least one product");
    }

    @Test(groups = "smoke")
    public void searchResultsShowProducts() {
        CategoryPage searchResultsPage = homePage.searchFor("laptop");

        Assert.assertTrue(searchResultsPage.hasProducts(),
                "Search results page should display at least one product");
    }

    @Test(groups = "smoke")
    public void productPageShowsPrice() {
        CategoryPage categoryPage = homePage.openCoffeeAppliancesCategory();
        ProductPage productPage = categoryPage.openFirstProduct();

        Assert.assertTrue(productPage.isPriceDisplayed(),
                "Product page should display a price");
    }

    @Test(groups = "smoke")
    public void cartPageOpensSuccessfully() {
        CartPage cartPage = homePage.openCart();

        Assert.assertTrue(cartPage.isLoaded(),
                "Cart page should load successfully when accessed from the header");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}