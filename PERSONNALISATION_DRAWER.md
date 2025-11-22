# 🎨 Guide de Personnalisation du Navigation Drawer

## 🎯 Personnalisations Rapides

### 1. Changer les Couleurs du Header

#### Option A: Modifier le Gradient

```xml
<!-- Dans app/src/main/res/drawable/nav_header_background.xml -->
<gradient
    android:angle="135"
    android:startColor="#VOTRE_COULEUR_1"
    android:centerColor="#VOTRE_COULEUR_2"
    android:endColor="#VOTRE_COULEUR_3"
    android:type="linear" />
```

#### Option B: Utiliser une Couleur Unie

```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="#4CAF50" />
</shape>
```

#### Option C: Utiliser une Image

```xml
<!-- Dans nav_header_home.xml -->
<LinearLayout
    android:background="@drawable/votre_image_background"
    ...>
```

---

### 2. Charger la Photo de Profil Réelle

#### Étape 1: Ajouter Glide dans build.gradle.kts

```kotlin
dependencies {
    implementation("com.github.bumptech.glide:glide:4.16.0")
}
```

#### Étape 2: Créer un Drawable pour Image Ronde

```xml
<!-- app/src/main/res/drawable/circle_image.xml -->
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <solid android:color="@android:color/white" />
</shape>
```

#### Étape 3: Charger l'Image dans HomeActivity

```kotlin
private fun loadUserInfo() {
    val headerView = binding.navView.getHeaderView(0)
    val profileImageView = headerView.findViewById<ImageView>(R.id.imageView_profile)

    lifecycleScope.launch {
        userPreferences.getToken().collect { token ->
            if (token != null) {
                val user = userRepository.getUserProfile(token)

                // Charger la photo avec Glide
                Glide.with(this@HomeActivity)
                    .load(user.profileImageUrl)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_profile_placeholder)
                    .circleCrop()
                    .into(profileImageView)

                nameTextView.text = "${user.firstName} ${user.lastName}"
                emailTextView.text = user.email
            }
        }
    }
}
```

---

### 3. Ajouter un Nouveau Item au Menu

#### Étape 1: Créer l'Icône

```xml
<!-- app/src/main/res/drawable/ic_settings.xml -->
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
    <path
        android:fillColor="#666666"
        android:pathData="M19.14,12.94c0.04,-0.3 0.06,-0.61 0.06,-0.94c0,-0.32 -0.02,-0.64 -0.07,-0.94l2.03,-1.58c0.18,-0.14 0.23,-0.41 0.12,-0.61l-1.92,-3.32c-0.12,-0.22 -0.37,-0.29 -0.59,-0.22l-2.39,0.96c-0.5,-0.38 -1.03,-0.7 -1.62,-0.94L14.4,2.81c-0.04,-0.24 -0.24,-0.41 -0.48,-0.41h-3.84c-0.24,0 -0.43,0.17 -0.47,0.41L9.25,5.35C8.66,5.59 8.12,5.92 7.63,6.29L5.24,5.33c-0.22,-0.08 -0.47,0 -0.59,0.22L2.74,8.87C2.62,9.08 2.66,9.34 2.86,9.48l2.03,1.58C4.84,11.36 4.8,11.69 4.8,12s0.02,0.64 0.07,0.94l-2.03,1.58c-0.18,0.14 -0.23,0.41 -0.12,0.61l1.92,3.32c0.12,0.22 0.37,0.29 0.59,0.22l2.39,-0.96c0.5,0.38 1.03,0.7 1.62,0.94l0.36,2.54c0.05,0.24 0.24,0.41 0.48,0.41h3.84c0.24,0 0.44,-0.17 0.47,-0.41l0.36,-2.54c0.59,-0.24 1.13,-0.56 1.62,-0.94l2.39,0.96c0.22,0.08 0.47,0 0.59,-0.22l1.92,-3.32c0.12,-0.22 0.07,-0.47 -0.12,-0.61L19.14,12.94zM12,15.6c-1.98,0 -3.6,-1.62 -3.6,-3.6s1.62,-3.6 3.6,-3.6s3.6,1.62 3.6,3.6S13.98,15.6 12,15.6z"/>
</vector>
```

#### Étape 2: Ajouter au Menu

```xml
<!-- Dans activity_home_drawer.xml -->
<item android:title="@string/menu_account">
    <menu>
        <item
            android:id="@+id/nav_profile"
            android:icon="@drawable/ic_profile"
            android:title="@string/menu_profile" />
        <item
            android:id="@+id/nav_settings"
            android:icon="@drawable/ic_settings"
            android:title="@string/menu_settings" />
        <item
            android:id="@+id/nav_logout"
            android:icon="@drawable/ic_logout"
            android:title="@string/menu_logout" />
    </menu>
</item>
```

#### Étape 3: Ajouter le String

```xml
<!-- Dans strings.xml -->
<string name="menu_settings">Paramètres</string>
```

#### Étape 4: Créer le Fragment

```kotlin
// SettingsFragment.kt
@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

#### Étape 5: Ajouter à la Navigation

```xml
<!-- Dans nav_home.xml -->
<fragment
    android:id="@+id/nav_settings"
    android:name="com.example.kawi_niveau_mobile_app.ui.home.SettingsFragment"
    android:label="@string/menu_settings"
    tools:layout="@layout/fragment_settings" />
```

---

### 4. Personnaliser le Style du Drawer

#### Changer la Largeur du Drawer

```xml
<!-- Dans nav_header_home.xml ou activity_home.xml -->
<com.google.android.material.navigation.NavigationView
    android:layout_width="280dp"  <!-- Changez cette valeur -->
    android:layout_height="match_parent"
    ... />
```

#### Changer la Couleur de Sélection

```xml
<!-- Dans res/values/colors.xml -->
<color name="nav_item_selected">#E8F5E9</color>

<!-- Dans res/color/nav_item_color.xml -->
<selector xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:color="@color/nav_item_selected" android:state_checked="true"/>
    <item android:color="@android:color/darker_gray"/>
</selector>

<!-- Dans activity_home.xml -->
<com.google.android.material.navigation.NavigationView
    app:itemTextColor="@color/nav_item_color"
    app:itemIconTint="@color/nav_item_color"
    ... />
```

---

### 5. Ajouter un Clic sur le Header

```kotlin
// Dans HomeActivity.onCreate()
val headerView = binding.navView.getHeaderView(0)
headerView.setOnClickListener {
    // Naviguer vers le profil
    navController.navigate(R.id.nav_profile)
    binding.drawerLayout.closeDrawers()
}
```

---

### 6. Ajouter un Badge de Notification

#### Étape 1: Créer le Badge

```xml
<!-- Dans activity_home_drawer.xml -->
<item
    android:id="@+id/nav_courses"
    android:icon="@drawable/ic_courses"
    android:title="@string/menu_courses"
    app:actionLayout="@layout/menu_badge" />
```

#### Étape 2: Créer le Layout du Badge

```xml
<!-- res/layout/menu_badge.xml -->
<TextView xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/badge"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_vertical"
    android:background="@drawable/badge_background"
    android:padding="4dp"
    android:text="3"
    android:textColor="@android:color/white"
    android:textSize="12sp" />
```

#### Étape 3: Mettre à Jour le Badge

```kotlin
// Dans HomeActivity
val menu = binding.navView.menu
val menuItem = menu.findItem(R.id.nav_courses)
val actionView = menuItem.actionView
val badge = actionView?.findViewById<TextView>(R.id.badge)
badge?.text = "5" // Nombre de nouveaux cours
```

---

### 7. Ajouter une Animation au Header

```kotlin
// Dans HomeActivity
val headerView = binding.navView.getHeaderView(0)
val profileImageView = headerView.findViewById<ImageView>(R.id.imageView_profile)

profileImageView.setOnClickListener {
    // Animation de rotation
    it.animate()
        .rotation(360f)
        .setDuration(500)
        .start()
}
```

---

### 8. Changer la Police du Header

```xml
<!-- Dans nav_header_home.xml -->
<TextView
    android:id="@+id/textView_name"
    android:fontFamily="@font/votre_police"
    android:textStyle="bold"
    ... />
```

---

### 9. Ajouter un Séparateur Personnalisé

```xml
<!-- Dans activity_home_drawer.xml -->
<group android:checkableBehavior="single">
    <item ... />
    <item ... />
</group>

<item android:title=""> <!-- Séparateur vide -->
    <menu>
        <item ... />
    </menu>
</item>
```

---

### 10. Gérer les Clics Personnalisés

```kotlin
// Dans HomeActivity
binding.navView.setNavigationItemSelectedListener { menuItem ->
    when (menuItem.itemId) {
        R.id.nav_logout -> {
            // Afficher une confirmation
            AlertDialog.Builder(this)
                .setTitle("Déconnexion")
                .setMessage("Voulez-vous vraiment vous déconnecter ?")
                .setPositiveButton("Oui") { _, _ ->
                    logout()
                }
                .setNegativeButton("Non", null)
                .show()
            true
        }
        R.id.nav_profile -> {
            // Ouvrir le profil avec animation
            navController.navigate(R.id.nav_profile)
            binding.drawerLayout.closeDrawers()
            true
        }
        else -> {
            menuItem.isChecked = true
            binding.drawerLayout.closeDrawers()
            navController.navigate(menuItem.itemId)
            true
        }
    }
}
```

---

## 🎨 Exemples de Thèmes

### Thème Sombre

```xml
<!-- Dans nav_header_background.xml -->
<gradient
    android:startColor="#263238"
    android:centerColor="#37474F"
    android:endColor="#455A64" />
```

### Thème Bleu

```xml
<gradient
    android:startColor="#42A5F5"
    android:centerColor="#2196F3"
    android:endColor="#1976D2" />
```

### Thème Orange

```xml
<gradient
    android:startColor="#FF9800"
    android:centerColor="#F57C00"
    android:endColor="#E65100" />
```

---

## 📚 Ressources Utiles

### Bibliothèques Recommandées

- **Glide** - Chargement d'images: `com.github.bumptech.glide:glide:4.16.0`
- **CircleImageView** - Images rondes: `de.hdodenhof:circleimageview:3.1.0`
- **Material Components** - Déjà inclus

### Outils de Design

- **Material Design Icons** - https://fonts.google.com/icons
- **Flaticon** - https://www.flaticon.com
- **Material Palette** - https://www.materialpalette.com

---

**Personnalisez votre Drawer selon vos besoins ! 🎨**
