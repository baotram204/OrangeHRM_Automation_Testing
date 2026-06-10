# OrangeHRM Automation Testing

Dự án kiểm thử tự động cho hệ thống **OrangeHRM**, sử dụng **Java**, **Selenium WebDriver**, **TestNG**, và **Maven**. Dự án bao gồm các bộ test (test suite) để kiểm tra các chức năng quản lý người dùng (User Management).

## 🚀 Tính năng chính / Test Cases
- Create User : 12 test cases
- Read/Search User : 21 test cases
- Update User : 15 test cases
- Delete User : 9 test cases

## 📋 Yêu cầu hệ thống
- **Java JDK 11** hoặc mới hơn.
- **Maven** đã được cài đặt và cấu hình biến môi trường.
- Trình duyệt Chrome và WebDriver tương ứng.

## ⚙️ Hướng dẫn chạy Test (Local)

Chạy toàn bộ Test Suite mặc định theo `testng.xml`:
```bash
mvn clean test
```

Chạy một Test Class cụ thể:
```bash
mvn clean test -Dtest=tests.DeleteUserTest
```

Chạy theo TestNG Group (Ví dụ: Smoke test):
```bash
mvn clean test -Dgroups=smoke
```

---

## 🛠 Tích hợp Jenkins (CI/CD)

Dự án đã được thiết lập để có thể chạy tự động qua hệ thống Jenkins.

### 1. Cấu hình Job
- **Loại Job**: Freestyle Project hoặc Pipeline.
- **Source Code Management**: Trỏ tới Git Repository của dự án.
- **Build Steps**: Thêm *Invoke top-level Maven targets*.

### 2. Các lệnh chạy phổ biến trên Jenkins

Chạy cấu hình gốc qua file `testng.xml`:
```bash
clean test -DsuiteXmlFile=src/test/resources/testng.xml -Dmaven.test.failure.ignore=true
```

Chỉ chạy một số test cụ thể (Ví dụ: Update và Delete) không cần sửa `testng.xml`:
```bash
clean test -Dtest=tests.UpdateUserTest,tests.DeleteUserTest -Dmaven.test.failure.ignore=true
```

### 3. Kết quả chạy Jenkins

**📈 Biểu đồ Test Result Trend:**
> ![Test Result Trend]([<img width="834" height="318" alt="image" src="https://github.com/user-attachments/assets/cb886167-aedc-48f0-b8d6-a9e2c9a3d0bb" />
])
> *Mô tả: Biểu đồ theo dõi số lượng test Pass/Fail/Skip qua các lần build trên Jenkins.*

**✅ Console Output / Chi tiết bản Build:**
> ![Test Report]([<img width="1449" height="822" alt="image" src="https://github.com/user-attachments/assets/40a546a2-bd4d-4021-8dab-d98829260c46" />
])
> *Mô tả: Hình ảnh log chứng minh các test case chạy thành công.*

**📊 TestNG / HTML Report trên Jenkins:**
> ![Console Output]([<img width="1436" height="450" alt="image" src="https://github.com/user-attachments/assets/d69c5024-055f-4175-9f9a-4542e6a72b61" />
])
> *Mô tả: Giao diện report chi tiết của các test cases.*
