package utils;

import com.aventstack.extentreports.ExtentTest;

public class ExtentTestUtils {

    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

}
