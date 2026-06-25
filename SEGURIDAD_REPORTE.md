# Reporte de Pruebas de Seguridad - Tienda Gestión

## 1. Seguridad en la Aplicación

### 1.1 Autenticación
- **Método**: Spring Security con formulario de login personalizado
- **Parámetro de login**: email (no username tradicional)
- **Codificación**: BCryptPasswordEncoder (hash + salt automático)

### 1.2 Autorización
- **Rutas públicas**: `/`, `/login`, `/registro`, `/css/**`, `/js/**`, `/images/**`,
  `/api/health`, `/api/info`, `/actuator/health`, `/actuator/info`
- **Rutas protegidas**: Todo lo demás requiere autenticación
- **Redirección**: Login exitoso redirige a `/dashboard`

### 1.3 Protección CSRF
- **Estado**: Habilitado
- **Implementación**: `CookieCsrfTokenRepository.withHttpOnlyFalse()`
- **Excepciones**: Endpoints `/api/**` y `/actuator/**` (uso desde servicios externos)

### 1.4 Seguridad de Contraseñas
- **Almacenamiento**: Hash BCrypt (no texto plano)
- **Validación**: Spring Security `DaoAuthenticationProvider`

## 2. Seguridad en la Base de Datos
- **Conexión remota**: Hostinger MySQL con SSL deshabilitado (entorno académico)
- **Credenciales**: Externalizadas mediante variables de entorno `${DB_USER}`, `${DB_PASS}`
- **DDL**: `ddl-auto=update` solo en desarrollo

## 3. OWASP Dependency Check
- **Plugin**: `dependency-check-maven` (versión 9.0.9)
- **Ejecución**: `mvn org.owasp:dependency-check-maven:check`
- **Umbral de fallo**: CVSS >= 8 (vulnerabilidades críticas)
- **Reportes**: HTML y JSON en `target/dependency-check-report.html`

## 4. Resumen de Vulnerabilidades Detectadas
*Pendiente de ejecutar OWASP Dependency Check. Ejecutar:*
```bash
.\mvnw.cmd org.owasp:dependency-check-maven:check
```

## 5. Buenas Prácticas Implementadas
- ✅ Contraseñas hasheadas con BCrypt
- ✅ CSRF habilitado con tokens por cookie
- ✅ Rutas públicas mínimas (solo lo necesario)
- ✅ Credenciales externalizadas (variables de entorno)
- ✅ Validación de stock antes de ventas
- ✅ Manejo global de excepciones
- ✅ Logging de eventos del sistema
