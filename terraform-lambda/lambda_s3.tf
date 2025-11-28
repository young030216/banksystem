# 1. Create S3 bucket for PDFs
resource "aws_s3_bucket" "transaction_pdf_bucket" {
  bucket = "transaction-pdf-${substr(md5(timestamp()), 0, 6)}"
  force_destroy = true

  tags = {
    Name = "transaction-pdf-bucket"
  }
}

resource "aws_s3_bucket_public_access_block" "transaction_pdf_bucket_block" {
  bucket                  = aws_s3_bucket.transaction_pdf_bucket.id
  block_public_acls       = true
  block_public_policy     = true
  ignore_public_acls      = true
  restrict_public_buckets = true
}

# 2. Lambda IAM Role
resource "aws_iam_role" "lambda_pdf_role" {
  name = "lambda_pdf_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = { Service = "lambda.amazonaws.com" }
      Action = "sts:AssumeRole"
    }]
  })
}

# Allow Lambda to upload PDFs to S3
resource "aws_iam_policy" "lambda_pdf_s3_policy" {
  name        = "lambda_pdf_s3_policy"
  
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Action = ["s3:PutObject", "s3:GetObject"]
      Resource = "${aws_s3_bucket.transaction_pdf_bucket.arn}/*"
    }]
  })
}

resource "aws_iam_role_policy_attachment" "attach_pdf_s3" {
  role       = aws_iam_role.lambda_pdf_role.name
  policy_arn = aws_iam_policy.lambda_pdf_s3_policy.arn
}

# CloudWatch Logs
resource "aws_iam_role_policy_attachment" "attach_pdf_logs" {
  role       = aws_iam_role.lambda_pdf_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

# 3. Lambda function
resource "aws_lambda_function" "transaction_pdf_lambda" {
  function_name = "transaction_pdf_lambda"
  handler       = "lambda_function.lambda_handler"
  runtime       = "python3.12"
  timeout       = 30

  role     = aws_iam_role.lambda_pdf_role.arn
  filename = "${path.module}/../lambda/lambda.zip"

  environment {
    variables = {
      BUCKET = aws_s3_bucket.transaction_pdf_bucket.bucket
    }
  }
}

# 4. Outputs
output "lambda_name" {
  value = aws_lambda_function.transaction_pdf_lambda.function_name
}

output "pdf_bucket_name" {
  value = aws_s3_bucket.transaction_pdf_bucket.bucket
}
