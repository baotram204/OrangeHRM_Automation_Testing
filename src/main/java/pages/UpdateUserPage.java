package pages;

import org.openqa.selenium.By;

import utils.LogUtils;

public class UpdateUserPage extends AddUserPage {

    private static final By checkboxChangePass = By.xpath("//div[contains(@class,'oxd-checkbox-wrapper')]//span[contains(@class,'oxd-checkbox-input')]");

    // Action
    public void clickCheckboxChangePass() {
        LogUtils.info("Click checkbox change password");
        click(checkboxChangePass);
    }

}
