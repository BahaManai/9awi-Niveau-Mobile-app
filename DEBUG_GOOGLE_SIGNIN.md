# 🐛 Debug Google Sign-In

## 📱 Logs Android à surveiller

### Logs normaux (succès)

```
D/LoginFragment: Google sign in initiated
D/LoginFragment: Google sign in result received
D/LoginFragment: ID Token: eyJhbGciOiJSUzI1NiIsImtpZCI6...
D/AuthViewModel: Login with Google started
D/AuthRepository: Sending Google token to backend
D/AuthRepository: JWT received and saved
D/LoginFragment: Navigation to home screen
```

### Logs d'erreur

#### Erreur 1: Developer Error

```
E/LoginFragment: Google sign in failed
E/LoginFragment: com.google.android.gms.common.api.ApiException: 10:
```

**Cause:** SHA-1 non configuré ou incorrect

**Solution:**

1. Exécutez `get-sha1.bat`
2. Ajoutez le SHA-1 dans Google Cloud Console
3. Attendez 2-3 minutes
4. Réinstallez l'application

---

#### Erreur 2: Token null

```
E/LoginFragment: Google sign in failed
E/LoginFragment: ID Token is null
```

**Cause:** Client ID incorrect ou mal configuré

**Solution:**

1. Vérifiez le Client ID dans `LoginFragment.kt`
2. Assurez-vous d'utiliser le Client ID Android (pas Web)
3. Vérifiez que `.requestIdToken()` est appelé

---

#### Erreur 3: Backend error

```
E/AuthRepository: Backend error: HTTP 401 Unauthorized
E/AuthRepository: Response: {"message": "Token Google invalide"}
```

**Cause:** Le backend ne peut pas valider le token

**Solution:**

1. Vérifiez que le backend a la bonne configuration OAuth2
2. Vérifiez que le Client ID du backend correspond
3. Consultez les logs du backend

---

#### Erreur 4: Network error

```
E/AuthRepository: Network error: java.net.ConnectException: Failed to connect to /10.0.2.2:8080
```

**Cause:** Backend non accessible

**Solution:**

1. Vérifiez que le backend tourne sur le port 8080
2. Pour émulateur: utilisez `http://10.0.2.2:8080`
3. Pour appareil physique: utilisez `adb reverse tcp:8080 tcp:8080`

---

## 🔍 Comment activer les logs détaillés

### Dans LoginFragment.kt

Ajoutez des logs pour le debugging:

```kotlin
private fun signInWithGoogle() {
    Log.d("LoginFragment", "Google sign in initiated")
    val signInIntent = googleSignInClient.signInIntent
    googleSignInLauncher.launch(signInIntent)
}

private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
    try {
        Log.d("LoginFragment", "Google sign in result received")
        val account = completedTask.getResult(ApiException::class.java)
        val idToken = account?.idToken

        Log.d("LoginFragment", "ID Token: ${idToken?.take(50)}...")

        if (idToken != null) {
            viewModel.loginWithGoogle(idToken)
        } else {
            Log.e("LoginFragment", "ID Token is null")
            Toast.makeText(requireContext(), "Échec de l'authentification Google", Toast.LENGTH_SHORT).show()
        }
    } catch (e: ApiException) {
        Log.e("LoginFragment", "Google sign in failed", e)
        Log.e("LoginFragment", "Status code: ${e.statusCode}")
        Toast.makeText(requireContext(), "Erreur Google Sign-In: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
```

### Dans AuthViewModel.kt

```kotlin
fun loginWithGoogle(googleIdToken: String) {
    Log.d("AuthViewModel", "Login with Google started")
    _loginResult.postValue(Resource.Loading())
    viewModelScope.launch {
        val result = authRepository.loginWithGoogle(googleIdToken)
        Log.d("AuthViewModel", "Login result: ${result.javaClass.simpleName}")
        _loginResult.postValue(result)
    }
}
```

### Dans AuthRepository.kt

```kotlin
suspend fun loginWithGoogle(googleIdToken: String): Resource<LoginResponse> {
    Log.d("AuthRepository", "Sending Google token to backend")
    return safeApiCall {
        remoteDataSource.loginWithGoogle(googleIdToken)
    }.also { result ->
        when (result) {
            is Resource.Success -> {
                Log.d("AuthRepository", "JWT received: ${result.data.token?.take(20)}...")
                result.data.token?.let {
                    userPreferences.saveToken(it)
                    Log.d("AuthRepository", "JWT saved to DataStore")
                }
            }
            is Resource.Error -> {
                Log.e("AuthRepository", "Backend error: ${result.message}")
            }
            else -> {}
        }
    }
}
```

---

## 🧪 Test manuel avec Logcat

### Filtrer les logs Google Sign-In

Dans Android Studio Logcat:

```
tag:LoginFragment|AuthViewModel|AuthRepository
```

Ou:

```
package:com.example.kawi_niveau_mobile_app level:debug
```

---

## 📊 Codes d'erreur Google Sign-In

| Code  | Signification     | Solution              |
| ----- | ----------------- | --------------------- |
| 10    | Developer Error   | Configurer SHA-1      |
| 12500 | Sign In Cancelled | Utilisateur a annulé  |
| 12501 | Sign In Failed    | Erreur générale       |
| 7     | Network Error     | Vérifier la connexion |

---

## 🔧 Vérifier la configuration

### 1. Vérifier le Client ID

```kotlin
// Dans LoginFragment.kt
private fun setupGoogleSignIn() {
    val clientId = "428009874445-uirq408arbih2pstc2225h67faophn0j.apps.googleusercontent.com"
    Log.d("LoginFragment", "Using Client ID: $clientId")

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(clientId)
        .requestEmail()
        .build()

    googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
}
```

### 2. Vérifier le package name

```bash
# Dans AndroidManifest.xml
<manifest package="com.example.kawi_niveau_mobile_app">
```

### 3. Vérifier le SHA-1

```bash
keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android | findstr "SHA1"
```

---

## 🌐 Tester l'endpoint backend

### Avec cURL

```bash
curl -v -X POST http://localhost:8080/api/auth/google \
  -H "Content-Type: application/json" \
  -d '{"token": "test_token"}'
```

### Avec Postman

1. **POST** `http://localhost:8080/api/auth/google`
2. **Headers:** `Content-Type: application/json`
3. **Body:**
   ```json
   {
     "token": "eyJhbGciOiJSUzI1NiIsImtpZCI6..."
   }
   ```

---

## 📝 Checklist de debugging

- [ ] Logs activés dans LoginFragment
- [ ] Logs activés dans AuthViewModel
- [ ] Logs activés dans AuthRepository
- [ ] Logcat filtré sur les bons tags
- [ ] SHA-1 vérifié et configuré
- [ ] Client ID vérifié
- [ ] Package name vérifié
- [ ] Backend accessible
- [ ] Endpoint backend testé

---

## 🆘 Logs backend à demander

Si le problème vient du backend, demandez ces logs:

```
- Réception de la requête POST /api/auth/google
- Token reçu (premiers caractères)
- Résultat de la validation du token Google
- Erreur éventuelle lors de la validation
- JWT généré (premiers caractères)
- Réponse envoyée au client
```

---

## 📞 Support

Si le problème persiste après avoir vérifié tous ces points:

1. Copiez les logs complets de Logcat
2. Copiez les logs du backend
3. Vérifiez la configuration Google Cloud Console
4. Consultez `INTEGRATION_GOOGLE_SIGNIN.md` pour plus de détails

---

**Bon debugging ! 🐛🔍**
