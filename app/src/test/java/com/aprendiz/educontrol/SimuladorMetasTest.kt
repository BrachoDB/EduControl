package com.aprendiz.educontrol

import org.junit.Assert.assertEquals
import org.junit.Test

class SimuladorMetasTest {

    private fun calcularNotaNecesaria(
        promedioAcumulado: Double,
        porcentajeAcumulado: Double,
        notaObjetivo: Double
    ): Double? {
        val porcentajeRestante = 100.0 - porcentajeAcumulado
        if (porcentajeRestante <= 0.001) return null
        
        val puntosNecesarios = notaObjetivo - promedioAcumulado
        if (puntosNecesarios <= 0) return 0.0
        
        return (puntosNecesarios * 100.0) / porcentajeRestante
    }

    @Test
    fun testCalculoNotaNecesariaCorrecto() {
        val resultado = calcularNotaNecesaria(
            promedioAcumulado = 2.4,
            porcentajeAcumulado = 60.0,
            notaObjetivo = 4.0
        )
        assertEquals(4.0, resultado!!, 0.01)
    }

    @Test
    fun testCalculoMetaYaAlcanzada() {
        val resultado = calcularNotaNecesaria(
            promedioAcumulado = 3.5,
            porcentajeAcumulado = 70.0,
            notaObjetivo = 3.0
        )
        assertEquals(0.0, resultado!!, 0.01)
    }

    @Test
    fun testCalculoMetaImposible() {
        val resultado = calcularNotaNecesaria(
            promedioAcumulado = 1.0,
            porcentajeAcumulado = 80.0,
            notaObjetivo = 4.5
        )
        assertEquals(17.5, resultado!!, 0.01)
    }

    @Test
    fun testMateriaCerrada() {
        val resultado = calcularNotaNecesaria(
            promedioAcumulado = 4.2,
            porcentajeAcumulado = 100.0,
            notaObjetivo = 4.5
        )
        assertEquals(null, resultado)
    }
}
