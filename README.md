# Tour Sales Manager

# Introduction 
TSM is a Java application that allows customers and staff members to interact directly to book tours and manage tour transactions. Tours can be edited flexibly by staff members with the appropriate permissions. 

# Technologies 
Programming language: Java 
User interface: Java Swing
Database: MySQL 


# Project structure
java/
...

# Installing and Setup 
git clone <repository-url>
cd <repository-folder>

## Database configuration

Copy `src/main/resources/config.properties.example` to
`src/main/resources/config.properties`, then set the MySQL username and password
for your local database. The real `config.properties` is ignored by Git; commit
changes to the example file only when the required configuration keys change,
and never put a real password in the example.

PowerShell:

```powershell
Copy-Item src\main\resources\config.properties.example src\main\resources\config.properties
```

# Luồng đăng nhập, xem tour và đặt tour

## Luồng hiện có trong ứng dụng

1. **Khách hàng** đăng ký tài khoản khách hàng trên màn hình đăng nhập, sau đó đăng nhập bằng tên đăng nhập và mật khẩu. Tài khoản đăng ký mới được gán vai trò khách hàng.
2. **Nhân viên** đăng nhập bằng tài khoản đã được cấp. Nhân viên không tự đăng ký qua màn hình đăng ký khách hàng; các màn hình và thao tác được hiển thị theo quyền của tài khoản.
3. Người dùng có quyền xem tour có thể mở danh sách tour, tìm kiếm và xem thông tin chi tiết. Các thao tác thêm, sửa hoặc xóa tour chỉ dành cho người có quyền quản lý tour.
4. Ngay tại màn hình **Tour**, khách hàng chọn một tour (không bắt buộc) rồi bấm **Chọn lịch & đặt tour**; cũng có thể mở **Kế hoạch tour** và bấm **Đặt tour**. Khách chọn kế hoạch khởi hành, nhập số vé và ghi chú rồi gửi yêu cầu. Hệ thống tự gắn phiếu với khách hàng đang đăng nhập, lấy giá từ kế hoạch tour và đặt trạng thái ban đầu là **Chờ xác nhận**. Danh sách của khách hàng chỉ hiển thị các phiếu của chính họ.
5. Nhân viên có quyền xem/tạo phiếu đặt có thể xem danh sách phục vụ công việc và lập phiếu cho khách hàng. Hóa đơn được quản lý ở màn hình riêng.

**Lưu ý:** Yêu cầu khách gửi được ghi nhận ở trạng thái **Chờ xác nhận**; việc kiểm tra chỗ, xác nhận đặt tour, thu/đối soát tiền và gửi thông báo vẫn cần nhân viên xử lý. Tạo phiếu không đồng nghĩa với đã giữ chỗ hoặc đã thanh toán. Khách chỉ xem được phiếu của mình, không được tự đổi khách hàng, giá hoặc trạng thái.

## Nghiệp vụ đặt tour

### Khách hàng

1. Đăng ký hoặc đăng nhập; tìm tour, xem chi tiết và chọn kế hoạch khởi hành phù hợp.
2. Nhập số vé và ghi chú cần thiết, kiểm tra giá/tổng tiền dự kiến rồi gửi yêu cầu. Hệ thống gắn yêu cầu với hồ sơ của tài khoản, tự tính giá và ghi nhận trạng thái **chờ xác nhận**.
3. Theo dõi phiếu đặt trong màn hình **Phiếu đặt tour**. Phiếu chỉ được xem là xác nhận sau khi nhân viên kiểm tra và cập nhật theo quy trình của đơn vị.
4. Chỉ thanh toán/đặt cọc theo hướng dẫn và thời hạn do nhân viên thông báo. Sau khi nhân viên xác nhận còn chỗ và ghi nhận khoản thanh toán theo quy định, khách nhận xác nhận đặt tour, thông tin thanh toán và hướng dẫn tiếp theo.
5. Nếu hết chỗ hoặc thông tin chưa đầy đủ, nhân viên liên hệ khách để đề xuất lịch khác hoặc bổ sung thông tin; không tự xác nhận một yêu cầu không đáp ứng điều kiện.

### Nhân viên

1. Đăng nhập bằng tài khoản được cấp và chỉ thao tác trong phạm vi quyền. Kiểm tra lịch tour, sức chứa/chỗ còn nhận, giá và điều kiện áp dụng trước khi nhận yêu cầu.
2. Tìm khách hàng đã có hoặc tạo hồ sơ khách hàng mới sau khi thu thập thông tin liên hệ cần thiết. Xác nhận lại lịch khởi hành, số lượng khách, giá, yêu cầu đặc biệt và chính sách với khách.
3. Tạo phiếu đặt gắn đúng khách hàng và kế hoạch tour; lưu số lượng, đơn giá/tổng tiền, ngày đặt, ghi chú và trạng thái ban đầu phù hợp (ví dụ: **chờ xác nhận**). Không tự ý đánh dấu đã thanh toán hoặc đã xác nhận khi chưa có căn cứ.
4. Kiểm tra chỗ và xử lý tiền đặt cọc/thanh toán theo quy trình của đơn vị. Chỉ chuyển trạng thái xác nhận sau khi điều kiện giữ chỗ được đáp ứng; ghi nhận giao dịch và lập/quản lý hóa đơn theo quy trình kế toán.
5. Thông báo mã đặt tour, tình trạng, số tiền đã nhận/còn phải thanh toán và các mốc cần lưu ý cho khách. Theo dõi các yêu cầu chờ xử lý; khi khách hủy, đổi lịch hoặc hoàn tiền, cập nhật phiếu và xử lý chỗ/tiền theo chính sách, lưu lại ghi chú để truy vết.

# Developer
Trần Viết Hiếu 

# Planned Improvements 

# Git Workflow  
git branch --show-current
git status

git add . / git add |files|
git commit -m "comment(what I done in these files)"
git push origin main/your-feture-name

# Design 

# Demo features 
1. 
2. 
3. 

# Contact 
Email: trhi4100@gmail.com
