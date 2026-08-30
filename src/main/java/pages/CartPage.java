package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {


    private final By cartPageBody =
            By.cssSelector("body.woocommerce-cart");

    private final By removeItemLink =
        By.cssSelector("a.remove.remove-product.position-absolute");

    private final By emptyCartMessage =
            By.cssSelector(".cart-empty");

    private final By quantityInput = By.cssSelector("input.qty");
private final By updateCartButton = By.cssSelector("button[name='update_cart']");

private final By productSubtotal = By.cssSelector("td.product-subtotal .amount");

private final By plusButton = By.cssSelector("button.plus");

public void increaseQuantityBy(int clicks) {
    int expectedFinalValue = 1 + clicks;
    WebElement quantityField = wait.until(ExpectedConditions.elementToBeClickable(quantityInput));

    JavascriptExecutor js = (JavascriptExecutor) driver;
    // Postavljamo vrijednost i okidamo jQuery + Native change/input događaje
    js.executeScript(
        "arguments[0].value = arguments[1];" +
        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
        "if (window.jQuery) { jQuery(arguments[0]).trigger('change'); }",
        quantityField, String.valueOf(expectedFinalValue)
    );

    // Sačekaj da dugme "Ažuriraj korpu" postane omogućeno (klikabilno)
    WebElement updateBtn = wait.until(ExpectedConditions.elementToBeClickable(updateCartButton));
    updateBtn.click();

    // Ključno: Sačekaj da se stranica/korpa ponovo učita (AJAX ili full reload)
    wait.until(ExpectedConditions.stalenessOf(quantityField));
}

public String getProductSubtotal() {
    return getText(productSubtotal);
}


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

