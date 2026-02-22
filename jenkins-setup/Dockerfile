FROM docker:24.0.7-cli

# Install tools
RUN apk add --no-cache \
    curl \
    unzip \
    bash \
    git \
    python3 \
    py3-pip \
    aws-cli

# Install kubectl
RUN curl -LO "https://dl.k8s.io/release/v1.29.0/bin/linux/amd64/kubectl" \
    && chmod +x kubectl \
    && mv kubectl /usr/local/bin/
