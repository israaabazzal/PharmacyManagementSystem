-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: pharmacy_management_system
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Current Database: `pharmacy_management_system`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `pharmacy_management_system` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `pharmacy_management_system`;

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
INSERT INTO `admin_details` VALUES (1,'Lareine Bayan',1,1,1,NULL);
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
  KEY `resolved_by` (`resolved_by`),
  KEY `idx_alerts_resolved` (`is_resolved`),
  CONSTRAINT `alerts_ibfk_1` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`),
  CONSTRAINT `alerts_ibfk_2` FOREIGN KEY (`resolved_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alerts`
--

LOCK TABLES `alerts` WRITE;
/*!40000 ALTER TABLE `alerts` DISABLE KEYS */;
INSERT INTO `alerts` VALUES (1,5,'low_stock','Aspirin 100mg is low in stock (2)','high',0,NULL,NULL,'2025-07-29 19:46:55'),(2,5,'expiry','Aspirin 100mg will expire on 2025-08-03','high',0,NULL,NULL,'2025-07-29 19:46:55');
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
  KEY `user_id` (`user_id`),
  CONSTRAINT `audit_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `audit_log`
--

LOCK TABLES `audit_log` WRITE;
/*!40000 ALTER TABLE `audit_log` DISABLE KEYS */;
INSERT INTO `audit_log` VALUES (1,4,'Edit','User',1,NULL,NULL,NULL,'2025-07-23 21:19:29'),(2,4,'Add','User',0,NULL,NULL,NULL,'2025-07-24 08:28:38'),(3,4,'Delete','User',6,'',NULL,NULL,'2025-07-24 08:28:59'),(4,4,'Edit','User',3,NULL,NULL,NULL,'2025-07-24 08:29:18'),(5,4,'Add','User',0,NULL,NULL,NULL,'2025-07-24 13:58:47'),(6,4,'Delete','User',7,'',NULL,NULL,'2025-07-24 13:58:53'),(7,4,'Add','User',0,NULL,NULL,NULL,'2025-07-24 18:49:10'),(8,8,'ADD','Medicine',12,NULL,'Name=nn, Description=nn, Category=Antibiotic, Batch=AN, Expiry=2026-09-24, Qty=33, UnitPrice=1.0, SellingPrice=2.0',NULL,'2025-07-26 18:25:41'),(9,8,'DELETE','Medicine',12,'Name=nn',NULL,NULL,'2025-07-26 18:34:23'),(10,8,'Add','Patient',5,NULL,'fullName=zahraa Bazzal; dob=2001-4-20; gender=Female; address=234 bazzaliya st; phone=323-4777',NULL,'2025-07-27 18:15:40'),(11,8,'Updated patient','Patient',5,'fullName=zahraa Bazzal; dob=2001-04-20; gender=Female; address=234 bazzaliya st; phone=323-4777','fullName=zahraa Bazzal; dob=2001-04-2; gender=Female; address=234 bazzaliya st; phone=323-4777',NULL,'2025-07-28 15:49:37'),(12,8,'Update','Doctor',2,'Dr. Nabil Youssef','Dr. Nabil Youssef',NULL,'2025-07-28 15:49:50'),(13,1,'Edit','Prescription',1,NULL,NULL,NULL,'2025-07-28 15:55:14'),(14,8,'Updated patient','Patient',1,'fullName=John Doe; dob=1980-05-15; gender=Male; address=123 Main St; phone=555-1234','fullName=John Doe; dob=1980-05-1; gender=Male; address=123 Main St; phone=555-1234',NULL,'2025-07-28 15:57:24'),(15,8,'EDIT','Medicine',5,NULL,'Updated to: Name=Aspirin 100mg, Description=Blood thinner & pain reliever, Category=Painkiller, Batch=ASP100A, Expiry=2025-08-03, Qty=150, UnitPrice=0.2, SellingPrice=0.55',NULL,'2025-07-28 16:30:35'),(16,8,'Add','Doctor',0,NULL,'Dr.Israa Ali',NULL,'2025-07-28 17:04:27'),(17,4,'Add','User',0,NULL,NULL,NULL,'2025-07-28 17:08:29'),(18,8,'EDIT','Medicine',5,NULL,'Updated to: Name=Aspirin 100mg, Description=Blood thinner & pain reliever, Category=Painkiller, Batch=ASP100A, Expiry=2025-08-03, Qty=2, UnitPrice=0.2, SellingPrice=0.55',NULL,'2025-07-28 17:13:43'),(19,4,'Add','User',0,NULL,NULL,NULL,'2025-07-29 12:01:22');
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
  `performed_by` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`transaction_id`),
  KEY `medicine_id` (`medicine_id`),
  KEY `performed_by` (`performed_by`),
  CONSTRAINT `inventory_transactions_ibfk_1` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`medicine_id`),
  CONSTRAINT `inventory_transactions_ibfk_2` FOREIGN KEY (`performed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_transactions`
--

LOCK TABLES `inventory_transactions` WRITE;
/*!40000 ALTER TABLE `inventory_transactions` DISABLE KEYS */;
INSERT INTO `inventory_transactions` VALUES (1,1,'sale',10,1.00,10.00,1,'sale','Sale processed',10,'2025-07-29 18:15:12');
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
  `added_by` int NOT NULL,
  `last_updated_by` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`medicine_id`),
  KEY `category_id` (`category_id`),
  KEY `supplier_id` (`supplier_id`),
  KEY `added_by` (`added_by`),
  KEY `last_updated_by` (`last_updated_by`),
  KEY `idx_medicines_name` (`name`),
  KEY `idx_medicines_expiry` (`expiry_date`),
  CONSTRAINT `medicines_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `medicine_categories` (`category_id`),
  CONSTRAINT `medicines_ibfk_2` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`supplier_id`),
  CONSTRAINT `medicines_ibfk_3` FOREIGN KEY (`added_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `medicines_ibfk_4` FOREIGN KEY (`last_updated_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicines`
--

LOCK TABLES `medicines` WRITE;
/*!40000 ALTER TABLE `medicines` DISABLE KEYS */;
INSERT INTO `medicines` VALUES (1,'Panadol Extra','Fast pain relief tablets',2,'B123PAN','2026-01-15',90,0.50,1.00,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-29 18:15:12'),(2,'Amoxicillin 500mg','Antibiotic for bacterial infections',1,'AMOX2023','2025-11-10',80,0.30,0.80,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:26:09'),(3,'Ibuprofen 400mg','Pain reliever and anti-inflammatory',3,'IBU400X','2026-06-01',120,0.25,0.60,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:36:18'),(4,'Vitamin C 1000mg','Immune booster supplement',8,'VITC1000','2027-03-20',200,0.15,0.50,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:26:09'),(5,'Aspirin 100mg','Blood thinner & pain reliever',2,'ASP100A','2025-08-03',2,0.20,0.55,NULL,8,NULL,'2025-07-25 19:37:52','2025-07-28 17:13:43'),(6,'Paracetamol Syrup','Fever reducer for children',9,'PARASYR','2026-12-12',60,0.35,0.90,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:26:09'),(7,'Cough Syrup DX','Dry cough suppressant',6,'COUGHX5','2026-09-15',75,0.40,1.10,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:36:18'),(8,'Loratadine 10mg','Antihistamine for allergies',5,'LORA10Z','2027-04-25',90,0.22,0.70,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:36:18'),(9,'Metformin 850mg','Diabetes management tablet',7,'METF850','2026-02-18',110,0.45,1.20,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:36:18'),(10,'Omeprazole 20mg','Reduces stomach acid',3,'OMEPZ20','2025-10-05',130,0.28,0.85,NULL,2,NULL,'2025-07-25 19:37:52','2025-07-26 16:36:18');
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
  `created_by` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`patient_id`),
  KEY `created_by` (`created_by`),
  CONSTRAINT `patients_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`)
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescription_items`
--

LOCK TABLES `prescription_items` WRITE;
/*!40000 ALTER TABLE `prescription_items` DISABLE KEYS */;
INSERT INTO `prescription_items` VALUES (4,2,2,14,'500mg','3 times a day','Complete full course'),(5,2,4,30,'1000mg','Once daily','Supports immune system'),(6,2,10,10,'20mg','Before meals','For acid reflux'),(7,1,1,10,'500mg','Take 1 tablet every 6 hours','For pain and fever'),(8,1,3,20,'400mg','Take after meals','Avoid on empty stomach'),(9,1,5,15,'100mg','Once daily in the morning','Low-dose therapy');
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
  `created_by` int NOT NULL,
  `approved_by` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`prescription_id`),
  KEY `doctor_id` (`doctor_id`),
  KEY `created_by` (`created_by`),
  KEY `approved_by` (`approved_by`),
  KEY `idx_prescriptions_patient` (`patient_id`),
  KEY `idx_prescriptions_status` (`status`),
  CONSTRAINT `prescriptions_ibfk_1` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`patient_id`),
  CONSTRAINT `prescriptions_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`doctor_id`),
  CONSTRAINT `prescriptions_ibfk_3` FOREIGN KEY (`created_by`) REFERENCES `users` (`user_id`),
  CONSTRAINT `prescriptions_ibfk_4` FOREIGN KEY (`approved_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prescriptions`
--

LOCK TABLES `prescriptions` WRITE;
/*!40000 ALTER TABLE `prescriptions` DISABLE KEYS */;
INSERT INTO `prescriptions` VALUES (1,1,1,'2025-07-02','Treating fever and headache','completed',2,NULL,'2025-07-27 10:06:52','2025-07-29 18:15:12'),(2,2,3,'2025-07-26','Antibiotic and vitamin boost','active',2,NULL,'2025-07-27 10:06:52','2025-07-27 10:06:52');
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sale_items`
--

LOCK TABLES `sale_items` WRITE;
/*!40000 ALTER TABLE `sale_items` DISABLE KEYS */;
INSERT INTO `sale_items` VALUES (1,1,1,10,1.00,10.00);
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
  `processed_by` int NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`sale_id`),
  KEY `prescription_id` (`prescription_id`),
  KEY `patient_id` (`patient_id`),
  KEY `processed_by` (`processed_by`),
  KEY `idx_sales_date` (`created_at`),
  CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`prescription_id`) REFERENCES `prescriptions` (`prescription_id`),
  CONSTRAINT `sales_ibfk_2` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`patient_id`),
  CONSTRAINT `sales_ibfk_3` FOREIGN KEY (`processed_by`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales`
--

LOCK TABLES `sales` WRITE;
/*!40000 ALTER TABLE `sales` DISABLE KEYS */;
INSERT INTO `sales` VALUES (1,1,1,10.00,0.50,1.10,'cash','paid',10,'2025-07-29 18:15:12');
/*!40000 ALTER TABLE `sales` ENABLE KEYS */;
UNLOCK TABLES;

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
INSERT INTO `staff_details` VALUES (3,'Rayan Staff','Sales Associate',NULL,1,1,0);
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
  `added_by` int NOT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`supplier_id`),
  KEY `added_by` (`added_by`),
  CONSTRAINT `suppliers_ibfk_1` FOREIGN KEY (`added_by`) REFERENCES `users` (`user_id`)
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'lareine','$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqOqBd4r1V8dVS8ZR7zC.4d1J9XG','lareine@pharmacy.com','123456789',2,1,NULL,'2025-07-22 18:02:44','2025-07-23 21:19:27'),(2,'israa','$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqOqBd4r1V8dVS8ZR7zC.4d1J9XG','israa@pharmacy.com','987654321',2,1,NULL,'2025-07-22 18:02:44','2025-07-22 18:02:44'),(3,'rayan','$2a$10$N9qo8uLOickgx2ZMRZoMy.MqrqOqBd4r1V8dVS8ZR7zC.4d1J9XG','rayan@pharmacy.com','555555555',3,1,NULL,'2025-07-22 18:02:44','2025-07-22 18:02:44'),(4,'aya','aya2008','aya@pharmacy.com','987654325',1,1,NULL,'2025-07-23 12:09:11','2025-07-23 12:09:11'),(8,'me','me123','me@pharmacy.com','76683559',2,1,NULL,'2025-07-24 18:49:08','2025-07-24 18:49:08'),(9,'malak','malak','malak@gmail.com','81859108',1,1,NULL,'2025-07-28 17:08:27','2025-07-28 17:08:27'),(10,'sarah','sarah123','sarah@gmail.com','81716303',3,1,NULL,'2025-07-29 12:01:20','2025-07-29 12:01:20');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-30 20:06:30
