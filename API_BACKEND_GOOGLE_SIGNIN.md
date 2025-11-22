# 📡 API Backend - Google Sign-In

## Endpoint requis

### POST `/api/auth/google`

Authentifie un utilisateur via Google OAuth2 et retourne un JWT.

---

## 📥 Requête

### Headers

```
Content-Type: application/json
```

### Body (JSON)

```json
{
  "token": "GOOGLE_ID_TOKEN_ICI"
}
```

**Exemple:**

```json
{
  "token": "eyJhbGciOiJSUzI1NiIsImtpZCI6IjE4MmU0NTBhMzVhMjA4MWZhYTFkOWFlMWQzZjZjZDNhNTk0NDUxODQiLCJ0eXAiOiJKV1QifQ..."
}
```

---

## 📤 Réponse

### Succès (200 OK)

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "user@example.com",
  "role": "ETUDIANT"
}
```

**Champs:**

- `token` (string): JWT pour les requêtes authentifiées
- `email` (string): Email de l'utilisateur
- `role` (string, optionnel): Rôle de l'utilisateur (ETUDIANT, ENSEIGNANT, etc.)

### Erreur (401 Unauthorized)

```json
{
  "message": "Token Google invalide"
}
```

### Erreur (500 Internal Server Error)

```json
{
  "message": "Erreur serveur"
}
```

---

## 🔐 Validation du token Google

Le backend doit:

1. **Vérifier le token Google** avec l'API Google
2. **Extraire les informations** (email, nom, etc.)
3. **Créer ou récupérer l'utilisateur** dans la base de données
4. **Générer un JWT** pour l'application
5. **Retourner le JWT** au client

---

## 💻 Exemple d'implémentation (Java/Spring Boot)

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody OAuth2LoginRequest request) {
        try {
            // 1. Vérifier le token Google
            GoogleIdToken idToken = googleTokenVerifier.verify(request.getToken());

            if (idToken == null) {
                return ResponseEntity.status(401)
                    .body(Map.of("message", "Token Google invalide"));
            }

            // 2. Extraire les informations
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // 3. Créer ou récupérer l'utilisateur
            User user = userService.findOrCreateGoogleUser(email, name);

            // 4. Générer le JWT
            String jwt = jwtTokenProvider.generateToken(user);

            // 5. Retourner la réponse
            return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getEmail(),
                user.getRole()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body(Map.of("message", "Erreur serveur: " + e.getMessage()));
        }
    }
}
```

---

## 📦 DTOs requis

### OAuth2LoginRequest.java

```java
public class OAuth2LoginRequest {
    private String token;

    // Getters et Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
```

### JwtResponse.java

```java
public class JwtResponse {
    private String token;
    private String email;
    private String role;

    // Constructeur
    public JwtResponse(String token, String email, String role) {
        this.token = token;
        this.email = email;
        this.role = role;
    }

    // Getters et Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
```

---

## 🔧 Configuration backend requise

### Dépendances Maven

```xml
<dependency>
    <groupId>com.google.api-client</groupId>
    <artifactId>google-api-client</artifactId>
    <version>2.2.0</version>
</dependency>
```

### Configuration Google Token Verifier

```java
@Configuration
public class GoogleConfig {

    @Value("${google.client.id}")
    private String clientId;

    @Bean
    public GoogleIdTokenVerifier googleTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(
            new NetHttpTransport(),
            new GsonFactory()
        )
        .setAudience(Collections.singletonList(clientId))
        .build();
    }
}
```

### application.properties

```properties
google.client.id=428009874445-uirq408arbih2pstc2225h67faophn0j.apps.googleusercontent.com
```

---

## 🧪 Test avec cURL

```bash
curl -X POST http://localhost:8080/api/auth/google \
  -H "Content-Type: application/json" \
  -d '{
    "token": "GOOGLE_ID_TOKEN_ICI"
  }'
```

---

## 🧪 Test avec Postman

1. **Méthode:** POST
2. **URL:** `http://localhost:8080/api/auth/google`
3. **Headers:**
   - `Content-Type: application/json`
4. **Body (raw JSON):**
   ```json
   {
     "token": "VOTRE_GOOGLE_ID_TOKEN"
   }
   ```

---

## 📝 Notes importantes

- Le token Google est un **JWT signé par Google**
- Il est **valide pendant 1 heure**
- Le backend doit **vérifier la signature** avec les clés publiques de Google
- Le **Client ID** doit correspondre à celui configuré dans Google Cloud Console
- Le backend doit gérer la **création automatique** des utilisateurs Google

---

## 🔗 Ressources

- [Google Identity Platform](https://developers.google.com/identity)
- [Vérifier les tokens Google](https://developers.google.com/identity/sign-in/web/backend-auth)
- [Google API Client Library](https://github.com/googleapis/google-api-java-client)

---

**✅ Le backend est maintenant prêt à recevoir les authentifications Google !**
