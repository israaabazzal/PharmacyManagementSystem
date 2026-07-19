# 💊 Pharmacy Management System

A desktop-based **Pharmacy Management System** developed using **Java Swing** and **MySQL**. The system provides an efficient solution for managing pharmacy operations, including medicine inventory, sales, prescriptions, patients, suppliers, user accounts, and reporting through a user-friendly graphical interface.

---

## 📖 Overview

The Pharmacy Management System is designed to simplify daily pharmacy operations by integrating inventory management, prescription processing, sales tracking, and user administration into a single desktop application. It helps maintain accurate stock levels, organize patient and supplier information, and improve workflow efficiency.

---

# ✨ Features

## 🔐 User Management
- Secure user login
- Role-based access (Administrator and Pharmacist)
- Add, edit, deactivate, and delete users
- Administrator and pharmacist profile management

## 💊 Medicine Management
- Add new medicines
- Edit medicine information
- Delete medicines
- Medicine categorization
- Stock quantity management
- Expiry date management
- Low stock monitoring

## 📦 Inventory Management
- Inventory transaction history
- Purchase records
- Sales records
- Stock adjustments
- Automatic inventory updates

## 🏥 Patient Management
- Register new patients
- Update patient information
- View patient records

## 🩺 Prescription Management
- Create prescriptions
- Manage prescription items
- Link prescriptions to patients and doctors
- Prescription approval

## 🚚 Supplier Management
- Add suppliers
- Update supplier information
- Manage supplier records

## 💰 Sales Management
- Process medicine sales
- Automatic stock deduction
- Sales history
- Sales transaction records

## 🔔 Alerts
- Low stock notifications
- Expired medicine notifications

## 📊 Dashboard & Reports
- Pharmacy statistics
- Inventory overview
- Sales reporting

---

# 🛠 Technologies Used

- Java
- Java Swing
- JDBC
- MySQL 8
- NetBeans IDE
- FlatLaf
- JFreeChart

---

# 📂 Project Structure

```
PharmacyManagementSystem
│
├── database/
│   └── pharmacy_management.sql
│
├── dist/
│   ├── PharmacyManagementSystem.jar
│   └── lib/
│
├── nbproject/
│
├── screenshots/
│
├── src/
│   ├── assets/
│   ├── com/
│   │   └── pharmacy/
│   │       ├── dao/
│   │       ├── models/
│   │       ├── utils/
│   │       └── views/
│   └── pharmacymanagementsystem/
│
├── README.md
├── build.xml
├── manifest.mf
└── .gitignore
```

---

# 🗄 Database

The SQL script required to create the database is included in:

```
database/pharmacy_management.sql
```

Import this file into MySQL before running the application.

---

# ⚙ Installation

## 1. Clone the repository

```bash
git clone https://github.com/israaabazzal/PharmacyManagementSystem.git
```

---

## 2. Import the database

Open **MySQL Workbench** and import:

```
database/pharmacy_management.sql
```

---

## 3. Configure the database connection

Open:

```
src/com/pharmacy/utils/DatabaseConnection.java
```

Update the following values according to your local MySQL configuration:

```java
private static final String URL = "jdbc:mysql://localhost:3306/pharmacy_management_system";
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

---

## 4. Open the project

Open the project in **NetBeans IDE**.

---

## 5. Run the application

Build and run the project from NetBeans, or execute the generated application JAR located in the `dist` folder.

---

# 📸 Screenshots

Screenshots demonstrating the application can be found in the **screenshots** folder.

Suggested screenshots:

- Login Screen
- Dashboard
- Medicine Management
- Inventory Management
- Sales Management
- User Management
- Reports

---

# 📌 Requirements

- Java JDK 8 or later
- MySQL 8.0
- NetBeans IDE
- MySQL Connector/J
- FlatLaf
- JFreeChart

---

# 🔒 Database Integrity

The system uses MySQL relational constraints to maintain data consistency, including:

- Primary Keys
- Foreign Keys
- Cascade Delete
- SET NULL relationships
- Transaction history tracking
- Inventory consistency

---

# 📄 License

This project was developed for educational purposes.

---

# 👨‍💻 Author

**Israa Bazzal**

Bachelor of Computer Science

Developed using Java Swing and MySQL.
