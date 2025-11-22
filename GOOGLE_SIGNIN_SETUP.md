# Configuration Google Sign-In

## ✅ Intégration terminée

L'intégration Google Sign-In a été ajoutée à votre application Android avec les éléments suivants :

### 📦 Dépendances ajoutées

- `com.google.android.gms:play-services-auth:20.7.0`

### 🔧 Fichiers modifiés/créés

1. **OAuth2LoginRequest.kt** - DTO pour envoyer le token Google au backend
2. **ApiService.kt** - Ajout de l'endpoint `/api/auth/google`
3. **RemoteDataSource.kt** - Méthode `loginWithGoogle()`
4. **AuthRepository.kt** - Méthode `loginWithGoogle()` avec sauvegarde du JWT
5. **AuthViewModel.kt** - Méthode `loginWithGoogle()` pour le ViewModel
6. **LoginFragment.kt** - Intégration complète du Google Sign-In
7. **fragment_login.xml** - Ajout du bouton Google Sign-In
8. **ic_google.xml** - Icône Google pour le bouton

### 🔑 Configuration utilisée

**Client ID Google OAuth2:**

```
428009874445-uirq408arbih2pstc2225h67faophn0j.apps.googleusercontent.com
```

**Backend Endpoint:**

```
POST /api/auth/google
Body: { "token": "GOOGLE_ID_TOKEN" }
Response: { "token": "JWT_TOKEN", "email": "user@example.com", "role": "ETUDIANT" }
```

### 📱 Fonctionnement

1. L'utilisateur clique sur "Continuer avec Google"
2. Google Sign-In s'ouvre et l'utilisateur sélectionne son compte
3. L'application récupère le Google ID Token
4. Le token est envoyé au backend via `POST /api/auth/google`
5. Le backend valide le token et retourne un JWT
6. Le JWT est sauvegardé dans DataStore
7. L'utilisateur est redirigé vers l'écran d'accueil

### ⚠️ Configuration supplémentaire requise

Pour que Google Sign-In fonctionne complètement, vous devez :

1. **Obtenir le SHA-1 de votre application** (pour debug et release)

   ```bash
   # Pour debug keystore
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

   # Pour Windows
   keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
   ```

2. **Ajouter le SHA-1 dans Google Cloud Console**

   - Allez sur https://console.cloud.google.com
   - Sélectionnez votre projet
   - Allez dans "APIs & Services" > "Credentials"
   - Modifiez votre OAuth 2.0 Client ID Android
   - Ajoutez le SHA-1 fingerprint

3. **Télécharger google-services.json** (si nécessaire)
   - Depuis Firebase Console ou Google Cloud Console
   - Placez-le dans `app/google-services.json`
   - Ajoutez le plugin dans `build.gradle.kts` si requis

### 🧪 Test

Pour tester l'intégration :

1. Assurez-vous que votre backend est démarré sur `http://localhost:8080`
2. Lancez l'application sur un émulateur ou appareil physique
3. Sur l'écran de connexion, cliquez sur "Continuer avec Google"
4. Sélectionnez un compte Google
5. Vérifiez que vous êtes redirigé vers l'écran d'accueil

### 🐛 Dépannage

**Erreur "Developer Error" ou "API not enabled":**

- Vérifiez que le SHA-1 est correctement configuré
- Vérifiez que le Client ID est correct
- Assurez-vous que l'API Google Sign-In est activée dans Google Cloud Console

**Token null:**

- Vérifiez la configuration du Client ID
- Assurez-vous d'utiliser `.requestIdToken()` avec le bon Client ID

**Erreur backend:**

- Vérifiez que le backend est accessible
- Vérifiez les logs du backend pour voir si le token est valide
- Testez l'endpoint avec Postman

### 📚 Ressources

- [Google Sign-In for Android](https://developers.google.com/identity/sign-in/android/start)
- [OAuth 2.0 Client IDs](https://console.cloud.google.com/apis/credentials)
