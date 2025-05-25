# Mental Health Tracker

Mental Health Tracker is a Spring Boot & Angular application designed to help users monitor and manage their mental
well-being. It includes features such as mood tracking, journaling, and sending wellness reminders via email.

---

## 🌟 Features

- ✅ Track daily mood entries
- 📓 Manage journal entries
- 📬 Send and test email reminders using MailDev
- 🔧 RESTful API built with Spring Boot
- 🗃️ PostgreSQL for persistent data storage

---

## 🚀 Technologies Used

- Java 24
- Spring Boot 3.5.0
- PostgreSQL
- Docker & Docker Compose
- MailDev (for email testing)
- Gradle

---

## ⚙️ Prerequisites

Make sure the following tools are installed on your machine:

- [Java 24](https://www.jetbrains.com/guide/java/tips/download-jdk/) Install guide, make sure to use the Amazon Corretto
  24.0.1
- [Docker](https://www.docker.com/) Install guide, use Docker desktop (no need to create an account for Docker Hub)

---

## 🐳 Setting up Docker Services

This project uses Docker to run PostgreSQL and MailDev (email testing server).
The configuration is available in the `./docker` directory.

### Start the Docker services in detached mode

This will start the database and MailDev containers in detached mode. Ensure Docker is running on your system before
executing this command:

```bash
  docker compose up -d
```

### Stop the Docker services and destroy the volumes

To stop the database and MailDev server and delete the data and container, run this command:

```bash
  docker compose down -v
```

### MailDev

When executing the ```docker compose up -d``` command,
this will start the MailDev service used to mock the sending of emails to users.

You can access the MailDev UI by opening ```localhost:1081``` in your browser.
