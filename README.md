# 📚 EduControl - Control de Calificaciones Académicas

Aplicación móvil desarrollada en Android (Kotlin) para la gestión de asignaturas y cálculo automático de promedios ponderados, utilizando persistencia de datos local con Jetpack Room y arquitectura basada en Activities.

---

## 🛠️ Stack Tecnológico
* **Lenguaje:** Kotlin
* **Interfaz de Usuario:** XML con `ConstraintLayout`, `Material Design 3` y `ViewBinding`
* **Persistencia de Datos:** Jetpack Room (Base de datos SQLite local)
* **Concurrencia:** Kotlin Coroutines (`lifecycleScope`)
* **Navegación:** Activities e Intents explícitos con paso de parámetros (`putExtra`)

---

## 📋 Fases de Desarrollo

### ⚙️ Fase 1: Configuración Inicial del Proyecto y Dependencias
*Objetivo: Preparar el entorno de trabajo en Android Studio y habilitar las herramientas necesarias para evitar errores de compilación futuros.*

1. **Creación del Proyecto:**
   * Abrir Android Studio y crear un nuevo proyecto tipo **Empty Views Activity** (asegúrate de que use *Views*, no Jetpack Compose).
   * Configurar el lenguaje en **Kotlin** y definir un `Minimum SDK` compatible (recomendado API 24 o superior).
2. **Configuración de Gradle (`build.gradle.kts` - Module: app):**
   * Habilitar **ViewBinding** dentro del bloque `buildFeatures`:
     ```kotlin
     buildFeatures {
         viewBinding = true
     }
     ```
   * Agregar las dependencias necesarias para **Room** y **Coroutines** (asegúrate de incluir los plugins de KSP o KAPT según corresponda a tu versión de Gradle):
     ```kotlin
     val roomVersion = "2.6.1"
     implementation("androidx.room:room-runtime:$roomVersion")
     implementation("androidx.room:room-ktx:$roomVersion")
     ksp("androidx.room:room-compiler:$roomVersion") // O usa kapt si prefieres
     ```
3. **Estructura de Paquetes:**
   * Organiza tus carpetas dentro de `com.tuusuario.educontrol` en subpaquetes limpios:
     * `data/` (Entities, DAOs, AppDatabase)
     * `ui/splash/`
     * `ui/auth/` (Login y Registro)
     * `ui/home/`
     * `ui/materia/` (Detalle y registro de notas)

---

### 🗄️ Fase 2: Capa de Datos y Persistencia (Base de Datos Room)
*Objetivo: Diseñar el modelo relacional local para evitar problemas de pérdida de datos o consultas nulas.*

1. **Creación de Entidades (`Entity`):**
   * **`UserEntity`**: `id` (Primary Key AutoGenerate), `email`, `password`.
   * **`MateriaEntity`**: `id` (Primary Key AutoGenerate), `userId` (Foránea), `nombreMateria`, `profesor`.
   * **`NotaEntity`**: `id` (Primary Key AutoGenerate), `materiaId` (Foránea), `nombreEvaluacion`, `calificacion` (Double), `porcentaje` (Double).
2. **Creación de los DAOs (Data Access Objects):**
   * Escribir las consultas SQL básicas (`@Query`, `@Insert`, `@Update`, `@Delete`) para cada entidad usando corrutinas (`suspend fun`).
3. **Configuración de la Base de Datos (`AppDatabase`):**
   * Crear la clase abstracta con el patrón *Singleton* para asegurar una única instancia de la base de datos durante toda la ejecución de la app.

---

### 🎨 Fase 3: Interfaz de Usuario - Pantallas de Autenticación y Splash
*Objetivo: Desarrollar el flujo inicial de entrada y validación de usuarios.*

1. **`SplashActivity`:**
   * Diseñar una vista limpia con el logo en el centro usando `ConstraintLayout`.
   * Usar una corrutina en el `onCreate` con un retraso de 2 segundos para redirigir a `LoginActivity`.
2. **`LoginActivity` y `RegisterActivity`:**
   * Implementar `ViewBinding` obligatoriamente en ambas Activities (ej: `private lateinit var binding: ActivityLoginBinding`).
   * Validar que los campos de texto no estén vacíos antes de interactuar con la base de datos.
   * Conectar el botón de registro para insertar el usuario en Room y el de login para verificar credenciales.

---

### 🏠 Fase 4: Pantalla Principal (`HomeActivity`) y Listado de Materias
*Objetivo: Mostrar el panel central con el resumen académico del estudiante.*

1. **Diseño del Home (`activity_home.xml`):**
   * Incluir un componente de tipo `TextView` destacado para mostrar el **Promedio General Acumulado**.
   * Agregar un `RecyclerView` para listar las materias y un `FloatingActionButton` (FAB) para agregar nuevas asignaturas.
2. **Adaptador del RecyclerView (`MateriaAdapter`):**
   * Crear la tarjeta visual (`item_materia.xml`) usando `MaterialCardView`.
   * Implementar el adaptador para pintar dinámicamente el nombre de la materia, el profesor y el promedio parcial calculado de esa asignatura.
3. **Conexión con la BD:**
   * Lanzar una corrutina (`lifecycleScope.launch`) para consultar las materias del usuario autenticado y pasárselas al adaptador.

---

### 📝 Fase 5: Formularios, Validaciones y Detalle de Evaluaciones
*Objetivo: Permitir el registro seguro de notas y el cálculo matemático ponderado.*

1. **Pantalla de Registro de Materia / Nota (`AddMateriaActivity` / `AddNotaActivity`):**
   * Formularios limpios con validación estricta de tipos de datos (ej. asegurar que el porcentaje ingresado en las notas no haga que la sumatoria total supere el 100% de la materia).
2. **Pantalla de Detalle de Materia (`DetalleMateriaActivity`):**
   * Al hacer clic en una materia del `HomeActivity`, enviar su `materiaId` mediante un `Intent`.
   * Cargar un segundo `RecyclerView` que muestre exclusivamente las notas parciales registradas para esa materia (Ej: Parcial 1 - 4.5 [30%]).
3. **Lógica de Cálculo de Promedio:**
   * Implementar una función matemática que multiplique cada nota por su porcentaje correspondiente y divida entre la suma de los porcentajes evaluados hasta el momento.

---

### 🧪 Fase 6: Pruebas Locales y Depuración (¡Para evitar tus errores pasados!)
*Objetivo: Garantizar la estabilidad de la app antes de entregarla.*

1. **Prueba de Ciclo de Vida:** Gira la pantalla de tu celular (rotación) en el `HomeActivity` y en los formularios para verificar que `ViewBinding` y los estados no provoquen cierres inesperados (*crashes*).
2. **Prueba de Hilos (Corrutinas):** Asegúrate de que ninguna consulta a Room se ejecute en el hilo principal (*Main Thread*). Si ves una excepción de tipo `RoomCannotAccessDatabaseOnMainThreadException`, verifica que estés usando bloques `suspend` y `lifecycleScope.launch(Dispatchers.IO)`.
3. **Limpieza de Caché:** Si Room te arroja errores raros de esquemas de base de datos tras modificar las entidades, ve al menú superior de Android Studio: `Build > Clean Project` y luego `Rebuild Project`.
