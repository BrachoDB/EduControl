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

            // 2. Seed Classes (Exactly 2 Classes - 1 per Teacher)
            val clases = listOf(
                ClaseEntity(id = 1L, teacherId = 101L, nombreClase = "Matemáticas 10°", codigoClase = "MAT10", colorTheme = ClaseEntity.THEME_TEAL),
                ClaseEntity(id = 2L, teacherId = 102L, nombreClase = "Inglés 10°", codigoClase = "ING10", colorTheme = ClaseEntity.THEME_PURPLE)
            )
            db.claseDao().insertClases(clases)

            // 3. Seed Enrollments (All 5 students in both classes)
            val inscripciones = mutableListOf<InscripcionEntity>()
            var inscripcionId = 1L
            for (student in students) {
                for (clase in clases) {
                    inscripciones.add(InscripcionEntity(id = inscripcionId++, studentId = student.id, claseId = clase.id))
                }
            }
            db.inscripcionDao().insertInscripciones(inscripciones)

            // 4. Seed Activities for Class 1 (Matemáticas 10°) & Class 2 (Inglés 10°)
            val actividadesMatematicas = listOf(
                ActividadEntity(id = 1L, claseId = 1L, titulo = "Taller #1: Funciones Lineales", descripcion = "Resolver ejercicios de dominio y rango.", tipo = ActividadEntity.TYPE_TASK, porcentaje = 10.0, fechaEntrega = "15 Ago", mesTimeline = "AGOSTO"),
                ActividadEntity(id = 2L, claseId = 1L, titulo = "Quiz #1: Evaluación Rápida", descripcion = "Cuestionario de 2 preguntas sobre cuadráticas.", tipo = ActividadEntity.TYPE_QUIZ, porcentaje = 10.0, fechaEntrega = "28 Ago", mesTimeline = "AGOSTO"),
                ActividadEntity(id = 3L, claseId = 1L, titulo = "Taller #2: Gráficas y Pendientes", descripcion = "Gráficas de funciones racionales.", tipo = ActividadEntity.TYPE_TASK, porcentaje = 15.0, fechaEntrega = "10 Sep", mesTimeline = "SEPTIEMBRE"),
                ActividadEntity(id = 4L, claseId = 1L, titulo = "Parcial #1: Examen Parcial", descripcion = "Evaluación acumulativa del primer corte.", tipo = ActividadEntity.TYPE_EXAM, porcentaje = 25.0, fechaEntrega = "22 Sep", mesTimeline = "SEPTIEMBRE"),
                ActividadEntity(id = 5L, claseId = 1L, titulo = "Proyecto Final de Geometría", descripcion = "Modelado 3D de cuerpos geométricos.", tipo = ActividadEntity.TYPE_TASK, porcentaje = 15.0, fechaEntrega = "12 Oct", mesTimeline = "OCTUBRE"),
                ActividadEntity(id = 6L, claseId = 1L, titulo = "Examen Final Global", descripcion = "Evaluación global de fin de periodo.", tipo = ActividadEntity.TYPE_EXAM, porcentaje = 25.0, fechaEntrega = "28 Oct", mesTimeline = "OCTUBRE")
            )

            val actividadesIngles = listOf(
                ActividadEntity(id = 7L, claseId = 2L, titulo = "Taller #1: Reading & Grammar", descripcion = "Lectura comprensiva e identificación de tiempos verbales.", tipo = ActividadEntity.TYPE_TASK, porcentaje = 15.0, fechaEntrega = "18 Ago", mesTimeline = "AGOSTO"),
                ActividadEntity(id = 8L, claseId = 2L, titulo = "Quiz #1: Verb Tenses & Vocabulary", descripcion = "Quiz corto sobre verbos irregulares y conectores.", tipo = ActividadEntity.TYPE_QUIZ, porcentaje = 15.0, fechaEntrega = "05 Sep", mesTimeline = "SEPTIEMBRE"),
                ActividadEntity(id = 9L, claseId = 2L, titulo = "Listening & Writing Exam", descripcion = "Evaluación de escucha y ensayo argumentativo.", tipo = ActividadEntity.TYPE_EXAM, porcentaje = 30.0, fechaEntrega = "25 Sep", mesTimeline = "SEPTIEMBRE")
            )

            db.actividadDao().insertActividades(actividadesMatematicas + actividadesIngles)

            // 5. Seed Submissions & Grades for All Students across Class 1 & Class 2
            val entregas = mutableListOf<EntregaEntity>()
            val calificaciones = mutableListOf<CalificacionEntity>()

            var entregaId = 1L
            var calificacionId = 1L

            // --- CLASS 1 (Matemáticas 10°) ---
            val santiagoGradesMat = mapOf(1L to 4.8, 2L to 4.5, 3L to 4.2, 4L to 4.5, 5L to 4.0)
            val lauraGradesMat = mapOf(1L to 3.8, 2L to 3.2, 3L to 3.5, 4L to 3.4)
            val carlosGradesMat = mapOf(1L to 3.0, 2L to 2.0, 3L to 2.5, 4L to 2.8)
            val valentinaGradesMat = mapOf(1L to 3.5, 2L to 3.0, 3L to 3.2, 4L to 3.6)
            val mateoGradesMat = mapOf(1L to 2.5, 2L to 2.2, 3L to 2.8, 4L to 2.5, 5L to 3.0)

            fun seedStudentClass1(studentId: Long, name: String, grades: Map<Long, Double>) {
                for ((actId, notaVal) in grades) {
                    val act = actividadesMatematicas.first { it.id == actId }
                    entregas.add(EntregaEntity(id = entregaId, actividadId = actId, studentId = studentId, contenidoRespuesta = "Respuesta de $name", fechaEntrega = act.fechaEntrega, estado = EntregaEntity.STATUS_GRADED))
                    calificaciones.add(CalificacionEntity(id = calificacionId++, entregaId = entregaId, nota = notaVal, retroalimentacion = "Buen trabajo en Matemáticas.", fechaCalificacion = act.fechaEntrega))
                    entregaId++
                }
            }

            seedStudentClass1(1L, "Santiago", santiagoGradesMat)
            seedStudentClass1(2L, "Laura", lauraGradesMat)
            seedStudentClass1(3L, "Carlos", carlosGradesMat)
            // Carlos Act 5 Pending
            entregas.add(EntregaEntity(id = entregaId++, actividadId = 5L, studentId = 3L, contenidoRespuesta = null, fechaEntrega = "12 Oct", estado = EntregaEntity.STATUS_PENDING))
            seedStudentClass1(4L, "Valentina", valentinaGradesMat)
            seedStudentClass1(5L, "Mateo", mateoGradesMat)

            // --- CLASS 2 (Inglés 10° - Prof. Carolina) ---
            val santiagoGradesIng = mapOf(7L to 4.6, 8L to 4.4, 9L to 4.5)
            val lauraGradesIng = mapOf(7L to 3.6, 8L to 3.5, 9L to 3.7)
            val carlosGradesIng = mapOf(7L to 2.6, 8L to 2.8)
            val valentinaGradesIng = mapOf(7L to 3.8, 8L to 3.4, 9L to 3.6)
            val mateoGradesIng = mapOf(7L to 2.8, 8L to 2.5, 9L to 2.6)

            fun seedStudentClass2(studentId: Long, name: String, grades: Map<Long, Double>) {
                for ((actId, notaVal) in grades) {
                    val act = actividadesIngles.first { it.id == actId }
                    entregas.add(EntregaEntity(id = entregaId, actividadId = actId, studentId = studentId, contenidoRespuesta = "Essay by $name", fechaEntrega = act.fechaEntrega, estado = EntregaEntity.STATUS_GRADED))
                    calificaciones.add(CalificacionEntity(id = calificacionId++, entregaId = entregaId, nota = notaVal, retroalimentacion = "Good effort in English class.", fechaCalificacion = act.fechaEntrega))
                    entregaId++
                }
            }

            seedStudentClass2(1L, "Santiago", santiagoGradesIng)
            seedStudentClass2(2L, "Laura", lauraGradesIng)
            seedStudentClass2(3L, "Carlos", carlosGradesIng)
            // Carlos Act 9 Pending for English
            entregas.add(EntregaEntity(id = entregaId++, actividadId = 9L, studentId = 3L, contenidoRespuesta = null, fechaEntrega = "25 Sep", estado = EntregaEntity.STATUS_PENDING))
            seedStudentClass2(4L, "Valentina", valentinaGradesIng)
            seedStudentClass2(5L, "Mateo", mateoGradesIng)

            db.entregaDao().insertEntregas(entregas)
            db.calificacionDao().insertCalificaciones(calificaciones)

            // 6. Seed Quiz Questions for Act 2 (Math Quiz) & Act 8 (English Quiz)
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
                ),
                PreguntaQuizEntity(
                    id = 3L,
                    actividadId = 8L,
                    enunciado = "Select the correct past participle of the verb 'To Speak':",
                    opcionA = "Speaked",
                    opcionB = "Spoke",
                    opcionC = "Spoken",
                    opcionD = "Speaking",
                    opcionCorrecta = 2
                ),
                PreguntaQuizEntity(
                    id = 4L,
                    actividadId = 8L,
                    enunciado = "Which connector best expresses contrast?",
                    opcionA = "Furthermore",
                    opcionB = "However",
                    opcionC = "Therefore",
                    opcionD = "In addition",
                    opcionCorrecta = 1
                )
            )
            db.preguntaQuizDao().insertPreguntas(preguntasQuiz)

            // 7. Seed Legacy Tables (`materias` and `notas`)
            for (student in students) {
                for (clase in clases) {
                    val legacyMateria = MateriaEntity(
                        id = (student.id * 10 + clase.id),
                        userId = student.id,
                        nombreMateria = clase.nombreClase,
                        profesor = if (clase.teacherId == 101L) "Prof. Andrés" else "Prof. Carolina"
                    )
                    db.materiaDao().insertMateria(legacyMateria)

                    val gradesMap = when {
                        clase.id == 1L && student.id == 1L -> santiagoGradesMat
                        clase.id == 1L && student.id == 2L -> lauraGradesMat
                        clase.id == 1L && student.id == 3L -> carlosGradesMat
                        clase.id == 1L && student.id == 4L -> valentinaGradesMat
                        clase.id == 1L && student.id == 5L -> mateoGradesMat
                        clase.id == 2L && student.id == 1L -> santiagoGradesIng
                        clase.id == 2L && student.id == 2L -> lauraGradesIng
                        clase.id == 2L && student.id == 3L -> carlosGradesIng
                        clase.id == 2L && student.id == 4L -> valentinaGradesIng
                        clase.id == 2L && student.id == 5L -> mateoGradesIng
                        else -> emptyMap()
                    }

                    val actList = if (clase.id == 1L) actividadesMatematicas else actividadesIngles
                    for ((actId, notaVal) in gradesMap) {
                        val act = actList.first { it.id == actId }
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
