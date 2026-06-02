package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;

import config.ConfigReader;
import driver.DriverFactory;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import utils.ExtentReportManagerUtils;
import utils.ExtentTestUtils;
import utils.LogUtils;
import utils.ScreenshotUtils;

import java.io.File;

public class BaseTest {

    protected static ExtentReports extent;

    @BeforeSuite(alwaysRun = true)
    public void setupReport() {

        extent = ExtentReportManagerUtils.getReportInstance();

    }

    @AfterSuite(alwaysRun = true)
    public void teardownReport() {
        extent.flush();
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        LogUtils.warn("==========================TEST SET UP===========================");
        LogUtils.info("Setting up WebDriver");
        String browser = ConfigReader.getProperty("browser");
        DriverFactory.initDriver(browser);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        if (result.getStatus() == ITestResult.FAILURE) {

            String testName = result.getMethod().getMethodName();

            String screenshotPath = ScreenshotUtils.captureScreenshot(testName);
            String relativePath = "../screenshots/" + testName + "/" + new File(screenshotPath).getName();

            System.out.print("Screenshot path: " + screenshotPath);
            System.out.print("Relative path: " + relativePath);

            ExtentTestUtils.getTest().fail("Test failed. Screenshot attached",
                    MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build());
        }

        LogUtils.info("Quitting WebDriver");
        LogUtils.warn("==========================TEST TEAR DOWN==========================");
        DriverFactory.quitDriver();

    }

}
