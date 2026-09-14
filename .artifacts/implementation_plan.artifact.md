# Plan de Implementación - Fase 3: Interfaz de Usuario - Pantallas de Autenticación y Splash

Este plan cubre el desarrollo del flujo inicial de la aplicación, incluyendo la pantalla de bienvenida (Splash) y la gestión de usuarios (Login y Registro).

## User Review Required

> [!IMPORTANT]
> Se utilizará `ViewBinding` para la interacción con las vistas.
> Las actividades se registrarán en el `AndroidManifest.xml`.
> La navegación inicial será: Splash -> Login -> Register (si es necesario) -> Home (pendiente de Fase 4).

## Proposed Changes

### [Componente] UI - Pantallas de Inicio

#### [NEW] [activity_splash.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/res/layout/activity_splash.xml)
Diseño simple con un logo (o texto representativo) y el nombre de la app.

#### [NEW] [SplashActivity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/ui/splash/SplashActivity.kt)
Lógica para esperar 2 segundos usando `lifecycleScope` y navegar a `LoginActivity`.

#### [NEW] [activity_login.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/res/layout/activity_login.xml)
Formulario de ingreso: Email, Password, Botón de Login y enlace a Registro.

#### [NEW] [LoginActivity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/ui/auth/LoginActivity.kt)
Lógica de autenticación consultando la base de datos Room.

#### [NEW] [activity_register.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/res/layout/activity_register.xml)
Formulario de registro: Email, Password y Botón de Crear Cuenta.

#### [NEW] [RegisterActivity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/ui/auth/RegisterActivity.kt)
Lógica para insertar nuevos usuarios en Room, validando que el email no esté duplicado.

#### [MODIFY] [AndroidManifest.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/AndroidManifest.xml)
Declaración de las nuevas actividades y configuración de `SplashActivity` como la actividad de inicio (`LAUNCHER`).

## Verification Plan

### Manual Verification
1. Ejecutar la app y verificar que se muestre el Splash durante 2 segundos.
2. Comprobar la transición automática a la pantalla de Login.
3. Probar el registro de un nuevo usuario y verificar que se guarde correctamente (intentando loguearse después).
4. Validar que los campos vacíos muestren errores visuales.
