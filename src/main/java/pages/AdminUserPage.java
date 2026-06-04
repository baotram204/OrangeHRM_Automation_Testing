package pages;

import java.util.Collections;
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

        // label error message
        protected static By errorFieldMessage = By
                        .xpath("//div[contains(@class,'oxd-input-group oxd-input-field-bottom-space')]//span");

        // Status
        protected static By statusDropdown = By.xpath(
                        "//label[normalize-space()='Status'] /ancestor::div[contains(@class,'oxd-input-group')] //div[contains(@class,'oxd-select-wrapper')]");

        // toast
        private static By successToast = By
                        .xpath("//p[text()='Successfully Saved' or contains(text(), 'Successfully Saved')]");
        private static By infoToast = By
                        .xpath("//p[text()='No Records Found' or contains(text(), 'No Records Found')]");

        // user table
        private static By userTable = By.className("oxd-table");
        private static By rowsTable = By.xpath("//div[@class='oxd-table-card']");
        private static By columnHeaders = By.xpath("//div[@role='columnheader']");

        // text
        private static By textTotalRecord = By.xpath(
                        "//div[@class='orangehrm-horizontal-padding orangehrm-vertical-padding']");

        // Button Action =========================================================

        public AddUserPage clickAddUser() {

                LogUtils.info("Clicked Add User button");
                click(addUserButton);

                return new AddUserPage();

        }

        public void clickSearch() {
                LogUtils.info("Clicked Search button");
                click(searchButton);
        }

        public AdminUserPage clickReset() {
                LogUtils.info("Clicked Reset button");
                click(resetButton);
                return new AdminUserPage();
        }

        // Field Action =============================================================
        public void typeUsername(String username) {
                LogUtils.info("Type username: " + username);
                type(usernameInput, username);

                // Sleep 1000ms -- FIX TẠM THỜI
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                }
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
         * Type employee name and don't wait to choose option in dropdown
         * 
         * @param name Employee name
         */
        public void typeEmployeeNameWithoutSelecting(String name) {
                LogUtils.info("Type employee name: " + name);
                type(employeeNameInput, name);
        }

        // Verify ===================================================

        /**
         * Get data from one or multiple columns.
         * For multiple columns, values in the same row are concatenated by a space.
         * 
         * @param fields column names
         * @return list of concatenated data for each row
         */
        public List<String> getDataFollowField(String... fields) {
                if (fields == null || fields.length == 0) {
                        throw new IllegalArgumentException("Please provide at least one field");
                }

                if (isInfoToastDisplayed()) {
                        LogUtils.info("No results found");
                        return Collections.emptyList();
                }

                List<WebElement> headers = getElementsList(columnHeaders);
                List<List<String>> columnsData = new java.util.ArrayList<>();

                for (String field : fields) {
                        int columnHeaderIndex = 0;
                        for (int i = 0; i < headers.size(); i++) {
                                if (headers.get(i).getText().trim().equals(field)) {
                                        columnHeaderIndex = i + 1;
                                        break;
                                }
                        }

                        if (columnHeaderIndex > 0) {
                                List<WebElement> values = getElementsList(By
                                                .xpath("//div[@class='oxd-table-card']//div[@role='cell']["
                                                                + columnHeaderIndex + "]"));
                                List<String> data = values.stream().map(WebElement::getText).toList();
                                columnsData.add(data);
                        } else {
                                columnsData.add(Collections.emptyList());
                                LogUtils.error("Column not found: " + field);
                        }
                }

                List<String> rowDataList = new java.util.ArrayList<>();
                if (columnsData.isEmpty() || columnsData.get(0).isEmpty()) {
                        return rowDataList;
                }

                int rowCount = columnsData.get(0).size();
                for (int i = 0; i < rowCount; i++) {
                        StringBuilder rowBuilder = new StringBuilder();
                        for (List<String> colData : columnsData) {
                                String val = (i < colData.size()) ? colData.get(i) : "";
                                rowBuilder.append(val).append(" ");
                        }
                        rowDataList.add(rowBuilder.toString().trim());
                }

                LogUtils.info("Data for fields " + java.util.Arrays.toString(fields) + ": " + rowDataList);
                return rowDataList;
        }

        /**
         * Get total row in table
         * 
         * @return int
         */
        public int getTableRowCount() {
                List<WebElement> rows = getElementsList(rowsTable);
                return rows.size();
        }

        /**
         * Get total records displayed.
         *
         * Examples:
         * - "Total Records: 5" -> 5
         * - "No Records Found" -> 0
         *
         * @return total records
         */
        public int getDisplayedRecordCount() {
                String text = getText(textTotalRecord);

                if (text == null || text.isBlank()
                                || text.contains("No Records Found")) {
                        return 0;
                }

                String numberOnly = text.replaceAll("[^0-9]", "");

                try {
                        return Integer.parseInt(numberOnly);
                } catch (NumberFormatException e) {
                        LogUtils.error("Could not parse total records: " + text);
                        return 0;
                }
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
         * Check success toast display
         * 
         * @return true if success toast display
         */
        public boolean isSuccessToastDisplayed() {
                return isDisplayed(successToast, 3);
        }

        /**
         * Check info toast display
         * 
         * @return true if info toast display
         */
        public boolean isInfoToastDisplayed() {
                return isDisplayed(infoToast, 3);
        }

        /**
         * Check user table display
         * 
         * @return true if user table display
         */
        public boolean isUserTableDisplayed() {
                return isDisplayed(userTable, 3);
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
                                "//label[normalize-space()='" + field
                                                + "'] /ancestor::div[contains(@class,'oxd-input-group')]//span");
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
                                "//label[normalize-space()='" + field
                                                + "'] /ancestor::div[contains(@class,'oxd-input-group')]//span");

                if (isDisplayed(errorField, 3)) {
                        return getText(errorField);
                }
                return "";
        }

        // HELPER ==================================================

        /**
         * 
         * 
         */

        /**
         * Choose option in dropdown
         *
         * @param dropdownLocator locator of dropdown
         * @param optionText      text of option
         */
        public void selectDropdown(By dropdownLocator, String optionText) {
                // 1. Click to open dropdown
                click(dropdownLocator);

                // 2. Wait for option list appear
                By optionsLocator = By.xpath(
                                "//div[contains(@class,'oxd-select-wrapper')]//div[contains(@class,'oxd-select-option')]");

                List<WebElement> options = getElementsList(optionsLocator);

                // 3. Find option with matching text
                for (WebElement opt : options) {
                        if (opt.getText().trim().equalsIgnoreCase(optionText.trim())) {
                                // 4. Click the correct option
                                opt.click();
                                LogUtils.info(String.format("Selected '%s' in dropdown", optionText));
                                return;
                        }
                }

                // If not found, log error and throw exception to let test know the error
                String err = String.format("Could not find option '%s' in dropdown", optionText);
                LogUtils.error(err);
                throw new NoSuchElementException(err);
        }

        /**
         * Choose option in Employee Name dropdown
         * 
         * @param name
         */
        public void selectDropdownForEmployeeName(String name) {
                List<WebElement> options = showDropdown(name);

                for (WebElement opt : options) {
                        String optionText = opt.getText().trim();
                        if (optionText.toLowerCase().contains(name.toLowerCase())) {
                                opt.click();
                                LogUtils.info(String.format("Selected employee '%s'", optionText));
                                return;
                        }
                }

                String err = String.format("Could not find employee '%s'", name);
                LogUtils.error(err);
                throw new NoSuchElementException(err);
        }

        public List<WebElement> showDropdown(String name) {
                // 1. Type employee name
                type(employeeNameInput, name);

                // 2. Wait for "Searching...." to disappear (if any)
                By searchingLocator = By
                                .xpath("//div[contains(@class,'oxd-autocomplete-option') and contains(.,'Searching')]");
                waitForInvisibility(searchingLocator);

                // 3. Wait for list results to display
                By optionsLocator = By.xpath("//div[contains(@class,'oxd-autocomplete-option')]");
                List<WebElement> options = getElementsList(optionsLocator);
                return options;
        }

}