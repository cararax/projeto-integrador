# Define a imagem base para a aplicação
FROM openjdk:17-alpine3.14

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo JAR para o diretório de trabalho
COPY target/nursenow-0.0.1-SNAPSHOT.jar .

# Define a porta em que a aplicação vai escutar
EXPOSE 8080

# Comando para iniciar a aplicação quando o container for executado
CMD ["java", "-jar", "nursenow-0.0.1-SNAPSHOT.jar"]

