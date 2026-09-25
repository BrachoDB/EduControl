package com.aprendiz.educontrol.domain.calculator

import java.util.Locale

enum class GoalStatus {
    ALCANZADA,
    ALCANZABLE,
    EXIGENTE,
    NO_ALCANZABLE,
    CURSO_CERRADO
}

data class ProjectionResult(
    val promedioActual: Double,
    val porcentajeEvaluado: Double,
    val porcentajeRestante: Double,
    val notaObjetivo: Double,
    val notaRequerida: Double?,
    val notaFinalEstimadaTendencia: Double,
    val notaFinalMaximaPosible: Double,
    val estado: GoalStatus,
    val mensajeExplicativo: String
)

object ProjectionCalculator {

    fun calculateProjection(
        promedioAcumulado: Double,
        porcentajeEvaluado: Double,
        notaObjetivo: Double
    ): ProjectionResult {
        val porcentajeRestante = (100.0 - porcentajeEvaluado).coerceAtLeast(0.0)

        if (porcentajeRestante <= 0.001) {
            val notaFormatted = String.format(Locale.getDefault(), "%.2f", promedioAcumulado)
            return ProjectionResult(
                promedioActual = promedioAcumulado,
                porcentajeEvaluado = porcentajeEvaluado,
                porcentajeRestante = 0.0,
                notaObjetivo = notaObjetivo,
                notaRequerida = null,
                notaFinalEstimadaTendencia = promedioAcumulado,
                notaFinalMaximaPosible = promedioAcumulado,
                estado = GoalStatus.CURSO_CERRADO,
                mensajeExplicativo = "Materia 100% evaluada. Nota final cerrada en $notaFormatted."
            )
        }

        val puntosNecesarios = notaObjetivo - promedioAcumulado
        val promedioPonderadoActual = if (porcentajeEvaluado > 0) (promedioAcumulado / porcentajeEvaluado) * 100.0 else 0.0
        val notaFinalEstimadaTendencia = (promedioAcumulado + (promedioPonderadoActual * (porcentajeRestante / 100.0))).coerceIn(0.0, 5.0)
        val notaFinalMaximaPosible = (promedioAcumulado + 5.0 * (porcentajeRestante / 100.0)).coerceIn(0.0, 5.0)

        val porcentajeRestanteStr = String.format(Locale.getDefault(), "%.0f", porcentajeRestante)

        if (puntosNecesarios <= 0) {
            val acumuladoStr = String.format(Locale.getDefault(), "%.2f", promedioAcumulado)
            val objetivoStr = String.format(Locale.getDefault(), "%.2f", notaObjetivo)
            return ProjectionResult(
                promedioActual = promedioAcumulado,
                porcentajeEvaluado = porcentajeEvaluado,
                porcentajeRestante = porcentajeRestante,
                notaObjetivo = notaObjetivo,
                notaRequerida = 0.0,
                notaFinalEstimadaTendencia = notaFinalEstimadaTendencia,
                notaFinalMaximaPosible = notaFinalMaximaPosible,
                estado = GoalStatus.ALCANZADA,
                mensajeExplicativo = "¡Meta alcanzada! Llevas $acumuladoStr acumulado, suficiente para asegurar $objetivoStr."
            )
        }

        val notaRequerida = (puntosNecesarios * 100.0) / porcentajeRestante
        val notaRequeridaStr = String.format(Locale.getDefault(), "%.2f", notaRequerida)
        val notaMaximaStr = String.format(Locale.getDefault(), "%.2f", notaFinalMaximaPosible)

        return when {
            notaRequerida <= 4.4 -> {
                ProjectionResult(
                    promedioActual = promedioAcumulado,
                    porcentajeEvaluado = porcentajeEvaluado,
                    porcentajeRestante = porcentajeRestante,
                    notaObjetivo = notaObjetivo,
                    notaRequerida = notaRequerida,
                    notaFinalEstimadaTendencia = notaFinalEstimadaTendencia,
                    notaFinalMaximaPosible = notaFinalMaximaPosible,
                    estado = GoalStatus.ALCANZABLE,
                    mensajeExplicativo = "Meta alcanzable: Necesitas obtener $notaRequeridaStr en el $porcentajeRestanteStr% restante."
                )
            }
            notaRequerida <= 5.0 -> {
                ProjectionResult(
                    promedioActual = promedioAcumulado,
                    porcentajeEvaluado = porcentajeEvaluado,
                    porcentajeRestante = porcentajeRestante,
                    notaObjetivo = notaObjetivo,
                    notaRequerida = notaRequerida,
                    notaFinalEstimadaTendencia = notaFinalEstimadaTendencia,
                    notaFinalMaximaPosible = notaFinalMaximaPosible,
                    estado = GoalStatus.EXIGENTE,
                    mensajeExplicativo = "Meta exigente: Requerirías un $notaRequeridaStr en el $porcentajeRestanteStr% restante."
                )
            }
            else -> {
                ProjectionResult(
                    promedioActual = promedioAcumulado,
                    porcentajeEvaluado = porcentajeEvaluado,
                    porcentajeRestante = porcentajeRestante,
                    notaObjetivo = notaObjetivo,
                    notaRequerida = notaRequerida,
                    notaFinalEstimadaTendencia = notaFinalEstimadaTendencia,
                    notaFinalMaximaPosible = notaFinalMaximaPosible,
                    estado = GoalStatus.NO_ALCANZABLE,
                    mensajeExplicativo = "Meta inalcanzable matemáticamente: Requerirías $notaRequeridaStr (supera escala 5.0). Nota máxima posible: $notaMaximaStr."
                )
            }
        }
    }
}
