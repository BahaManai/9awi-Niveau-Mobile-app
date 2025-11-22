# 🌐 Guide Visuel: Configurer le SHA-1 dans Google Cloud Console

## 🎯 Objectif

Ajouter votre SHA-1 dans Google Cloud Console pour résoudre le problème "Connexion annulée".

---

## 📋 Prérequis

**Avant de commencer, obtenez votre SHA-1:**

```bash
# Double-cliquez sur:
get-sha1.bat
```

**Copiez la ligne complète, exemple:**

```
SHA1: AA:BB:CC:DD:EE:FF:11:22:33:44:55:66:77:88:99:00:AA:BB:CC:DD
```

---

## 🚀 Étapes Détaillées

### Étape 1: Ouvrir Google Cloud Console

**URL:** https://console.cloud.google.com/apis/credentials

**Vous verrez une page avec:**

```
┌─────────────────────────────────────────────────────────────┐
│ APIs & Services > Credentials                               │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ OAuth 2.0 Client IDs                                        │
│                                                             │
│ ┌─────────────────────────────────────────────────────┐   │
│ │ Name                    Type        Created         │   │
│ ├─────────────────────────────────────────────────────┤   │
│ │ Android client 1        Android     Jan 1, 2024    │ ✏️ │
│ │ Web client 1            Web         Jan 1, 2024    │ ✏️ │
│ └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

---

### Étape 2: Trouver le Bon OAuth 2.0 Client ID

**Cherchez celui avec:**

- **Type:** Android
- **Package name:** `com.example.kawi_niveau_mobile_app`

**Si vous avez plusieurs clients Android:**

1. Cliquez sur chacun (icône crayon ✏️)
2. Vérifiez le Package name
3. Trouvez celui qui correspond à votre app

---

### Étape 3: Modifier le Client ID

**Cliquez sur l'icône crayon ✏️ à droite**

**Vous verrez:**

```
┌─────────────────────────────────────────────────────────────┐
│ Edit OAuth client                                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ Name                                                        │
│ ┌─────────────────────────────────────────────────────┐   │
│ │ Android client 1                                    │   │
│ └─────────────────────────────────────────────────────┘   │
│                                                             │
│ Package name                                                │
│ ┌─────────────────────────────────────────────────────┐   │
│ │ com.example.kawi_niveau_mobile_app                  │   │
│ └─────────────────────────────────────────────────────┘   │
│                                                             │
│ SHA-1 certificate fingerprints                              │
│ ┌─────────────────────────────────────────────────────┐   │
│ │ (vide ou avec d'anciens SHA-1)                      │   │
│ └─────────────────────────────────────────────────────┘   │
│                                                             │
│ [+ ADD FINGERPRINT]                                         │
│                                                             │
│                                    [CANCEL]  [SAVE]         │
└─────────────────────────────────────────────────────────────┘
```

---

### Étape 4: Vérifier le Package Name

**IMPORTANT:** Le Package name doit être **exactement:**

```
com.example.kawi_niveau_mobile_app
```

**Pas de:**

- Espaces
- Majuscules différentes
- Caractères en plus ou en moins

**Si le Package name est différent:**

- Vous êtes sur le mauvais Client ID
- Retournez à l'étape 2

---

### Étape 5: Ajouter le SHA-1

**1. Cliquez sur le bouton:** `[+ ADD FINGERPRINT]`

**2. Un nouveau champ apparaît:**

```
┌─────────────────────────────────────────────────────────────┐
│ SHA-1 certificate fingerprints                              │
│ ┌─────────────────────────────────────────────────────┐   │
│ │ [Collez votre SHA-1 ici]                            │ ❌ │
│ └─────────────────────────────────────────────────────┘   │
│                                                             │
│ [+ ADD FINGERPRINT]                                         │
└─────────────────────────────────────────────────────────────┘
```

**3. Collez votre SHA-1** (celui copié depuis `get-sha1.bat`)

**Exemple:**

```
AA:BB:CC:DD:EE:FF:11:22:33:44:55:66:77:88:99:00:AA:BB:CC:DD
```

**4. Vérifiez que:**

- Le SHA-1 est complet (20 paires de caractères séparées par `:`)
- Pas d'espaces au début ou à la fin
- Format correct: `XX:XX:XX:XX:...`

---

### Étape 6: Enregistrer

**1. Cliquez sur le bouton:** `[SAVE]` (en bas à droite)

**2. Vous verrez un message de confirmation:**

```
┌─────────────────────────────────────────────────────────────┐
│ ✓ OAuth client updated successfully                         │
└─────────────────────────────────────────────────────────────┘
```

**3. ⏰ ATTENDEZ 2-3 MINUTES**

- La configuration doit se propager
- Ne testez pas immédiatement !
- Prenez un café ☕

---

### Étape 7: Vérification

**Après avoir enregistré, vous devriez voir:**

```
┌─────────────────────────────────────────────────────────────┐
│ Edit OAuth client                                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│ Package name                                                │
│ ┌─────────────────────────────────────────────────────┐   │
│ │ com.example.kawi_niveau_mobile_app                  │   │
│ └─────────────────────────────────────────────────────┘   │
│                                                             │
│ SHA-1 certificate fingerprints                              │
│ ┌─────────────────────────────────────────────────────┐   │
│ │ AA:BB:CC:DD:EE:FF:11:22:33:44:55:66:77:88:99:00:... │ ❌ │
│ └─────────────────────────────────────────────────────┘   │
│                                                             │
│ [+ ADD FINGERPRINT]                                         │
└─────────────────────────────────────────────────────────────┘
```

**✅ Parfait ! Le SHA-1 est ajouté.**

---

## ⏰ Temps d'Attente Important

### Pourquoi attendre 2-3 minutes ?

La configuration Google Cloud Console doit se **propager** sur tous les serveurs de Google dans le monde.

**Timeline:**

```
0 min  → Vous cliquez sur SAVE
        ↓
1 min  → Configuration en cours de propagation...
        ↓
2 min  → Propagation presque terminée...
        ↓
3 min  → ✅ Configuration propagée partout
        ↓
        → Vous pouvez tester maintenant !
```

**Si vous testez trop tôt:**

- Vous verrez encore "Connexion annulée"
- Attendez 1-2 minutes de plus

---

## 🔍 Vérifications Importantes

### ✅ Checklist Avant de Sauvegarder

- [ ] Package name = `com.example.kawi_niveau_mobile_app`
- [ ] SHA-1 complet (20 paires de caractères)
- [ ] SHA-1 au bon format: `XX:XX:XX:XX:...`
- [ ] Pas d'espaces au début ou à la fin du SHA-1
- [ ] Vous êtes sur le bon Client ID (type: Android)

### ✅ Checklist Après avoir Sauvegardé

- [ ] Message de confirmation affiché
- [ ] SHA-1 visible dans la liste
- [ ] Attendu 2-3 minutes
- [ ] Application réinstallée: `.\gradlew installDebug`

---

## 🎯 Après la Configuration

### Étape 8: Réinstaller l'Application

**Dans Android Studio:**

```
Run → Run 'app' (Shift+F10)
```

**OU en ligne de commande:**

```bash
.\gradlew clean
.\gradlew installDebug
```

### Étape 9: Tester

1. **Ouvrez Logcat** dans Android Studio
2. **Filtrez** avec: `LoginFragment`
3. **Lancez l'application**
4. **Cliquez** sur "Continuer avec Google"
5. **Sélectionnez** votre compte

**Résultat attendu dans Logcat:**

```
D/LoginFragment: Google sign in initiated
D/LoginFragment: Google sign in result received - Result code: -1
D/LoginFragment: Handling Google sign in result
D/LoginFragment: Account: votre.email@gmail.com
D/LoginFragment: ID Token: eyJhbGciOiJSUzI1NiIsImtpZCI6...
D/LoginFragment: Calling viewModel.loginWithGoogle
```

**Result code: -1** = `RESULT_OK` = **Succès !** ✅

---

## ❌ Problèmes Courants

### Problème 1: "Je ne trouve pas le bon Client ID"

**Solution:**

1. Cherchez celui avec **Type: Android**
2. Vérifiez le Package name dans chaque Client ID
3. Si aucun ne correspond, créez-en un nouveau:
   - Cliquez sur **"+ CREATE CREDENTIALS"**
   - Sélectionnez **"OAuth client ID"**
   - Type: **Android**
   - Package name: `com.example.kawi_niveau_mobile_app`
   - SHA-1: (collez votre SHA-1)

### Problème 2: "Le Package name est différent"

**Solution:**

- Vous êtes sur le mauvais Client ID
- Cherchez celui avec le bon Package name
- Ou créez-en un nouveau avec le bon Package name

### Problème 3: "J'ai plusieurs SHA-1, lequel utiliser ?"

**Solution:**

- Utilisez celui de votre **debug keystore**
- C'est celui retourné par `get-sha1.bat`
- Vous pouvez ajouter plusieurs SHA-1 (debug + release)

### Problème 4: "Ça ne marche toujours pas après 3 minutes"

**Solution:**

1. Vérifiez que le SHA-1 est bien enregistré (rechargez la page)
2. Attendez encore 2 minutes
3. Réinstallez l'application: `.\gradlew clean installDebug`
4. Redémarrez Android Studio
5. Vérifiez les logs Logcat

---

## 📞 Support

**Si le problème persiste, partagez:**

1. **Screenshot de votre configuration Google Cloud Console**

   - Montrant le Package name
   - Montrant le SHA-1 ajouté

2. **Votre SHA-1**

   ```bash
   get-sha1.bat
   ```

3. **Logs Logcat**

   ```
   Filtre: LoginFragment
   ```

4. **Temps d'attente**
   - Combien de temps avez-vous attendu après avoir cliqué sur SAVE ?

---

## 🎉 Résultat Final

Après avoir suivi ce guide:

```
✅ SHA-1 ajouté dans Google Cloud Console
✅ Configuration propagée (après 2-3 minutes)
✅ Application réinstallée
✅ Connexion Google fonctionne
✅ Plus de message "Connexion annulée"
✅ Redirection vers l'écran d'accueil
```

---

**Bon courage ! 🚀**

Le SHA-1 est la clé pour résoudre le problème "Connexion annulée".
