# Використовуємо офіційний образ OpenJDK
FROM openjdk:21-jdk

# Встановлюємо робочу директорію
WORKDIR /app

# Копіюємо jar-файл у контейнер (переконайтеся, що ім'я jar-файлу відповідає вашому)
COPY target/javabackend-0.0.1-SNAPSHOT.jar app.jar

# Відкриваємо порт 8080
EXPOSE 8080

# Запускаємо застосування
ENTRYPOINT ["java", "-jar", "app.jar"]
