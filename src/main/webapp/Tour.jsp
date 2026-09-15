<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản Lý Tour</title>
    <style>
        :root {
            --bg-body: #f0f2f5;
            --panel-bg: #ffffff;
            --primary: #40c8f2;
            --primary-dark: #40c8f2;
            --secondary: #5f6368;
            --success: #1e8e3e;
            --danger: #d93025;
            --accent: #e8f0fe;
            --shadow: 0 4px 20px rgba(0,0,0,0.08);
        }

        body {
            font-family: 'Inter', -apple-system, sans-serif;
            margin: 0; padding: 0;
            background-color: var(--bg-body);
            color: #202124;
        }

        .container {
            width: 90%; max-width: 1200px; margin: 40px auto;
            background: var(--panel-bg); min-height: 600px;
            border-radius: 12px; overflow: hidden;
            box-shadow: var(--shadow);
            position: relative;
        }

        .header-panel {
            background: linear-gradient(135deg, var(--primary-dark), var(--primary));
            color: white; padding: 30px; text-align: center;
        }
        .header-panel h2 { margin: 0; font-weight: 500; letter-spacing: 0.5px; }

        #panelList { padding: 30px; animation: fadeIn 0.5s; }

        /* Search Bar cải tiến */
        .search-bar {
            display: flex;
            flex-direction: column; /* Xếp tiêu đề và ô tìm kiếm theo hàng dọc */
            align-items: center;    /* Căn giữa theo chiều ngang */
            gap: 15px;
            margin-bottom: 25px;
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
        }
        .filter-group { display: flex; gap: 10px; }

        .search-bar select, .search-bar input {
            padding: 10px 15px; border: 1px solid #dadce0;
            border-radius: 6px; outline: none; font-size: 14px;
        }

        /* Table Style */
        .table-container { border-radius: 8px; border: 1px solid #e0e0e0; overflow: hidden; }
        table { width: 100%; border-collapse: collapse; }
        th {
            background-color: #f1f3f4; padding: 15px; text-align: left;
            font-size: 13px; text-transform: uppercase; color: var(--secondary);
        }
        td { padding: 14px 15px; border-bottom: 1px solid #f1f3f4; font-size: 14px; }

        .clickable-row { transition: 0.2s; }
        .clickable-row:hover { background-color: #f8f9fa; cursor: pointer; }
        .selected-row { background-color: var(--accent) !important; color: var(--primary-dark); font-weight: 600; }

        /* Panel Nhập liệu */
        #panelInput {
            display: none; padding: 40px; max-width: 500px; margin: auto;
            animation: slideUp 0.4s ease-out;
        }

        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; margin-bottom: 8px; font-weight: 500; color: var(--secondary); font-size: 14px; }
        .form-group input {
            width: 100%; padding: 12px; border: 2px solid #eee;
            border-radius: 6px; box-sizing: border-box; transition: 0.3s;
        }
        .form-group input:focus { border-color: var(--primary); outline: none; background: #fff; }

        .button-group { margin-top: 30px; display: flex; justify-content: center; gap: 12px; }
        button {
            padding: 12px 24px; cursor: pointer; border-radius: 6px;
            font-weight: 600; font-size: 14px; transition: 0.2s; border: none;
        }

        .btn-main { background: var(--primary); color: white; }
        .btn-main:hover { background: var(--primary-dark); transform: translateY(-1px); }
        .btn-outline { background: white; color: var(--primary); border: 1px solid var(--primary); }
        .btn-danger { background: white; color: var(--danger); border: 1px solid var(--danger); }
        .btn-danger:hover { background: #fce8e6; }

        @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
        @keyframes slideUp { from { transform: translateY(20px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
    </style>
</head>
<body>

<div class="container">
    <div class="header-panel">
        <h2>Hệ thống Quản lý Tour</h2>
    </div>

    <div id="panelList">
        <div class="search-bar">
            <div style="font-weight: 600; color: var(--secondary);">Tìm kiếm tour</div>
            <div class="filter-group">
                <select id="filterType">
                    <option value="1">Tên tour</option>
                </select>
                <input type="text" id="filterKeyword" placeholder="Nhập từ khóa cần tìm..." onkeyup="filterTable()">
            </div>
        </div>

        <div class="table-container">
            <table id="mainTable">
                <thead>
                <tr>
                    <th>Mã tour</th>
                    <th>Tên</th>
                    <th>Số ngày</th>
                    <th>Đơn giá</th>
                    <th>Số chỗ</th>
                    <th>Điểm khởi hành</th>
                    <th>Ảnh</th>
                    <th>Mã loại tour</th>
                    <th>Mã địa điểm</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="t" items="${danhSachTour}">
                    <tr>
                        <td>${t.maTour}</td>
                        <td>${t.ten}</td>
                        <td>${t.soNgay}</td>
                        <td>${t.donGia}</td>
                        <td>${t.soCho}</td>
                        <td>${t.diaDiemKhoiHanh}</td>
                        <td>${t.imgLink}</td>
                        <td>${t.maLoaiTour}</td>
                        <td>${t.maDiaDiem}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="button-group">
            <button class="btn-main" onclick="changePanel('add')">+ Thêm mới</button>
            <button class="btn-outline" style="border-color:#ccc; color:#666" onclick="location.href='${pageContext.request.contextPath}/Tour'">Làm mới</button>
        </div>
    </div>

    <div id="panelInput">
        <h3 id="panelTitle" style="margin-bottom: 25px; text-align:center;">THÔNG TIN CHI TIẾT</h3>
        <form action="${pageContext.request.contextPath}/Tour" method="POST">
            <input type="hidden" name="action" id="txtAction">

            <div class="form-group">
                <label>Mã tour</label>
                <input type="text" name="maTour">
            </div>

            <div class="form-group">
                <label>Tên</label>
                <input type="text" name="ten">
            </div>

            <div class="form-group">
                <label>Số ngày</label>
                <input type="number" name="soNgay">
            </div>

            <div class="form-group">
                <label>Đơn giá</label>
                <input type="number" name="donGia">
            </div>

            <div class="form-group">
                <label>Số chỗ</label>
                <input type="number" name="soCho">
            </div>

            <div class="form-group">
                <label>Điểm khởi hành</label>
                <input type="text" name="diaDiemKhoiHanh">
            </div>

            <div class="form-group">
                <label>Link ảnh</label>
                <input type="text" name="imgLink">
            </div>

            <div class="form-group">
                <label>Mã loại tour</label>
                <input type="text" name="maLoaiTour">
            </div>

            <div class="form-group">
                <label>Mã địa điểm</label>
                <input type="text" name="maDiaDiem">
            </div>

            <div class="button-group">
                <button type="submit" class="btn-main" style="background: var(--success);">Lưu dữ liệu</button>
                <button type="button" class="btn-outline" style="border-color:#777; color:#777" onclick="hidePanel()">Quay lại</button>
            </div>
        </form>
    </div>
</div>

<script>
    let selectedData = null;

    // Hàm lọc bảng không cần load lại trang
    function filterTable() {
        const input = document.getElementById("filterKeyword");
        const filter = input.value.toUpperCase();
        const colIndex = document.getElementById("filterType").value;
        const table = document.getElementById("mainTable");
        const tr = table.getElementsByTagName("tr");

        for (let i = 1; i < tr.length; i++) {
            const td = tr[i].getElementsByTagName("td")[colIndex];
            if (td) {
                const txtValue = td.textContent || td.innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    tr[i].style.display = "";
                } else {
                    tr[i].style.display = "none";
                }
            }
        }
    }

    function mouseClicked(row, ma, ten, songay, dongia, socho, dd, link, maloaitour, madd) {
        document.querySelectorAll('.clickable-row').forEach(r => r.classList.remove('selected-row'));
        row.classList.add('selected-row');
        selectedData = { ma, ten, songay, dongia, socho, dd, link, maloaitour, madd};
    }

    function changePanel(type) {
        if (type === 'edit') {
            if (!selectedData) return alert("Chọn 1 dòng!");

            document.getElementById('txtMa').value = selectedData.ma;
            document.getElementById('txtTen').value = selectedData.ten;
            document.getElementById('txtSoNgay').value = selectedData.songay;
            document.getElementById('txtDonGia').value = selectedData.dongia;
            document.getElementById('txtSoCho').value = selectedData.socho;
            document.getElementById('txtDD').value = selectedData.dd;
            document.getElementById('txtImg').value = selectedData.link;
            document.getElementById('txtLoai').value = selectedData.maloai;
            document.getElementById('txtDiaDiem').value = selectedData.madd;

            document.getElementById('panelTitle').innerText = "CẬP NHẬT TOUR";
        } else {
            document.querySelectorAll('#panelInput input').forEach(i => i.value = "");
            document.getElementById('panelTitle').innerText = "THÊM TOUR";
        }

        document.getElementById('txtAction').value = type;
        document.getElementById('panelList').style.display = 'none';
        document.getElementById('panelInput').style.display = 'block';
    }

    function hidePanel() {
        document.getElementById('panelList').style.display = 'block';
        document.getElementById('panelInput').style.display = 'none';
    }

    function btnXoa_Click() {
        if (!selectedData) return alert("Chọn 1 dòng!");
        if (confirm("Xóa tour " + selectedData.ten + "?")) {
            const f = document.createElement('form');
            f.method = 'POST';
            f.action = '${pageContext.request.contextPath}/Tour';

            const act = document.createElement('input');
            act.type = 'hidden';
            act.name = 'action';
            act.value = 'delete';

            const id = document.createElement('input');
            id.type = 'hidden';
            id.name = 'maTour';
            id.value = selectedData.ma;

            f.appendChild(act);
            f.appendChild(id);

            document.body.appendChild(f);
            f.submit();
        }
    }
</script>

</body>
</html>