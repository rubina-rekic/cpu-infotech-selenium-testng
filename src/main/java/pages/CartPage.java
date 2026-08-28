package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private final By cartPageBody =
            By.cssSelector("body.woocommerce-cart");

    private final By removeItemLink =
        By.cssSelector("a.remove.remove-product.position-absolute");

    private final By emptyCartMessage =
            By.cssSelector(".cart-empty");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(cartPageBody);
    }

    public void removeFirstItem() {
        

        click(removeItemLink);
    }

    public boolean isCartEmpty() {
        return isDisplayed(emptyCartMessage);
    }
}

