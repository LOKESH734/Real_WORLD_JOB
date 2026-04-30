module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "20.8.4"

  cluster_name    = var.cluster_name
  cluster_version = "1.30"

  vpc_id     = module.vpc.vpc_id
  subnet_ids = module.vpc.private_subnets

  enable_irsa = true

  cluster_endpoint_public_access  = true
  cluster_endpoint_private_access = true
  cluster_endpoint_public_access_cidrs = ["0.0.0.0/0"]

  # ✅ Logging enabled (NOW SAFE — no deny policy blocking)
  cluster_enabled_log_types = [
    "api",
    "audit",
    "authenticator",
    "controllerManager",
    "scheduler"
  ]

  # ✅ Correct access config
  authentication_mode = "API"
  enable_cluster_creator_admin_permissions = true

  # ✅ Managed Node Group
  eks_managed_node_groups = {
    default = {
      instance_types = [var.node_instance_type]

      desired_size = 1
      min_size     = 1
      max_size     = 2

      capacity_type = "ON_DEMAND"
      disk_size     = 20

      labels = {
        role = "general"
      }

      tags = local.common_tags
    }
  }

  tags = local.common_tags
}