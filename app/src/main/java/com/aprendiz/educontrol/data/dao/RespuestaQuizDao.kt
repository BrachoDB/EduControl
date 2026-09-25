package com.aprendiz.educontrol.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aprendiz.educontrol.data.entity.RespuestaQuizEntity

@Dao
interface RespuestaQuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRespuesta(respuesta: RespuestaQuizEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRespuestas(respuestas: List<RespuestaQuizEntity>)

    @Query("SELECT * FROM respuestas_quiz WHERE entregaId = :entregaId")
    suspend fun getRespuestasByEntrega(entregaId: Long): List<RespuestaQuizEntity>
}
