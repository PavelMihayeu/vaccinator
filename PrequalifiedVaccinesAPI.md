# Prequalified Vaccines API

This document describes the new Prequalified Vaccines API functionality that allows bulk loading of WHO prequalified vaccines from CSV data into PostgreSQL database.

## Overview

The Prequalified Vaccines API provides endpoints to:
- Bulk load prequalified vaccines from the `WHO_prequalified_vaccines.csv` file
- Query prequalified vaccines by various criteria
- Replace existing vaccines with fresh data from CSV

## Database Schema

### PrequalifiedVaccineEntity
- `id` (Long): Primary key
- `dateOfPrequalification` (LocalDate): Date when vaccine was prequalified
- `vaccineType` (String): Type of vaccine (e.g., "BCG", "Hepatitis B")
- `commercialName` (String): Commercial name of the vaccine
- `presentation` (String): Presentation format (e.g., "Vial", "Ampoule")
- `numberOfDoses` (Integer): Number of doses (nullable)
- `manufacturer` (String): Manufacturer name
- `responsibleNRA` (String): Responsible National Regulatory Authority

## API Endpoints

### 1. Bulk Load Prequalified Vaccines
**POST** `/api/v1/prequalified-vaccines/bulk-load`

Loads prequalified vaccines from the CSV file into PostgreSQL database.

**Request Body:**
```json
{
  "replaceExisting": true
}
```

**Response:**
```json
{
  "success": true,
  "vaccinesLoaded": 278,
  "vaccinesReplaced": 0,
  "timestamp": "2025-01-15T10:30:00",
  "vaccines": [...],
  "errors": []
}
```

### 2. Get All Prequalified Vaccines
**GET** `/api/v1/prequalified-vaccines`

Retrieves all prequalified vaccines from the database.

### 3. Get Vaccines by Type
**GET** `/api/v1/prequalified-vaccines/by-type/{vaccineType}`

Filters vaccines by vaccine type.

### 4. Get Vaccines by Manufacturer
**GET** `/api/v1/prequalified-vaccines/by-manufacturer/{manufacturer}`

Filters vaccines by manufacturer name.

### 5. Get Vaccines by NRA
**GET** `/api/v1/prequalified-vaccines/by-nra/{responsibleNRA}`

Filters vaccines by responsible National Regulatory Authority.

### 6. Get Total Count
**GET** `/api/v1/prequalified-vaccines/count`

Returns the total number of prequalified vaccines in the database.

## CSV File Format

The API reads from `src/main/resources/static/WHO_prequalified_vaccines.csv` with the following columns:

1. **Date of Prequalification** (dd/MM/yyyy format)
2. **Vaccine Type**
3. **Commercial Name**
4. **Presentation**
5. **No. of doses** (can be empty)
6. **Manufacturer**
7. **Responsible NRA**

## Configuration

### Dependencies Added
- `spring-boot-starter-data-mongodb`: MongoDB support
- `com.opencsv:opencsv:5.9`: CSV parsing

### Database Configuration
```properties
# MongoDB Configuration
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=vaccinator_db
```

### Docker Configuration
The `docker-compose.yml` includes a MongoDB service:
```yaml
mongodb:
  image: mongo:7.0
  environment:
    MONGO_INITDB_DATABASE: vaccinator_db
```

## Usage Examples

### Bulk Load Vaccines
```bash
curl -X POST "http://localhost:8080/api/v1/prequalified-vaccines/bulk-load" \
  -H "X-API-Key: your-api-key" \
  -H "Content-Type: application/json" \
  -d '{"replaceExisting": true}'
```

### Get BCG Vaccines
```bash
curl -X GET "http://localhost:8080/api/v1/prequalified-vaccines/by-type/BCG" \
  -H "X-API-Key: your-api-key"
```

### Get Vaccines by Manufacturer
```bash
curl -X GET "http://localhost:8080/api/v1/prequalified-vaccines/by-manufacturer/Serum%20Institute%20of%20India%20Pvt.%20Ltd." \
  -H "X-API-Key: your-api-key"
```

## Testing

Use the provided test scripts:
- `test-prequalified-vaccines.sh` (Linux/Mac)
- `test-prequalified-vaccines.ps1` (Windows PowerShell)

## Features

- **Bulk Loading**: Load all vaccines from CSV in one operation
- **Replace Existing**: Option to replace all existing vaccines with fresh data
- **Error Handling**: Detailed error reporting for CSV parsing issues
- **Filtering**: Query vaccines by type, manufacturer, or NRA
- **Type Safety**: Uses Java records for DTOs and proper entity mapping
- **Validation**: Input validation and error handling
- **Documentation**: Full OpenAPI/Swagger documentation

## Security

All endpoints require API key authentication via the `X-API-Key` header.

## Error Handling

The bulk load operation provides detailed error reporting:
- Invalid date formats
- Missing required fields
- CSV parsing errors
- Database operation failures

Each error includes the line number and specific error message for easy debugging. 