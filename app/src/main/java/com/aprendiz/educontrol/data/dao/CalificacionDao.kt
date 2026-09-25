package com.aprendiz.educontrol.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aprendiz.educontrol.data.entity.CalificacionEntity

@Dao
interface CalificacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalificacion(calificacion: CalificacionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalificaciones(calificaciones: List<CalificacionEntity>)

    @Query("SELECT * FROM calificaciones WHERE entregaId = :entregaId LIMIT 1")
    suspend fun getCalificacionByEntrega(entregaId: Long): CalificacionEntity?
}
