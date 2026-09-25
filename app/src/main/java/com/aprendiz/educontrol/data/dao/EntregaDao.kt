package com.aprendiz.educontrol.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aprendiz.educontrol.data.entity.EntregaEntity

@Dao
interface EntregaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntrega(entrega: EntregaEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntregas(entregas: List<EntregaEntity>)

    @Query("SELECT * FROM entregas WHERE actividadId = :actividadId AND studentId = :studentId LIMIT 1")
    suspend fun getEntrega(actividadId: Long, studentId: Long): EntregaEntity?

    @Query("SELECT * FROM entregas WHERE studentId = :studentId")
    suspend fun getEntregasByStudent(studentId: Long): List<EntregaEntity>

    @Query("SELECT * FROM entregas WHERE actividadId = :actividadId")
    suspend fun getEntregasByActividad(actividadId: Long): List<EntregaEntity>
}
