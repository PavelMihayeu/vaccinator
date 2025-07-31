#!/bin/bash

# Test script for Vaccinator API
# Make sure the application is running on localhost:8080

echo "Testing Vaccinator API..."

# Base URL
BASE_URL="http://localhost:8080"

echo "1. Getting data status..."
curl -X GET "$BASE_URL/api/admin/data/status" | jq .

echo -e "\n2. Loading default WHO vaccination data..."
curl -X POST "$BASE_URL/api/admin/data/load-default-who-data" | jq .

echo -e "\n3. Getting updated data status..."
curl -X GET "$BASE_URL/api/admin/data/status" | jq .

echo -e "\n4. Getting all vaccines..."
curl -X GET "$BASE_URL/api/vaccines" | jq .

echo -e "\nTest completed!" 