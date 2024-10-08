# Smart Home Management System

This is my final project for the course **Development of Scalable Production ICT Systems**, conducted by the **Department of Mathematics**, Faculty of Science, University of Zagreb, in collaboration with **Infobip**.

## Once the project is started:

- [Swagger UI - device-management-service](http://localhost:8081/swagger-ui/)

- [Swagger UI - user-management-service](http://localhost:8082/swagger-ui/)

- [RabbitMQ Management UI](http://localhost:15672)  &nbsp; &nbsp; &nbsp;   username | password  &nbsp; &nbsp;  guest | guest

- [Prometheus](http://localhost:9090)

- [Grafana](http://localhost:3000)   &nbsp; &nbsp; &nbsp;  username | password    &nbsp; &nbsp; admin | admin &nbsp; &nbsp; (default)
          
## Project Overview

This system is designed to manage smart home devices and users while facilitating seamless communication between microservices. The system comprises three core microservices, with authentication handled at the API Gateway using an API key filter. The system also includes robust monitoring and event-driven communication.

### Microservices:

1. **Device Management Service**: Manages devices in the smart home and handles user-device associations.
2. **User Management Service**: Handles user registration, API key-based authentication, and user management.
3. **Notification Service**: Sends email notifications using the external [Infobip API](https://www.infobip.com/docs/api#channels/email) based on user and device events.

### Architecture Overview:
- **Microservice architecture** using Spring Boot.
- **RabbitMQ** for event-driven communication between microservices.
- **Feign clients** for direct service-to-service communication.
- **Prometheus** and **Grafana** integrated for metrics collection and visualization, tracking active devices in the system.
- **MariaDB** databases:
  - One for **User Management Service** (storing user data: username, email, API key).
  - One for **Device Management Service** (storing devices and user-device associations).
- **API Gateway** with an API key-based authentication filter for secure routing to downstream services.

## Prerequisites

Ensure you have the following installed on your machine:
- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

## Running the Project

   ```bash
   docker-compose up
  ```

This will start the following services:

- API Gateway (localhost:8080)
- Device Management Service (localhost:8081)
- User Management Service (localhost:8082)
- Notification Service (localhost:8083)
- RabbitMQ Management UI (localhost:15672)
- Prometheus (localhost:9090)
- Grafana (localhost:3000)

The databases (for the User and Device Management services) will be initialized with pre-populated data from [init_user_db.sql](init_user_db.sql) and [init_device_db.sql](init_device_db.sql). 

View all running containers with their names and IDs:

```bash
    docker ps -a
```

Shut down the containers:

```bash
    docker-compose down
```

Health Checks

To monitor the health of the services, you can view the health status by accessing the corresponding `/actuator/health` endpoints. 

To monitor the health of the containers check the container logs: 

```bash
    docker logs <container_id>
```

## Event Flow and Logging

In the system, events play a critical role in coordinating communication between services. For instance, when a user is deleted, the **UserDeletedEvent** is published to both the **Device Management Service** and the **Notification Service**. The **Device Management Service** disassociates all devices linked to that user, while the **Notification Service** sends email notifications to other users associated with those devices. Additionally, when a device's status changes (via the device update endpoint), a **DeviceStatusChangedEvent** is published to the **Notification Service**, triggering notifications to all users associated with that device.

To verify event processing, you can check the logs of each service container using `docker logs <container_id>`. The logs will show event handling details, including when the notification emails are triggered and when devices are disassociated.

### Handling Race Conditions and Concurrency

In developing this system, special attention was given to potential race conditions and concurrent event processing. When processing the **UserDeletedEvent**, steps were taken to ensure that the email addresses for notifications are fetched before the user and their associations are deleted from the database. Additionally, checks are in place to verify if users were deleted concurrently before emails are sent. Similarly, during the processing of the **DeviceStatusChangedEvent**, it is ensured that the device hasn’t been deleted before sending notifications. User associations with the device are fetched just before sending emails to account for any changes in user-device associations that may have occurred in the meantime. In both cases, logs are generated to track event processing, and checks ensure the system handles concurrent deletions and status changes gracefully.

## Custom Metric: Active Devices Gauge

I implemented a custom **active_devices** gauge metric that tracks the number of active devices in the system. You can visualize this metric using Prometheus and Grafana. To see this metric, navigate to the Prometheus UI at http://localhost:9090 and search for `active_devices`. The metric is also available for visualization on Grafana, where it’s displayed on the dashboard.

## Example HTTP Requests

All requests (excluding user registration) require an X-API-KEY header for authentication.

User Registration (No API Key Required):
```bash
curl -X POST http://localhost:8080/users/register \
-H "Content-Type: application/json" \
-d '{"username": "newuser", "email": "doris.djivanovic@gmail.com"}'
```

**Email Limitation**: For user creation, you must use the email `doris.djivanovic@gmail.com` due to limitations of the **Infobip trial account**.

Create a New Device:

```bash
curl -X POST http://localhost:8080/devices \
-H "X-API-KEY: 123e4567-e89b-12d3-a456-426614174001" \
-H "Content-Type: application/json" \
-d '{"name": "Living Room Thermostat", "location": "Living Room", "status": "active"}'
```

Get All Devices:

```bash
curl -X GET http://localhost:8080/devices \
-H "X-API-KEY: 123e4567-e89b-12d3-a456-426614174001"
```

Associate a User with a Device:

```bash
curl -X POST http://localhost:8080/devices/2/users/1/associate \
-H "X-API-KEY: 123e4567-e89b-12d3-a456-426614174001" 
```

Delete a User (Triggers Notification Service):

```bash
curl -X DELETE http://localhost:8080/users/1 \
-H "X-API-KEY: 123e4567-e89b-12d3-a456-426614174001"
```

## TO DO

- Ensuring databases persistence across container restarts by configuring Docker volumes
- Integration with custom domains for Infobip email service
- Adding unit and integration tests
   
  



