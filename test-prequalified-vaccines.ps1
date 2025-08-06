# Test script for Prequalified Vaccines API (PowerShell)

$API_BASE_URL = "http://localhost:8080"
$API_KEY = "your-super-secret-api-key-here-change-in-production"

Write-Host "🧪 Testing Prequalified Vaccines API" -ForegroundColor Green
Write-Host "===================================" -ForegroundColor Green
Write-Host ""

# Test 1: Bulk load prequalified vaccines
Write-Host "📋 Test 1: Bulk load prequalified vaccines from CSV" -ForegroundColor Yellow
Write-Host "---------------------------------------------------" -ForegroundColor Yellow
$body = @{
    replaceExisting = $true
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "$API_BASE_URL/api/v1/prequalified-vaccines/bulk-load" `
    -Method POST `
    -Headers @{
        "X-API-Key" = $API_KEY
        "Content-Type" = "application/json"
    } `
    -Body $body

$response | ConvertTo-Json -Depth 10
Write-Host ""

# Wait a moment for processing
Start-Sleep -Seconds 2

# Test 2: Get total count
Write-Host "📋 Test 2: Get total count of prequalified vaccines" -ForegroundColor Yellow
Write-Host "---------------------------------------------------" -ForegroundColor Yellow
$count = Invoke-RestMethod -Uri "$API_BASE_URL/api/v1/prequalified-vaccines/count" `
    -Method GET `
    -Headers @{
        "X-API-Key" = $API_KEY
    }
Write-Host "Total count: $count"
Write-Host ""

# Test 3: Get all prequalified vaccines (first 5)
Write-Host "📋 Test 3: Get all prequalified vaccines (first 5)" -ForegroundColor Yellow
Write-Host "---------------------------------------------------" -ForegroundColor Yellow
$vaccines = Invoke-RestMethod -Uri "$API_BASE_URL/api/v1/prequalified-vaccines" `
    -Method GET `
    -Headers @{
        "X-API-Key" = $API_KEY
    }
$vaccines[0..4] | ConvertTo-Json -Depth 10
Write-Host ""

# Test 4: Get vaccines by type
Write-Host "📋 Test 4: Get vaccines by type (BCG)" -ForegroundColor Yellow
Write-Host "-------------------------------------" -ForegroundColor Yellow
$bcgVaccines = Invoke-RestMethod -Uri "$API_BASE_URL/api/v1/prequalified-vaccines/by-type/BCG" `
    -Method GET `
    -Headers @{
        "X-API-Key" = $API_KEY
    }
$bcgVaccines[0..2] | ConvertTo-Json -Depth 10
Write-Host ""

# Test 5: Get vaccines by manufacturer
Write-Host "📋 Test 5: Get vaccines by manufacturer" -ForegroundColor Yellow
Write-Host "--------------------------------------" -ForegroundColor Yellow
$manufacturerVaccines = Invoke-RestMethod -Uri "$API_BASE_URL/api/v1/prequalified-vaccines/by-manufacturer/Serum%20Institute%20of%20India%20Pvt.%20Ltd." `
    -Method GET `
    -Headers @{
        "X-API-Key" = $API_KEY
    }
$manufacturerVaccines[0..2] | ConvertTo-Json -Depth 10
Write-Host ""

# Test 6: Get vaccines by NRA
Write-Host "📋 Test 6: Get vaccines by NRA" -ForegroundColor Yellow
Write-Host "------------------------------" -ForegroundColor Yellow
$nraVaccines = Invoke-RestMethod -Uri "$API_BASE_URL/api/v1/prequalified-vaccines/by-nra/Central%20Drugs%20Standard%20Control%20Organization" `
    -Method GET `
    -Headers @{
        "X-API-Key" = $API_KEY
    }
$nraVaccines[0..2] | ConvertTo-Json -Depth 10
Write-Host ""

Write-Host "✅ Prequalified Vaccines API Tests Complete!" -ForegroundColor Green
Write-Host ""
Write-Host "🌐 Swagger UI: $API_BASE_URL/swagger-ui/index.html" -ForegroundColor Cyan
Write-Host "📖 API Docs: $API_BASE_URL/api-docs" -ForegroundColor Cyan 