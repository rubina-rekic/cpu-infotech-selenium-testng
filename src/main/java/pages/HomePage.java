package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;


public class HomePage extends BasePage {

    private final By mainMenu = By.cssSelector("[data-id='abe7457']");

    private final By searchInput = By.name("s");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("https://cpuinfotech.ba/");
    }

    public boolean isMenuVisible() {
        return isDisplayed(mainMenu);
    }

    public void searchFor(String term) {
        type(searchInput, term);
        waitForVisible(searchInput).sendKeys(Keys.ENTER);
    }
}