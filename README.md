# 📚 EduControl - Control de Calificaciones Escolares

![Android Studio](https://img.shields.io/badge/Android%20Studio-2026.1+-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Room](https://img.shields.io/badge/Jetpack%20Room-2.6.1-4285F4?style=for-the-badge&logo=sqlite&logoColor=white)
![Material Design 3](https://img.shields.io/badge/Material%20Design-3-757575?style=for-the-badge&logo=materialdesign&logoColor=white)

**EduControl** es una aplicación móvil nativa para Android desarrollada en Kotlin que permite a estudiantes gestionar su historial académico escolar, registrar asignaturas con sus respectivos profesores, calcular automáticamente promedios ponderados en tiempo real y **simular la nota requerida** para lograr una calificación objetivo final.

---

## 🚀 Características Principales

### 🔐 1. Módulo de Autenticación y Usuarios
* **Pantalla de Bienvenida (Splash Screen):** Carga inicial fluida con redirección automática y pre-creación asíncrona de cuenta de pruebas (`test@educontrol.com` / `123456`).
* **Registro de Usuarios:** Creación de nuevas cuentas en la base de datos SQLite local con validación de campos y unicidad de correo electrónico.
* **Inicio de Sesión:** Autenticación local mediante Jetpack Room vinculando la sesión activa (`USER_ID`) a las asignaturas del usuario.

### 🏠 2. Panel Principal / Dashboard Escolar (`HomeActivity`)
* **Hero Card (Tarjeta Destacada):** Muestra de forma visual el **Promedio Ponderado General** (`X.XX / 5.0`) del estudiante y la cantidad total de asignaturas matriculadas.
* **Listado de Materias (`RecyclerView`):** Visualización modular de asignaturas registradas, mostrando el nombre de la materia, nombre del profesor y promedio ponderado acumulado.
* **Soporte UTF-8 e Idioma Español:** Compatibilidad completa con teclados virtuales para el uso de la letra **ñ**, **Ñ** y acentos en los campos de asignaturas y docentes.
* **Indicadores Visuales de Desempeño (Badges de Estado):**
  * 🟢 **Aprobado / Sobresaliente (≥ 3.0):** Badge en verde esmeralda suave (`#DCFCE7`) con texto `#15803D`.
  * 🔴 **En Riesgo / Bajo (< 3.0):** Badge en rojo claro (`#FEE2E2`) con texto `#B91C1C`.
* **Menú Contextual de Gestión:** Opciones mediante `PopupMenu` para:
  * **Editar Asignatura:** Modificar nombre o docente.
  * **Eliminar Asignatura:** Eliminación con modal de confirmación (`AlertDialog`) y borrado en cascada en la base de datos local (`CASCADE`).

### 📝 3. Gestión de Asignaturas y Evaluación Ponderada (`DetalleMateriaActivity`)
* **Métrica en Tiempo Real:** Cálculo automático del promedio parcial ponderado según el porcentaje evaluado hasta el momento.
* **Barra de Avance Ponderado (`LinearProgressIndicator`):** Indicador visual que muestra el acumulado del porcentaje calificado (`X% / 100% Evaluado`).
* **Listado de Evaluaciones:** Tarjetas detalladas de calificaciones mostrando la actividad escolar (ej: *Examen Final*, *Taller Grupal*), el peso porcentual y la nota obtenida (`0.0 - 5.0`).

### 🎯 4. Simulador de Metas ("¿Qué nota necesito?")
Sección interactiva dentro de `DetalleMateriaActivity` en una tarjeta de **Material Design 3** donde el estudiante ingresa su **Nota Objetivo** (ej: `4.0`) y el sistema realiza un cálculo inverso en tiempo real:
* 🧮 **Cálculo Inverso:** Evalúa los puntos ya acumulados y determina la calificación exacta requerida sobre el porcentaje restante de la materia.
* 🚦 **Respuestas Dinámicas e Indicadores de Estado:**
  * 🟢 **Meta Alcanzable ($\le 5.0$):** Informa exactamente la nota necesaria en el porcentaje restante.
  * 🔴 **Meta Difícil (> 5.0):** Alerta de imposibilidad matemática si la nota requerida supera la escala de `5.0`.
  * 🎉 **Meta Alcanzada:** Notifica al usuario que sus notas acumuladas ya garantizan el objetivo deseado.
  * 🔒 **Materia Cerrada (100% evaluado):** Inhabilita la entrada e indica que la nota final ya está fijada.

### ➕ 5. Registro y Validación Estricta de Notas (`AddNotaActivity`)
* **Lógica de Control Porcentual:** Sistema inteligente que previene errores impidiendo registrar notas cuyo porcentaje supere el 100% disponible de la asignatura.
* **Retroalimentación Dinámica:** En caso de exceso, muestra un mensaje de error personalizado indicando exactamente cuánto porcentaje resta por evaluar (ej: *"El porcentaje supera el 100% disponible. Restante: 30%"*).
* **Validación de Rangos:** Restricción estricta de calificaciones entre `0.0` y `5.0` y porcentajes entre `1%` y `100%`.

---

## 🧪 Pruebas Unitarias Automatizadas (`src/test`)

La aplicación incluye un conjunto de pruebas unitarias automatizadas ejecutadas mediante JUnit:
* **`SimuladorMetasTest.kt`:** Prueba los cálculos matemáticos del simulador de metas (cálculos estándar, metas alcanzadas, metas imposibles mayores a 5.0 y materias cerradas al 100%).
* **`MateriaNombreTest.kt`:** Garantiza el soporte de caracteres hispanos ('ñ', 'Ñ', acentos) en la asignación de entidades de materias.

---

## 🛠️ Arquitectura y Stack Tecnológico

La aplicación sigue los principios recomendados por Android Jetpack con una arquitectura limpia dividida en capas de **Presentación** y **Datos**:

```
com.aprendiz.educontrol
│
├── data/                       # Capa de Datos y Persistencia Local
│   ├── AppDatabase.kt          # Singleton de Jetpack Room (educontrol_db)
│   ├── dao/                    # Data Access Objects (Operaciones SQL)
│   │   ├── UserDao.kt          # Consultas y registro de usuarios
│   │   ├── MateriaDao.kt       # CRUD de asignaturas por usuario
│   │   └── NotaDao.kt          # CRUD y sumatoria de notas/porcentajes
│   └── entity/                 # Entidades / Tablas SQLite
│       ├── UserEntity.kt       # Tabla 'users'
│       ├── MateriaEntity.kt    # Tabla 'materias' (FK -> users.id)
│       └── NotaEntity.kt       # Tabla 'notas' (FK -> materias.id)
│
└── ui/                         # Capa de Interfaz de Usuario (ViewBinding)
    ├── splash/                 # SplashActivity (Pantalla de carga inicial)
    ├── auth/                   # LoginActivity & RegisterActivity
    ├── home/                   # HomeActivity, HomeAdapter & AddMateriaActivity
    └── materia/                # DetalleMateriaActivity, NotasAdapter & AddNotaActivity
```

### 🧰 Tecnologías Utilizadas
* **Lenguaje:** Kotlin 1.9+ (Target JVM 21)
* **SDK:** Min SDK 24 (Android 7.0) | Target & Compile SDK 34 (Android 14)
* **UI & Layouts:** XML Layouts, `ConstraintLayout`, `Material Design 3 (M3)`, `ViewBinding`
* **Persistencia Local:** Jetpack Room `2.6.1` con **KSP** (*Kotlin Symbol Processing*)
* **Concurrencia:** Kotlin Coroutines (`lifecycleScope`, `Dispatchers.IO`, `withContext`)
* **Pruebas:** JUnit 4 (`testDebugUnitTest`)

---

## 🗄️ Modelo de Base de Datos (Relacional - Room)

El esquema relacional implementa claves foráneas (`ForeignKey`) con eliminación en cascada (`CASCADE`) e índices optimizados:

```
+------------------+         +-----------------------+         +--------------------------+
|      users       |         |       materias        |         |          notas           |
+------------------+         +-----------------------+         +--------------------------+
| id (PK Auto)     |<-------1| id (PK Auto)          |<-------1| id (PK Auto)             |
| email (String)   |  (FK)   | userId (FK)           |  (FK)   | materiaId (FK)           |
| password (String)|<--------| nombreMateria (String)|<--------| nombreEvaluacion (String)|
+------------------+         | profesor (String)     |         | calificacion (Double)    |
                             +-----------------------+         | porcentaje (Double)      |
                                                               +--------------------------+
```

---

## 🎨 Paleta de Colores y Estilo Visual

Para garantizar una experiencia limpia e intuitiva, se diseñó un tema personalizado en verde esmeralda y turquesa:

| Elemento / Rol | Código Hexadecimal | Descripción Visual |
| :--- | :---: | :--- |
| **Primary Emerald** | `#0D9488` | Color principal para encabezados, acentos y notas aprobatorias |
| **Dark Teal Hero** | `#115E59` | Fondo del contenedor principal de resumen general (Hero Card) |
| **Surface Background** | `#F0FDF4` | Fondo suave claro para reducir la fatiga visual |
| **Badge Aprobado (BG / Text)** | `#DCFCE7` / `#15803D` | Fondo y texto verde para promedios/calificaciones ≥ 3.0 |
| **Badge En Riesgo (BG / Text)** | `#FEE2E2` / `#B91C1C` | Fondo y texto rojo para promedios/calificaciones < 3.0 |
| **Form Button Accent** | `#0F766E` | Color sólido para botones de acción y guardado |

---

## ⚙️ Instrucciones de Instalación y Ejecución

1. **Clonar el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd EduControl
   ```
2. **Abrir en Android Studio:**
   * Abrir Android Studio (Iguana / Jellyfish / 2024.1+ recomendado).
   * Seleccionar la carpeta raíz del proyecto `EduControl`.
3. **Sincronizar Gradle:**
   * Permitir que Gradle descargue las dependencias necesarias (`Room`, `Material Design`, `KSP`).
4. **Ejecutar Pruebas Unitarias:**
   ```bash
   ./gradlew test
   ```
5. **Ejecutar la App:**
   * Seleccionar un emulador o dispositivo físico con **Android 7.0 (API 24)** o superior.
   * Hacer clic en **Run** (`Shift + F10`).
6. **Credenciales de Prueba Rápida:**
   * **Email:** `test@educontrol.com`
   * **Contraseña:** `123456`

---

## 📝 Licencia y Créditos

Desarrollado como proyecto educativo para la gestión académica escolar en Android.
