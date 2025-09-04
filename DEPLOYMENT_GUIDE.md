# 🚀 Goal Tracker Deployment Guide

## 📋 Overview

This guide explains how to deploy your Goal Tracker application to Render with proper database setup and user authentication.

## 🔍 Current Data Storage Issues

### **Problem 1: Data Loss on Render**
- **Current Setup**: H2 file-based database (`./data/goaltracker.mv.db`)
- **Issue**: Render uses ephemeral file systems - files are deleted on app restart
- **Result**: All data is lost on every deployment

### **Problem 2: No User Isolation**
- **Current Setup**: Single database shared by all users
- **Issue**: Anyone can see/edit anyone's goals
- **Result**: Complete privacy breach

## 🛠️ Solutions Implemented

### **1. PostgreSQL Database Support**
✅ **Added PostgreSQL dependency** to `pom.xml`
✅ **Created production config** (`application-prod.properties`)
✅ **Environment-based configuration** for Render deployment

### **2. User Authentication System**
✅ **Created User entity** for account management
✅ **Updated Goal entity** with user relationship
✅ **Data isolation** - users only see their own goals

## 🚀 Deployment Steps

### **Step 1: Set Up PostgreSQL Database**

#### **Option A: Render PostgreSQL (Recommended)**
1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click **"New +"** → **"PostgreSQL"**
3. Configure:
   - **Name**: `goal-tracker-db`
   - **Database**: `goaltracker`
   - **User**: `goaltracker_user`
   - **Region**: Choose closest to you
4. **Save** and note the connection details

#### **Option B: External PostgreSQL Service**
- **Railway**: https://railway.app/
- **Supabase**: https://supabase.com/
- **Neon**: https://neon.tech/

### **Step 2: Deploy to Render**

1. **Push your code** to GitHub
2. **Create new Web Service** on Render:
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -jar target/goal-tracker-1.0.0.jar --spring.profiles.active=prod`
   - **Environment**: Java

3. **Add Environment Variables**:
   ```
   SPRING_PROFILES_ACTIVE=prod
   DATABASE_URL=jdbc:postgresql://your-postgres-host:5432/goaltracker
   DB_USERNAME=your_username
   DB_PASSWORD=your_password
   PORT=8080
   ```

### **Step 3: Database Migration**

The application will automatically:
- **Create tables** on first startup (`ddl-auto=update`)
- **Set up user authentication** system
- **Migrate existing data** (if any)

## 🔐 User Authentication Features

### **Current Implementation**
- **User Registration**: Users can create accounts
- **Data Isolation**: Each user sees only their goals
- **Session Management**: Secure login/logout

### **Security Benefits**
- ✅ **Private Goals**: Users can't see others' goals
- ✅ **Secure Access**: Password-protected accounts
- ✅ **Data Persistence**: Goals survive app restarts

## 📊 Data Storage Comparison

| Aspect | Current (H2) | Production (PostgreSQL) |
|--------|-------------|------------------------|
| **Persistence** | ❌ Lost on restart | ✅ Permanent storage |
| **Multi-user** | ❌ Shared data | ✅ Isolated per user |
| **Security** | ❌ No authentication | ✅ User accounts |
| **Scalability** | ❌ File-based | ✅ Cloud database |
| **Backup** | ❌ Manual | ✅ Automatic |

## 🎯 Next Steps

### **Immediate Actions**
1. **Set up PostgreSQL** database on Render
2. **Deploy application** with production profile
3. **Test user registration** and goal creation
4. **Verify data persistence** across restarts

### **Future Enhancements**
- **Email verification** for new accounts
- **Password reset** functionality
- **Social login** (Google, GitHub)
- **Data export** feature
- **Goal sharing** between users

## 🔧 Troubleshooting

### **Common Issues**

**Issue**: Database connection failed
```
Solution: Check DATABASE_URL, DB_USERNAME, DB_PASSWORD environment variables
```

**Issue**: Tables not created
```
Solution: Ensure SPRING_PROFILES_ACTIVE=prod is set
```

**Issue**: Users can't register
```
Solution: Check if User entity is properly configured
```

### **Useful Commands**

**Check application logs**:
```bash
# On Render dashboard or via CLI
render logs --service your-service-name
```

**Test database connection**:
```bash
# Connect to PostgreSQL
psql $DATABASE_URL
```

## 📞 Support

If you encounter issues:
1. **Check Render logs** for error messages
2. **Verify environment variables** are set correctly
3. **Test database connection** manually
4. **Review application-prod.properties** configuration

---

**🎉 Your Goal Tracker will now have:**
- ✅ **Persistent data storage**
- ✅ **User authentication**
- ✅ **Data privacy**
- ✅ **Production-ready deployment**
