# MyRepresentative

_The service creates and maintains powers of attorney (mandates) between parties in a municipality and a specific namespace. The powers of attorney are stored to keep track of who or which parties (i.e. the grantee) are allowed to act as an agent for a specific counterparty (i.e. the grantor of the power of attorney)._

## Getting Started

### Prerequisites

- **Java 25 or higher**
- **Maven**
- **MariaDB**
- **Git**
- **[Dependent Microservices](#dependencies)**

### Installation

1. **Clone the repository:**

```bash
git clone https://github.com/Sundsvallskommun/api-service-my-representative.git
cd api-service-my-representative
```

2. **Configure the application:**

   Before running the application, you need to set up configuration settings.
   See [Configuration](#configuration)

   **Note:** Ensure all required configurations are set; otherwise, the application may fail to start.

3. **Ensure dependent services are running:**

   If this microservice depends on other services, make sure they are up and accessible. See [Dependencies](#dependencies) for more details.

4. **Build and run the application:**

   - Using Maven:

```bash
mvn spring-boot:run
```

## Dependencies

none

## API Documentation

Access the API documentation via:

- **Swagger UI:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Usage

### API Endpoints

See the [API Documentation](#api-documentation) for detailed information on available endpoints.

### Example Request

```bash
curl -X 'GET' \
  'http://localhost:8080/2281/MY_NAMESPACE/mandates?page=1&limit=15&grantorPartyId=fb2f0290-3820-11ed-a261-0242ac120002&granteePartyId=fb2f0290-3820-11ed-a261-0242ac120004&signatoryPartyId=fb2f0290-3820-11ed-a261-0242ac120003' \
  -H 'accept: application/json'
```

## Configuration

Configuration is crucial for the application to run successfully. Ensure all necessary settings are configured in `application.yml`.

### Key Configuration Parameters

- **Server Port:**

```yaml
server:
  port: 8080
```

- **Database Settings**

```yaml
spring:
  datasource:
    username: <db_username>
    password: <db_password>
    url: jdbc:mariadb://<db_host>:<db_port>/<database>
  jpa:
    properties:
      jakarta:
        persistence:
          schema-generation:
            database:
              action: validate
```

### Database Initialization

The project is set up with [Flyway](https://github.com/flyway/flyway) for database migrations. Flyway is disabled by default so you will have to enable it to automatically populate the database schema upon application startup.

```yaml
spring:
  flyway:
    enabled: true
```

- **No additional setup is required** for database initialization, as long as the database connection settings are correctly configured.

### Additional Notes

- **Application Profiles:**

  Use Spring profiles (`dev`, `prod`, etc.) to manage different configurations for different environments.

- **Logging Configuration:**

  Adjust logging levels if necessary.

## Contributing

Contributions are welcome! Please see [CONTRIBUTING.md](https://github.com/Sundsvallskommun/.github/blob/main/.github/CONTRIBUTING.md) for guidelines.

## License

This project is licensed under the [MIT License](LICENSE).

## Status

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-my-representative&metric=alert_status)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-my-representative)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-my-representative&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-my-representative)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-my-representative&metric=security_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-my-representative)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-my-representative&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-my-representative)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-my-representative&metric=vulnerabilities)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-my-representative)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=Sundsvallskommun_api-service-my-representative&metric=bugs)](https://sonarcloud.io/summary/overall?id=Sundsvallskommun_api-service-my-representative)

## 

&copy; 2021 Sundsvalls kommun
