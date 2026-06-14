@echo off
echo 🚀 Iniciando todos los servicios del Sistema de Bienestar Universitario
echo ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

echo 📦 Compilando el proyecto...
call mvn clean compile

start "Appointment Service" cmd /k "cd appointment-service && mvn exec:java -Dexec.mainClass=edu.eci.arsw.wellness.appointment.AppointmentServer"
start "Medical Service" cmd /k "cd medical-service && mvn exec:java -Dexec.mainClass=edu.eci.arsw.wellness.medical.MedicalServer"
start "Gym Service" cmd /k "cd gym-service && mvn exec:java -Dexec.mainClass=edu.eci.arsw.wellness.gym.GymServer"
start "Recreation Service" cmd /k "cd recreation-service && mvn exec:java -Dexec.mainClass=edu.eci.arsw.wellness.recreation.RecreationServer"

timeout /t 3 /nobreak >nul

echo 🌐 Iniciando API Gateway...
cd wellness-gateway
mvn exec:java -Dexec.mainClass="edu.eci.arsw.wellness.gateway.GatewayConsole"