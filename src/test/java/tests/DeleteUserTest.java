package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import config.ConfigReader;
import constants.AppConstants;
import pages.AddUserPage;
import pages.AdminUserPage;
import pages.DashboardPage;
import pages.LoginPage;
import utils.ExtentTestUtils;
import utils.LogUtils;

public class DeleteUserTest extends BaseTest {
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
        public void createUser(String userRole, String employeeName, String status, String username,
                        String password) {

                addUserPage = new AddUserPage();
                addUserPage = adminUserPage.clickAddUser();

                // -- Steps --
                addUserPage.chooseUserRole(userRole);

                addUserPage.typeEmployeeName(employeeName);
                addUserPage.chooseStatus(status);

                addUserPage.typeUsername(username);
                addUserPage.typePassword(password);
                addUserPage.typeConfirmPassword(password);

                addUserPage.clickSave();

                boolean saved = addUserPage.isSuccessToastDisplayed();
                Assert.assertTrue(saved);
        }

        @Test(groups = { "regession",
                        "positive" }, description = "Verify that system cancels deletion when user clicks Cancel in confirmation dialog")
        public void verifyDeleteUserWithCancelButton() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_DELETE_001 - Verify that system cancels deletion when user clicks Cancel in confirmation dialog"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ESS, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, AppConstants.PASSWORD_VALID);

                ExtentTestUtils.getTest().info("Step 1: Click delete button for user: " + username);
                adminUserPage.clickDeleteButton(username);

                ExtentTestUtils.getTest().info("Step 2: Click No in confirmation dialog");
                adminUserPage.clickNoConfirmDelete();

                boolean isUserPresent = adminUserPage.isUserInList(username);
                Assert.assertTrue(isUserPresent, "User should remain in the list after cancelling deletion");

                ExtentTestUtils.getTest().pass("UM_DELETE_001 PASSED – User deletion cancelled successfully");
        }

        @Test(groups = { "regession",
                        "positive" }, description = "Verify that system deletes one selected user successfully")
        public void verifyDeleteUserWithYesButton() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_DELETE_002 - Verify that system deletes one selected user successfully"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ESS, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, AppConstants.PASSWORD_VALID);

                ExtentTestUtils.getTest().info("Step 1: Click delete button for user: " + username);
                adminUserPage.clickDeleteButton(username);

                ExtentTestUtils.getTest().info("Step 2: Click Yes in confirmation dialog");
                adminUserPage.clickYesConfirmDelete();

                boolean deleted = adminUserPage.isSuccessToastDisplayed();
                Assert.assertTrue(deleted, "Success toast should be displayed");

                boolean isUserPresent = adminUserPage.isUserInList(username);
                Assert.assertFalse(isUserPresent, "User should be removed from the list");

                ExtentTestUtils.getTest().pass("UM_DELETE_002 PASSED – User deleted successfully");
        }

        @Test(groups = { "regession",
                        "positive" }, description = "Verify that system deletes multiple selected users successfully")
        public void verifyDeleteMultipleUsers() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_DELETE_003 - Verify that system deletes multiple selected users successfully"));

                long time = System.currentTimeMillis();
                String username1 = AppConstants.USERNAME_USER + time + "_1";
                String username2 = AppConstants.USERNAME_USER + time + "_2";

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, users exist");
                createUser(AppConstants.ROLE_ESS, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username1, AppConstants.PASSWORD_VALID);
                createUser(AppConstants.ROLE_ESS, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username2, AppConstants.PASSWORD_VALID);

                ExtentTestUtils.getTest().info("Step 1: Click reset button");
                adminUserPage.clickReset();

                ExtentTestUtils.getTest().info("Step 2: Type username");
                adminUserPage.typeUsername(AppConstants.USERNAME_USER);

                ExtentTestUtils.getTest().info("Step 3: Click search button");
                adminUserPage.clickSearch();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                }

                ExtentTestUtils.getTest().info("Step 4: Click checkbox for user 1");
                adminUserPage.clickCheckboxForUser(username1);

                ExtentTestUtils.getTest().info("Step 5: Click checkbox for user 2");
                adminUserPage.clickCheckboxForUser(username2);

                ExtentTestUtils.getTest().info("Step 6: Click delete selected button");
                adminUserPage.clickDeleteSelectedButton();
                adminUserPage.clickYesConfirmDelete();

                boolean deleted = adminUserPage.isSuccessToastDisplayed();
                Assert.assertTrue(deleted, "Success toast should be displayed");

                Assert.assertFalse(adminUserPage.isUserInList(username1), "User 1 should be deleted");
                Assert.assertFalse(adminUserPage.isUserInList(username2), "User 2 should be deleted");

                ExtentTestUtils.getTest().pass("UM_DELETE_003 PASSED – Multiple users deleted successfully");
        }

        @Test(groups = { "regession",
                        "negative" }, description = "Verify that ESS user cannot access delete function")
        public void verifyDeleteFunctionForESSUser() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_DELETE_004 - Verify that ESS user cannot access delete function"));

                long time = System.currentTimeMillis();
                String username = AppConstants.USERNAME_USER + time;
                String password = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ESS, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click logout button");
                adminUserPage.logout();

                DashboardPage dashboardPage = loginPage.login(username, password);

                Assert.assertFalse(dashboardPage.isAdminMenuDisplayed(),
                                "Admin menu should not be displayed for ESS user");

                ExtentTestUtils.getTest().pass("UM_DELETE_004 PASSED – ESS user cannot access delete function");
        }

        @Test(groups = { "regession",
                        "positive" }, description = "Verify delete using Select All on current page")
        public void verifyDeleteUsingSelectAll() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_DELETE_005 - Verify delete using Select All on current page"));

                ExtentTestUtils.getTest().info("Step 1: Click select all checkbox");
                adminUserPage.clickSelectAllCheckbox();

                ExtentTestUtils.getTest().info("Step 2: Click delete selected button");
                adminUserPage.clickDeleteSelectedButton();

                ExtentTestUtils.getTest().info("Step 3: Click yes in confirmation dialog");
                adminUserPage.clickYesConfirmDelete();

                boolean deleted = adminUserPage.isSuccessToastDisplayed();
                Assert.assertTrue(deleted, "Success toast should be displayed");

                ExtentTestUtils.getTest().pass("UM_DELETE_005 PASSED – Multiple users deleted successfully");
        }

        @Test(groups = { "regession",
                        "negative" }, description = "Verify system does not delete user when closing confirmation dialog")
        public void verifyDeleteFunctionWhenClosingDialog() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_DELETE_006 - Verify system does not delete user when closing confirmation dialog"));

                long time = System.currentTimeMillis();
                String username = AppConstants.USERNAME_USER + time;
                String password = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ESS, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click delete button");
                adminUserPage.clickDeleteButton(username);

                ExtentTestUtils.getTest().info("Step 2: Close confirmation dialog");
                adminUserPage.closePopupDeleteDialog();

                boolean isUserPresent = adminUserPage.isUserInList(username);
                Assert.assertTrue(isUserPresent, "User should remain in the list after closing dialog");

                ExtentTestUtils.getTest()
                                .pass("UM_DELETE_006 PASSED – System does not delete user when closing dialog");
        }

        @Test(groups = { "regression",
                        "negative" }, description = "Verify that deleted user is removed from search results")
        public void verifyDeletedUserIsRemovedFromSearchResults() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_DELETE_007 - Verify that deleted user is removed from search results"));

                long time = System.currentTimeMillis();
                String username = AppConstants.USERNAME_USER + time;
                String password = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ESS, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                adminUserPage.clickReset();

                ExtentTestUtils.getTest().info("Step 1: Type username");
                adminUserPage.typeUsername(username);

                ExtentTestUtils.getTest().info("Step 2: Click search button");
                adminUserPage.clickSearch();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                }

                ExtentTestUtils.getTest().info("Step 3: Click delete button for user");
                adminUserPage.clickDeleteButton(username);

                ExtentTestUtils.getTest().info("Step 4: Click yes confirm delete");
                adminUserPage.clickYesConfirmDelete();
                adminUserPage.isSuccessToastDisplayed();

                boolean isUserPresent = adminUserPage.isUserInList(username);
                Assert.assertFalse(isUserPresent, "User should not appear in search results");

                ExtentTestUtils.getTest().pass("UM_DELETE_007 PASSED – Deleted user removed from search results");
        }

        @Test(groups = { "regression",
                        "negative" }, description = "Verify that deleted user cannot login")
        public void verifyDeletedUserCannotLogin() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_DELETE_008 - Verify that deleted user cannot login"));

                long time = System.currentTimeMillis();
                String username = AppConstants.USERNAME_USER + time;
                String password = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ESS, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click delete button for user: " + username);
                adminUserPage.clickDeleteButton(username);

                ExtentTestUtils.getTest().info("Step 2: Click yes confirm delete");
                adminUserPage.clickYesConfirmDelete();

                ExtentTestUtils.getTest().info("Step 3: Verify success toast displayed");
                adminUserPage.isSuccessToastDisplayed();

                ExtentTestUtils.getTest().info("Step 4: Logout");
                adminUserPage.logout();

                ExtentTestUtils.getTest().info("Step 5: Login with deleted user credentials");
                loginPage.login(username, password);

                boolean loginFailed = loginPage.isErrorDisplayed();
                Assert.assertTrue(loginFailed, "Deleted user should not be able to login");

                ExtentTestUtils.getTest().pass("UM_DELETE_008 PASSED – Deleted user cannot login");
        }

        @Test(groups = { "regression",
                        "negative" }, description = "Verify system prevents deleting own account")
        public void verifySystemPreventsDeletingOwnAccount() {
                ExtentTestUtils.setTest(extent.createTest(
                                "UM_DELETE_009 - Verify system prevents deleting own account"));

                String adminUsername = System.getProperty("admin.username", ConfigReader.getProperty("username"));

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in");

                ExtentTestUtils.getTest().info("Step 1: Click reset button");
                adminUserPage.clickReset();

                ExtentTestUtils.getTest().info("Step 2: Type admin username");
                adminUserPage.typeUsername(adminUsername);

                ExtentTestUtils.getTest().info("Step 3: Click search button");
                adminUserPage.clickSearch();
                try {
                        Thread.sleep(2000);
                } catch (InterruptedException e) {
                }

                ExtentTestUtils.getTest().info("Step 4: Click delete button for admin user");
                adminUserPage.clickDeleteButton(adminUsername);

                boolean errorDisplayed = adminUserPage.isErrorMessageDisplayed();
                Assert.assertTrue(errorDisplayed, "Error message should be displayed");

                ExtentTestUtils.getTest().pass("UM_DELETE_009 PASSED – System prevents deleting own account");
        }

}
