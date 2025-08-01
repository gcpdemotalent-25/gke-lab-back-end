# Projet Backend Spring Boot - Lab de déploiement sur GKE

Ce projet est le service backend de notre application de laboratoire. Il s'agit d'une simple API REST construite avec Spring Boot qui expose un endpoint `/api/hello`. Il est destiné à être conteneurisé avec Docker et déployé sur Google Kubernetes Engine (GKE).

## Prérequis

*   Java JDK 17+
*   Maven 3.8+
*   Docker Desktop (pour les tests d'image en local)

## Lancement en local

Pour tester le service sur votre machine locale, suivez ces étapes :

1.  **Ouvrez un terminal** à la racine de ce projet.

2.  **Lancez l'application** avec le wrapper Maven :
    ```bash
    ./mvnw spring-boot:run
    ```

3.  **Vérifiez que le service fonctionne**. Ouvrez un autre terminal et utilisez `curl` (ou votre navigateur) pour interroger l'endpoint :
    ```bash
    curl http://localhost:8080/api/hello
    ```
    Vous devriez recevoir une réponse JSON comme celle-ci :
    ```json
    {"message":"Bonjour depuis le Backend Spring Boot sur GKE ! 🎉"}
    ```

> **Note sur CORS** : Le fichier `HelloController.java` contient une annotation `@CrossOrigin(origins = "http://localhost:3000")`. Celle-ci est essentielle pour permettre au frontend React (qui tourne sur le port 3000) de communiquer avec ce backend (sur le port 8080) pendant le développement local. En production sur GKE, cette communication sera gérée par le proxy Nginx du frontend.

## Dockerisation de l'application

Le `Dockerfile` à la racine de ce projet permet de créer une image conteneur portable de notre application.

1.  **Compilez le projet** pour créer le fichier `.jar` exécutable :
    ```bash
    # L'option -DskipTests permet d'accélérer la compilation
    mvn clean package -DskipTests
    ```

2.  **Construisez l'image Docker**. La commande suivante utilise les conventions de nommage pour Artifact Registry. Exécutez-la depuis un environnement où `gcloud` est configuré (comme Cloud Shell).
    ```bash
    # Assurez-vous que la variable d'environnement PROJECT_ID est définie
    # export PROJECT_ID=$(gcloud config get-value project)
    docker build -t europe-west1-docker.pkg.dev/${PROJECT_ID}/gke-lab-repo/backend:v1 .
    ```

## Déploiement sur GKE

Une fois l'image Docker construite, elle doit être poussée vers un registre d'images (Artifact Registry) pour que GKE puisse y accéder.

1.  **Poussez l'image vers Artifact Registry** :
    ```bash
    docker push europe-west1-docker.pkg.dev/${PROJECT_ID}/gke-lab-repo/backend:v1
    ```

2.  **Configuration Kubernetes** :
    Ce service est déployé sur GKE à l'aide de deux fichiers de configuration principaux :

    *   `backend-deployment.yaml`: Ce fichier demande à Kubernetes de créer plusieurs instances (pods) de notre application en se basant sur l'image Docker que nous venons de pousser. Il gère le cycle de vie des pods (création, mise à jour, auto-réparation).

    *   `backend-service.yaml`: Ce fichier crée un point d'accès réseau **interne** au cluster, nommé `backend-service`. C'est un service de type `ClusterIP`, ce qui signifie qu'il n'est pas accessible depuis l'extérieur, mais que d'autres services à l'intérieur du cluster (comme notre frontend) peuvent le joindre en utilisant simplement son nom (`http://backend-service:8080`).

## Commandes utiles

-   **Compiler le projet :** `mvn clean package -DskipTests`
-   **Lancer en local :** `./mvnw spring-boot:run`
-   **Construire l'image Docker :** `docker build -t <nom-image> .`
-   **Pousser l'image :** `docker push <nom-image>`