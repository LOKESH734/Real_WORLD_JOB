module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "20.8.4"

  cluster_name    = var.cluster_name
  cluster_version = "1.29"

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets

  # ✅ PUBLIC ACCESS (Important for Codespaces / Jenkins outside VPC)
  cluster_endpoint_public_access  = true
  cluster_endpoint_private_access = true

  # Optional but recommended
  cluster_endpoint_public_access_cidrs = ["0.0.0.0/0"]

  # ✅ New EKS Authentication Mode
  authentication_mode = "API"

  # ✅ Automatically give admin access to creator (jenkins-ci-user)
  enable_cluster_creator_admin_permissions = true

  # ✅ Managed Node Group
  eks_managed_node_groups = {
    default = {
      instance_types = ["t3.small"]   # ⚠️ Free tier NOT supported by EKS properly
      desired_size   = 1
      max_size       = 2
      min_size       = 1
      capacity_type  = "ON_DEMAND"
    }
  }

  tags = {
    Environment = "dev"
    Project     = "realworld"
  }
}
