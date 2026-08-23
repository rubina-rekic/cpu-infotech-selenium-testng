package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public class ProductPage extends BasePage {

    private final By price = By.cssSelector("p.price");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public boolean isPriceDisplayed() {
        return isDisplayed(price);
    }
}