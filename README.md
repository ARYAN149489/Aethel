<div align="center">

# Aethel — Real Estate Management System

### A full-stack desktop CRM built with **JavaFX**, **MySQL**, and **Java 9 Modules** for managing customers, property listings, and real estate deals — complete with analytics, document exports, and automated email notifications.

[![Java](https://img.shields.io/badge/Java-9+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-007396?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)](LICENSE)

</div>

---

## Overview

**Aethel** is a production-grade, modular desktop application designed for real estate agencies to digitize and streamline their day-to-day operations. It replaces traditional paper-based workflows with an intuitive JavaFX interface backed by a MySQL relational database.

The system covers the **complete real estate lifecycle** — from onboarding customers with Aadhar verification, listing and managing properties with image uploads, to tracking deals through negotiation, advance payments, and completion — all from a unified admin dashboard.

---

## Key Features

### Customer Management (CRM)
- **Full CRUD** — Register, search, update, and delete customer profiles
- **Document Verification** — Upload and store profile photo, Aadhar front, and Aadhar back images directly in the database as BLOBs
- **Customer Classification** — Categorize customers as Buyer, Seller, or Both
- **Search by Mobile** — Instant lookup using primary mobile number

### Property Listing & Management
- **Comprehensive Listings** — Capture property name, address, city, area, dimensions (front/rear/left/right), total size, facing direction, and demanded price
- **Auto Area Calculation** — Computes plot area from dimensional inputs using trapezoidal formula
- **Property Classification** — Filter by Usage Type (Commercial / Residential / Agriculture) and Land Status (Plot / Constructed)
- **Dual Image Support** — Attach up to 2 property images per listing
- **Approval Tracking** — Track which authority approved the property (JDA, NAC, RAJ, etc.)
- **Update & Remove Listings** — Modify pricing and details, or delist properties entirely

### Deal Tracking & Negotiation
- **End-to-End Deal Management** — Create deals linking buyer ↔ seller ↔ property with financial terms
- **Financial Tracking** — Record final amount, commission, advance payments, remaining balances, and commission splits
- **Deal Lifecycle** — Track deals through Ongoing → Completed / Cancelled statuses
- **Auto-Delist on Completion** — Properties are automatically removed from listings when a deal is marked as Completed
- **Date Range Filtering** — Query deals by registry date range and status

### Analytics Dashboard
- **Pie Charts** — Visual breakdown of customer types (Buyer / Seller / Both)
- **Bar Charts** — Deal status distribution (Ongoing / Completed / Cancelled)
- Built using JavaFX `PieChart` and `BarChart` components with live database queries

### Document Export
- **Excel (`.xlsx`)** — Export filtered customer records with embedded profile images using Apache POI
- **PDF (`.pdf`)** — Generate formatted PDF reports for customers, properties, and deals using OpenPDF
- **Auto-Open** — Exported files open automatically in the system's default application

### Automated Email Notifications
- **Customer Registration/Update** — Sends styled HTML confirmation emails with full profile details upon customer registration or profile update
- **Property Listing/Update** — Automatically looks up the property owner's email and sends formatted HTML notifications with complete property details (dimensions, price, approval, etc.)
- **Asynchronous Delivery** — Emails are dispatched on background threads to keep the UI responsive
- **Smart Validation** — Email addresses are validated using `javax.mail.InternetAddress` before any send attempt

### Authentication
- Password-based admin login with MySQL-backed credential verification
- Session management with dashboard navigation and logout functionality

### User Experience
- **JavaFX Alert System** — Contextual UI popups (Warning, Error, Information) for every user action — replacing console-only output with interactive feedback
- **Input Validation** — Comprehensive form validation for numeric fields, required selections, and date ranges
- **Responsive Dashboard** — 3×3 grid-based navigation hub with icon-driven module cards

---

## Architecture

```
src/main/java/
├── com.example.javaproj/    # Application entry point (HelloApplication, Launcher)
├── loginpage/               # Password authentication controller
├── dashboard/               # Central navigation hub (9-module grid)
├── customer/                # Customer CRUD controller + image handling
├── addproperty/             # New property listing with area calculation
├── updateproperty/          # Property price/details update & delisting
├── listproperties/          # Combined listing + update property controller
├── adddeal/                 # Deal creation with buyer-seller-property linking
├── updatedeal/              # Deal lifecycle management (update/cancel/complete)
├── allcustomer/             # Filtered customer view + Excel/PDF export
├── alldeals/                # Date-range filtered deal reports + PDF export
├── allproperties/           # Multi-filter property search + PDF export
├── chart/                   # Analytics — PieChart & BarChart visualizations
├── emailsender/             # Async HTML email dispatch (SMTP/Gmail)
├── jdbcc/                   # Centralized MySQL JDBC connection manager
├── myalert/                 # Reusable JavaFX Alert utility
└── module-info.java         # Java 9 module descriptor

src/main/resources/
├── dashboardview/           # Dashboard FXML + navigation icons
├── customerView/            # Customer form FXML
├── addpropertyview/         # Add Property form FXML
├── updatepropertyview/      # Update Property form FXML
├── listPropertiesView/      # List Properties form FXML
├── adddealview/             # Add Deal form FXML
├── updatedealview/          # Update Deal form FXML
├── allcustomerview/         # All Customers table FXML
├── alldealview/             # All Deals table FXML
├── allpropertiesview/       # All Properties table FXML
├── chartview/               # Analytics chart FXML
└── loginpageview/           # Login screen FXML
```

### Design Patterns & Principles

| Pattern | Usage |
|---|---|
| **MVC** | FXML views (View) ↔ Controller classes (Controller) ↔ MySQL database (Model) |
| **Singleton-style Connection** | `DatabaseConnection.doConnectToDb()` provides centralized DB access |
| **Static Utility Classes** | `MyAlert`, `EmailSender` — stateless, reusable service methods |
| **Java 9 Module System** | Strict `module-info.java` with explicit `requires`, `opens`, and `exports` |
| **Async Processing** | Email dispatch on daemon threads to prevent UI blocking |
| **Bean Pattern** | `CustomerBean`, `DealBean`, `PropertyBean` — JavaFX-compatible data carriers |

---

## Tech Stack

| Layer | Technology | Version |
|---|---|---|
| **Language** | Java | 9+ |
| **UI Framework** | JavaFX (Controls + FXML) | 21.0.6 |
| **Database** | MySQL | 8.0+ |
| **JDBC Driver** | MySQL Connector/J | 8.0.33 |
| **PDF Generation** | OpenPDF (LibrePDF) | 1.3.39 |
| **PDF Processing** | Apache PDFBox | 3.0.3 |
| **Excel Export** | Apache POI (XSSF) | 5.2.5 / 5.4.0 |
| **Email** | JavaMail (javax.mail) | 1.6.2 |
| **Build Tool** | Apache Maven (with Wrapper) | 3.9+ |
| **Module System** | Java Platform Module System (JPMS) | Java 9+ |

---

## Getting Started

### Prerequisites

- **JDK 17+** (recommended JDK 21 for full JavaFX 21 compatibility)
- **MySQL 8.0+** running on `localhost:3306`
- **Maven 3.9+** (or use the included `mvnw` wrapper)

### Database Setup

```sql
CREATE DATABASE javaProj;
USE javaProj;

-- Core Tables
CREATE TABLE mainUser (
    password VARCHAR(255) NOT NULL
);

CREATE TABLE Customers (
    mobileNumber VARCHAR(15) PRIMARY KEY,
    name VARCHAR(100),
    address VARCHAR(255),
    city VARCHAR(100),
    email VARCHAR(150),
    type VARCHAR(20),
    profilePic LONGBLOB,
    aadharFrontPic LONGBLOB,
    aadharBackPic LONGBLOB,
    lastUpdatedAt DATE
);

CREATE TABLE Properties (
    mobileNumber VARCHAR(15),
    prop_name VARCHAR(150),
    address VARCHAR(255),
    area VARCHAR(100),
    city VARCHAR(100),
    size_dim FLOAT,
    front FLOAT,
    rear_side FLOAT,
    left_side FLOAT,
    right_side FLOAT,
    direction VARCHAR(50),
    type_usage VARCHAR(50),
    type_status VARCHAR(50),
    approved_by VARCHAR(50),
    price_demanded FLOAT,
    other_info TEXT,
    pic1 LONGBLOB,
    pic2 LONGBLOB,
    updated_at DATE,
    PRIMARY KEY (mobileNumber, prop_name)
);

CREATE TABLE Deals (
    buyer_mobile VARCHAR(15),
    buyer_name VARCHAR(100),
    seller_mobile VARCHAR(15),
    seller_name VARCHAR(100),
    property_name VARCHAR(150),
    final_amount FLOAT,
    my_commission FLOAT,
    adv_commission FLOAT,
    adv_amount FLOAT,
    amount_left FLOAT,
    commission_left FLOAT,
    adv_given_date DATE,
    registry_date DATE,
    other_info TEXT,
    deal_status VARCHAR(20)
);

-- Insert default admin password
INSERT INTO mainUser VALUES ('admin123');
```

### Build & Run

```bash
# Clone the repository
git clone https://github.com/yourusername/Aethel.git
cd Aethel

# Build the project
./mvnw clean compile

# Run the application
./mvnw javafx:run
```

### Configuration

Update the database credentials in [`DatabaseConnection.java`](src/main/java/jdbcc/DatabaseConnection.java):

```java
con = DriverManager.getConnection(
    "jdbc:mysql://localhost/javaProj",  // Database URL
    "root",                             // Username
    "123456"                            // Password
);
```

For email notifications, configure your Gmail App Password in [`EmailSender.java`](src/main/java/emailsender/EmailSender.java):

```java
String fromEmail = "your-email@gmail.com";
String appPassword = "your-app-password";
```

---

## Module Breakdown

| Module | Screens | Key Operations |
|---|---|---|
| `loginpage` | Login | Password auth, session start |
| `dashboard` | Dashboard | 9-card grid navigation, logout |
| `customer` | Manage Customer | Save, Update, Delete, Search with 3 image uploads |
| `addproperty` | Add Property | List new property with dimensional area calculator |
| `listproperties` | List Properties | Combined add + update + remove property view |
| `updateproperty` | Update Property | Modify price, remarks, images; remove listings |
| `adddeal` | Add Deal | Create deals with buyer-seller-property linkage |
| `updatedeal` | Update Deal | Update financials, complete or cancel deals |
| `allcustomer` | View All Customers | Filter by type, export to Excel/PDF with images |
| `alldeals` | View All Deals | Filter by date range & status, export to PDF |
| `allproperties` | View All Properties | Filter by city, area, price, type; export to PDF |
| `chart` | Analytics | PieChart (customer types) + BarChart (deal status) |
| `emailsender` | — | Async HTML email dispatch service |

---

## Skills & Concepts Demonstrated

- **Object-Oriented Design** — Modular package architecture with separation of concerns
- **Java 9 Module System (JPMS)** — Explicit module boundaries with `requires`, `opens`, and `exports`
- **JavaFX + FXML** — Declarative UI with controller binding, `TableView`, `ComboBox`, `DatePicker`, `RadioButton`, `ImageView`, charts
- **JDBC & Prepared Statements** — Parameterized queries preventing SQL injection, BLOB handling for images
- **Multithreading** — Background thread email dispatch to maintain UI responsiveness
- **File I/O** — `FileChooser` integration, temp file management with `deleteOnExit()`, `InputStream` BLOB streaming
- **Third-Party Library Integration** — Apache POI (Excel with embedded images), OpenPDF (PDF table generation), JavaMail (SMTP with TLS)
- **Error Handling & UX** — Centralized alert system with contextual error messages, input validation, and graceful exception handling
- **MVC Architecture** — Clean separation between FXML views, controller logic, and database model layer

---

## License

This project is open source and available under the [MIT License](LICENSE).

---

<div align="center">

**Built with Java by Aryan Kansal**

</div>
