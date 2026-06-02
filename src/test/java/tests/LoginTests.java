package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import config.ConfigReader;
import driver.DriverFactory;
import pages.LoginPage;

public class LoginTests extends BaseTest {

        @Test(priority = 1)
        public void testLoginValid() {
                LoginPage loginPage = new LoginPage();
                loginPage.open(ConfigReader.getProperty("baseUrl"))
                                .login(ConfigReader.getProperty("username"), ConfigReader.getProperty("password"));

                Assert.assertTrue(DriverFactory.getDriver().getCurrentUrl().contains("dashboard"),
                                "Login successfully but not redirected to dashboard");
        }

}