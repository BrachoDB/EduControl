# Tareas de Implementación - EduControl

## Fase 1: Configuración Inicial del Proyecto y Dependencias
- [x] Crear `.gitignore` para excluir archivos innecesarios (incluyendo `.artifacts/`).
- [x] Crear `settings.gradle.kts`.
- [x] Crear `build.gradle.kts` (Proyecto).
- [x] Crear `app/build.gradle.kts` (Modulo app) con ViewBinding y dependencias de Room.
- [x] Crear `AndroidManifest.xml`.
- [x] Crear estructura de directorios (`data`, `ui`, etc.).
- [x] Crear archivos de recursos básicos (`strings.xml`, `themes.xml`, `colors.xml`).

## Fase 2: Capa de Datos y Persistencia (Room)
- [x] Implementar `UserEntity`.
- [x] Implementar `MateriaEntity`.
- [x] Implementar `NotaEntity`.
- [x] Crear `UserDao`.
- [x] Crear `MateriaDao`.
- [x] Crear `NotaDao`.
- [x] Crear `AppDatabase` (Singleton).

## Fase 3: Interfaz de Usuario - Pantallas de Autenticación y Splash
- [x] Crear `activity_splash.xml`.
- [x] Crear `SplashActivity.kt` con retraso de 2s.
- [x] Crear `activity_login.xml`.
- [x] Crear `LoginActivity.kt` con lógica de Room.
- [x] Crear `activity_register.xml`.
- [x] Crear `RegisterActivity.kt` con lógica de Room.
- [x] Registrar actividades en `AndroidManifest.xml`.
