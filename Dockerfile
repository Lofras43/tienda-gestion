FROM eclipse-temurin:21-jre-alpine
LABEL maintainer="Tienda Gestion <jairoescobedo48@gmail.com>"
LABEL description="Sistema de Gestion Inteligente para Tiendas Pequenas"

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

COPY target/tienda-gestion-0.0.1-SNAPSHOT.jar app.jar

RUN mkdir -p /app/logs /app/backups && \
    chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

ENV DB_USER=u450735954_Integrador
ENV DB_PASS=Integrador#123
ENV JAVA_OPTS="-Xms256m -Xmx512m"

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget -qO- http://localhost:8080/api/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]