# OrangeHRM Automation Testing

An automation testing project for the OrangeHRM system using Java, Selenium WebDriver, TestNG, and Maven. The project includes comprehensive test suites for validating User Management functionalities.

## Key Features / Test Coverage
User Management Test Cases
Create User: 12 test cases
Read/Search User: 21 test cases
Update User: 15 test cases
Delete User: 9 test cases

## Prerequisites
Before running the project, ensure the following are installed:

- Java JDK 11 or later
- Maven configured in system environment variables
- Google Chrome browser
- Compatible ChromeDriver version

## ⚙️ Running Tests Locally

Run the Default Test Suite `testng.xml`:
```bash
mvn clean test
```

Run a Specific Test Class
```bash
mvn clean test -Dtest=tests.DeleteUserTest
```

Run Tests by TestNG Group (e.g., Smoke Tests)
```bash
mvn clean test -Dgroups=smoke
```

---

## Jenkins Integration (CI/CD)

The project is configured to support automated execution through Jenkins.

### 1. Jenkins Job Configuration
- **Job Type**: Freestyle Project or Pipeline.
- **Source Code Management**: Configure the project's Git repository.
- **Build Steps**: Add *Invoke top-level Maven targets*.

### 2. Common Maven Commands for Jenkins
Run the Full Test Suite Using testng.xml:
```bash
clean test -DsuiteXmlFile=src/test/resources/testng.xml -Dmaven.test.failure.ignore=true
```

Run Selected Test Classes Without Modifying testng.xml:
```bash
clean test -Dtest=tests.UpdateUserTest,tests.DeleteUserTest -Dmaven.test.failure.ignore=true
```

### 3. Jenkins Test Results

**Test Result Trend:**
> ![Test Result Trend]([<img width="834" height="318" alt="image" src="https://github.com/user-attachments/assets/cb886167-aedc-48f0-b8d6-a9e2c9a3d0bb" />
])
> *Description: Displays the number of Passed, Failed, and Skipped tests across Jenkins builds.*

**✅ Console Output/ Build Detail:**
> ![Test Report]([<img width="1449" height="822" alt="image" src="https://github.com/user-attachments/assets/40a546a2-bd4d-4021-8dab-d98829260c46" />
])
> *Description: Console logs demonstrating successful execution of automated test cases.*

**📊 TestNG HTML Report:**
> ![Console Output]([<img width="1436" height="450" alt="image" src="https://github.com/user-attachments/assets/d69c5024-055f-4175-9f9a-4542e6a72b61" />
])
> *Description: Detailed TestNG execution report showing test results, execution status, and test statistics.*
