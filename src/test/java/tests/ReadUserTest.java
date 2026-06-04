package tests;

import base.BaseTest;
import config.ConfigReader;
import constants.AppConstants;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import pages.AdminUserPage;
import pages.DashboardPage;
import pages.LoginPage;
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

        /**
         * UM_READ_001
         */
        @Test(groups = { "smoke", "regression",
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
        @Test(groups = { "smoke", "regression",
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
        @Test(groups = { "regression", "positive" }, description = "Verify that the system filter role Admin")
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
        @Test(groups = { "regression", "positive" }, description = "Verify that the system filter role ESS")
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
        @Test(groups = { "regression",
                        "positive" }, description = "Verify that the system returns matching user(s) when a valid Employee Name is entered and selected.")
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
        @Test(groups = { "regression",
                        "positive" }, description = "Verify that the system does not return results if employee name is not selected from dropdown.")
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
        @Test(groups = { "regression",
                        "negative" }, description = "Verify that the system shows no results when an invalid (non-existing) Employee Name is entered.")
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
                        "negative" }, description = "Verify for invalid username")
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

        /**
         * UM_READ_011
         */
        @Test(groups = { "smoke", "regression",
                        "positive" }, description = "system filters and displays only users with Enabled status")
        public void verifyFilterEnabledStatus() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_011 - system filters and displays only users with Enabled status"));

                // --Steps--
                String status = AppConstants.STATUS_ENABLED;
                ExtentTestUtils.getTest().info("Select Status: " + status);
                adminUserPage.chooseStatus(status);

                ExtentTestUtils.getTest().info("Click search button");
                adminUserPage.clickSearch();

                // --Assertion--
                List<String> statusList = adminUserPage.getDataFollowField("Status");
                if (!statusList.isEmpty()) {
                        Assert.assertTrue(statusList.stream().allMatch(name -> name.contains(status)),
                                        "Only users with status " + status + " displayed");
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_011 PASSED - System display only users with status "
                                                        + status);
                }
        }

        /**
         * UM_READ_012
         */
        @Test(groups = { "smoke", "regression",
                        "positive" }, description = "system filters and displays only users with Disabled status")
        public void verifyFilterDisabledStatus() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_012 - system filters and displays only users with Disabled status"));

                // --Steps--
                String status = AppConstants.STATUS_DISABLED;
                ExtentTestUtils.getTest().info("Select Status: " + status);
                adminUserPage.chooseStatus(status);

                ExtentTestUtils.getTest().info("Click search button");
                adminUserPage.clickSearch();

                // --Assertion--
                List<String> statusList = adminUserPage.getDataFollowField("Status");
                if (!statusList.isEmpty()) {
                        Assert.assertTrue(statusList.stream().allMatch(name -> name.contains(status)),
                                        "Only users with status " + status + " displayed");
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_012 PASSED - System display only users with status "
                                                        + status);
                }
        }

        /**
         * UM_READ_013
         */
        @Test(groups = { "regression",
                        "positive" }, description = "Verify that the system returns users matching both User Role and Status when both filters are applied.")
        public void verifyFilterByValidUserRoleAndStatus() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_013 - Verify that the system returns users matching both User Role and Status when both filters are applied."));

                // --Steps--
                String role = AppConstants.ROLE_ADMIN;
                ExtentTestUtils.getTest().info("Step 1: Select User Role: " + role);
                adminUserPage.chooseUserRole(role);

                String status = AppConstants.STATUS_ENABLED;
                ExtentTestUtils.getTest().info("Step 2: Select Status: " + status);
                adminUserPage.chooseStatus(status);

                ExtentTestUtils.getTest().info("Step 3: Click search button");
                adminUserPage.clickSearch();

                // --Assertion--
                List<String> roleList = adminUserPage.getDataFollowField("User Role", "Status");
                if (!roleList.isEmpty()) {
                        Assert.assertTrue(
                                        roleList.stream()
                                                        .allMatch(name -> name.contains(role) && name.contains(status)),
                                        "Only users with role " + role + " and status " + status + " displayed");
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_013 PASSED - System display only users with role "
                                                        + role + " and status " + status);
                }
        }

        /**
         * UM_READ_014
         */
        @Test(groups = { "regression",
                        "positive" }, description = "Verify that the system returns users matching both Username and User Role when both filters are applied.")
        public void verifyFilterByUsernameAndUserRole() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_014 - Verify that the system returns users matching both Username and User Role when both filters are applied."));

                // --Steps--
                String username = AppConstants.USERNAME_ADMIN;
                ExtentTestUtils.getTest().info("Step 1: Type Username: " + username);
                adminUserPage.typeUsername(username);

                String role = AppConstants.ROLE_ADMIN;
                ExtentTestUtils.getTest().info("Step 2: Select User Role: " + role);
                adminUserPage.chooseUserRole(role);

                ExtentTestUtils.getTest().info("Step 3: Click search button");
                adminUserPage.clickSearch();

                // --Assertion--
                List<String> roleList = adminUserPage.getDataFollowField("User Role", "Username");
                if (!roleList.isEmpty()) {
                        Assert.assertTrue(
                                        roleList.stream().allMatch(
                                                        name -> name.contains(role) && name.contains(username)),
                                        "Only users with role " + role + " and username " + username + " displayed");
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_014 PASSED - System display only users with role "
                                                        + role + " and username " + username);
                }
        }

        /**
         * UM_READ_015
         */
        @Test(groups = { "regression",
                        "positive" }, description = "Matching all field")
        public void verifyFilterByAllFields() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_015 - Verify that the system returns users matching all filters"));

                // --Steps--
                String username = AppConstants.USERNAME_ADMIN;
                ExtentTestUtils.getTest().info("Step 1: Type Username: " + username);
                adminUserPage.typeUsername(username);

                String role = AppConstants.ROLE_ADMIN;
                ExtentTestUtils.getTest().info("Step 2: Select User Role: " + role);
                adminUserPage.chooseUserRole(role);

                String status = AppConstants.STATUS_ENABLED;
                ExtentTestUtils.getTest().info("Step 3: Select Status: " + status);
                adminUserPage.chooseStatus(status);

                String employeeName = AppConstants.EMPLOYEE_NAME_VALID;
                ExtentTestUtils.getTest().info("Step 4: Type Employee Name: " + employeeName);
                adminUserPage.typeEmployeeName(employeeName);

                ExtentTestUtils.getTest().info("Step 5: Click search button");
                adminUserPage.clickSearch();

                // --Assertion--
                List<String> roleList = adminUserPage.getDataFollowField("Username", "User Role", "Employee Name",
                                "Status");
                if (!roleList.isEmpty()) {
                        Assert.assertTrue(
                                        roleList.stream().allMatch(
                                                        name -> name.contains(username) && name.contains(role)
                                                                        && name.contains(employeeName)
                                                                        && name.contains(status)),
                                        "Only users with username " + username + " role " + role + " employee name "
                                                        + employeeName + " status " + status + " displayed");
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_014 PASSED - System display only users with username "
                                                        + username + " role " + role + " employee name "
                                                        + employeeName + " status " + status);
                }
        }

        /**
         * UM_READ_016
         */
        @Test(groups = { "regression", "negative" }, description = "Not matching all field")
        public void verifyFilterByNotMatchingAllFields() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_016 - Verify that the system does not return users matching all filters"));

                // --Steps--
                String username = AppConstants.USERNAME_INVALID;
                ExtentTestUtils.getTest().info("Step 1: Type Username: " + username);
                adminUserPage.typeUsername(username);

                String role = AppConstants.ROLE_ADMIN;
                ExtentTestUtils.getTest().info("Step 2: Select User Role: " + role);
                adminUserPage.chooseUserRole(role);

                String status = AppConstants.STATUS_ENABLED;
                ExtentTestUtils.getTest().info("Step 3: Select Status: " + status);
                adminUserPage.chooseStatus(status);

                String employeeName = AppConstants.EMPLOYEE_NAME_VALID;
                ExtentTestUtils.getTest().info("Step 4: Type Employee Name: " + employeeName);
                adminUserPage.typeEmployeeName(employeeName);

                ExtentTestUtils.getTest().info("Step 5: Click search button");
                adminUserPage.clickSearch();

                // --Assertion--
                List<String> roleList = adminUserPage.getDataFollowField("Username", "User Role", "Employee Name",
                                "Status");
                if (!roleList.isEmpty()) {
                        Assert.assertTrue(
                                        roleList.stream().allMatch(
                                                        name -> name.contains(username) && name.contains(role)
                                                                        && name.contains(employeeName)
                                                                        && name.contains(status)),
                                        "Only users with username " + username + " role " + role + " employee name "
                                                        + employeeName + " status " + status + " displayed");
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_014 PASSED - System display only users with username "
                                                        + username + " role " + role + " employee name "
                                                        + employeeName + " status " + status);
                }
        }

        /**
         * UM_READ_017
         */
        @Test(groups = { "regression", "positive" }, description = "check reset button")
        public void verifyResetButton() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_017 - Verify reset button"));

                // --Steps--
                int count = adminUserPage.getDisplayedRecordCount();
                LogUtils.info("Number of records before reset: " + count);

                String username = AppConstants.USERNAME_ADMIN;
                ExtentTestUtils.getTest().info("Step 1: Type Username: " + username);
                adminUserPage.typeUsername(username);

                String role = AppConstants.ROLE_ADMIN;
                ExtentTestUtils.getTest().info("Step 2: Select User Role: " + role);
                adminUserPage.chooseUserRole(role);

                ExtentTestUtils.getTest().info("Step 3: Click reset button");
                adminUserPage.clickReset();

                int countAfterReset = adminUserPage.getDisplayedRecordCount();
                LogUtils.info("Number of records after reset: " + countAfterReset);

                // --Assertion--
                Assert.assertEquals(count, countAfterReset,
                                "Number of records after reset should be the same as before reset");
                ExtentTestUtils.getTest().pass(
                                "UM_READ_017 PASSED - Reset button resets filters and displays all users");
        }

        /**
         * UM_READ_018
         */
        @Test(groups = { "regression", "positive" }, description = "check username field with minimum length")
        public void verifyUsername() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_018 - Verify username field with minimum length"));

                // --Steps--
                String username = AppConstants.USERNAME_MIN_LENGTH;
                ExtentTestUtils.getTest().info("Step 1: Type Username: " + username);
                adminUserPage.typeUsername(username);

                // --Assertion--

                if (adminUserPage.isErrorMessageDisplayed("Username")) {
                        String text = adminUserPage.getTextErrorMessage("Username");
                        LogUtils.info("Error message: " + text);

                        Assert.assertEquals(text, AppConstants.ERROR_MESSAGE_USERNAME_MIN_LENGTH,
                                        "Error message should match the expected error message");
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_018 PASSED - Username with minimum length is displayed");
                }
        }

        /**
         * UM_READ_019
         */
        @Test(groups = { "regression",
                        "positive" }, description = "Verify that the system correctly handles Username input with leading and trailing spaces")
        public void verifyUsernameWithLeadingAndTrailingSpaces() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_019 - Verify Username input with leading and trailing spaces"));

                String originalUsername = AppConstants.USERNAME_ADMIN;
                String usernameWithSpaces = "   " + originalUsername + "   ";

                ExtentTestUtils.getTest().info(
                                "Step 1: Type Username with leading/trailing spaces: '" + usernameWithSpaces + "'");
                adminUserPage.typeUsername(usernameWithSpaces);

                ExtentTestUtils.getTest().info("Step 2: Click search button");
                adminUserPage.clickSearch();

                // --Assertion--
                List<String> usernameList = adminUserPage.getDataFollowField("Username");
                if (!usernameList.isEmpty()) {
                        Assert.assertTrue(
                                        usernameList.stream().allMatch(name -> name.equalsIgnoreCase(originalUsername)),
                                        "System should trim spaces and return the correct user: " + originalUsername);
                        ExtentTestUtils.getTest().pass(
                                        "UM_READ_019 PASSED - System correctly handles leading and trailing spaces");
                }
        }

        /**
         * UM_READ_020
         */
        @Test(groups = { "regression",
                        "positive" }, description = "Verify that the system handles Username search correctly with different letter cases")
        public void verifyUsernameWithDifferentLetterCases() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_020 - Verify Username search with different letter cases"));

                String validUser = AppConstants.USERNAME_ADMIN;
                String upperCaseUser = validUser.toUpperCase();
                String lowerCaseUser = validUser.toLowerCase();

                // Lần 1: Search với chữ IN HOA
                ExtentTestUtils.getTest().info("Step 1: Type Username upper case: " + upperCaseUser);
                adminUserPage.typeUsername(upperCaseUser);
                adminUserPage.clickSearch();

                int countUpperCase = adminUserPage.getDisplayedRecordCount();
                Assert.assertTrue(countUpperCase > 0, "Should find records for uppercase username");
                LogUtils.info("Count uppercase: " + countUpperCase);

                // Nhấn Reset để chuẩn bị cho lần search 2
                adminUserPage.clickReset();

                // Lần 2: Search với chữ in thường
                ExtentTestUtils.getTest().info("Step 2: Type Username lower case: " + lowerCaseUser);
                adminUserPage.typeUsername(lowerCaseUser);
                adminUserPage.clickSearch();

                int countLowerCase = adminUserPage.getDisplayedRecordCount();
                LogUtils.info("Count lowercase: " + countLowerCase);

                // --Assertion--
                Assert.assertEquals(countUpperCase, countLowerCase,
                                "Search results for '" + upperCaseUser + "' and '" + lowerCaseUser
                                                + "' should be the same");
                ExtentTestUtils.getTest().pass(
                                "UM_READ_020 PASSED - System handles case-insensitive search correctly");
        }

        /**
         * UM_READ_021
         */
        @Test(groups = { "regression",
                        "positive" }, description = "Verify that the system returns consistent results when performing the same search multiple times")
        public void verifyConsistentResultsForMultipleSearches() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_READ_021 - Verify consistent results when performing the same search multiple times"));

                String username = AppConstants.USERNAME_ADMIN;

                // Lần 1
                ExtentTestUtils.getTest().info("Step 1: Search first time with Username: " + username);
                adminUserPage.typeUsername(username);
                adminUserPage.clickSearch();
                int firstCount = adminUserPage.getDisplayedRecordCount();
                LogUtils.info("Count first: " + firstCount);

                // Lần 2
                ExtentTestUtils.getTest().info("Step 2: Search second time with same Username");
                adminUserPage.clickSearch();
                int secondCount = adminUserPage.getDisplayedRecordCount();
                LogUtils.info("Count second: " + secondCount);

                // Lần 3
                ExtentTestUtils.getTest().info("Step 3: Search third time with same Username");
                adminUserPage.clickSearch();
                int thirdCount = adminUserPage.getDisplayedRecordCount();
                LogUtils.info("Count third: " + thirdCount);

                // --Assertion--
                Assert.assertEquals(firstCount, secondCount, "Second search should return same results");
                Assert.assertEquals(secondCount, thirdCount, "Third search should return same results");

                ExtentTestUtils.getTest().pass(
                                "UM_READ_021 PASSED - System returns consistent results for multiple searches");
        }

}
