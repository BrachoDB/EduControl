# 📚 EduControl — Plataforma Académica Móvil Nativa

![Android Studio](https://img.shields.io/badge/Android%20Studio-2026.1+-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Room](https://img.shields.io/badge/Jetpack%20Room-2.6.1-4285F4?style=for-the-badge&logo=sqlite&logoColor=white)
![Material Design 3](https://img.shields.io/badge/Material%20Design-3-757575?style=for-the-badge&logo=materialdesign&logoColor=white)

**EduControl** es una plataforma académica móvil nativa desarrollada en Kotlin y Android Studio. Permite a **Estudiantes** y **Profesores** gestionar la vida escolar de forma integrada mediante dashboards modernos en Material Design 3, líneas de tiempo académicas por mes, cuestionarios autocorregibles en vivo, libretas de calificaciones y un **Motor de Proyección Académica** ("¿Qué nota necesito?") respaldado por pruebas unitarias automatizadas.

---

## 🚀 Acceso Simplificado para Demostración

Para facilitar presentaciones y demostraciones académicas, **EduControl elimina el login tradicional con correo y contraseña**.

```text
                     ◈
                 EDUCONTROL
          Tu progreso académico,
            en un solo lugar.

     ┌──────────────────────────────┐
     │      👨‍🎓 ESTUDIANTE          │
     │   Explorar como estudiante   │
     └──────────────────────────────┘

     ┌──────────────────────────────┐
     │       👨‍🏫 PROFESOR           │
     │   Explorar como profesor     │
     └──────────────────────────────┘
```

1. **Pantalla Inicial (`DemoRoleActivity`):** Permite seleccionar el rol **👨‍🎓 ESTUDIANTE** o **👨‍🏫 PROFESOR**.
2. **Modal Desplegable de Cuentas Demo (`DemoAccountBottomSheetFragment`):** Al tocar un rol, despliega una lista de cuentas precargadas en un `BottomSheetDialogFragment`. Al hacer clic en un usuario, inicia sesión de inmediato sin requerir credenciales manuales.

---

## 📊 Escenarios Demo Precargados (`DemoDataSeeder`)

El componente `DemoDataSeeder.kt` puebla automáticamente la base de datos local SQLite/Room con escenarios diseñados para probar las diferentes situaciones del sistema:

### 👨‍🎓 Cuentas de Estudiantes (5)
* 🌟 **Santiago:** Alto rendimiento (Promedio acumulado `4.38`, `75%` evaluado).
* 📈 **Laura:** Rendimiento medio constante (Promedio acumulado `3.46`, `60%` evaluado).
* ⚠️ **Carlos:** En riesgo académico (Promedio acumulado `2.63`, tareas pendientes).
* 🎯 **Valentina:** Meta alcanzable (Promedio `3.38`, objetivo `4.0` exige $4.93$ en $40\%$ restante $\rightarrow$ `🟡 Exigente`).
* 🔒 **Mateo:** Meta imposible (Promedio `2.62`, objetivo `4.5` exige $10.14 > 5.0$ $\rightarrow$ `🔴 Imposible`, Nota máxima posible $3.22$).

### 👨‍🏫 Cuentas de Profesores (2)
* 👨‍🏫 **Prof. Andrés:** Docente de *Matemáticas 10°* (`MAT10`) y *Física 10°* (`FIS10`).
* 👩‍🏫 **Prof. Carolina:** Docente de *Inglés 10°* (`ING10`) y *Química 10°* (`QUI10`).

---

## 🔥 Funcionalidades Clave por Rol

### 👨‍🎓 Módulo del Estudiante
* **Dashboard Principal (`StudentHomeFragment`):**
  * **Hero Card M3 (`navy_hero`):** Saludo personalizado (*"Buenos días, Santiago 👋"*), Promedio General en pantalla grande (ej: `4.38 / 5.0`) y avance del periodo.
  * **Alertas Académicas Dinámicas:** Notificaciones automáticas de asignaturas en riesgo o entregas pendientes.
  * **Carrusel "Próximas Entregas":** Lista horizontal de actividades pendientes con sus fechas y pesos porcentuales.
  * **Mis Materias:** Tarjetas con barra de progreso (`LinearProgressIndicator`) y badges de nota.
* **Detalle de Clase y Timeline Académica (`DetalleMateriaActivity`):**
  * **Línea de Tiempo Visual:** Nodos interconectados con íconos por tipo de actividad (📝 Taller, 🧪 Quiz, 📚 Examen) agrupados por mes (Agosto, Septiembre, Octubre).
  * **Calificaciones y Retroalimentación:** Visualización de notas con la retroalimentación escrita por el docente.
* **Motor de Proyección Académica ("¿Qué nota necesito?") (`StudentGoalsFragment`):**
  * **Cálculo Inverso en Tiempo Real:** Entrada para Nota Objetivo con cálculo instantáneo.
  * **Escenarios de Simulación:** Tendencia si mantiene su ritmo, Nota Máxima Posible y Nota Necesaria Requerida.
  * **Badges de Estado:** `🟢 Alcanzable`, `🟡 Exigente`, `🔴 Imposible`, `🎯 Meta Alcanzada`, `🔒 Curso Cerrado`.
* **Actividades Interactivas y Quizzes Autocorregibles (`ActivityDetailActivity`):**
  * **Envío de Tareas:** Formulario de respuesta de texto multilínea persistido en Room.
  * **Quizzes Interactivas:** Cuestionario de opción múltiple generado dinámicamente. Al presionar *"Finalizar Quiz e Autocorregir"*, calcula la calificación automáticamente ($\text{Nota} = 5.0 \times \text{Correctas} / \text{Total}$), muestra un diálogo con el resultado y actualiza el promedio en Room.

### 👨‍🏫 Módulo del Profesor
* **Dashboard del Docente (`TeacherHomeFragment`):**
  * **Hero Card Docente:** Saludo (*"Buenos días, Prof. Andrés 👋"*), métricas globales (`📚 2 Clases`, `👥 5 Alumnos`, `📝 6 Tareas`) y banner de entregas pendientes por revisar.
  * **Mis Clases:** Tarjetas con código de materia, número de alumnos inscritos y promedio del grupo.
* **Detalle de Clase y Libreta de Calificaciones (`TeacherClassDetailActivity`):**
  * **Lista de Alumnos Matriculados:** Tarjetas con promedios individuales y badges de rendimiento.
  * **Libreta de Calificaciones por Actividad:** Desplegable de tareas/quizzes y diálogo flotante `AlertDialog` para modificar notas ($0.0 - 5.0$) y comentarios en vivo.
* **Perfil Académico 360° del Estudiante (`TeacherStudentProfileActivity`):**
  * **Vista 360° del Alumno:** Muestra el promedio global del estudiante en todas las materias dictadas por el profesor y un desglose detallado por asignatura y actividades evaluadas.

---

## 🛠️ Arquitectura y Estructura del Proyecto

El proyecto sigue una arquitectura limpia estructurada en capas de **Presentación (UI)**, **Dominio (Domain)** y **Persistencia de Datos (Data)**:

```
com.aprendiz.educontrol
│
├── data/                             # Capa de Persistencia Local (Room)
│   ├── AppDatabase.kt                # Database SQLite v2 (fallbackToDestructiveMigration)
│   ├── seeder/
│   │   └── DemoDataSeeder.kt         # Puebla la BD con los 5 estudiantes, 2 profesores, clases y notas
│   ├── session/
│   │   └── SessionManager.kt         # Mantiene la sesión activa en SharedPreferences
│   ├── repository/
│   │   ├── StudentRepository.kt      # Consultas y lógica de negocio para el estudiante
│   │   └── TeacherRepository.kt      # Consultas y libreta de calificaciones para el docente
│   ├── dao/                          # Interfaces Data Access Object (8 DAOs)
│   └── entity/                       # Modelos de tablas relacionales (8 Entidades)
│
├── domain/
│   └── calculator/
│       └── ProjectionCalculator.kt   # Lógica pura del Motor de Proyección de Calificaciones
│
└── ui/                               # Capa de Interfaz de Usuario
    ├── auth/                         # DemoRoleActivity & DemoAccountBottomSheetFragment
    ├── student/                      # StudentMainActivity & Fragments (Home, Classes, Activities, Goals, Profile)
    ├── teacher/                      # TeacherMainActivity & Fragments (Home, Classes, Performance, Profile)
    └── materia/                      # DetalleMateriaActivity (Timeline & Simulador)
```

---

## 🗄️ Modelo de Base de Datos Relacional (8 Tablas Room)

```
+--------------------+        +--------------------+        +--------------------+
|    users (Room)    |        |       clases       |        |   inscripciones    |
+--------------------+        +--------------------+        +--------------------+
| id (PK)            |        | id (PK)            |        | id (PK)            |
| nombre             |        | teacherId (FK)     |<-------| studentId (FK)     |
| email              |<-------| nombreClase        |        | claseId (FK)       |
| rol                |        | codigoClase        |        +--------------------+
| avatarEmoji        |        | colorTheme         |
+--------------------+        +--------------------+
                                        |
                                        v
+--------------------+        +--------------------+        +--------------------+
|     entregas       |        |    actividades     |        |   preguntas_quiz   |
+--------------------+        +--------------------+        +--------------------+
| id (PK)            |        | id (PK)            |        | id (PK)            |
| actividadId (FK)   |<-------| claseId (FK)       |------->| actividadId (FK)   |
| studentId (FK)     |        | titulo             |        | enunciado          |
| contenidoRespuesta |        | tipo               |        | opcionCorrecta     |
| estado             |        | porcentaje         |        +--------------------+
+--------------------+        | mesTimeline        |
          |                   +--------------------+
          v
+--------------------+        +--------------------+
|   calificaciones   |        |  respuestas_quiz   |
+--------------------+        +--------------------+
| id (PK)            |        | id (PK)            |
| entregaId (FK)     |        | entregaId (FK)     |
| nota               |        | preguntaId (FK)    |
| retroalimentacion  |        | esCorrecta         |
+--------------------+        +--------------------+
```

---

## 🎨 Paleta de Colores Material Design 3

| Elemento / Rol | Código Hexadecimal | Descripción Visual |
| :--- | :---: | :--- |
| **Navy Hero** | `#0F172A` | Fondo principal de cabeceras M3, selector de acceso y tarjetas destacadas |
| **Teal Dark / Accent** | `#0F766E` | Color turquesa de acento para la experiencia del docente |
| **Primary Emerald** | `#0D9488` | Verde esmeralda principal para acciones del estudiante |
| **Surface Background** | `#F8FAFC` | Fondo claro suave para reducir la fatiga visual |
| **Badge Aprobado (BG / Text)** | `#DCFCE7` / `#15803D` | Fondo y texto verde para calificaciones o metas alcanzables |
| **Badge En Riesgo (BG / Text)** | `#FEE2E2` / `#B91C1C` | Fondo y texto rojo para calificaciones bajas o metas imposibles |
| **Badge Pendiente (BG / Text)** | `#FEF3C7` / `#B45309` | Fondo y texto amarillo para entregas pendientes o metas exigentes |
| **Badge Completado (BG / Text)** | `#E0F2FE` / `#0369A1` | Fondo y texto azul para actividades entregadas o evaluadas |

---

## 🧪 Suite de Pruebas Unitarias Automatizadas (JUnit 4)

El proyecto cuenta con **19 pruebas unitarias automatizadas** que garantizan la precisión matemática del sistema:

```bash
./gradlew test
```

* **`EduControlTestSuite.kt`:** Prueba los cálculos del promedio ponderado acumulado de Santiago y Carlos, así como las proyecciones de Valentina y Mateo.
* **`ProjectionCalculatorTest.kt`:** Prueba las fórmulas puras del motor de proyecciones (metas alcanzables, exigentes, imposibles $> 5.0$ y materias cerradas al $100\%$).
* **`QuizAutoGradingTest.kt`:** Prueba la autocalificación de quizzes ($2/2 \rightarrow 5.0$, $1/2 \rightarrow 2.5$, $0/2 \rightarrow 0.0$).
* **`MateriaNombreTest.kt`:** Prueba el soporte de caracteres hispanos ('ñ', 'Ñ', acentos) en la capa de persistencia.

---

## ⚙️ Instrucciones para Demostraciones y Presentaciones

1. **Clonar el repositorio y abrir en Android Studio:**
   ```bash
   git clone <URL_REPOSITORIO>
   cd EduControl
   ```
2. **Ejecutar Pruebas Unitarias:**
   ```bash
   ./gradlew test
   ```
3. **Ejecutar la App en Emulador o Dispositivo:**
   * Hacer clic en **Run** (`Shift + F10`).
4. **Flujo Recomendado para la Demo:**
   * **Demo Estudiante:** Toca **ESTUDIANTE** $\rightarrow$ Selecciona **Santiago** (Alto rendimiento) $\rightarrow$ Revisa su Dashboard con promedio `4.38` $\rightarrow$ Entra a *Matemáticas 10°* y explora la Timeline por meses (Agosto, Septiembre, Octubre).
   * **Demo Simulador de Metas:** Ve a la pestaña **Metas** $\rightarrow$ Selecciona **Valentina** o **Mateo** para mostrar cómo la app calcula que $10.14 > 5.0$ es una meta *imposible* o $4.93$ es *exigente*.
   * **Demo Quiz Autocorregible:** Ve a la pestaña **Actividades** $\rightarrow$ Abre el *Quiz #1* $\rightarrow$ Responde las preguntas y presiona *"Finalizar Quiz e Autocorregir"* para ver el resultado $5.00$ instantáneo.
   * **Demo Profesor:** Ve a Perfil $\rightarrow$ Cambiar Usuario $\rightarrow$ Toca **PROFESOR** $\rightarrow$ Selecciona **Prof. Andrés** $\rightarrow$ Explora sus clases y abre la Libreta de Calificaciones para calificar a un alumno.

---

## 📝 Licencia

Desarrollado como proyecto académico de plataforma educativa nativa en Android.
