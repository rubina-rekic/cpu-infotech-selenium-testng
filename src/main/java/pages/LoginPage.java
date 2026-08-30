package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for the login popup/form (WooCommerce "My Account" login),
 * triggered from the "Log In" link in the site header.
 */
public class LoginPage extends BasePage {

    private final By logInTriggerLink = By.cssSelector("a.porto-link-login");
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginSubmitButton = By.cssSelector("button[name='login']");
    private final By logoutLink = By.cssSelector("a[href*='customer-logout']");
    private final By logoutConfirmLink = By.linkText("Potvrdite i odjavite se");

    public void logout() {
        click(logoutLink);
        click(logoutConfirmLink);
    }
    public boolean isLoggedOut() {
        return isDisplayed(logInTriggerLink);
    }

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoggedIn() {
        return isDisplayed(logoutLink);
    }

    public void openLoginForm() {
        click(logInTriggerLink);
    }

    public void loginAs(String usernameOrEmail, String password) {
        type(usernameField, usernameOrEmail);
        type(passwordField, password);
        click(loginSubmitButton);
    }
}
