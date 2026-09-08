# Plan de Implementación - Fases 1 y 2: Configuración y Capa de Datos

Este plan cubre la inicialización del proyecto Android "EduControl" y la implementación de la capa de persistencia con Room, siguiendo las especificaciones del `README.md`.

## User Review Required

> [!IMPORTANT]
> El proyecto se encuentra actualmente vacío (solo contiene el `README.md`). Procederé a inicializar la estructura completa del proyecto Android (Gradle, AndroidManifest, carpetas de fuentes).

> [!NOTE]
> Utilizaré `com.aprendiz.educontrol` como nombre de paquete base. Si prefieres otro, por favor indícalo.

## Proposed Changes

### Fase 1: Configuración Inicial del Proyecto y Dependencias

#### [NEW] [settings.gradle.kts](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/settings.gradle.kts)
Configuración de repositorios y nombre del proyecto.

#### [NEW] [build.gradle.kts](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/build.gradle.kts) (Proyecto)
Configuración de plugins (Kotlin, Android, KSP).

#### [NEW] [app/build.gradle.kts](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/build.gradle.kts)
- Habilitar `viewBinding`.
- Configurar dependencias de Room (2.6.1), Coroutines y Lifecycle.
- Configurar SDK (Min: 24, Target: 34).

#### [NEW] Estructura de Directorios
Creación de los siguientes paquetes bajo `app/src/main/java/com/aprendiz/educontrol/`:
- `data/`
- `ui/splash/`
- `ui/auth/`
- `ui/home/`
- `ui/materia/`

#### [NEW] [AndroidManifest.xml](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/AndroidManifest.xml)
Configuración básica de la aplicación.

---

### Fase 2: Capa de Datos y Persistencia (Room)

#### [NEW] [UserEntity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/data/entity/UserEntity.kt)
Entidad para usuarios: `id`, `email`, `password`.

#### [NEW] [MateriaEntity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/data/entity/MateriaEntity.kt)
Entidad para materias: `id`, `userId`, `nombreMateria`, `profesor`.

#### [NEW] [NotaEntity.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/data/entity/NotaEntity.kt)
Entidad para notas: `id`, `materiaId`, `nombreEvaluacion`, `calificacion`, `porcentaje`.

#### [NEW] DAOs ([UserDao.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/data/dao/UserDao.kt), [MateriaDao.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/data/dao/MateriaDao.kt), [NotaDao.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/data/dao/NotaDao.kt))
Interfaces para operaciones CRUD en la base de datos.

#### [NEW] [AppDatabase.kt](file:///C:/Users/Aprendiz/AndroidStudioProjects/EduControl/app/src/main/java/com/aprendiz/educontrol/data/AppDatabase.kt)
Clase abstracta con patrón Singleton para la base de datos Room.

## Verification Plan

### Automated Tests
- No se implementarán tests unitarios en esta fase inicial, pero se verificará la compilación del proyecto.

### Manual Verification
1. Verificar que el proyecto sincronice correctamente con Gradle.
2. Comprobar que no haya errores de compilación en las clases de Room (DAOs y Database).
3. Verificar la creación física de la estructura de carpetas.
