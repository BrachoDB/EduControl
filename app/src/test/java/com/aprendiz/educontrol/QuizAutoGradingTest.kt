package com.aprendiz.educontrol

import org.junit.Assert.assertEquals
import org.junit.Test

class QuizAutoGradingTest {

    private fun calcularNotaQuiz(respuestasCorrectas: Int, totalPreguntas: Int): Double {
        if (totalPreguntas == 0) return 5.0
        return (respuestasCorrectas.toDouble() / totalPreguntas) * 5.0
    }

    @Test
    fun testQuizPerfecto() {
        val nota = calcularNotaQuiz(2, 2)
        assertEquals(5.0, nota, 0.01)
    }

    @Test
    fun testQuizMitad() {
        val nota = calcularNotaQuiz(1, 2)
        assertEquals(2.5, nota, 0.01)
    }

    @Test
    fun testQuizCero() {
        val nota = calcularNotaQuiz(0, 2)
        assertEquals(0.0, nota, 0.01)
    }
}
