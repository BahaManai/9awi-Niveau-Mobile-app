# 🔍 Diagnostic du Problème Google Sign-In

## ⚠️ Symptôme

Lorsque vous cliquez sur "Continuer avec Google" et sélectionnez votre compte, **rien ne se passe**.

## 🎯 Causes Possibles

### 1. **Problème de Configuration SHA-1** (Le plus probable)

Si le SHA-1 n'est pas correctement configuré dans Google Cloud Console, l'authentification échoue silencieusement.

**Solution:**

```bash
# Exécutez ce script pour obtenir votre SHA-1
get-sha1.bat
```

Puis ajoutez-le dans Google Cloud Console:

- URL: https://console.cloud.google.com/apis/credentials
- Trouvez votre OAuth 2.0 Client ID (Android)
- Ajoutez le SHA-1 fingerprint
- **Attendez 2-3 minutes** pour la propagation

---

### 2. **Backend Non Démarré**

Si le backend n'est pas en cours d'exécution, l'application ne peut pas valider le token Google.

**Vérification:**

```bash
# Testez si le backend répond
curl http://localhost:8080/api/auth/google
```

**Solution:**

- Démarrez votre backend sur le port 8080
- Vérifiez que l'endpoint `/api/auth/google` est implémenté

---

### 3. **Problème de Réseau (Émulateur)**

L'émulateur Android utilise `10.0.2.2` pour accéder à `localhost`.

**Vérification:**

- L'URL configurée est: `http://10.0.2.2:8080/`
- Si vous utilisez un appareil physique, changez pour votre IP locale

---

### 4. **Client ID Incorrect**

Le Client ID dans le code doit correspondre à celui de Google Cloud Console.

**Vérification:**

- Client ID actuel: `428009874445-uirq408arbih2pstc2225h67faophn0j.apps.googleusercontent.com`
- Vérifiez qu'il correspond dans Google Cloud Console

---

## 🔧 Étapes de Diagnostic

### Étape 1: Vérifier les Logs Android

J'ai ajouté des logs détaillés dans le code. Lancez l'application et regardez Logcat:

**Dans Android Studio:**

1. Ouvrez l'onglet **Logcat**
2. Filtrez avec: `tag:LoginFragment|AuthViewModel|AuthRepository|BaseRepository`
3. Cliquez sur "Continuer avec Google"
4. Observez les logs

**Logs attendus (succès):**

```
D/LoginFragment: Google sign in initiated
D/LoginFragment: Google sign in result received - Result code: -1
D/LoginFragment: Handling Google sign in result
D/LoginFragment: Account: votre.email@gmail.com
D/LoginFragment: ID Token: eyJhbGciOiJSUzI1NiIsImtpZCI6...
D/LoginFragment: Calling viewModel.loginWithGoogle
D/AuthViewModel: Login with Google started - Token: eyJhbGciOiJSUzI1NiIsImtpZCI6...
D/AuthRepository: Sending Google token to backend
D/BaseRepository: Making API call
D/BaseRepository: Response code: 200
D/BaseRepository: API call successful
D/AuthRepository: Backend success - Token: present
D/AuthRepository: JWT saved to DataStore
D/LoginFragment: Login result received: Success
D/LoginFragment: Login success - Token: present
D/LoginFragment: Navigating to home
```

---

### Étape 2: Identifier l'Erreur

#### Scénario A: Aucun log n'apparaît

**Problème:** Le bouton ne déclenche rien
**Solution:** Vérifiez que le bouton est bien configuré dans `fragment_login.xml`

#### Scénario B: Log "Result code: 0" (RESULT_CANCELED)

**Problème:** L'utilisateur a annulé ou l'authentification a échoué
**Solution:** Vérifiez le SHA-1 dans Google Cloud Console

#### Scénario C: Log "ID Token is null"

**Problème:** Le Client ID est incorrect
**Solution:** Vérifiez le Client ID dans `LoginFragment.kt` ligne 70

#### Scénario D: Log "Status code: 10" (Developer Error)

**Problème:** SHA-1 non configuré ou incorrect
**Solution:** Exécutez `get-sha1.bat` et ajoutez-le dans Google Cloud Console

#### Scénario E: Log "Exception during API call"

**Problème:** Le backend n'est pas accessible
**Solution:**

- Vérifiez que le backend est démarré
- Pour émulateur: utilisez `http://10.0.2.2:8080/`
- Pour appareil physique: utilisez votre IP locale

#### Scénario F: Log "Response code: 401" ou "Response code: 500"

**Problème:** Le backend ne peut pas valider le token
**Solution:** Vérifiez les logs du backend

---

### Étape 3: Tester le Backend Manuellement

```bash
# Test simple
curl -X POST http://localhost:8080/api/auth/google \
  -H "Content-Type: application/json" \
  -d '{"token": "test"}'
```

**Réponse attendue:** Une erreur de validation (normal avec un faux token)

**Si aucune réponse:** Le backend n'est pas démarré ou l'endpoint n'existe pas

---

## 🚀 Actions Immédiates

### 1. Synchroniser Gradle

```bash
# Dans Android Studio
File > Sync Project with Gradle Files
```

### 2. Nettoyer et Rebuilder

```bash
./gradlew clean
./gradlew build
```

### 3. Réinstaller l'Application

```bash
./gradlew installDebug
```

### 4. Vérifier les Logs

- Ouvrez Logcat
- Filtrez sur `LoginFragment`
- Testez la connexion Google
- **Copiez tous les logs** et analysez-les

---

## 📋 Checklist Rapide

- [ ] SHA-1 obtenu avec `get-sha1.bat`
- [ ] SHA-1 ajouté dans Google Cloud Console
- [ ] Attendu 2-3 minutes après l'ajout du SHA-1
- [ ] Backend démarré sur le port 8080
- [ ] Endpoint `/api/auth/google` implémenté
- [ ] Gradle synchronisé
- [ ] Application réinstallée
- [ ] Logs vérifiés dans Logcat

---

## 🆘 Prochaines Étapes

1. **Lancez l'application**
2. **Ouvrez Logcat** et filtrez sur `LoginFragment`
3. **Cliquez sur "Continuer avec Google"**
4. **Copiez les logs** qui apparaissent
5. **Partagez-les** pour un diagnostic précis

Les logs me diront exactement où le problème se situe !

---

## 📞 Logs à Partager

Après avoir testé, partagez ces informations:

```
1. Logs Logcat (filtre: LoginFragment)
2. Code d'erreur Google (si affiché)
3. Réponse du backend (si accessible)
4. SHA-1 utilisé
5. Client ID utilisé
```

---

**Bon diagnostic ! 🔍**
