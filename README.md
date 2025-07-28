# 📚 RESTful Bookstore API

A lightweight RESTful API for managing a bookstore, built using **JAX-RS** in **Java**. This project supports full CRUD operations using in-memory data structures, making it ideal for learning REST principles and backend API development. All endpoints have been tested using **Postman**.

---

## 🔧 Features

- 📖 Add new books
- 📚 View all books or search by ID
- 📝 Update book details
- ❌ Delete a book from the collection
- 🧠 Uses in-memory storage (no database)
- 🧪 API tested with Postman

---

## 🛠️ Technologies Used

- **Language**: Java
- **Framework**: JAX-RS 
- **Data Handling**: In-memory Lists
- **Tools**: Postman, Maven, NetBeans

---


---

## 🚀 Getting Started

### Prerequisites
- Java JDK (8 or above)
- A JAX-RS compatible server (Tomcat)
- Postman for testing API endpoints

### Running the Application
1. Import the project into your IDE (NetBeans)
2. Build and deploy the application
3. Use Postman to interact with the API

---

## 📡 Example API Endpoints

| Method | Endpoint         | Description               |
|--------|------------------|---------------------------|
| GET    | `/books`         | Get all books             |
| GET    | `/books/{id}`    | Get a book by ID          |
| POST   | `/books`         | Add a new book            |
| PUT    | `/books/{id}`    | Update an existing book   |
| DELETE | `/books/{id}`    | Delete a book             |

All requests/responses are in **JSON** format.





