# Homeless Support API



## Table of Contents

- [Introduction](#introduction)
- [Team Information](#team-information)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
  - [API Endpoints](#api-endpoints)
- [Style Checking and Documentation](#style-checking-and-documentation)
- [Project Management](#project-management)


## Introduction

Welcome to the **Homeless Support API** by **Byte Alchemists**. This API is designed to assist individuals experiencing homelessness by providing access to essential resources, services, and information. Our goal is to improve the quality of life for homeless individuals by connecting them with vital support systems and enabling service providers to manage and deliver resources efficiently.

## Team Information

**Team Name:** Byte Alchemists

| Team Member # | Name          | UNI    |
|---------------|---------------|--------|
| 1             | Zihao Cui     | zc2715 |
| 2             | Linshuo Li    | ll3815 |
| 3             | Zhentao Yang  | zy2679 |
| 4             | Ruoxing Liao  | rl3392 |
| 5             | Zhicheng Zou  | zz3105 |

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Git**: Installed on your local machine. [Download Git](https://git-scm.com/downloads)
- **Java JDK**: Version 17 or higher. [Download JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- **Maven**: For building the project. [Install Maven](https://maven.apache.org/install.html)
- **Postman**: For API testing. [Download Postman](https://www.postman.com/downloads/)
- **IDE**: Such as IntelliJ IDEA or Eclipse for development.

## Installation

Follow these steps to set up the project locally:

1. **Clone the Repository**

   ```bash
   git clone git@github.com:czh2152215/4156-GroupProject.git
   ```

2. **Build the Project**

   Use Maven to build the project:

   ```bash
   mvn clean install
   ```

3. **Run the Application**

   ```bash
   mvn spring-boot:run
   ```

   The API will be accessible at `http://localhost:8080`.


4. **Style Check**

   ```bash
   mvn checkstyle:check 
   mvn checkstyle:checkstyle
   ```

5. **Run Test and Generate Coverage Report**

   ```bash
   mvn clean test jacoco:report
   ```

5. **Run PMD**

   ```bash
   mvn pmd:pmd
   ```
## Usage

### API Endpoints

Our API provides various endpoints to manage and access homeless support services. Below is the documentation for each operational entry point.

#### 1. **Get All Services**

- **Endpoint:** `GET /services`
- **Description:** Retrieves all services across all categories.
- **Response:**
  - **Status Code:** `200 OK`
  - **Body:** JSON array of all `ServiceEntity` objects.

#### 2. **Get All Service Categories**

- **Endpoint:** `GET /services/categories`
- **Description:** Retrieves all available service categories.
- **Response:**
  - Status Code:
    - `200 OK`
    - `204 No Content`: If no categories are available.
    - `500 Internal Server Error`: If an exception occurs during the operation.
  - Body:
    - 200 OK: A plain text string where the category names are joined by commas.
    - 204 No Content: No body, indicating there are no categories available.
    - 500 Internal Server Error: A plain text message like "An Error has occurred" in case of an exception.

#### 3. **Add a New Category**

- **Endpoint:** `POST /services/categories/name/{name}`
- **Description:** Adds a new service category based on the category name.
- **Request Body:**
  ```json
  {
    "category_name": "shelters"
  }
  ```
- **Response:**
  - Status Code:
    - `200 OK`
    - `400 Bad Request`
    - `409 Conflict`
    - `500 Internal Server Error`
  - Body:
    - 200 OK: A success message indicating the category was updated successfully, e.g., "Attribute was updated successfully."
    - 400 Bad Request: An error message like "Invalid category name."
    - 409 Conflict: An error message like "Category already exists."
    - 500 Internal Error: A plain text message like "An error has occurred" in case of an exception.


#### 4. **Delete a Category**

- **Endpoint:** `DELETE /services/categories/name/{name}`
- **Description:** Deletes an existing service category.
- **Response:**
  - **Status Code:** `If successful, returns a success message with HTTP status 200 (OK), otherwise 404 Not Found`

#### 5. **Register a New Service**

- **Endpoint:** `POST /services/{category}`
- **Description:** Registers a new service under a specified category.
- **Path Parameter:**
  - `category`: The category under which to register the service (e.g., `shelters`, `food_banks`, `healthcare_centers`).
- **Request Body:**
  ```json
  {
    "name": "Shelter A",
    "category": "shelters",
    "latitude": 40.7128,
    "longitude": -74.0060,
    "address": "123 Main St",
    "city": "New York",
    "state": "NY",
    "zipcode": "10001",
    "contact_number": "123-456-7890",
    "operation_hour": "9 AM - 5 PM",
    "availability": true
  }
  ```
- **Response:**
  - **Status Code:** `201 Created`
  - **Body:** Success message.

#### 6. **Get Service by ID**

- **Endpoint:** `GET /services/{id}`
- **Description:** Retrieves details of a specific service by its ID.
- **Path Parameter:**
  - `id`: The unique identifier of the service.
- **Response:**
  - **Status Code:** `200 OK`
  - **Body:** JSON object of the `ServiceEntity`.

#### 7. **Update Service by ID**

- **Endpoint:** `PUT /services/{id}`
- **Description:** Updates details of a specific service by its ID. Supports both full and partial updates.
- **Path Parameter:**
  - `id`: The unique identifier of the service.
- **Request Body:** Partial or full `ServiceEntity` JSON object.
  - **Full Update Example:**
    ```json
    {
      "name": "Updated Shelter A",
      "category": "shelters",
      "latitude": 40.7128,
      "longitude": -74.0060,
      "address": "456 New St",
      "city": "New City",
      "state": "NY",
      "zipcode": "10002",
      "contact_number": "987-654-3210",
      "operation_hour": "10 AM - 6 PM",
      "availability": false
    }
    ```
  - **Partial Update Example:**
    ```json
    {
      "name": "Updated Shelter A",
      "city": "New City"
    }
    ```
- **Response:**
  - **Status Code:** `200 OK`
  - **Body:** JSON object of the updated `ServiceEntity`.

#### 8. **Delete Service by ID**

- **Endpoint:** `DELETE /services/{id}`
- **Description:** Deletes a specific service by its ID.
- **Path Parameter:**
  - `id`: The unique identifier of the service.
- **Response:**
  - **Status Code:** `204 No Content`

#### 9. **Query Services**

- **Endpoint:** `GET /services/query`
- **Description:** Retrieves services based on query parameters.
- **Query Parameters:**
  - `latitude` (optional): Latitude for location-based search.
  - `longitude` (optional): Longitude for location-based search.
  - `category` (optional): Category of services to filter.
  - `availability` (optional): Availability status (`true` or `false`).
- **Example Request:**
  ```
  GET /services/query?latitude=40.748817&longitude=-73.985428&category=shelters&availability=true
  ```
- **Response:**
  - **Status Code:** `200 OK`
  - **Body:** JSON array of matching `ServiceEntity` objects.







## Style Checking and Documentation

Our codebase adheres to the **Java Code Conventions** and is compliant with **Checkstyle** for style checking. All non-trivial code includes proper documentation using **Javadoc**.

### Style Checker Reports

We utilize **Checkstyle** to enforce coding standards. Reports are included in the repository under the `reports/` directory.

### Documentation

- **Javadoc:** All classes and major methods are documented using Javadoc. You can generate the documentation using Maven:

  ```bash
  mvn javadoc:javadoc
  ```

  The generated documentation will be located in the `target/site/apidocs/` directory.

## Project Management

We use **Jira** for project management. The board includes all tasks, assignments, and progress tracking for team members.

- **Jira Board Link:** [Byte Alchemists Jira](https://columbia-team-skl5pnkl.atlassian.net/jira/software/projects/KAN/list)





### Branch Protection Rules

Our `main` branch is protected with the following rules:

- **Required Reviews:** At least one team member must review and approve the pull request.
- **Commit Message Standards:** All commit messages must be meaningful and describe the changes made.



