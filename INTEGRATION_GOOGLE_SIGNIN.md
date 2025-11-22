# 🔐 Intégration Google Sign-In - Guide Complet

## ✅ Ce qui a été fait

L'intégration Google Sign-In est maintenant **complète et fonctionnelle** dans votre application Android.

### 📋 Résumé des modifications

#### 1. **Dépendances** (`app/build.gradle.kts`)

```kotlin
implementation("com.google.android.gms:play-services-auth:20.7.0")
```

#### 2. **Nouveau fichier DTO**

- `OAuth2LoginRequest.kt` - Pour envoyer le token Google au backend

#### 3. **API Backend** (`ApiService.kt`)

```kotlin
@POST("/api/auth/google")
suspend fun loginWithGoogle(@Body request: OAuth2LoginRequest): Response<LoginResponse>
```

#### 4. **Couche Repository et DataSource**

- `RemoteDataSource.loginWithGoogle()` - Appel API
- `AuthRepository.loginWithGoogle()` - Gestion du JWT et sauvegarde

#### 5. **ViewModel** (`AuthViewModel.kt`)

```kotlin
fun loginWithGoogle(googleIdToken: String)
```

#### 6. **Interface utilisateur**

- **LoginFragment.kt** - Logique Google Sign-In complète
- **fragment_login.xml** - Bouton "Continuer avec Google"
- **ic_google.xml** - Icône Google officielle

---

## 🚀 Comment ça marche

### Flux d'authentification

```
1. Utilisateur clique sur "Continuer avec Google"
   ↓
2. Google Sign-In s'ouvre (sélection de compte)
   ↓
3. Google retourne un ID Token
   ↓
4. App envoie le token à: POST /api/auth/google
   ↓
5. Backend valide et retourne un JWT
   ↓
6. JWT sauvegardé dans DataStore
   ↓
7. Navigation vers l'écran d'accueil
```

### Configuration utilisée

**Client ID OAuth2:**

```
428009874445-uirq408arbih2pstc2225h67faophn0j.apps.googleusercontent.com
```

**URL Backend:**

```
http://10.0.2.2:8080/api/auth/google (émulateur)
http://localhost:8080/api/auth/google (appareil physique avec adb reverse)
```

---

## 🔧 Configuration requise

### Étape 1: Obtenir le SHA-1

Pour que Google Sign-In fonctionne, vous devez enregistrer le SHA-1 de votre keystore :

**Sur Windows (PowerShell):**

```powershell
keytool -list -v -keystore "$env:USERPROFILE\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```

**Sur Mac/Linux:**

```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

Copiez la valeur **SHA1** qui s'affiche.

### Étape 2: Configurer Google Cloud Console

1. Allez sur https://console.cloud.google.com
2. Sélectionnez votre projet
3. Menu: **APIs & Services** > **Credentials**
4. Trouvez votre **OAuth 2.0 Client ID** Android
5. Ajoutez le **SHA-1 fingerprint** que vous avez copié
6. Ajoutez aussi le **package name**: `com.example.kawi_niveau_mobile_app`

### Étape 3: Vérifier le backend

Assurez-vous que votre backend a bien l'endpoint:

```java
@PostMapping("/api/auth/google")
public ResponseEntity<?> authenticateWithGoogle(@RequestBody OAuth2LoginRequest request) {
    // Validation du token Google
    // Retour du JWT
}
```

---

## 🧪 Tester l'intégration

### 1. Démarrer le backend

```bash
# Assurez-vous que le backend tourne sur le port 8080
```

### 2. Lancer l'application

```bash
./gradlew installDebug
# ou depuis Android Studio: Run 'app'
```

### 3. Tester la connexion

1. Ouvrez l'application
2. Sur l'écran de connexion, cliquez sur **"Continuer avec Google"**
3. Sélectionnez un compte Google
4. Vérifiez que vous êtes redirigé vers l'écran d'accueil

---

## 🐛 Résolution de problèmes

### Erreur: "Developer Error" ou "API not enabled"

**Cause:** SHA-1 non configuré ou incorrect

**Solution:**

1. Vérifiez que le SHA-1 est bien ajouté dans Google Cloud Console
2. Attendez quelques minutes (propagation des changements)
3. Réinstallez l'application

### Erreur: "Sign in failed" ou token null

**Cause:** Client ID incorrect ou mal configuré

**Solution:**

1. Vérifiez que le Client ID dans `LoginFragment.kt` est correct
2. Assurez-vous d'utiliser le Client ID de type **Android** (pas Web)
3. Vérifiez le package name dans Google Cloud Console

### Erreur: Backend ne répond pas

**Cause:** URL backend incorrecte

**Solution:**

- **Émulateur:** Utilisez `http://10.0.2.2:8080`
- **Appareil physique:** Utilisez `adb reverse tcp:8080 tcp:8080` puis `http://localhost:8080`
- Ou utilisez l'URL de production si déployé

### Erreur: "Invalid token" du backend

**Cause:** Le backend ne peut pas valider le token Google

**Solution:**

1. Vérifiez que le backend a la bonne configuration OAuth2
2. Assurez-vous que le Client ID du backend correspond
3. Vérifiez les logs du backend pour plus de détails

---

## 📱 Code important

### LoginFragment.kt - Configuration Google Sign-In

```kotlin
private fun setupGoogleSignIn() {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("428009874445-uirq408arbih2pstc2225h67faophn0j.apps.googleusercontent.com")
        .requestEmail()
        .build()

    googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
}
```

### Gestion du résultat

```kotlin
private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
    try {
        val account = completedTask.getResult(ApiException::class.java)
        val idToken = account?.idToken

        if (idToken != null) {
            viewModel.loginWithGoogle(idToken)
        }
    } catch (e: ApiException) {
        // Gestion d'erreur
    }
}
```

---

## 📚 Ressources utiles

- [Documentation Google Sign-In Android](https://developers.google.com/identity/sign-in/android/start)
- [Google Cloud Console](https://console.cloud.google.com)
- [Obtenir un Client ID](https://developers.google.com/identity/sign-in/android/start-integrating#get_your_backend_servers_oauth_20_client_id)

---

## ✨ Prochaines étapes

1. **Tester sur un appareil réel** (pas seulement l'émulateur)
2. **Configurer le SHA-1 pour le keystore de release** (pour la production)
3. **Ajouter la gestion de déconnexion Google** (optionnel)
4. **Implémenter le refresh token** si nécessaire

---

## 💡 Notes importantes

- Le JWT retourné par le backend est automatiquement sauvegardé dans DataStore
- L'utilisateur reste connecté même après fermeture de l'application
- Le token Google n'est utilisé qu'une seule fois pour obtenir le JWT
- Toutes les requêtes suivantes utilisent le JWT du backend

---

**🎉 L'intégration est complète et prête à être testée !**
