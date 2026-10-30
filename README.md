# Tour Sales Manager

# Introduction 
TSM is a Java application that allows customers and staff members to interact directly to book tours and manage tour transactions. Tours can be edited flexibly by staff members with the appropriate permissions. 

# Technologies 
- Programming language: Java 21 (không dùng tính năng preview)
- User interface: Java Swing (JCalendar cho chọn ngày)
- Database: MySQL 8+ (MySQL Connector/J 8.3)
- Thư viện khác: Lombok, jBCrypt (băm mật khẩu), Apache POI (xuất Excel), Angus Mail (gửi OTP qua email)
- Build: Maven, đóng gói thành một file jar chạy được (`target/TourSalesManager.jar`)

# Project structure
```
src/main/java/org/toursalesmanager/
├── main/         # Điểm khởi chạy ứng dụng (Main)
├── login/        # Màn hình đăng nhập, SessionManager (tài khoản + quyền đang đăng nhập)
├── security/     # AuthorizationService: quyền mặc định theo vai trò, tính quyền theo tài khoản
├── enums/        # Role (vai trò), Permission (danh sách quyền, có nhóm + nhãn tiếng Việt)
├── dto/          # Đối tượng dữ liệu (AccountDTO, TourDTO, TourPlanDTO, ...)
├── dao/          # Truy vấn MySQL (MyConnection đọc cấu hình từ config.properties)
├── bus/          # Xử lý nghiệp vụ, kiểm tra dữ liệu và kiểm tra quyền
├── service/      # MailService gửi OTP
├── validate/     # ValidationException
└── gui/
    ├── MainFrame.java  # Khung chính, sidebar hiển thị theo quyền
    ├── panel/          # Các trang: Tour, Kế hoạch tour, Khách hàng, Phân quyền, ...
    ├── dialog/         # Form thêm/sửa
    ├── component/      # ButtonFactory
    └── helper/         # Màu, font, mật khẩu, Excel, PermissionUI, ...

database/
├── quanlytour.sql                 # Toàn bộ CSDL (cấu trúc + dữ liệu mẫu), dùng khi cài mới
├── add_account_email.sql          # Migration cho CSDL cũ
└── add_account_permission.sql     # Migration cho CSDL cũ
src/main/resources/       # config.properties(.example), hình ảnh
```

# Installing and Setup 
```
git clone <repository-url>
cd <repository-folder>
```

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

## Tạo cơ sở dữ liệu (cài mới)

File `database/quanlytour.sql` chứa toàn bộ 15 bảng (kể cả `taikhoan_quyen`, `tourbooking`) và dữ liệu mẫu. File được dump lại từ CSDL đang dùng, khớp với code hiện tại. Khi cài mới **chỉ cần chạy file này**, không cần chạy các migration bên dưới.

```sql
CREATE DATABASE tour CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
```
Sau đó mở `database/quanlytour.sql` trong MySQL Workbench, chọn database `tour` và bấm ⚡. Hoặc chạy bằng dòng lệnh:
```powershell
cmd /c "\"C:\Program Files\MySQL\MySQL Server 9.3\bin\mysql.exe\" -u root -p --default-character-set=utf8mb4 tour < database\quanlytour.sql"
```

**Lưu ý:** file dump có lệnh `DROP TABLE IF EXISTS`, nên chạy vào database đang có dữ liệu sẽ **xoá dữ liệu cũ** của các bảng trùng tên.

Tài khoản mẫu: `admin` (ADMIN), `NV003`, `NV005` (TOUR_OPERATE), `NV004`, `NV006` (ACCOUNTANT). Mật khẩu đã được băm BCrypt, hãy dùng mật khẩu bạn đã đặt hoặc đặt lại.

Để cập nhật dump sau khi đổi cấu trúc CSDL:
```powershell
cmd /c "\"C:\Program Files\MySQL\MySQL Server 9.3\bin\mysqldump.exe\" -u root -p --single-transaction --routines --triggers --skip-dump-date --set-gtid-purged=OFF --default-character-set=utf8mb4 --result-file=database\quanlytour.sql tour"
```
**Trước khi commit file dump mới:** CSDL local chứa email thật (dùng để nhận OTP), nên mở `database/quanlytour.sql` và thay các email thật trong phần `INSERT INTO taikhoan` bằng email mẫu (ví dụ `kh008@example.com`), để không lộ email lên repo.

## Gửi OTP qua email

Đặt biến môi trường `MAIL_USERNAME` và `MAIL_APP_PASSWORD` trước khi mở
ứng dụng để gửi OTP đặt lại mật khẩu; với Gmail, `MAIL_APP_PASSWORD` là App
Password. OTP có hiệu lực trong 5 phút.

## Chạy ứng dụng

**Khi phát triển:** mở project bằng IntelliJ và chạy `org.toursalesmanager.main.Main`. IntelliJ chạy thẳng từ `target/classes`, sửa code xong chạy lại là thấy thay đổi, **không cần build jar**. Cấu hình DB lấy từ `src/main/resources/config.properties`.

**Khi cần gửi app đi** (nộp đồ án, demo trên máy khác), build file jar:

```powershell
mvn clean package
```
Trong IntelliJ thì bấm *Maven → Lifecycle → package*. Kết quả là file `target/TourSalesManager.jar` (khoảng 23 MB), đã gói kèm toàn bộ thư viện.

File jar **không tự cập nhật**. Sau khi sửa code, muốn gửi bản mới thì chạy lại `mvn clean package`.

Để chạy file jar:
1. Chép `TourSalesManager.jar` sang thư mục bất kỳ.
2. Đặt file `config.properties` **cùng thư mục với file jar**. File này cố ý không được đóng gói vào jar để không lộ mật khẩu DB khi gửi jar cho người khác.
3. Chạy `java -jar TourSalesManager.jar` hoặc nhấp đúp vào file. Máy chạy cần có **Java 21** trở lên và kết nối được MySQL.

Thứ tự tìm `config.properties`: cạnh file jar → thư mục đang chạy lệnh → `src/main/resources` (khi chạy từ IDE).

## Các script cập nhật cơ sở dữ liệu (chỉ cho CSDL cũ)

Chỉ dùng khi bạn đang có CSDL tạo từ bản dump **cũ** và không muốn tạo lại từ `quanlytour.sql`. Chạy lần lượt trên database đang dùng (mặc định là `tour`), mỗi script chỉ cần chạy một lần. CSDL tạo từ `quanlytour.sql` mới đã có sẵn các thay đổi này.

| Thứ tự | Script | Nội dung |
|---|---|---|
| 1 | `add_account_email.sql` | Thêm cột `email` (unique) vào bảng `taikhoan` |
| 2 | `add_account_permission.sql` | Tạo bảng `taikhoan_quyen` lưu quyền riêng của từng tài khoản |

**Cách chạy bằng MySQL Workbench:** File → Open SQL Script… → chọn file → nhấp đúp database `tour` ở cột Schemas → bấm ⚡ (Execute).

**Cách chạy bằng dòng lệnh (PowerShell):**
```powershell
cmd /c "\"C:\Program Files\MySQL\MySQL Server 9.3\bin\mysql.exe\" -u root -p tour < database\add_account_permission.sql"
```

Nếu chưa chạy `add_account_permission.sql`, ứng dụng vẫn hoạt động với quyền mặc định theo vai trò, nhưng trang **Phân quyền** sẽ báo lỗi khi lưu.

## Tài khoản cũ chưa có `idstaff` / `idcustomer`

Tài khoản cũ vẫn đăng nhập được, nhưng trang **Thông tin cá nhân** sẽ không tìm thấy hồ sơ. Khách hàng chưa liên kết cũng không tự đặt tour được. Để gắn lại tự động những tài khoản có `username` trùng mã nhân viên/khách hàng:


# Phân quyền

## Vai trò và quyền mặc định
Có 4 vai trò (`enums/Role`): `ADMIN`, `TOUR_OPERATE`, `ACCOUNTANT`, `CUSTOMER`. Mỗi vai trò có một bộ quyền mặc định trong `security/AuthorizationService`. Người chưa đăng nhập chỉ được xem tour, loại tour, kế hoạch tour, địa điểm và khuyến mãi.

## Quyền riêng theo tài khoản
Quyền thực tế của một tài khoản được tính trong `AuthorizationService.resolvePermissions`:
1. `ADMIN` luôn có toàn quyền và không thể bị sửa quyền, để tránh admin tự khóa mình.
2. Tài khoản đã được phân quyền riêng (có dòng trong `taikhoan_quyen`) dùng đúng danh sách đó.
3. Tài khoản còn lại dùng quyền mặc định theo vai trò.

Quyền được nạp một lần khi đăng nhập (`SessionManager`). Vì vậy **thay đổi quyền có hiệu lực từ lần đăng nhập tiếp theo** của tài khoản đó.

## Trang Phân quyền (chỉ admin)
- Mục **Phân quyền** chỉ hiện trên sidebar với tài khoản `ADMIN`. Trang và `AccountPermissionBUS` đều kiểm tra lại vai trò admin, kể cả khi tài khoản khác được cấp quyền `MANAGE_ROLE`.
- Bên trái là danh sách tài khoản (có tìm kiếm). Bên phải là danh sách tất cả quyền, đánh dấu cấp/bỏ quyền bằng checkbox.
- Các nút: **Chọn tất cả**, **Bỏ chọn tất cả**, **Khôi phục mặc định** (xóa quyền riêng, quay về quyền theo vai trò), **Lưu thay đổi**, **Làm mới**. Ứng dụng hỏi xác nhận nếu chuyển tài khoản khi chưa lưu.
- Không cho lưu danh sách quyền rỗng. Muốn chặn hẳn một tài khoản thì nên khóa tài khoản.

# Luồng đăng nhập, xem tour và đặt tour

## Luồng hiện có trong ứng dụng

1. **Khách hàng** đăng ký tài khoản khách hàng trên màn hình đăng nhập, sau đó đăng nhập bằng tên đăng nhập và mật khẩu. Tài khoản đăng ký mới được gán vai trò khách hàng.
2. **Nhân viên** đăng nhập bằng tài khoản đã được cấp. Nhân viên không tự đăng ký qua màn hình đăng ký khách hàng; các màn hình và thao tác được hiển thị theo quyền của tài khoản.
3. Người dùng có quyền xem tour có thể mở danh sách tour, tìm kiếm và xem thông tin chi tiết. Các thao tác thêm, sửa hoặc xóa tour chỉ dành cho người có quyền quản lý tour.
4. Ngay tại màn hình **Tour**, khách hàng chọn một tour (không bắt buộc) rồi bấm **Chọn lịch & đặt tour**; cũng có thể mở **Kế hoạch tour** và bấm **Đặt tour**. Khách chọn kế hoạch khởi hành, nhập số vé và ghi chú rồi gửi yêu cầu. Hệ thống tự gắn phiếu với khách hàng đang đăng nhập, lấy giá từ kế hoạch tour và đặt trạng thái ban đầu là **Chờ xác nhận**. Danh sách của khách hàng chỉ hiển thị các phiếu của chính họ.
5. Nhân viên có quyền xem/tạo phiếu đặt có thể xem danh sách phục vụ công việc và lập phiếu cho khách hàng. Hóa đơn được quản lý ở màn hình riêng.
6. Trong màn hình **Kế hoạch tour**, chọn một kế hoạch rồi bấm **Xem chi tiết** để mở danh sách chi tiết (ngày thực hiện, các khoản chi, điểm đi/điểm đến). Người có quyền `MANAGE_TOUR_PLAN` có thể thêm, sửa, xóa chi tiết ngay trong cửa sổ này.

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

# Update 

## Mã tự tăng
Mã được tạo tự động theo số lớn nhất hiện có + 1, ô nhập mã bị khóa trên form:

| Đối tượng | Định dạng | Ví dụ |
|---|---|---|
| Loại tour | `LTxxx` | `LT001` |
| Kế hoạch tour | theo `TourPlanDAO` | |
| Hóa đơn | theo `InvoiceDAO` | |
| Khách hàng | `KHxxx` (tự dài thêm khi vượt `KH999`) | `KH013` |
| Chi tiết kế hoạch tour | `CKHTxx` | `CKHT02` |

## Phân quyền theo tài khoản
- Thêm bảng `taikhoan_quyen` (`database/add_account_permission.sql`), `AccountPermissionDAO`, `AccountPermissionBUS` và trang `PermissionPanel` (chỉ admin).
- `Permission` có thêm nhóm và nhãn tiếng Việt để hiển thị.
- `SessionManager` lưu sẵn quyền của tài khoản khi đăng nhập thay vì chỉ tra theo vai trò.
- `AccountDAO.getAllAccounts()` dùng cho danh sách tài khoản.

## Khách hàng
- Mã khách hàng tự tăng (`CustomerDAO.getNextCustomerId`), không còn nhập tay.
- Form khách hàng hiển thị giữa cửa sổ cha.
- Sửa lỗi kiểm tra "Tên" đang kiểm tra nhầm ô "Họ".

## Chi tiết kế hoạch tour
- **Xem chi tiết** mở đúng cửa sổ danh sách chi tiết theo **mã kế hoạch tour** đang chọn. Trước đây nút này mở nhầm form thêm và truyền mã tour.
- Sửa lỗi DAO đọc dữ liệu trước `rs.next()` khiến danh sách không bao giờ tải được.
- Sửa các lỗi sau:
  - Cột "Điểm đến" hiển thị nhầm điểm đi.
  - Tìm theo mã chi tiết đang so sánh nhầm với mã kế hoạch.
  - Bốn khoản tiền đều lấy từ ô "Tổng chi".
  - Tiền ở bị lưu bằng tiền ăn.
  - Lỗi ép kiểu ngày.
  - Form sửa luôn trống.
- Truy vấn chuyển sang `PreparedStatement`. Thêm/sửa/xóa chi tiết yêu cầu quyền `MANAGE_TOUR_PLAN`.

## Tài khoản
- Sửa `AccountDAO.existsByStaffId`: truy vấn đúng bảng `taikhoan` (trước đây là bảng `account` không tồn tại). Khi có lỗi CSDL, hàm ném lỗi thay vì trả về `false`, để không bỏ qua bước kiểm tra trùng tài khoản.
- Đăng nhập kiểm tra cột `status`: chỉ tài khoản `ACTIVE` mới được vào. Tài khoản cũ có `status` là `NULL` vẫn được coi là đang hoạt động. Trạng thái chỉ được kiểm tra sau khi mật khẩu đúng.

## Giao diện
- Thêm `gui/helper/WindowHelper`. Tất cả dialog giờ hiển thị giữa cửa sổ cha thay vì giữa màn hình.
- Các nơi mở dialog trước đây truyền `null` làm cửa sổ cha nay truyền cửa sổ thật (nhân viên, phiếu đặt tour, kế hoạch tour, nhập chi tiết hóa đơn).
- Thêm `gui/helper/TableSelectionHelper`. Đây là cách dùng chung để bật/tắt nút theo dòng đang chọn trong bảng, thay cho các hàm `hasSelectedRow()` viết lặp lại và các `MouseListener` ở các trang Tour, Loại tour, Kế hoạch tour, Chi tiết kế hoạch tour, Phiếu đặt tour, Khách hàng, Nhân viên, Địa điểm, Hóa đơn.

# Planned Improvements 

## Lỗi đã biết
- [x] ~~`AccountDAO.existsByStaffId` truy vấn nhầm bảng `account`~~. Đã sửa.
- [x] ~~Đăng nhập chưa kiểm tra cột `status`~~. Đã sửa. Chức năng khóa/mở khóa tài khoản trong `AccountBUS` vẫn đang bị comment, nên hiện chỉ khóa được bằng SQL: `UPDATE taikhoan SET status = 'LOCKED' WHERE idaccount = ?`.
- [x] ~~`quanlytour.sql` đã cũ, cấu trúc bảng `taikhoan` khác với code~~. Đã dump lại từ CSDL hiện tại và chuyển vào `database/quanlytour.sql`.
- [x] ~~Dữ liệu mẫu trong `taikhoan` còn lỗi~~. Đã dọn trên CSDL `tour` và dump lại:
  - Xoá tài khoản trùng tên `hieutran` (id 8, giữ id 7) và tài khoản gõ nhầm `` hieutran` `` (id 10).
  - Băm BCrypt mật khẩu `123` của `hieutran1` (vẫn đăng nhập bằng `123`).
  - Trong **file dump**, email thật được thay bằng email mẫu `kh008@example.com`, `kh009@example.com`. CSDL local vẫn giữ email thật để nhận OTP.
  - Thêm `UNIQUE` cho `userName`; `email` thành `varchar(254)` + `UNIQUE`.
- [ ] Một số tài khoản khách hàng chưa có `idcustomer` (`hieutran`, `hieutran1`), nên không tự đặt tour được. Xem mục *Tài khoản cũ chưa có `idstaff` / `idcustomer`*.
- [ ] `AccountBUS.registerCustomer` chưa bắt lỗi trùng khoá. Nếu hai người đăng ký cùng tên/email cùng lúc, ràng buộc `UNIQUE` sẽ chặn lại nhưng thông báo lỗi chưa thân thiện.
- [ ] `TourPlanDAO`, `TourTypeDAO` còn ghép chuỗi SQL (`createStatement`), dễ lỗi khi dữ liệu có dấu `'` và có nguy cơ SQL injection. Các DAO này cũng giữ một `Connection` dùng chung suốt vòng đời đối tượng.

## Còn thiếu 
- [ ] Trang quản lý tài khoản cho admin: tạo tài khoản, khóa/mở khóa, đặt lại mật khẩu, **liên kết tài khoản với hồ sơ nhân viên/khách hàng** (`idstaff`/`idcustomer`).
- [ ] Tự liên kết khi đăng nhập: nếu tài khoản thiếu `idstaff`/`idcustomer` mà `username` trùng mã hồ sơ thì tự cập nhật.
- [ ] Ràng buộc unique cho `taikhoan.idstaff`, `taikhoan.idcustomer` và khóa ngoại `taikhoan_quyen.idaccount → taikhoan.idaccount`.
- [ ] Mật khẩu mặc định của tài khoản nhân viên đang cố định là `123456`. Nên sinh ngẫu nhiên và bắt đổi ở lần đăng nhập đầu.
- [ ] Áp dụng quyền mới ngay cho phiên đang đăng nhập, không phải chờ đăng nhập lại.
- [x] ~~Dialog hiển thị giữa màn hình thay vì giữa cửa sổ cha~~. Đã sửa.
- [x] ~~Trang Thông tin cá nhân hiển thị giới tính `null` và email `-`~~. Đã sửa: email lấy từ tài khoản, ngày sinh định dạng `dd/MM/yyyy`. Giới tính hiện `-` vì CSDL chưa có cột này.
- [x] ~~`ProfilePanel` dùng String Template `STR` (preview)~~. Đã đổi sang ghép chuỗi thường và bỏ `--enable-preview` trong `pom.xml`.
- [x] ~~`pom.xml` đóng gói `war` và có dependency servlet/JSTL thừa~~. Đã chuyển sang `jar` chạy được (`maven-shade-plugin`, `Main-Class`), bỏ servlet/JSTL, `config.properties` đặt ngoài jar.
- [ ] Chưa có unit test (`src/test` trống). Nên bắt đầu với các lớp BUS: kiểm tra dữ liệu, phân quyền, tạo mã tự tăng.
- [ ] Tạo mã tự tăng bằng `MAX + 1` có thể trùng khi hai người thêm cùng lúc. Nên tạo mã trong transaction hoặc xử lý lỗi trùng khóa rồi thử lại.

# Git Workflow  
```
git branch --show-current
git status

git add . / git add |files|
git commit -m "comment(what I done in these files)"
git push origin main/your-feture-name
```

# Design 

# Demo features 
1. Đăng ký tài khoản khách hàng, đăng nhập, quên mật khẩu bằng OTP qua email.
2. Xem tour / kế hoạch tour khi chưa đăng nhập; đặt tour sau khi đăng nhập (trạng thái **Chờ xác nhận**).
3. Quản lý tour, loại tour, kế hoạch tour và chi tiết kế hoạch tour, địa điểm, khách hàng, nhân viên, hóa đơn, khuyến mãi; mã tự tăng.
4. Lịch khuyến mãi, thống kê, xuất Excel.
5. Giao diện và thao tác hiển thị theo quyền; admin phân quyền riêng cho từng tài khoản trong trang **Phân quyền**.

# Developer
Trần Viết Hiếu 

# Contact 
Email: trhi4100@gmail.com
