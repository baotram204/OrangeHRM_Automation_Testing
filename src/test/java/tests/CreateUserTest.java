package tests;

import base.BaseTest;
import config.ConfigReader;
import constants.AppConstants;
import driver.DriverFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import pages.AddUserPage;
import pages.AdminUserPage;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ExcelUtils;
import utils.ExtentTestUtils;
import utils.LogUtils;

import java.io.IOException;
import java.util.List;

/**
 *
 * Annotation groups:
 * 
 * @Test(groups = {"smoke"})
 * @Test(groups = {"regression"})
 * @Test(groups = {"negative"})
 * @Test(groups = {"security"})
 */
public class CreateUserTest extends BaseTest {

        private LoginPage loginPage;
        private AdminUserPage adminUserPage;
        private AddUserPage addUserPage;

        // SETUP
        // ===========================================================================
        @BeforeMethod(alwaysRun = true)
        public void loginBeforeTest() {
                loginPage = new LoginPage();
                adminUserPage = new AdminUserPage();

                LogUtils.info("Navigate to OrangeHRM Login page");

                DashboardPage dashboardPage = loginPage.open(ConfigReader.getProperty("baseUrl"))
                                .login(
                                                System.getProperty("admin.username",
                                                                ConfigReader.getProperty("username")),
                                                System.getProperty("admin.password",
                                                                ConfigReader.getProperty("password")));

                LogUtils.info("Navigate to User Management > Users");
                adminUserPage = dashboardPage.goToUsersPage();
        }

        // Helper
        // ============================================================================
        @DataProvider(name = "smartDataProvider", parallel = true)
        public Object[][] getExcelData(Method method) {
                try {
                        String filePath = AppConstants.EXCEL_FILE_PATH;

                        // LẤY TÊN HÀM TEST ĐỂ TÌM TÊN SHEET TƯƠNG ỨNG
                        String sheetName = method.getName();

                        ExcelUtils.loadExcel(filePath, sheetName);

                        int rows = ExcelUtils.getRowCount();
                        int cols = ExcelUtils.getColCount();

                        Object[][] data = new Object[rows - 1][cols];

                        for (int i = 1; i < rows; i++) {
                                for (int j = 0; j < cols; j++) {
                                        data[i - 1][j] = ExcelUtils.getCellData(i, j);
                                }
                        }
                        return data;

                } catch (Exception e) {
                        e.printStackTrace();
                        return new Object[0][0];
                } finally {
                        try {
                                ExcelUtils.closeExcel();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }
        }

        /**
         * UM_CREATE_001,UM_CREATE_002, UM_CREATE_003
         */
        @Test(groups = { "smoke", "regression",
                        "positive" }, description = "Verify that user can create user with valid data", dataProvider = "smartDataProvider")
        public void verifyCreateUser(String testCaseId, String userRole, String employeeName, String status,
                        String username, String password, String confirmPassword, String expectedResult) {

                if (userRole == null || userRole.trim().isEmpty()) {
                        return;
                }

                ExtentTestUtils.setTest(extent.createTest(
                                "[" + testCaseId + "] Create User - Role: " + userRole + ", Status: " + status));
                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, navigate to Add User form");

                addUserPage = adminUserPage.clickAddUser();

                // -- Steps --
                ExtentTestUtils.getTest().info("Step1: Choose User Role: " + userRole);
                addUserPage.chooseUserRole(userRole);

                ExtentTestUtils.getTest().info("Step2: Type Employee Name: " + employeeName);
                addUserPage.typeEmployeeName(employeeName);

                ExtentTestUtils.getTest().info("Step3: Choose Status: " + status);
                addUserPage.chooseStatus(status);

                ExtentTestUtils.getTest().info("Step4: Type Username: " + username);
                addUserPage.typeUsername(username);

                ExtentTestUtils.getTest().info("Step5: Type Password");
                addUserPage.typePassword(password);

                ExtentTestUtils.getTest().info("Step6: Type Confirm Password");
                addUserPage.typeConfirmPassword(confirmPassword);

                ExtentTestUtils.getTest().info("Step7: Click Save button");
                addUserPage.clickSave();

                // -- Assertion --
                boolean saved = addUserPage.isSuccessToastDisplayed();
                Assert.assertTrue(saved,
                                "Expected '" + expectedResult + "' toast but not found");

                ExtentTestUtils.getTest().pass(
                                testCaseId + " PASSED – Admin user created successfully");
        }

        /**
         * UM_CREATE_005, UM_CREATE_06
         */
        @Test(groups = { "smoke", "regression",
                        "positive" }, description = "Verify that auto-suggest displays matching employee names", dataProvider = "smartDataProvider")
        public void verifyEmployeeDropdown(String testCaseId, String employeeName) {
                ExtentTestUtils.setTest(extent.createTest(
                                "[" + testCaseId + "] Verify that auto-suggest displays matching employee names"));

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, navigate to Add User form");
                addUserPage = adminUserPage.clickAddUser();

                // -- Steps --

                ExtentTestUtils.getTest().info("Enter Employee Name: " + employeeName);
                boolean verify = addUserPage.verifyEmployeeDropdownOptions(employeeName);

                // -- Assertion --
                Assert.assertTrue(verify,
                                "Expected '" + employeeName + "' in employee dropdown but not found");

                ExtentTestUtils.getTest().pass(
                                testCaseId + " PASSED – Employee dropdown matching with text");
        }

        /**
         * UM_CREATE_008
         */
        @Test(groups = { "smoke", "regression",
                        "positive" }, description = "Verify that system prevents creation with form empty")
        public void verifyPreventSubmitWithFormEmpty() {

                ExtentTestUtils.setTest(
                                extent.createTest("UM_CREATE_008 - Verify prevent submit with form empty"));
                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, navigate to Add User form");

                addUserPage = adminUserPage.clickAddUser();

                // -- Steps --
                ExtentTestUtils.getTest().info("Click Save button");
                addUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = addUserPage.checkErrorMessage();
                Assert.assertTrue(errorMessage, "Error message not displayed");

                ExtentTestUtils.getTest().pass("UM_CREATE_008 PASSED – Prevent submit with form empty");
        }

        /**
         * UM_CREATE_009
         * 
         * @throws IOException
         */
        @Test(groups = { "regression",
                        "negative" }, description = "Verify that system prevents creation with duplicate username")
        public void verifyPreventSubmitWithDuplicateUsername() throws IOException {

                ExtentTestUtils.setTest(
                                extent.createTest("UM_CREATE_009 - Verify prevent submit with duplicate username"));
                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, navigate to Add User form");

                addUserPage = adminUserPage.clickAddUser();

                // -- Steps --
                String username = "TestUser_" + System.currentTimeMillis();
                String userRole = AppConstants.ROLE_ADMIN;
                String employeeName = AppConstants.EMPLOYEE_NAME_VALID;
                String status = AppConstants.STATUS_ENABLED;
                String password = AppConstants.PASSWORD_VALID;
                String confirmPassword = AppConstants.PASSWORD_VALID;

                LogUtils.info("data for testcase UM_CREATE_008 : " + username + " " + userRole + " " + employeeName
                                + " " + status + " " + password + " " + confirmPassword);

                // Create a new user
                ExtentTestUtils.getTest().info("Pre-condition: Create a new user with valid data");
                addUserPage.chooseUserRole(userRole);
                addUserPage.typeEmployeeName(employeeName);
                addUserPage.chooseStatus(status);
                addUserPage.typeUsername(username);
                addUserPage.typePassword(password);
                addUserPage.typeConfirmPassword(confirmPassword);
                addUserPage.clickSave();

                // Create user with duplicate username
                ExtentTestUtils.getTest().info("Redirect to Add User form");
                addUserPage = adminUserPage.clickAddUser();

                ExtentTestUtils.getTest().info("Create a new user with duplicate username");

                ExtentTestUtils.getTest().info("Step1: Choose User Role: " + userRole);
                addUserPage.chooseUserRole(userRole);

                ExtentTestUtils.getTest().info("Step2: Type Employee Name: " + employeeName);
                addUserPage.typeEmployeeName(employeeName);

                ExtentTestUtils.getTest().info("Step3: Choose Status: " + status);
                addUserPage.chooseStatus(status);

                ExtentTestUtils.getTest().info("Step4: Type Username: " + username);
                addUserPage.typeUsername(username);

                ExtentTestUtils.getTest().info("Step5: Type Password");
                addUserPage.typePassword(password);

                ExtentTestUtils.getTest().info("Step6: Type Confirm Password");
                addUserPage.typeConfirmPassword(confirmPassword);

                ExtentTestUtils.getTest().info("Step7: Click Save button");
                addUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = addUserPage.isErrorMessageDisplayed("Username");
                Assert.assertTrue(errorMessage, "Error message not displayed");

                String errorMsg = addUserPage.getTextErrorMessage("Username");
                Assert.assertEquals(errorMsg, AppConstants.MSG_ALREADY_EXISTS);

                ExtentTestUtils.getTest().pass("UM_CREATE_009 PASSED – Prevent submit with duplicate username");
        }

        /**
         * UM_CREATE_010, UM_CREATE_011, UM_CREATE_012
         */
        @Test(groups = { "regression",
                        "negative", "positive" }, description = "Verify for valid username", dataProvider = "smartDataProvider")
        public void verifyValidUsername(String testCaseId, String username, String expectedResult) {
                ExtentTestUtils.setTest(
                                extent.createTest("[" + testCaseId + "] - Verify valid username - " + username + " - "
                                                + expectedResult));

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, navigate to Add User form");
                addUserPage = adminUserPage.clickAddUser();

                // --Steps--
                ExtentTestUtils.getTest().info("Type Username: " + username);
                addUserPage.typeUsername(username);

                // --Assertion--
                boolean errorMessage = addUserPage.isErrorMessageDisplayed("Username");
                if (expectedResult == null || expectedResult.trim().isEmpty()) {
                        Assert.assertFalse(errorMessage, "Expected no error message but one was displayed");
                } else {
                        Assert.assertTrue(errorMessage, "Expected error message but not displayed");
                        String errorMsg = addUserPage.getTextErrorMessage("Username");
                        Assert.assertEquals(errorMsg, expectedResult);
                }
                ExtentTestUtils.getTest().pass("UM_CREATE_010 PASSED – Verify valid username");

        }

        /**
         * UM_CREATE_013, UM_CREATE_014, UM_CREATE_015, UM_CREATE_016, UM_CREATE_017
         */
        @Test(groups = { "regression",
                        "negative" }, description = "Verify for password", dataProvider = "smartDataProvider")
        public void verifyPassword(String testCaseId, String password, String expectedResult) {
                ExtentTestUtils.setTest(
                                extent.createTest("[" + testCaseId + "] - Verify password - " + expectedResult));

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, navigate to Add User form");
                addUserPage = adminUserPage.clickAddUser();

                // --Steps--
                ExtentTestUtils.getTest().info("Type Password: " + password);
                addUserPage.typePassword(password);

                // --Assertion--
                boolean errorMessage = addUserPage.isErrorMessageDisplayed("Password");
                if (expectedResult == null || expectedResult.trim().isEmpty()) {
                        Assert.assertFalse(errorMessage, "Expected no error message but one was displayed");
                } else {
                        Assert.assertTrue(errorMessage, "Expected error message but not displayed");
                        String errorMsg = addUserPage.getTextErrorMessage("Password");
                        Assert.assertEquals(errorMsg, expectedResult);
                }
                ExtentTestUtils.getTest().pass("UM_CREATE_013 PASSED – Verify password");
        }

        /**
         * UM_CREATE_018
         */
        @Test(groups = { "regression", "negative" }, description = "Verify password and confirm password mismatch")
        public void verifyPasswordMismatch() {
                ExtentTestUtils.setTest(
                                extent.createTest("UM_CREATE_018 - Verify password and confirm password mismatch"));
                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, navigate to Add User form");

                addUserPage = adminUserPage.clickAddUser();

                // --Steps--
                String username = "TestUser_" + System.currentTimeMillis();
                String userRole = AppConstants.ROLE_ADMIN;
                String employeeName = AppConstants.EMPLOYEE_NAME_VALID;
                String status = AppConstants.STATUS_ENABLED;
                String password = AppConstants.PASSWORD_VALID;
                String confirmPassword = AppConstants.PASSWORD_INVALID;

                LogUtils.info("data for testcase UM_CREATE_018 : " + username + " " + userRole + " " + employeeName
                                + " " + status + " " + password + " " + confirmPassword);

                // Create a new user
                ExtentTestUtils.getTest().info("Pre-condition: Create a new user with valid data");
                addUserPage.chooseUserRole(userRole);
                addUserPage.typeEmployeeName(employeeName);
                addUserPage.chooseStatus(status);
                addUserPage.typeUsername(username);
                addUserPage.typePassword(password);
                addUserPage.typeConfirmPassword(confirmPassword);
                addUserPage.clickSave();

                // --Assertion--
                boolean errorMessage = addUserPage.isErrorMessageDisplayed("Confirm Password");
                Assert.assertTrue(errorMessage, "Error message not displayed");

                String errorMsg = addUserPage.getTextErrorMessage("Confirm Password");
                Assert.assertEquals(errorMsg, AppConstants.MSG_PASS_NOT_MATCH);

                ExtentTestUtils.getTest().pass("UM_CREATE_018 PASSED – Verify password and confirm password mismatch");
        }

        /**
         * UM_CREATE_019
         */
        @Test(groups = { "regression", "negative" }, description = "Verify type confirm pass before password")
        public void verifyTypePasswordAfterConfirmPassword() {

                ExtentTestUtils.setTest(extent.createTest(
                                "UM_CREATE_019 - Verify that system shows error when Password is changed after Confirm Password is entered"));
                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, navigate to Add User form");

                addUserPage = adminUserPage.clickAddUser();

                // --Steps--
                String username = "TestUser_" + System.currentTimeMillis();
                String userRole = AppConstants.ROLE_ADMIN;
                String employeeName = AppConstants.EMPLOYEE_NAME_VALID;
                String status = AppConstants.STATUS_ENABLED;
                String password = AppConstants.PASSWORD_VALID;
                String confirmPassword = AppConstants.PASSWORD_INVALID;
                String passwordNew = "Valid@124";

                LogUtils.info("data for testcase UM_CREATE_019 : " + username + " " + userRole + " " + employeeName
                                + " " + status + " " + password + " " + confirmPassword);

                // Create a new user
                ExtentTestUtils.getTest().info("Pre-condition: Create a new user with valid data");
                addUserPage.chooseUserRole(userRole);
                addUserPage.typeEmployeeName(employeeName);
                addUserPage.chooseStatus(status);
                addUserPage.typeUsername(username);
                addUserPage.typePassword(password);
                addUserPage.typeConfirmPassword(confirmPassword);
                addUserPage.typePassword(passwordNew);
                addUserPage.clickSave();

                // --Assertion--
                boolean errorMessage = addUserPage.isErrorMessageDisplayed("Confirm Password");
                Assert.assertTrue(errorMessage, "Error message not displayed");

                String errorMsg = addUserPage.getTextErrorMessage("Confirm Password");
                Assert.assertEquals(errorMsg, AppConstants.MSG_PASS_NOT_MATCH);

                ExtentTestUtils.getTest().pass(
                                "UM_CREATE_019 PASSED – Verify that system shows error when Password is changed after Confirm Password is entered");
        }

}
