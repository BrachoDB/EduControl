package com.aprendiz.educontrol.data.seeder

import android.content.Context
import com.aprendiz.educontrol.data.AppDatabase
import com.aprendiz.educontrol.data.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DemoDataSeeder {

    suspend fun seedDatabaseIfNeeded(context: Context) {
        withContext(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(context)
            
            // Check if user table has demo users
            if (db.userDao().getUserCount() > 0) {
                return@withContext
            }

            // 1. Seed Users (5 Students, 2 Teachers)
            val students = listOf(
                UserEntity(id = 1L, nombre = "Santiago", email = "santiago@educontrol.com", rol = UserEntity.ROLE_STUDENT, avatarEmoji = "👨‍🎓"),
                UserEntity(id = 2L, nombre = "Laura", email = "laura@educontrol.com", rol = UserEntity.ROLE_STUDENT, avatarEmoji = "👩‍🎓"),
                UserEntity(id = 3L, nombre = "Carlos", email = "carlos@educontrol.com", rol = UserEntity.ROLE_STUDENT, avatarEmoji = "👨‍🎓"),
                UserEntity(id = 4L, nombre = "Valentina", email = "valentina@educontrol.com", rol = UserEntity.ROLE_STUDENT, avatarEmoji = "👩‍🎓"),
                UserEntity(id = 5L, nombre = "Mateo", email = "mateo@educontrol.com", rol = UserEntity.ROLE_STUDENT, avatarEmoji = "👨‍🎓")
            )

            val teachers = listOf(
                UserEntity(id = 101L, nombre = "Prof. Andrés", email = "andres@educontrol.com", rol = UserEntity.ROLE_TEACHER, avatarEmoji = "👨‍🏫"),
                UserEntity(id = 102L, nombre = "Prof. Carolina", email = "carolina@educontrol.com", rol = UserEntity.ROLE_TEACHER, avatarEmoji = "👩‍🏫")
            )

            db.userDao().insertUsers(students)
            db.userDao().insertUsers(teachers)

            // 2. Seed Classes
            val clases = listOf(
                ClaseEntity(id = 1L, teacherId = 101L, nombreClase = "Matemáticas 10°", codigoClase = "MAT10", colorTheme = ClaseEntity.THEME_TEAL),
                ClaseEntity(id = 2L, teacherId = 101L, nombreClase = "Física 10°", codigoClase = "FIS10", colorTheme = ClaseEntity.THEME_BLUE),
                ClaseEntity(id = 3L, teacherId = 102L, nombreClase = "Inglés 10°", codigoClase = "ING10", colorTheme = ClaseEntity.THEME_PURPLE),
                ClaseEntity(id = 4L, teacherId = 102L, nombreClase = "Química 10°", codigoClase = "QUI10", colorTheme = ClaseEntity.THEME_ORANGE)
            )
            db.claseDao().insertClases(clases)

            // 3. Seed Enrollments (all students in all 4 classes)
            val inscripciones = mutableListOf<InscripcionEntity>()
            var inscripcionId = 1L
            for (student in students) {
                for (clase in clases) {
                    inscripciones.add(InscripcionEntity(id = inscripcionId++, studentId = student.id, claseId = clase.id))
                }
            }
            db.inscripcionDao().insertInscripciones(inscripciones)

            // 4. Seed Activities for Matemáticas 10° (Class 1)
            val actividadesMatematicas = listOf(
                ActividadEntity(id = 1L, claseId = 1L, titulo = "Taller #1: Funciones Linear", descripcion = "Resolver ejercicios de dominio y rango.", tipo = ActividadEntity.TYPE_TASK, porcentaje = 10.0, fechaEntrega = "15 Ago", mesTimeline = "AGOSTO"),
                ActividadEntity(id = 2L, claseId = 1L, titulo = "Quiz #1: Evaluación Rápida", descripcion = "Cuestionario de 2 preguntas sobre cuadráticas.", tipo = ActividadEntity.TYPE_QUIZ, porcentaje = 10.0, fechaEntrega = "28 Ago", mesTimeline = "AGOSTO"),
                ActividadEntity(id = 3L, claseId = 1L, titulo = "Taller #2: Gráficas y Pendientes", descripcion = "Gráficas de funciones racionales.", tipo = ActividadEntity.TYPE_TASK, porcentaje = 15.0, fechaEntrega = "10 Sep", mesTimeline = "SEPTIEMBRE"),
                ActividadEntity(id = 4L, claseId = 1L, titulo = "Parcial #1: Examen Parcial", descripcion = "Evaluación acumulativa del primer corte.", tipo = ActividadEntity.TYPE_EXAM, porcentaje = 25.0, fechaEntrega = "22 Sep", mesTimeline = "SEPTIEMBRE"),
                ActividadEntity(id = 5L, claseId = 1L, titulo = "Proyecto Final de Geometría", descripcion = "Modelado 3D de cuerpos geométricos.", tipo = ActividadEntity.TYPE_TASK, porcentaje = 15.0, fechaEntrega = "12 Oct", mesTimeline = "OCTUBRE"),
                ActividadEntity(id = 6L, claseId = 1L, titulo = "Examen Final Global", descripcion = "Evaluación global de fin de periodo.", tipo = ActividadEntity.TYPE_EXAM, porcentaje = 25.0, fechaEntrega = "28 Oct", mesTimeline = "OCTUBRE")
            )
            db.actividadDao().insertActividades(actividadesMatematicas)

            // 5. Seed Submissions & Grades (Creating the 5 Student Scenarios)
            val entregas = mutableListOf<EntregaEntity>()
            val calificaciones = mutableListOf<CalificacionEntity>()
            val legacyNotas = mutableListOf<NotaEntity>()

            var entregaId = 1L
            var calificacionId = 1L

            // Scenario 1: Santiago (High Performance)
            val santiagoGrades = mapOf(1L to 4.8, 2L to 4.5, 3L to 4.2, 4L to 4.5, 5L to 4.0)
            for ((actId, notaVal) in santiagoGrades) {
                val act = actividadesMatematicas.first { it.id == actId }
                entregas.add(EntregaEntity(id = entregaId, actividadId = actId, studentId = 1L, contenidoRespuesta = "Respuesta de Santiago", fechaEntrega = act.fechaEntrega, estado = EntregaEntity.STATUS_GRADED))
                calificaciones.add(CalificacionEntity(id = calificacionId++, entregaId = entregaId, nota = notaVal, retroalimentacion = "Excelente desempeño y solución bien estructurada.", fechaCalificacion = act.fechaEntrega))
                entregaId++
            }

            // Scenario 2: Laura (Medium Performance)
            val lauraGrades = mapOf(1L to 3.8, 2L to 3.2, 3L to 3.5, 4L to 3.4)
            for ((actId, notaVal) in lauraGrades) {
                val act = actividadesMatematicas.first { it.id == actId }
                entregas.add(EntregaEntity(id = entregaId, actividadId = actId, studentId = 2L, contenidoRespuesta = "Respuesta de Laura", fechaEntrega = act.fechaEntrega, estado = EntregaEntity.STATUS_GRADED))
                calificaciones.add(CalificacionEntity(id = calificacionId++, entregaId = entregaId, nota = notaVal, retroalimentacion = "Buen trabajo, revisar procedimientos en el parcial.", fechaCalificacion = act.fechaEntrega))
                entregaId++
            }

            // Scenario 3: Carlos (At Risk)
            val carlosGrades = mapOf(1L to 3.0, 2L to 2.0, 3L to 2.5, 4L to 2.8)
            for ((actId, notaVal) in carlosGrades) {
                val act = actividadesMatematicas.first { it.id == actId }
                entregas.add(EntregaEntity(id = entregaId, actividadId = actId, studentId = 3L, contenidoRespuesta = "Respuesta de Carlos", fechaEntrega = act.fechaEntrega, estado = EntregaEntity.STATUS_GRADED))
                calificaciones.add(CalificacionEntity(id = calificacionId++, entregaId = entregaId, nota = notaVal, retroalimentacion = "Atención: requiere reforzar bases de funciones.", fechaCalificacion = act.fechaEntrega))
                entregaId++
            }
            // Carlos has Act 5 PENDING
            entregas.add(EntregaEntity(id = entregaId++, actividadId = 5L, studentId = 3L, contenidoRespuesta = null, fechaEntrega = "12 Oct", estado = EntregaEntity.STATUS_PENDING))

            // Scenario 4: Valentina (Achievable Goal)
            val valentinaGrades = mapOf(1L to 3.5, 2L to 3.0, 3L to 3.2, 4L to 3.6)
            for ((actId, notaVal) in valentinaGrades) {
                val act = actividadesMatematicas.first { it.id == actId }
                entregas.add(EntregaEntity(id = entregaId, actividadId = actId, studentId = 4L, contenidoRespuesta = "Respuesta de Valentina", fechaEntrega = act.fechaEntrega, estado = EntregaEntity.STATUS_GRADED))
                calificaciones.add(CalificacionEntity(id = calificacionId++, entregaId = entregaId, nota = notaVal, retroalimentacion = "Rendimiento constante. Meta alcanzable.", fechaCalificacion = act.fechaEntrega))
                entregaId++
            }

            // Scenario 5: Mateo (Impossible Goal)
            val mateoGrades = mapOf(1L to 2.5, 2L to 2.2, 3L to 2.8, 4L to 2.5, 5L to 3.0)
            for ((actId, notaVal) in mateoGrades) {
                val act = actividadesMatematicas.first { it.id == actId }
                entregas.add(EntregaEntity(id = entregaId, actividadId = actId, studentId = 5L, contenidoRespuesta = "Respuesta de Mateo", fechaEntrega = act.fechaEntrega, estado = EntregaEntity.STATUS_GRADED))
                calificaciones.add(CalificacionEntity(id = calificacionId++, entregaId = entregaId, nota = notaVal, retroalimentacion = "Dificultades matemáticas acumuladas.", fechaCalificacion = act.fechaEntrega))
                entregaId++
            }

            db.entregaDao().insertEntregas(entregas)
            db.calificacionDao().insertCalificaciones(calificaciones)

            // 6. Seed Quiz Questions for Act 2 (Quiz #1)
            val preguntasQuiz = listOf(
                PreguntaQuizEntity(
                    id = 1L,
                    actividadId = 2L,
                    enunciado = "¿Cuál es el dominio de una función cuadrática f(x) = x² + 2x + 1?",
                    opcionA = "Todos los números reales (ℝ)",
                    opcionB = "Solo enteros positivos",
                    opcionC = "Números mayores a cero",
                    opcionD = "Solo enteros negativos",
                    opcionCorrecta = 0
                ),
                PreguntaQuizEntity(
                    id = 2L,
                    actividadId = 2L,
                    enunciado = "¿Cuál es la pendiente de la recta y = 3x - 5?",
                    opcionA = "-5",
                    opcionB = "3",
                    opcionC = "5",
                    opcionD = "0",
                    opcionCorrecta = 1
                )
            )
            db.preguntaQuizDao().insertPreguntas(preguntasQuiz)

            // 7. Seed Legacy Tables (`materias` and `notas`) for Backwards Compatibility
            for (student in students) {
                for (clase in clases) {
                    val legacyMateria = MateriaEntity(
                        id = (student.id * 10 + clase.id),
                        userId = student.id,
                        nombreMateria = clase.nombreClase,
                        profesor = if (clase.teacherId == 101L) "Prof. Andrés" else "Prof. Carolina"
                    )
                    db.materiaDao().insertMateria(legacyMateria)

                    // Seed corresponding notes for class 1
                    if (clase.id == 1L) {
                        val gradesMap = when (student.id) {
                            1L -> santiagoGrades
                            2L -> lauraGrades
                            3L -> carlosGrades
                            4L -> valentinaGrades
                            5L -> mateoGrades
                            else -> emptyMap()
                        }
                        for ((actId, notaVal) in gradesMap) {
                            val act = actividadesMatematicas.first { it.id == actId }
                            val legacyNota = NotaEntity(
                                materiaId = legacyMateria.id,
                                nombreEvaluacion = act.titulo,
                                calificacion = notaVal,
                                porcentaje = act.porcentaje
                            )
                            db.notaDao().insertNota(legacyNota)
                        }
                    }
                }
            }
        }
    }
}
