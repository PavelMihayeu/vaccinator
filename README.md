# Vaccinator API

A Spring Boot 3.5.0 API for managing vaccine information and WHO guidelines, designed to help people understand vaccination recommendations.

## Features

- **Vaccine Management**: Store and retrieve vaccine information with schedules and doses
- **WHO Guidelines**: Reference WHO recommendations and guidelines
- **API Key Authentication**: Secure admin endpoints with API key authentication
- **Swagger Documentation**: Interactive API documentation
- **Docker Support**: Run the entire stack with Docker Compose
- **Database Migrations**: Liquibase for database schema management
- **Validation**: Comprehensive input validation with @Validated

## Technology Stack

- **Java 21**
- **Spring Boot 3.5.0**
- **PostgreSQL 15**
- **Liquibase** (Database migrations)
- **MapStruct** (Object mapping)
- **Spring Security** (API key authentication)
- **SpringDoc OpenAPI** (Swagger documentation)
- **Docker & Docker Compose**

## Database Schema

The application uses a normalized database schema with the following core tables:

- `vaccine` - Basic vaccine information
- `vaccine_schedule` - Dose schedules (primary & booster)
- `dose` - Individual doses in a schedule
- `age_group` - Target age groups (e.g., infant, adolescent)
- `region` - Regional applicability (e.g., global, Asia)
- `consideration` - Special use cases (e.g., HIV, pregnancy)
- Junction tables for many-to-many relationships

## Quick Start

### Prerequisites

- Docker and Docker Compose
- Java 21 (for local development)

### Running with Docker

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd vaccinator-api
   ```

2. **Set up environment variables**
   ```bash
   # Copy the .env file and modify as needed
   cp .env.example .env
   ```

3. **Start the application**
   ```bash
   docker-compose up -d
   ```

4. **Access the application**
   - API: http://localhost:8080
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/api-docs

### Local Development

1. **Set up PostgreSQL database**
   ```bash
   docker-compose up -d db
   ```

2. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

## API Endpoints

### Public Endpoints (No Authentication Required)

- `GET /api/v1/public/vaccines` - Get all vaccines
- `GET /api/v1/public/vaccines/{id}` - Get vaccine by ID
- `GET /api/v1/public/vaccines/search?name={name}` - Search vaccines by name

### Admin Endpoints (Requires API Key)

- `POST /api/v1/admin/vaccines` - Create a new vaccine
- `PUT /api/v1/admin/vaccines/{id}` - Update a vaccine
- `DELETE /api/v1/admin/vaccines/{id}` - Delete a vaccine

### Authentication

Admin endpoints require an API key in the `X-API-Key` header:

```bash
curl -H "X-API-Key: your-api-key" \
     -H "Content-Type: application/json" \
     -X POST http://localhost:8080/api/v1/admin/vaccines \
     -d '{"name": "COVID-19 Vaccine", "type": "mRNA"}'
```

## Environment Variables

Create a `.env` file with the following variables:

```env
# Database Configuration
POSTGRES_DB=vaccinator
POSTGRES_USER=vaccinator_user
POSTGRES_PASSWORD=vaccinator_password
DB_URL=jdbc:postgresql://db:5432/vaccinator
DB_USERNAME=vaccinator_user
DB_PASSWORD=vaccinator_password

# Application Configuration
API_KEY_SECRET=your-super-secret-api-key-here-change-in-production
JWT_SECRET=your-jwt-secret
JWT_EXPIRATION_MS=86400000

# Server Configuration
SERVER_PORT=8080
```

## Example API Usage

### Creating a Vaccine

```bash
curl -H "X-API-Key: your-api-key" \
     -H "Content-Type: application/json" \
     -X POST http://localhost:8080/api/v1/admin/vaccines \
     -d '{
       "name": "COVID-19 Vaccine",
       "type": "mRNA",
       "description": "Vaccine against SARS-CoV-2",
       "whoReferenceUrl": "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/covid-19-vaccines",
       "targetGroups": ["Adult", "Elderly"],
       "regions": ["Global"],
       "considerations": ["Co-administration"],
       "schedules": [
         {
           "scheduleType": "standard",
           "description": "Standard 2-dose schedule",
           "doses": [
             {
               "doseNumber": 1,
               "minAge": "16 years",
               "isBooster": false
             },
             {
               "doseNumber": 2,
               "minAge": "16 years",
               "isBooster": false,
               "note": "3-4 weeks after first dose"
             }
           ]
         }
       ]
     }'
```

### Retrieving Vaccines

```bash
# Get all vaccines
curl http://localhost:8080/api/v1/public/vaccines

# Search vaccines by name
curl "http://localhost:8080/api/v1/public/vaccines/search?name=COVID"

# Get specific vaccine
curl http://localhost:8080/api/v1/public/vaccines/{vaccine-id}
```

## Database Migrations

The application uses Liquibase for database migrations. Migration files are located in:
- `src/main/resources/db/changelog/db.changelog-master.xml`
- `src/main/resources/db/changelog/changes/`

To add a new migration:

1. Create a new changelog file in `src/main/resources/db/changelog/changes/`
2. Include it in the master changelog
3. Restart the application

## Development

### Building the Project

```bash
./gradlew build
```

### Running Tests

```bash
./gradlew test
```

### Code Generation

MapStruct generates mapping classes automatically during compilation. If you modify the mapper interfaces, rebuild the project:

```bash
./gradlew clean build
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.

