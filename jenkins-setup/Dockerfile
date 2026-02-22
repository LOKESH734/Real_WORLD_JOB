FROM docker:24.0.7-cli

# Install base tools
RUN apk add --no-cache \
    curl \
    unzip \
    bash \
    git \
    python3 \
    py3-pip \
    aws-cli

# Install Terraform
RUN curl -LO https://releases.hashicorp.com/terraform/1.6.6/terraform_1.6.6_linux_amd64.zip \
    && unzip terraform_1.6.6_linux_amd64.zip \
    && mv terraform /usr/local/bin/ \
    && rm terraform_1.6.6_linux_amd64.zip

# Install kubectl
RUN curl -LO "https://dl.k8s.io/release/v1.29.0/bin/linux/amd64/kubectl" \
    && chmod +x kubectl \
    && mv kubectl /usr/local/bin/

# Verify installations (optional but pro)
RUN terraform -version && kubectl version --client && aws --version && docker --version
