# ✅ Checklist Google Sign-In

## 📋 Avant de tester

### 1. Configuration Android ✓

- [x] Dépendance Google Sign-In ajoutée dans `build.gradle.kts`
- [x] Bouton Google ajouté dans `fragment_login.xml`
- [x] Code d'intégration ajouté dans `LoginFragment.kt`
- [x] Endpoint backend configuré dans `ApiService.kt`
- [x] Repository et ViewModel mis à jour

### 2. Configuration Google Cloud Console ⚠️

- [ ] **SHA-1 obtenu** (exécutez `get-sha1.bat`)
- [ ] **SHA-1 ajouté** dans Google Cloud Console
- [ ] **Package name vérifié**: `com.example.kawi_niveau_mobile_app`
- [ ] **Client ID vérifié**: `428009874445-uirq408arbih2pstc2225h67faophn0j.apps.googleusercontent.com`
- [ ] **Attendu 2-3 minutes** après la configuration

### 3. Backend ⚠️

- [ ] **Backend démarré** sur le port 8080
- [ ] **Endpoint `/api/auth/google` implémenté**
- [ ] **DTO `OAuth2LoginRequest` créé**
- [ ] **Validation du token Google configurée**
- [ ] **Génération du JWT fonctionnelle**

### 4. Synchronisation Gradle ⚠️

- [ ] **Sync Gradle effectué** (File > Sync Project with Gradle Files)
- [ ] **Build réussi** (pas d'erreurs de compilation)

---

## 🚀 Pour tester

### Étape 1: Obtenir le SHA-1

```bash
# Double-cliquez sur get-sha1.bat
# OU exécutez manuellement:
keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```

**Résultat attendu:** Une ligne avec `SHA1: AA:BB:CC:...`

✅ SHA-1 copié: **********************\_\_\_**********************

---

### Étape 2: Configurer Google Cloud Console

1. Allez sur: https://console.cloud.google.com/apis/credentials
2. Trouvez votre OAuth 2.0 Client ID (type Android)
3. Cliquez sur "Modifier"
4. Ajoutez:
   - **SHA-1 certificate fingerprint:** (collez le SHA-1)
   - **Package name:** `com.example.kawi_niveau_mobile_app`
5. Cliquez sur "Enregistrer"
6. **Attendez 2-3 minutes**

✅ Configuration Google Cloud terminée: [ ]

---

### Étape 3: Vérifier le backend

Testez l'endpoint avec cURL ou Postman:

```bash
curl -X POST http://localhost:8080/api/auth/google \
  -H "Content-Type: application/json" \
  -d '{"token": "test"}'
```

**Résultat attendu:** Une réponse (même si erreur de validation)

✅ Backend répond: [ ]

---

### Étape 4: Synchroniser Gradle

Dans Android Studio:

1. **File** > **Sync Project with Gradle Files**
2. Attendez la fin de la synchronisation
3. Vérifiez qu'il n'y a pas d'erreurs

✅ Gradle synchronisé: [ ]

---

### Étape 5: Lancer l'application

```bash
./gradlew installDebug
```

Ou dans Android Studio: **Run 'app'** (Shift+F10)

✅ Application lancée: [ ]

---

### Étape 6: Tester Google Sign-In

1. Sur l'écran de connexion, cliquez sur **"Continuer avec Google"**
2. Sélectionnez un compte Google
3. Vérifiez que vous êtes redirigé vers l'écran d'accueil

✅ Connexion Google réussie: [ ]

---

## 🐛 En cas de problème

### Erreur: "Developer Error"

- ❌ SHA-1 non configuré ou incorrect
- ✅ **Solution:** Vérifiez le SHA-1 dans Google Cloud Console

### Erreur: "Sign in failed"

- ❌ Configuration Google Cloud pas encore propagée
- ✅ **Solution:** Attendez 2-3 minutes et réessayez

### Erreur: "API not enabled"

- ❌ Google Sign-In API non activée
- ✅ **Solution:** Activez l'API dans Google Cloud Console

### Erreur: Backend ne répond pas

- ❌ Backend non démarré ou URL incorrecte
- ✅ **Solution:** Vérifiez que le backend tourne sur le port 8080

### Erreur: "Invalid token"

- ❌ Backend ne peut pas valider le token
- ✅ **Solution:** Vérifiez la configuration OAuth2 du backend

---

## 📚 Documentation

- **Démarrage rapide:** `DEMARRAGE_RAPIDE_GOOGLE_SIGNIN.md`
- **Guide complet:** `INTEGRATION_GOOGLE_SIGNIN.md`
- **API Backend:** `API_BACKEND_GOOGLE_SIGNIN.md`
- **Résumé:** `RESUME_INTEGRATION.md`

---

## ✨ Après le test réussi

Une fois que tout fonctionne:

- [ ] Tester sur un appareil physique (pas seulement l'émulateur)
- [ ] Configurer le SHA-1 pour le keystore de release
- [ ] Tester la déconnexion
- [ ] Vérifier la persistance de la session

---

## 🎉 Félicitations !

Si toutes les cases sont cochées, votre intégration Google Sign-In est **complète et fonctionnelle** ! 🚀

---

**Date de test:** ******\_\_\_******

**Testé par:** ******\_\_\_******

**Résultat:** ✅ Succès / ❌ Échec

**Notes:**

---

---

---
