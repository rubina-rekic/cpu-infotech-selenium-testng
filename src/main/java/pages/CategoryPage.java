package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CategoryPage extends BasePage {

    private final By productCards = By.cssSelector(".product");

    // The product title link inside each card - takes us to the individual product page.
    private final By firstProductTitleLink = By.cssSelector(".product h3.post-title a");

    private final By noResultsMessage = By.cssSelector("p.woocommerce-info");

    public boolean hasNoResultsMessage() {
        return isDisplayed(noResultsMessage);
    }

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public int getProductCount() {
        return driver.findElements(productCards).size();
    }

    public boolean hasProducts() {
        return getProductCount() > 0;
    }

    public ProductPage openFirstProduct() {
        click(firstProductTitleLink);
        return new ProductPage(driver);
    }
}
