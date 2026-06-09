package pages;

import org.openqa.selenium.By;

public class DashboardPage extends BasePage {
    // private By adminMenu =
    // By.cssSelector("a[href='/web/index.php/admin/viewAdminModule']");

    private By adminMenu = By.xpath("//ul[@class='oxd-main-menu']//span[text()='Admin']");

    public AdminUserPage goToUsersPage() {
        // Wait for the admin menu to be clickable before clicking
        click(adminMenu);
        return new AdminUserPage();
    }

    public boolean isAdminMenuDisplayed() {
        return isDisplayed(adminMenu, 3);
    }
}
