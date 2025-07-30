#!/bin/bash

echo "Testing Vaccine Data Loading API..."

# Test data status first
echo "1. Getting current data status..."
curl -X GET "http://localhost:8080/api/admin/data/status" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}\n\n"

echo "2. Loading vaccine data..."
curl -X POST "http://localhost:8080/api/admin/data/load-vaccines" \
  -H "Content-Type: application/json" \
  -d @test-vaccine-data.json \
  -w "\nHTTP Status: %{http_code}\n\n"

echo "3. Getting updated data status..."
curl -X GET "http://localhost:8080/api/admin/data/status" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}\n\n" 