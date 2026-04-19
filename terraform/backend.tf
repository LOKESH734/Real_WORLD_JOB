terraform {
  backend "s3" {
    bucket         = "jenkins-terraform-state-loki-new"
    key            = "eks/terraform.tfstate"
    region         = "ap-south-1"
    dynamodb_table = "terraform-locks"
    encrypt        = true
  }
}