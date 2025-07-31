#!/bin/bash

# Test script for Vaccinator API with Versioning and WHO Guidelines
# Make sure the application is running on localhost:8080

echo "Testing Vaccinator API with Versioning and WHO Guidelines..."

# Base URL
BASE_URL="http://localhost:8080"

echo "1. Testing API health check..."
curl -X GET "$BASE_URL/api/health" | jq .

echo -e "\n2. Getting API information..."
curl -X GET "$BASE_URL/api/info" | jq .

echo -e "\n3. Getting data status..."
curl -X GET "$BASE_URL/api/v1/admin/data/status" | jq .

echo -e "\n4. Loading default WHO vaccination data with guidelines..."
curl -X POST "$BASE_URL/api/v1/admin/data/load-default-who-data" | jq .

echo -e "\n5. Getting updated data status..."
curl -X GET "$BASE_URL/api/v1/admin/data/status" | jq .

echo -e "\n6. Testing V1 API - First request (should hit database)..."
time curl -X GET "$BASE_URL/api/v1/vaccines" > /dev/null

echo -e "\n7. Testing V1 API - Second request (should hit cache)..."
time curl -X GET "$BASE_URL/api/v1/vaccines" > /dev/null

echo -e "\n8. Testing V2 API - Enhanced response format..."
curl -X GET "$BASE_URL/api/v2/vaccines" | jq .

echo -e "\n9. Testing V1 API - Get all vaccines with WHO guidelines..."
curl -X GET "$BASE_URL/api/v1/vaccines" | jq .

echo -e "\n9.1. Testing WHO guideline summary specifically..."
curl -X GET "$BASE_URL/api/v1/vaccines" | jq '.whoGuidelineSummary'

echo -e "\n10. Testing V2 API - Search vaccines..."
curl -X GET "$BASE_URL/api/v2/vaccines/search?name=COVID" | jq .

echo -e "\n11. Testing V1 API - Search vaccines with WHO guidelines..."
curl -X GET "$BASE_URL/api/v1/vaccines/search?name=COVID" | jq .

echo -e "\n12. Testing cache clearing..."
curl -X DELETE "$BASE_URL/api/v1/admin/data/clear-caches" | jq .

echo -e "\n13. Testing cache after clearing - V1 API..."
time curl -X GET "$BASE_URL/api/v1/vaccines" > /dev/null

echo -e "\n14. Testing cache after clearing - V2 API..."
time curl -X GET "$BASE_URL/api/v2/vaccines" > /dev/null

echo -e "\nAPI Versioning and WHO Guidelines Test completed!"
echo "V1 endpoints: /api/v1/vaccines (includes WHO guidelines)"
echo "V2 endpoints: /api/v2/vaccines (enhanced response format)"
echo "API Info: /api/info"
echo "Health Check: /api/health" 