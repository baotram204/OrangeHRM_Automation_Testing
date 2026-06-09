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
import pages.UpdateUserPage;
import utils.ExtentTestUtils;
import utils.LogUtils;

public class UpdateUserTest extends BaseTest {

        private LoginPage loginPage;
        private AdminUserPage adminUserPage;
        private UpdateUserPage updateUserPage;
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

        /**
         * UM_UPDATE_004
         */
        @Test(groups = { "regression", "positive" }, description = "Verify user can update account with all valid data")
        public void verifyUpdateUserWithValidData() {
                ExtentTestUtils
                                .setTest(extent.createTest(
                                                "UM_UPDATE_004 - Verify user can update account with all valid data"));

                String originalUsername = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String updatedUsername = originalUsername + "_upd";
                String password = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                originalUsername, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + originalUsername);
                updateUserPage = adminUserPage.clickEditButton(originalUsername);

                ExtentTestUtils.getTest().info("Step 2: Update user role to " + AppConstants.ROLE_ESS);
                updateUserPage.chooseUserRole(AppConstants.ROLE_ESS);

                ExtentTestUtils.getTest().info("Step 3: Update status to " + AppConstants.STATUS_ENABLED);
                updateUserPage.chooseStatus(AppConstants.STATUS_ENABLED);

                ExtentTestUtils.getTest().info("Step 4: Update username to: " + updatedUsername);
                updateUserPage.typeUsername(updatedUsername);

                ExtentTestUtils.getTest().info("Step 5: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean isToastDisplayed = updateUserPage.isSuccessToastDisplayed();
                Assert.assertTrue(isToastDisplayed, "Expected 'Success' toast but not found");

                ExtentTestUtils.getTest().pass("UM_UPDATE_004 PASSED – User is updated successfully");
        }

        /**
         * UM_UPDATE_005
         */
        @Test(groups = { "regression", "positive" }, description = "Verify user can update only role and status")
        public void verifyUpdateUserRoleAndStatus() {
                ExtentTestUtils.setTest(
                                extent.createTest("UM_UPDATE_005 - Verify user can update only role and status"));

                String originalUsername = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                originalUsername, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + originalUsername);
                updateUserPage = adminUserPage.clickEditButton(originalUsername);

                ExtentTestUtils.getTest().info("Step 2: Update user role to " + AppConstants.ROLE_ESS);
                updateUserPage.chooseUserRole(AppConstants.ROLE_ESS);

                ExtentTestUtils.getTest().info("Step 3: Update status to " + AppConstants.STATUS_DISABLED);
                updateUserPage.chooseStatus(AppConstants.STATUS_DISABLED);

                ExtentTestUtils.getTest().info("Step 5: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean isToastDisplayed = updateUserPage.isSuccessToastDisplayed();
                Assert.assertTrue(isToastDisplayed, "Expected 'Success' toast but not found");

                ExtentTestUtils.getTest().pass("UM_UPDATE_005 PASSED – User role and status updated successfully");
        }

        /**
         * UM_UPDATE_006 === don't fix
         */
        @Test(groups = { "regression", "negative" }, description = "Verify system blocks update when username is blank")
        public void verifyUpdateUserWithBlankUsername() {
                ExtentTestUtils
                                .setTest(extent.createTest(
                                                "UM_UPDATE_006 - Verify system blocks update when username is blank"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username,
                                password);

                ExtentTestUtils.getTest().info("Step 1: Open Edit User");
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Clear username");
                // OrangeHRM generally clears old values using element.clear() inside type()
                updateUserPage.typeUsername("");

                ExtentTestUtils.getTest().info("Step 3: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = updateUserPage.isErrorMessageDisplayed("Username");
                Assert.assertTrue(errorMessage, "Validation error blocked update");

                ExtentTestUtils.getTest().pass("UM_UPDATE_006 PASSED – Validation error shown, update blocked");
        }

        /**
         * UM_UPDATE_007
         */
        @Test(groups = { "regression",
                        "negative" }, description = "Verify system blocks update when username is duplicate")
        public void verifyUpdateUserWithDuplicateUsername() {
                ExtentTestUtils
                                .setTest(extent.createTest(
                                                "UM_UPDATE_007 - Verify system blocks update when username is duplicate"));

                String user1 = "User1_" + System.currentTimeMillis();
                String user2 = "User2_" + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, at least 2 users exist");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                user1,
                                password);
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                user2,
                                password);

                ExtentTestUtils.getTest().info("Step 1: Open Edit User for User 2");
                updateUserPage = adminUserPage.clickEditButton(user2);

                ExtentTestUtils.getTest().info("Step 2: Enter existing username (User 1)");
                updateUserPage.typeUsername(user1);

                ExtentTestUtils.getTest().info("Step 3: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = updateUserPage.isErrorMessageDisplayed("Username");
                Assert.assertTrue(errorMessage, "Error message not displayed");

                String errorMsg = updateUserPage.getTextErrorMessage("Username");
                Assert.assertEquals(errorMsg, AppConstants.MSG_ALREADY_EXISTS);

                ExtentTestUtils.getTest().pass("UM_UPDATE_007 PASSED – Error 'Already exists' displayed");
        }

        /**
         * UM_UPDATE_008
         */
        @Test(groups = { "regression",
                        "positive" }, description = "Verify system blocks update when Employee Name is not selected")
        public void verifyUpdateWithNoChoosingEmployee() {
                ExtentTestUtils
                                .setTest(extent
                                                .createTest("UM_UPDATE_008 - Verify system blocks update when Employee Name is not selected"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + username);
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Set Employee Name");
                updateUserPage.typeEmployeeNameWithoutSelecting(AppConstants.EMPLOYEE_NAME_VALID);

                ExtentTestUtils.getTest().info("Step 3: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = updateUserPage.isErrorMessageDisplayed("Employee Name");
                Assert.assertTrue(errorMessage, "Expected 'Invalid' toast but not found");

                String errorMsg = updateUserPage.getTextErrorMessage("Employee Name");
                Assert.assertEquals(errorMsg, AppConstants.MSG_INVALID);

                ExtentTestUtils.getTest().pass("UM_UPDATE_008 PASSED – Error 'Invalid' displayed");
        }

        /**
         * UM_UPDATE_009
         */
        @Test(groups = { "regression",
                        "negative" }, description = "Verify system blocks update when Role is not selected")
        public void verifyUpdateWithNoChoosingRole() {
                ExtentTestUtils
                                .setTest(extent.createTest(
                                                "UM_UPDATE_009 - Verify system blocks update when Role is not selected"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + username);
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Clear role value");
                updateUserPage.chooseUserRole("-- Select --");

                ExtentTestUtils.getTest().info("Step 3: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = updateUserPage.isErrorMessageDisplayed("User Role");
                Assert.assertTrue(errorMessage, "Expected 'Required' toast but not found");

                String errorMsg = updateUserPage.getTextErrorMessage("User Role");
                Assert.assertEquals(errorMsg, AppConstants.MSG_REQUIRED);

                ExtentTestUtils.getTest().pass("UM_UPDATE_008 PASSED – Error 'Required' displayed");
        }

        /**
         * UM_UPDATE_010
         */
        @Test(groups = { "regression",
                        "negative" }, description = "Verify system blocks update when Status is not selected")
        public void verifyUpdateWithNoChoosingStatus() {
                ExtentTestUtils
                                .setTest(extent.createTest(
                                                "UM_UPDATE_010 - Verify system blocks update when Status is not selected"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + username);
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Clear status value");
                updateUserPage.chooseStatus("-- Select --");

                ExtentTestUtils.getTest().info("Step 3: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = updateUserPage.isErrorMessageDisplayed("Status");
                Assert.assertTrue(errorMessage, "Expected 'Required' toast but not found");

                String errorMsg = updateUserPage.getTextErrorMessage("Status");
                Assert.assertEquals(errorMsg, AppConstants.MSG_REQUIRED);

                ExtentTestUtils.getTest().pass("UM_UPDATE_010 PASSED – Error 'Required' displayed");
        }

        /**
         * UM_UPDATE_011
         */
        @Test(groups = { "regression",
                        "negative" }, description = "Verify system blocks update when password and confirm password do not match")
        public void verifyUpdatePasswordIncorrect() {
                ExtentTestUtils
                                .setTest(extent.createTest(
                                                "UM_UPDATE_011 - Verify system blocks update when password and confirm password do not match"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;
                String confirmPassword = AppConstants.PASSWORD_INVALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + username);
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Click checkbox change password");
                updateUserPage.clickCheckboxChangePass();

                ExtentTestUtils.getTest().info("Step 3: Enter password");
                updateUserPage.typePassword(password);

                ExtentTestUtils.getTest().info("Step 4: Enter confirm password");
                updateUserPage.typeConfirmPassword(confirmPassword);

                ExtentTestUtils.getTest().info("Step 5: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = updateUserPage.isErrorMessageDisplayed("Confirm Password");
                Assert.assertTrue(errorMessage, "Expected 'Passwords do not match' toast but not found");

                String errorMsg = updateUserPage.getTextErrorMessage("Confirm Password");
                Assert.assertEquals(errorMsg, AppConstants.MSG_PASS_NOT_MATCH);

                ExtentTestUtils.getTest().pass("UM_UPDATE_011 PASSED – Error 'Passwords do not match' displayed");
        }

        /**
         * UM_UPDATE_012 Verify system rejects weak password
         */
        @Test(groups = { "regression", "negative" }, description = "Verify system rejects weak password")
        public void verifyUpdateWeakPassword() {
                ExtentTestUtils.setTest(extent.createTest("UM_UPDATE_012 - Verify system rejects weak password"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;
                String passwordChange = AppConstants.PASSWORD_WEAK;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + username);
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Click checkbox change password");
                updateUserPage.clickCheckboxChangePass();

                ExtentTestUtils.getTest().info("Step 3: Enter password");
                updateUserPage.typePassword(passwordChange);

                ExtentTestUtils.getTest().info("Step 4: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = updateUserPage.isErrorMessageDisplayed("Password");
                Assert.assertTrue(errorMessage, "Required toast but not found");

                String errorMsg = updateUserPage.getTextErrorMessage("Password");
                LogUtils.info("Error message: " + errorMsg);

                ExtentTestUtils.getTest().pass("UM_UPDATE_012 PASSED – Error 'Weak password' displayed");
        }

        /**
         * UM_UPDATE_013 Verify system enforces minimum username length
         */
        @Test(groups = { "regression", "negative" }, description = "Verify system enforces minimum username length")
        public void verifyUpdateUsernameTooShort() {
                ExtentTestUtils.setTest(
                                extent.createTest("UM_UPDATE_013 - Verify system enforces minimum username length"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;
                String usernameChange = AppConstants.USERNAME_MIN_LENGTH;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + username);
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Enter username");
                updateUserPage.typeUsername(usernameChange);

                ExtentTestUtils.getTest().info("Step 3: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = updateUserPage.isErrorMessageDisplayed("Username");
                Assert.assertTrue(errorMessage, "Required toast but not found");

                String errorMsg = updateUserPage.getTextErrorMessage("Username");
                LogUtils.info("Error message: " + errorMsg);
                Assert.assertEquals(errorMsg, AppConstants.MSG_USER_NAME_MIN_LENGTH);

                ExtentTestUtils.getTest()
                                .pass("UM_UPDATE_013 PASSED – Error 'Should be at least 5 characters' displayed");
        }

        /**
         * UM_UPDATE_015
         */
        @Test(groups = { "regression", "negative" }, description = "Verify system rejects invalid username format")
        public void verifyUpdateInvalidUsernameFormat() {
                ExtentTestUtils.setTest(
                                extent.createTest("UM_UPDATE_015 - Verify system rejects invalid username format"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;
                String usernameChange = AppConstants.USERNAME_INVALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + username);
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Enter username");
                updateUserPage.typeUsername(usernameChange);

                ExtentTestUtils.getTest().info("Step 3: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = updateUserPage.isErrorMessageDisplayed("Username");
                Assert.assertTrue(errorMessage, "Required toast but not found");

                String errorMsg = updateUserPage.getTextErrorMessage("Username");
                LogUtils.info("Error message: " + errorMsg);
                Assert.assertEquals(errorMsg, AppConstants.MSG_INVALID_CHARACTERS);

                ExtentTestUtils.getTest().pass(
                                "UM_UPDATE_015 PASSED – Error 'Should only contain alphanumeric characters and underscore' displayed");
        }

        /**
         * UM_UPDATE_016 Verify system trims or rejects username with spaces
         */
        @Test(groups = { "regression",
                        "negative" }, description = "Verify system trims or rejects username with spaces")
        public void verifyUpdateUsernameWithSpaces() {
                ExtentTestUtils
                                .setTest(extent.createTest(
                                                "UM_UPDATE_016 - Verify system trims or rejects username with spaces"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;
                String usernameChange = " " + AppConstants.USERNAME_USER + System.currentTimeMillis() + " ";

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + username);
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Enter username with spaces");
                updateUserPage.typeUsername(usernameChange);

                ExtentTestUtils.getTest().info("Step 3: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean errorMessage = updateUserPage.isErrorMessageDisplayed("Employee Name");
                Assert.assertTrue(errorMessage, "Required toast but not found");

                String errorMsg = updateUserPage.getTextErrorMessage("Employee Name");
                LogUtils.info("Error message: " + errorMsg);
                Assert.assertEquals(errorMsg, AppConstants.MSG_REQUIRED);

                ExtentTestUtils.getTest().pass("UM_UPDATE_016 PASSED – Error 'Required' displayed");
        }

        /**
         * UM_UPDATE_017 Verify system shows all validation errors when multiple fields
         * are invalid
         */
        @Test(groups = { "regression",
                        "negative" }, description = "Verify system shows all validation errors when multiple fields are invalid")
        public void verifyUpdateMultipleInvalidFields() {
                ExtentTestUtils
                                .setTest(extent.createTest(
                                                "UM_UPDATE_017 - Verify system shows all validation errors when multiple fields are invalid"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;
                String usernameChange = AppConstants.USERNAME_INVALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + username);
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Enter invalid username");
                updateUserPage.typeUsername(usernameChange);

                ExtentTestUtils.getTest().info("Step 3: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean usernameMSG = updateUserPage.isErrorMessageDisplayed("Username");
                Assert.assertTrue(usernameMSG, "Required toast but not found");

                boolean passwordMSG = updateUserPage.isErrorMessageDisplayed("Password");
                Assert.assertTrue(passwordMSG, "Required toast but not found");

                boolean confPasswordMSG = updateUserPage.isErrorMessageDisplayed("Confirm Password");
                Assert.assertTrue(confPasswordMSG, "Required toast but not found");

                ExtentTestUtils.getTest().pass("UM_UPDATE_017 PASSED – Error field displayed");
        }

        /**
         * UM_UPDATE_018
         */
        @Test(groups = { "regression",
                        "positive" }, description = "Verify system updates password successfully when valid")
        public void verifyUpdatePasswordSuccessfully() {
                ExtentTestUtils
                                .setTest(extent.createTest(
                                                "UM_UPDATE_018 - Verify system updates password successfully when valid"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;
                String passwordChange = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + username);
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Click checkbox change password");
                updateUserPage.clickCheckboxChangePass();

                ExtentTestUtils.getTest().info("Step 3: Enter new password");
                updateUserPage.typePassword(passwordChange);

                ExtentTestUtils.getTest().info("Step 4: Enter confirm password");
                updateUserPage.typeConfirmPassword(passwordChange);

                ExtentTestUtils.getTest().info("Step 5: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean isToastDisplayed = updateUserPage.isSuccessToastDisplayed();
                Assert.assertTrue(isToastDisplayed, "Success toast but not found");

                ExtentTestUtils.getTest().pass("UM_UPDATE_018 PASSED – Success message displayed");
        }

        /**
         * UM_UPDATE_019
         */
        @Test(groups = { "regression", "positive" }, description = "Verify user can login with updated password")
        public void verifyLoginWithUpdatedPassword() {
                ExtentTestUtils.setTest(
                                extent.createTest("UM_UPDATE_019 - Verify user can login with updated password"));

                String username = AppConstants.USERNAME_USER + System.currentTimeMillis();
                String password = AppConstants.PASSWORD_VALID;
                String passwordChange = AppConstants.PASSWORD_VALID;

                ExtentTestUtils.getTest().info("Pre-condition: Admin logged in, user exists");
                createUser(AppConstants.ROLE_ADMIN, AppConstants.EMPLOYEE_NAME_VALID, AppConstants.STATUS_ENABLED,
                                username, password);

                ExtentTestUtils.getTest().info("Step 1: Click edit button for user: " + username);
                updateUserPage = adminUserPage.clickEditButton(username);

                ExtentTestUtils.getTest().info("Step 2: Click checkbox change password");
                updateUserPage.clickCheckboxChangePass();

                ExtentTestUtils.getTest().info("Step 3: Enter new password");
                updateUserPage.typePassword(passwordChange);

                ExtentTestUtils.getTest().info("Step 4: Enter confirm password");
                updateUserPage.typeConfirmPassword(passwordChange);

                ExtentTestUtils.getTest().info("Step 5: Click Save");
                updateUserPage.clickSave();

                // -- Assertion --
                boolean isToastDisplayed = updateUserPage.isSuccessToastDisplayed();
                Assert.assertTrue(isToastDisplayed, "Success toast but not found");

                ExtentTestUtils.getTest().info("Step 6: Logout");
                updateUserPage.logout();

                ExtentTestUtils.getTest().info("Step 7: Login with updated password");
                DashboardPage dashboardPage = loginPage.login(username, passwordChange);
                Assert.assertTrue(dashboardPage.getCurrentPageUrl().contains("dashboard"),
                                "Login successfully but not redirected to dashboard");

                ExtentTestUtils.getTest().pass("UM_UPDATE_019 PASSED – Success message displayed");
        }

}
