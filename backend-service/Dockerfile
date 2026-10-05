# ---- Build ----
FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace
COPY . .
# Windows checkouts may convert gradlew to CRLF, which breaks it on Linux
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew \
    && ./gradlew :app-service:bootJar --no-daemon --console=plain \
    && cp applications/app-service/build/libs/*.jar /workspace/app.jar

# ---- Run ----
FROM eclipse-temurin:25-jre
RUN useradd --system --uid 1001 appuser
USER appuser
WORKDIR /app
COPY --from=build /workspace/app.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70"
ENTRYPOINT ["/bin/sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
