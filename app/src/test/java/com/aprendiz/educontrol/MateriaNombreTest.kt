package com.aprendiz.educontrol

import com.aprendiz.educontrol.data.entity.MateriaEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class MateriaNombreTest {

    @Test
    fun testNombreMateriaConEneYAcentos() {
        val nombreConEne = "Diseño de Sistemas"
        val profesorConEne = "Prof. Nuñez"

        val materia = MateriaEntity(
            id = 1L,
            userId = 100L,
            nombreMateria = nombreConEne,
            profesor = profesorConEne
        )

        assertEquals("Diseño de Sistemas", materia.nombreMateria)
        assertEquals("Prof. Nuñez", materia.profesor)
    }

    @Test
    fun testNombreMateriaEspanol() {
        val materia = MateriaEntity(
            id = 2L,
            userId = 100L,
            nombreMateria = "Español y Literatura",
            profesor = "Doña Berta"
        )

        assertEquals("Español y Literatura", materia.nombreMateria)
        assertEquals("Doña Berta", materia.profesor)
    }
}
