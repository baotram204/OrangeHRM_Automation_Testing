package utils;

import driver.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;

public class ScreenshotUtils {

    public static String captureScreenshot(String testcaseName) {

        WebDriver driver = DriverFactory.getDriver();

        String rootPath = System.getProperty("user.dir") + "/screenshots/";
        String testFolderPath = rootPath + testcaseName + "/";

        // Make sure the folder exists
        File testFolder = new File(testFolderPath);

        if (!testFolder.exists()) {
            testFolder.mkdirs();
        }

        // Take screenshot
        String fileName = testcaseName + "_" + System.currentTimeMillis() + ".png";
        String fullPath = testFolderPath + fileName;

        File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File(fullPath);

        try {

            FileUtils.copyFile(sourceFile, destFile);

        } catch (Exception e) {
            throw new RuntimeException("Failed to capture screenshot", e);
        }


        return fullPath;
    }

}
