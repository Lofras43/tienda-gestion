# Plan de Monitoreo - Tienda Gestión

## 1. Logging
- **Archivo de logs**: `logs/tienda-gestion.log` (rotación diaria, 7 días de retención)
- **Niveles configurados**: INFO para la app, WARN para librerías
- **Formato**: Fecha | Hilo | Nivel | Logger | Mensaje

## 2. Health Check
- **Endpoint**: `GET /api/health`
- **Responde**: Estado UP/DOWN/DEGRADED, total de productos/ventas/compras, conexión BD
- **Frecuencia**: Verificaciones cada 30s desde Docker

## 3. Información de la aplicación
- **Endpoint**: `GET /api/info`
- **Responde**: Nombre, versión, descripción, tecnología, repositorio

## 4. Spring Boot Actuator
- **Endpoints expuestos**: health, info, metrics, env, logfile
- **URLs**: `/actuator/health`, `/actuator/info`, `/actuator/metrics`

## 5. Alertas del sistema
- **Stock bajo**: Endpoint propio `/alertas` (interfaz web)
- **Productos por vencer**: Sección en dashboard y alertas
- **Logs de error**: Monitorear `logs/tienda-gestion.log` para errores CRITICAL

## 6. Monitoreo de base de datos
- **Conexión**: Verificada en cada health check
- **Backups**: Automáticos diarios a las 2:00 AM (`BackupService`)
- **Limpieza**: Automática cada 6:00 AM
