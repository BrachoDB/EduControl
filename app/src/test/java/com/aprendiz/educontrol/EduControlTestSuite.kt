package com.aprendiz.educontrol

import com.aprendiz.educontrol.domain.calculator.GoalStatus
import com.aprendiz.educontrol.domain.calculator.ProjectionCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class EduControlTestSuite {

    @Test
    fun testCalculoPromedioPonderadoSantiago() {
        // Santiago: 4.8 (10%), 4.5 (10%), 4.2 (15%), 4.5 (25%), 4.0 (15%) = Total 75%
        val notasYPorcentajes = listOf(
            4.8 to 10.0,
            4.5 to 10.0,
            4.2 to 15.0,
            4.5 to 25.0,
            4.0 to 15.0
        )

        var sumaPonderada = 0.0
        var sumaPorcentajes = 0.0

        for ((nota, porcentaje) in notasYPorcentajes) {
            sumaPonderada += nota * (porcentaje / 100.0)
            sumaPorcentajes += porcentaje
        }

        assertEquals(3.285, sumaPonderada, 0.001)
        assertEquals(75.0, sumaPorcentajes, 0.001)

        // Promedio acumulado equivalente
        val promedioAcumulado = (sumaPonderada / (sumaPorcentajes / 100.0))
        assertEquals(4.38, promedioAcumulado, 0.01)
    }

    @Test
    fun testCalculoPromedioPonderadoCarlos() {
        // Carlos: 3.0 (10%), 2.0 (10%), 2.5 (15%), 2.8 (25%) = Total 60%
        val notasYPorcentajes = listOf(
            3.0 to 10.0,
            2.0 to 10.0,
            2.5 to 15.0,
            2.8 to 25.0
        )

        var sumaPonderada = 0.0
        var sumaPorcentajes = 0.0

        for ((nota, porcentaje) in notasYPorcentajes) {
            sumaPonderada += nota * (porcentaje / 100.0)
            sumaPorcentajes += porcentaje
        }

        assertEquals(1.575, sumaPonderada, 0.001)
        assertEquals(60.0, sumaPorcentajes, 0.001)

        val promedioAcumulado = (sumaPonderada / (sumaPorcentajes / 100.0))
        assertEquals(2.625, promedioAcumulado, 0.01)
    }

    @Test
    fun testProyeccionSantiagoMetaAlcanzada() {
        // Santiago lleva 3.285 puntos acumulados sobre 5.0. Meta 3.0 -> Alcanzada!
        val projection = ProjectionCalculator.calculateProjection(
            promedioAcumulado = 3.285,
            porcentajeEvaluado = 75.0,
            notaObjetivo = 3.0
        )
        assertEquals(GoalStatus.ALCANZADA, projection.estado)
        assertEquals(0.0, projection.notaRequerida!!, 0.001)
    }

    @Test
    fun testProyeccionValentinaMetaExigente() {
        // Valentina: Puntos 2.03 en 60% evaluado. Meta 4.0
        val projection = ProjectionCalculator.calculateProjection(
            promedioAcumulado = 2.03,
            porcentajeEvaluado = 60.0,
            notaObjetivo = 4.0
        )
        assertEquals(GoalStatus.EXIGENTE, projection.estado)
        assertEquals(4.925, projection.notaRequerida!!, 0.01)
    }

    @Test
    fun testProyeccionMateoMetaImposible() {
        // Mateo: Puntos 1.965 en 75% evaluado. Meta 4.5
        val projection = ProjectionCalculator.calculateProjection(
            promedioAcumulado = 1.965,
            porcentajeEvaluado = 75.0,
            notaObjetivo = 4.5
        )
        assertEquals(GoalStatus.NO_ALCANZABLE, projection.estado)
        assertEquals(10.14, projection.notaRequerida!!, 0.01)
        assertEquals(3.215, projection.notaFinalMaximaPosible, 0.01)
    }

    @Test
    fun testQuizAutoGradingFormula() {
        // 2 de 2 correctas
        val scorePerfect = (2.0 / 2) * 5.0
        assertEquals(5.0, scorePerfect, 0.001)

        // 1 de 2 correctas
        val scoreHalf = (1.0 / 2) * 5.0
        assertEquals(2.5, scoreHalf, 0.001)

        // 0 de 2 correctas
        val scoreZero = (0.0 / 2) * 5.0
        assertEquals(0.0, scoreZero, 0.001)
    }
}
