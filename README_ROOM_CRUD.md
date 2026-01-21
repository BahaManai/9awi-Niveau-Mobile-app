# 📚 Architecture Room & CRUD - Documentation

## Table des matières

- [Introduction](#introduction)
- [Les 3 composants principaux](#les-3-composants-principaux)
- [Les opérations CRUD](#les-opérations-crud)
- [Configuration avec Hilt](#configuration-avec-hilt)
- [Flux de données complet](#flux-de-données-complet)
- [Avantages de Room](#avantages-de-room)

---

## Introduction

**Room** est une bibliothèque de persistance de données pour Android qui fournit une couche d'abstraction au-dessus de SQLite. Elle permet de sauvegarder les données localement sur l'appareil de manière simple et sécurisée.

Dans cette application, Room est utilisé pour :

- Sauvegarder les informations de l'utilisateur connecté
- Stocker le token d'authentification
- Permettre l'accès aux données hors ligne
- Gérer la session utilisateur

---

## Les 3 composants principaux

### 🗂️ 1. Entity - La table de base de données

**Fichier** : `data/local/entity/UserEntity.kt`

```kotlin
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long,
    val email: String,
    val token: String,
    val firstName: String?,
    val lastName: String?,
    val role: String,
    val profileImage: String?,
    val dateOfBirth: String?,
    val phoneNumber: String?,
    val provider: String?,
    val emailVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
```

**Rôle** :

- Représente une table SQL dans la base de données
- `@Entity` : Indique que cette classe est une table
- `@PrimaryKey` : Définit la clé primaire (identifiant unique)
- Chaque propriété devient une colonne dans la table

**Annotations importantes** :

- `@Entity(tableName = "users")` : Nom de la table dans la base de données
- `@PrimaryKey` : Clé primaire pour identifier chaque enregistrement
- `val` : Propriétés immuables (recommandé pour les entités)

---

### 🔧 2. DAO - Les opérations CRUD

**Fichier** : `data/local/dao/UserDao.kt`

```kotlin
@Dao
interface UserDao {

    // CREATE - Insertion
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // READ - Lecture simple (une fois)
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserEntity?

    // READ - Lecture réactive (observation en temps réel)
    @Query("SELECT * FROM users LIMIT 1")
    fun getUserLiveData(): LiveData<UserEntity?>

    // READ - Récupérer le token
    @Query("SELECT token FROM users LIMIT 1")
    suspend fun getToken(): String?

    // READ - Observer le token
    @Query("SELECT token FROM users LIMIT 1")
    fun getTokenLiveData(): LiveData<String?>

    // READ - Vérifier si connecté
    @Query("SELECT COUNT(*) > 0 FROM users WHERE token IS NOT NULL AND token != ''")
    suspend fun isUserLoggedIn(): Boolean

    // READ - Observer l'état de connexion
    @Query("SELECT COUNT(*) > 0 FROM users WHERE token IS NOT NULL AND token != ''")
    fun isUserLoggedInLiveData(): LiveData<Boolean>

    // UPDATE - Mise à jour
    @Update
    suspend fun updateUser(user: UserEntity)

    // DELETE - Suppression spécifique
    @Delete
    suspend fun deleteUser(user: UserEntity)

    // DELETE - Suppression totale (déconnexion)
    @Query("DELETE FROM users")
    suspend fun clearUser()
}
```

**Rôle** :

- DAO = Data Access Object (objet d'accès aux données)
- Définit toutes les méthodes pour interagir avec la base de données
- `@Dao` : Indique que c'est une interface DAO
- `suspend` : Fonction asynchrone (ne bloque pas l'interface utilisateur)

**Types de requêtes** :

- `@Insert` : Ajouter des données
- `@Query` : Requêtes SQL personnalisées
- `@Update` : Modifier des données existantes
- `@Delete` : Supprimer des données

---

### 🏛️ 3. Database - La base de données

**Fichier** : `data/local/AppDatabase.kt`

```kotlin
@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
```

**Rôle** :

- Point d'entrée principal de la base de données Room
- Contient la liste de toutes les tables (entities)
- Fournit l'accès aux DAOs
- Gère les versions et migrations

**Paramètres** :

- `entities` : Liste des tables de la base de données
- `version` : Numéro de version (incrémenté lors de modifications)
- `exportSchema` : Exporter le schéma de la base (utile pour les migrations)

---

## Les opérations CRUD

### ✅ CREATE - Insertion de données

```kotlin
@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertUser(user: UserEntity)
```

**Utilisation** : Sauvegarder un utilisateur après connexion

**Stratégies de conflit** :

- `REPLACE` : Remplace l'enregistrement existant
- `IGNORE` : Ignore la nouvelle insertion
- `ABORT` : Annule la transaction

**Exemple dans AuthRepository** :

```kotlin
private suspend fun saveUserSession(token: String) {
    // 1. Récupérer le profil depuis l'API
    val profileResponse = profileApiService.getProfile("Bearer $token")

    if (profileResponse.isSuccessful && profileResponse.body() != null) {
        val profile = profileResponse.body()!!

        // 2. Convertir en entité Room
        val userEntity = profile.toUserEntity(token)

        // 3. Sauvegarder dans Room
        userDao.insertUser(userEntity) // ✅ Données persistées localement
    }
}
```

---

### 📖 READ - Lecture de données

#### Lecture simple (une fois)

```kotlin
@Query("SELECT * FROM users LIMIT 1")
suspend fun getUser(): UserEntity?
```

- Récupère l'utilisateur UNE FOIS
- Retourne `null` si aucun utilisateur n'existe
- Utilisé pour des vérifications ponctuelles

#### Lecture réactive (observation en temps réel)

```kotlin
@Query("SELECT * FROM users LIMIT 1")
fun getUserLiveData(): LiveData<UserEntity?>
```

- `LiveData` : Observe les changements automatiquement
- L'interface utilisateur se met à jour automatiquement
- Pas besoin de rafraîchir manuellement

#### Requêtes spécifiques

```kotlin
// Récupérer uniquement le token
@Query("SELECT token FROM users LIMIT 1")
suspend fun getToken(): String?

// Vérifier si l'utilisateur est connecté
@Query("SELECT COUNT(*) > 0 FROM users WHERE token IS NOT NULL AND token != ''")
suspend fun isUserLoggedIn(): Boolean
```

**Exemple d'utilisation** :

```kotlin
// Dans UserRepository
suspend fun getProfile(): Resource<ProfileResponse> {
    // Récupérer le token depuis Room
    val token = userDao.getToken()

    if (token.isNullOrEmpty()) {
        return Resource.Error("Token manquant")
    }

    // Utiliser le token pour l'API
    return safeApiCall {
        profileApiService.getProfile("Bearer $token")
    }
}
```

---

### 🔄 UPDATE - Mise à jour de données

```kotlin
@Update
suspend fun updateUser(user: UserEntity)
```

**Fonctionnement** :

- Room utilise la clé primaire (`id`) pour identifier l'enregistrement
- Tous les champs sont mis à jour
- Retourne une erreur si l'utilisateur n'existe pas

**Exemple d'utilisation** :

```kotlin
// Récupérer l'utilisateur actuel
val currentUser = userDao.getUser()

if (currentUser != null) {
    // Modifier les informations
    val updatedUser = currentUser.copy(
        firstName = "Nouveau prénom",
        phoneNumber = "+33612345678"
    )

    // Sauvegarder les modifications
    userDao.updateUser(updatedUser)
}
```

---

### 🗑️ DELETE - Suppression de données

#### Suppression spécifique

```kotlin
@Delete
suspend fun deleteUser(user: UserEntity)
```

- Supprime un utilisateur spécifique
- Nécessite l'objet complet

#### Suppression totale (déconnexion)

```kotlin
@Query("DELETE FROM users")
suspend fun clearUser()
```

- Supprime TOUS les utilisateurs de la table
- Utilisé lors de la déconnexion

**Exemple dans AuthRepository** :

```kotlin
suspend fun logout() {
    userDao.clearUser() // ✅ Supprime toutes les données utilisateur
}
```

---

## Configuration avec Hilt

**Fichier** : `di/NetworkModule.kt`

### Qu'est-ce que Hilt ?

**Hilt** est un framework d'injection de dépendances pour Android. Il permet de :

- Créer automatiquement les instances des classes
- Gérer le cycle de vie des objets
- Éviter le code répétitif
- Faciliter les tests

### Configuration de Room avec Hilt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // 1. Créer la base de données
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "kawi_niveau_db" // Nom du fichier de base de données
        )
        .fallbackToDestructiveMigration() // Supprime et recrée si migration échoue
        .build()
    }

    // 2. Fournir le DAO
    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }
}
```

**Annotations importantes** :

- `@Module` : Indique un module Hilt
- `@InstallIn(SingletonComponent::class)` : Disponible dans toute l'application
- `@Provides` : Indique comment créer une instance
- `@Singleton` : Une seule instance dans toute l'application
- `@ApplicationContext` : Contexte de l'application

### Utilisation dans les Repositories

```kotlin
class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
    private val profileApiService: ProfileApiService,
    private val userDao: UserDao // ✅ Injecté automatiquement par Hilt
) : BaseRepository() {
    // ...
}
```

**Avantages** :

- Pas besoin de créer manuellement `UserDao`
- Hilt gère automatiquement les dépendances
- Une seule instance partagée dans toute l'application

---

## Flux de données complet

### 🔐 Scénario 1 : Connexion utilisateur

```
┌─────────────┐
│  Utilisateur │
│  se connecte │
└──────┬──────┘
       │
       ▼
┌─────────────────────┐
│ AuthRepository      │
│ .login(email, pwd)  │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│ Appel API Backend   │
│ Retourne token JWT  │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────────┐
│ Récupération du profil  │
│ profileApiService       │
│ .getProfile(token)      │
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ Conversion              │
│ profile.toUserEntity()  │
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ Sauvegarde Room         │
│ userDao.insertUser()    │
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ Données persistées      │
│ Disponibles hors ligne  │
└─────────────────────────┘
```

**Code complet** :

```kotlin
suspend fun login(email: String, password: String): Resource<LoginResponse> {
    return safeApiCall {
        authApiService.login(LoginRequest(email, password))
    }.also { result ->
        if (result is Resource.Success) {
            // Vérifier le rôle
            val role = result.data.role
            if (role == "ETUDIANT" || role == "FORMATEUR") {
                // Sauvegarder dans Room
                result.data.token?.let { token ->
                    saveUserSession(token)
                }
            }
        }
    }
}
```

---

### ✅ Scénario 2 : Vérification de connexion

```
┌─────────────────────┐
│ Démarrage de l'app  │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────────┐
│ AuthRepository          │
│ .isUserLoggedIn()       │
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ userDao.isUserLoggedIn()│
│ Vérifie si token existe │
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ Retourne true/false     │
│ Pas d'appel réseau      │
│ Instantané et hors ligne│
└─────────────────────────┘
```

**Code** :

```kotlin
suspend fun isUserLoggedIn(): Boolean {
    return userDao.isUserLoggedIn()
}
```

**Avantages** :

- ✅ Pas besoin de connexion internet
- ✅ Réponse instantanée
- ✅ Fonctionne hors ligne

---

### 🚪 Scénario 3 : Déconnexion

```
┌─────────────────────┐
│ Utilisateur clique  │
│ sur "Déconnexion"   │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────────┐
│ UserRepository.logout() │
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ userDao.clearUser()     │
│ Supprime toutes les     │
│ données utilisateur     │
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ Table "users" vide      │
│ Utilisateur déconnecté  │
└─────────────────────────┘
```

**Code** :

```kotlin
suspend fun logout() {
    userDao.clearUser() // Supprime toutes les données
}
```

---

### 🔄 Scénario 4 : Observation en temps réel avec LiveData

```
┌─────────────────────────┐
│ ViewModel observe       │
│ getUserLiveData()       │
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ Room surveille la table │
│ "users" automatiquement │
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ Données modifiées       │
│ (insert/update/delete)  │
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ LiveData notifie        │
│ automatiquement         │
└──────┬──────────────────┘
       │
       ▼
┌─────────────────────────┐
│ UI se met à jour        │
│ automatiquement         │
└─────────────────────────┘
```

**Exemple** :

```kotlin
// Dans le ViewModel
val currentUser: LiveData<UserEntity?> = authRepository.getCurrentUserLiveData()

// Dans l'Activity/Fragment
viewModel.currentUser.observe(viewLifecycleOwner) { user ->
    if (user != null) {
        // Afficher les informations utilisateur
        binding.textViewName.text = "${user.firstName} ${user.lastName}"
    } else {
        // Rediriger vers la page de connexion
        navigateToLogin()
    }
}
```

---

## Avantages de Room

### ✅ 1. Persistance locale

- Les données restent même si l'application est fermée
- Pas besoin de se reconnecter à chaque ouverture
- Expérience utilisateur fluide

### ✅ 2. Fonctionnement hors ligne

- Accès aux données sans connexion internet
- Vérification de connexion instantanée
- Pas de dépendance au réseau

### ✅ 3. Performance

- Plus rapide que les appels réseau
- Pas de latence
- Réponse instantanée

### ✅ 4. Type-safe (Sécurité des types)

- Erreurs détectées à la compilation
- Pas d'erreurs SQL à l'exécution
- Auto-complétion dans l'IDE

### ✅ 5. LiveData (Observation réactive)

- Mise à jour automatique de l'interface
- Pas besoin de rafraîchir manuellement
- Gestion automatique du cycle de vie

### ✅ 6. Coroutines (Asynchrone)

- Opérations en arrière-plan
- Ne bloque pas l'interface utilisateur
- Code simple et lisible

### ✅ 7. Intégration avec Hilt

- Injection de dépendances automatique
- Pas de code répétitif
- Facilite les tests

---

## Mapper - Conversion de données

**Fichier** : `data/local/entity/UserMapper.kt`

```kotlin
fun ProfileResponse.toUserEntity(token: String): UserEntity {
    return UserEntity(
        id = this.id,
        email = this.email,
        token = token,
        firstName = this.firstName,
        lastName = this.lastName,
        role = this.role,
        profileImage = this.profileImage,
        dateOfBirth = this.dateOfBirth,
        phoneNumber = this.phoneNumber,
        provider = this.provider,
        emailVerified = this.emailVerified,
        createdAt = this.createdAt
    )
}
```

**Rôle** :

- Convertir les données réseau (`ProfileResponse`) en entité Room (`UserEntity`)
- Séparer les modèles réseau des modèles de base de données
- Faciliter les modifications futures

**Pourquoi séparer ?**

- Le modèle réseau peut changer (API backend)
- Le modèle Room reste stable (base de données locale)
- Évite les impacts sur toute l'application

---

## Résumé de l'architecture

```
┌─────────────────────────────────────────────────────────┐
│                      UI Layer                           │
│  (Activity, Fragment, Composable)                       │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                   ViewModel Layer                       │
│  (Gère l'état de l'UI)                                  │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                 Repository Layer                        │
│  (AuthRepository, UserRepository)                       │
│  - Coordonne les sources de données                     │
│  - Décide d'utiliser Room ou l'API                      │
└────────┬───────────────────────────┬────────────────────┘
         │                           │
         ▼                           ▼
┌────────────────────┐    ┌──────────────────────┐
│   Room Database    │    │   API Network        │
│   (Local)          │    │   (Remote)           │
│                    │    │                      │
│ - UserEntity       │    │ - Retrofit           │
│ - UserDao          │    │ - AuthApiService     │
│ - AppDatabase      │    │ - ProfileApiService  │
└────────────────────┘    └──────────────────────┘
```

---

## Conclusion

Cette application utilise **Room** de manière professionnelle avec :

- ✅ Architecture propre (Repository pattern)
- ✅ Injection de dépendances (Hilt)
- ✅ Opérations asynchrones (Coroutines)
- ✅ Observation réactive (LiveData)
- ✅ Séparation des responsabilités (Entity, DAO, Database)

Les données utilisateur sont sauvegardées localement pour offrir une expérience fluide, rapide et fonctionnant hors ligne.
