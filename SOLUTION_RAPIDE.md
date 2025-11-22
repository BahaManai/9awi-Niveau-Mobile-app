# 🚀 Solution Rapide - Problème Google Sign-In

## 🎯 Problème

Vous cliquez sur "Continuer avec Google", sélectionnez votre compte, mais **rien ne se passe**.

---

## ✅ Solution en 5 Étapes

### Étape 1: Obtenir le SHA-1 ✓

```bash
# Double-cliquez sur ce fichier:
get-sha1.bat
```

**Résultat:** Vous verrez une ligne comme:

```
SHA1: AA:BB:CC:DD:EE:FF:11:22:33:44:55:66:77:88:99:00:AA:BB:CC:DD
```

**→ COPIEZ ce SHA-1**

---

### Étape 2: Configurer Google Cloud Console ✓

1. Allez sur: https://console.cloud.google.com/apis/credentials
2. Trouvez votre **OAuth 2.0 Client ID** (type: Android)
3. Cliquez sur **Modifier** (icône crayon)
4. Dans **SHA-1 certificate fingerprints**, collez le SHA-1
5. Vérifiez le **Package name**: `com.example.kawi_niveau_mobile_app`
6. Cliquez sur **Enregistrer**
7. **⏰ ATTENDEZ 2-3 MINUTES** (important!)

---

### Étape 3: Vérifier le Backend ✓

**Option A: Tester avec curl**

```bash
curl http://localhost:8080/api/auth/google
```

**Option B: Tester avec le navigateur**
Ouvrez: http://localhost:8080/api/auth/google

**Résultat attendu:** Une réponse (même une erreur, c'est OK)

**Si aucune réponse:**

- Démarrez votre backend
- Vérifiez qu'il écoute sur le port 8080
- Vérifiez que l'endpoint `/api/auth/google` existe

---

### Étape 4: Nettoyer et Rebuilder ✓

```bash
# Nettoyage
gradlew clean

# Rebuild
gradlew build

# Réinstallation
gradlew installDebug
```

**OU dans Android Studio:**

- **Build** > **Clean Project**
- **Build** > **Rebuild Project**
- **Run** > **Run 'app'**

---

### Étape 5: Tester avec les Logs ✓

1. **Ouvrez Logcat** dans Android Studio
2. **Filtrez** avec: `LoginFragment`
3. **Lancez l'application**
4. **Cliquez** sur "Continuer avec Google"
5. **Observez les logs**

**Logs de succès:**

```
D/LoginFragment: Google sign in initiated
D/LoginFragment: Google sign in result received - Result code: -1
D/LoginFragment: Handling Google sign in result
D/LoginFragment: Account: votre.email@gmail.com
D/LoginFragment: ID Token: eyJhbGciOiJSUzI1NiIsImtpZCI6...
D/LoginFragment: Calling viewModel.loginWithGoogle
D/AuthViewModel: Login with Google started
D/AuthRepository: Sending Google token to backend
D/BaseRepository: Response code: 200
D/LoginFragment: Navigating to home
```

**Logs d'erreur:**

```
E/LoginFragment: Google sign in failed - Status code: 10
```

→ **Code 10 = SHA-1 non configuré** (retournez à l'étape 1)

---

## 🔍 Diagnostic Rapide

### Problème: "Developer Error" (Code 10)

**Cause:** SHA-1 non configuré ou incorrect

**Solution:**

1. Exécutez `get-sha1.bat`
2. Ajoutez le SHA-1 dans Google Cloud Console
3. Attendez 2-3 minutes
4. Réinstallez l'application

---

### Problème: "ID Token is null"

**Cause:** Client ID incorrect

**Solution:**
Vérifiez dans `LoginFragment.kt` ligne 70:

```kotlin
.requestIdToken("428009874445-uirq408arbih2pstc2225h67faophn0j.apps.googleusercontent.com")
```

Ce Client ID doit correspondre à celui dans Google Cloud Console.

---

### Problème: "Network error" ou "Connection refused"

**Cause:** Backend non accessible

**Solution:**

- **Émulateur:** Utilisez `http://10.0.2.2:8080/` (déjà configuré)
- **Appareil physique:** Changez pour votre IP locale dans `app/build.gradle.kts`

---

### Problème: "Response code: 401" ou "500"

**Cause:** Le backend ne peut pas valider le token

**Solution:**

1. Vérifiez les logs du backend
2. Vérifiez que l'endpoint `/api/auth/google` est implémenté
3. Vérifiez la configuration OAuth2 du backend

---

## 🛠️ Script de Test Automatique

J'ai créé un script pour tester votre configuration:

```bash
# Double-cliquez sur:
test-google-signin.bat
```

Ce script vérifie:

- ✓ SHA-1
- ✓ Package name
- ✓ Client ID
- ✓ Dépendance Google Sign-In
- ✓ Backend accessible

---

## 📋 Checklist Complète

Avant de tester, vérifiez:

- [ ] SHA-1 obtenu avec `get-sha1.bat`
- [ ] SHA-1 ajouté dans Google Cloud Console
- [ ] Attendu 2-3 minutes après l'ajout
- [ ] Backend démarré sur le port 8080
- [ ] Endpoint `/api/auth/google` implémenté
- [ ] Gradle synchronisé (File > Sync Project)
- [ ] Projet nettoyé et rebuilder
- [ ] Application réinstallée
- [ ] Logcat ouvert et filtré sur `LoginFragment`

---

## 🎯 Test Final

1. **Lancez l'application**
2. **Cliquez** sur "Continuer avec Google"
3. **Sélectionnez** votre compte Google
4. **Résultat attendu:** Redirection vers l'écran d'accueil

---

## 🆘 Toujours Bloqué?

Si le problème persiste après avoir suivi toutes ces étapes:

1. **Exécutez** `test-google-signin.bat`
2. **Copiez** les logs de Logcat (filtre: `LoginFragment`)
3. **Vérifiez** la configuration dans Google Cloud Console
4. **Consultez** `DIAGNOSTIC_PROBLEME.md` pour plus de détails

---

## 📚 Documentation Complète

- **Diagnostic détaillé:** `DIAGNOSTIC_PROBLEME.md`
- **Guide complet:** `INTEGRATION_GOOGLE_SIGNIN.md`
- **Debug:** `DEBUG_GOOGLE_SIGNIN.md`
- **Checklist:** `CHECKLIST_GOOGLE_SIGNIN.md`

---

## ✨ Modifications Apportées

J'ai ajouté des **logs détaillés** dans:

- ✓ `LoginFragment.kt` - Logs de l'authentification Google
- ✓ `AuthViewModel.kt` - Logs du ViewModel
- ✓ `AuthRepository.kt` - Logs du Repository
- ✓ `BaseRepository.kt` - Logs des appels API

Ces logs vous permettront de voir **exactement** où le problème se situe.

---

**Bonne chance ! 🚀**

Si vous voyez des logs d'erreur, partagez-les pour un diagnostic précis.
