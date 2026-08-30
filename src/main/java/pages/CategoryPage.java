package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class CategoryPage extends BasePage {

    private final By productCards = By.cssSelector(".product");

    // The product title link inside each card - takes us to the individual product page.
    private final By firstProductTitleLink = By.cssSelector(".product h3.post-title a");

    private final By noResultsMessage = By.cssSelector("p.woocommerce-info");

    private final By sortDropdown = By.id("woocommerce-orderby-1");

    private final By notFoundIndicator = By.xpath("//*[contains(text(), '403 Forbidden')]");

    public boolean showsForbiddenError() {
        return isDisplayed(notFoundIndicator);
    }

    public void sortByPriceLowToHigh() {
        Select select = new Select(waitForVisible(sortDropdown));
        select.selectByValue("price");
    }

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
