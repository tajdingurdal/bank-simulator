## About the Project

This project is a **simulation bank integration** developed for our e-commerce application. It provides a comprehensive payment transaction simulation platform for testing and development.

## Features

- **Payment Transaction Simulation**: Supports successful and failed payment scenarios
- **Flexible Payment Flow**: Handles transactions with and without OTP authentication
- **API Support**: Provides RESTful APIs for payment processing
- **Easy Setup**: Quickly deployable using Docker
- **Database Management**: Manages database changes with Liquibase

## API Endpoints

### 1. Initialize Payment
- **Endpoint**: `POST /api/v1/payments/initialize`
- **Description**: Starts the payment transaction process
- **Request Body**:
  ```json
  {
    "merchantTransactionCode": "unique-transaction-code",
    "apiKey": "merchant-api-key",
    "amount": 70,
    "currency": "USD"
  }
  ```
- **Response**:
  ```json
  {
    "id": 42,
    "bankTransactionCode": "unique-bank-transaction-code",
    "message": "Transaction subscribed successfully",
    "subscribe": true
  }
  ```

### 2. Authenticate Payment
- **Endpoint**: `POST /api/v1/payments/authenticate-and-prepare-otp`
- **Description**: Authenticates payment and prepares for OTP (if required)
- **Request Body**:
  ```json
  {
    "merchantTransactionCode": "transaction-code",
    "apiKey": "merchant-api-key",
    "bankTransactionCode": "bank-transaction-code",
    "amount": 70,
    "currency": "USD",
    "card": {
      "name": "Customer Name",
      "surname": "Customer Surname",
      "cardNo": "card-number",
      "expiredDate": "MM/YY",
      "cvc": "123",
      "amount": 70,
      "currency": "USD"
    }
  }
  ```
  ![image](https://github.com/user-attachments/assets/da62aa9e-2ee0-40b2-9ccc-52b1a69d7e9f)

- **Response Scenarios**:
  - For amounts ≤ 100 USD:
    ```json
    {
      "status": true,
      "message": "Authentication successful, no OTP required",
      "url": null
    }
    ```
  - For amounts > 100 USD:
    ```json
    {
      "status": true,
      "message": "Authentication successful",
      "url": "otp-verification-url"
    }
    ```
      ![image](https://github.com/user-attachments/assets/0b5ea83f-a421-46df-9c29-fce9558dfdc7)

## Technologies

- Docker
- Liquibase
- Spring Framework 3.2.8
- Maven

## Contributing

If you wish to contribute, please create a pull request or open an issue.

## For more, please check out the project.
