locals {
  common_tags = {
    Environment = var.environment
    Project     = "realworld"
    Owner       = "Lokesh"
    ManagedBy   = "Terraform"
  }
}
