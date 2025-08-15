# Docker Hub configuration
$DOCKER_USERNAME = "your-username"  # Replace with your Docker Hub username
$REPOSITORY_NAME = "vaccinator-api"
$VERSION = "1.0.0"

Write-Host "🐳 Building and pushing Vaccinator API to Docker Hub..." -ForegroundColor Green

# Check if user is logged in to Docker Hub
try {
    $dockerInfo = docker info 2>&1
    if ($dockerInfo -match "Username") {
        Write-Host "✅ Logged in to Docker Hub" -ForegroundColor Green
    } else {
        Write-Host "❌ Not logged in to Docker Hub. Please run 'docker login' first." -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "❌ Docker not running or not installed" -ForegroundColor Red
    exit 1
}

# Build the image
Write-Host "🔨 Building Docker image..." -ForegroundColor Yellow
docker build -t ${REPOSITORY_NAME}:latest .

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Build failed!" -ForegroundColor Red
    exit 1
}

# Tag images
Write-Host "🏷️  Tagging images..." -ForegroundColor Yellow
docker tag ${REPOSITORY_NAME}:latest ${DOCKER_USERNAME}/${REPOSITORY_NAME}:latest
docker tag ${REPOSITORY_NAME}:latest ${DOCKER_USERNAME}/${REPOSITORY_NAME}:${VERSION}

# Push to Docker Hub
Write-Host "📤 Pushing to Docker Hub..." -ForegroundColor Yellow
docker push ${DOCKER_USERNAME}/${REPOSITORY_NAME}:latest
docker push ${DOCKER_USERNAME}/${REPOSITORY_NAME}:${VERSION}

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Successfully pushed to Docker Hub!" -ForegroundColor Green
    Write-Host "📦 Image: ${DOCKER_USERNAME}/${REPOSITORY_NAME}:latest" -ForegroundColor Cyan
    Write-Host "📦 Image: ${DOCKER_USERNAME}/${REPOSITORY_NAME}:${VERSION}" -ForegroundColor Cyan
} else {
    Write-Host "❌ Push failed!" -ForegroundColor Red
    exit 1
}

