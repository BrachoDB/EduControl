package com.aprendiz.educontrol.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aprendiz.educontrol.data.entity.ActividadEntity

@Dao
interface ActividadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActividad(actividad: ActividadEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActividades(actividades: List<ActividadEntity>)

    @Query("SELECT * FROM actividades WHERE id = :id LIMIT 1")
    suspend fun getActividadById(id: Long): ActividadEntity?

    @Query("SELECT * FROM actividades WHERE claseId = :claseId")
    suspend fun getActividadesByClase(claseId: Long): List<ActividadEntity>

    @Query("SELECT SUM(porcentaje) FROM actividades WHERE claseId = :claseId")
    suspend fun getSumPercentageByClase(claseId: Long): Double?
}
