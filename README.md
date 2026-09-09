# 📚 EduControl - Control de Calificaciones Escolares

Aplicación móvil desarrollada en Android (Kotlin) para la gestión académica escolar y cálculo automático de promedios ponderados por periodo/asignatura, utilizando persistencia de datos local con Jetpack Room y arquitectura basada en Activities.

---

## 🛠️ Stack Tecnológico
* **Lenguaje:** Kotlin
* **Interfaz de Usuario:** XML con `ConstraintLayout`, `Material Design 3 (M3)` y `ViewBinding`
* **Persistencia de Datos:** Jetpack Room (Base de datos SQLite local)
* **Concurrencia:** Kotlin Coroutines (`lifecycleScope` / `Dispatchers.IO`)
* **Navegación:** Activities e Intents explícitos con paso de parámetros (`putExtra`)

---

## 🎨 Sistema de Diseño y Requisitos Visuales (UI/UX)

Para alejar la interfaz de apariencias genéricas o grises, la aplicación implementa una estética limpia y moderna basada en tonos **verde esmeralda, turquesa/teal y menta**, combinados con tarjetas translúcidas de bordes redondeados y fondos suaves.

### 🎨 Paleta de Colores (`res/values/colors.xml`)
* **Primary / Emerald Accent:** `#0D9488` (Teal / Esmeralda principal para cabeceras de promedio, badges y acciones clave)
* **Dark Teal / Hero:** `#115E59` (Contenedor principal del resumen semestral/académico)
* **Surface Background:** `#F0FDF4` / `#F8FAFC` (Fondo general suave para descanso visual)
* **Card Surface:** `#FFFFFF` con trazo `#E2E8F0` y elevaciones sutiles (`cardCornerRadius="16dp"` a `20dp"`)
* **Score Badges (Semáforo de Notas):**
  * *Aprobado / Sobresaliente (≥ 3.0):* Fondo `#DCFCE7`, texto `#15803D`
  * *En Riesgo / Bajo (< 3.0):* Fondo `#FEE2E2`, texto `#B91C1C`
* **Form & Button Accent:** `#1E293B` / `#0F766E` (Botones de acción de formulario y bordes de input estilizados)

### 📌 Directriz de Datos de Demostración (Mock Data)
> **Importante para desarrollo y pruebas:** Todos los ejemplos visuales y datos iniciales deben representar asignaturas y evaluaciones de **nivel escolar/colegio** (secundaria o bachillerato), tales como:
> * **Materias:** *Matemáticas*, *Historia y Geografía*, *Biología*, *Inglés*, *Lengua Castellana*, *Educación Física*.
> * **Tipos de Evaluaciones:** *Parcial 1 (Álgebra)*, *Quiz (Geometría)*, *Taller en Clase*, *Examen Final*, *Exposición Oral*.

---

## 📋 Fases de Desarrollo

### ⚙️ Fase 1: Configuración Inicial del Proyecto y Dependencias
*Objetivo: Preparar el entorno en Android Studio asegurando el soporte para M3, ViewBinding y Room.*

1. **Creación del Proyecto:**
   * Tipo **Empty Views Activity** (Kotlin, Min SDK 24+).
2. **Configuración de Gradle (`build.gradle.kts` - Module: app):**
   * Habilitar **ViewBinding**:
     ```kotlin
     buildFeatures {
         viewBinding = true
     }
     ```
   * Dependencias necesarias:
     ```kotlin
     val roomVersion = "2.6.1"
     implementation("androidx.room:room-runtime:$roomVersion")
     implementation("androidx.room:room-ktx:$roomVersion")
     ksp("androidx.room:room-compiler:$roomVersion") // O kapt según corresponda
     implementation("com.google.android.material:material:1.11.0")
     ```
3. **Estructura Modular de Paquetes:**
   * `data/local/` (Entities, DAOs, AppDatabase)
   * `ui/splash/`
   * `ui/auth/`
   * `ui/home/` (Panel principal escolar)
   * `ui/materia/` (Detalle de asignatura, lista de notas y formulario)

---

### 🗄️ Fase 2: Capa de Datos y Persistencia (Room)
*Objetivo: Diseñar el modelo relacional adaptado a asignaturas escolares y sus notas parciales.*

1. **Entidades (`@Entity`):**
   * **`UserEntity`**: `id` (PK Auto), `email`, `password`.
   * **`MateriaEntity`**: `id` (PK Auto), `userId` (FK), `nombreMateria` (ej: *Matemáticas*), `profesor` (ej: *Prof. García*).
   * **`NotaEntity`**: `id` (PK Auto), `materiaId` (FK), `nombreEvaluacion` (ej: *Taller Ecuaciones*), `calificacion` (Double de 0.0 a 5.0), `porcentaje` (Double de 1% a 100%).
2. **DAOs:**
   * Operaciones suspendidas (`suspend fun`) para inserción, consulta de materias por estudiante y sumatoria de ponderaciones por materia (`SUM(porcentaje)`).
3. **Base de Datos (`AppDatabase`):**
   * Singleton thread-safe asegurando una única conexión local SQLite.

---

### 🎨 Fase 3: Interfaz de Usuario - Pantallas de Acceso y Estilo Inicial
*Objetivo: Experiencia de inicio atractiva y bienvenida alineada a la paleta esmeralda.*

1. **`SplashActivity`:**
   * Fondo menta claro con logo institucional o libro académico y transición diferida (2 seg) hacia `LoginActivity`.
2. **`LoginActivity` y `RegisterActivity`:**
   * Implementación de `ViewBinding` sin excepciones.
   * `TextInputLayout` con esquinas redondeadas (`boxCornerRadius = 12dp`) e iconos ilustrativos.
   * Validación local de correo y clave antes de disparar la consulta a Room.

---

### 🏠 Fase 4: Panel Principal Escolar (`HomeActivity`) y Tarjetas de Materia
*Objetivo: Visualizar el desempeño global del estudiante mediante tarjetas temáticas.*

1. **Diseño del Home (`activity_home.xml`):**
   * **Hero Card Superior (Resumen):** Tarjeta destacada verde esmeralda/teal profundo (`#115E59`) mostrando el *Promedio Ponderado General* en tipografía grande (ej: `4.25 / 5.0`) y contador de asignaturas matriculadas.
   * **Listado de Materias (`RecyclerView`):** Configurado con espaciado vertical y márgenes respirables.
   * **Botón de Acción:** `ExtendedFloatingActionButton` inferior con icono y texto `+ Nueva Materia`.
2. **Diseño de Tarjeta de Materia (`item_materia.xml`):**
   * Contenedor `MaterialCardView` con radio de 16dp y borde tenue.
   * Iconografía o cápsula de color identificador por materia escolar (ej: icono de compás/números para matemáticas, libro para historia).
   * **Chip Badge de Calificación:** Contenedor redondeado con el promedio parcial de la materia coloreado dinámicamente (verde para aprobatorio, rojo para bajo).
3. **Conexión de Datos:**
   * Carga asíncrona mediante `lifecycleScope.launch(Dispatchers.IO)` actualizando el adaptador en el hilo principal.

---

### 📝 Fase 5: Detalle de Asignatura y Registro de Calificaciones
*Objetivo: Control del avance porcentual y registro de notas individuales sin superar el 100%.*

1. **Pantalla de Detalle (`DetalleMateriaActivity`):**
   * Encabezado con el nombre de la materia escolar seleccionada (ej: *Matemáticas - Prof. García*).
   * **Barra de Avance Ponderado:** `LinearProgressIndicator` que muestra el porcentaje acumulado calificado (ej: `70% / 100% Evaluado`), con color turquesa.
   * **Listado de Evaluaciones:** `RecyclerView` con `item_nota.xml` (nombre de la actividad escolar, porcentaje individual y nota obtenida).
2. **Formulario de Registro (`AddNotaActivity`):**
   * Campos estructurados:
     * *Nombre de la prueba:* Hint descriptivo (ej: *Examen Final*, *Taller Grupal*).
     * *Puntaje obtenido:* Restricción numérica con ayuda visual (`0.0 - 5.0`).
     * *Porcentaje:* Con sufijo `%` integrado.
   * Botón principal de guardado con esquinas suaves y estilo sólido.
3. **Lógica de Validación Ponderada:**
   * Antes de almacenar en Room, verificar que `porcentajeActual + nuevoPorcentaje <= 100.0`. Si supera el tope, notificar error claro en el `TextInputLayout`.

---

### 🧪 Fase 6: Pruebas Locales, Depuración y Estabilidad
*Objetivo: Asegurar funcionamiento fluido sin cierres inesperados.*

1. **Ciclo de Vida y Rotación:** Verificar que al girar la pantalla en cualquier Activity la información persista y `ViewBinding` no genere llamadas sobre vistas recicladas.
2. **Seguridad de Hilos:** Ejecución rigurosa de todas las consultas a Room en `Dispatchers.IO` para prevenir `RoomCannotAccessDatabaseOnMainThreadException`.
3. **Limpieza de Esquema:** En caso de alterar las entidades de Room, realizar `Build > Clean Project` y `Rebuild Project` para recompilar las clases generadas por KSP.
