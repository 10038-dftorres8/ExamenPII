FROM mongo:latest

# Variables de entorno para usuario root y contraseña
ENV MONGO_INITDB_ROOT_USERNAME=admin
ENV MONGO_INITDB_ROOT_PASSWORD=password123

# Puerto que expone el contenedor
EXPOSE 27017


CMD ["mongod"]
