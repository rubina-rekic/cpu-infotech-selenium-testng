package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;


public class HomePage extends BasePage {

    private final By mainMenu = By.cssSelector("[data-id='abe7457']");

    private final By searchInput = By.name("s");

    private final By whiteGoodsTopLink = By.linkText("BIJELA TEHNIKA & KLIME");

    private final By coffeeAppliancesSubcategoryLink = By.cssSelector("a[href='/kategorija/kafe-aparati/']");
    
    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("https://cpuinfotech.ba/");
    }

    public boolean isMenuVisible() {
        return isDisplayed(mainMenu);
    }

    public CategoryPage searchFor(String term) {
        type(searchInput, term);
        waitForVisible(searchInput).sendKeys(Keys.ENTER);

        return new CategoryPage(driver);
    }

    public CategoryPage openCoffeeAppliancesCategory() {
    Actions actions = new Actions(driver);
    actions.moveToElement(waitForVisible(whiteGoodsTopLink)).perform();
    click(coffeeAppliancesSubcategoryLink);
    return new CategoryPage(driver);
}

}