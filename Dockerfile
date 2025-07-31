# Etapa de build: imagem com Java 17 + Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copia os arquivos do projeto
COPY . .

# Executa o build do projeto, sem testes
RUN mvn clean package -DskipTests

# Etapa de execução: runtime apenas com Java 17 (sem Maven)
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copia o JAR gerado na etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta 8080 (opcional no Render)
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
