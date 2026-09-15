-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: tour
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
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
INSERT INTO `cthoadon` VALUES ('HD001','KH01',5000000),('HD002','KH01',5000000),('HD003','KH01',10000000),('HD004','KH01',5000000),('HD005','KH01',20000000),('HD006','KH01',30000000),('HD007','KH002',15000000),('HD008','KH002',20000000),('HD010','KH002',10000000),('HD011','KH003',10000000),('HD012','KH002',10000000);
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
INSERT INTO `ctietkhtour` VALUES ('CKHT01','2026-08-18',9000000,2000000,3000000,2000000,'HCM','Hà Nội','KH06');
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
INSERT INTO `ctrinhkm` VALUES ('KM01','Khuyến mãi khách hàng mới','2026-03-29','2026-03-31',0,5,''),('KM02','Kỷ niệm','2026-03-29','2026-03-31',1,5,'');
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
INSERT INTO `diadiem` VALUES ('DD01','Lanmark 81','abcd','Việt Nam'),('DD02','TPHCM','abc 123','Việt Nam'),('DD04','Thành phố Đà Lạt 2','abcd','Việt Nam');
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
INSERT INTO `hoadon` VALUES ('HD001','KH01','KH01','NV01','2026-03-24',1,NULL,5000000),('HD002','KH01','KH01','NV01','2026-03-24',1,NULL,5000000),('HD003','KH008','KH01','NV01','2026-02-11',1,NULL,10000000),('HD004','KH04','KH01','NV01','2026-02-28',1,NULL,5000000),('HD005','KH008','KH01','NV01','2026-03-29',2,NULL,20000000),('HD006','KH008','KH01','NV01','2026-01-01',3,NULL,30000000),('HD007','KH06','KH002','NV01','2026-01-22',3,NULL,15000000),('HD008','KH008','KH002','NV003','2026-03-30',4,'KM02',39000000),('HD010','KH05','KH002','NV01','2026-03-27',4,'KM02',19500000),('HD011','KH04','KH003','NV01','2026-04-15',4,'KM02',19500000),('HD012','KH03','KH003','NV003','2026-03-30',4,'KM01',19500000);
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
  `tongchi` decimal(10,0) DEFAULT NULL,
  `tongthu` decimal(10,0) DEFAULT NULL,
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
INSERT INTO `kehoachtour` VALUES ('KH008','2026-03-29','2026-03-30',-15,8000000,9000000,'T02','NV01'),('KH01','2026-01-01','2026-02-01',4,10000000,8000000,'T01','NV01'),('KH02','2026-03-09','2026-03-10',5,1000000,2000000,'T01','NV02'),('KH03','2026-03-09','2026-03-10',0,1000000,1000000,'T01','NV03'),('KH04','2026-03-25','2026-03-26',25,8000000,9000000,'T01','NV01'),('KH05','2026-03-27','2026-03-28',27,8000000,7000000,'T01','NV01'),('KH06','2026-03-27','2026-03-28',46,800000,700000,'T01','NV02'),('KH07','2026-03-29','2026-03-30',9,8000000,9000000,'T01','NV01'),('KH08','2026-03-29','2026-03-30',5,900000,800000,'T01','NV01'),('KH09','2026-03-30','2026-03-31',9,8000000,10000000,'T01','NV003'),('KH10','2026-03-30','2026-03-31',9,8000000,10000000,'T01','NV003');
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
INSERT INTO `khachhang` VALUES ('KH002','Trần','Hiếu','abcxyz 123','0123456789','2006-06-18'),('KH003','Trần','Hiếu','abc','0123456789','2026-03-29'),('KH01','Nguyễn Huỳnh','Phúc','abcxyz','0123456789','2026-03-24');
/*!40000 ALTER TABLE `khachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khang_khtour`
--

DROP TABLE IF EXISTS `khang_khtour`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khang_khtour` (
  `maKHTour` varchar(10) DEFAULT NULL,
  `maKHang` varchar(10) DEFAULT NULL,
  `giaVe` decimal(10,0) DEFAULT NULL,
  KEY `fk_khangkhtour_kehoachtour_idx` (`maKHTour`),
  KEY `fk_khangkhtour_khachhang_idx` (`maKHang`),
  CONSTRAINT `fk_khangkhtour_kehoachtour` FOREIGN KEY (`maKHTour`) REFERENCES `kehoachtour` (`makhtour`),
  CONSTRAINT `fk_khangkhtour_khachhang` FOREIGN KEY (`maKHang`) REFERENCES `khachhang` (`maKH`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khang_khtour`
--

LOCK TABLES `khang_khtour` WRITE;
/*!40000 ALTER TABLE `khang_khtour` DISABLE KEYS */;
INSERT INTO `khang_khtour` VALUES ('KH01','KH01',5000000);
/*!40000 ALTER TABLE `khang_khtour` ENABLE KEYS */;
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
INSERT INTO `kmhd_chitiet` VALUES ('KM02','0.0');
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
INSERT INTO `kmtour_chitiet` VALUES ('KM01','T01');
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
INSERT INTO `loaitour` VALUES ('LT01','Tour trong nước','','1'),('LT02','Tour nước ngoài','123','1'),('LT03','Tour khám phá','','1'),('LT04','Tour du lịch ','1234','1');
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
  `chucVu` varchar(255) DEFAULT NULL,
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
INSERT INTO `nhanvien` VALUES ('NV003','Nhân viên','Tiêu Viết','Vương','abc','0123456789','2026-03-29'),('NV004','Saler','Trần','Hiếu','abc','0123456789','2026-03-29'),('NV01','Nhân viên','Trần Viết','Hiếu','abc','0123456789','2006-01-01'),('NV02','Nhân viên','Nguyễn','Phúc','abc','0123456789','2026-03-24');
/*!40000 ALTER TABLE `nhanvien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taikhoan`
--

DROP TABLE IF EXISTS `taikhoan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taikhoan` (
  `userName` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL,
  `position` varchar(10) NOT NULL,
  PRIMARY KEY (`userName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taikhoan`
--

LOCK TABLES `taikhoan` WRITE;
/*!40000 ALTER TABLE `taikhoan` DISABLE KEYS */;
INSERT INTO `taikhoan` VALUES ('admin','admin123','quản lí'),('NV003','123','Nhân viên'),('NV004','123','Saler'),('NV01','123','Nhân viên'),('NV02','123','Nhân viên');
/*!40000 ALTER TABLE `taikhoan` ENABLE KEYS */;
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
INSERT INTO `tour` VALUES ('T01','Du lịch Phú Quốc',7,5000000,27,'Hồ Chí Minh',NULL,'LT01',NULL),('T02','Du lịch Miền Tây',14,10000000,27,'Hồ Chí Minh',NULL,'LT01',NULL),('T03','Du lịch Miền Trung',5,10000000,50,'Cần Thơ',NULL,'LT01',NULL),('T04','Du lịch Thượng Hải',5,10000000,9,'Hồ Chí Minh',NULL,'LT01',NULL),('T05','Du lịch sapa ',3,8000000,50,'Hồ Chí Minh','C:\\Users\\pc\\Pictures\\Camera Roll\\TuTien.jpg','LT01',NULL),('T06','Du lịch sapa ',6,9000000,50,'abc da lat','C:\\Users\\pc\\Pictures\\sapa.jpg','LT01','DD04'),('T07','Du lịch HCM',50,9000000,8000000,'abc 123','C:\\Users\\pc\\Pictures\\sapa.jpg','LT01','DD02');
/*!40000 ALTER TABLE `tour` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-03 15:41:51
