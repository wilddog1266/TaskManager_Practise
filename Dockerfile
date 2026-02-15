# --- build stage ---
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew

# сначала подтягиваем зависимости (кэш)
RUN ./gradlew --no-daemon dependencies

# копируем исходники и собираем
COPY . .
RUN ./gradlew --no-daemon clean bootJar -x test

# --- run stage ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]