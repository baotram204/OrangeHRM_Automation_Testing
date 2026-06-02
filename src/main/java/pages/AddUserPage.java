package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import utils.LogUtils;
import utils.WaitUtils;

public class AddUserPage extends AdminUserPage {

    // button
    private static By saveButton = By.xpath("//button[normalize-space()='Save']");
    private static By cancelButton = By.xpath("//button[normalize-space()='Cancel']");

    // input
    // usernaem, employee, status, role extends AdminUserPage

    // Password and Confirm Password
    private static By passwordInput = By
            .xpath("(//div[contains(@class,'oxd-input-group')]//input[@type='password'])[1]");
    private static By confirmPasswordInput = By
            .xpath("(//div[contains(@class,'oxd-input-group')]//input[@type='password'])[2]");

    // label error message
    private static By errorFieldMessage = By
            .xpath("//div[contains(@class,'oxd-input-group oxd-input-field-bottom-space')]//span");

    // private static String flatformErrorMessage = "//label[normalize-space()='User
    // Role'] /ancestor::div[contains(@class,'oxd-input-group')]//span"

    // action

    // button
    public void clickSave() {
        LogUtils.info("Click Save button");
        click(saveButton);
    }

    public void clickCancel() {
        LogUtils.info("Click Cancel button");
        click(cancelButton);
    }

    // field

    public void typeUsername(String username) {
        LogUtils.info("Type username: " + username);
        type(usernameInput, username);

        // TAB
        getElementsList(usernameInput).get(0).sendKeys(org.openqa.selenium.Keys.TAB);

        // Sleep 1000ms -- FIX TẠM THỜI
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void typePassword(String password) {
        LogUtils.info("Type password: " + password);
        type(passwordInput, password);
    }

    public void typeConfirmPassword(String confirmPassword) {
        LogUtils.info("Type confirm password: " + confirmPassword);
        type(confirmPasswordInput, confirmPassword);
    }

    public void chooseUserRole(String role) {
        LogUtils.info("Choose user role");
        selectDropdown(userRoleDropdown, role);
    }

    public void chooseStatus(String status) {
        LogUtils.info("Choose status");
        selectDropdown(statusDropdown, status);
    }

    public void typeEmployeeName(String name) {
        LogUtils.info("Type employee name: " + name);
        selectDropdownForEmployeeName(name);
    }

    /**
     * Verify Employee dropdown matching with text
     * 
     * @param name Employee name
     * @return true if all value in dropdown matching with text
     */
    public boolean verifyEmployeeDropdownOptions(String name) {
        List<WebElement> option = showDropdown(name);
        for (WebElement element : option) {
            String opt = element.getText().trim();
            if (!opt.toLowerCase().contains(name.toLowerCase())) {
                LogUtils.error("Employee dropdown option not matching with text: " + opt);
                return false;
            }
        }
        LogUtils.info("Employee dropdown option matching with text: " + name);
        return true;
    }

    /**
     * Check error message in password field
     * 
     * @return true if error message displayed
     */
    public boolean checkErrorMessage() {
        List<WebElement> errorMessages = getElementsList(errorFieldMessage);
        return errorMessages.size() > 0;
    }

    /**
     * Check error message in specific field in web like User Role, Username,...
     * 
     * @param field Field name
     * @return true if error message displayed
     */
    public boolean isErrorMessageDisplayed(String field) {
        By errorField = By.xpath(
                "//label[normalize-space()='" + field + "'] /ancestor::div[contains(@class,'oxd-input-group')]//span");
        return isDisplayed(errorField, 3);
    }

    /**
     * Get error message in specific field in web like User Role, Username,...
     * 
     * @param field Field name
     * @return Error message
     */
    public String getTextErrorMessage(String field) {
        By errorField = By.xpath(
                "//label[normalize-space()='" + field + "'] /ancestor::div[contains(@class,'oxd-input-group')]//span");

        if (isDisplayed(errorField, 3)) {
            return getText(errorField);
        }
        return "";
    }

}
