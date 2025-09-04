@echo off
echo ========================================
echo Goal Tracker - Local PostgreSQL Test
echo ========================================
echo.

echo Starting PostgreSQL with Docker...
docker-compose up -d postgres

echo.
echo Waiting for PostgreSQL to start...
timeout /t 5 /nobreak > nul

echo.
echo Testing PostgreSQL connection...
echo Database: goaltracker
echo Username: goaltracker_user
echo Password: goaltracker_password
echo Port: 5432
echo.

echo Starting Goal Tracker with local-prod profile...
echo.
set SPRING_PROFILES_ACTIVE=local-prod
.\mvnw.cmd spring-boot:run

echo.
echo ========================================
echo Test completed!
echo ========================================
echo.
echo To stop PostgreSQL: docker-compose down
echo To view logs: docker-compose logs postgres
echo.
pause
