package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage extends BasePage {

    private final By price = By.cssSelector("p.price");
    private final By addToCartButton = By.cssSelector("button[name='add-to-cart']");
    private final By productTitle = By.cssSelector("h2.product_title");
    private final By originalPrice = By.cssSelector("p.price del .amount");
private final By salePrice = By.cssSelector("p.price ins .amount");

public boolean hasDiscount() {
    return isDisplayed(originalPrice) && isDisplayed(salePrice);
}

    public void addToCart() {
        click(addToCartButton);
    }

    public String getProductTitle() {
        return getText(productTitle);
    }

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPriceDisplayed() {
        return isDisplayed(price);
    }
}