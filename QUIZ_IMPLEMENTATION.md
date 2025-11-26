# 📝 Implémentation de la Fonctionnalité Quiz - Android

## ✅ Fichiers Créés

### 1. Models/DTOs (5 fichiers)

- `QuizResponse.kt` - Réponse du quiz avec questions
- `QuestionResponse.kt` - Réponse d'une question avec options
- `QuizSubmissionRequest.kt` - Requête de soumission du quiz
- `QuizAttemptResponse.kt` - Réponse d'une tentative de quiz
- `ResultatQuizResponse.kt` - Résultat détaillé du quiz avec corrections

### 2. Repository (1 fichier)

- `QuizRepository.kt` - Gestion des appels API quiz

### 3. UI - ViewModel (2 fichiers)

- `ModuleDetailViewModel.kt` - Mis à jour pour charger le quiz du module
- `QuizViewerViewModel.kt` - ViewModel pour le passage du quiz

### 4. UI - Fragments (2 fichiers)

- `ModuleDetailFragment.kt` - Mis à jour pour afficher le quiz
- `QuizViewerFragment.kt` - Fragment pour passer le quiz

### 5. Layouts (2 fichiers)

- `fragment_module_detail.xml` - Mis à jour avec section Quiz
- `fragment_quiz_viewer.xml` - Layout du passage de quiz

### 6. Navigation (1 fichier)

- `nav_cours.xml` - Mis à jour avec navigation vers QuizViewerFragment

### 7. Network (2 fichiers)

- `ApiService.kt` - Mis à jour avec endpoints Quiz
- `RemoteDataSource.kt` - Mis à jour avec méthodes Quiz

## 🎯 Fonctionnalités Implémentées

### Pour les ÉTUDIANTS :

#### 1. Affichage du Quiz dans le Module

- ✅ Section "📝 Quiz du module" dans ModuleDetailFragment
- ✅ Card avec titre, description et nombre de questions
- ✅ Badge indiquant le nombre de questions
- ✅ Bouton "Passer le quiz"

#### 2. Contrôle d'Accès au Quiz

- ✅ Quiz verrouillé si toutes les leçons ne sont pas complétées
- ✅ Message d'avertissement : "⚠️ Complétez toutes les leçons pour débloquer le quiz"
- ✅ Bouton désactivé avec texte "Quiz verrouillé"
- ✅ Bouton activé avec texte "Passer le quiz" quand débloqué

#### 3. Écran d'Accueil du Quiz (QuizViewerFragment)

- ✅ Affichage du titre et description du quiz
- ✅ Nombre de questions
- ✅ Meilleur score (si tentatives précédentes)
- ✅ Nombre de tentatives précédentes
- ✅ Bouton "Commencer le quiz"

#### 4. Passage du Quiz

- ✅ Barre de progression (Question X/Y)
- ✅ Affichage de la question
- ✅ Options de réponse (RadioButtons)
- ✅ Navigation "Précédent" / "Suivant"
- ✅ Bouton "Valider" sur la dernière question
- ✅ Sauvegarde des réponses lors de la navigation
- ✅ Validation : toutes les questions doivent être répondues

#### 5. Soumission du Quiz

- ✅ Calcul du temps passé
- ✅ Envoi des réponses au backend
- ✅ Affichage du loading pendant la soumission

#### 6. Affichage des Résultats

- ✅ Score en pourcentage avec couleur (vert ≥80%, orange ≥60%, rouge <60%)
- ✅ Message de félicitations adapté au score
- ✅ Nombre de réponses correctes / total
- ✅ Temps passé (format MM:SS)
- ✅ Bouton "Réessayer" pour refaire le quiz
- ✅ Bouton "Retour" pour revenir au module

#### 7. Historique des Tentatives

- ✅ Affichage du nombre de tentatives
- ✅ Affichage du meilleur score
- ✅ Rechargement après chaque tentative

## 📊 Endpoints API Utilisés

### Quiz

- `GET /api/quiz/module/{moduleId}` - Récupérer le quiz d'un module
- `GET /api/quiz/{quizId}` - Récupérer un quiz par ID

### Résultats Quiz

- `POST /api/quiz-resultats/quiz/{quizId}/submit` - Soumettre un quiz
- `GET /api/quiz-resultats/quiz/{quizId}/attempts` - Récupérer les tentatives
- `GET /api/quiz-resultats/quiz/{quizId}/best-score` - Récupérer le meilleur score
- `GET /api/quiz-resultats/{resultatId}` - Récupérer les détails d'un résultat

## 🎨 Design

### Couleurs Utilisées

- **Quiz Card** : Fond blanc, bordure arrondie 16dp
- **Bouton Quiz** : Violet (#9C27B0)
- **Badge Questions** : Violet clair
- **Score Excellent** : Vert (#4CAF50)
- **Score Moyen** : Orange (#FF9800)
- **Score Faible** : Rouge (#F44336)

### Animations

- Navigation : Slide in/out (droite/gauche)
- Transitions fluides entre les écrans

## 🔄 Flux Utilisateur

1. **Étudiant accède au module**

   - Voit la liste des leçons
   - Voit la section Quiz en bas

2. **Quiz verrouillé**

   - Si leçons non complétées : bouton désactivé + message

3. **Quiz débloqué**

   - Toutes les leçons complétées : bouton activé
   - Clic sur "Passer le quiz" → Navigation vers QuizViewerFragment

4. **Écran d'accueil du quiz**

   - Affichage des informations
   - Clic sur "Commencer le quiz"

5. **Passage du quiz**

   - Navigation entre les questions
   - Sélection des réponses
   - Validation finale

6. **Résultats**
   - Affichage du score et détails
   - Options : Réessayer ou Retour

## 🚀 Prochaines Étapes (Optionnelles)

### Améliorations Possibles

- [ ] Affichage des détails des réponses (correctes/incorrectes) après soumission
- [ ] Timer pour limiter le temps du quiz
- [ ] Sauvegarde locale des réponses en cas de perte de connexion
- [ ] Animations entre les questions
- [ ] Graphique de progression des scores
- [ ] Partage des résultats

### Partie Formateur (Non implémentée)

- [ ] Création de quiz
- [ ] Ajout/modification de questions
- [ ] Gestion des quiz
- [ ] Statistiques des résultats étudiants

## ✅ Tests à Effectuer

1. **Test du verrouillage**

   - Vérifier que le quiz est verrouillé si leçons non complétées
   - Vérifier le déverrouillage après complétion des leçons

2. **Test du passage du quiz**

   - Navigation entre les questions
   - Sauvegarde des réponses
   - Validation de la soumission

3. **Test des résultats**

   - Affichage correct du score
   - Affichage du temps passé
   - Mise à jour du meilleur score

4. **Test de la navigation**
   - Retour au module
   - Réessayer le quiz
   - Navigation entre les écrans

## 📝 Notes Importantes

- Les quiz sont liés aux modules (1 quiz par module)
- Le quiz est accessible uniquement aux étudiants inscrits au cours
- Les tentatives sont illimitées
- Le meilleur score est conservé
- Les réponses correctes ne sont pas affichées (à implémenter si souhaité)

## 🎉 Implémentation Complète !

Tous les fichiers nécessaires ont été créés et configurés. L'application est prête pour tester la fonctionnalité Quiz !
