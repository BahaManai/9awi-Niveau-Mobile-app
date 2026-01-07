# 📱 9awi Niveau - Application Mobile Android

<div align="center">

<img src="app/src/main/res/drawable/app_logo.png" alt="Logo 9awi Niveau" width="300"/>

**Application mobile native Android pour la plateforme d'apprentissage gamifiée 9awi Niveau**

[![Android](https://img.shields.io/badge/Android-Kotlin-green.svg)](https://kotlinlang.org/)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue.svg)](https://developer.android.com/topic/architecture)
[![DI](https://img.shields.io/badge/DI-Hilt-orange.svg)](https://dagger.dev/hilt/)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-23-brightgreen.svg)](https://developer.android.com/about/versions/marshmallow)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-36-blue.svg)](https://developer.android.com/)

</div>

---

## 📋 Table des matières

- [À propos du projet](#-à-propos-du-projet)
- [Fonctionnalités](#-fonctionnalités)
- [Technologies utilisées](#-technologies-utilisées)
- [Architecture](#-architecture)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Structure du projet](#-structure-du-projet)
- [Captures d'écran](#-captures-décran)
- [Backend API](#-backend-api)
- [Équipe](#-équipe)
- [Licence](#-licence)

---

## 🎯 À propos du projet

**9awi Niveau Mobile** est l'application mobile native Android de la plateforme d'apprentissage en ligne gamifiée **9awi Niveau**.

Cette application offre une expérience d'apprentissage mobile optimisée pour les apprenants et formateurs, permettant :

- 📚 **Accès mobile aux cours** : Consultation et suivi des cours en déplacement
- 🎮 **Gamification complète** : Système de points XP, niveaux, badges et défis
- 👨‍🎓 **Interface Apprenant** : Parcours d'apprentissage, quiz interactifs, suivi de progression
- 👨‍🏫 **Interface Formateur** : Gestion des cours, statistiques, suivi des apprenants
- 🔐 **Authentification sécurisée** : Login local et Google OAuth 2.0
- 📊 **Statistiques en temps réel** : Dashboard avec métriques de performance

### 🎓 Contexte académique

- **Matière** : Projet d'Intégration
- **Encadrante** : Madame Marwa Chaabani
- **Année académique** : 2024-2025

### 🌐 Plateforme Web

Ce projet mobile est complémentaire à la plateforme web complète :

- **Repository Web** : [9awi-niveau-gamified-elearning-platform](https://github.com/BahaManai/9awi-niveau-gamified-elearning-platform)
- **Technologies Web** : Spring Boot 3.5.7 + Angular 20.2.0

---

## ✨ Fonctionnalités

### 👤 Pour les Apprenants

- ✅ **Authentification sécurisée**

  - Connexion avec email/mot de passe
  - Connexion avec Google OAuth 2.0
  - Inscription avec validation d'email
  - Gestion de session persistante

- ✅ **Catalogue de cours**

  - Navigation par catégories et niveaux de difficulté
  - Recherche et filtrage avancés
  - Badges de statut (Nouveau, Inscrit, Vérifié)
  - Affichage des statistiques (étudiants inscrits, modules, leçons)

- ✅ **Suivi de progression**

  - Dashboard personnalisé avec statistiques
  - Progression par cours et modules
  - Système de gamification (XP, niveaux, badges)
  - Classement et leaderboard
  - Défis et récompenses

- ✅ **Apprentissage interactif**

  - Consultation des leçons avec contenu riche
  - Quiz interactifs avec correction automatique
  - Suivi de complétion des modules
  - Parcours d'apprentissage structurés

- ✅ **Profil utilisateur**
  - Informations personnelles
  - Statistiques de progression
  - Badges et réalisations
  - Historique d'apprentissage

### 👨‍🏫 Pour les Formateurs

- ✅ **Dashboard formateur**

  - Statistiques globales (cours, étudiants, taux de complétion)
  - Vue d'ensemble des cours créés
  - Métriques de performance en temps réel

- ✅ **Gestion des cours**

  - Création et modification de cours
  - Upload d'images pour les cours
  - Organisation en modules et leçons
  - Définition des niveaux de difficulté

- ✅ **Suivi des apprenants**

  - Liste des étudiants inscrits par cours
  - Statistiques de progression détaillées
  - Taux de complétion par cours
  - Analytics de performance

- ✅ **Gestion des parcours**
  - Consultation des parcours d'apprentissage
  - Statistiques de progression des parcours
  - Suivi des étapes et validations

---

## 🛠 Technologies utilisées

### Langage et Framework

| Technologie         | Version           | Utilisation                        |
| ------------------- | ----------------- | ---------------------------------- |
| **Kotlin**          | 1.9+              | Langage de programmation principal |
| **Android SDK**     | Min 23, Target 36 | Plateforme de développement        |
| **Jetpack Compose** | 1.5.0             | UI moderne (partiel)               |
| **View Binding**    | -                 | Liaison des vues XML               |

### Architecture et Patterns

| Technologie              | Version | Utilisation                       |
| ------------------------ | ------- | --------------------------------- |
| **MVVM**                 | -       | Architecture Model-View-ViewModel |
| **Hilt**                 | 2.48    | Injection de dépendances          |
| **Coroutines**           | 1.7.3   | Programmation asynchrone          |
| **LiveData**             | -       | Observation de données réactives  |
| **ViewModel**            | -       | Gestion d'état UI                 |
| **Navigation Component** | 2.7.0   | Navigation entre fragments        |

### Réseau et Données

| Technologie   | Version | Utilisation                        |
| ------------- | ------- | ---------------------------------- |
| **Retrofit**  | 2.9.0   | Client HTTP REST                   |
| **OkHttp**    | 4.11.0  | Client HTTP et intercepteurs       |
| **Gson**      | 2.9.0   | Sérialisation/Désérialisation JSON |
| **Room**      | 2.6.1   | Base de données locale SQLite      |
| **DataStore** | 1.0.0   | Stockage de préférences            |

### UI et Design

| Technologie            | Version | Utilisation                  |
| ---------------------- | ------- | ---------------------------- |
| **Material Design 3**  | 1.11.0  | Composants UI modernes       |
| **Glide**              | 4.16.0  | Chargement et cache d'images |
| **SwipeRefreshLayout** | 1.1.0   | Pull-to-refresh              |
| **ConstraintLayout**   | 2.1.4   | Layouts flexibles            |

### Authentification

| Technologie        | Version | Utilisation                           |
| ------------------ | ------- | ------------------------------------- |
| **Google Sign-In** | 20.7.0  | Authentification OAuth 2.0            |
| **JWT**            | -       | Gestion des tokens d'authentification |

---

## 🏗 Architecture

### Architecture Globale

L'application suit le pattern **MVVM (Model-View-ViewModel)** avec une architecture en couches :

```
┌─────────────────────────────────────────────────────────────┐
│                      UI LAYER (View)                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  Activities  │  │  Fragments   │  │   Adapters   │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                            │
                    ┌───────▼───────┐
                    │   ViewModel   │
                    │   (LiveData)  │
                    └───────┬───────┘
                            │
┌─────────────────────────────────────────────────────────────┐
│                    DOMAIN LAYER                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Repositories (Single Source of Truth)               │  │
│  │  • AuthRepository, CoursRepository, UserRepository   │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
┌───────▼───────┐  ┌────────▼────────┐  ┌──────▼──────┐
│  DATA LAYER   │  │  NETWORK LAYER  │  │ LOCAL LAYER │
│  • Models     │  │  • API Services │  │  • Room DB  │
│  • DTOs       │  │  • Retrofit     │  │  • DataStore│
│  • Requests   │  │  • Interceptors │  │  • DAOs     │
└───────────────┘  └─────────────────┘  └─────────────┘
```

### Architecture Détaillée

#### 1. UI Layer (Présentation)

**Activities** :

- `MainActivity` : Point d'entrée, gestion de la navigation initiale
- `AuthActivity` : Authentification (Login/Register)
- `HomeActivity` : Interface principale Apprenant
- `FormateurHomeActivity` : Interface principale Formateur

**Fragments** :

- **Auth** : `LoginFragment`, `RegisterFragment`
- **Apprenant** : `DashboardFragment`, `CoursListFragment`, `CoursDetailFragment`, `ModuleDetailFragment`, `ProfileFragment`
- **Formateur** : `FormateurDashboardFragment`, `FormateurCoursListFragment`, `CoursDetailFormateurFragment`, `CoursFormFragment`
- **Quiz** : `QuizViewerFragment`

**Adapters** :

- `CoursAdapter` : Liste des cours
- `ModuleAdapter` : Liste des modules
- `LeconAdapter` : Liste des leçons
- `ModuleReadOnlyAdapter` : Modules en lecture seule (formateur)

#### 2. ViewModel Layer

Gestion de l'état UI et logique de présentation :

- `AuthViewModel` : Authentification et session
- `HomeViewModel` : Dashboard apprenant
- `CoursListViewModel` : Liste des cours
- `CoursDetailViewModel` : Détails d'un cours
- `ModuleDetailViewModel` : Détails d'un module
- `QuizViewerViewModel` : Quiz interactifs
- `FormateurDashboardViewModel` : Dashboard formateur

#### 3. Repository Layer

Single Source of Truth pour les données :

- `AuthRepository` : Authentification (login, register, OAuth2)
- `UserRepository` : Gestion utilisateur et profil
- `CoursRepository` : CRUD cours
- `ModuleRepository` : CRUD modules
- `LeconRepository` : CRUD leçons
- `QuizRepository` : Quiz et soumissions
- `EnrollmentRepository` : Inscriptions aux cours
- `ApprenantRepository` : Données apprenant (progression, stats)
- `FormateurRepository` : Données formateur (stats, cours)
- `ParcoursRepository` : Parcours d'apprentissage

#### 4. Network Layer

Communication avec l'API REST :

- `AuthApiService` : Endpoints d'authentification
- `CoursApiService` : Endpoints cours
- `ModuleApiService` : Endpoints modules
- `LeconApiService` : Endpoints leçons
- `QuizApiService` : Endpoints quiz
- `EnrollmentApiService` : Endpoints inscriptions
- `ApprenantApiService` : Endpoints apprenant
- `FormateurApiService` : Endpoints formateur
- `ParcoursApiService` : Endpoints parcours
- `ProfileApiService` : Endpoints profil

**Intercepteurs** :

- `AuthInterceptor` : Injection automatique du token JWT dans les requêtes

#### 5. Local Layer

Persistance locale :

- **Room Database** : `AppDatabase`
  - `UserDao` : Accès aux données utilisateur
  - `UserEntity` : Entité utilisateur locale
- **DataStore** : `UserPreferences`
  - Stockage du token JWT
  - Préférences utilisateur

#### 6. Dependency Injection (Hilt)

- `NetworkModule` : Configuration Retrofit, OkHttp, API Services
- `@HiltAndroidApp` : Application class
- `@AndroidEntryPoint` : Injection dans Activities/Fragments
- `@Inject` : Injection de dépendances

### Flux de Données

```
User Action → Fragment → ViewModel → Repository → API Service → Backend
                  ↑                                                  │
                  └──────────── LiveData/Flow ←─────────────────────┘
```

---

## 📦 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

### Environnement de développement

- **Android Studio** : Hedgehog (2023.1.1) ou supérieur ([Télécharger](https://developer.android.com/studio))
- **JDK** : Java 11 ou supérieur
- **Android SDK** : API Level 23 (Android 6.0) minimum, API Level 36 recommandé
- **Kotlin Plugin** : Intégré dans Android Studio

### Émulateur ou Appareil physique

- **Émulateur Android** : API Level 23+ (recommandé : API 34)
- **Appareil physique** : Android 6.0 (Marshmallow) ou supérieur

### Backend API

L'application nécessite le backend Spring Boot en cours d'exécution :

- **Backend Repository** : [9awi-niveau-gamified-elearning-platform](https://github.com/BahaManai/9awi-niveau-gamified-elearning-platform)
- **URL par défaut** : `http://10.0.2.2:8080` (pour émulateur)
- **URL appareil physique** : `http://[VOTRE_IP_LOCAL]:8080`

---

## 🚀 Installation

### 1. Cloner le repository

```bash
git clone https://github.com/BahaManai/9awi-Niveau-Mobile-app.git
cd 9awi-Niveau-Mobile-app
```

### 2. Ouvrir le projet dans Android Studio

1. Lancez **Android Studio**
2. Cliquez sur **File → Open**
3. Sélectionnez le dossier du projet cloné
4. Attendez que Gradle synchronise les dépendances

### 3. Configurer le Backend

Assurez-vous que le backend Spring Boot est démarré :

```bash
# Dans le repository du backend
cd backend
mvn spring-boot:run
```

Le backend doit être accessible sur `http://localhost:8080`

### 4. Configurer l'URL de l'API

#### Pour l'émulateur Android

L'URL par défaut `http://10.0.2.2:8080` est déjà configurée dans `app/build.gradle.kts` :

```kotlin
buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8080/\"")
```

> **Note** : `10.0.2.2` est l'adresse spéciale de l'émulateur pour accéder à `localhost` de la machine hôte.

#### Pour un appareil physique

Modifiez l'URL dans `app/build.gradle.kts` avec votre IP locale :

```kotlin
buildConfigField("String", "API_BASE_URL", "\"http://192.168.1.X:8080/\"")
```

Pour trouver votre IP locale :

- **Windows** : `ipconfig` dans CMD
- **Mac/Linux** : `ifconfig` dans Terminal

### 5. Synchroniser Gradle

Cliquez sur **File → Sync Project with Gradle Files** ou sur l'icône 🐘 dans la barre d'outils.

### 6. Lancer l'application

1. Sélectionnez un émulateur ou connectez un appareil physique
2. Cliquez sur le bouton **Run** ▶️ ou appuyez sur `Shift + F10`
3. L'application se lancera sur l'appareil sélectionné

---

## ⚙️ Configuration

### Configuration de l'API

Fichier : `app/build.gradle.kts`

```kotlin
android {
    defaultConfig {
        // URL de base de l'API
        buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8080/\"")
    }
}
```

### Configuration Google OAuth 2.0

Pour activer la connexion Google, configurez les credentials OAuth :

1. Créez un projet dans [Google Cloud Console](https://console.cloud.google.com/)
2. Activez l'API Google Sign-In
3. Créez des credentials OAuth 2.0 pour Android
4. Ajoutez le SHA-1 de votre keystore :

```bash
# Obtenir le SHA-1 (Debug)
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

5. Téléchargez le fichier `google-services.json` et placez-le dans `app/`

### Configuration des permissions

Fichier : `app/src/main/AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
```

### Configuration du réseau (Cleartext Traffic)

Pour permettre les connexions HTTP non sécurisées en développement :

```xml
<application
    android:usesCleartextTraffic="true"
    ...>
```

> **⚠️ Important** : En production, utilisez HTTPS uniquement !

---

## 📁 Structure du projet

```
app/src/main/
├── java/com/example/kawi_niveau_mobile_app/
│   ├── data/                           # Couche de données
│   │   ├── api/                        # Interfaces API (vide, logique dans network/)
│   │   ├── dto/                        # Data Transfer Objects
│   │   │   ├── CoursStatsResponse.kt
│   │   │   ├── FormateurStatsResponse.kt
│   │   │   └── ParcoursResponse.kt
│   │   ├── enums/                      # Énumérations
│   │   │   └── NiveauDifficulte.kt
│   │   ├── local/                      # Base de données locale
│   │   │   ├── dao/                    # Data Access Objects
│   │   │   │   └── UserDao.kt
│   │   │   ├── entity/                 # Entités Room
│   │   │   │   ├── UserEntity.kt
│   │   │   │   └── UserMapper.kt
│   │   │   └── AppDatabase.kt
│   │   ├── model/                      # Modèles de domaine
│   │   │   └── Role.kt
│   │   ├── network/                    # Services API REST
│   │   │   ├── dto/                    # DTOs réseau
│   │   │   ├── ApprenantApiService.kt
│   │   │   ├── AuthApiService.kt
│   │   │   ├── AuthInterceptor.kt
│   │   │   ├── CoursApiService.kt
│   │   │   ├── EnrollmentApiService.kt
│   │   │   ├── FormateurApiService.kt
│   │   │   ├── LeconApiService.kt
│   │   │   ├── ModuleApiService.kt
│   │   │   ├── ParcoursApiService.kt
│   │   │   ├── ProfileApiService.kt
│   │   │   ├── QuizApiService.kt
│   │   │   ├── LoginRequest.kt
│   │   │   ├── RegisterRequest.kt
│   │   │   ├── OAuth2LoginRequest.kt
│   │   │   └── Resource.kt
│   │   ├── repository/                 # Repositories (SSOT)
│   │   │   ├── ApprenantRepository.kt
│   │   │   ├── AuthRepository.kt
│   │   │   ├── BaseRepository.kt
│   │   │   ├── CoursRepository.kt
│   │   │   ├── EnrollmentRepository.kt
│   │   │   ├── FormateurRepository.kt
│   │   │   ├── LeconRepository.kt
│   │   │   ├── ModuleRepository.kt
│   │   │   ├── ParcoursRepository.kt
│   │   │   ├── QuizRepository.kt
│   │   │   └── UserRepository.kt
│   │   ├── requests/                   # Requêtes API
│   │   │   ├── CoursRequest.kt
│   │   │   ├── EnrollmentRequest.kt
│   │   │   ├── LeconCompletionRequest.kt
│   │   │   └── QuizSubmissionRequest.kt
│   │   ├── responses/                  # Réponses API
│   │   │   ├── CoursResponse.kt
│   │   │   ├── EnrollmentResponse.kt
│   │   │   ├── ErrorResponse.kt
│   │   │   ├── FormateurStatsResponse.kt
│   │   │   ├── LeconResponse.kt
│   │   │   ├── LoginResponse.kt
│   │   │   ├── MessageResponse.kt
│   │   │   ├── ModuleProgressResponse.kt
│   │   │   ├── ModuleResponse.kt
│   │   │   ├── ParcoursProgressionStatsResponse.kt
│   │   │   ├── ParcoursResponse.kt
│   │   │   ├── ProfileResponse.kt
│   │   │   ├── QuizAttemptResponse.kt
│   │   │   ├── QuizResponse.kt
│   │   │   ├── ResultatQuizResponse.kt
│   │   │   └── UploadResponse.kt
│   │   └── UserPreferences.kt
│   ├── di/                             # Dependency Injection
│   │   └── NetworkModule.kt
│   ├── ui/                             # Couche UI
│   │   ├── auth/                       # Authentification
│   │   │   ├── AuthActivity.kt
│   │   │   ├── AuthViewModel.kt
│   │   │   ├── LoginFragment.kt
│   │   │   └── RegisterFragment.kt
│   │   ├── base/                       # Classes de base
│   │   │   ├── BaseFragment.kt
│   │   │   └── BaseViewModel.kt
│   │   ├── cours/                      # Cours (Apprenant)
│   │   │   ├── CoursAdapter.kt
│   │   │   ├── CoursDetailFragment.kt
│   │   │   ├── CoursDetailViewModel.kt
│   │   │   ├── CoursListFragment.kt
│   │   │   ├── CoursListViewModel.kt
│   │   │   ├── LeconAdapter.kt
│   │   │   ├── ModuleAdapter.kt
│   │   │   ├── ModuleDetailFragment.kt
│   │   │   └── ModuleDetailViewModel.kt
│   │   ├── formateur/                  # Interface Formateur
│   │   │   ├── cours/                  # Gestion cours
│   │   │   ├── dashboard/              # Dashboard formateur
│   │   │   └── FormateurHomeActivity.kt
│   │   ├── home/                       # Interface Apprenant
│   │   │   ├── accueil/                # Accueil
│   │   │   ├── cours/                  # Cours
│   │   │   ├── profil/                 # Profil
│   │   │   ├── progres/                # Progression
│   │   │   ├── HomeActivity.kt
│   │   │   └── HomeViewModel.kt
│   │   ├── quiz/                       # Quiz
│   │   │   ├── QuizViewerFragment.kt
│   │   │   └── QuizViewerViewModel.kt
│   │   └── theme/                      # Thème Compose
│   │       └── Theme.kt
│   ├── utils/                          # Utilitaires
│   │   └── NiveauBadgeHelper.kt
│   ├── KawiNiveauApplication.kt        # Application class (Hilt)
│   ├── MainActivity.kt                 # Activity principale
│   └── Utils.kt                        # Fonctions utilitaires
├── res/                                # Ressources
│   ├── anim/                           # Animations
│   ├── drawable/                       # Drawables et icônes
│   ├── font/                           # Polices (Poppins)
│   ├── layout/                         # Layouts XML
│   ├── menu/                           # Menus de navigation
│   ├── navigation/                     # Graphes de navigation
│   ├── values/                         # Valeurs (colors, strings, styles)
│   └── xml/                            # Configurations XML
└── AndroidManifest.xml                 # Manifeste Android
```

### Modules principaux

#### Data Layer

- **network/** : Communication avec l'API REST (Retrofit)
- **local/** : Persistance locale (Room, DataStore)
- **repository/** : Single Source of Truth, coordination des sources de données
- **dto/**, **requests/**, **responses/** : Objets de transfert de données

#### UI Layer

- **auth/** : Écrans d'authentification
- **home/** : Interface principale apprenant
- **formateur/** : Interface formateur
- **cours/** : Gestion et consultation des cours
- **quiz/** : Quiz interactifs

#### DI Layer

- **di/** : Configuration Hilt pour l'injection de dépendances

---

## 🔌 Backend API

### Endpoints principaux utilisés

**Base URL** : `http://10.0.2.2:8080/api` (émulateur)

#### Authentification

```
POST   /api/auth/login                 # Connexion
POST   /api/auth/register              # Inscription
POST   /api/auth/oauth2/google         # Connexion Google
GET    /api/auth/me                    # Utilisateur connecté
```

#### Cours (Apprenant)

```
GET    /api/mobile/apprenant/cours                    # Liste des cours
GET    /api/mobile/apprenant/cours/{id}               # Détails d'un cours
GET    /api/mobile/apprenant/cours/{id}/modules       # Modules d'un cours
POST   /api/mobile/apprenant/cours/{id}/enroll        # S'inscrire à un cours
```

#### Modules et Leçons

```
GET    /api/mobile/apprenant/modules/{id}             # Détails d'un module
GET    /api/mobile/apprenant/modules/{id}/lecons      # Leçons d'un module
POST   /api/mobile/apprenant/lecons/{id}/complete     # Marquer une leçon comme complétée
```

#### Quiz

```
GET    /api/mobile/apprenant/quiz/{id}                # Détails d'un quiz
POST   /api/mobile/apprenant/quiz/{id}/submit         # Soumettre un quiz
GET    /api/mobile/apprenant/quiz/{id}/attempts       # Historique des tentatives
```

#### Profil et Progression

```
GET    /api/mobile/apprenant/profile                  # Profil utilisateur
GET    /api/mobile/apprenant/stats                    # Statistiques de progression
GET    /api/mobile/apprenant/badges                   # Badges obtenus
GET    /api/mobile/apprenant/leaderboard              # Classement
GET    /api/mobile/apprenant/challenges               # Défis actifs
```

#### Formateur

```
GET    /api/mobile/formateur/stats                    # Statistiques formateur
GET    /api/mobile/formateur/cours                    # Cours du formateur
POST   /api/mobile/formateur/cours                    # Créer un cours
PUT    /api/mobile/formateur/cours/{id}               # Modifier un cours
GET    /api/mobile/formateur/cours/{id}/stats         # Stats d'un cours
GET    /api/mobile/formateur/parcours                 # Parcours du formateur
GET    /api/mobile/formateur/parcours/{id}/stats      # Stats d'un parcours
```

#### Upload

```
POST   /api/upload/image                              # Upload d'image
```

### Authentification JWT

Toutes les requêtes (sauf login/register) nécessitent un token JWT dans le header :

```
Authorization: Bearer <token>
```

Le token est automatiquement injecté par `AuthInterceptor`.

### Gestion des erreurs

L'application gère les erreurs HTTP avec des messages appropriés :

- **401 Unauthorized** : Session expirée, redirection vers login
- **403 Forbidden** : Accès refusé
- **404 Not Found** : Ressource introuvable
- **500 Internal Server Error** : Erreur serveur

---

## 💻 Utilisation

### Lancement de l'application

1. **Démarrer le backend** :

```bash
cd backend
mvn spring-boot:run
```

2. **Lancer l'application mobile** :
   - Ouvrez Android Studio
   - Sélectionnez un émulateur ou appareil
   - Cliquez sur Run ▶️

### Comptes de test

#### Administrateur

```
Email : admin@9awiniveau.com
Mot de passe : admin123
```

#### Formateur

```
Email : formateur@9awiniveau.com
Mot de passe : formateur123
```

#### Apprenant

```
Email : apprenant@9awiniveau.com
Mot de passe : apprenant123
```

### Parcours utilisateur

#### Apprenant

1. **S'inscrire ou se connecter**

   - Utiliser email/mot de passe ou Google OAuth
   - Vérifier l'email si nécessaire

2. **Explorer le catalogue**

   - Parcourir les cours par catégorie
   - Filtrer par niveau de difficulté
   - Consulter les détails des cours

3. **S'inscrire à un cours**

   - Cliquer sur "S'inscrire"
   - Accéder aux modules et leçons

4. **Suivre les leçons**

   - Lire le contenu des leçons
   - Marquer comme complétées
   - Suivre la progression

5. **Passer les quiz**

   - Répondre aux questions
   - Obtenir le score
   - Gagner des XP

6. **Consulter le profil**
   - Voir les statistiques
   - Badges et réalisations
   - Classement

#### Formateur

1. **Se connecter**

   - Utiliser les credentials formateur

2. **Consulter le dashboard**

   - Statistiques globales
   - Cours créés
   - Performance des étudiants

3. **Créer un cours**

   - Remplir les informations
   - Ajouter une image
   - Définir le niveau

4. **Gérer les cours**

   - Modifier les cours existants
   - Consulter les statistiques
   - Suivre les inscriptions

5. **Suivre les apprenants**
   - Liste des étudiants inscrits
   - Progression par cours
   - Taux de complétion

---

## 🧪 Tests

### Tests unitaires

Exécuter les tests unitaires :

```bash
./gradlew test
```

### Tests instrumentés

Exécuter les tests instrumentés (nécessite un émulateur ou appareil) :

```bash
./gradlew connectedAndroidTest
```

### Couverture de code

Générer un rapport de couverture :

```bash
./gradlew jacocoTestReport
```

---

## 🔧 Build et Déploiement

### Build Debug

```bash
./gradlew assembleDebug
```

L'APK sera généré dans : `app/build/outputs/apk/debug/app-debug.apk`

### Build Release

1. Créer un keystore (si nécessaire) :

```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
```

2. Configurer le signing dans `app/build.gradle.kts` :

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("my-release-key.jks")
            storePassword = "your-store-password"
            keyAlias = "my-key-alias"
            keyPassword = "your-key-password"
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(...)
        }
    }
}
```

3. Build release :

```bash
./gradlew assembleRelease
```

L'APK sera généré dans : `app/build/outputs/apk/release/app-release.apk`

### Installation sur appareil

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 🐛 Débogage

### Logs Android

Afficher les logs de l'application :

```bash
adb logcat | grep "KawiNiveau"
```

### Déboguer le réseau

Utiliser **Android Studio Profiler** :

1. Run → Profile 'app'
2. Sélectionner l'onglet **Network**
3. Observer les requêtes HTTP

### Déboguer la base de données Room

Utiliser **Database Inspector** :

1. View → Tool Windows → App Inspection
2. Sélectionner l'onglet **Database Inspector**
3. Explorer les tables et données

### Problèmes courants

#### Erreur de connexion au backend

**Problème** : `Unable to resolve host` ou `Connection refused`

**Solutions** :

- Vérifier que le backend est démarré
- Utiliser `10.0.2.2` pour l'émulateur (pas `localhost`)
- Utiliser l'IP locale pour un appareil physique
- Vérifier le firewall

#### Erreur 401 Unauthorized

**Problème** : Token JWT expiré ou invalide

**Solutions** :

- Se reconnecter
- Vérifier la configuration JWT dans le backend
- Vérifier que `AuthInterceptor` injecte bien le token

#### Erreur de build Gradle

**Problème** : Dépendances non résolues

**Solutions** :

- File → Invalidate Caches / Restart
- Supprimer `.gradle` et `.idea`
- Sync Project with Gradle Files

---

## 📚 Documentation supplémentaire

### Ressources Android

- [Documentation officielle Android](https://developer.android.com/docs)
- [Guide MVVM](https://developer.android.com/topic/architecture)
- [Hilt Documentation](https://dagger.dev/hilt/)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Room Documentation](https://developer.android.com/training/data-storage/room)

### Ressources Kotlin

- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Flow Documentation](https://kotlinlang.org/docs/flow.html)

### Backend API

- [Repository Backend](https://github.com/BahaManai/9awi-niveau-gamified-elearning-platform)
- Documentation API REST (Swagger/OpenAPI)

---

## 👥 Équipe

Ce projet a été développé par :

| Nom                   | Rôle                       | GitHub / Contact                                 |
| --------------------- | -------------------------- | ------------------------------------------------ |
| **Baha Eddine Manai** | Développeur Mobile Android | [@BahaManai](https://github.com/BahaManai)       |
| **Amine Kilani**      | Développeur Full Stack     | [@amineekilani](https://github.com/amineekilani) |
| **Kamel Anas Farrah** | Développeur Full Stack     | [@anas-farrah](https://github.com)               |

**Encadrante** : Madame Marwa Chaabani

---

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

## 🙏 Remerciements

Nous tenons à remercier :

- **Madame Marwa Chaabani** pour son encadrement et ses conseils précieux
- **Notre institution** pour le cadre et les ressources fournis
- **La communauté Android** pour les bibliothèques et outils open source
- **Google** pour Android Studio et les outils de développement

---

## 📞 Support et Contact

Pour toute question ou problème :

- 📧 Email : bahaeddinmanai7@gmail.com
- 🐛 Issues : [GitHub Issues](https://github.com/BahaManai/9awi-Niveau-Mobile-app/issues)
- 💬 Discussions : [GitHub Discussions](https://github.com/BahaManai/9awi-Niveau-Mobile-app/discussions)

---

## 🔮 Évolutions futures

### Fonctionnalités prévues

- 🔔 **Notifications push** avec Firebase Cloud Messaging
- 📥 **Mode hors ligne** avec synchronisation automatique
- 🎥 **Lecture de vidéos** intégrée dans les leçons
- 💬 **Messagerie interne** entre apprenants et formateurs
- 🌙 **Mode sombre** complet
- 🌍 **Multilingue** (Français, Anglais, Arabe)
- 📊 **Graphiques avancés** pour les statistiques
- 🔍 **Recherche full-text** dans les cours
- 📱 **Widgets Android** pour le suivi de progression
- 🎨 **Personnalisation** du thème et de l'interface
- 🔐 **Authentification biométrique** (empreinte digitale, Face ID)
- 📤 **Partage de réalisations** sur les réseaux sociaux

### Améliorations techniques

- ✅ Migration complète vers **Jetpack Compose**
- ✅ Implémentation de **Kotlin Flow** pour la réactivité
- ✅ **Pagination** pour les listes longues
- ✅ **Cache intelligent** avec Room et DataStore
- ✅ **Tests automatisés** (Unit, Integration, UI)
- ✅ **CI/CD** avec GitHub Actions
- ✅ **Monitoring** avec Firebase Crashlytics
- ✅ **Analytics** avec Firebase Analytics

---

## 🌐 Liens utiles

- 🌍 **Plateforme Web** : [9awi-niveau-gamified-elearning-platform](https://github.com/BahaManai/9awi-niveau-gamified-elearning-platform)
- 📱 **Application Mobile** : [9awi-Niveau-Mobile-app](https://github.com/BahaManai/9awi-Niveau-Mobile-app)
- 📖 **Documentation Backend** : Voir le README du repository web
- 🎨 **Design System** : Material Design 3

---

<div align="center">

**Développé avec ❤️ par l'équipe 9awi Niveau**

⭐ Si vous aimez ce projet, n'hésitez pas à lui donner une étoile sur GitHub !

---

**Version** : 1.0.0  
**Dernière mise à jour** : Janvier 2025  
**Statut** : ✅ En développement actif

</div>
