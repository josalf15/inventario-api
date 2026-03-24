# Inventory Synchronization API

This project is a RESTful API built with Java 17 and Spring Boot 3.

The application is designed around a background synchronization process and a query API:
1. **Cron Scheduler:** a job every 10 minutes. 
2. **Parallel Ingestion:** Makes asynchronous, parallel HTTP calls to Provider A and Provider B to avoid blocking threads and reduce total execution time.
3. **Data Homologation:** Maps different JSON structures into a unified `HomologatedProduct` entity, handling business rules (like defaulting missing stock to 0).
4. **Local Storage:** Upserts the normalized data into a local H2 Database.
5. **Query API:** Exposes a GET endpoint to filter the local inventory dynamically without hitting the external providers in real-time.

## Tech Stack
* **Java 17**
* **Spring Boot 3.x** (Web, Data JPA, Validation)
* **H2 Database** (In-Memory for easy testing)
* **Docker** * **Lombok** (To reduce boilerplate code)

## How to Run (Docker)

To run this application using Docker, follow these steps:

1. **Build the application (Maven):**
   ```bash
   ./mvnw clean package -DskipTests