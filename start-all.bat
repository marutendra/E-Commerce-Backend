@echo off
REM Starts all services in order, each in its own window.
REM Needs: MongoDB already running, Maven (mvn) in PATH, JDK 17+.
REM No mvn in PATH? Run each *Application class from IntelliJ instead.

echo Starting Eureka Server...
start "Eureka (8761)" cmd /k "cd /d %~dp0eureka-server && mvn spring-boot:run"
timeout /t 25 /nobreak > nul

echo Starting Seller Service...
start "Seller (8082)" cmd /k "cd /d %~dp0seller-service && mvn spring-boot:run"
timeout /t 20 /nobreak > nul

echo Starting User Service...
start "User (8081)" cmd /k "cd /d %~dp0user-service && mvn spring-boot:run"
timeout /t 20 /nobreak > nul

echo Starting Order Service...
start "Order (8083)" cmd /k "cd /d %~dp0order-service && mvn spring-boot:run"

echo.
echo All started. Open http://localhost:8761 to see registered services.
