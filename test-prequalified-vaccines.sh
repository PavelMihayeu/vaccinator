#!/bin/bash

# Test script for Prequalified Vaccines API

API_BASE_URL="http://localhost:8080"
API_KEY="your-super-secret-api-key-here-change-in-production"

echo "🧪 Testing Prequalified Vaccines API"
echo "==================================="
echo ""

# Test 1: Bulk load prequalified vaccines
echo "📋 Test 1: Bulk load prequalified vaccines from CSV"
echo "---------------------------------------------------"
curl -s -X POST "$API_BASE_URL/api/v1/prequalified-vaccines/bulk-load" \
     -H "X-API-Key: $API_KEY" \
     -H "Content-Type: application/json" \
     -d '{"replaceExisting": true}' | jq '.'
echo ""

# Wait a moment for processing
sleep 2

# Test 2: Get total count
echo "📋 Test 2: Get total count of prequalified vaccines"
echo "---------------------------------------------------"
curl -s -X GET "$API_BASE_URL/api/v1/prequalified-vaccines/count" \
     -H "X-API-Key: $API_KEY" | jq '.'
echo ""

# Test 3: Get all prequalified vaccines (first 5)
echo "📋 Test 3: Get all prequalified vaccines (first 5)"
echo "---------------------------------------------------"
curl -s -X GET "$API_BASE_URL/api/v1/prequalified-vaccines" \
     -H "X-API-Key: $API_KEY" | jq '.[0:5]'
echo ""

# Test 4: Get vaccines by type
echo "📋 Test 4: Get vaccines by type (BCG)"
echo "-------------------------------------"
curl -s -X GET "$API_BASE_URL/api/v1/prequalified-vaccines/by-type/BCG" \
     -H "X-API-Key: $API_KEY" | jq '.[0:3]'
echo ""

# Test 5: Get vaccines by manufacturer
echo "📋 Test 5: Get vaccines by manufacturer"
echo "--------------------------------------"
curl -s -X GET "$API_BASE_URL/api/v1/prequalified-vaccines/by-manufacturer/Serum%20Institute%20of%20India%20Pvt.%20Ltd." \
     -H "X-API-Key: $API_KEY" | jq '.[0:3]'
echo ""

# Test 6: Get vaccines by NRA
echo "📋 Test 6: Get vaccines by NRA"
echo "------------------------------"
curl -s -X GET "$API_BASE_URL/api/v1/prequalified-vaccines/by-nra/Central%20Drugs%20Standard%20Control%20Organization" \
     -H "X-API-Key: $API_KEY" | jq '.[0:3]'
echo ""

echo "✅ Prequalified Vaccines API Tests Complete!"
echo ""
echo "🌐 Swagger UI: $API_BASE_URL/swagger-ui/index.html"
echo "📖 API Docs: $API_BASE_URL/api-docs" 