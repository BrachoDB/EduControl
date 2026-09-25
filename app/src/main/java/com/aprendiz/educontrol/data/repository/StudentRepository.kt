package com.aprendiz.educontrol.data.repository

import android.content.Context
import com.aprendiz.educontrol.data.AppDatabase
import com.aprendiz.educontrol.data.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class StudentClassSummaryModel(
    val claseId: Long,
    val nombreClase: String,
    val profesor: String,
    val promedio: Double,
    val porcentajeEvaluado: Double,
    val colorTheme: String
)

data class StudentUpcomingActivityModel(
    val actividadId: Long,
    val claseId: Long,
    val nombreClase: String,
    val titulo: String,
    val tipo: String,
    val fechaEntrega: String,
    val porcentaje: Double
)

data class StudentDashboardData(
    val studentName: String,
    val promedioGeneral: Double,
    val porcentajeEvaluadoGlobal: Double,
    val classes: List<StudentClassSummaryModel>,
    val upcomingActivities: List<StudentUpcomingActivityModel>,
    val alertMessage: String?
)

sealed class TimelineListItem {
    data class MonthHeader(val monthName: String) : TimelineListItem()
    data class ActivityItem(
        val actividadId: Long,
        val titulo: String,
        val descripcion: String,
        val tipo: String,
        val porcentaje: Double,
        val fechaEntrega: String,
        val estado: String,
        val nota: Double?,
        val retroalimentacion: String?
    ) : TimelineListItem()
}

data class StudentClassDetailData(
    val claseId: Long,
    val nombreClase: String,
    val codigoClase: String,
    val profesor: String,
    val promedioActual: Double,
    val porcentajeEvaluado: Double,
    val colorTheme: String,
    val timelineItems: List<TimelineListItem>
)

data class StudentActivityItemModel(
    val actividadId: Long,
    val claseId: Long,
    val nombreClase: String,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val porcentaje: Double,
    val fechaEntrega: String,
    val estado: String,
    val nota: Double?,
    val retroalimentacion: String?,
    val contenidoRespuesta: String? = null
)

class StudentRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)

    suspend fun getStudentDashboardData(studentId: Long): StudentDashboardData = withContext(Dispatchers.IO) {
        val user = db.userDao().getUserById(studentId)
        val studentName = user?.nombre ?: "Estudiante"

        val clases = db.claseDao().getClasesByStudent(studentId)
        val classSummaries = mutableListOf<StudentClassSummaryModel>()
        val upcomingActivities = mutableListOf<StudentUpcomingActivityModel>()

        var sumaPromedios = 0.0
        var sumaPorcentajesEvaluados = 0.0

        for (clase in clases) {
            val teacher = db.userDao().getUserById(clase.teacherId)
            val teacherName = teacher?.nombre ?: "Profesor"
            val actividades = db.actividadDao().getActividadesByClase(clase.id)

            var promedioPonderadoClase = 0.0
            var porcentajeEvaluadoClase = 0.0

            for (act in actividades) {
                val entrega = db.entregaDao().getEntrega(act.id, studentId)
                if (entrega != null) {
                    val calificacion = db.calificacionDao().getCalificacionByEntrega(entrega.id)
                    if (calificacion != null) {
                        promedioPonderadoClase += calificacion.nota * (act.porcentaje / 100.0)
                        porcentajeEvaluadoClase += act.porcentaje
                    } else if (entrega.estado == EntregaEntity.STATUS_PENDING) {
                        upcomingActivities.add(
                            StudentUpcomingActivityModel(
                                actividadId = act.id, claseId = clase.id, nombreClase = clase.nombreClase,
                                titulo = act.titulo, tipo = act.tipo, fechaEntrega = act.fechaEntrega, porcentaje = act.porcentaje
                            )
                        )
                    }
                } else {
                    upcomingActivities.add(
                        StudentUpcomingActivityModel(
                            actividadId = act.id, claseId = clase.id, nombreClase = clase.nombreClase,
                            titulo = act.titulo, tipo = act.tipo, fechaEntrega = act.fechaEntrega, porcentaje = act.porcentaje
                        )
                    )
                }
            }

            classSummaries.add(
                StudentClassSummaryModel(
                    claseId = clase.id, nombreClase = clase.nombreClase, profesor = teacherName,
                    promedio = promedioPonderadoClase, porcentajeEvaluado = porcentajeEvaluadoClase, colorTheme = clase.colorTheme
                )
            )

            sumaPromedios += promedioPonderadoClase
            sumaPorcentajesEvaluados += porcentajeEvaluadoClase
        }

        val promedioGeneral = if (clases.isNotEmpty()) sumaPromedios / clases.size else 0.0
        val porcentajeEvaluadoGlobal = if (clases.isNotEmpty()) sumaPorcentajesEvaluados / clases.size else 0.0

        val atRiskClass = classSummaries.firstOrNull { it.promedio < 3.0 && it.porcentajeEvaluado > 0 }
        val alertMessage = when {
            atRiskClass != null -> "Atención: Tienes rendimiento bajo en ${atRiskClass.nombreClase} (${String.format(java.util.Locale.getDefault(), "%.2f", atRiskClass.promedio)})."
            upcomingActivities.isNotEmpty() -> "Tienes ${upcomingActivities.size} actividades pendientes por entregar."
            else -> null
        }

        StudentDashboardData(
            studentName = studentName,
            promedioGeneral = promedioGeneral,
            porcentajeEvaluadoGlobal = porcentajeEvaluadoGlobal,
            classes = classSummaries,
            upcomingActivities = upcomingActivities,
            alertMessage = alertMessage
        )
    }

    suspend fun getStudentClassDetail(studentId: Long, claseId: Long): StudentClassDetailData? = withContext(Dispatchers.IO) {
        val clase = db.claseDao().getClaseById(claseId) ?: return@withContext null
        val teacher = db.userDao().getUserById(clase.teacherId)
        val teacherName = teacher?.nombre ?: "Profesor"

        val actividades = db.actividadDao().getActividadesByClase(claseId)

        var promedioPonderadoClase = 0.0
        var porcentajeEvaluadoClase = 0.0

        val timelineMap = mutableMapOf<String, MutableList<TimelineListItem.ActivityItem>>()

        for (act in actividades) {
            val entrega = db.entregaDao().getEntrega(act.id, studentId)
            val calificacion = if (entrega != null) db.calificacionDao().getCalificacionByEntrega(entrega.id) else null

            var estado = EntregaEntity.STATUS_PENDING
            var notaVal: Double? = null
            var retro: String? = null

            if (calificacion != null) {
                promedioPonderadoClase += calificacion.nota * (act.porcentaje / 100.0)
                porcentajeEvaluadoClase += act.porcentaje
                estado = EntregaEntity.STATUS_GRADED
                notaVal = calificacion.nota
                retro = calificacion.retroalimentacion
            } else if (entrega != null) {
                estado = entrega.estado
            }

            val item = TimelineListItem.ActivityItem(
                actividadId = act.id,
                titulo = act.titulo,
                descripcion = act.descripcion,
                tipo = act.tipo,
                porcentaje = act.porcentaje,
                fechaEntrega = act.fechaEntrega,
                estado = estado,
                nota = notaVal,
                retroalimentacion = retro
            )

            val month = act.mesTimeline.ifEmpty { "GENERAL" }
            timelineMap.getOrPut(month) { mutableListOf() }.add(item)
        }

        val timelineList = mutableListOf<TimelineListItem>()
        for ((month, items) in timelineMap) {
            timelineList.add(TimelineListItem.MonthHeader(monthName = month))
            timelineList.addAll(items)
        }

        StudentClassDetailData(
            claseId = clase.id,
            nombreClase = clase.nombreClase,
            codigoClase = clase.codigoClase,
            profesor = teacherName,
            promedioActual = promedioPonderadoClase,
            porcentajeEvaluado = porcentajeEvaluadoClase,
            colorTheme = clase.colorTheme,
            timelineItems = timelineList
        )
    }

    suspend fun getStudentActivitiesList(studentId: Long): List<StudentActivityItemModel> = withContext(Dispatchers.IO) {
        val clases = db.claseDao().getClasesByStudent(studentId)
        val resultList = mutableListOf<StudentActivityItemModel>()

        for (clase in clases) {
            val actividades = db.actividadDao().getActividadesByClase(clase.id)
            for (act in actividades) {
                val entrega = db.entregaDao().getEntrega(act.id, studentId)
                val calificacion = if (entrega != null) db.calificacionDao().getCalificacionByEntrega(entrega.id) else null

                val estado = calificacion?.let { EntregaEntity.STATUS_GRADED } ?: (entrega?.estado ?: EntregaEntity.STATUS_PENDING)

                resultList.add(
                    StudentActivityItemModel(
                        actividadId = act.id,
                        claseId = clase.id,
                        nombreClase = clase.nombreClase,
                        titulo = act.titulo,
                        descripcion = act.descripcion,
                        tipo = act.tipo,
                        porcentaje = act.porcentaje,
                        fechaEntrega = act.fechaEntrega,
                        estado = estado,
                        nota = calificacion?.nota,
                        retroalimentacion = calificacion?.retroalimentacion,
                        contenidoRespuesta = entrega?.contenidoRespuesta
                    )
                )
            }
        }
        resultList
    }

    suspend fun submitTask(studentId: Long, actividadId: Long, respuestaTexto: String) = withContext(Dispatchers.IO) {
        val todayStr = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date())
        val existingEntrega = db.entregaDao().getEntrega(actividadId, studentId)

        if (existingEntrega == null) {
            db.entregaDao().insertEntrega(
                EntregaEntity(
                    actividadId = actividadId,
                    studentId = studentId,
                    contenidoRespuesta = respuestaTexto,
                    fechaEntrega = todayStr,
                    estado = EntregaEntity.STATUS_SUBMITTED
                )
            )
        } else {
            db.entregaDao().insertEntrega(
                existingEntrega.copy(
                    contenidoRespuesta = respuestaTexto,
                    fechaEntrega = todayStr,
                    estado = EntregaEntity.STATUS_SUBMITTED
                )
            )
        }
    }

    suspend fun submitQuiz(studentId: Long, actividadId: Long, respuestasSeleccionadas: Map<Long, Int>): Double = withContext(Dispatchers.IO) {
        val todayStr = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date())
        val preguntas = db.preguntaQuizDao().getPreguntasByActividad(actividadId)

        var correctCount = 0
        for (p in preguntas) {
            val selected = respuestasSeleccionadas[p.id]
            if (selected != null && selected == p.opcionCorrecta) {
                correctCount++
            }
        }

        val calculatedGrade = if (preguntas.isNotEmpty()) {
            (correctCount.toDouble() / preguntas.size) * 5.0
        } else {
            5.0
        }

        var entrega = db.entregaDao().getEntrega(actividadId, studentId)
        if (entrega == null) {
            db.entregaDao().insertEntrega(
                EntregaEntity(
                    actividadId = actividadId,
                    studentId = studentId,
                    contenidoRespuesta = "Quiz completado ($correctCount de ${preguntas.size} correctas)",
                    fechaEntrega = todayStr,
                    estado = EntregaEntity.STATUS_GRADED
                )
            )
            entrega = db.entregaDao().getEntrega(actividadId, studentId)
        }

        if (entrega != null) {
            val retroText = "Autocorregido: $correctCount de ${preguntas.size} respuestas correctas."
            db.calificacionDao().insertCalificacion(
                CalificacionEntity(
                    entregaId = entrega.id,
                    nota = calculatedGrade,
                    retroalimentacion = retroText,
                    fechaCalificacion = todayStr
                )
            )

            for ((preguntaId, opcionSel) in respuestasSeleccionadas) {
                val pregunta = preguntas.firstOrNull { it.id == preguntaId }
                val isCorrect = pregunta != null && pregunta.opcionCorrecta == opcionSel
                db.respuestaQuizDao().insertRespuesta(
                    RespuestaQuizEntity(
                        entregaId = entrega.id,
                        preguntaId = preguntaId,
                        respuestaSeleccionada = opcionSel,
                        esCorrecta = isCorrect
                    )
                )
            }
        }

        calculatedGrade
    }

    suspend fun getQuestionsForQuiz(actividadId: Long): List<PreguntaQuizEntity> = withContext(Dispatchers.IO) {
        db.preguntaQuizDao().getPreguntasByActividad(actividadId)
    }
}
