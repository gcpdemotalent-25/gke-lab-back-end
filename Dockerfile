# Étape 1: Utiliser une image de base Java 17
FROM eclipse-temurin:17-jdk-jammy

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier .jar compilé dans le conteneur
# Le pattern 'target/*.jar' est simple et efficace pour ce type de projet
COPY target/gke-lab-backend-*.jar app.jar

# Exposer le port sur lequel l'application tourne
EXPOSE 8080

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]