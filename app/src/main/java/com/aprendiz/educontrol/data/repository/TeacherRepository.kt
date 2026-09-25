package com.aprendiz.educontrol.data.repository

import android.content.Context
import com.aprendiz.educontrol.data.AppDatabase
import com.aprendiz.educontrol.data.entity.EntregaEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
}
