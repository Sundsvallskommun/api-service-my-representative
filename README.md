# MyRepresentative

_The service creates and maintains powers of attorney between parties in a municipality. The powers of attorney are stored at the Swedish Companies Registration Office and enable them to keep track of who or which parties (i.e. the power of attorney holder) are allowed to act as an agent for a specific counterparty (i.e. the grantor of the power of attorney)._

## Getting Started

### Prerequisites

- **Java 21 or higher**
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

- Using Gradle:

```bash
gradle bootRun
```

## Dependencies

This microservice depends on the following services:

- **MinaOmbud**
  - **Purpose:** Used for finding mandates and authorities for a person or organization.
  - **Repository:** Service is provided by third party (Bolagsverket)
- **Party**
  - **Purpose:** Used for translating between party id and legal id.
  - **Repository:** [https://github.com/Sundsvallskommun/api-service-party](https://github.com/Sundsvallskommun/api-service-party)
  - **Setup Instructions:** See documentation in repository above for installation and configuration steps.

Ensure that these services are running and properly configured before starting this microservice.

## API Documentation

Access the API documentation via:

- **Swagger UI:** [http://localhost:8080/api-docs](http://localhost:8080/api-docs)

## Usage

### API Endpoints

See the [API Documentation](#api-documentation) for detailed information on available endpoints.

### Example Request

```bash
curl -X 'GET' 'https://localhost:8080/2281/mandates' \
'?mandateIssuer.partyId=fb2f0290-3820-11ed-a261-0242ac120002' \
'&mandateIssuer.type=pnr' \
'&mandateAcquirer.partyId=06b0ecca-8f77-4698-850d-5f20b56702e2' \
'&mandateAcquirer.type=pnr'
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

- **External Service URLs**

```yaml
integration:
  minaombud:
    oauth2-client-id: <client_id>
    oauth2-client-secret: <client_secret>
    oauth2-token-url: <token_url>
    url: <service_endpoint>
  party:
    oauth2-client-id: <client_id>
    oauth2-client-secret: <client_secret>
    oauth2-token-url: <token_url>
    url: <service_endpoint>
minaombud:
  jwt:
    audience: <audience>
    expiration: <expire_after>
    issuer: <issuer>
  scheduling:
    # Must match jwt.expiration
    cron: <cron>
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
