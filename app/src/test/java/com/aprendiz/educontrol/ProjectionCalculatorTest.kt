package com.aprendiz.educontrol

import com.aprendiz.educontrol.domain.calculator.GoalStatus
import com.aprendiz.educontrol.domain.calculator.ProjectionCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class ProjectionCalculatorTest {

    @Test
    fun testValentinaMetaAlcanzable() {
        // Valentina: Promedio acumulado 2.03 en 60% evaluado. Meta: 4.0
        // Porcentaje restante: 40%. Puntos necesarios: 4.0 - 2.03 = 1.97
        // Nota requerida: (1.97 * 100) / 40 = 4.925 (Exigente <= 5.0)
        val res = ProjectionCalculator.calculateProjection(
            promedioAcumulado = 2.03,
            porcentajeEvaluado = 60.0,
            notaObjetivo = 4.0
        )
        assertEquals(GoalStatus.EXIGENTE, res.estado)
        assertEquals(4.925, res.notaRequerida!!, 0.01)
    }

    @Test
    fun testMateoMetaImposible() {
        // Mateo: Promedio acumulado 1.965 en 75% evaluado. Meta: 4.5
        // Porcentaje restante: 25%. Puntos necesarios: 4.5 - 1.965 = 2.535
        // Nota requerida: (2.535 * 100) / 25 = 10.14 (> 5.0 Imposible)
        val res = ProjectionCalculator.calculateProjection(
            promedioAcumulado = 1.965,
            porcentajeEvaluado = 75.0,
            notaObjetivo = 4.5
        )
        assertEquals(GoalStatus.NO_ALCANZABLE, res.estado)
        assertEquals(10.14, res.notaRequerida!!, 0.01)
        assertEquals(3.215, res.notaFinalMaximaPosible, 0.01)
    }

    @Test
    fun testCursoCerrado() {
        // 100% evaluado
        val res = ProjectionCalculator.calculateProjection(
            promedioAcumulado = 4.2,
            porcentajeEvaluado = 100.0,
            notaObjetivo = 4.5
        )
        assertEquals(GoalStatus.CURSO_CERRADO, res.estado)
        assertEquals(null, res.notaRequerida)
    }

    @Test
    fun testMetaAlcanzada() {
        // Promedio acumulado 3.5 en 70% evaluado. Meta: 3.0
        val res = ProjectionCalculator.calculateProjection(
            promedioAcumulado = 3.5,
            porcentajeEvaluado = 70.0,
            notaObjetivo = 3.0
        )
        assertEquals(GoalStatus.ALCANZADA, res.estado)
        assertEquals(0.0, res.notaRequerida!!, 0.01)
    }
}
