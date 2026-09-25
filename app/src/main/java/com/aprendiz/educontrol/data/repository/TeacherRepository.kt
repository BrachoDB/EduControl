package com.aprendiz.educontrol.data.repository

import android.content.Context
import com.aprendiz.educontrol.data.AppDatabase
import com.aprendiz.educontrol.data.entity.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class TeacherClassSummaryModel(
    val claseId: Long,
    val nombreClase: String,
    val codigoClase: String,
    val studentCount: Int,
    val promedioCurso: Double,
    val porcentajeEvaluado: Double,
    val colorTheme: String
)

data class TeacherDashboardData(
    val teacherName: String,
    val totalClases: Int,
    val totalEstudiantes: Int,
    val totalActividades: Int,
    val pendingGradingCount: Int,
    val clases: List<TeacherClassSummaryModel>
)

data class TeacherStudentSummaryModel(
    val studentId: Long,
    val nombre: String,
    val email: String,
    val avatarEmoji: String,
    val promedioClase: Double
)

data class TeacherActivityGradeModel(
    val studentId: Long,
    val studentName: String,
    val avatarEmoji: String,
    val actividadId: Long,
    val entregaId: Long?,
    val nota: Double?,
    val retroalimentacion: String?,
    val estadoEntrega: String
)

data class TeacherClassDetailData(
    val claseId: Long,
    val nombreClase: String,
    val codigoClase: String,
    val profesor: String,
    val studentCount: Int,
    val promedioCurso: Double,
    val porcentajeEvaluado: Double,
    val students: List<TeacherStudentSummaryModel>,
    val actividades: List<ActividadEntity>
)

class TeacherRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)

    suspend fun getTeacherDashboardData(teacherId: Long): TeacherDashboardData = withContext(Dispatchers.IO) {
        val teacher = db.userDao().getUserById(teacherId)
        val teacherName = teacher?.nombre ?: "Profesor"

        val clases = db.claseDao().getClasesByTeacher(teacherId)
        val classSummaries = mutableListOf<TeacherClassSummaryModel>()

        val uniqueStudentIds = mutableSetOf<Long>()
        var totalActividadesCount = 0
        var pendingGradingCount = 0

        for (clase in clases) {
            val students = db.inscripcionDao().getStudentsByClase(clase.id)
            uniqueStudentIds.addAll(students.map { it.id })

            val actividades = db.actividadDao().getActividadesByClase(clase.id)
            totalActividadesCount += actividades.size

            var sumaPromediosEstudiantes = 0.0
            var porcentajeEvaluadoCurso = 0.0

            if (students.isNotEmpty()) {
                for (student in students) {
                    var promedioEstudianteEnClase = 0.0
                    for (act in actividades) {
                        val entrega = db.entregaDao().getEntrega(act.id, student.id)
                        if (entrega != null) {
                            val calificacion = db.calificacionDao().getCalificacionByEntrega(entrega.id)
                            if (calificacion != null) {
                                promedioEstudianteEnClase += calificacion.nota * (act.porcentaje / 100.0)
                            } else if (entrega.estado == EntregaEntity.STATUS_SUBMITTED) {
                                pendingGradingCount++
                            }
                        }
                    }
                    sumaPromediosEstudiantes += promedioEstudianteEnClase
                }

                for (act in actividades) {
                    val anyGraded = students.any { student ->
                        val entrega = db.entregaDao().getEntrega(act.id, student.id)
                        entrega != null && db.calificacionDao().getCalificacionByEntrega(entrega.id) != null
                    }
                    if (anyGraded) {
                        porcentajeEvaluadoCurso += act.porcentaje
                    }
                }
            }

            val promedioCurso = if (students.isNotEmpty()) sumaPromediosEstudiantes / students.size else 0.0

            classSummaries.add(
                TeacherClassSummaryModel(
                    claseId = clase.id,
                    nombreClase = clase.nombreClase,
                    codigoClase = clase.codigoClase,
                    studentCount = students.size,
                    promedioCurso = promedioCurso,
                    porcentajeEvaluado = porcentajeEvaluadoCurso,
                    colorTheme = clase.colorTheme
                )
            )
        }

        TeacherDashboardData(
            teacherName = teacherName,
            totalClases = clases.size,
            totalEstudiantes = uniqueStudentIds.size,
            totalActividades = totalActividadesCount,
            pendingGradingCount = pendingGradingCount,
            clases = classSummaries
        )
    }

    suspend fun getTeacherClassDetail(claseId: Long): TeacherClassDetailData? = withContext(Dispatchers.IO) {
        val clase = db.claseDao().getClaseById(claseId) ?: return@withContext null
        val teacher = db.userDao().getUserById(clase.teacherId)
        val teacherName = teacher?.nombre ?: "Profesor"

        val students = db.inscripcionDao().getStudentsByClase(claseId)
        val actividades = db.actividadDao().getActividadesByClase(claseId)

        val studentSummaries = mutableListOf<TeacherStudentSummaryModel>()
        var sumaPromediosEstudiantes = 0.0

        for (student in students) {
            var promedioEstudiante = 0.0
            for (act in actividades) {
                val entrega = db.entregaDao().getEntrega(act.id, student.id)
                if (entrega != null) {
                    val calificacion = db.calificacionDao().getCalificacionByEntrega(entrega.id)
                    if (calificacion != null) {
                        promedioEstudiante += calificacion.nota * (act.porcentaje / 100.0)
                    }
                }
            }
            studentSummaries.add(
                TeacherStudentSummaryModel(
                    studentId = student.id,
                    nombre = student.nombre,
                    email = student.email,
                    avatarEmoji = student.avatarEmoji,
                    promedioClase = promedioEstudiante
                )
            )
            sumaPromediosEstudiantes += promedioEstudiante
        }

        var porcentajeEvaluadoCurso = 0.0
        for (act in actividades) {
            val anyGraded = students.any { student ->
                val entrega = db.entregaDao().getEntrega(act.id, student.id)
                entrega != null && db.calificacionDao().getCalificacionByEntrega(entrega.id) != null
            }
            if (anyGraded) {
                porcentajeEvaluadoCurso += act.porcentaje
            }
        }

        val promedioCurso = if (students.isNotEmpty()) sumaPromediosEstudiantes / students.size else 0.0

        TeacherClassDetailData(
            claseId = clase.id,
            nombreClase = clase.nombreClase,
            codigoClase = clase.codigoClase,
            profesor = teacherName,
            studentCount = students.size,
            promedioCurso = promedioCurso,
            porcentajeEvaluado = porcentajeEvaluadoCurso,
            students = studentSummaries,
            actividades = actividades
        )
    }

    suspend fun getActivityGradesForClass(claseId: Long, actividadId: Long): List<TeacherActivityGradeModel> = withContext(Dispatchers.IO) {
        val students = db.inscripcionDao().getStudentsByClase(claseId)
        val result = mutableListOf<TeacherActivityGradeModel>()

        for (student in students) {
            val entrega = db.entregaDao().getEntrega(actividadId, student.id)
            val calificacion = if (entrega != null) db.calificacionDao().getCalificacionByEntrega(entrega.id) else null

            result.add(
                TeacherActivityGradeModel(
                    studentId = student.id,
                    studentName = student.nombre,
                    avatarEmoji = student.avatarEmoji,
                    actividadId = actividadId,
                    entregaId = entrega?.id,
                    nota = calificacion?.nota,
                    retroalimentacion = calificacion?.retroalimentacion,
                    estadoEntrega = entrega?.estado ?: EntregaEntity.STATUS_PENDING
                )
            )
        }
        result
    }

    suspend fun saveGrade(actividadId: Long, studentId: Long, nota: Double, retroalimentacion: String) = withContext(Dispatchers.IO) {
        val todayStr = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date())

        var entrega = db.entregaDao().getEntrega(actividadId, studentId)
        if (entrega == null) {
            val newEntregaId = db.entregaDao().insertEntrega(
                EntregaEntity(
                    actividadId = actividadId,
                    studentId = studentId,
                    contenidoRespuesta = "Entrega registrada por profesor",
                    fechaEntrega = todayStr,
                    estado = EntregaEntity.STATUS_GRADED
                )
            )
            entrega = db.entregaDao().getEntrega(actividadId, studentId)
        }

        if (entrega != null) {
            val existingCalif = db.calificacionDao().getCalificacionByEntrega(entrega.id)
            if (existingCalif != null) {
                db.calificacionDao().insertCalificacion(
                    existingCalif.copy(nota = nota, retroalimentacion = retroalimentacion, fechaCalificacion = todayStr)
                )
            } else {
                db.calificacionDao().insertCalificacion(
                    CalificacionEntity(
                        entregaId = entrega.id,
                        nota = nota,
                        retroalimentacion = retroalimentacion,
                        fechaCalificacion = todayStr
                    )
                )
            }
        }
    }
}
