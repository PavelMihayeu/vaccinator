# Vaccination Recommendation System

This system provides comprehensive vaccination recommendations based on CDC guidelines and other authoritative sources. It supports bulk-loading of vaccination data and personalized recommendations based on age, risk factors, and other criteria.

## Overview

The vaccination recommendation system provides:
- **Bulk data loading**: Load comprehensive vaccination schedules from JSON format
- **Personalized recommendations**: Get vaccine recommendations based on individual characteristics
- **Flexible querying**: Support for both request body and query parameter queries
- **Comprehensive coverage**: Support for all age groups, risk factors, and special populations
- **Real-time updates**: Easy to update recommendations with new data

## Features

### 1. Bulk Data Loading
- Load vaccination recommendations from structured JSON format
- Automatic clearing of existing data before loading new recommendations
- Support for complex vaccination schedules with branches and rules
- Comprehensive error handling and logging

### 2. Personalized Recommendations
- Age-based recommendations (months/years)
- Life stage considerations (infant, child, adolescent, adult)
- Risk factor assessment (immunocompromised, pregnancy, etc.)
- Product-specific recommendations
- Special population considerations

### 3. Flexible Query Interface
- **POST /recommendations**: Complex queries with request body
- **GET /recommendations**: Simple queries with query parameters
- Support for multiple filter combinations
- Comprehensive response with dose information and status

## API Endpoints

### 1. Bulk Load Recommendations
```
POST /api/v1/vaccination-recommendations/bulk-load
```
Loads vaccination recommendations from JSON format.

**Request Body**: `VaccinationRecommendationRequest` (see DTO structure below)

**Response**: `BulkLoadResponse` with loading statistics

### 2. Get Personalized Recommendations (POST)
```
POST /api/v1/vaccination-recommendations/recommendations
```
Returns personalized recommendations using request body.

**Request Body**: `RecommendationQuery` with detailed criteria

### 3. Get Personalized Recommendations (GET)
```
GET /api/v1/vaccination-recommendations/recommendations?ageYears=2&lifeStage=child
```
Returns personalized recommendations using query parameters.

**Query Parameters**:
- `ageYears`: Age in years (e.g., 2.5)
- `lifeStage`: Life stage (infant, child, adolescent, adult)
- `sex`: Gender (male, female, any)
- `pregnancyStatus`: Pregnancy status (pregnant, not_pregnant)
- `riskFactors`: List of risk factors
- `immunocompromised`: Boolean for immunocompromised status
- `product`: Specific vaccine product
- `needRapidProtection`: Boolean for rapid protection needs

### 4. Get All Available Vaccines
```
GET /api/v1/vaccination-recommendations/vaccines
```
Returns list of all available vaccine names.

### 5. Get Recommendation Metadata
```
GET /api/v1/vaccination-recommendations/metadata
```
Returns metadata about current recommendations (schema version, source, etc.).

### 6. Health Check
```
GET /api/v1/vaccination-recommendations/health
```
Simple health check endpoint.

## Data Structure

### VaccinationRecommendationRequest
The main DTO for bulk-loading vaccination data:

```json
{
  "schema_version": "1.1",
  "source": "CDC Child & Adolescent Immunization Schedule, United States, 2025",
  "schema_notes": "Ages in months; intervals in weeks",
  "schedule_keys": {
    "dose.min_age_months": "Earliest age the dose may be given",
    "dose.max_age_months": "Latest age the dose should be given",
    "dose.fixed_months_from_start": "Give at fixed offset from series start",
    "dose.min_interval_weeks_from_prev": "Minimum weeks from the previous dose"
  },
  "vaccines": [
    {
      "vaccine": "Hepatitis B (HepB)",
      "why": "Prevents chronic liver infection, cirrhosis, and liver cancer.",
      "recommendations": [
        {
          "label": "Routine infant series",
          "who": {
            "age": { "min_years": 0, "max_years": 1.5 },
            "life_stage": "infant",
            "sex": "any"
          },
          "schedule": {
            "series": "3-dose (0, 1–2, 6–18 months)",
            "doses": [
              { "dose": 1, "min_age_months": 0, "fixed_months_from_start": 0 },
              { "dose": 2, "min_age_months": 1, "min_interval_weeks_from_prev": 4 },
              { "dose": 3, "min_age_months": 6, "min_interval_weeks_from_prev": 8 }
            ]
          },
          "citations": ["CDC reference"]
        }
      ]
    }
  ]
}
```

### Response Structure
The system returns personalized recommendations with:

- **Vaccine information**: Name, purpose, series details
- **Dose information**: Timing, intervals, age requirements
- **Status indicators**: Due, overdue, not due, completed
- **Applicability reasoning**: Why the recommendation applies
- **Citations**: Source references

## Usage Examples

### 1. Load Vaccination Data
```bash
curl -X POST "http://localhost:8080/api/v1/vaccination-recommendations/bulk-load" \
  -H "Content-Type: application/json" \
  -d @vaccination-recommendations.json
```

### 2. Get Recommendations for 2-Year-Old Child
```bash
curl -X GET "http://localhost:8080/api/v1/vaccination-recommendations/recommendations?ageYears=2&lifeStage=child"
```

### 3. Get Recommendations for Immunocompromised Adult
```bash
curl -X GET "http://localhost:8080/api/v1/vaccination-recommendations/recommendations?ageYears=30&immunocompromised=true"
```

### 4. Get Recommendations for Pregnant Woman
```bash
curl -X GET "http://localhost:8080/api/v1/vaccination-recommendations/recommendations?pregnancyStatus=pregnant&sex=female"
```

## Testing

### Test Scripts
Use the provided test scripts to validate functionality:

**Linux/Mac:**
```bash
chmod +x test-vaccination-recommendations.sh
./test-vaccination-recommendations.sh
```

**Windows:**
```powershell
.\test-vaccination-recommendations.ps1
```

### Manual Testing
Test individual endpoints using curl or Postman:

```bash
# Health check
curl http://localhost:8080/api/v1/vaccination-recommendations/health

# Get metadata
curl http://localhost:8080/api/v1/vaccination-recommendations/metadata

# Get all vaccines
curl http://localhost:8080/api/v1/vaccination-recommendations/vaccines
```

## Implementation Details

### Service Layer
- **VaccinationRecommendationService**: Main service interface
- **VaccinationRecommendationServiceImpl**: Core business logic implementation
- **Complex filtering**: Age ranges, risk factors, life stages, special populations

### Data Persistence
- **MongoDB**: Document-based storage for flexible schema
- **Single active set**: Only one recommendation set active at a time
- **Versioning**: Schema version tracking for updates

### Query Processing
- **Multi-criteria matching**: Age, sex, life stage, risk factors
- **Branch logic**: Product-specific and condition-specific schedules
- **Dose status calculation**: Due, overdue, not due based on age

## Configuration

### MongoDB
Ensure MongoDB is running and accessible. The system will create the collection `vaccination_recommendations` automatically.

### Application Properties
No special configuration required beyond standard Spring Boot MongoDB settings.

## Error Handling

The system provides comprehensive error handling:
- **Validation errors**: Invalid data format or missing required fields
- **Service errors**: Database connection issues or processing failures
- **HTTP status codes**: Appropriate status codes for different error types
- **Detailed logging**: Comprehensive logging for debugging and monitoring

## Performance Considerations

- **Efficient queries**: Single document retrieval for recommendations
- **Caching potential**: Recommendations can be cached for frequently requested criteria
- **Scalability**: Document-based structure supports large recommendation sets
- **Indexing**: MongoDB indexes on creation date for efficient retrieval

## Security

- **Input validation**: Comprehensive validation of all input data
- **No authentication**: Public endpoints (consider adding if needed)
- **Data sanitization**: Proper handling of user input
- **Audit logging**: Comprehensive logging of all operations

## Future Enhancements

Potential improvements:
- **Historical tracking**: Track changes in recommendations over time
- **User preferences**: Save and retrieve user-specific preferences
- **Notification system**: Alert users about upcoming or overdue vaccines
- **Integration**: Connect with immunization registries and EHR systems
- **Analytics**: Track recommendation effectiveness and usage patterns
- **Multi-language support**: Support for multiple languages and regions
- **Advanced filtering**: More sophisticated risk factor assessment
- **Batch operations**: Support for bulk recommendation queries

## Support and Maintenance

### Monitoring
- Health check endpoint for system monitoring
- Comprehensive logging for operational insights
- Performance metrics for optimization

### Updates
- Easy bulk updates via API
- Schema version tracking
- Backward compatibility considerations

### Troubleshooting
- Detailed error messages and logging
- Health check endpoints
- Comprehensive API documentation

This vaccination recommendation system provides a robust, flexible, and comprehensive solution for managing and querying vaccination schedules based on authoritative sources like the CDC. 