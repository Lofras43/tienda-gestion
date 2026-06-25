# Plan de Mantenimiento - Tienda Gestión

## 1. Backup de Datos
- **Frecuencia**: Diario a las 2:00 AM (cron: `0 0 2 * * ?`)
- **Ubicación**: `backups/backup_YYYYMMDD_HHmmss.txt`
- **Contenido**: Productos, ventas, compras con totales
- **Implementación**: `BackupService.java` con `@Scheduled`

## 2. Limpieza de Logs
- **Frecuencia**: Diario a las 6:00 AM
- **Retención**: Logs principales 7 días, logs de salud 30 días
- **Rotación**: `TimeBasedRollingPolicy` en logback-spring.xml

## 3. Mantenimiento de Base de Datos
- **DDL automático**: `spring.jpa.hibernate.ddl-auto=update`
- **Tablas faltantes**: `schema.sql` se ejecuta al iniciar (`spring.sql.init.mode=always`)
- **Datos de prueba**: `datos-prueba.sql` disponible para entornos de desarrollo

## 4. Actualización del Proyecto
- **Control de versiones**: Git + GitHub
- **Commits**: Mensajes descriptivos con prefijos (feat:, fix:, chore:)
- **Ramas**: main (producción), feature/* (desarrollo)

## 5. Seguridad
- **Contraseñas**: Codificadas con BCryptPasswordEncoder
- **CSRF**: Habilitado con CookieCsrfTokenRepository
- **Rutas públicas**: Solo login, registro, recursos estáticos y health/info
- **OWASP**: Dependency-check integrado en el build de Maven

## 6. Construcción y Despliegue
- **Build**: `.\mvnw.cmd clean package` (genera JAR ejecutable)
- **Despliegue local**: `deploy.bat` (compila + ejecuta)
- **Contenedor**: Dockerfile disponible para despliegue en contenedor
- **Puerto**: 8080 (expuesto en 0.0.0.0)
