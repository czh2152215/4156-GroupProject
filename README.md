# Homeless Support API



## Table of Contents

- [Introduction](#introduction)
- [Team Information](#team-information)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
  - [API Endpoints](#api-endpoints)
- [Testing](#testing)
  - [Unit Tests](#unit-tests)
  - [API Tests](#api-tests)
- [Style Checking and Documentation](#style-checking-and-documentation)
- [Project Management](#project-management)
- [Contributing](#contributing)
- [License](#license)
- [Repository Links](#repository-links)

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
  - **Status Code:** `200 OK`
  - **Body:** JSON array of `Category` objects.

#### 3. **Add a New Category**

- **Endpoint:** `POST /services/categories`
- **Description:** Adds a new service category.
- **Request Body:**
  ```json
  {
    "category_name": "shelters"
  }
  ```
- **Response:**
  - **Status Code:** `201 Created`
  - **Body:** Success message.

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

## Testing

### Unit Tests

We have implemented comprehensive unit tests for all major components of the `ServiceService` class using **JUnit 5** and **Mockito**. These tests cover scenarios including successful operations, partial and full updates, handling non-existing services, and exception handling.

To run the unit tests:

1. **Navigate to the Project Directory**

   ```bash
   cd Homeless-Support-API
   ```

2. **Execute the Tests**

   ```bash
   mvn test
   ```

   Alternatively, you can run the tests directly from your IDE.

### API Tests

API testing is performed using **Postman**. These tests ensure that all API endpoints function correctly, handle edge cases, and return appropriate status codes.

#### Running API Tests with Postman

1. **Import the Collection**

   - Open Postman.
   - Click on `Import` and select the provided `HomelessSupportAPI.postman_collection.json` file from the repository.

2. **Execute the Tests**

   - Select the `HomelessSupportAPI` collection.
   - Click on `Run` to execute all the predefined requests.

3. **Review the Results**

   - Ensure all requests return the expected status codes and responses.
   - Address any failed tests by debugging the API endpoints.

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

We use **Trello** for project management. The board includes all tasks, assignments, and progress tracking for team members.

- **Trello Board Link:** [Byte Alchemists Trello](https://trello.com/invite/b/your-trello-board-link)

## Contributing

We welcome contributions from all team members. To contribute:

1. **Fork the Repository**

   Click on the `Fork` button in the top right corner of the repository page.

2. **Create a Feature Branch**

   ```bash
   git checkout -b feature/your-feature-name
   ```

3. **Commit Your Changes**

   ```bash
   git commit -m "Add your descriptive commit message"
   ```

4. **Push to the Branch**

   ```bash
   git push origin feature/your-feature-name
   ```

5. **Create a Pull Request**

   - Navigate to your forked repository on GitHub.
   - Click on `Compare & pull request`.
   - Provide a descriptive title and comment for your pull request.
   - Submit the pull request for review.

### Branch Protection Rules

Our `main` branch is protected with the following rules:

- **Required Reviews:** At least one team member must review and approve the pull request.
- **Commit Message Standards:** All commit messages must be meaningful and describe the changes made.

## License

This project is licensed under the [MIT License](LICENSE).

## Repository Links

- **Main Repository:** [Homeless Support API](https://github.com/byte-alchemists/Homeless-Support-API)
- **Alternative Repository:** [Capybara](https://github.com/mm6234/Capybara)

## Contact

For any questions or support, please reach out to the team members or your project mentor.
