package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    /**
     * init browser
     */
    public static void initDriver(String browser) {
        try {

            if (browser.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                org.openqa.selenium.chrome.ChromeOptions options = new org.openqa.selenium.chrome.ChromeOptions();
                driver.set(new ChromeDriver(options));
            } else {
                throw new IllegalStateException("Browser do not support");
            }

            getDriver().manage().window().maximize();

            getDriver().manage().timeouts()
                    .pageLoadTimeout(Duration.ofSeconds(30));

            getDriver().manage().timeouts()
                    .scriptTimeout(Duration.ofSeconds(20));

        } catch (Exception e) {
            e.printStackTrace();
            quitDriver();
            throw e;
        }

    }

    /**
     * get driver instance for current thread
     * 
     * @return driver
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * quit driver
     */
    public static void quitDriver() {
        if (getDriver() != null) {
            getDriver().quit();

            // remove the driver instance from ThreadLocal to prevent memory leaks
            driver.remove();
        }
    }

}
