#!/bin/bash

# Docker Hub configuration
DOCKER_USERNAME="your-username"  # Replace with your Docker Hub username
REPOSITORY_NAME="vaccinator-api"
VERSION="1.0.0"

echo "🐳 Building and pushing Vaccinator API to Docker Hub..."

# Check if user is logged in to Docker Hub
if ! docker info | grep -q "Username"; then
    echo "❌ Not logged in to Docker Hub. Please run 'docker login' first."
    exit 1
fi

# Build the image
echo "🔨 Building Docker image..."
docker build -t ${REPOSITORY_NAME}:latest .

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

# Tag images
echo "🏷️  Tagging images..."
docker tag ${REPOSITORY_NAME}:latest ${DOCKER_USERNAME}/${REPOSITORY_NAME}:latest
docker tag ${REPOSITORY_NAME}:latest ${DOCKER_USERNAME}/${REPOSITORY_NAME}:${VERSION}

# Push to Docker Hub
echo "📤 Pushing to Docker Hub..."
docker push ${DOCKER_USERNAME}/${REPOSITORY_NAME}:latest
docker push ${DOCKER_USERNAME}/${REPOSITORY_NAME}:${VERSION}

if [ $? -eq 0 ]; then
    echo "✅ Successfully pushed to Docker Hub!"
    echo "📦 Image: ${DOCKER_USERNAME}/${REPOSITORY_NAME}:latest"
    echo "📦 Image: ${DOCKER_USERNAME}/${REPOSITORY_NAME}:${VERSION}"
else
    echo "❌ Push failed!"
    exit 1
fi

