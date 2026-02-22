FROM jenkins/jenkins:lts

USER root

RUN apt-get update && apt-get install -y wget unzip curl

# Terraform
RUN wget https://releases.hashicorp.com/terraform/1.6.6/terraform_1.6.6_linux_amd64.zip \
    && unzip terraform_1.6.6_linux_amd64.zip \
    && mv terraform /usr/local/bin/ \
    && rm terraform_1.6.6_linux_amd64.zip

# AWS CLI
RUN apt-get install -y awscli

# kubectl
RUN curl -LO "https://dl.k8s.io/release/v1.29.0/bin/linux/amd64/kubectl" \
    && install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

USER jenkins
