@echo off
setlocal
cd /d "%~dp0\67client\67client"
call gradlew.bat build || exit /b 1
cd /d "%~dp0\Anubis\anubis-1.0-src"
call gradlew.bat build || exit /b 1
echo DigDown Client build complete. Check each project's build\libs folder.
endlocal
