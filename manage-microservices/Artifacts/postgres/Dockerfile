# Utiliza la imagen base de MySQL oficial desde Docker Hub
FROM postgres:13

# Variables de entorno para la configuración de MySQL
ENV POSTGRES_USER=cmaza
ENV POSTGRES_PASSWORD=cmaza123**
ENV POSTGRES_DB: mazatest3
# Copia el script de generacion de tablas SQL
COPY BaseDatos.sql /docker-entrypoint-initdb.d/
