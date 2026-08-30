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

/**
 * Regression tests for cart functionality: adding, updating, and removing items
 */
public class CartRegressionTest {

    private WebDriver driver;
    private HomePage homePage;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.createDriver();
        homePage = new HomePage(driver);
        homePage.open();
    }

    @Test(groups = "regression")
    public void addingAndRemovingProduct_updatesCartCorrectly() {
        int countBefore = homePage.getHeaderCartItemCount();

        CategoryPage categoryPage = homePage.openCoffeeAppliancesCategory();
        ProductPage productPage = categoryPage.openFirstProduct();
        productPage.addToCart();

        int countAfterAdding = homePage.getHeaderCartItemCount();
        Assert.assertEquals(countAfterAdding, countBefore + 1,
                "Cart item count should increase by 1 after adding a product");

        CartPage cartPage = homePage.openCart();
        cartPage.removeFirstItem();

        Assert.assertTrue(cartPage.isCartEmpty(),
                "Cart should be empty after removing the only item in it");
    }

    @Test(groups = "regression")
    public void updatingQuantity_updatesSubtotal() {
        homePage.searchFor("hama");
        CategoryPage searchResults = new CategoryPage(driver);
        ProductPage productPage = searchResults.openFirstProduct();
        productPage.addToCart();

        CartPage cartPage = homePage.openCart();
        String subtotalBefore = cartPage.getProductSubtotal();

        cartPage.increaseQuantityBy(2); // Increase quantity from 1 to 3
        String subtotalAfter = cartPage.getProductSubtotal();

        Assert.assertNotEquals(subtotalAfter, subtotalBefore,
                "Product subtotal should change after updating quantity from 1 to 3");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            try {
                CartPage cartPage = new CartPage(driver);
                driver.get("https://cpuinfotech.ba/cart/");
                if (!cartPage.isCartEmpty()) {
                    cartPage.removeFirstItem();
                }
            } catch (Exception e) {

            } finally {
                driver.quit();
            }
        }
    }
}
