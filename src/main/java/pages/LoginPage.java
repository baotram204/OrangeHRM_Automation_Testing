package pages;

import org.openqa.selenium.By;
import utils.LogUtils;

public class LoginPage extends BasePage {

    public LoginPage open(String url) {
        getDriver().get(url);
        return this;
    }

    // Locator
    private final By usernameField = By.name("username");
    private final By passwordField = By.name("password");
    private final By loginButton = By.cssSelector("button[type='submit']");
    private final By errorMessage = By.cssSelector(".oxd-alert-content-text");
    private final By dashboardHeader = By.cssSelector("h6.oxd-text--h6");


    /**
     * Type username into username text box
     */
    public LoginPage enterUsername(String username) {
        LogUtils.info("Enter username: " + username);
        type(usernameField, username);
        return this;
    }

    /**
     * Type password into password text box
     */
    public LoginPage enterPassword(String password) {
        LogUtils.info("Enter password: " + password);
        type(passwordField, password);
        return this;
    }

    /**
     * Click Login button
     */
    public DashboardPage clickLogin() {
        LogUtils.info("Click login button");
        click(loginButton);
        return new DashboardPage();
    }

    /**
     * Complete login
     */
    public DashboardPage login(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    // ===================== GETTER / ASSERTION HELPER =====================

    /**
     * Get error message text when login failed
     */
    public String getErrorMessage() {
        return getText(errorMessage);
    }

    /**
     * Check if Dashboard is displayed => login success
     */
    public boolean isDashboardDisplayed() {
        return isDisplayed(dashboardHeader);
    }

    /**
     * Check error message is displayed
     */
    public boolean isErrorDisplayed() {
        return isDisplayed(errorMessage);
    }
}
