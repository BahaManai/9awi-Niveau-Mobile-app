@echo off
echo ========================================
echo   Obtention du SHA-1 pour Google Sign-In
echo ========================================
echo.
echo Recherche du keystore debug...
echo.

set KEYSTORE_PATH=%USERPROFILE%\.android\debug.keystore

if exist "%KEYSTORE_PATH%" (
    echo Keystore trouve: %KEYSTORE_PATH%
    echo.
    echo Extraction du SHA-1...
    echo.
    keytool -list -v -keystore "%KEYSTORE_PATH%" -alias androiddebugkey -storepass android -keypass android | findstr "SHA1"
    echo.
    echo ========================================
    echo Copiez le SHA-1 ci-dessus et ajoutez-le
    echo dans Google Cloud Console
    echo ========================================
) else (
    echo ERREUR: Keystore debug non trouve!
    echo Chemin attendu: %KEYSTORE_PATH%
)

echo.
pause
