-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: pharmacy_management_system
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `admin_details`
--

DROP TABLE IF EXISTS `admin_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin_details` (
  `admin_id` int NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `can_manage_users` tinyint(1) DEFAULT '1',
  `can_backup_db` tinyint(1) DEFAULT '1',
  `can_configure_system` tinyint(1) DEFAULT '1',
  `notes` text,
  PRIMARY KEY (`admin_id`),
  CONSTRAINT `admin_details_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin_details`
--

LOCK TABLES `admin_details` WRITE;
/*!40000 ALTER TABLE `admin_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `alerts`
--

DROP TABLE IF EXISTS `alerts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alerts` (
  `alert_id` int NOT NULL AUTO_INCREMENT,
  `medicine_id` int DEFAULT NULL,
  `alert_type` enum('low_stock','expiry','recall','other') NOT NULL,
  `message` text NOT NULL,
  `severity` enum('low','medium','high') NOT NULL,
  `is_resolved` tinyint(1) DEFAULT '0',
  `resolved_by` int DEFAULT NULL,
  `resolved_at` timestamp NULL DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`alert_id`),
  KEY `medicine_id` (`medicine_id`),
  KEY `idx_alerts_resolved` (`is_resolved`),
  KEY `alerts_ibfk_2` (`resolved_by`),
  CONSTRAINT `alerts_ibfk_1` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`),
  CONSTRAINT `alerts_ibfk_2` FOREIGN KEY (`resolved_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alerts`
--

LOCK TABLES `alerts` WRITE;
/*!40000 ALTER TABLE `alerts` DISABLE KEYS */;
INSERT INTO `alerts` VALUES (1,5,'low_stock','Aspirin 100mg is low in stock (2)','high',1,NULL,'2025-08-15 21:42:35','2025-07-29 19:46:55'),(2,5,'expiry','Aspirin 100mg will expire on 2025-08-03','high',1,NULL,'2025-08-15 21:42:35','2025-07-29 19:46:55'),(3,5,'expiry','Medicine Aspirin 100mg is expiring soon','high',1,NULL,'2025-08-15 21:42:35','2025-08-07 13:38:54'),(4,5,'expiry','Medicine Aspirin 100mg is expiring soon','high',1,NULL,'2025-08-15 21:42:35','2025-08-07 13:39:05'),(5,5,'expiry','Medicine Aspirin 100mg is expiring soon','high',1,NULL,'2025-08-15 21:42:35','2025-08-15 16:57:02'),(6,5,'low_stock','Medicine stock is low','medium',1,NULL,'2025-08-15 21:42:35','2025-08-15 16:57:02'),(7,5,'expiry','Medicine Aspirin 100mg is expiring soon','high',1,NULL,'2025-08-15 21:42:35','2025-08-15 16:57:02'),(8,5,'expiry','Medicine Aspirin 100mg is expiring soon','high',1,NULL,'2025-08-15 21:42:35','2025-08-15 18:34:24'),(9,4,'low_stock','Vitamin C 1000mg is low in stock (6)','high',1,NULL,'2025-08-15 21:44:48','2025-08-15 21:42:57'),(10,7,'expiry','Medicine Cough Syrup DX is expiring soon','high',1,NULL,'2025-08-15 21:44:48','2025-08-15 21:43:12');
/*!40000 ALTER TABLE `alerts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `audit_log`
--

DROP TABLE IF EXISTS `audit_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `audit_log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `action` varchar(100) NOT NULL,
  `entity_type` varchar(50) DEFAULT NULL,
  `entity_id` int DEFAULT NULL,
  `old_values` text,
  `new_values` text,
  `ip_address` varchar(45) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`),
  KEY `audit_log_ibfk_1` (`user_id`),
  CONSTRAINT `audit_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
INSERT INTO `audit_log` VALUES (1,4,'Edit','User',1,NULL,NULL,NULL,'2025-07-23 21:19:29'),(2,4,'Add','User',0,NULL,NULL,NULL,'2025-07-24 08:28:38'),(3,4,'Delete','User',6,'',NULL,NULL,'2025-07-24 08:28:59'),(4,4,'Edit','User',3,NULL,NULL,NULL,'2025-07-24 08:29:18'),(5,4,'Add','User',0,NULL,NULL,NULL,'2025-07-24 13:58:47'),(6,4,'Delete','User',7,'',NULL,NULL,'2025-07-24 13:58:53'),(7,4,'Add','User',0,NULL,NULL,NULL,'2025-07-24 18:49:10'),(8,8,'ADD','Medicine',12,NULL,'Name=nn, Description=nn, Category=Antibiotic, Batch=AN, Expiry=2026-09-24, Qty=33, UnitPrice=1.0, SellingPrice=2.0',NULL,'2025-07-26 18:25:41'),(9,8,'DELETE','Medicine',12,'Name=nn',NULL,NULL,'2025-07-26 18:34:23'),(10,8,'Add','Patient',5,NULL,'fullName=zahraa Bazzal; dob=2001-4-20; gender=Female; address=234 bazzaliya st; phone=323-4777',NULL,'2025-07-27 18:15:40'),(11,8,'Updated patient','Patient',5,'fullName=zahraa Bazzal; dob=2001-04-20; gender=Female; address=234 bazzaliya st; phone=323-4777','fullName=zahraa Bazzal; dob=2001-04-2; gender=Female; address=234 bazzaliya st; phone=323-4777',NULL,'2025-07-28 15:49:37'),(12,8,'Update','Doctor',2,'Dr. Nabil Youssef','Dr. Nabil Youssef',NULL,'2025-07-28 15:49:50'),(13,NULL,'Edit','Prescription',1,NULL,NULL,NULL,'2025-07-28 15:55:14'),(14,8,'Updated patient','Patient',1,'fullName=John Doe; dob=1980-05-15; gender=Male; address=123 Main St; phone=555-1234','fullName=John Doe; dob=1980-05-1; gender=Male; address=123 Main St; phone=555-1234',NULL,'2025-07-28 15:57:24'),(15,8,'EDIT','Medicine',5,NULL,'Updated to: Name=Aspirin 100mg, Description=Blood thinner & pain reliever, Category=Painkiller, Batch=ASP100A, Expiry=2025-08-03, Qty=150, UnitPrice=0.2, SellingPrice=0.55',NULL,'2025-07-28 16:30:35'),(16,8,'Add','Doctor',0,NULL,'Dr.Israa Ali',NULL,'2025-07-28 17:04:27'),(17,4,'Add','User',0,NULL,NULL,NULL,'2025-07-28 17:08:29'),(18,8,'EDIT','Medicine',5,NULL,'Updated to: Name=Aspirin 100mg, Description=Blood thinner & pain reliever, Category=Painkiller, Batch=ASP100A, Expiry=2025-08-03, Qty=2, UnitPrice=0.2, SellingPrice=0.55',NULL,'2025-07-28 17:13:43'),(19,4,'Add','User',0,NULL,NULL,NULL,'2025-07-29 12:01:22'),(20,8,'EDIT','Medicine',3,NULL,'Updated to: Name=Ibuprofen 400mg, Description=Pain reliever and anti-inflammatory, Category=Antacid, Batch=IBU400X, Expiry=2026-06-01, Qty=300, UnitPrice=0.25, SellingPrice=0.6',NULL,'2025-07-30 20:54:34'),(21,8,'Add','Prescription',3,NULL,NULL,NULL,'2025-08-07 13:11:48'),(22,8,'EDIT','Medicine',5,NULL,'Updated to: Name=Aspirin 100mg, Description=Blood thinner & pain reliever, Category=Painkiller, Batch=ASP100A, Expiry=2025-08-03, Qty=-5, UnitPrice=0.2, SellingPrice=0.55',NULL,'2025-08-07 13:38:54'),(23,8,'EDIT','Medicine',5,NULL,'Updated to: Name=Aspirin 100mg, Description=Blood thinner & pain reliever, Category=Painkiller, Batch=ASP100A, Expiry=2025-08-03, Qty=9, UnitPrice=0.2, SellingPrice=0.55',NULL,'2025-08-07 13:39:05'),(24,4,'Edit','User',3,NULL,NULL,NULL,'2025-08-13 11:00:57'),(25,4,'Edit','User',3,NULL,NULL,NULL,'2025-08-13 11:02:49'),(26,4,'Edit','User',3,NULL,NULL,NULL,'2025-08-13 11:03:07'),(27,4,'Edit','User',3,NULL,NULL,NULL,'2025-08-13 11:07:48'),(28,4,'Add','User',0,NULL,NULL,NULL,'2025-08-13 11:09:40'),(29,4,'Edit','User',11,NULL,NULL,NULL,'2025-08-13 11:09:51'),(30,NULL,'Add','Prescription',4,NULL,NULL,NULL,'2025-08-15 12:38:38'),(35,8,'Add','Prescription',6,NULL,NULL,NULL,'2025-08-15 17:38:45'),(36,8,'Add','Prescription',7,NULL,NULL,NULL,'2025-08-15 17:39:46'),(37,8,'EDIT','Medicine',5,NULL,'Updated to: Name=Aspirin 100mg, Description=Blood thinner & pain reliever, Category=Painkiller, Batch=ASP100A, Expiry=2025-08-03, Qty=88, UnitPrice=0.2, SellingPrice=0.55',NULL,'2025-08-15 18:34:24'),(38,8,'EDIT','Medicine',5,NULL,'Updated to: Name=Aspirin 100mg, Description=Blood thinner & pain reliever, Category=Painkiller, Batch=ASP100A, Expiry=2026-08-03, Qty=88, UnitPrice=0.2, SellingPrice=0.55',NULL,'2025-08-15 18:35:05'),(39,8,'Edit','Prescription',4,NULL,NULL,NULL,'2025-08-15 19:03:19'),(40,8,'Edit','Prescription',2,NULL,NULL,NULL,'2025-08-15 19:16:50'),(41,8,'Edit','Prescription',1,NULL,NULL,NULL,'2025-08-15 19:18:05'),(42,8,'Edit','Prescription',1,NULL,NULL,NULL,'2025-08-15 19:21:17'),(43,8,'Edit','Prescription',6,NULL,NULL,NULL,'2025-08-15 19:34:44'),(44,8,'Edit','Prescription',4,NULL,NULL,NULL,'2025-08-15 19:42:05'),(45,8,'Edit','Prescription',4,NULL,NULL,NULL,'2025-08-15 19:46:02'),(46,8,'Edit','Prescription',3,NULL,NULL,NULL,'2025-08-15 19:47:37'),(47,8,'Edit','Prescription',6,NULL,NULL,NULL,'2025-08-15 21:08:00'),(48,8,'EDIT','Medicine',4,NULL,'Updated to: Name=Vitamin C 1000mg, Description=Immune booster supplement, Category=Vitamin, Batch=VITC1000, Expiry=2027-03-20, Qty=6, UnitPrice=0.15, SellingPrice=0.5',NULL,'2025-08-15 21:42:54'),(49,8,'EDIT','Medicine',7,NULL,'Updated to: Name=Cough Syrup DX, Description=Dry cough suppressant, Category=Cough Syrup, Batch=COUGHX5, Expiry=2025-09-15, Qty=58, UnitPrice=0.4, SellingPrice=1.1',NULL,'2025-08-15 21:43:12'),(50,8,'EDIT','Medicine',4,NULL,'Updated to: Name=Vitamin C 1000mg, Description=Immune booster supplement, Category=Vitamin, Batch=VITC1000, Expiry=2027-03-20, Qty=108, UnitPrice=0.15, SellingPrice=0.5',NULL,'2025-08-15 21:44:31'),(51,8,'EDIT','Medicine',7,NULL,'Updated to: Name=Cough Syrup DX, Description=Dry cough suppressant, Category=Cough Syrup, Batch=COUGHX5, Expiry=2027-09-15, Qty=58, UnitPrice=0.4, SellingPrice=1.1',NULL,'2025-08-15 21:44:42'),(52,8,'Edit','Prescription',7,NULL,NULL,NULL,'2025-08-16 17:47:48'),(53,8,'Edit','Prescription',4,NULL,NULL,NULL,'2025-08-16 17:49:50'),(54,4,'Delete','User',11,'testactivation',NULL,NULL,'2026-07-19 11:11:32'),(55,4,'Edit','User',1,NULL,NULL,NULL,'2026-07-19 11:26:00'),(56,4,'Delete','User',1,'lareine',NULL,NULL,'2026-07-19 11:36:36'),(57,4,'Delete','User',3,'rayan',NULL,NULL,'2026-07-19 11:36:46');
/*!40000 ALTER TABLE `audit_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctors` (
  `doctor_id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `specialization` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `license_number` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`doctor_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (1,'Dr. Sara Khalil','General Practitioner','555-1111','sara.khalil@hospital.com','DOC001','2025-07-27 10:06:52'),(2,'Dr. Nabil Youssef','Cardiologist','555-2225','nabil.youssef@hospital.com','DOC002','2025-07-27 10:06:52'),(3,'Dr. Leila Ahmad','Pediatrician','555-3333','leila.ahmad@hospital.com','DOC003','2025-07-27 10:06:52'),(4,'Dr. Omar Fawzi','Dermatologist','555-4444','omar.fawzi@hospital.com','DOC004','2025-07-27 10:06:52'),(5,'Dr. Maha Tarek','Neurologist','555-5555','maha.tarek@hospital.com','DOC005','2025-07-27 10:06:52'),(6,'Dr.Israa Ali','Cardiologist','76683559','israaali@gmail.com','DOC119','2025-07-28 17:04:27');
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `function_permissions`
--

DROP TABLE IF EXISTS `function_permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `function_permissions` (
  `permission_id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `function_name` varchar(50) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `can_access` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`permission_id`),
  UNIQUE KEY `role_id` (`role_id`,`function_name`),
  CONSTRAINT `function_permissions_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `function_permissions`
--

LOCK TABLES `function_permissions` WRITE;
/*!40000 ALTER TABLE `function_permissions` DISABLE KEYS */;
INSERT INTO `function_permissions` VALUES (1,1,'user_management','Manage all system users',1,'2025-07-22 18:02:44'),(2,1,'system_config','Configure system settings',1,'2025-07-22 18:02:44'),(3,1,'db_backup','Perform database backups',1,'2025-07-22 18:02:44'),(4,2,'medicine_management','Add/edit medicines',1,'2025-07-22 18:02:44'),(5,2,'prescription_approval','Approve prescriptions',1,'2025-07-22 18:02:44'),(6,2,'inventory_control','Manage inventory levels',1,'2025-07-22 18:02:44'),(7,3,'process_sales','Process customer sales',1,'2025-07-22 18:02:44'),(8,3,'view_inventory','View current inventory',1,'2025-07-22 18:02:44');
/*!40000 ALTER TABLE `function_permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_transactions`
--

DROP TABLE IF EXISTS `inventory_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_transactions` (
  `transaction_id` int NOT NULL AUTO_INCREMENT,
  `medicine_id` int NOT NULL,
  `transaction_type` enum('purchase','sale','adjustment','return','expired') NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `total_value` decimal(10,2) DEFAULT NULL,
  `reference_id` int DEFAULT NULL,
  `reference_type` varchar(50) DEFAULT NULL,
  `notes` text,
  `performed_by` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`transaction_id`),
  KEY `medicine_id` (`medicine_id`),
  KEY `inventory_transactions_ibfk_2` (`performed_by`),
  CONSTRAINT `inventory_transactions_ibfk_1` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`),
  CONSTRAINT `inventory_transactions_ibfk_2` FOREIGN KEY (`performed_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_transactions`
--

LOCK TABLES `inventory_transactions` WRITE;
/*!40000 ALTER TABLE `inventory_transactions` DISABLE KEYS */;
INSERT INTO `inventory_transactions` VALUES (1,1,'sale',10,1.00,10.00,1,'sale','Sale processed',10,'2025-07-29 18:15:12'),(2,1,'sale',4,1.00,4.00,2,'sale','Sale processed',8,'2025-08-07 14:10:53'),(3,6,'sale',4,0.90,3.60,3,'sale','Sale processed',8,'2025-08-09 18:08:57'),(4,5,'sale',4,0.55,2.20,4,'sale','Sale processed',NULL,'2025-08-15 16:57:02'),(5,6,'sale',2,0.90,1.80,6,'sale','Sale processed',8,'2025-08-15 17:40:09'),(6,7,'sale',2,1.10,2.20,8,'sale','Sale processed',8,'2025-08-15 18:29:38'),(7,5,'sale',2,0.55,1.10,9,'sale','Sale processed',8,'2025-08-15 19:03:54'),(8,4,'sale',30,0.50,15.00,10,'sale','Sale processed',8,'2025-08-15 19:17:19'),(9,1,'sale',10,1.00,10.00,11,'sale','Sale processed',8,'2025-08-15 19:18:26'),(10,1,'sale',10,1.00,10.00,12,'sale','Sale processed',8,'2025-08-15 19:21:45'),(11,7,'sale',1,1.10,1.10,13,'sale','Sale processed',8,'2025-08-15 19:35:10'),(12,1,'sale',3,1.00,3.00,15,'sale','Sale processed',8,'2025-08-15 19:46:24'),(13,7,'sale',4,1.10,4.40,16,'sale','Sale processed',8,'2025-08-15 19:48:33'),(14,5,'sale',5,0.55,2.75,17,'sale','Sale processed',8,'2025-08-15 20:14:37'),(15,7,'sale',3,1.10,3.30,18,'sale','Sale processed',8,'2025-08-15 21:08:35'),(16,6,'sale',4,0.90,3.60,19,'sale','Sale processed',8,'2025-08-16 17:48:44'),(17,5,'sale',2,0.55,1.10,20,'sale','Sale processed',8,'2025-08-16 17:50:16');
/*!40000 ALTER TABLE `inventory_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicine_categories`
--

DROP TABLE IF EXISTS `medicine_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicine_categories` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicine_categories`
--

LOCK TABLES `medicine_categories` WRITE;
/*!40000 ALTER TABLE `medicine_categories` DISABLE KEYS */;
INSERT INTO `medicine_categories` VALUES (1,'Antibiotic','Used to treat bacterial infections','2025-07-26 16:15:33'),(2,'Painkiller','Used for pain relief','2025-07-26 16:15:33'),(3,'Antacid','Reduces stomach acid','2025-07-26 16:15:33'),(4,'Supplement','Boosts nutritional intake','2025-07-26 16:15:33'),(5,'Antihistamine','Relieves allergies','2025-07-26 16:15:33'),(6,'Cough Syrup','Soothes dry or productive coughs','2025-07-26 16:15:33'),(7,'Antidiabetic','Controls blood sugar levels','2025-07-26 16:15:33'),(8,'Vitamin','Improves overall health and immunity','2025-07-26 16:15:33'),(9,'Antipyretic','Reduces fever','2025-07-26 16:15:33');
/*!40000 ALTER TABLE `medicine_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `medicine_statistics`
--

DROP TABLE IF EXISTS `medicine_statistics`;
/*!50001 DROP VIEW IF EXISTS `medicine_statistics`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `medicine_statistics` AS SELECT 
 1 AS `medicine_id`,
 1 AS `medicine_name`,
 1 AS `current_stock`,
 1 AS `expiry_date`,
 1 AS `low_stock`,
 1 AS `expiring_soon`,
 1 AS `total_sold`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `medicines`
--

DROP TABLE IF EXISTS `medicines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicines` (
  `medicine_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  `category_id` int NOT NULL,
  `batch_number` varchar(50) NOT NULL,
  `expiry_date` date NOT NULL,
  `quantity` int NOT NULL DEFAULT '0',
  `unit_price` decimal(10,2) NOT NULL,
  `selling_price` decimal(10,2) NOT NULL,
  `supplier_id` int DEFAULT NULL,
  `added_by` int DEFAULT NULL,
  `last_updated_by` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`medicine_id`),
  KEY `category_id` (`category_id`),
  KEY `supplier_id` (`supplier_id`),
  KEY `idx_medicines_name` (`name`),
  KEY `idx_medicines_expiry` (`expiry_date`),
  KEY `medicines_ibfk_3` (`added_by`),
  KEY `medicines_ibfk_4` (`last_updated_by`),
  CONSTRAINT `medicines_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `medicine_categories` (`category_id`),
  CONSTRAINT `medicines_ibfk_2` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`),
  CONSTRAINT `medicines_ibfk_3` FOREIGN KEY (`added_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL,
  CONSTRAINT `medicines_ibfk_4` FOREIGN KEY (`last_updated_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicines`
--

LOCK TABLES `medicines` WRITE;
/*!40000 ALTER TABLE `medicines` DISABLE KEYS */;
INSERT INTO `medicines` VALUES (1,'Panadol Extra','Fast pain relief tablets',2,'B123PAN','2026-01-15',36,0.50,1.00,NULL,2,NULL,'2025-07-25 19:37:52','2025-08-15 19:46:24'),(2,'Amoxicillin 500mg','Antibiotic for bacterial infections',1,'AMOX2023','2025-11-10',80,0.30,0.80,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:26:09'),(3,'Ibuprofen 400mg','Pain reliever and anti-inflammatory',3,'IBU400X','2026-06-01',300,0.25,0.60,NULL,8,NULL,'2025-07-25 19:37:52','2025-07-30 20:54:34'),(4,'Vitamin C 1000mg','Immune booster supplement',8,'VITC1000','2027-03-20',108,0.15,0.50,NULL,8,NULL,'2025-07-25 19:37:52','2025-08-15 21:44:31'),(5,'Aspirin 100mg','Blood thinner & pain reliever',2,'ASP100A','2026-08-03',72,0.20,0.55,NULL,8,NULL,'2025-07-25 19:37:52','2025-08-16 17:50:16'),(6,'Paracetamol Syrup','Fever reducer for children',9,'PARASYR','2026-12-12',44,0.35,0.90,NULL,2,NULL,'2025-07-25 19:37:52','2025-08-16 17:48:44'),(7,'Cough Syrup DX','Dry cough suppressant',6,'COUGHX5','2027-09-15',58,0.40,1.10,NULL,8,NULL,'2025-07-25 19:37:52','2025-08-15 21:44:42'),(8,'Loratadine 10mg','Antihistamine for allergies',5,'LORA10Z','2027-04-25',90,0.22,0.70,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:36:18'),(9,'Metformin 850mg','Diabetes management tablet',7,'METF850','2026-02-18',110,0.45,1.20,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:36:18'),(10,'Omeprazole 20mg','Reduces stomach acid',3,'OMEPZ20','2025-10-05',130,0.28,0.85,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:36:18');
/*!40000 ALTER TABLE `medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patients` (
  `patient_id` int NOT NULL AUTO_INCREMENT,
  `full_name` varchar(100) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `gender` enum('Male','Female','Other') DEFAULT NULL,
  `address` text,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `medical_history` text,
  `created_by` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`patient_id`),
  KEY `patients_ibfk_1` (`created_by`),
  CONSTRAINT `patients_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (1,'John Doe','1980-05-01','Male','123 Main St','555-1234',NULL,NULL,2,'2025-07-27 10:06:52','2025-07-28 15:57:24'),(2,'Jane Smith','1992-08-20','Female','456 Elm St','555-5678','jane@example.com','Asthma',2,'2025-07-27 10:06:52','2025-07-27 10:06:52'),(3,'Ali Hasan','1975-02-10','Male','789 Oak St','555-9012','ali@example.com','Hypertension',2,'2025-07-27 10:06:52','2025-07-27 10:06:52'),(5,'zahraa Bazzal','2001-04-02','Female','234 bazzaliya st','323-4777',NULL,NULL,8,'2025-07-27 18:15:40','2025-07-28 15:49:37');
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pharmacist_details`
--

DROP TABLE IF EXISTS `pharmacist_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pharmacist_details` (
  `pharmacist_id` int NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `license_number` varchar(50) NOT NULL,
  `specialization` varchar(100) DEFAULT NULL,
  `can_manage_medicines` tinyint(1) DEFAULT '1',
  `can_manage_prescriptions` tinyint(1) DEFAULT '1',
  `can_approve_orders` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`pharmacist_id`),
  UNIQUE KEY `license_number` (`license_number`),
  CONSTRAINT `pharmacist_details_ibfk_1` FOREIGN KEY (`pharmacist_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pharmacist_details`
--

LOCK TABLES `pharmacist_details` WRITE;
/*!40000 ALTER TABLE `pharmacist_details` DISABLE KEYS */;
INSERT INTO `pharmacist_details` VALUES (2,'Israa Bazzal','PHARM12345',NULL,1,1,1);
/*!40000 ALTER TABLE `pharmacist_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescription_items`
--

DROP TABLE IF EXISTS `prescription_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescription_items` (
  `item_id` int NOT NULL AUTO_INCREMENT,
  `prescription_id` int NOT NULL,
  `medicine_id` int NOT NULL,
  `quantity` int NOT NULL,
  `dosage` text,
  `instructions` text,
  `notes` text,
  PRIMARY KEY (`item_id`),
  KEY `prescription_id` (`prescription_id`),
  KEY `medicine_id` (`medicine_id`),
  CONSTRAINT `prescription_items_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`prescription_id`),
  CONSTRAINT `prescription_items_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription_items`
--

LOCK TABLES `prescription_items` WRITE;
/*!40000 ALTER TABLE `prescription_items` DISABLE KEYS */;
INSERT INTO `prescription_items` VALUES (19,2,2,14,'500mg','3 times a day','Complete full course'),(20,2,4,30,'1000mg','Once daily','Supports immune system'),(21,2,10,10,'20mg','Before meals','For acid reflux'),(25,1,1,10,'500mg','Take 1 tablet every 6 hours','For pain and fever'),(26,1,3,20,'400mg','Take after meals','Avoid on empty stomach'),(27,1,5,15,'100mg','Once daily in the morning','Low-dose therapy'),(31,3,7,2,'10mg','no','no'),(32,6,7,4,'300mg','dd','dd'),(33,7,6,2,'400mg','att','w'),(34,4,5,2,'600mg','after lunch','...');
/*!40000 ALTER TABLE `prescription_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prescriptions`
--

DROP TABLE IF EXISTS `prescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prescriptions` (
  `prescription_id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int NOT NULL,
  `doctor_id` int NOT NULL,
  `prescription_date` date NOT NULL,
  `notes` text,
  `status` enum('draft','active','completed','cancelled') DEFAULT 'draft',
  `created_by` int DEFAULT NULL,
  `approved_by` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`prescription_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `idx_prescriptions_patient` (`patient_id`),
  KEY `idx_prescriptions_status` (`status`),
  KEY `prescriptions_ibfk_3` (`created_by`),
  KEY `prescriptions_ibfk_4` (`approved_by`),
  CONSTRAINT `prescriptions_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`patient_id`),
  CONSTRAINT `prescriptions_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`doctor_id`),
  CONSTRAINT `prescriptions_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL,
  CONSTRAINT `prescriptions_ibfk_4` FOREIGN KEY (`approved_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (1,1,1,'2025-07-02','Treating fever and headache','completed',2,NULL,'2025-07-27 10:06:52','2025-08-15 19:21:45'),(2,2,3,'2025-07-26','Antibiotic and vitamin boost','completed',2,NULL,'2025-07-27 10:06:52','2025-08-15 19:17:19'),(3,5,1,'2025-08-07','no notes','completed',8,NULL,'2025-08-07 13:11:46','2025-08-15 19:48:33'),(4,5,2,'2025-08-15',',,,,,,,','completed',NULL,NULL,'2025-08-15 12:38:36','2025-08-16 17:50:16'),(6,1,3,'2025-08-15','xx','completed',8,NULL,'2025-08-15 17:38:44','2025-08-15 21:08:35'),(7,1,3,'2025-08-15','dd','completed',8,NULL,'2025-08-15 17:39:45','2025-08-16 17:48:44');
/*!40000 ALTER TABLE `prescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `role_id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'admin','System administrator with full access','2025-07-22 18:02:43'),(2,'pharmacist','Can manage medicines and prescriptions','2025-07-22 18:02:43'),(3,'staff','Can view inventory and process sales','2025-07-22 18:02:43'),(4,'inactive','Deactivated accounts','2025-07-22 18:02:43');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sale_items`
--

DROP TABLE IF EXISTS `sale_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sale_items` (
  `sale_item_id` int NOT NULL AUTO_INCREMENT,
  `sale_id` int NOT NULL,
  `medicine_id` int NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `total_price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`sale_item_id`),
  KEY `sale_id` (`sale_id`),
  KEY `medicine_id` (`medicine_id`),
  CONSTRAINT `sale_items_ibfk_1` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`sale_id`),
  CONSTRAINT `sale_items_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sale_items`
--

LOCK TABLES `sale_items` WRITE;
/*!40000 ALTER TABLE `sale_items` DISABLE KEYS */;
INSERT INTO `sale_items` VALUES (1,1,1,10,1.00,10.00),(2,2,1,4,1.00,4.00),(3,3,6,4,0.90,3.60),(4,4,5,4,0.55,2.20),(5,6,6,2,0.90,1.80),(6,8,7,2,1.10,2.20),(7,9,5,2,0.55,1.10),(8,10,4,30,0.50,15.00),(9,11,1,10,1.00,10.00),(10,12,1,10,1.00,10.00),(11,13,7,1,1.10,1.10),(12,15,1,3,1.00,3.00),(13,16,7,4,1.10,4.40),(14,17,5,5,0.55,2.75),(15,18,7,3,1.10,3.30),(16,19,6,4,0.90,3.60),(17,20,5,2,0.55,1.10);
/*!40000 ALTER TABLE `sale_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales`
--

DROP TABLE IF EXISTS `sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales` (
  `sale_id` int NOT NULL AUTO_INCREMENT,
  `prescription_id` int DEFAULT NULL,
  `patient_id` int DEFAULT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `discount` decimal(10,2) DEFAULT '0.00',
  `tax_amount` decimal(10,2) DEFAULT '0.00',
  `payment_method` enum('cash','credit_card','insurance','other') NOT NULL,
  `payment_status` enum('pending','paid','partially_paid') DEFAULT 'paid',
  `processed_by` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`sale_id`),
  KEY `prescription_id` (`prescription_id`),
  KEY `patient_id` (`patient_id`),
  KEY `idx_sales_date` (`created_at`),
  KEY `sales_ibfk_3` (`processed_by`),
  CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`prescription_id`),
  CONSTRAINT `sales_ibfk_2` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`patient_id`),
  CONSTRAINT `sales_ibfk_3` FOREIGN KEY (`processed_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,1,1,10.00,0.50,1.10,'cash','paid',10,'2025-07-29 18:15:12'),(2,NULL,3,4.00,0.20,0.44,'cash','paid',8,'2025-08-07 14:10:53'),(3,2,2,3.60,0.18,0.40,'cash','paid',8,'2025-08-09 18:08:57'),(4,4,5,2.20,0.11,0.24,'cash','paid',NULL,'2025-08-15 16:57:02'),(6,7,1,1.80,0.09,0.20,'cash','paid',8,'2025-08-15 17:40:09'),(8,6,1,2.20,0.11,0.24,'cash','paid',8,'2025-08-15 18:29:38'),(9,4,5,1.10,0.06,0.12,'cash','paid',8,'2025-08-15 19:03:54'),(10,2,2,15.00,0.75,1.65,'cash','paid',8,'2025-08-15 19:17:19'),(11,1,1,10.00,0.50,1.10,'cash','paid',8,'2025-08-15 19:18:26'),(12,1,1,10.00,0.50,1.10,'cash','paid',8,'2025-08-15 19:21:45'),(13,6,1,5.50,0.28,0.61,'cash','paid',8,'2025-08-15 19:35:10'),(14,4,5,2.75,0.14,0.30,'cash','paid',8,'2025-08-15 19:42:45'),(15,NULL,5,3.00,0.15,0.33,'cash','paid',8,'2025-08-15 19:46:24'),(16,3,5,4.40,0.22,0.48,'cash','paid',8,'2025-08-15 19:48:33'),(17,4,5,2.75,0.14,0.30,'cash','paid',8,'2025-08-15 20:14:37'),(18,6,1,3.30,0.17,0.36,'cash','paid',8,'2025-08-15 21:08:35'),(19,7,1,3.60,0.18,0.40,'cash','paid',8,'2025-08-16 17:48:44'),(20,4,5,1.10,0.06,0.12,'cash','paid',8,'2025-08-16 17:50:16');
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `sales_summary`
--

DROP TABLE IF EXISTS `sales_summary`;
/*!50001 DROP VIEW IF EXISTS `sales_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `sales_summary` AS SELECT 
 1 AS `sale_id`,
 1 AS `patient_name`,
 1 AS `processed_by`,
 1 AS `total_amount`,
 1 AS `discount`,
 1 AS `tax_amount`,
 1 AS `payment_method`,
 1 AS `created_at`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `staff_details`
--

DROP TABLE IF EXISTS `staff_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `staff_details` (
  `staff_id` int NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `position` varchar(50) NOT NULL,
  `department` varchar(50) DEFAULT NULL,
  `can_process_sales` tinyint(1) DEFAULT '1',
  `can_view_inventory` tinyint(1) DEFAULT '1',
  `can_print_reports` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`staff_id`),
  CONSTRAINT `staff_details_ibfk_1` FOREIGN KEY (`staff_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `staff_details`
--

LOCK TABLES `staff_details` WRITE;
/*!40000 ALTER TABLE `staff_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `staff_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `suppliers`
--

DROP TABLE IF EXISTS `suppliers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `suppliers` (
  `supplier_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) NOT NULL,
  `address` text,
  `added_by` int DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`supplier_id`),
  KEY `suppliers_ibfk_1` (`added_by`),
  CONSTRAINT `suppliers_ibfk_1` FOREIGN KEY (`added_by`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `role_id` int NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `last_login` datetime DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (2,'israa','$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqOqBd4r1V8dVS8ZR7zC.4d1J9XG','israa@pharmacy.com','987654321',2,1,NULL,'2025-07-22 18:02:44','2025-07-22 18:02:44'),(4,'aya','aya2008','aya@pharmacy.com','987654325',1,1,NULL,'2025-07-23 12:09:11','2025-07-23 12:09:11'),(8,'me','me123','me@pharmacy.com','76683559',2,1,NULL,'2025-07-24 18:49:08','2025-07-24 18:49:08'),(9,'malak','malak','malak@gmail.com','81859108',1,1,NULL,'2025-07-28 17:08:27','2025-07-28 17:08:27'),(10,'sarah','sarah123','sarah@gmail.com','81716303',3,1,NULL,'2025-07-29 12:01:20','2025-07-29 12:01:20');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `medicine_statistics`
--

/*!50001 DROP VIEW IF EXISTS `medicine_statistics`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `medicine_statistics` AS select `m`.`medicine_id` AS `medicine_id`,`m`.`name` AS `medicine_name`,`m`.`quantity` AS `current_stock`,`m`.`expiry_date` AS `expiry_date`,if((`m`.`quantity` < 10),'YES','NO') AS `low_stock`,if((`m`.`expiry_date` <= (curdate() + interval 30 day)),'YES','NO') AS `expiring_soon`,ifnull(sum(`si`.`quantity`),0) AS `total_sold` from (`medicines` `m` left join `sale_items` `si` on((`m`.`medicine_id` = `si`.`medicine_id`))) group by `m`.`medicine_id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sales_summary`
--

/*!50001 DROP VIEW IF EXISTS `sales_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `sales_summary` AS select `s`.`sale_id` AS `sale_id`,`p`.`full_name` AS `patient_name`,`u`.`username` AS `processed_by`,`s`.`total_amount` AS `total_amount`,`s`.`discount` AS `discount`,`s`.`tax_amount` AS `tax_amount`,`s`.`payment_method` AS `payment_method`,`s`.`created_at` AS `created_at` from ((`sales` `s` left join `patients` `p` on((`s`.`patient_id` = `p`.`patient_id`))) left join `users` `u` on((`s`.`processed_by` = `u`.`user_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-19 14:53:31
