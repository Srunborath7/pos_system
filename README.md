# POS System (Spring Boot + MySQL + HTML + Bootstrap)

A fully functional Point of Sale (POS) system built with:

* Spring Boot (Java)
* MySQL
* HTML, Bootstrap, and JavaScript (Fetch API)
* JWT Authentication with Role-based Access Control
* SweetAlert2 for alert dialogs
* PDF invoice generation

---

## ✨ Features

* User Registration & JWT Login
* Role-based Access (Admin, Manager, User)
* Product Management (CRUD + image upload)
* Inventory Tracking (IN/OUT entries per product)
* POS Order Creation with Cart
* PDF Invoice Generation after Payment
* Dashboard Summary (sales, product count, order count)
* Mobile-friendly Frontend UI using Bootstrap

---

## 🔧 Tech Stack

| Layer      | Technology            |
| ---------- | --------------------- |
| Backend    | Spring Boot (Java 17) |
| Auth       | Spring Security + JWT |
| DB         | MySQL                 |
| Frontend   | HTML + Bootstrap 5    |
| Alerts     | SweetAlert2           |
| PDF Export | iText PDF             |

---

## ⚖️ Prerequisites

* Java 17+
* Maven
* MySQL Server

---

## ⚡ Installation & Run

### 1. Clone the Repository

```bash
git clone https://github.com/yourname/pos-system.git
cd pos-system
```

### 2. MySQL Setup

```sql
CREATE DATABASE pos_db;
```

### 3. Backend Configuration

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pos_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
jwt.secret=your_jwt_secret_key
```

### 4. Run the Backend

```bash
mvn spring-boot:run
```
### Change local file photo and store pdf file 
```
public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/product-images/**")
                .addResourceLocations("file:///E:/UP_Year_3_S2/Java spring boot/pos_system/uploads/product-images/");

        registry.addResourceHandler("/invoice/**")
                .addResourceLocations("file:///E:/UP_Year_3_S2/Java spring boot/pos_system/invoice/");
    }
```
Spring Boot will start at: `http://localhost:8080`

### 5. Frontend Setup

Open these files in a browser:

* `frontend/login.html` (login page)
* `frontend/dashboard.html` (admin dashboard)
* `frontend/pos.html` (POS checkout)

No build tools are needed. You can use a static server to host them.

---

## 💳 Default Admin Account

Created automatically if no users exist.

```text
Email:    admin@gmail.com
Password: admin123
Role:     ROLE_ADMIN
```

---

## 📄 API Overview

| Method | Endpoint             | Description             |
| ------ | -------------------- | ----------------------- |
| POST   | /api/auth/login      | Authenticate user       |
| POST   | /api/auth/register   | Public register (USER)  |
| GET    | /api/products/count  | Count of all products   |
| GET    | /api/orders/count    | Total order count       |
| POST   | /api/orders          | Create new order        |
| GET    | /api/inventory       | Get inventory logs      |
| POST   | /api/products/upload | Upload product w/ image |

Include `Authorization: Bearer <JWT>` in headers when calling protected endpoints.

---

## 📈 Dashboard UI Components

* Total Sales (\$)
* Total Products (count)
* Total Orders (count)

```html
<div class="container row d-flex g-3 mt-3">
  <div class="col-4 card text-white bg-success">
    <div class="card-header">Total Sales</div>
    <div class="card-body">
      <h5 class="card-title">$<span id="totalSaleAmount">0.00</span></h5>
    </div>
  </div>
  <div class="col-4 card text-white bg-primary">
    <div class="card-header">Total Products</div>
    <div class="card-body">
      <h5 class="card-title"><span id="totalProductCount">0</span></h5>
    </div>
  </div>
  <div class="col-4 card text-white bg-danger">
    <div class="card-header">Total Orders</div>
    <div class="card-body">
      <h5 class="card-title"><span id="orderCount">0</span></h5>
    </div>
  </div>
</div>
```

---

## 💌 PDF Invoice Sample

Generated after successful order payment. Saved under `/upload/invoice-<id>.pdf`.

---

## 📁 Directory Structure

```
pos-system/
├── backend/
│   └── src/
│       ├── main/java/com/example/pos_system/
│       ├── main/resources/application.properties
├── frontend/
│   ├── login.html
│   ├── dashboard.html
│   ├── pos.html
│   ├── js/
│   │   ├── auth.js
│   │   ├── dashboard.js
│   │   └── pos.js
└── README.md
```

---

## 🚀 Future Improvements

* Category-based filtering
* Expense tracking
* Barcode scanner support
* Monthly reports & charts
* RESTful API versioning

---

## 💼 License

MIT License. Use it freely for commercial or personal use.

---

## 📅 Author

Developed by **Mosco Ally** (2025)
