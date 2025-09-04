# 🎯 Goal Tracker - Spring Boot Application

A modern, motivational goal tracking application built with Spring Boot, JSP, and Bootstrap 5. Track your daily progress, set goals, and achieve your dreams!

## ✨ Features

- **User Authentication**: Register, login, and manage your profile
- **Goal Management**: Create, edit, and delete goals with custom durations
- **Progress Tracking**: Visual calendar interface to track daily progress
- **Smart Validation**: 1-year goal duration limit with client and server-side validation
- **Responsive Design**: Modern UI that works on all devices
- **Real-time Updates**: Dynamic UI updates without page refresh
- **Data Persistence**: PostgreSQL database for reliable data storage

## 🚀 Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- PostgreSQL (for production)

### Local Development

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd goal-tracker
   ```

2. **Run with H2 Database (Development)**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Run with PostgreSQL (Local)**
   ```bash
   # Start PostgreSQL with Docker
   docker-compose up -d
   
   # Run the application
   $env:JAVA_HOME="C:\jdk\jdk-21.0.7"; $env:SPRING_PROFILES_ACTIVE="local-prod"; .\mvnw.cmd spring-boot:run
   ```

4. **Access the application**
   - Open: http://localhost:8080
   - Register a new account
   - Start creating and tracking your goals!

## 🐳 Docker Deployment

### Local Docker Testing
```bash
# Build the Docker image
docker build -t goal-tracker .

# Run the container
docker run -p 8080:8080 goal-tracker
```

## ☁️ Deploy to Render

### Option 1: Using render.yaml (Recommended)

1. **Push your code to GitHub**
   ```bash
   git add .
   git commit -m "Add Docker deployment configuration"
   git push origin main
   ```

2. **Deploy on Render**
   - Go to [Render Dashboard](https://dashboard.render.com)
   - Click "New +" → "Blueprint"
   - Connect your GitHub repository
   - Render will automatically detect `render.yaml` and deploy both the database and web service

### Option 2: Manual Deployment

1. **Create PostgreSQL Database**
   - Go to Render Dashboard
   - Click "New +" → "PostgreSQL"
   - Choose "Free" plan
   - Note down the connection details

2. **Create Web Service**
   - Click "New +" → "Web Service"
   - Connect your GitHub repository
   - Choose "Docker" environment
   - Set build command: `docker build -t goal-tracker .`
   - Set start command: `docker run -p $PORT:8080 goal-tracker`

3. **Configure Environment Variables**
   ```
   SPRING_PROFILES_ACTIVE=prod
   DATABASE_URL=<your-postgresql-connection-string>
   DB_USERNAME=<your-db-username>
   DB_PASSWORD=<your-db-password>
   ```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/goaltracker/
│   │   ├── controller/          # Web controllers
│   │   ├── service/            # Business logic
│   │   ├── repository/         # Data access
│   │   ├── entity/            # JPA entities
│   │   └── dto/               # Data transfer objects
│   ├── resources/
│   │   ├── static/            # CSS, JS, images
│   │   ├── application.properties
│   │   └── data.sql           # Initial data
│   └── webapp/
│       └── WEB-INF/views/     # JSP templates
└── test/                      # Unit tests
```

## 🔧 Configuration

### Application Properties

- **Development**: Uses H2 in-memory database
- **Local Production**: Uses local PostgreSQL
- **Production**: Uses Render PostgreSQL

### Key Features

- **Goal Duration Limit**: Maximum 1 year (365 days)
- **Date Validation**: Client and server-side validation
- **User Isolation**: Each user sees only their own goals
- **Progress Tracking**: Visual calendar with real-time updates

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run specific test
./mvnw test -Dtest=GoalServiceTest
```

## 📝 API Endpoints

### Web Pages
- `GET /` - Home page with goals list
- `GET /goals` - Goals list page
- `GET /goals/{id}` - Goal details page
- `GET /goals/{id}/edit` - Edit goal page
- `GET /auth/login` - Login page
- `GET /auth/register` - Registration page
- `GET /auth/profile` - User profile page

### REST API
- `POST /api/goals/{id}/toggle-day` - Toggle goal day completion
- `GET /api/goals/{id}/progress` - Get goal progress

## 🛠️ Technologies Used

- **Backend**: Spring Boot 3.x, Spring Data JPA, Spring Security
- **Frontend**: JSP, Bootstrap 5, JavaScript, CSS3
- **Database**: PostgreSQL (production), H2 (development)
- **Build Tool**: Maven
- **Containerization**: Docker
- **Deployment**: Render

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

If you encounter any issues:
1. Check the application logs
2. Verify database connectivity
3. Ensure all environment variables are set correctly
4. Check the browser console for JavaScript errors

---

**Happy Goal Tracking! 🎯✨**
