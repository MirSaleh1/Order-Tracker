# OrderTracker — E-Commerce Order Management Backend with Webhook Integration

OrderTracker is a comprehensive backend system designed for managing e-commerce orders with real-time webhook status updates and asynchronous email notifications.

## Features
- **JWT Authentication & Authorization**: Role-based access control (`ROLE_USER`, `ROLE_ADMIN`).
- **Webhook Integration**: Real-time event notifications for payments (`/api/webhooks/payment`) and shipments (`/api/webhooks/shipment`).
- **Order Status Automation**: Automatic order status updates upon receiving webhook callbacks.
- **Asynchronous Email Notifications**: Non-blocking transactional emails sent via Spring Mail and `@Async`.
- **Webhook Event Logging & Audit**: Filterable, paginated audit logs for administrators (`/api/v1/admin/webhook-logs`).
- **API Documentation**: Interactive Swagger UI at `http://localhost:8080/swagger-ui/index.html`.

## Webhook Integration (Stripe CLI)
To test webhook endpoints locally using the Stripe CLI:

1. **Install Stripe CLI**: Follow instructions at [Stripe CLI GitHub](https://github.com/stripe/stripe-cli#installation)
2. **Login**: Authenticate your account:
   ```bash
   stripe login
   ```
3. **Forward Webhooks**: Forward payment events to your local endpoint:
   ```bash
   stripe listen --forward-to localhost:8080/api/webhooks/payment
   ```

## Running with Docker Compose
To run both PostgreSQL and the Spring Boot application together:
```bash
docker compose up --build
```
