# 🚀 Démarrage Rapide - Google Sign-In

## ⚡ En 3 étapes simples

### Étape 1️⃣ : Obtenir votre SHA-1

**Sur Windows, double-cliquez sur:** `get-sha1.bat`

Ou exécutez manuellement :

```powershell
keytool -list -v -keystore "$env:USERPROFILE\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```

📋 **Copiez le SHA-1** qui s'affiche (format: `AA:BB:CC:DD:...`)

---

### Étape 2️⃣ : Configurer Google Cloud Console

1. 🌐 Allez sur: https://console.cloud.google.com/apis/credentials
2. 🔍 Trouvez votre **OAuth 2.0 Client ID** (type Android)
3. ✏️ Cliquez sur **Modifier**
4. 📝 Ajoutez:
   - **SHA-1:** (celui que vous avez copié)
   - **Package name:** `com.example.kawi_niveau_mobile_app`
5. 💾 **Enregistrez**

⏱️ Attendez 2-3 minutes pour la propagation

---

### Étape 3️⃣ : Tester

1. ▶️ Lancez votre backend sur le port 8080
2. 📱 Lancez l'application Android
3. 👆 Cliquez sur **"Continuer avec Google"**
4. ✅ Sélectionnez un compte Google
5. 🎉 Vous devriez être connecté !

---

## 🔧 Configuration actuelle

| Paramètre       | Valeur                                                                     |
| --------------- | -------------------------------------------------------------------------- |
| **Client ID**   | `428009874445-uirq408arbih2pstc2225h67faophn0j.apps.googleusercontent.com` |
| **Backend URL** | `http://10.0.2.2:8080` (émulateur)                                         |
| **Endpoint**    | `POST /api/auth/google`                                                    |
| **Package**     | `com.example.kawi_niveau_mobile_app`                                       |

---

## ❓ Problèmes courants

### "Developer Error"

➡️ **Solution:** Vérifiez que le SHA-1 est bien ajouté dans Google Cloud Console

### "Sign in failed"

➡️ **Solution:** Attendez 2-3 minutes après avoir ajouté le SHA-1

### Backend ne répond pas

➡️ **Solution:** Vérifiez que le backend tourne sur `http://localhost:8080`

---

## 📞 Besoin d'aide ?

Consultez le guide complet: `INTEGRATION_GOOGLE_SIGNIN.md`

---

**C'est tout ! Votre Google Sign-In est prêt ! 🎉**
