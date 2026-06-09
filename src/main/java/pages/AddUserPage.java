package pages;

import org.openqa.selenium.By;
import utils.LogUtils;

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

    public void typePassword(String password) {
        LogUtils.info("Type password: " + password);
        type(passwordInput, password);
    }

    public void typeConfirmPassword(String confirmPassword) {
        LogUtils.info("Type confirm password: " + confirmPassword);
        type(confirmPasswordInput, confirmPassword);
    }

}
