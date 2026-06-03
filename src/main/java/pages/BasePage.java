package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
import org.openqa.selenium.support.ui.WebDriverWait;

import driver.DriverFactory;
import utils.WaitUtils;

public class BasePage {

    protected int timeout = 30;

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    public void click(By locator) {
        WaitUtils.waitForClickable(getDriver(), locator, timeout).click();
    }

    public void type(By locator, String text) {
        WebElement el = WaitUtils.waitForVisible(getDriver(), locator, timeout);
        el.clear();
        el.sendKeys(text);
    }

    public String getText(By locator) {
        return WaitUtils.waitForVisible(getDriver(), locator, timeout).getText();
    }

    public boolean isDisplayed(By locator) {
        return isDisplayed(locator, timeout);
    }

    public boolean isDisplayed(By locator, int customTimeout) {
        try {
            return WaitUtils.waitForVisible(getDriver(), locator, customTimeout).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<WebElement> getElementsList(By locator) {
        return WaitUtils.waitForVisibleElements(getDriver(), locator, timeout);
    }

    public void waitForInvisibility(By locator) {
        WaitUtils.waitForInvisibility(getDriver(), locator, timeout);
    }
}
