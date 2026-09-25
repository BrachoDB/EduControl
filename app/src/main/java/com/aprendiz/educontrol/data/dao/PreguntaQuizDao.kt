package com.aprendiz.educontrol.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aprendiz.educontrol.data.entity.PreguntaQuizEntity

@Dao
interface PreguntaQuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPregunta(pregunta: PreguntaQuizEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreguntas(preguntas: List<PreguntaQuizEntity>)

    @Query("SELECT * FROM preguntas_quiz WHERE actividadId = :actividadId")
    suspend fun getPreguntasByActividad(actividadId: Long): List<PreguntaQuizEntity>
}
