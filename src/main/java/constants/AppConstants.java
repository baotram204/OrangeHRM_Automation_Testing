package constants;


public class AppConstants {

    // ========================= URL FRAGMENTS =========================
    public static final String URL_ADMIN_USERS = "viewSystemUsers";
    public static final String URL_ADD_USER = "saveSystemUser";
    public static final String URL_LOGIN = "auth/login";

    // ========================= MESSAGES =========================
    public static final String MSG_REQUIRED = "Required";
    public static final String MSG_INVALID = "Invalid";
    public static final String MSG_ALREADY_EXISTS = "Already exists";
    public static final String MSG_SUCCESS_SAVED = "Successfully Saved";

    // ========================= VALIDATION MESSAGES =========================
    public static final String MSG_PASS_MIN_LENGTH = "Should have at least 8 characters";
    public static final String MSG_PASS_NO_UPPER = "Your password must contain minimum 1 upper-case letter";
    public static final String MSG_PASS_NO_LOWER = "Your password must contain minimum 1 lower-case letter";
    public static final String MSG_PASS_NO_NUMBER = "Your password must contain minimum 1 number";
    public static final String MSG_PASS_NO_SPECIAL = "Your password must contain minimum 1 special character";
    public static final String MSG_PASS_NOT_MATCH = "Passwords do not match";
    public static final String MSG_USER_NAME_MIN_LENGTH = "Should be at least 5 characters";
    public static final String MSG_USER_NAME_MAX_LENGTH = "Should not exceed 40 characters";
    public static final String MSG_INVALID_CHARACTERS = "Should only contain alphanumeric characters and underscore";

    // ========================= TEST ROLES & STATUS =========================
    public static final String ROLE_ADMIN = "Admin";
    public static final String ROLE_ESS = "ESS";
    public static final String STATUS_ENABLED = "Enabled";
    public static final String STATUS_DISABLED = "Disabled";
    public static final String PASSWORD_VALID = "Valid@123";
    public static final String PASSWORD_INVALID = "Wrong@123";
    public static final String PASSWORD_WEAK = "12345677";
    public static final String EMPLOYEE_NAME_VALID = "John";
    public static final String EMPLOYEE_NAME_INVALID = "Invalid";

    // ========================= USERNAME & ERROR MESSAGE =========================
    public static final String USERNAME_ADMIN = "Admin";
    public static final String USERNAME_USER = "User_";
    public static final String USERNAME_INVALID = "Invalid";
    public static final String USERNAME_MIN_LENGTH = "Adm";
    public static final String ERROR_MESSAGE_USERNAME_MIN_LENGTH = "Should be at least 5 characters";

    // ========================= MISC =========================
    public static final String EXCEL_FILE_PATH = System.getProperty("user.dir") + "/testdata/TestData.xlsx";

    private AppConstants() {
        /* utility class, no instantiation */ }
}
