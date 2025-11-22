@echo off
echo ========================================
echo Test Configuration Google Sign-In
echo ========================================
echo.

echo [1/5] Verification du SHA-1...
echo.
keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android 2>nul | findstr "SHA1"
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Impossible de recuperer le SHA-1
    echo Verifiez que le keystore debug existe
) else (
    echo OK: SHA-1 trouve
)
echo.

echo [2/5] Verification du package name...
echo.
findstr "namespace" app\build.gradle.kts
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Package name non trouve
) else (
    echo OK: Package name trouve
)
echo.

echo [3/5] Verification du Client ID...
echo.
findstr "requestIdToken" app\src\main\java\com\example\kawi_niveau_mobile_app\ui\auth\LoginFragment.kt
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Client ID non trouve
) else (
    echo OK: Client ID trouve
)
echo.

echo [4/5] Verification de la dependance Google Sign-In...
echo.
findstr "play-services-auth" app\build.gradle.kts
if %ERRORLEVEL% NEQ 0 (
    echo ERREUR: Dependance Google Sign-In manquante
) else (
    echo OK: Dependance Google Sign-In presente
)
echo.

echo [5/5] Test du backend...
echo.
curl -s -o nul -w "HTTP Status: %%{http_code}\n" http://localhost:8080/api/auth/google 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ATTENTION: Backend non accessible sur http://localhost:8080
    echo Assurez-vous que le backend est demarre
) else (
    echo OK: Backend repond
)
echo.

echo ========================================
echo Test termine
echo ========================================
echo.
echo PROCHAINES ETAPES:
echo 1. Copiez le SHA-1 ci-dessus
echo 2. Ajoutez-le dans Google Cloud Console
echo 3. Attendez 2-3 minutes
echo 4. Reinstallez l'application: gradlew installDebug
echo 5. Testez la connexion Google
echo.
pause
