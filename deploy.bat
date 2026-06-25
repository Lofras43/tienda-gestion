@echo off
echo ==========================================
echo  Despliegue - Tienda Gestion
echo ==========================================
echo.

REM === Verificar Java ===
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java no esta instalado. Instala JDK 21.
    exit /b 1
)
echo [OK] Java detectado

REM === Verificar JAVA_HOME ===
if "%JAVA_HOME%"=="" (
    echo [WARN] JAVA_HOME no definido. Usando java del PATH.
) else (
    echo [OK] JAVA_HOME=%JAVA_HOME%
)

REM === Compilar con Maven ===
echo.
echo [1/3] Compilando con Maven...
call .\mvnw.cmd clean package -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] Fallo la compilacion.
    exit /b 1
)
echo [OK] Compilacion exitosa

REM === Verificar JAR ===
echo.
echo [2/3] Verificando JAR...
if not exist "target\tienda-gestion-0.0.1-SNAPSHOT.jar" (
    echo [ERROR] JAR no encontrado en target/
    exit /b 1
)
echo [OK] JAR encontrado

REM === Ejecutar ===
echo.
echo [3/3] Iniciando aplicacion...
echo.
echo ==========================================
echo  Aplicacion disponible en:
echo  http://localhost:8080
echo ==========================================
echo.

java -jar target\tienda-gestion-0.0.1-SNAPSHOT.jar

pause