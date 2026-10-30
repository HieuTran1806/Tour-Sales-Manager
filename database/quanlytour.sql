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
INSERT INTO `diadiem` VALUES ('DD01','Lanmark 81','abcd','Việt Nam'),('DD02','TPHCM','abc','Việt Nam'),('DD03','avadv','aesf','VietNam'),('DD04','Thành phố Đà Lạt 2','abcd','Việt Nam');
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
INSERT INTO `kehoachtour` VALUES ('KH008','2026-03-29','2026-03-30',1,8000000,NULL,NULL,'T02','NV01'),('KH01','2026-01-01','2026-02-01',5,10000000,NULL,NULL,'T01','NV003'),('KH01 ','2026-09-13','2026-09-14',4,4,0,'','T01','NV003'),('KH010','2026-09-21','2026-09-21',10,22222,17,'Sắp khởi hành','T01','NV003'),('KH02','2026-01-01','2026-01-09',5,10000000,50,'Sắp khởi hành','T06','NV003'),('KH03','2026-03-09','2026-03-10',0,1000000,NULL,NULL,'T01','NV03'),('KH04','2026-03-25','2026-03-26',25,8000000,NULL,NULL,'T01','NV01'),('KH05','2026-03-27','2026-03-27',27,6000000,10,'Sắp khởi hành','T01','NV003'),('KH06','2026-03-27','2026-03-27',2,800000,25,'Sắp khởi hành','T01','NV003'),('KH07','2026-03-29','2026-03-29',11,8000000,27,'Đã hủy','T02','NV003'),('KH08','2026-09-17','2026-09-17',20,22000,50,'Đã hủy','T03','NV003'),('KH09','2026-09-17','2026-09-18',200,100000000,9,'Sắp khởi hành','T04','NV003'),('KH10','2026-09-17','2026-09-18',11,111111,50,'Sắp khởi hành','T03','NV003');
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
INSERT INTO `khachhang` VALUES ('KH002','Trần','Hiếu','abcxyz 123','0123456789','2006-06-18'),('KH003','Trần','Hiếu','abc','0123456789','2026-03-29'),('KH004','t','t','abc','0815943949','2026-09-20'),('KH005','Tt','t','afd','0123456789','2026-09-20'),('KH006','Tran','Hieu','abc','0815943948','2026-10-15'),('KH007','tran','hieu','abc','0815943941','2026-10-14'),('KH008','Tran','Hieu','abc','0123456788','2026-10-26'),('KH009','Phan','Nguyen','89 Chu Van An','0999999999','2006-12-12'),('KH01','Nguyễn Huỳnh','Phúc','abcxyz','0123456789','2026-03-24');
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
INSERT INTO `loaitour` VALUES ('LT01','Tour trong nước','','Đang hoạt động'),('LT02','Tour nước ngoài','','Đang hoạt động'),('LT03','Tour khám phá','123','Đang hoạt động'),('LT04','abc','ac','Đang hoạt động');
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
INSERT INTO `nhanvien` VALUES ('NV003','Tiêu Viết','Vương','abc','0123456789','2026-03-29'),('NV004','Trần','Hiếu','abc','0123456789','2026-03-29'),('NV005','Tran','Hieu','abc','0815943949','2026-10-11'),('NV006','Tiêu','Vương','123 Điện Biên Phủ','0123456781','2004-07-01'),('NV01','Trần Viết','Hiếu','abc','0123456789','2006-01-01'),('NV02','Nguyễn','Phúc','abc','0123456789','2026-03-24'),('Trần','Trần','Viết','abc','0815943949','2026-09-19');
/*!40000 ALTER TABLE `nhanvien` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taikhoan`
--

LOCK TABLES `taikhoan` WRITE;
/*!40000 ALTER TABLE `taikhoan` DISABLE KEYS */;
INSERT INTO `taikhoan` VALUES (1,'admin','$2a$10$D6thL4xbsM/k959v3cWgoutLbgcBuiBdbZ06czcuqi3jidPUm.qiC','ADMIN','ACTIVE',NULL,NULL,NULL),(2,'NV003','$2a$10$BYkHcf0MEw0PyNEKGLlgKeUluRJ/yJhSlrnr.VGBrKKrIUtXBipMC','TOUR_OPERATE','ACTIVE','NV003',NULL,NULL),(3,'NV004','$2a$10$U8mNaWDVULgBCor90K6zdOsogDB.h6vpJs7ayzPo3fDyPh23YZtgK','ACCOUNTANT','ACTIVE','NV004',NULL,NULL),(7,'hieutran','$2a$10$Bt1eVkFiVmoC6LdWtQ0mBONWYijzEqqxuXgJv3YGaMH0oWLVIoz1m','CUSTOMER','ACTIVE',NULL,NULL,NULL),(9,'hieutran1','$2a$10$tgOC0IB1IPRw3QsSqedA0uRkgoKeK1WHMYweA3OHUyEy45tBENBYW','CUSTOMER','ACTIVE',NULL,NULL,NULL),(11,'NV005','$2a$10$zfDQgPT1kDDKs7GUhSxswO2NgDakJcGPnohURcI32q2G.GnsoL64i','TOUR_OPERATE','ACTIVE','NV005',NULL,NULL),(12,'hieutran123','$2a$10$Z7EvOoqtVc8x5c.YkhDwkOuei03UFTmmffoJRqE4aEJcmBxh9Vhhe','CUSTOMER','ACTIVE',NULL,'KH006',NULL),(13,'hieutran3','$2a$10$vfPkKYXNoXhbqhshx24aEuBXn.cxucqSpSc5MOXDaCJ612z26KFuG','CUSTOMER','ACTIVE',NULL,'KH007',NULL),(14,'Hieutran666','$2a$10$BLf25f66P4H/s4hh85GdG.dbKU4y7E1e68f6j1gPgK63wDvS2.xta','CUSTOMER','ACTIVE',NULL,'KH008','kh008@example.com'),(15,'tn','$2a$10$mw9kWd2egRhSKO4zMLgHu.9IKY2kdzmJ9L223LwTfDibeXBkSxmNm','CUSTOMER','ACTIVE',NULL,'KH009','kh009@example.com'),(16,'NV006','$2a$10$Zrq8Qw8lkyo8JkMEp8JUSu7n5gTMj9LnNAcHIZGFkrtZ2Daida.5q','ACCOUNTANT','ACTIVE','NV006',NULL,NULL);
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
INSERT INTO `taikhoan_quyen` VALUES (16,'MANAGE_CUSTOMER'),(16,'MANAGE_INVOICE'),(16,'MANAGE_PAYMENT'),(16,'MANAGE_TOUR'),(16,'VIEW_BOOKING'),(16,'VIEW_CUSTOMER'),(16,'VIEW_INVOICE'),(16,'VIEW_LOCATION'),(16,'VIEW_PAYMENT'),(16,'VIEW_PROMOTION'),(16,'VIEW_REVENUE_REPORT'),(16,'VIEW_STATISTICS'),(16,'VIEW_TOUR'),(16,'VIEW_TOUR_PLAN');
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
INSERT INTO `tour` VALUES ('T01','Du lịch Phú Quốc',7,5000000,27,'Hồ Chí Minh',NULL,'LT01',NULL),('T02','Du lịch Miền Tây',14,10000000,27,'Hồ Chí Minh',NULL,'LT01',NULL),('T04','Du lịch Thượng Hải',5,10000000,9,'Hồ Chí Minh',NULL,'LT01',NULL),('T06','Du lịch sapa ',6,9000000,50,'abc da lat','C:\\Users\\pc\\Pictures\\sapa.jpg','LT01','DD04'),('T07','Du lịch HCM',50,9000000,8000000,'abc','C:\\Users\\pc\\Pictures\\sapa.jpg','LT01','DD02'),('T08','Du lịch ',5,10000000,50,'Hồ Chí Minh ','abc ','LT01','DD01');
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tourbooking`
--

LOCK TABLES `tourbooking` WRITE;
/*!40000 ALTER TABLE `tourbooking` DISABLE KEYS */;
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
