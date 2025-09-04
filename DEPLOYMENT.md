# 🚀 Deployment Guide - Render

This guide will help you deploy your Goal Tracker application to Render using Docker.

## 📋 Prerequisites

1. **GitHub Account**: Your code must be in a GitHub repository
2. **Render Account**: Sign up at [render.com](https://render.com)
3. **Docker Knowledge**: Basic understanding of Docker concepts

## 🐳 Docker Configuration

The application is already configured with Docker:

- **Dockerfile**: Multi-stage build for optimal image size
- **.dockerignore**: Excludes unnecessary files
- **render.yaml**: Blueprint for automatic deployment

## 🚀 Deployment Steps

### Step 1: Prepare Your Repository

1. **Ensure all files are committed**:
   ```bash
   git add .
   git commit -m "Add Docker deployment configuration"
   git push origin main
   ```

2. **Verify these files exist in your repository**:
   - `Dockerfile`
   - `.dockerignore`
   - `render.yaml`
   - `application-prod.properties`
   - `pom.xml`
   - `src/` directory

### Step 2: Deploy Using Blueprint (Recommended)

1. **Go to Render Dashboard**:
   - Visit [dashboard.render.com](https://dashboard.render.com)
   - Sign in to your account

2. **Create Blueprint**:
   - Click "New +" button
   - Select "Blueprint"
   - Connect your GitHub repository
   - Render will automatically detect `render.yaml`

3. **Review Configuration**:
   - **Database**: PostgreSQL (Free plan)
   - **Web Service**: Docker container (Free plan)
   - **Environment Variables**: Automatically configured

4. **Deploy**:
   - Click "Apply" to start deployment
   - Wait for both services to be ready (5-10 minutes)

### Step 3: Manual Deployment (Alternative)

If you prefer manual deployment:

#### Create PostgreSQL Database

1. **New PostgreSQL**:
   - Click "New +" → "PostgreSQL"
   - Name: `goal-tracker-db`
   - Plan: Free
   - Region: Choose closest to your users

2. **Note Connection Details**:
   - Internal Database URL
   - External Database URL
   - Username
   - Password

#### Create Web Service

1. **New Web Service**:
   - Click "New +" → "Web Service"
   - Connect your GitHub repository
   - Name: `goal-tracker-app`

2. **Configure Service**:
   - **Environment**: Docker
   - **Region**: Same as database
   - **Branch**: main
   - **Build Command**: `docker build -t goal-tracker .`
   - **Start Command**: `docker run -p $PORT:8080 goal-tracker`

3. **Environment Variables**:
   ```
   SPRING_PROFILES_ACTIVE=prod
   DATABASE_URL=<your-postgresql-internal-url>
   DB_USERNAME=<your-db-username>
   DB_PASSWORD=<your-db-password>
   ```

## 🔧 Configuration Details

### Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `prod` |
| `DATABASE_URL` | PostgreSQL connection string | `postgresql://...` |
| `DB_USERNAME` | Database username | `goal_tracker_user` |
| `DB_PASSWORD` | Database password | `secure_password` |
| `PORT` | Application port | `8080` (auto-set by Render) |

### Database Configuration

The application uses PostgreSQL with these settings:
- **Connection Pool**: HikariCP (max 10 connections)
- **Schema**: Auto-generated from JPA entities
- **SSL**: Enabled for security

### Security Features

- **HTTPS**: Automatically enabled by Render
- **Session Management**: Secure cookies with 30-minute timeout
- **Password Hashing**: BCrypt with strength 12
- **SQL Injection Protection**: JPA/Hibernate parameterized queries

## 🌐 Access Your Application

After successful deployment:

1. **Get Your URL**:
   - Go to your web service dashboard
   - Copy the provided URL (e.g., `https://goal-tracker-app.onrender.com`)

2. **Test the Application**:
   - Visit the URL
   - Register a new account
   - Create and track goals
   - Verify all features work

## 📊 Monitoring & Logs

### View Logs

1. **Application Logs**:
   - Go to your web service dashboard
   - Click "Logs" tab
   - Monitor startup and runtime logs

2. **Database Logs**:
   - Go to your PostgreSQL dashboard
   - Click "Logs" tab
   - Monitor database connections and queries

### Health Checks

- **Endpoint**: `/` (root path)
- **Expected Response**: 200 OK
- **Check Frequency**: Every 30 seconds

## 🔄 Updates & Maintenance

### Deploy Updates

1. **Push Changes**:
   ```bash
   git add .
   git commit -m "Update application"
   git push origin main
   ```

2. **Automatic Deployment**:
   - Render automatically detects changes
   - Builds new Docker image
   - Deploys updated application

### Database Migrations

- **Automatic**: JPA `ddl-auto=update` handles schema changes
- **Manual**: Use Flyway or Liquibase for complex migrations

## 🛠️ Troubleshooting

### Common Issues

1. **Build Failures**:
   - Check Dockerfile syntax
   - Verify all dependencies in `pom.xml`
   - Check build logs for specific errors

2. **Database Connection Issues**:
   - Verify environment variables
   - Check database status
   - Ensure correct connection string format

3. **Application Startup Failures**:
   - Check application logs
   - Verify database connectivity
   - Check port configuration

### Debug Commands

```bash
# Test Docker build locally
docker build -t goal-tracker .

# Test Docker run locally
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=prod goal-tracker

# Check container logs
docker logs <container-id>
```

## 📈 Scaling

### Free Plan Limitations

- **Web Service**: 750 hours/month
- **Database**: 90 days max
- **Bandwidth**: 100GB/month

### Upgrade Options

1. **Paid Plans**: Better performance and reliability
2. **Custom Domains**: Use your own domain name
3. **SSL Certificates**: Automatic HTTPS
4. **Backup**: Automated database backups

## 🔒 Security Best Practices

1. **Environment Variables**: Never commit secrets to Git
2. **Database Access**: Use internal URLs when possible
3. **HTTPS**: Always enabled in production
4. **Regular Updates**: Keep dependencies updated

## 📞 Support

- **Render Documentation**: [docs.render.com](https://docs.render.com)
- **Community Forum**: [community.render.com](https://community.render.com)
- **GitHub Issues**: Report bugs in your repository

---

**Your Goal Tracker is now live! 🎉**

Users can register, login, and track their goals just like in your local environment.
