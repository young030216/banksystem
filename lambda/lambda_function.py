import json
import boto3
from fpdf import FPDF
import io
import os

s3 = boto3.client("s3")
BUCKET = os.environ["BUCKET"]

def lambda_handler(event, context):

    # Basic transaction fields
    transaction_id = event.get("transactionId", "N/A")
    amount = event.get("amount", "N/A")
    timestamp = event.get("timestamp", "N/A")
    status = event.get("status", "N/A")
    description = event.get("description", "")

    # Nested user objects
    from_user = event.get("fromUser", {})
    to_user   = event.get("toUser", {})

    from_user_name  = from_user.get("username", "N/A")
    to_user_name    = to_user.get("username", "N/A")
    from_user_email = from_user.get("email", "N/A")
    to_user_email   = to_user.get("email", "N/A")

    # PDF Generation
    pdf = FPDF()
    pdf.add_page()
    pdf.set_font("Arial", size=12)

    pdf.cell(200, 10, txt="Transaction Receipt", ln=True)

    pdf.cell(200, 10, txt=f"Transaction ID: {transaction_id}", ln=True)
    pdf.cell(200, 10, txt=f"Status: {status}", ln=True)
    pdf.cell(200, 10, txt=f"Amount: {amount}", ln=True)
    pdf.cell(200, 10, txt=f"Timestamp: {timestamp}", ln=True)

    pdf.ln(5)
    pdf.cell(200, 10, txt="From User:", ln=True)
    pdf.cell(200, 10, txt=f" - Username: {from_user_name}", ln=True)
    pdf.cell(200, 10, txt=f" - Email: {from_user_email}", ln=True)

    pdf.ln(2)
    pdf.cell(200, 10, txt="To User:", ln=True)
    pdf.cell(200, 10, txt=f" - Username: {to_user_name}", ln=True)
    pdf.cell(200, 10, txt=f" - Email: {to_user_email}", ln=True)

    pdf.ln(5)
    pdf.multi_cell(0, 10, txt=f"Description: {description}")

    # Save to memory buffer
    buffer = io.BytesIO()
    pdf.output(buffer)
    buffer.seek(0)

    # Upload to S3
    key = f"transactions/{transaction_id}.pdf"
    s3.put_object(
        Bucket=BUCKET,
        Key=key,
        Body=buffer.getvalue(),
        ContentType="application/pdf"
    )

    return {
        "statusCode": 200,
        "message": "PDF generated",
        "s3_key": key,
        "s3_path": f"s3://{BUCKET}/{key}"
    }
