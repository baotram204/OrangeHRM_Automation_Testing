package pages;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import utils.LogUtils;
import utils.WaitUtils;

public class AdminUserPage extends BasePage {

        // button
        private static By addUserButton = By.xpath("//button[normalize-space()='Add']");
        private static By resetButton = By.xpath("//button[normalize-space()='Reset']");
        private static By searchButton = By.xpath("//button[normalize-space()='Search']");

        // input
        protected static By usernameInput = By
                        .xpath("//label[normalize-space()='Username'] /ancestor::div[contains(@class,'oxd-input-group')] //input");
        protected static By employeeNameInput = By.xpath(
                        "//label[normalize-space()='Employee Name'] /ancestor::div[contains(@class,'oxd-input-group')] //input");

        // User role
        protected static By userRoleDropdown = By.xpath(
                        "//label[normalize-space()='User Role'] /ancestor::div[contains(@class,'oxd-input-group')] //div[contains(@class,'oxd-select-wrapper')]");

        // Status
        protected static By statusDropdown = By.xpath(
                        "//label[normalize-space()='Status'] /ancestor::div[contains(@class,'oxd-input-group')] //div[contains(@class,'oxd-select-wrapper')]");

        // toast
        private static By successToast = By
                        .xpath("//p[text()='Successfully Saved' or contains(text(), 'Successfully Saved')]");

        public AddUserPage clickAddUser() {

                LogUtils.info("Clicked Add User button");
                click(addUserButton);

                return new AddUserPage();

        }

        // HELPER =========================

        /**
         * Chọn một tùy chọn trong dropdown.
         *
         * @param dropdownLocator locator của phần tử dropdown
         * @param optionText      văn bản của tùy chọn muốn chọn
         */
        public void selectDropdown(By dropdownLocator, String optionText) {
                // 1. Click để mở dropdown
                click(dropdownLocator);

                // 2. Đợi danh sách các option xuất hiện
                By optionsLocator = By.xpath(
                                "//div[contains(@class,'oxd-select-wrapper')]//div[contains(@class,'oxd-select-option')]");

                // TÌM CÁCH GET ELEMENT THAY VÌ DÙNG STATIC
                List<WebElement> options = getElementsList(optionsLocator);

                // 3. Tìm option có text khớp
                for (WebElement opt : options) {
                        if (opt.getText().trim().equalsIgnoreCase(optionText.trim())) {
                                // 4. Click vào option đúng
                                opt.click();
                                LogUtils.info(String.format("Đã chọn '%s' trong dropdown", optionText));
                                return;
                        }
                }

                // Nếu không tìm thấy, ghi log và ném ngoại lệ để test biết lỗi
                String err = String.format("Không tìm thấy tùy chọn '%s' trong dropdown", optionText);
                LogUtils.error(err);
                throw new NoSuchElementException(err);
        }

        public void selectDropdownForEmployeeName(String name) {
                List<WebElement> options = showDropdown(name);

                for (WebElement opt : options) {
                        String optionText = opt.getText().trim();
                        if (optionText.toLowerCase().contains(name.toLowerCase())) {
                                opt.click();
                                LogUtils.info(String.format("Đã chọn nhân viên '%s'", optionText));
                                return;
                        }
                }

                String err = String.format("Không tìm thấy nhân viên nào khớp với '%s'", name);
                LogUtils.error(err);
                throw new NoSuchElementException(err);
        }

        public List<WebElement> showDropdown(String name) {
                // 1. Nhập tên vào trường Employee Name
                type(employeeNameInput, name);

                // 2. Chờ cho chữ "Searching...." biến mất (nếu có xuất hiện)
                By searchingLocator = By
                                .xpath("//div[contains(@class,'oxd-autocomplete-option') and contains(.,'Searching')]");
                waitForInvisibility(searchingLocator);

                // 3. Đợi danh sách kết quả hiển thị
                By optionsLocator = By.xpath("//div[contains(@class,'oxd-autocomplete-option')]");
                List<WebElement> options = getElementsList(optionsLocator);
                return options;
        }

        public boolean isSuccessToastDisplayed() {
                return isDisplayed(successToast);
        }

}