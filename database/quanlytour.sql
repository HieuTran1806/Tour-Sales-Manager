-- MySQL dump 10.13  Distrib 9.3.0, for Win64 (x86_64)
--
-- Host: localhost    Database: tour
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cthoadon`
--

DROP TABLE IF EXISTS `cthoadon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cthoadon` (
  `mahd` varchar(10) NOT NULL,
  `makhang` varchar(10) DEFAULT NULL,
  `giave` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`mahd`),
  KEY `fk_ctiethd_khachhang_idx` (`makhang`),
  CONSTRAINT `fk_ctiethd_hoadon` FOREIGN KEY (`mahd`) REFERENCES `hoadon` (`maHD`),
  CONSTRAINT `fk_ctiethd_khachhang` FOREIGN KEY (`makhang`) REFERENCES `khachhang` (`maKH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cthoadon`
--

LOCK TABLES `cthoadon` WRITE;
/*!40000 ALTER TABLE `cthoadon` DISABLE KEYS */;
INSERT INTO `cthoadon` VALUES ('HD001','KH001',5780000),('HD002','KH002',11560000),('HD003','KH003',6580000),('HD004','KH004',26970000),('HD005','KH005',9180000),('HD006','KH006',27960000),('HD007','KH019',12861600),('HD008','KH007',14670000),('HD009','KH020',45571500),('HD010','KH001',31980000),('HD011','KH008',32450000),('HD012','KH002',59980000),('HD013','KH012',29990000),('HD014','KH009',8980000),('HD015','KH013',6570000),('HD016','KH018',9180000),('HD017','KH003',6580000),('HD018','KH004',24980000),('HD019','KH006',99980000),('HD020','KH007',13160000),('HD021','KH014',29980000);
/*!40000 ALTER TABLE `cthoadon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ctietkhtour`
--

DROP TABLE IF EXISTS `ctietkhtour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ctietkhtour` (
  `mactietkhtour` varchar(10) NOT NULL,
  `ngaythuchien` date DEFAULT NULL,
  `tongchi` decimal(10,0) DEFAULT NULL,
  `tieno` decimal(10,0) DEFAULT NULL,
  `tienan` decimal(10,0) DEFAULT NULL,
  `tiendilai` decimal(10,0) DEFAULT NULL,
  `diemdi` varchar(45) DEFAULT NULL,
  `diemden` varchar(45) DEFAULT NULL,
  `makhtour` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`mactietkhtour`),
  KEY `fk_ctietkhtour_kehoachtour_idx` (`makhtour`),
  CONSTRAINT `fk_ctietkhtour_kehoachtour` FOREIGN KEY (`makhtour`) REFERENCES `kehoachtour` (`makhtour`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ctietkhtour`
--

LOCK TABLES `ctietkhtour` WRITE;
/*!40000 ALTER TABLE `ctietkhtour` DISABLE KEYS */;
INSERT INTO `ctietkhtour` VALUES ('CKHT01','2026-01-16',29000000,12000000,7000000,10000000,'TP. Hồ Chí Minh','Cần Thơ','KH001'),('CKHT02','2026-01-17',23000000,12000000,7000000,4000000,'Cần Thơ','Chợ nổi Cái Răng','KH001'),('CKHT03','2026-01-18',8000000,0,3000000,5000000,'Cần Thơ','TP. Hồ Chí Minh','KH001'),('CKHT04','2026-11-05',202000000,40000000,12000000,150000000,'Hà Nội','Tokyo','KH011'),('CKHT05','2026-11-06',79000000,45000000,14000000,20000000,'Tokyo','Núi Phú Sĩ','KH011'),('CKHT06','2026-11-07',70000000,40000000,12000000,18000000,'Núi Phú Sĩ','Tokyo','KH011'),('CKHT07','2026-11-08',60000000,40000000,12000000,8000000,'Tokyo','Tokyo','KH011'),('CKHT08','2026-11-10',9000000,0,4000000,5000000,'Tokyo','Hà Nội','KH011'),('CKHT09','2026-11-08',33000000,16000000,8000000,9000000,'Hà Nội','Tràng An','KH012'),('CKHT10','2026-11-09',27000000,0,9000000,18000000,'Tràng An','Hà Nội','KH012'),('CKHT11','2026-11-20',32000000,12000000,6000000,14000000,'TP. Hồ Chí Minh','Đà Lạt','KH013'),('CKHT12','2026-11-21',24000000,12000000,7000000,5000000,'Đà Lạt','Langbiang','KH013'),('CKHT13','2026-11-22',12000000,0,4000000,8000000,'Đà Lạt','TP. Hồ Chí Minh','KH013');
/*!40000 ALTER TABLE `ctietkhtour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ctrinhkm`
--

DROP TABLE IF EXISTS `ctrinhkm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ctrinhkm` (
  `maKM` varchar(255) NOT NULL,
  `tenKM` varchar(255) NOT NULL,
  `ngayBD` date NOT NULL,
  `ngayKT` date NOT NULL,
  `hinhThucKM` tinyint DEFAULT NULL,
  `chietKhau` float NOT NULL,
  `ghiChu` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`maKM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ctrinhkm`
--

LOCK TABLES `ctrinhkm` WRITE;
/*!40000 ALTER TABLE `ctrinhkm` DISABLE KEYS */;
INSERT INTO `ctrinhkm` VALUES ('KM01','Chào hè 2026','2026-05-01','2026-06-30',0,8,'Giảm 8% cho tour biển'),('KM02','Tri ân hóa đơn từ 20 triệu','2026-07-01','2026-07-31',1,5,'Áp dụng cho hóa đơn từ 20.000.000 đ'),('KM03','Mùa thu vàng','2026-09-01','2026-10-31',0,10,'Giảm 10% tour Hà Giang và Nhật Bản'),('KM04','Khách hàng mới','2026-11-01','2026-12-31',1,3,'Áp dụng cho hóa đơn từ 10.000.000 đ'),('KM05','Black Friday','2026-11-20','2026-11-30',1,7,'Áp dụng cho hóa đơn từ 30.000.000 đ'),('KM06','Du xuân Tết Đinh Mùi 2027','2026-12-15','2027-02-15',0,12,'Giảm 12% tour nước ngoài dịp Tết');
/*!40000 ALTER TABLE `ctrinhkm` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diadiem`
--

DROP TABLE IF EXISTS `diadiem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diadiem` (
  `MaDiaDiem` varchar(10) NOT NULL,
  `TenDiaDiem` varchar(255) DEFAULT NULL,
  `DiaChi` varchar(255) DEFAULT NULL,
  `QuocGia` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`MaDiaDiem`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diadiem`
--

LOCK TABLES `diadiem` WRITE;
/*!40000 ALTER TABLE `diadiem` DISABLE KEYS */;
INSERT INTO `diadiem` VALUES ('DD01','Vịnh Hạ Long','Vịnh Hạ Long, Quảng Ninh','Việt Nam'),('DD02','Sa Pa','Thị trấn Sa Pa và đỉnh Fansipan, Lào Cai','Việt Nam'),('DD03','Phố cổ Hội An','Phố cổ Hội An, TP. Đà Nẵng','Việt Nam'),('DD04','Đà Lạt','Trung tâm Đà Lạt, Lâm Đồng','Việt Nam'),('DD05','Phú Quốc','Đặc khu Phú Quốc, An Giang','Việt Nam'),('DD06','Đà Nẵng - Bà Nà','Bán đảo Sơn Trà và Bà Nà Hills, TP. Đà Nẵng','Việt Nam'),('DD07','Nha Trang','Vịnh Nha Trang, Khánh Hòa','Việt Nam'),('DD08','Cố đô Huế','Đại Nội Huế, TP. Huế','Việt Nam'),('DD09','Tràng An - Bái Đính','Quần thể danh thắng Tràng An, Ninh Bình','Việt Nam'),('DD10','Chợ nổi Cái Răng','Chợ nổi Cái Răng, TP. Cần Thơ','Việt Nam'),('DD11','Côn Đảo','Đặc khu Côn Đảo, TP. Hồ Chí Minh','Việt Nam'),('DD12','Cao nguyên đá Đồng Văn','Đồng Văn - Mã Pì Lèng, Tuyên Quang','Việt Nam'),('DD13','Quy Nhơn - Kỳ Co','Bãi Kỳ Co và Eo Gió, Quy Nhơn, Gia Lai','Việt Nam'),('DD14','Hồ Hoàn Kiếm','Hồ Hoàn Kiếm và phố cổ, Hà Nội','Việt Nam'),('DD15','Bangkok - Pattaya','Bangkok và Pattaya','Thái Lan'),('DD16','Seoul - Đảo Nami','Seoul và đảo Nami','Hàn Quốc'),('DD17','Tokyo - Núi Phú Sĩ','Tokyo và núi Phú Sĩ','Nhật Bản'),('DD18','Singapore','Marina Bay và đảo Sentosa','Singapore'),('DD19','Bali','Ubud và Kuta, đảo Bali','Indonesia'),('DD20','Thượng Hải - Hàng Châu','Bến Thượng Hải và Tây Hồ Hàng Châu','Trung Quốc'),('DD21','Paris','Tháp Eiffel và bảo tàng Louvre, Paris','Pháp');
/*!40000 ALTER TABLE `diadiem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hoadon`
--

DROP TABLE IF EXISTS `hoadon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoadon` (
  `maHD` varchar(10) NOT NULL,
  `maKHTour` varchar(10) DEFAULT NULL,
  `maKHangDat` varchar(10) DEFAULT NULL,
  `maNV` varchar(10) DEFAULT NULL,
  `ngay` date DEFAULT NULL,
  `soLuong` int DEFAULT NULL,
  `maKM` varchar(10) DEFAULT NULL,
  `tongTien` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`maHD`),
  KEY `fk_hoadon_kehoachtour_idx` (`maKHTour`),
  KEY `fk_hoadon_nhanvien_idx` (`maNV`),
  KEY `fk_hoadon_khuyenmai_idx` (`maKM`),
  CONSTRAINT `fk_hoadon_kehoachtour` FOREIGN KEY (`maKHTour`) REFERENCES `kehoachtour` (`makhtour`),
  CONSTRAINT `fk_hoadon_khuyenmai` FOREIGN KEY (`maKM`) REFERENCES `ctrinhkm` (`maKM`),
  CONSTRAINT `fk_hoadon_nhanvien` FOREIGN KEY (`maNV`) REFERENCES `nhanvien` (`maNV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoadon`
--

LOCK TABLES `hoadon` WRITE;
/*!40000 ALTER TABLE `hoadon` DISABLE KEYS */;
INSERT INTO `hoadon` VALUES ('HD001','KH001','KH001','NV004','2026-01-06',2,NULL,5780000),('HD002','KH001','KH002','NV004','2026-01-09',4,NULL,11560000),('HD003','KH002','KH003','NV006','2026-02-04',2,NULL,6580000),('HD004','KH003','KH004','NV004','2026-02-21',3,NULL,26970000),('HD005','KH004','KH005','NV006','2026-04-11',2,NULL,9180000),('HD006','KH005','KH006','NV004','2026-05-21',4,NULL,27960000),('HD007','KH005','KH019','NV006','2026-05-25',2,'KM01',12861600),('HD008','KH006','KH007','NV004','2026-06-16',3,NULL,14670000),('HD009','KH007','KH020','NV006','2026-07-10',3,'KM02',45571500),('HD010','KH007','KH001','NV004','2026-07-26',2,NULL,31980000),('HD011','KH008','KH008','NV006','2026-08-16',5,NULL,32450000),('HD012','KH011','KH002','NV004','2026-09-21',2,NULL,59980000),('HD013','KH011','KH012','NV006','2026-09-27',1,NULL,29990000),('HD014','KH009','KH009','NV004','2026-09-29',2,NULL,8980000),('HD015','KH012','KH013','NV006','2026-10-31',3,NULL,6570000),('HD016','KH021','KH018','NV004','2026-11-02',2,NULL,9180000),('HD017','KH013','KH003','NV004','2026-11-03',2,NULL,6580000),('HD018','KH014','KH004','NV006','2026-11-03',2,NULL,24980000),('HD019','KH019','KH006','NV004','2026-11-05',2,NULL,99980000),('HD020','KH013','KH007','NV006','2026-11-05',4,NULL,13160000),('HD021','KH017','KH014','NV004','2026-11-06',2,NULL,29980000);
/*!40000 ALTER TABLE `hoadon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kehoachtour`
--

DROP TABLE IF EXISTS `kehoachtour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kehoachtour` (
  `makhtour` varchar(10) NOT NULL,
  `ngaykhoihanh` date DEFAULT NULL,
  `ngayketthuc` date DEFAULT NULL,
  `tongsove` int DEFAULT NULL,
  `tongchidukien` decimal(10,0) DEFAULT NULL,
  `soveconlai` int DEFAULT NULL,
  `trangthai` varchar(45) DEFAULT NULL,
  `matour` varchar(10) DEFAULT NULL,
  `manvhd` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`makhtour`),
  KEY `fk_kehoachtour` (`matour`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kehoachtour`
--

LOCK TABLES `kehoachtour` WRITE;
/*!40000 ALTER TABLE `kehoachtour` DISABLE KEYS */;
INSERT INTO `kehoachtour` VALUES ('KH001','2026-01-16','2026-01-18',30,60000000,24,'Đã kết thúc','T09','NV005'),('KH002','2026-02-20','2026-02-22',30,68000000,28,'Đã kết thúc','T05','NV003'),('KH003','2026-03-10','2026-03-14',25,160000000,22,'Đã kết thúc','T12','NV007'),('KH004','2026-04-28','2026-04-30',30,95000000,28,'Đã kết thúc','T01','NV003'),('KH005','2026-06-12','2026-06-15',30,150000000,24,'Đã kết thúc','T06','NV005'),('KH006','2026-07-03','2026-07-06',35,120000000,32,'Đã kết thúc','T07','NV008'),('KH007','2026-08-18','2026-08-22',25,280000000,20,'Đã kết thúc','T13','NV007'),('KH008','2026-09-02','2026-09-05',35,160000000,30,'Đã kết thúc','T03','NV003'),('KH009','2026-10-15','2026-10-18',20,62000000,17,'Đã kết thúc','T11','NV005'),('KH010','2026-10-23','2026-10-25',25,70000000,25,'Đã hủy','T02','NV008'),('KH011','2026-11-05','2026-11-10',20,420000000,17,'Đang diễn ra','T14','NV007'),('KH012','2026-11-08','2026-11-09',40,60000000,37,'Đang diễn ra','T08','NV003'),('KH013','2026-11-20','2026-11-22',30,68000000,24,'Sắp khởi hành','T05','NV005'),('KH014','2026-11-26','2026-11-29',25,220000000,23,'Sắp khởi hành','T15','NV007'),('KH015','2026-12-04','2026-12-07',30,150000000,27,'Sắp khởi hành','T06','NV008'),('KH016','2026-12-11','2026-12-13',40,80000000,38,'Sắp khởi hành','T09','NV005'),('KH017','2026-12-18','2026-12-22',20,210000000,18,'Sắp khởi hành','T16','NV007'),('KH018','2026-12-26','2026-12-30',25,280000000,21,'Sắp khởi hành','T13','NV003'),('KH019','2027-01-12','2027-01-19',20,700000000,18,'Sắp khởi hành','T18','NV007'),('KH020','2027-02-03','2027-02-07',30,190000000,30,'Sắp khởi hành','T12','NV008'),('KH021','2026-12-31','2027-01-02',30,95000000,28,'Sắp khởi hành','T01','NV003'),('KH022','2026-11-27','2026-11-29',20,85000000,20,'Đã hủy','T10','NV008'),('KH023','2026-12-19','2026-12-21',30,75000000,30,'Sắp khởi hành','T19','NV005'),('KH024','2027-01-08','2027-01-10',30,70000000,30,'Sắp khởi hành','T20','NV003');
/*!40000 ALTER TABLE `kehoachtour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khachhang`
--

DROP TABLE IF EXISTS `khachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khachhang` (
  `maKH` varchar(10) NOT NULL,
  `ho` varchar(45) DEFAULT NULL,
  `ten` varchar(45) DEFAULT NULL,
  `diaChi` varchar(255) DEFAULT NULL,
  `sdt` varchar(20) DEFAULT NULL,
  `ngaySinh` date DEFAULT NULL,
  PRIMARY KEY (`maKH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khachhang`
--

LOCK TABLES `khachhang` WRITE;
/*!40000 ALTER TABLE `khachhang` DISABLE KEYS */;
INSERT INTO `khachhang` VALUES ('KH001','Nguyễn Văn','An','12 Lý Thường Kiệt, Phường Diên Hồng, TP. Hồ Chí Minh','0981112233','1988-02-14'),('KH002','Trần Thị','Bình','56 Hai Bà Trưng, Phường Sài Gòn, TP. Hồ Chí Minh','0982223344','1979-06-21'),('KH003','Lê Hoàng','Cường','89 Chu Văn An, Phường Bình Thạnh, TP. Hồ Chí Minh','0983334455','1995-10-03'),('KH004','Phạm Thị','Dung','34 Hàng Bài, Phường Hoàn Kiếm, Hà Nội','0984445566','1985-12-12'),('KH005','Hoàng Minh','Đức','102 Nguyễn Văn Linh, Phường Hải Châu, TP. Đà Nẵng','0985556677','1992-04-27'),('KH006','Vũ Thị','Giang','7 Trần Phú, Phường Nha Trang, Khánh Hòa','0986667788','1990-09-15'),('KH007','Đỗ Quang','Huy','250 Lạc Long Quân, Phường Tây Hồ, Hà Nội','0987778899','1983-01-08'),('KH008','Ngô Thị','Lan','18 Mậu Thân, Phường Ninh Kiều, TP. Cần Thơ','0988889900','1976-07-19'),('KH009','Phan Văn','Long','66 Nguyễn Huệ, Phường Thuận Hóa, TP. Huế','0989990011','1999-11-11'),('KH010','Huỳnh Thị','Mai','41 Lê Lợi, Phường Bến Thành, TP. Hồ Chí Minh','0971234560','1993-03-03'),('KH011','Trương Văn','Nam','9 Bà Triệu, Phường Hai Bà Trưng, Hà Nội','0962345671','1987-05-30'),('KH012','Lý Thị','Oanh','150 Võ Thị Sáu, Phường Xuân Hòa, TP. Hồ Chí Minh','0953456782','1980-08-08'),('KH013','Đinh Công','Phát','27 Trường Chinh, Phường Đống Đa, Hà Nội','0944567893','2000-02-29'),('KH014','Cao Thị','Quỳnh','73 Hùng Vương, Phường Đà Lạt, Lâm Đồng','0935678904','1996-06-06'),('KH015','Mai Văn','Sơn','200 Cộng Hòa, Phường Tân Bình, TP. Hồ Chí Minh','0926789015','1975-10-10'),('KH016','Tạ Thị','Thảo','5 Phạm Ngũ Lão, Phường Bến Thành, TP. Hồ Chí Minh','0917890126','1998-12-01'),('KH017','Dương Minh','Trí','88 Điện Biên Phủ, Phường Thanh Khê, TP. Đà Nẵng','0908901237','1991-07-17'),('KH018','La Thị','Uyên','14 Nguyễn Văn Cừ, Phường Ninh Kiều, TP. Cần Thơ','0389012348','1994-09-24'),('KH019','Hồ Văn','Vinh','3 Quang Trung, Phường Hồng Bàng, Hải Phòng','0370123459','1986-04-04'),('KH020','Kiều Thị','Yến','61 Lê Duẩn, Phường Sài Gòn, TP. Hồ Chí Minh','0361234560','1989-01-25');
/*!40000 ALTER TABLE `khachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kmhd_chitiet`
--

DROP TABLE IF EXISTS `kmhd_chitiet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kmhd_chitiet` (
  `maKM` varchar(10) NOT NULL,
  `tongTienApDung` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`maKM`),
  CONSTRAINT `fk_kmhd_ctrinhkm` FOREIGN KEY (`maKM`) REFERENCES `ctrinhkm` (`maKM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kmhd_chitiet`
--

LOCK TABLES `kmhd_chitiet` WRITE;
/*!40000 ALTER TABLE `kmhd_chitiet` DISABLE KEYS */;
INSERT INTO `kmhd_chitiet` VALUES ('KM02','20000000'),('KM04','10000000'),('KM05','30000000');
/*!40000 ALTER TABLE `kmhd_chitiet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kmtour_chitiet`
--

DROP TABLE IF EXISTS `kmtour_chitiet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kmtour_chitiet` (
  `maKM` varchar(10) NOT NULL,
  `maTour` varchar(10) NOT NULL,
  PRIMARY KEY (`maKM`,`maTour`),
  KEY `fk_kmtour_chitiet_tour_idx` (`maTour`),
  CONSTRAINT `fk_kmtour_chitiet_ctrinhkm` FOREIGN KEY (`maKM`) REFERENCES `ctrinhkm` (`maKM`),
  CONSTRAINT `fk_kmtour_chitiet_tour` FOREIGN KEY (`maTour`) REFERENCES `tour` (`matour`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kmtour_chitiet`
--

LOCK TABLES `kmtour_chitiet` WRITE;
/*!40000 ALTER TABLE `kmtour_chitiet` DISABLE KEYS */;
INSERT INTO `kmtour_chitiet` VALUES ('KM01','T03'),('KM01','T06'),('KM01','T07'),('KM03','T11'),('KM03','T14'),('KM06','T12'),('KM06','T13'),('KM06','T15');
/*!40000 ALTER TABLE `kmtour_chitiet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `loaitour`
--

DROP TABLE IF EXISTS `loaitour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loaitour` (
  `maloaitour` varchar(10) NOT NULL,
  `theloai` varchar(255) DEFAULT NULL,
  `mota` varchar(255) DEFAULT NULL,
  `trangthai` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`maloaitour`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loaitour`
--

LOCK TABLES `loaitour` WRITE;
/*!40000 ALTER TABLE `loaitour` DISABLE KEYS */;
INSERT INTO `loaitour` VALUES ('LT001','Tour trong nước','Tham quan các điểm đến nổi tiếng trong nước','Đang hoạt động'),('LT002','Tour nước ngoài','Du lịch quốc tế, đã bao gồm vé máy bay và visa','Đang hoạt động'),('LT003','Tour khám phá','Trekking, leo núi, trải nghiệm vùng cao','Đang hoạt động'),('LT004','Tour nghỉ dưỡng biển','Nghỉ dưỡng tại resort ven biển và biển đảo','Đang hoạt động'),('LT005','Tour văn hóa - lịch sử','Tham quan di sản, di tích và làng nghề truyền thống','Đang hoạt động'),('LT006','Tour doanh nghiệp','Tour đoàn kết hợp hội nghị, team building','Ngưng');
/*!40000 ALTER TABLE `loaitour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhanvien`
--

DROP TABLE IF EXISTS `nhanvien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhanvien` (
  `maNV` varchar(10) NOT NULL,
  `ho` varchar(45) DEFAULT NULL,
  `ten` varchar(45) DEFAULT NULL,
  `diaChi` varchar(255) DEFAULT NULL,
  `sdt` varchar(20) DEFAULT NULL,
  `ngaySinh` date DEFAULT NULL,
  PRIMARY KEY (`maNV`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhanvien`
--

LOCK TABLES `nhanvien` WRITE;
/*!40000 ALTER TABLE `nhanvien` DISABLE KEYS */;
INSERT INTO `nhanvien` VALUES ('NV001','Trần Viết','Hiếu','45 Nguyễn Thị Minh Khai, Phường Bến Thành, TP. Hồ Chí Minh','0901234567','1990-05-12'),('NV002','Nguyễn Huỳnh','Phúc','120 Lê Văn Sỹ, Phường Phú Nhuận, TP. Hồ Chí Minh','0912345678','1992-08-23'),('NV003','Tiêu Viết','Vương','78 Cách Mạng Tháng Tám, Phường Bàn Cờ, TP. Hồ Chí Minh','0923456789','1994-03-07'),('NV004','Lê Thị','Hạnh','15 Võ Văn Tần, Phường Xuân Hòa, TP. Hồ Chí Minh','0934567890','1991-11-30'),('NV005','Phạm Minh','Tuấn','210 Nguyễn Trãi, Phường Chợ Quán, TP. Hồ Chí Minh','0945678901','1996-01-18'),('NV006','Võ Thị','Ngọc','123 Điện Biên Phủ, Phường Gia Định, TP. Hồ Chí Minh','0956789012','1993-07-01'),('NV007','Đặng Quốc','Khánh','36 Trần Hưng Đạo, Phường Cầu Ông Lãnh, TP. Hồ Chí Minh','0967890123','1989-09-09'),('NV008','Bùi Thanh','Tâm','8 Phan Xích Long, Phường Cầu Kiệu, TP. Hồ Chí Minh','0978901234','1998-12-25');
/*!40000 ALTER TABLE `nhanvien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `idPayment` int NOT NULL AUTO_INCREMENT,
  `idTourBooking` int NOT NULL,
  `method` varchar(20) NOT NULL,
  `amount` decimal(12,0) NOT NULL,
  `receivedAmount` decimal(12,0) DEFAULT NULL,
  `transferContent` varchar(50) DEFAULT NULL,
  `transactionRef` varchar(50) DEFAULT NULL,
  `status` varchar(30) NOT NULL,
  `createdAt` datetime NOT NULL,
  `expiresAt` datetime DEFAULT NULL,
  `paidAt` datetime DEFAULT NULL,
  `confirmedAt` datetime DEFAULT NULL,
  `confirmedBy` varchar(50) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idPayment`),
  KEY `fk_payment_tourbooking_idx` (`idTourBooking`),
  KEY `idx_payment_status` (`status`),
  CONSTRAINT `fk_payment_tourbooking` FOREIGN KEY (`idTourBooking`) REFERENCES `tourbooking` (`idTourBooking`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,1,'CASH',5780000,5780000,'TSM PDT001 GD1',NULL,'CONFIRMED','2026-01-05 09:15:00','2026-01-07 00:00:00',NULL,'2026-01-06 10:20:00','NV004','Khách thanh toán tiền mặt tại quầy'),(2,2,'BANK_QR',11560000,11560000,'TSM PDT002 GD2','FT26008204511','CONFIRMED','2026-01-08 20:05:00','2026-01-08 20:20:00','2026-01-08 20:09:00','2026-01-09 08:30:00','NV004','Đã đối soát sao kê'),(3,3,'BANK_QR',6580000,6580000,'TSM PDT003 GD3','FT26034140217','CONFIRMED','2026-02-03 14:00:00','2026-02-03 14:15:00','2026-02-03 14:04:00','2026-02-04 09:00:00','NV006','Đã đối soát sao kê'),(4,4,'BANK_QR',26970000,26970000,'TSM PDT004 GD4','FT26051103688','CONFIRMED','2026-02-20 10:30:00','2026-02-20 10:45:00','2026-02-20 10:36:00','2026-02-21 08:45:00','NV004','Đã đối soát sao kê'),(5,5,'CASH',9180000,9180000,'TSM PDT005 GD5',NULL,'CONFIRMED','2026-04-10 16:00:00','2026-04-12 00:00:00',NULL,'2026-04-11 09:30:00','NV006','Khách thanh toán tiền mặt tại quầy'),(6,6,'BANK_QR',27960000,27960000,'TSM PDT006 GD6','FT26140211473','CONFIRMED','2026-05-20 21:10:00','2026-05-20 21:25:00','2026-05-20 21:14:00','2026-05-21 08:15:00','NV004','Đã đối soát sao kê'),(7,7,'BANK_QR',14670000,14670000,'TSM PDT007 GD7','FT26166110520','CONFIRMED','2026-06-15 11:00:00','2026-06-15 11:15:00','2026-06-15 11:05:00','2026-06-16 09:10:00','NV004','Đã đối soát sao kê'),(8,8,'BANK_QR',31980000,NULL,'TSM PDT008 GD8',NULL,'CANCELLED','2026-07-25 19:00:00','2026-07-25 19:15:00',NULL,NULL,NULL,'Khách tạo giao dịch mới'),(9,8,'BANK_QR',31980000,31980000,'TSM PDT008 GD9','FT26206192409','CONFIRMED','2026-07-25 19:20:00','2026-07-25 19:35:00','2026-07-25 19:24:00','2026-07-26 08:40:00','NV004','Đã đối soát sao kê'),(10,9,'BANK_QR',32450000,32450000,'TSM PDT009 GD10','FT26227150633','CONFIRMED','2026-08-15 15:00:00','2026-08-15 15:15:00','2026-08-15 15:06:00','2026-08-16 09:00:00','NV006','Đã đối soát sao kê'),(11,10,'BANK_QR',59980000,59980000,'TSM PDT010 GD11','FT26263130518','CONFIRMED','2026-09-20 13:00:00','2026-09-20 13:15:00','2026-09-20 13:05:00','2026-09-21 08:30:00','NV004','Đã đối soát sao kê'),(12,11,'BANK_QR',29990000,29900000,'TSM PDT011 GD12','FT26268100644','REJECTED','2026-09-25 10:00:00','2026-09-25 10:15:00','2026-09-25 10:06:00','2026-09-25 15:00:00','NV006','Sai số tiền: cần 29.990.000 đ, nhận 29.900.000 đ'),(13,11,'BANK_QR',29990000,29990000,'TSM PDT011 GD13','FT26269090502','CONFIRMED','2026-09-26 09:00:00','2026-09-26 09:15:00','2026-09-26 09:05:00','2026-09-27 08:30:00','NV006','Đã đối soát sao kê'),(14,12,'BANK_QR',8980000,8980000,'TSM PDT012 GD14','FT26271090427','CONFIRMED','2026-09-28 09:00:00','2026-09-28 09:15:00','2026-09-28 09:04:00','2026-09-29 08:20:00','NV004','Đã đối soát sao kê'),(15,13,'BANK_QR',4490000,NULL,'TSM PDT013 GD15',NULL,'EXPIRED','2026-10-01 20:00:00','2026-10-01 20:15:00',NULL,NULL,NULL,'Hết hạn thanh toán'),(16,14,'CASH',7980000,NULL,'TSM PDT014 GD16',NULL,'REJECTED','2026-10-05 10:00:00','2026-10-07 00:00:00',NULL,'2026-10-06 09:00:00','NV004','Kế hoạch tour bị hủy'),(17,16,'CASH',6570000,6570000,'TSM PDT016 GD17',NULL,'CONFIRMED','2026-10-30 14:00:00','2026-11-01 00:00:00',NULL,'2026-10-31 10:00:00','NV006','Khách thanh toán tiền mặt tại quầy'),(18,17,'CASH',9180000,9180000,'TSM PDT017 GD18',NULL,'CONFIRMED','2026-11-01 09:00:00','2026-11-03 00:00:00',NULL,'2026-11-02 15:30:00','NV004','Khách thanh toán tiền mặt tại quầy'),(19,18,'BANK_QR',6580000,6580000,'TSM PDT018 GD19','FT26306200311','CONFIRMED','2026-11-02 20:00:00','2026-11-02 20:15:00','2026-11-02 20:03:00','2026-11-03 08:30:00','NV004','Đã đối soát sao kê'),(20,19,'BANK_QR',24980000,24980000,'TSM PDT019 GD20','FT26307093405','CONFIRMED','2026-11-03 09:30:00','2026-11-03 09:45:00','2026-11-03 09:34:00','2026-11-03 14:00:00','NV006','Đã đối soát sao kê'),(21,20,'BANK_QR',99980000,99980000,'TSM PDT020 GD21','FT26308100752','CONFIRMED','2026-11-04 10:00:00','2026-11-04 10:15:00','2026-11-04 10:07:00','2026-11-05 09:00:00','NV004','Đã đối soát sao kê'),(22,21,'CASH',13160000,13160000,'TSM PDT021 GD22',NULL,'CONFIRMED','2026-11-04 16:00:00','2026-11-06 00:00:00',NULL,'2026-11-05 10:30:00','NV006','Khách thanh toán tiền mặt tại quầy'),(23,22,'BANK_QR',29980000,29980000,'TSM PDT022 GD23','FT26309190618','CONFIRMED','2026-11-05 19:00:00','2026-11-05 19:15:00','2026-11-05 19:06:00','2026-11-06 08:45:00','NV004','Đã đối soát sao kê'),(24,23,'BANK_QR',37470000,NULL,'TSM PDT023 GD24',NULL,'CANCELLED','2026-11-06 21:00:00','2026-11-06 21:15:00',NULL,NULL,NULL,'Khách hủy giao dịch'),(25,24,'BANK_QR',20970000,NULL,'TSM PDT024 GD25','FT26311221437','PENDING_VERIFICATION','2026-11-07 22:10:00','2026-11-07 22:25:00','2026-11-07 22:14:00',NULL,NULL,'Khách tạo mã QR chuyển khoản'),(26,26,'CASH',5780000,NULL,'TSM PDT026 GD26',NULL,'AWAITING_PAYMENT','2026-11-09 08:30:00','2026-11-11 00:00:00',NULL,NULL,NULL,'Khách chọn thanh toán trực tiếp');
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taikhoan`
--

DROP TABLE IF EXISTS `taikhoan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taikhoan` (
  `idAccount` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(45) NOT NULL,
  `status` varchar(45) DEFAULT 'ACTIVE',
  `idstaff` varchar(10) DEFAULT NULL,
  `idCustomer` varchar(10) DEFAULT NULL,
  `email` varchar(254) DEFAULT NULL,
  PRIMARY KEY (`idAccount`),
  UNIQUE KEY `uq_taikhoan_username` (`userName`),
  UNIQUE KEY `uq_taikhoan_email` (`email`),
  KEY `idStaff_idx` (`idstaff`),
  KEY `idCustomer_idx` (`idCustomer`),
  CONSTRAINT `idCustomer` FOREIGN KEY (`idCustomer`) REFERENCES `khachhang` (`maKH`),
  CONSTRAINT `idStaff` FOREIGN KEY (`idstaff`) REFERENCES `nhanvien` (`maNV`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taikhoan`
--

LOCK TABLES `taikhoan` WRITE;
/*!40000 ALTER TABLE `taikhoan` DISABLE KEYS */;
INSERT INTO `taikhoan` VALUES (1,'admin','$2a$10$D6thL4xbsM/k959v3cWgoutLbgcBuiBdbZ06czcuqi3jidPUm.qiC','ADMIN','ACTIVE',NULL,NULL,NULL),(2,'NV003','$2a$10$BYkHcf0MEw0PyNEKGLlgKeUluRJ/yJhSlrnr.VGBrKKrIUtXBipMC','TOUR_OPERATE','ACTIVE','NV003',NULL,'nv003@tsm.example.com'),(3,'NV004','$2a$10$U8mNaWDVULgBCor90K6zdOsogDB.h6vpJs7ayzPo3fDyPh23YZtgK','ACCOUNTANT','ACTIVE','NV004',NULL,'nv004@tsm.example.com'),(4,'NV005','$2a$10$zfDQgPT1kDDKs7GUhSxswO2NgDakJcGPnohURcI32q2G.GnsoL64i','TOUR_OPERATE','ACTIVE','NV005',NULL,'nv005@tsm.example.com'),(5,'NV006','$2a$10$Zrq8Qw8lkyo8JkMEp8JUSu7n5gTMj9LnNAcHIZGFkrtZ2Daida.5q','ACCOUNTANT','ACTIVE','NV006',NULL,'nv006@tsm.example.com'),(6,'NV007','$2a$10$YzETn4vgKUlHeOZS5CualO9AMNAWaNGoM1fhxocbxfKnbgdWyt9Ay','TOUR_OPERATE','ACTIVE','NV007',NULL,'nv007@tsm.example.com'),(7,'NV008','$2a$10$YzETn4vgKUlHeOZS5CualO9AMNAWaNGoM1fhxocbxfKnbgdWyt9Ay','TOUR_OPERATE','ACTIVE','NV008',NULL,'nv008@tsm.example.com'),(8,'nguyenvanan','$2a$10$YzETn4vgKUlHeOZS5CualO9AMNAWaNGoM1fhxocbxfKnbgdWyt9Ay','CUSTOMER','ACTIVE',NULL,'KH001','kh001@example.com'),(9,'tranthibinh','$2a$10$YzETn4vgKUlHeOZS5CualO9AMNAWaNGoM1fhxocbxfKnbgdWyt9Ay','CUSTOMER','ACTIVE',NULL,'KH002','kh002@example.com'),(10,'lehoangcuong','$2a$10$YzETn4vgKUlHeOZS5CualO9AMNAWaNGoM1fhxocbxfKnbgdWyt9Ay','CUSTOMER','ACTIVE',NULL,'KH003','kh003@example.com'),(11,'phamthidung','$2a$10$YzETn4vgKUlHeOZS5CualO9AMNAWaNGoM1fhxocbxfKnbgdWyt9Ay','CUSTOMER','ACTIVE',NULL,'KH004','kh004@example.com'),(12,'hoangminhduc','$2a$10$YzETn4vgKUlHeOZS5CualO9AMNAWaNGoM1fhxocbxfKnbgdWyt9Ay','CUSTOMER','ACTIVE',NULL,'KH005','kh005@example.com'),(13,'vuthigiang','$2a$10$YzETn4vgKUlHeOZS5CualO9AMNAWaNGoM1fhxocbxfKnbgdWyt9Ay','CUSTOMER','ACTIVE',NULL,'KH006','kh006@example.com'),(14,'maivanson','$2a$10$YzETn4vgKUlHeOZS5CualO9AMNAWaNGoM1fhxocbxfKnbgdWyt9Ay','CUSTOMER','ACTIVE',NULL,'KH015','kh015@example.com');
/*!40000 ALTER TABLE `taikhoan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taikhoan_quyen`
--

DROP TABLE IF EXISTS `taikhoan_quyen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taikhoan_quyen` (
  `idaccount` int NOT NULL,
  `permission` varchar(50) NOT NULL,
  PRIMARY KEY (`idaccount`,`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taikhoan_quyen`
--

LOCK TABLES `taikhoan_quyen` WRITE;
/*!40000 ALTER TABLE `taikhoan_quyen` DISABLE KEYS */;
INSERT INTO `taikhoan_quyen` VALUES (5,'MANAGE_CUSTOMER'),(5,'MANAGE_INVOICE'),(5,'MANAGE_PAYMENT'),(5,'MANAGE_TOUR'),(5,'VIEW_BOOKING'),(5,'VIEW_CUSTOMER'),(5,'VIEW_INVOICE'),(5,'VIEW_LOCATION'),(5,'VIEW_PAYMENT'),(5,'VIEW_PROMOTION'),(5,'VIEW_REVENUE_REPORT'),(5,'VIEW_STATISTICS'),(5,'VIEW_TOUR'),(5,'VIEW_TOUR_PLAN');
/*!40000 ALTER TABLE `taikhoan_quyen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tour`
--

DROP TABLE IF EXISTS `tour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tour` (
  `matour` varchar(10) NOT NULL,
  `ten` varchar(255) DEFAULT NULL,
  `songay` int DEFAULT NULL,
  `dongia` decimal(10,0) DEFAULT NULL,
  `socho` int DEFAULT NULL,
  `ddkhoihanh` varchar(255) DEFAULT NULL,
  `imglink` varchar(255) DEFAULT NULL,
  `maloaitour` varchar(10) DEFAULT NULL,
  `madiadiem` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`matour`),
  KEY `fk_tour_diadiem_idx` (`madiadiem`),
  KEY `fk_tour_loaitour_idx` (`maloaitour`),
  CONSTRAINT `fk_tour_diadiem` FOREIGN KEY (`madiadiem`) REFERENCES `diadiem` (`MaDiaDiem`),
  CONSTRAINT `fk_tour_loaitour` FOREIGN KEY (`maloaitour`) REFERENCES `loaitour` (`maloaitour`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tour`
--

LOCK TABLES `tour` WRITE;
/*!40000 ALTER TABLE `tour` DISABLE KEYS */;
INSERT INTO `tour` VALUES ('T01','Hạ Long - Du thuyền ngủ đêm trên vịnh',3,4590000,30,'Hà Nội',NULL,'LT004','DD01'),('T02','Sa Pa - Chinh phục Fansipan',3,3990000,25,'Hà Nội',NULL,'LT003','DD02'),('T03','Đà Nẵng - Bà Nà - Hội An',4,6490000,35,'TP. Hồ Chí Minh',NULL,'LT001','DD06'),('T04','Hội An - Cù Lao Chàm',3,4290000,30,'TP. Hồ Chí Minh',NULL,'LT005','DD03'),('T05','Đà Lạt - Thành phố ngàn hoa',3,3290000,30,'TP. Hồ Chí Minh',NULL,'LT001','DD04'),('T06','Phú Quốc - Nghỉ dưỡng Đảo Ngọc',4,6990000,30,'TP. Hồ Chí Minh',NULL,'LT004','DD05'),('T07','Nha Trang - Khám phá 4 đảo',4,4890000,35,'TP. Hồ Chí Minh',NULL,'LT004','DD07'),('T08','Ninh Bình - Tràng An - Bái Đính',2,2190000,40,'Hà Nội',NULL,'LT005','DD09'),('T09','Miền Tây sông nước - Chợ nổi Cái Răng',3,2890000,40,'TP. Hồ Chí Minh',NULL,'LT001','DD10'),('T10','Côn Đảo - Hành trình tâm linh',3,5990000,20,'TP. Hồ Chí Minh',NULL,'LT005','DD11'),('T11','Hà Giang - Mùa hoa tam giác mạch',4,4490000,20,'Hà Nội',NULL,'LT003','DD12'),('T12','Thái Lan: Bangkok - Pattaya',5,8990000,30,'TP. Hồ Chí Minh',NULL,'LT002','DD15'),('T13','Hàn Quốc: Seoul - Đảo Nami - Everland',5,15990000,25,'TP. Hồ Chí Minh',NULL,'LT002','DD16'),('T14','Nhật Bản: Tokyo - Núi Phú Sĩ mùa lá đỏ',6,29990000,20,'Hà Nội',NULL,'LT002','DD17'),('T15','Singapore - Gardens by the Bay - Sentosa',4,12490000,25,'TP. Hồ Chí Minh',NULL,'LT002','DD18'),('T16','Bali - Thiên đường nhiệt đới',5,14990000,20,'TP. Hồ Chí Minh',NULL,'LT002','DD19'),('T17','Trung Quốc: Thượng Hải - Hàng Châu',5,13490000,25,'Hà Nội',NULL,'LT002','DD20'),('T18','Pháp: Paris lãng mạn',8,49990000,20,'TP. Hồ Chí Minh',NULL,'LT002','DD21'),('T19','Quy Nhơn - Kỳ Co - Eo Gió',3,3790000,30,'TP. Hồ Chí Minh',NULL,'LT004','DD13'),('T20','Huế - Di sản cố đô',3,3590000,30,'TP. Hồ Chí Minh',NULL,'LT005','DD08'),('T21','Hà Nội - Phố cổ nghìn năm văn hiến',2,2490000,35,'TP. Hồ Chí Minh',NULL,'LT005','DD14');
/*!40000 ALTER TABLE `tour` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tourbooking`
--

DROP TABLE IF EXISTS `tourbooking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tourbooking` (
  `idTourBooking` int NOT NULL AUTO_INCREMENT,
  `idCustomer` varchar(10) DEFAULT NULL,
  `idTourPlan` varchar(10) DEFAULT NULL,
  `bookingdate` date DEFAULT NULL,
  `tickets` int DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `costTotal` decimal(10,0) DEFAULT NULL,
  `bookingStatus` varchar(45) DEFAULT NULL,
  `note` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idTourBooking`),
  KEY `fk_khangkhtour_kehoachtour_idx` (`idTourBooking`),
  KEY `fk_khangkhtour_khachhang_idx` (`idCustomer`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tourbooking`
--

LOCK TABLES `tourbooking` WRITE;
/*!40000 ALTER TABLE `tourbooking` DISABLE KEYS */;
INSERT INTO `tourbooking` VALUES (1,'KH001','KH001','2026-01-05',2,2890000,5780000,'Hoàn thành',''),(2,'KH002','KH001','2026-01-08',4,2890000,11560000,'Hoàn thành','Gia đình 2 người lớn, 2 trẻ em'),(3,'KH003','KH002','2026-02-03',2,3290000,6580000,'Hoàn thành',''),(4,'KH004','KH003','2026-02-20',3,8990000,26970000,'Hoàn thành',''),(5,'KH005','KH004','2026-04-10',2,4590000,9180000,'Hoàn thành','Phòng đôi'),(6,'KH006','KH005','2026-05-20',4,6990000,27960000,'Hoàn thành',''),(7,'KH007','KH006','2026-06-15',3,4890000,14670000,'Hoàn thành',''),(8,'KH001','KH007','2026-07-25',2,15990000,31980000,'Hoàn thành',''),(9,'KH008','KH008','2026-08-15',5,6490000,32450000,'Hoàn thành','Đoàn công ty'),(10,'KH002','KH011','2026-09-20',2,29990000,59980000,'Đã xác nhận',''),(11,'KH012','KH011','2026-09-25',1,29990000,29990000,'Đã xác nhận',''),(12,'KH009','KH009','2026-09-28',2,4490000,8980000,'Hoàn thành',''),(13,'KH011','KH009','2026-10-01',1,4490000,4490000,'Hết hạn','Quá hạn thanh toán'),(14,'KH010','KH010','2026-10-05',2,3990000,7980000,'Đã hủy','Kế hoạch tour bị hủy'),(15,'KH016','KH022','2026-10-20',2,5990000,11980000,'Đã hủy','Kế hoạch tour bị hủy do bão'),(16,'KH013','KH012','2026-10-30',3,2190000,6570000,'Đã xác nhận',''),(17,'KH018','KH021','2026-11-01',2,4590000,9180000,'Đã xác nhận','Đi dịp Tết Dương lịch'),(18,'KH003','KH013','2026-11-02',2,3290000,6580000,'Đã xác nhận',''),(19,'KH004','KH014','2026-11-03',2,12490000,24980000,'Đã xác nhận',''),(20,'KH006','KH019','2026-11-04',2,49990000,99980000,'Đã xác nhận','Tuần trăng mật'),(21,'KH007','KH013','2026-11-04',4,3290000,13160000,'Đã xác nhận',''),(22,'KH014','KH017','2026-11-05',2,14990000,29980000,'Đã xác nhận',''),(23,'KH017','KH014','2026-11-06',3,12490000,37470000,'Đã hủy','Khách đổi lịch'),(24,'KH001','KH015','2026-11-07',3,6990000,20970000,'Chờ xác nhận',''),(25,'KH015','KH018','2026-11-08',4,15990000,63960000,'Chờ xác nhận',''),(26,'KH005','KH016','2026-11-09',2,2890000,5780000,'Chờ xác nhận','');
/*!40000 ALTER TABLE `tourbooking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'tour'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed
