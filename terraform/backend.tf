terraform {
  backend "s3" {
    bucket         = "jenkins-terraform-state-loki"
    key            = "eks/terraform.tfstate"
    region         = "ap-south-2"
    dynamodb_table = "terraform-locks"
    encrypt        = true
  }
}
