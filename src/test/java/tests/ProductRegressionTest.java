package tests;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.CategoryPage;
import pages.HomePage;
import pages.ProductPage;

public class ProductRegressionTest {
    private WebDriver driver;
    private HomePage homePage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = utils.DriverFactory.createDriver();
        homePage = new HomePage(driver);
        homePage.open();
    }

    @Test(groups="regression")
    public void homePage_DiscountedProducts_ShowDiscountedPrice() {
        var categoryPage = homePage.openCoffeeAppliancesCategory();
        var productPage = categoryPage.openFirstProduct();

        Assert.assertTrue(productPage.hasDiscount(), "Product should have a discount displayed");
    }

    @Test(groups = "regression")
    public void productTitle_matchesBetweenCategoryAndProductPage() {
        CategoryPage categoryPage = homePage.openCoffeeAppliancesCategory();
        String titleFromCategoryPage = categoryPage.getFirstProductName();

        ProductPage productPage = categoryPage.openFirstProduct();
        String titleFromProductPage = productPage.getProductTitle();

        Assert.assertEquals(titleFromProductPage, titleFromCategoryPage,
                "Product title on the product page should match the title shown in the category listing");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
    
}
