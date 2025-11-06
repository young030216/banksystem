# Configure the AWS Provider
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}

provider "aws" {
  region = "ap-southeast-2"
}

# Create a new VPC
resource "aws_vpc" "bank_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true
  tags = {
    Name = "bank_vpc"
  }
}

# Create a internet gateway
resource "aws_internet_gateway" "bank_igw" {
  vpc_id = aws_vpc.bank_vpc.id
  tags = {
    Name = "bank_igw"
  }
}

# create a public subnet a
resource "aws_subnet" "bank_public_subnet_a" {
  vpc_id                  = aws_vpc.bank_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "ap-southeast-2a"
  map_public_ip_on_launch = true
  tags = {
    Name = "bank_public_subnet_a"
  }
}

# create a public subnet b
resource "aws_subnet" "bank_public_subnet_b" {
  vpc_id                  = aws_vpc.bank_vpc.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "ap-southeast-2b"
  map_public_ip_on_launch = true
  tags = {
    Name = "bank_public_subnet_b"
  }
}
# Create a route table
resource "aws_route_table" "bank_public_rt" {
  vpc_id = aws_vpc.bank_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.bank_igw.id

  }

  tags = {
    Name = "bank_public_rt"
  }
}
# Associate the route table with the public subnet a & b
resource "aws_route_table_association" "bank_public_rt_assoc_a" {
  subnet_id      = aws_subnet.bank_public_subnet_a.id
  route_table_id = aws_route_table.bank_public_rt.id
}
resource "aws_route_table_association" "bank_public_rt_assoc_b" {
  subnet_id      = aws_subnet.bank_public_subnet_b.id
  route_table_id = aws_route_table.bank_public_rt.id
}
# Create a security group for RDS
resource "aws_security_group" "bank_rds_sg" {
  name        = "bank_rds_sg"
  description = "Security group for RDS instance"
  vpc_id      = aws_vpc.bank_vpc.id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "bank_rds_sg"
  }
}

# Create DB subnet group
resource "aws_db_subnet_group" "bank_db_subnet_group" {
  name       = "bank_db_subnet_group"
  subnet_ids = [aws_subnet.bank_public_subnet_a.id, aws_subnet.bank_public_subnet_b.id]
  tags = {
    Name = "bank_db_subnet_group"
  }
}

# Create a PostgreSQL RDS instance
resource "aws_db_instance" "bank_postgres" {
  identifier            = "bank-postgres-db"
  allocated_storage     = 20
  engine                = "postgres"
  max_allocated_storage = 30
  engine_version        = "16.8"
  instance_class        = "db.t4g.micro"
  db_name               = "bankdb"
  username              = "young"
  password              = "young0216!"

  # Network Settings
  vpc_security_group_ids = [aws_security_group.bank_rds_sg.id]
  db_subnet_group_name   = aws_db_subnet_group.bank_db_subnet_group.name

  skip_final_snapshot = true
  publicly_accessible = true
  tags = {
    Name = "bank_postgres-db"
  }
}

# Create security group for EC2
resource "aws_security_group" "bank_ec2_sg" {
  name   = "bank_ec2_sg"
  vpc_id = aws_vpc.bank_vpc.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "bank_ec2_sg"
  }
}

data "aws_ami" "amazon_linux" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-*-x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

# Create an EC2 instance
resource "aws_instance" "bank_ec2_instance" {

  ami = data.aws_ami.amazon_linux.id

  instance_type = "t3.micro"

  subnet_id = aws_subnet.bank_public_subnet_a.id
  associate_public_ip_address = true

  vpc_security_group_ids = [aws_security_group.bank_ec2_sg.id]

  #User data to install web server
  user_data = <<-EOF
                #!/bin/bash
                yum update -y
                yum install -y docker
                systemctl start docker
                systemctl enable docker
                usermod -a -G docker ec2-user
    EOF

  tags = {
    Name = "bank_app_server"
  }
}

output "rds_endpoint" {
  description = "PostgreSQL database endpoint"
  value       = aws_db_instance.bank_postgres.endpoint
}

output "rds_port" {
  description = "PostgreSQL database port"
  value       = aws_db_instance.bank_postgres.port
}

output "ec2_public_ip" {
  description = "Public IP of the EC2 instance"
  value       = aws_instance.bank_ec2_instance.public_ip
}

output "ec2_public_dns" {
  description = "Public DNS of the EC2 instance"
  value       = aws_instance.bank_ec2_instance.public_dns
}
