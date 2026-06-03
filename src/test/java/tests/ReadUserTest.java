package tests;

import base.BaseTest;
import config.ConfigReader;
import constants.AppConstants;

import java.lang.reflect.Method;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import pages.AddUserPage;
import pages.AdminUserPage;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ExcelUtils;
import utils.ExtentTestUtils;
import utils.LogUtils;

public class ReadUserTest extends BaseTest {
        private LoginPage loginPage;
        private AdminUserPage adminUserPage;

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
         * UM_READ_001
         */
        @Test(groups = { "smoke",
                        "positive" }, description = "Verify that the system displays the user list when navigating to the User Management page.")
        public void verifyUserListIsDisplayed() {
                LogUtils.info("Verify that the user list is displayed.");

                ExtentTestUtils.setTest(
                                extent.createTest("UM_READ_001 - Verify the system displays the user list"));

                int getTableRowCount = adminUserPage.getTableRowCount();
                int getDisplayedRecordCount = adminUserPage.getDisplayedRecordCount();
                LogUtils.info("Table row count: " + getTableRowCount);
                LogUtils.info("Displayed record count: " + getDisplayedRecordCount);

                if (getDisplayedRecordCount > 0) {
                        Assert.assertTrue(adminUserPage.isUserTableDisplayed(), "User table is displayed");
                        ExtentTestUtils.getTest()
                                        .pass("UM_READ_001 PASSED – System displays the user list with "
                                                        + getTableRowCount
                                                        + " records.");
                } else {
                        LogUtils.error("No records found in the table.");
                        ExtentTestUtils.getTest()
                                        .fail("UM_READ_001 FAILED – No records found in the table.");
                }
        }

        /**
         * UM_READ_002
         */
        @Test(groups = { "smoke",
                        "positive" }, description = "Verify that the system returns all users when all search fields are left blank.")
        public void verifyAllUsersAreReturnedWhenAllSearchFieldsAreLeftBlank() {
                LogUtils.info("Verify that the user list when all search fields are left blank.");

                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_002 - Verify that the system returns all users when all search fields are left blank."));

                // --Steps--
                int getTableRowCount = adminUserPage.getTableRowCount();
                int getDisplayedRecordCount = adminUserPage.getDisplayedRecordCount();
                LogUtils.info("Table row count: " + getTableRowCount);
                LogUtils.info("Displayed record count: " + getDisplayedRecordCount);

                ExtentTestUtils.getTest().info("Click search button");
                adminUserPage.clickSearch();

                // --Assertion--
                if (getDisplayedRecordCount > 0) {
                        Assert.assertTrue(adminUserPage.isUserTableDisplayed(), "User table is displayed");
                        ExtentTestUtils.getTest()
                                        .pass("UM_READ_001 PASSED – System displays the user list with "
                                                        + getTableRowCount
                                                        + " records.");
                } else {
                        LogUtils.error("No records found in the table.");
                        ExtentTestUtils.getTest()
                                        .fail("UM_READ_001 FAILED – No records found in the table.");
                }
        }

        /**
         * UM_READ_003
         */
        @Test(groups = { "positive" }, description = "Verify that the system filter role Admin")
        public void verifyFilterByRoleAdmin() {
                LogUtils.info("Verify that the system filter follow role");

                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_003 - Verify that the system filter follow role."));

                String role = AppConstants.ROLE_ADMIN;

                ExtentTestUtils.getTest().info("Step1: Choose User Role: " + role);
                adminUserPage.chooseUserRole(role);

                ExtentTestUtils.getTest().info("Step2: Click search button");
                adminUserPage.clickSearch();

                ExtentTestUtils.getTest().info("Step3: Check filter admin role");
                List<String> roleList = adminUserPage.getDataFollowField("User Role");

                if (!roleList.isEmpty()) {
                        Assert.assertTrue(roleList.stream().allMatch(item -> item.equals(role)),
                                        "Only Admin users displayed");
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_003 PASSED - System display only Admin users");
                }

        }

        /**
         * UM_READ_004
         */
        @Test(groups = { "positive" }, description = "Verify that the system filter role ESS")
        public void verifyFilterByRoleESS() {
                LogUtils.info("Verify that the system filter follow role");

                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_005 - Verify that the system filter follow role."));

                String role = AppConstants.ROLE_ESS;

                ExtentTestUtils.getTest().info("Step1: Choose User Role: " + role);
                adminUserPage.chooseUserRole(role);

                ExtentTestUtils.getTest().info("Step2: Click search button");
                adminUserPage.clickSearch();

                ExtentTestUtils.getTest().info("Step3: Check filter ESS role");
                List<String> roleList = adminUserPage.getDataFollowField("User Role");

                if (!roleList.isEmpty()) {
                        Assert.assertTrue(roleList.stream().allMatch(item -> item.equals(role)),
                                        "Only ESS users displayed");
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_005 PASSED - System display only ESS users");
                }
        }

        /**
         * UM_READ_005
         */
        @Test(groups = { "positive" }, description = "Verify that the system returns matching user(s) when a valid Employee Name is entered and selected.")
        public void verifyFilterByEmployeeName() {
                LogUtils.info("Verify that the system filter follow employee name");

                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_006 - Verify that the system filter follow employee name."));

                String employeeName = AppConstants.EMPLOYEE_NAME_VALID;

                ExtentTestUtils.getTest().info("Step1: Type Employee Name: " + employeeName);
                adminUserPage.typeEmployeeName(employeeName);

                ExtentTestUtils.getTest().info("Step2: Click search button");
                adminUserPage.clickSearch();

                ExtentTestUtils.getTest().info("Step3: Check filter employee name");
                List<String> employeeNameList = adminUserPage.getDataFollowField("Employee Name");

                // --Assertion--
                boolean errorMessage = adminUserPage.isErrorMessageDisplayed("Employee Name");
                Assert.assertFalse(errorMessage, "Error message not displayed");

                if (!employeeNameList.isEmpty()) {
                        Assert.assertTrue(employeeNameList.stream().allMatch(name -> name.contains(employeeName)),
                                        "Only users with employee name " + employeeName + " displayed");
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_005 PASSED - System display only users with employee name "
                                                        + employeeName);
                }

        }

        /**
         * UM_READ_006
         */
        @Test(groups = { "smoke", "regression",
                        "positive" }, description = "Verify that auto-suggest displays matching employee names")
        public void verifyEmployeeDropdown() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_006 - Verify that auto-suggest displays matching employee names"));

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, navigate to Add User form");
                adminUserPage = adminUserPage.clickAddUser();

                // -- Steps --
                String employeeName = "Joh";
                ExtentTestUtils.getTest().info("Enter Employee Name: " + employeeName);
                boolean verify = adminUserPage.verifyEmployeeDropdownOptions(employeeName);

                // -- Assertion --
                Assert.assertTrue(verify,
                                "Expected '" + employeeName + "' in employee dropdown but not found");

                ExtentTestUtils.getTest().pass(
                                "UM_READ_006 PASSED – Employee dropdown matching with text");
        }

        /**
         * UM_READ_007
         */
        @Test(groups = { "positive" }, description = "Verify that the system does not return results if employee name is not selected from dropdown.")
        public void verifyNoResultsReturnedForUnselectedEmployeeName() {
                LogUtils.info("Verify that the system does not return results if employee name is not selected from dropdown.");

                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_007 - Verify that the system does not return results if employee name is not selected from dropdown."));

                String employeeName = AppConstants.EMPLOYEE_NAME_VALID;

                ExtentTestUtils.getTest().info("Step1: Type Employee Name: " + employeeName);
                adminUserPage.typeEmployeeNameWithoutSelecting(employeeName);

                ExtentTestUtils.getTest().info("Step2: Click search button");
                adminUserPage.clickSearch();

                // --Assertion---
                boolean noResult = adminUserPage.isErrorMessageDisplayed("Employee Name");
                Assert.assertTrue(noResult,
                                "Expected can't search");

                ExtentTestUtils.getTest().pass(
                                "UM_READ_007 PASSED - System can't search without select option");

        }

        /**
         * UM_READ_008
         */
        @Test(groups = { "negative" }, description = "Verify that the system shows no results when an invalid (non-existing) Employee Name is entered.")
        public void verifyFilterByInvalidEmployeeName() {
                LogUtils.info("Verify that the system shows no results when an invalid (non-existing) Employee Name is entered.");

                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_008 - Verify that the system shows no results when an invalid (non-existing) Employee Name is entered."));

                String employeeName = AppConstants.EMPLOYEE_NAME_INVALID;

                ExtentTestUtils.getTest().info("Step1: Type Employee Name: " + employeeName);
                adminUserPage.typeEmployeeNameWithoutSelecting(employeeName);

                ExtentTestUtils.getTest().info("Step2: Click search button");
                adminUserPage.clickSearch();

                // --Assertion---
                boolean noResult = adminUserPage.isErrorMessageDisplayed("Employee Name");
                Assert.assertTrue(noResult,
                                "No records found");

                ExtentTestUtils.getTest().pass(
                                "UM_READ_008 PASSED - System can not display data with invalid employee name");

        }

        /**
         * UM_READ_009
         */
        @Test(groups = { "regression",
                        "negative",
                        "positive" }, description = "Verify for valid username")
        public void verifyValidUsername() {
                ExtentTestUtils.setTest(
                                extent.createTest("UM_READ_009 - Verify valid username"));

                // --Steps--
                String username = AppConstants.USERNAME_ADMIN;
                ExtentTestUtils.getTest().info("Type Username: " + username);
                adminUserPage.typeUsername(username);

                ExtentTestUtils.getTest().info("Click search button");
                adminUserPage.clickSearch();

                // --Assertion--
                List<String> usernameList = adminUserPage.getDataFollowField("Username");
                if (!usernameList.isEmpty()) {
                        Assert.assertTrue(usernameList.stream().allMatch(name -> name.contains(username)),
                                        "Only users with username " + username + " displayed");
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_009 PASSED - System display only users with username "
                                                        + username);
                }

        }

        /**
         * UM_READ_010
         */
        @Test(groups = { "regression",
                        "negative",
                        "positive" }, description = "Verify for invalid username")
        public void verifyInvalidUsername() {
                ExtentTestUtils.setTest(
                                extent.createTest("UM_READ_010 - Verify invalid username"));

                // --Steps--
                String username = AppConstants.USERNAME_INVALID;
                ExtentTestUtils.getTest().info("Type Username: " + username);
                adminUserPage.typeUsername(username);

                ExtentTestUtils.getTest().info("Click search button");
                adminUserPage.clickSearch();

                // --Assertion--
                boolean infoMsg = adminUserPage.isInfoToastDisplayed();
                Assert.assertTrue(infoMsg, "Expected info message but not displayed");
                ExtentTestUtils.getTest().info("Info message: " + infoMsg);

                ExtentTestUtils.getTest().pass(
                                "UM_READ_010 PASSED - System display only users with username "
                                                + username);

        }

}
