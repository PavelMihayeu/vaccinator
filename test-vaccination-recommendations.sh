#!/bin/bash

# Test script for vaccination recommendation endpoints
# Make sure the application is running before executing this script

BASE_URL="http://localhost:8080/api/v1/vaccination-recommendations"

echo "Testing Vaccination Recommendation API endpoints..."
echo "=================================================="

# Test 1: Health check
echo -e "\n1. Testing health check..."
response=$(curl -s -X GET "${BASE_URL}/health" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}")

echo "$response"

# Test 2: Get recommendation metadata
echo -e "\n2. Testing recommendation metadata..."
response=$(curl -s -X GET "${BASE_URL}/metadata" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}")

echo "$response"

# Test 3: Get all available vaccines
echo -e "\n3. Testing get all available vaccines..."
response=$(curl -s -X GET "${BASE_URL}/vaccines" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}")

echo "$response"

# Test 4: Get recommendations by query parameters (2-year-old child)
echo -e "\n4. Testing recommendations for 2-year-old child..."
response=$(curl -s -X GET "${BASE_URL}/recommendations?ageYears=2&lifeStage=child" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}")

echo "$response"

# Test 5: Get recommendations by query parameters (immunocompromised adult)
echo -e "\n5. Testing recommendations for immunocompromised adult..."
response=$(curl -s -X GET "${BASE_URL}/recommendations?ageYears=30&immunocompromised=true" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}")

echo "$response"

# Test 6: Get recommendations by query parameters (pregnant woman)
echo -e "\n6. Testing recommendations for pregnant woman..."
response=$(curl -s -X GET "${BASE_URL}/recommendations?pregnancyStatus=pregnant&sex=female" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}")

echo "$response"

echo -e "\n=================================================="
echo "Vaccination recommendation testing completed!"
echo -e "\nNote: To test bulk-load endpoint, you need to provide a JSON file with vaccination data."
echo "Example bulk-load command:"
echo "curl -X POST \"${BASE_URL}/bulk-load\" \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -d @vaccination-recommendations.json"

